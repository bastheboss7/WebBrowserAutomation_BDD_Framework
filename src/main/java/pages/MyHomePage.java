package pages;

import io.cucumber.java.en.When;
import wrappers.LeafTapsWrappers;

public class MyHomePage extends LeafTapsWrappers {

	@When("I set 'From' date as(.*)")
	public void setFromDate(String fromDate){
		reportStep("Setting 'From' date", "INFO");
		enterByCssJS("span.date-range",fromDate);
	}

	@When("I set 'To' date as(.*)")
	public void setToDate(String toDate){
		reportStep("Setting 'To' date", "INFO");
		enterByCssJS("span.date-range:nth-child(3)",toDate);
	}

	@When("I click 'Maximize' table")
	public void maxSize(){
		reportStep("Maximizing table window", "INFO");
		clickByXpath("//i[@class='fa fa-expand']");
		isDisplayed("//i[@class='fa fa-close close-full-screen']");
	}

	@When("I click 'Close' to minimize the table")
	public void minSize(){
		reportStep("Deafauting table size", "INFO");
		clickByXpath("//i[@class='fa fa-close close-full-screen']");
		isDisplayed("//i[@class='fa fa-expand']");
	}

	@When("I see search results in the table")
	public void resultAssert(){
		reportStep("Verify search result is shown in the table", "INFO");
		isDisplayed("//tr[@class='MuiTableRow-root MuiTableRow-hover']");
	}

	@When("I add a record under singular(.*)")
	public void singularRecord(String value){
		reportStep("Adding a record under singular tab", "INFO");
		clickByXpath("//*[@id=\"data-input-add-icon-id\"]");
		enterByXpath("//*[@id=\"value\"]",value);
		clickByXpath("//span[text()='Submit']");
	}

	@When("I confirm the record updated with the value(.*)")
	public void assertRecord(String value){
		reportStep("Asserting the record got updated", "INFO");
		isDisplayed("//td[text()='"+value+"']");
	}

	@When("I click delete record of singular value(.*)")
	public void singularRecordDeletion(String value){
		reportStep("Deleting the record", "INFO");
		clickByXpath("(//td[@class='MuiTableCell-root MuiTableCell-body']/div)[2]");
		clickByXpath("(//table[@class='MuiTable-root MuiTable-stickyHeader'])[2]//td[text()='"+value+"']/../td[6]/*");
		clickByXpath("//span[text()='Confirm']");
	}

	@When("I confirm the record deleted successfully(.*)")
	public void singularRecordDeletionAsser(String value){
		reportStep("Asserting the record got deleted", "INFO");
		isNOTDisplayed("(//table[@class='MuiTable-root MuiTable-stickyHeader'])[2]//td[text()='"+value+"']");

	}
}
