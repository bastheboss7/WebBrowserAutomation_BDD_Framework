package pages;

import io.cucumber.java.en.*;
import wrappers.LeafTapsWrappers;
public class MyLeadsPage extends LeafTapsWrappers {
	
	
	@When("Click Create Lead Link")
	public void clickCreateLead(){
		clickByLink("Create Lead");
	}
	
	@When("Click Find Leads Link")
	public void clickFindLeads(){
		clickByLink("Find Leads");
	}
	
	


}
