package wrappers;

import io.github.bonigarcia.wdm.WebDriverManager;
import models.Environment;
import models.EnvironmentConfig;
import models.WindowSize;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.*;
import utils.Reporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class GenericWrappers extends Reporter implements Wrappers {

	// Constants
	private static final int DEFAULT_TIMEOUT_SECONDS = 30;
	private static final int EXPLICIT_WAIT_SECONDS = 10;
	private static final String CONFIG_FILE_PATH = "./src/main/resources/config.properties";
	private static final String OBJECT_FILE_PATH = "src/main/resources/object.properties";
	private static final String SCREENSHOT_PATH = "./target/reports/extent-report/images/";

	protected static final ThreadLocal<GenericWrappers> driverThreadLocal = new ThreadLocal<>();
	private RemoteWebDriver driver;
	private static Properties prop;
	private String url;
	private String hubUrl;
	private String hubPort;
	private static String browser;
	
	// Backward compatibility - deprecated
	@Deprecated
	public static String sBrowser;
	
	public void setDriver(GenericWrappers wrappers) {
		driverThreadLocal.set(wrappers);
	}

	public RemoteWebDriver getDriver() {
		GenericWrappers wrapper = driverThreadLocal.get();
		if (wrapper == null || wrapper.driver == null) {
			throw new IllegalStateException("Driver not initialized. Call invokeApp() first.");
		}
		return wrapper.driver;
	}
	
	/**
	 * Get properties object
	 * @return Properties object
	 */
	public static Properties getProperties() {
		return prop;
	}
	
	/**
	 * Get browser name
	 * @return browser name
	 */
	public static String getBrowser() {
		return browser;
	}

	/**
	 * Cleanup ThreadLocal to prevent memory leaks
	 */
	public static void cleanupThreadLocal() {
		driverThreadLocal.remove();
	}

	public GenericWrappers() {
		Properties localProp = new Properties();
		try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
			localProp.load(fis);
			this.hubUrl = localProp.getProperty("HUB");
			this.hubPort = localProp.getProperty("PORT");
			
			// Environment selection with system property override
			// Priority: 1) System property (-Denv=TEST) 2) Config file 3) Default to PROD
			String envName = System.getProperty("env", 
			                 System.getProperty("environment",
			                 localProp.getProperty("Environment", "PROD")));
			
			Environment environment = Environment.fromString(envName);
			this.url = environment.getBaseUrl();
			
			browser = localProp.getProperty("Browser");
			sBrowser = browser; // Backward compatibility
			
			// Log environment selection for visibility
			System.out.println("🌍 Running tests on: " + environment.toString());
			
		} catch (IOException e) {
			reportStep("Failed to load config.properties: " + e.getMessage(), "FAIL");
			throw new RuntimeException("Configuration file not found", e);
		}
	}

	public void loadObjects() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(OBJECT_FILE_PATH)) {
			prop.load(fis);
		} catch (IOException e) {
			reportStep("Failed to load object.properties: " + e.getMessage(), "FAIL");
		}
	}

	public void unloadObjects() {
		prop = null;
	}

	/*
	 * This method will launch the browser in local machine and maximise the browser and set the
	 * wait for 30 seconds and load the url
	 * @author Baskar
	 * @param url - The url with http or https
	 * @return
	 *
	 **/
	public RemoteWebDriver invokeApp(String browser) {
		return invokeApp(browser,false);
	}

	/**
	 * This method will launch the browser in grid node (if remote) and maximise the browser and set the
	 * wait for 30 seconds and load the url
	 * @author Baskar
	 *
	 **/
	public synchronized RemoteWebDriver invokeApp(String browserName, boolean isRemote) {
		try {
			// Load browser configuration from properties
			Properties localProp = new Properties();
			try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
				localProp.load(fis);
			}
			
			// Read browser configurations with system property overrides
			   // Force headless in CI, otherwise use config/system property
			   boolean isHeadless;
			   if (System.getenv("CI") != null && System.getenv("CI").equalsIgnoreCase("true")) {
				   isHeadless = true;
			   } else {
				   isHeadless = Boolean.parseBoolean(
					   System.getProperty("headless", localProp.getProperty("Browser.Headless", "false"))
				   );
			   }
			
			String windowSizeStr = System.getProperty("windowSize", 
				localProp.getProperty("Browser.WindowSize", "MAXIMIZED")
			);
			
			int pageLoadTimeout = Integer.parseInt(
				System.getProperty("pageLoadTimeout", localProp.getProperty("Browser.PageLoadTimeout", "60"))
			);
			
			int scriptTimeout = Integer.parseInt(
				System.getProperty("scriptTimeout", localProp.getProperty("Browser.ScriptTimeout", "30"))
			);
			
			String userAgent = System.getProperty("userAgent", localProp.getProperty("Browser.UserAgent", ""));
			String downloadDir = System.getProperty("downloadDir", localProp.getProperty("Browser.DownloadDir", ""));
			
			// Parse window size
			WindowSize windowSize = WindowSize.fromString(windowSizeStr);
			if (windowSize == null && windowSizeStr.equals("CUSTOM")) {
				int customWidth = Integer.parseInt(localProp.getProperty("Browser.CustomWidth", "1920"));
				int customHeight = Integer.parseInt(localProp.getProperty("Browser.CustomHeight", "1080"));
				windowSize = new WindowSize(customWidth, customHeight);
			} else if (windowSize == null) {
				windowSize = WindowSize.MAXIMIZED;
			}
			
			System.out.println("🖥️  Browser Config: " + browserName + 
				(isHeadless ? " (headless)" : "") + 
				", Window: " + windowSizeStr);
			
			// Java 21 enhanced switch expression for browser instantiation
			driver = switch (browserName.toLowerCase()) {
				case "chrome" -> {
					ChromeOptions options = new ChromeOptions();
					
					   // Apply headless mode
					   if (isHeadless) {
						   options.addArguments("--headless=new"); // Modern headless mode
					   }
					   // Add no-sandbox only in CI/CD environments
					   if (System.getenv("CI") != null && System.getenv("CI").equalsIgnoreCase("true")) {
						   options.addArguments("--no-sandbox");
					   }
					
					// Apply user agent if provided
					if (userAgent != null && !userAgent.isEmpty()) {
						options.addArguments("--user-agent=" + userAgent);
					}
					
					// Apply download directory if provided
					if (downloadDir != null && !downloadDir.isEmpty()) {
						options.addArguments("--download.default_directory=" + downloadDir);
					}
					
					// Additional common options for stability
					options.addArguments("--disable-blink-features=AutomationControlled");
					options.addArguments("--disable-dev-shm-usage");
					options.addArguments("--remote-allow-origins=*");
					options.addArguments("--disable-gpu"); // Disable GPU for stability
					options.setPageLoadStrategy(PageLoadStrategy.NORMAL); // Ensure proper page load waiting
					
					if (isRemote) {
						yield new RemoteWebDriver(URI.create("http://" + hubUrl + ":" + hubPort + "/wd/hub").toURL(), options);
					} else {
						WebDriverManager.chromedriver().setup();
						yield new ChromeDriver(options);
					}
				}
				case "firefox", "mozilla" -> {
					FirefoxOptions options = new FirefoxOptions();
					
					// Apply headless mode
					if (isHeadless) {
						options.addArguments("-headless");
					}
					
					// Apply user agent if provided
					if (userAgent != null && !userAgent.isEmpty()) {
						options.addPreference("general.useragent.override", userAgent);
					}
					
					// Apply download directory if provided
					if (downloadDir != null && !downloadDir.isEmpty()) {
						options.addPreference("browser.download.dir", downloadDir);
						options.addPreference("browser.download.folderList", 2);
					}
					
					if (isRemote) {
						yield new RemoteWebDriver(URI.create("http://" + hubUrl + ":" + hubPort + "/wd/hub").toURL(), options);
					} else {
						WebDriverManager.firefoxdriver().setup();
						yield new FirefoxDriver(options);
					}
				}
				case "safari" -> {
					SafariOptions options = new SafariOptions();
					// Safari doesn't support headless mode, user agent override, or download directory changes
					if (isHeadless) {
						System.out.println("⚠️  Warning: Safari does not support headless mode. Running in normal mode.");
					}
					
					if (isRemote) {
						yield new RemoteWebDriver(URI.create("http://" + hubUrl + ":" + hubPort + "/wd/hub").toURL(), options);
					} else {
						yield new SafariDriver(options);
					}
				}
				default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
			};
			
			GenericWrappers gw = new GenericWrappers();
			gw.driver = driver;
			setDriver(gw);

			// Apply window size configuration
			// Note: Safari has limited setWindowRect support and doesn't reliably support maximize()
			if (windowSize.equals(WindowSize.MAXIMIZED)) {
				if (browserName.equalsIgnoreCase("safari")) {
					// Safari workaround: Use setSize instead of maximize
					getDriver().manage().window().setSize(new Dimension(1920, 1080));
				} else {
					getDriver().manage().window().maximize();
				}
			} else {
				getDriver().manage().window().setSize(new Dimension(windowSize.width(), windowSize.height()));
			}
			
			// Apply timeout configurations
			getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
			getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
			getDriver().manage().timeouts().scriptTimeout(Duration.ofSeconds(scriptTimeout));
			getDriver().get(url);
			reportStep("Browser " + browserName + " launched successfully", "PASS");

		} catch (Exception e) {
			reportStep("Failed to launch browser " + browserName + ": " + e.getMessage(), "FAIL");
			throw new RuntimeException("Browser launch failed", e);
		}

		return getDriver();
	}

	/**
	 * This method will enter the value to the text field using id attribute to locate
	 * @param data - The data to be sent to the webelement
	 * @author Baskar
	 *
	 */
	//

	public boolean enterByElement(WebElement element, String data) {
		try {
			waitForElementToBeClickable(element);
			element.clear();
			element.sendKeys(data);
			reportStep("Data '" + data + "' entered successfully in element", "PASS");
			return true;
		} catch (NoSuchElementException e) {
			reportStep("Element not found. Could not enter data: " + data, "FAIL");
			return false;
		} catch (Exception e) {
			reportStep("Exception occurred while entering '" + data + "': " + e.getMessage(), "FAIL");
			return false;
		}
	}

	// Keep old method for backward compatibility
	@Deprecated
	public void enterByEle(WebElement ele, String data) {
		enterByElement(ele, data);
	}

	public boolean enterByJavaScript(WebElement element, String data) {
		try {
			((JavascriptExecutor)getDriver()).executeScript("arguments[0].value = arguments[1];", element, data);
			reportStep("Data '" + data + "' entered successfully via JavaScript", "PASS");
			return true;
		} catch (NoSuchElementException e) {
			reportStep("Element not found. Could not enter data: " + data, "FAIL");
			return false;
		} catch (Exception e) {
			reportStep("Exception occurred while entering '" + data + "': " + e.getMessage(), "FAIL");
			return false;
		}
	}

	// Keep old method for backward compatibility
	@Deprecated
	public void enterByCssJsEle(WebElement ele, String data) {
		enterByJavaScript(ele, data);
	}

	/**
	 * This method will verify the title of the browser 
	 * @param title - The expected title of the browser
	 * @author Baskar
	 */
	public boolean verifyTitle(String title){
		boolean bReturn = false;
		try{
			if (getDriver().getTitle().equalsIgnoreCase(title)){
				reportStep("The title of the page matches with the value :"+title, "PASS");
				bReturn = true;
			}else
				reportStep("The title of the page:"+getDriver().getTitle()+" did not match with the value :"+title, "FAIL");

		}catch (Exception e) {
			reportStep("Exception occurred while verifying the title: " + e.getMessage(), "FAIL");
		}
		return bReturn;
	}

    public boolean verifyElementDisplayed(WebElement element){
        try{
            boolean isDisplayed = element.isDisplayed();
            if(isDisplayed) {
                reportStep("Element is displayed", "PASS");
            } else {
                reportStep("Element is not displayed", "FAIL");
            }
            return isDisplayed;
        }catch (Exception e) {
            reportStep("Exception occurred while verifying element: " + e.getMessage(), "FAIL");
            return false;
        }
    }

    // Keep old method for backward compatibility
    @Deprecated
    public void verifyEleDisplayed(WebElement ele){
        verifyElementDisplayed(ele);
    }

	/**
	 * This method will verify the given text matches in the element text
	 * @param xpath - The locator of the object in xpath
//	 * @param text  - The text to be verified
	 * @author Baskar
	 */
	public String verifyTextByXpath(String xpath){
		String sText="";
		try {
			sText = getDriver().findElement(By.xpath(xpath)).getText();
		}catch (Exception e) {
			reportStep("Exception occurred while getting text by xpath: " + e.getMessage(), "FAIL");
		}
		return sText;
	}

	/**
	 * This method will verify the given text is available in the element text
	 * @param xpath - The locator of the object in xpath
	 * @param text  - The text to be verified
	 * @author Baskar
	 */
	public void verifyTextContainsByXpath(String xpath, String text){
		try{
			String sText = getDriver().findElement(By.xpath(xpath)).getText();
			if (sText.contains(text)){
				reportStep("The text: "+sText+" contains the value :"+text, "PASS");
			}else{
				reportStep("The text: "+sText+" did not contain the value :"+text, "FAIL");
			}
		}catch (Exception e) {
			reportStep("Exception occurred while verifying text: " + e.getMessage(), "FAIL");
		}
	}

	/**
	 * This method will verify the given text is available in the element text
	 * @param id - The locator of the object in id
	 * @param text  - The text to be verified
	 * @author Baskar
	 */
	public void verifyTextById(String id, String text) {
		try{
			String sText = getDriver().findElement(By.id(id)).getText();
			if (sText.equalsIgnoreCase(text)){
				reportStep("The text: "+sText+" matches with the value :"+text, "PASS");
			}else{
				reportStep("The text: "+sText+" did not match with the value :"+text, "FAIL");
			}
		}catch (Exception e) {
			reportStep("Exception occurred while verifying text by id: " + e.getMessage(), "FAIL");
		}
	}

    public void verifyTextByEle(WebElement ele, String text) {
        try{
            String sText = ele.getText();
            if (sText.equalsIgnoreCase(text)){
                reportStep("The text: "+sText+" matches with the value :"+text, "PASS");
            }else{
                reportStep("The text: "+sText+" did not match with the value :"+text, "FAIL");
            }
        }catch (Exception e) {
            reportStep("Exception occurred while verifying text by element: " + e.getMessage(), "FAIL");
        }
    }


	/**
	 * This method will verify the given text is available in the element text
	 * @param id - The locator of the object in id
	 * @param text  - The text to be verified
	 * @author Baskar
	 */
	public void verifyTextContainsById(String id, String text) {
		try{
			String sText = getDriver().findElement(By.id(id)).getText();
			if (sText.contains(text)){
				reportStep("The text: "+sText+" contains the value :"+text, "PASS");
			}else{
				reportStep("The text: "+sText+" did not contain the value :"+text, "FAIL");
			}
		}catch (Exception e) {
			reportStep("Exception occurred while verifying text contains: " + e.getMessage(), "FAIL");
		}
	}

	/**
	 * This method will close all the browsers
	 * @author Baskar
	 */
	public void quitBrowser() {
		try {
			if(getDriver() != null) {
				reportStep("Browser closed successfully", "INFO"); // Report as INFO to skip screenshot
				getDriver().quit();
			}
		} catch (Exception e) {
			reportStep("Failed to close browser: " + e.getMessage(), "WARN");
		} finally {
			cleanupThreadLocal();
		}
	}

	/**
	 * This method will click the given element
	 * @param ele - The webelement
	 * @author Baskar
	 */
	public boolean clickByElement(WebElement element) {
		try{
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(EXPLICIT_WAIT_SECONDS));
			// Wait for element to be clickable
			wait.until(ExpectedConditions.elementToBeClickable(element));
			// Wait for element to be enabled
			wait.until(driver -> element.isEnabled());
			element.click();
			reportStep("Element clicked successfully", "PASS");
			return true;
		} catch (org.openqa.selenium.TimeoutException e) {
			reportStep("Timeout waiting for element to be clickable or enabled: " + e.getMessage(), "FAIL");
			return false;
		} catch (WebDriverException e) {
			reportStep("Failed to click element: " + e.getMessage(), "FAIL");
			return false;
		}
	}

	// Keep old method for backward compatibility
	@Deprecated
	public void clickByEle(WebElement ele) {
		clickByElement(ele);
	}

	/**
	 * This method will mouse over on the element using xpath as locator
	 * @param xpathVal  The xpath (locator) of the element to be moused over
	 * @author Baskar
	 */
	public void mouseOverByXpath(String xpathVal) {
		try{
			new Actions(driver).moveToElement(getDriver().findElement(By.xpath(xpathVal))).build().perform();
			reportStep("The mouse over by xpath : "+xpathVal+" is performed.", "PASS");
		} catch (Exception e) {
			reportStep("The mouse over by xpath : "+xpathVal+" could not be performed.", "FAIL");
		}
	}


	 /* This method will mouse over on the element using link name as locator
	 * @param xpathVal  The link name (locator) of the element to be moused over
	 * @author Baskar*/

	public void mouseOverByLinkText(String linkName) {
		try{
			new Actions(driver).moveToElement(getDriver().findElement(By.linkText(linkName))).build().perform();
			reportStep("The mouse over by link : "+linkName+" is performed.", "PASS");
		} catch (Exception e) {
			reportStep("The mouse over by link : "+linkName+" could not be performed.", "FAIL");
		}
	}

	/**
	 * This method will mouse over on the element using WebElement
	 * @param element  The WebElement to be moused over
	 * @author Baskar
	 */
	public void mouseOverByElement(WebElement element) {
		try{
			new Actions(driver).moveToElement(element).build().perform();
			reportStep("The mouse over on element is performed.", "PASS");
		} catch (Exception e) {
			reportStep("The mouse over on element could not be performed.", "WARN");
		}
	}



	/**
	 * This method will return the text of the element using xpath as locator
	 * @param xpathVal  The xpath (locator) of the element
	 * @author Baskar
	 */
	public String getTextByXpath(String xpathVal){
		String bReturn = "";
		try{
			return getDriver().findElement(By.xpath(xpathVal)).getText();
		} catch (Exception e) {
			reportStep("The element with xpath: "+xpathVal+" could not be found.", "FAIL");
		}
		return bReturn; 
	}

	/*
	 * This method will return the text of the element using id as locator
	 * @param xpathVal  The id (locator) of the element
	 * @author Baskar
	 */
	public String getTextById(String idVal) {
		String bReturn = "";
		try{
			return getDriver().findElement(By.id(idVal)).getText();
		} catch (Exception e) {
			reportStep("The element with id: "+idVal+" could not be found.", "FAIL");
		}
		return bReturn; 
	}


	/**
	 * This method will select the drop down value using id as locator
	 * @param id The id (locator) of the drop down element
	 * @param value The value to be selected (visibletext) from the dropdown 
	 * @author Baskar
	 */
	public boolean selectVisibleTextById(String id, String value) {
		try{
			WebElement element = waitForElementPresence(By.id(id));
			new Select(element).selectByVisibleText(value);
			reportStep("Selected value '" + value + "' from dropdown with id: " + id, "PASS");
			return true;
		} catch (Exception e) {
			reportStep("Failed to select value '" + value + "': " + e.getMessage(), "FAIL");
			return false;
		}
	}

	// Keep old method for backward compatibility
	@Deprecated
	public void selectVisibileTextById(String id, String value) {
		selectVisibleTextById(id, value);
	}

    public boolean selectVisibleTextByElement(WebElement element, String value) {
        try{
            waitForElementToBeClickable(element);
            new Select(element).selectByVisibleText(value);
            reportStep("Selected value '" + value + "' from dropdown", "PASS");
            return true;
        } catch (Exception e) {
            reportStep("Failed to select value '" + value + "': " + e.getMessage(), "FAIL");
            return false;
        }
    }

    // Keep old method for backward compatibility
    @Deprecated
    public void selectVisibileTextByEle(String id, String value) {
        selectVisibleTextById(id, value);
    }


	/**
	 * Select dropdown value by visible text and trigger all form events
	 * This ensures JavaScript form validation runs properly
	 * @param element The dropdown WebElement
	 * @param visibleText The text to select
	 * @return true if successful, false otherwise
	 */
	public boolean selectDropdownWithEvents(WebElement element, String visibleText) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) getDriver();
			
			// Find the option by visible text and set it as selected
			String script = 
				"var select = arguments[0];" +
				"var optionText = arguments[1];" +
				"for(var i = 0; i < select.options.length; i++) {" +
				"  if(select.options[i].text === optionText) {" +
				"    select.selectedIndex = i;" +
				"    break;" +
				"  }" +
				"}" +
				"select.dispatchEvent(new Event('input', { bubbles: true }));" +
				"select.dispatchEvent(new Event('change', { bubbles: true }));" +
				"select.dispatchEvent(new Event('blur', { bubbles: true }));";
			
			js.executeScript(script, element, visibleText);
			reportStep("Selected value '" + visibleText + "' from dropdown with events triggered", "PASS");
			return true;
		} catch (Exception e) {
			reportStep("Failed to select dropdown value '" + visibleText + "': " + e.getMessage(), "FAIL");
			return false;
		}
	}

	public void selectVisibileTextByXPath(String xpath, String value) {
		try{
			new Select(getDriver().findElement(By.xpath(xpath))).selectByVisibleText(value);
			reportStep("The element with xpath: "+xpath+" is selected with value :"+value, "PASS");
		} catch (Exception e) {
			reportStep("The value: "+value+" could not be selected.", "FAIL");
		}
	}

	/**
	 * Wait for element to be enabled (checks disabled property via JavaScript)
	 * @param element The WebElement to wait for
	 * @param timeoutSeconds Maximum seconds to wait
	 * @return true if enabled, false if timeout
	 */
	public boolean waitForElementEnabled(WebElement element, int timeoutSeconds) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) getDriver();
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds));
			wait.until(driver -> {
				Boolean isEnabled = (Boolean) js.executeScript("return arguments[0].disabled === false;", element);
				return isEnabled;
			});
			reportStep("Element became enabled", "PASS");
			return true;
		} catch (TimeoutException e) {
			reportStep("Timeout waiting for element to be enabled after " + timeoutSeconds + " seconds", "FAIL");
			return false;
		}
	}

	public void selectIndexById(String id, String value) {
		try{
			new Select(getDriver().findElement(By.id(id))).selectByIndex(Integer.parseInt(value));
			reportStep("The element with id: "+id+" is selected with index :"+value, "PASS");
		} catch (Exception e) {
			reportStep("The index: "+value+" could not be selected.", "FAIL");
		}
	}

	public void switchToParentWindow() {
		try {
			Set<String> winHandles = getDriver().getWindowHandles();
			for (String wHandle : winHandles) {
				getDriver().switchTo().window(wHandle);
				break;
			}
		} catch (Exception e) {
			reportStep("The window could not be switched to the first window.", "FAIL");
		}
	}

	public void switchToLastWindow() {
		try {
			Set<String> winHandles = getDriver().getWindowHandles();
			for (String wHandle : winHandles) {
				getDriver().switchTo().window(wHandle);
			}
		} catch (Exception e) {
			reportStep("The window could not be switched to the last window.", "FAIL");
		}
	}

	public void acceptAlert() {
		try {
			getDriver().switchTo().alert().accept();
		} catch (NoAlertPresentException e) {
			reportStep("The alert could not be found.", "FAIL");
		} catch (Exception e) {
			reportStep("The alert could not be accepted.", "FAIL");
		}

	}

	public boolean switchToIFrameByXpath(String iFrameXpath) {
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(iFrameXpath)));
			reportStep("Switched to iframe successfully", "PASS");
			return true;
		} catch (Exception e) {
			reportStep("Failed to switch to iframe: " + e.getMessage(), "FAIL");
			return false;
		}
	}

	// Keep old method for backward compatibility
	@Deprecated
	public void switchToiFrameByXpath(String iFrameXpath) {
		switchToIFrameByXpath(iFrameXpath);
	}

	public String getAlertText() {		
		String text = null;
		try {
			text = getDriver().switchTo().alert().getText();
		} catch (NoAlertPresentException e) {
			reportStep("The alert could not be found.", "FAIL");
		} catch (Exception e) {
			reportStep("The alert text could not be retrieved.", "FAIL");
		}
		return text;

	}

	public void dismissAlert() {
		try {
			getDriver().switchTo().alert().dismiss();
		} catch (NoAlertPresentException e) {
			reportStep("The alert could not be found.", "FAIL");
		} catch (Exception e) {
			reportStep("The alert could not be accepted.", "FAIL");
		}

	}

	/**
	 * Dismiss feedback popup if present based on visible text
	 * Provides a fallback mechanism to handle feedback overlays without breaking test flow
	 * Uses a 5-second timeout to avoid impacting test performance
	 * @param expectedText Text to check for (e.g., "feedback", "How would you rate")
	 * @return true if popup was dismissed, false if not found
	 * @author Baskar
	 */
	public boolean dismissFeedbackPopupIfPresent(String expectedText) {
		long startTime = System.currentTimeMillis();
		long timeout = 5000; // 5 seconds timeout
		
		try {
			// Check if any element contains the expected text (case-insensitive) with timeout
			List<WebElement> elements = getDriver().findElements(By.xpath(
				"//*[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + 
				expectedText.toLowerCase() + "')]"
			));
			
			if (!elements.isEmpty()) {
				// Check timeout before proceeding
				if (System.currentTimeMillis() - startTime > timeout) {
					return false;
				}
				
				// Find the first visible element with the text
				WebElement visibleElement = null;
				for (WebElement element : elements) {
					try {
						if (element.isDisplayed()) {
							visibleElement = element;
							break;
						}
					} catch (Exception ignored) {
						// Element may be stale, continue checking others
					}
					// Check timeout
					if (System.currentTimeMillis() - startTime > timeout) {
						return false;
					}
				}
				
				if (visibleElement != null) {
					// Try to find and click close button using multiple strategies
					List<By> closeButtonSelectors = List.of(
						// Aria labels
						By.xpath("//button[contains(translate(@aria-label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'close')]"),
						By.xpath("//button[contains(translate(@aria-label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'dismiss')]"),
						// Common close button classes
						By.cssSelector("button.close, button[class*='close'], button[class*='Close']"),
						By.cssSelector("button[class*='dismiss'], button[class*='Dismiss']"),
						// Close icons and text
						By.xpath("//button[contains(text(), '×')]"),
						By.xpath("//button[contains(text(), 'X')]"),
						By.xpath("//span[contains(text(), '×')]/parent::button"),
						By.xpath("//button[.//svg[contains(@class, 'close')]]"),
						// Generic buttons near feedback text
						By.xpath("//button[contains(@class, 'btn') and (contains(@class, 'close') or contains(@class, 'dismiss'))]"),
						// Data attributes
						By.cssSelector("button[data-dismiss], button[data-close]")
					);
					
					for (By selector : closeButtonSelectors) {
						// Check timeout before trying each selector
						if (System.currentTimeMillis() - startTime > timeout) {
							return false;
						}
						
						try {
							List<WebElement> closeButtons = getDriver().findElements(selector);
							for (WebElement closeButton : closeButtons) {
								if (closeButton.isDisplayed() && closeButton.isEnabled()) {
									closeButton.click();
									return true; // Success - exit silently without logging
								}
							}
						} catch (Exception ignored) {
							// Try next selector
						}
					}
					
					// Check timeout before ESC key attempt
					if (System.currentTimeMillis() - startTime > timeout) {
						return false;
					}
					
					// If no close button found, try ESC key
					try {
						Actions actions = new Actions(getDriver());
						actions.sendKeys(Keys.ESCAPE).perform();
						return true;
					} catch (Exception ignored) {
						// Continue silently
					}
					
					// Check timeout before last resort
					if (System.currentTimeMillis() - startTime > timeout) {
						return false;
					}
					
					// Last resort: click outside the popup area (bottom-left corner)
					try {
						Actions actions = new Actions(getDriver());
						actions.moveByOffset(10, 10).click().perform();
						return true;
					} catch (Exception ignored) {
						// All strategies failed
					}
				}
			}
			return false;
		} catch (Exception e) {
			// Silently fail - this is a fallback mechanism that should never break tests
			return false;
		}
	}

	public long takeSnap(){
		long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L; 
		try {
			FileUtils.copyFile(getDriver().getScreenshotAs(OutputType.FILE), new File(SCREENSHOT_PATH + number + ".jpg"));
			// Don't call reportStep here to avoid infinite recursion
		} catch (WebDriverException e) {
			System.err.println("WebDriver exception while taking screenshot: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("IO exception while saving screenshot: " + e.getMessage());
		}
		return number;
	}

	public byte[] captureScreen(){
		return ((TakesScreenshot) (getDriver())).getScreenshotAs(OutputType.BYTES);
	}

	/**
	 * Wait for element to be clickable
	 */
	protected WebElement waitForElementToBeClickable(WebElement element) {
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(EXPLICIT_WAIT_SECONDS));
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	/**
	 * Wait for element presence by locator
	 */
	private WebElement waitForElementPresence(By locator) {
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(EXPLICIT_WAIT_SECONDS));
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	/**
	 * Wait for element to be visible
	 */
	protected WebElement waitForElementVisible(WebElement element) {
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(EXPLICIT_WAIT_SECONDS));
		return wait.until(ExpectedConditions.visibilityOf(element));
	}
	
	/**
	 * Wait for list of elements to be populated
	 * @param elements List of WebElements to wait for
	 * @param timeoutSeconds Timeout in seconds
	 * @return true if list is populated within timeout, false otherwise
	 */
	public boolean waitForListPopulated(List<WebElement> elements, int timeoutSeconds) {
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds));
			wait.until(driver -> !elements.isEmpty());
			reportStep("List populated with " + elements.size() + " elements", "PASS");
			return true;
		} catch (Exception e) {
			reportStep("Timeout waiting for list to be populated: " + e.getMessage(), "FAIL");
			return false;
		}
	}
	
	/**
	 * Validate all elements in a list contain specific text (case-insensitive)
	 * @param elements List of WebElements to validate
	 * @param expectedText Text that should be present in each element
	 * @return ValidationResult object with match count and validation status
	 */
	public ValidationResult validateListContainsText(List<WebElement> elements, String expectedText) {
		int matchCount = 0;
		int totalCount = elements.size();
		boolean allMatch = true;
		
		for (WebElement element : elements) {
			String elementText = element.getText();
			if (elementText.toUpperCase().contains(expectedText.toUpperCase())) {
				matchCount++;
			} else {
				allMatch = false;
				reportStep("Element does not contain expected text '" + expectedText + "': " + elementText, "INFO");
			}
		}
		
		return new ValidationResult(allMatch, matchCount, totalCount);
	}
	
	/**
	 * Check if element is displayed safely
	 * @param element WebElement to check
	 * @return true if element is displayed, false otherwise
	 */
	protected boolean isElementDisplayed(WebElement element) {
		try {
			return element.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	
	/**
	 * Inner class to hold validation results
	 */
	public static class ValidationResult {
		public final boolean allMatch;
		public final int matchCount;
		public final int totalCount;
		
		public ValidationResult(boolean allMatch, int matchCount, int totalCount) {
			this.allMatch = allMatch;
			this.matchCount = matchCount;
			this.totalCount = totalCount;
		}
	}
	
	/**
	 * Get framework capabilities summary using Java 21 Text Blocks
	 * Demonstrates modern Java feature for multi-line strings
	 * @return Framework capabilities as formatted string
	 */
	public static String getFrameworkCapabilities() {
		// Java 15+ Text Block feature for cleaner multi-line strings
		return """
			====================================
			Framework Capabilities Summary
			====================================
			
			Java Version: Java 21 LTS
			Build Tool: Maven
			Testing Framework: TestNG + Cucumber BDD
			Browser Automation: Selenium WebDriver 4.38.0
			
			Supported Browsers:
			  • Chrome (local & remote)
			  • Firefox (local & remote)
			  • Safari (local & remote)
			
			Reporting:
			  • Extent Reports (HTML with screenshots)
			  • Allure Reports (interactive)
			  • Cucumber Reports (JSON/HTML)
			
			Features:
			  ✓ Parallel execution support
			  ✓ Data-driven testing (Excel, Properties)
			  ✓ Page Object Model
			  ✓ ThreadLocal WebDriver management
			  ✓ Explicit & implicit waits
			  ✓ Screenshot on failure
			  ✓ Grid/Remote execution ready
			
			Java 21 Modern Features:
			  ✓ Records for immutable data (BrowserConfig, WindowSize, ParcelTestData)
			  ✓ Enhanced switch expressions with yield
			  ✓ Text blocks for multi-line strings
			  ✓ Pattern matching (ready for future enhancements)
			====================================
			""";
	}

}