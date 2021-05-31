package pages;

import io.cucumber.java.en.*;
import wrappers.LeafTapsWrappers;
public class ViewLead extends LeafTapsWrappers {
	
	
	@When("Click Delete Lead Button")
	public void clickDeleteLead() {
		clickByLink("Delete");
	}
	
	@Then("Verify the First Name (.*)")
	public void verifyFname(String fname) {
		verifyTextById("viewLead_firstName_sp", fname);
	}

}
