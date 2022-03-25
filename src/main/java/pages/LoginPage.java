package pages;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import wrappers.LeafTapsWrappers;
public class LoginPage extends LeafTapsWrappers {

	MyHomePage myHomePage;
	public LoginPage() {
		PageFactory.initElements(getDriver(), this);
		this.myHomePage = new MyHomePage();
	}

	@FindBy(id = "emailAddress")
	private WebElement emailBox;

	@FindBy(id = "password")
	private WebElement passwordBox;

	@FindBy(xpath = "//button[@type=\"button\"]/span/h6/../..")
	private WebElement submitBtn;


	@When("I login to intellisense web")
	public void loginWebIntellisense(){
		reportStep("Login to portal", "INFO");
		enterByEle(emailBox, prop.getProperty("LoginPage.UserName.Data"));
		enterByEle(passwordBox, prop.getProperty("LoginPage.Password.Data"));
		clickByEle(submitBtn);
		Assert.assertTrue(myHomePage.fromDateBox.isDisplayed());
		reportStep("Login completed", "INFO");

	}
}
