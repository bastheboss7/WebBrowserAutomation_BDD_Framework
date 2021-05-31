package pages;

import io.cucumber.java.en.*;
import wrappers.LeafTapsWrappers;
public class MyHomePage extends LeafTapsWrappers {
	
	
	@When("Click Leads")
	public void clickLead(){
		clickByLink("Leads");
	}
	
	


}
