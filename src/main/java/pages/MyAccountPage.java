package pages;

import io.cucumber.java.en.*;
import wrappers.LeafTapsWrappers;

public class MyAccountPage extends LeafTapsWrappers {
	String randomName;

	@And("I go to My account")
	public void clkMyAccount(){
		reportStep("Clicking MyAccount", "INFO");
		clickByXpath("//*[@class=\"account\"]");
		verifyTextByXpath("//*[@id=\"center_column\"]/h1","My account");
	}

	@And("I should see my details")
	public void personalInfo(){
		reportStep("Clicking My Personal Information", "INFO");
		clickByXpath("//*[@title=\"Information\"]");
		verifyTextByXpath("//*[@id=\"center_column\"]//h1","Your personal information");
	}

	@And("I change my first name(.*)")
	public void changeFname(String newName){
		reportStep("Changing first name", "INFO");
		enterById("firstname",newName);
		randomName = newName;

		reportStep("Saving the change", "INFO");
		enterByXpath("//*[@id=\"old_passwd\"]","Box@1234");
		clickByXpath("(//button[@type=\"submit\"])[2]");
	}

	@And("I should see the change at my account")
	public void changedAccName(){
		verifyTextByXpath("//*[@class=\"account\"]/span",randomName+" Padmanaban");
	}
}
