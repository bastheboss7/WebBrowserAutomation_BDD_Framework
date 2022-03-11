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

	@When("I login to intellisense web")
	public void loginWebIntellisense(){
		reportStep("Login to portal", "INFO");
//		clickByXpath("//*[@class=\"header_user_info\"]/a");
		enterByXpath("//*[@id=\"emailAddress\"]","menna+testproject@intellisense.io");
		enterByXpath("//*[@id=\"password\"]","AutMaNewTest1#");
		clickByXpath("//button[@type=\"button\"]/span/h6/../..");
		reportStep("Login completed", "INFO");
	}
}
