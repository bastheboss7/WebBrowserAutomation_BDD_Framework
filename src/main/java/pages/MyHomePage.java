package pages;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import wrappers.SkyWrappers;

public class MyHomePage extends SkyWrappers {

	public MyHomePage() {
		PageFactory.initElements(getDriver(), this);
	}

	@FindBy(xpath = "//button[@title=\"Agree\"]")
	protected WebElement acceptCookies;

	@FindBy(xpath = "//*[text()=\"Deals\"]")
	protected WebElement deals;

	@FindBy(xpath = "//*[text()=\"Sign in\"]")
	protected WebElement signIn;

	@FindBy(id = "masthead-search-toggle")
	protected WebElement search;

	@FindBy(xpath = "//*[@data-test-id=\"input-box\"]")
	protected WebElement inputBox;

	@FindBy(id = "search-results-wrapper")
	protected WebElement editorialSection;

	@Given("I am on the home page")
	public void cookiesHomePage(){
		switchToiFrameByXpath(prop.getProperty("Home.Cookies.Xpath"));
		try {
			acceptCookies.click();
			getDriver().switchTo().parentFrame();
			reportStep("Cookies accepted and user landed on homepage", "PASS");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("Unknown error occured", "FAIL");
		}
	}

	@When("I navigate to ‘Deals’")
	public void navigateToDeals(){
		try {
			deals.click();
			reportStep("User clicked the link 'deals'", "PASS");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("Unknown error occured", "FAIL");
		}
	}

	@Then("the user should be on the below page(.*)")
	public void assertLandingPage(String url){
		try {
			Assert.assertEquals(getDriver().getCurrentUrl(),url);
			reportStep("Asserted User navigated to "+url, "PASS");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("Assertion Failed", "FAIL");
		}
	}

	@When("I search ‘sky’ in the search bar")
	public void skySearch(){
		try {
			search.click();
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("Unknown error occured", "FAIL");
		}
		enterByEle(inputBox, prop.getProperty("Home.Search.Data"));
		reportStep("Entered data into the search", "PASS");
	}

	@Then("I should see an editorial section")
	public void editorial(){
		try {
			Assert.assertTrue(editorialSection.isDisplayed());
			reportStep("Asserted editorial section displayed", "PASS");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("Assertion failed", "FAIL");
		}
	}
}
