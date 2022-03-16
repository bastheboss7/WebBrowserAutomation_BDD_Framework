package wrappers;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import utils.Reporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;

public class GenericWrappers extends Reporter implements Wrappers {


	protected static final ThreadLocal<GenericWrappers> driverThreadLocal = new ThreadLocal<>();
	public RemoteWebDriver driver;
	protected Properties prop;
	public String sUrl;
	public String sHubUrl;
	public String sHubPort;

	public void setDriver(GenericWrappers wrappers) {
		driverThreadLocal.set(wrappers);
	}

	public RemoteWebDriver getDriver() {
		return driverThreadLocal.get().driver;
	}

	public GenericWrappers() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("./src/main/resources/config.properties"));
			sHubUrl = prop.getProperty("HUB");
			sHubPort = prop.getProperty("PORT");
			sUrl = prop.getProperty("URL");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadObjects() {
		prop = new Properties();
		try {
			prop.load(new FileInputStream("src/main/resources/object.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	 * @return
	 **/
	public synchronized RemoteWebDriver invokeApp(String browser, boolean bRemote) {
		try {

			DesiredCapabilities dc = new DesiredCapabilities();
			dc.setBrowserName(browser);
			dc.setPlatform(Platform.WINDOWS);
			
			// this is for grid run
			if(bRemote)
				driver = new RemoteWebDriver(new URL("http://"+sHubUrl+":"+sHubPort+"/wd/hub"), dc);
			else{ // this is for local run
				if(browser.equalsIgnoreCase("chrome")){
					System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver");
					driver = new ChromeDriver();
				}else{
					System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver.exe");
					driver = new FirefoxDriver();
				}
			}
			GenericWrappers gw = new GenericWrappers();
			gw.driver = driver;
			setDriver(gw);

			getDriver().manage().window().maximize();
			getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			getDriver().get(sUrl);

			//reportStep("The browser:" + browser + " launched successfully", "PASS");

		} catch (Exception e) {
			e.printStackTrace();
			//reportStep("The browser:" + browser + " could not be launched", "FAIL");
		}

		return getDriver();
	}

	/**
	 * This method will enter the value to the text field using id attribute to locate
	 * 
	 * @param idValue - id of the webelement
	 * @param data - The data to be sent to the webelement
	 * @author Baskar
	 * @throws IOException 
	 
	 */
	//
	public void enterById(String idValue, String data) {
		try {
			getDriver().findElement(By.id(idValue)).clear();
			getDriver().findElement(By.id(idValue)).sendKeys(data);	
			reportStep("The data: "+data+" entered successfully in field :"+idValue, "PASS");
		} catch (NoSuchElementException e) {
			reportStep("The data: "+data+" could not be entered in the field :"+idValue, "FAIL");
			throw new RuntimeException();
		}
		catch (WebDriverException e) {
			e.printStackTrace();
			reportStep("Unknown exception occured while entering "+data+" in the field :"+idValue, "FAIL");
		}
		catch (Exception e) {
			reportStep("Unknown exception occured while entering "+data+" in the field :"+idValue, "FAIL");
		}
	}

	/**
	 * This method will enter the value to the text field using name attribute to locate
	 * 
	 * @param nameValue - name of the webelement
	 * @param data - The data to be sent to the webelement
	 * @author Baskar
	 * @throws IOException 
	 
	 */

	//Enter the values using Name Locator
	public void enterByName(String nameValue, String data) {
		try {
			getDriver().findElement(By.name(nameValue)).clear();
			getDriver().findElement(By.name(nameValue)).sendKeys(data);	
			reportStep("The data: "+data+" entered successfully in field :"+nameValue, "PASS");

		} catch (NoSuchElementException e) {
			reportStep("The data: "+data+" could not be entered in the field :"+nameValue, "FAIL");
		} catch (Exception e) {
			reportStep("Unknown exception occured while entering "+data+" in the field :"+nameValue, "FAIL");
		}

	}

	/**
	 * This method will enter the value to the text field using name attribute to locate
	 * 
	 * @param xpathValue - xpathValue of the webelement
	 * @param data - The data to be sent to the webelement
	 * @author Baskar
	 * @throws IOException 
	 
	 */
	public void enterByXpath(String xpathValue, String data) {
		try {
			getDriver().findElement(By.xpath(xpathValue)).clear();
			getDriver().findElement(By.xpath(xpathValue)).sendKeys(data);	
			reportStep("The data: "+data+" entered successfully in field :"+xpathValue, "PASS");

		} catch (NoSuchElementException e) {
			reportStep("The data: "+data+" could not be entered in the field :"+xpathValue, "FAIL");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("Unknown exception occured while entering "+data+" in the field :"+xpathValue, "FAIL");
		}

	}

	public void enterByCssJS(String cssValue, String data) {
		try {
			WebElement element = getDriver().findElement(By.cssSelector(cssValue));
			((JavascriptExecutor)getDriver()).executeScript("arguments[0].innerText = '"+data+"'", element);
			reportStep("The data: "+data+" entered successfully in field :"+cssValue, "PASS");

		} catch (NoSuchElementException e) {
			reportStep("The data: "+data+" could not be entered in the field :"+cssValue, "FAIL");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("Unknown exception occured while entering "+data+" in the field :"+cssValue, "FAIL");
		}

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
				reportStep("The title of the page:"+getDriver().getTitle()+" did not match with the value :"+title, "SUCCESS");

		}catch (Exception e) {
			reportStep("Unknown exception occured while verifying the title", "FAIL");
		}
		return bReturn;
	}

	/**
	 * This method will verify the given text matches in the element text
	 * @param xpath - The locator of the object in xpath
	 * @param text  - The text to be verified
	 * @author Baskar
	 */
	public void verifyTextByXpath(String xpath, String text){

		try {
			String sText = getDriver().findElementByXPath(xpath).getText();
			if (sText.equalsIgnoreCase(text)){
				reportStep("The text: "+sText+" matches with the value :"+text, "PASS");
			}else{
				reportStep("The text: "+sText+" did not match with the value :"+text, "FAIL");
			}
		}catch (Exception e) {
			e.printStackTrace();
			reportStep("Unknown exception occured while verifying the text", "FAIL");
		}
	}

	/**
	 * This method will verify the given text is available in the element text
	 * @param xpath - The locator of the object in xpath
	 * @param text  - The text to be verified
	 * @author Baskar
	 */
	public void verifyTextContainsByXpath(String xpath, String text){
		try{
			String sText = getDriver().findElementByXPath(xpath).getText();
			if (sText.contains(text)){
				reportStep("The text: "+sText+" contains the value :"+text, "PASS");
			}else{
				reportStep("The text: "+sText+" did not contain the value :"+text, "FAIL");
			}
		}catch (Exception e) {
			reportStep("Unknown exception occured while verifying the title", "FAIL");
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
			String sText = getDriver().findElementById(id).getText();
			if (sText.equalsIgnoreCase(text)){
				reportStep("The text: "+sText+" matches with the value :"+text, "PASS");
			}else{
				reportStep("The text: "+sText+" did not match with the value :"+text, "FAIL");
			}
		}catch (Exception e) {
			reportStep("Unknown exception occured while verifying the title", "FAIL");
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
			String sText = getDriver().findElementById(id).getText();
			if (sText.contains(text)){
				reportStep("The text: "+sText+" contains the value :"+text, "PASS");
			}else{
				reportStep("The text: "+sText+" did not contain the value :"+text, "FAIL");
			}
		}catch (Exception e) {
			reportStep("Unknown exception occured while verifying the title", "FAIL");
		}
	}

	/**
	 * This method will close all the browsers
	 * @author Baskar
	 */
	public void quitBrowser() {
		try {
			getDriver().quit();
		} catch (Exception e) {
			reportStep("The browser:"+getDriver().getCapabilities().getBrowserName()+" could not be closed.", "FAIL");
		}

	}

	/**
	 * This method will click the element using id as locator
	 * @param id  The id (locator) of the element to be clicked
	 * @author Baskar
	 */
	public void clickById(String id) {
		try{

			getDriver().findElement(By.id(id)).click();
			reportStep("The element with id: "+id+" is clicked.", "PASS");

		}catch (NoSuchElementException e) {			
			reportStep("The element with id: "+id+" could not be clicked.", "FAIL");
			throw new RuntimeException();
		}

		catch (Exception e) {
			reportStep("The element with id: "+id+" could not be clicked.", "FAIL");
		}
	}

	 /*
	 * This method will click the element using id as locator
	 * @param id  The id (locator) of the element to be clicked
	 * @author Baskar
	 **/
	public void clickByClassName(String classVal) {
		try{
			getDriver().findElement(By.className(classVal)).click();
			reportStep("The button with class Name: "+classVal+" is clicked.", "PASS");
		}catch (NoSuchElementException e){
			reportStep("The button with class Name: "+classVal+" could not be found.", "FAIL");
		}catch (Exception e) {
			reportStep("The button with class Name: "+classVal+" could not be clicked.", "FAIL");
		}
	}
	/**
	 * This method will click the element using name as locator
	 * @param name  The name (locator) of the element to be clicked
	 * @author Baskar
	 */
	public void clickByName(String name) {
		try{
			getDriver().findElement(By.name(name)).click();
			reportStep("The element with name: "+name+" is clicked.", "PASS");
		} catch (Exception e) {
			reportStep("The element with name: "+name+" could not be clicked.", "FAIL");
		}
	}

	/**
	 * This method will click the element using link name as locator
	 * @param name  The link name (locator) of the element to be clicked
	 * @author Baskar
	 */
	public void clickByLink(String name) {
		//	getDriver().findElementByLinkText(name).click();
		try{
			getDriver().findElementByLinkText(name).click();
			reportStep("The element with link name: "+name+" is clicked.", "PASS");
		} catch (WebDriverException e) {
			reportStep("The element with link name: "+name+" could not be clicked.", "FAIL");
		}
	}

	public void clickByLinkNoSnap(String name) {
		try{
			getDriver().findElement(By.linkText(name)).click();
			//reportStep("The element with link name: "+name+" is clicked.", "PASS");
		} catch (Exception e) {
			reportStep("The element with link name: "+name+" could not be clicked.", "FAIL");
		}
	}

	/**
	 * This method will click the element using xpath as locator
	 * @param xpathVal  The xpath (locator) of the element to be clicked
	 * @author Baskar
	 */
	public void clickByXpath(String xpathVal) {
		try{

			getDriver().findElement(By.xpath(xpathVal)).click();
			reportStep("The element : "+xpathVal+" is clicked.", "PASS");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The element with xpath: "+xpathVal+" could not be clicked.", "FAIL");
		}
	}

	public void clickByXpathNoSnap(String xpathVal) {
		try{
			getDriver().findElement(By.xpath(xpathVal)).click();
			reportStep("The element : "+xpathVal+" is clicked.", "INFO");
		} catch (WebDriverException e) {
			reportStep("The element with xpath: "+xpathVal+" could not be clicked.", "FAIL");
		}
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
			return getDriver().findElementById(idVal).getText();
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
	public void selectVisibileTextById(String id, String value) {
		try{
			new Select(getDriver().findElement(By.id(id))).selectByVisibleText(value);
			reportStep("The element with id: "+id+" is selected with value :"+value, "PASS");
		} catch (Exception e) {
			reportStep("The value: "+value+" could not be selected.", "FAIL");
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

	public String getAlertText() {		
		String text = null;
		try {
			getDriver().switchTo().alert().dismiss();
		} catch (NoAlertPresentException e) {
			reportStep("The alert could not be found.", "FAIL");
		} catch (Exception e) {
			reportStep("The alert could not be accepted.", "FAIL");
		}
		return null;

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

	public long takeSnap(){
		long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L; 
		try {
			FileUtils.copyFile(getDriver().getScreenshotAs(OutputType.FILE) , new File("./reports/images/"+number+".jpg"));
		} catch (WebDriverException e) {
			e.printStackTrace();
		} catch (IOException e) {
			reportStep("The snapshot could not be taken", "WARN");
		}
		return number;
	}

	public void isDisplayed(String xpath) {
		try {
			Assert.assertTrue(getDriver().findElementByXPath(xpath).isDisplayed());
			reportStep("The element with xpath: "+xpath+" is displayed.", "PASS");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The element with xpath: "+xpath+" is NOT displayed.", "FAIL");
		}
	}

	public void isNOTDisplayed(String xpath) {
		try {
			assertFalse(getDriver().findElementByXPath(xpath).isDisplayed());
			reportStep("The element with xpath: "+xpath+" is displayed.", "FAIL");
		} catch (NoSuchElementException e) {
			reportStep("The element with xpath: "+xpath+" is NOT displayed.", "PASS");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("Unknown error occured", "FAIL");
		}
	}

}