package wrappers;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Base Page Object class for all page objects in the framework
 * Extends GenericWrappers to inherit all WebDriver interaction methods
 * 
 * Note: Lifecycle management is handled by Hooks.java using Cucumber annotations
 * 
 * @author Baskar
 */
public class BasePageObject extends GenericWrappers {

	/**
	 * This method will click the element using Js query untill the attribute of the referenced element shown
	 * @param ele - The webelement
	 * @author Baskar
	 */
	public void clickByJsUntillProChg(WebElement ele, String value) {
		try{
			JavascriptExecutor executor = (JavascriptExecutor) getDriver();
			while (ele.getAttribute("class").equalsIgnoreCase(value)) {
				new WebDriverWait(getDriver(), java.time.Duration.ofSeconds(30)).ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(ele));
				executor.executeScript("arguments[0].click();", ele);
			}
			reportStep("Clicked broadband deals", "PASS");
		} catch (Exception e) {
			reportStep("Error occurred while clicking element: " + e.getMessage(), "FAIL");
		}
	}


}