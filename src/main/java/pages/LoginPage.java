package pages;

import io.cucumber.java.en.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import wrappers.SkyWrappers;

public class LoginPage extends SkyWrappers {

	MyHomePage myHomePage;
	public LoginPage() {
		PageFactory.initElements(getDriver(), this);
		this.myHomePage = new MyHomePage();
	}

	@FindBy(xpath = "//*[@class=\"input-elements\"]/input")
	protected WebElement emailBox;

	@FindBy(xpath = "//*[text()=\"Continue\"]")
	protected WebElement continueBtn;

	@When("I try to sign in with invalid credentials")
	public void signInInvalid(){
		try {
			myHomePage.signIn.click();
			reportStep("Signing in with invalid credential", "INFO");
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			reportStep("Element not found", "FAIL");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("Unknown error occured", "FAIL");
		}
		switchToiFrameByXpath(prop.getProperty("LoginPage.Cookies.Xpath"));
		enterByEle(emailBox, prop.getProperty("LoginPage.UserName.Data"));
		clickByEle(continueBtn);
	}

	@Then("I should see a box with the text ‘Create your My Sky password’")
	public void assertPasswordBox(){
		try {
			Assert.assertEquals("Create your My Sky password",verifyTextByXpath(prop.getProperty("LoginPage.PasswordMsg.Xpath")));
			reportStep("The message 'Create your My Sky password' seen", "PASS");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("Unknown error occured", "FAIL");
		}
	}
}
