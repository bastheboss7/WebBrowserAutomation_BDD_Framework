package pages;

import io.cucumber.java.en.*;
import wrappers.LeafTapsWrappers;
public class LoginPage extends LeafTapsWrappers {

	@When("I login to intellisense web")
	public void loginWebIntellisense(){
		reportStep("Login to portal", "INFO");
		enterByXpath("//*[@id=\"emailAddress\"]","menna+testproject@intellisense.io");
		enterByXpath("//*[@id=\"password\"]","AutMaNewTest1#");
		clickByXpath("//button[@type=\"button\"]/span/h6/../..");
		reportStep("Login completed", "INFO");
	}
}
