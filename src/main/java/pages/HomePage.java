package pages;

import io.cucumber.java.en.*;
import wrappers.LeafTapsWrappers;
public class HomePage extends LeafTapsWrappers {
	
	
	@When("Click CRMSFA")
	public void clickCreateLead(){
		clickByLink("CRM/SFA");
	}
	
	


}
