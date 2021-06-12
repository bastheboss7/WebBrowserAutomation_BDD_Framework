package pages;

import io.cucumber.java.en.*;
import wrappers.LeafTapsWrappers;
public class LoginPage extends LeafTapsWrappers {

	@When("I login to the portal")
	public void loginWeb(){
		reportStep("Login to portal", "INFO");
		clickByXpath("//*[@class=\"header_user_info\"]/a");
		enterByXpath("//*[@id=\"email\"]","bskr_5chennai@yahoo.co.in");
		enterByXpath("//*[@id=\"passwd\"]","Box@1234");
		clickByXpath("//*[@id=\"SubmitLogin\"]");
		reportStep("Login completed", "INFO");
	}
}
