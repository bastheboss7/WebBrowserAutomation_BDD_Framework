package pages;

import io.cucumber.java.en.*;
//import org.apache.xpath.operations.String;
import wrappers.LeafTapsWrappers;

public class CreateLead extends LeafTapsWrappers {	
	
	@When("Enter Company Name(.*)")
	public void enterCompanyName(String cname){
		enterById("createLeadForm_companyName", cname);
	}
	
	@When("Enter First Name(.*)")
	public void enterFirstName(String fname){
		enterById("createLeadForm_firstName", fname);
	}
	

	@When("Enter Last Name(.*)")
	public void enterLastName(String lname){
		enterById("createLeadForm_lastName", lname);
	}
	
	@When("Click Create Lead Button")
	public void clickCreateLead(){
		clickByName("submitButton");
	}
	
	@When("Select Source (.*)")
	public void selectSource(String source){
		selectVisibileTextById("createLeadForm_dataSourceId", source);
	}
	
	
	@When("Select Marketing Campaign (.*)")
	public void selectMarketingCampaign(String campaign){
		selectVisibileTextById("createLeadForm_marketingCampaignId", campaign);
	}
	
	
	@When("Enter Phone Number (.*)")
	public void enterPhone(String phone){
		enterById("createLeadForm_primaryPhoneNumber", phone);
	}

}
