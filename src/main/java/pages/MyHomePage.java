package pages;

import io.cucumber.java.en.When;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import wrappers.LeafTapsWrappers;

public class MyHomePage extends LeafTapsWrappers {

	public MyHomePage() {
		PageFactory.initElements(getDriver(), this);
	}

	@FindBy(css = "span.date-range")
	public WebElement fromDateBox;

	@FindBy(css = "span.date-range:nth-child(3)")
	private WebElement toDateBox;

	@FindBy(xpath = "//i[@class='fa fa-expand']")
	private WebElement maximizeTable;

	@FindBy(xpath = "//i[@class='fa fa-close close-full-screen']")
	private WebElement closeFullScreen;

	@FindBy(xpath = "//tr[@class='MuiTableRow-root MuiTableRow-hover']")
	private WebElement tableResults;

	@FindBy(xpath = "//*[@id=\"data-input-add-icon-id\"]")
	private WebElement inputBox;

	@FindBy(xpath = "//*[@id=\"value\"]")
	private WebElement textBox;

	@FindBy(xpath = "//span[text()='Submit']")
	private WebElement submitBtn;


	@When("I set 'From' date as(.*)")
	public void setFromDate(String fDate){
		reportStep("Setting 'From' date", "INFO");
		enterByCssJsEle(fromDateBox,fDate);
	}

	@When("I set 'To' date as(.*)")
	public void setToDate(String toDate){
		reportStep("Setting 'To' date", "INFO");
		enterByCssJsEle(toDateBox,toDate);
	}

	@When("I click 'Maximize' table")
	public void maxSize(){
		reportStep("Maximizing table window", "INFO");
		clickByEle(maximizeTable);
		Assert.assertTrue(closeFullScreen.isDisplayed());
	}

	@When("I click 'Close' to minimize the table")
	public void minSize(){
		reportStep("Deafauting table size", "INFO");
		clickByEle(closeFullScreen);
		Assert.assertTrue(maximizeTable.isDisplayed());
	}

	@When("I see search results in the table")
	public void resultAssert(){
		reportStep("Verify search result is shown in the table", "INFO");
		Assert.assertTrue(tableResults.isDisplayed());
	}

	@When("I add a record under singular(.*)")
	public void singularRecord(String value){
		reportStep("Adding a record under singular tab", "INFO");
		clickByEle(inputBox);
		enterByEle(textBox,value);
		clickByEle(submitBtn);
	}

	@When("I confirm the record updated with the value(.*)")
	public void assertRecord(String value){
		reportStep("Asserting the record got updated", "INFO");

	}

	@When("I click delete record of singular value(.*)")
	public void singularRecordDeletion(String value){
		reportStep("Deleting the record", "INFO");

	}

	@When("I confirm the record deleted successfully(.*)")
	public void singularRecordDeletionAsser(String value){
		reportStep("Asserting the record got deleted", "INFO");


	}
}
