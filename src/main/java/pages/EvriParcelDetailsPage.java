package pages;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import wrappers.BasePageObject;

public class EvriParcelDetailsPage extends BasePageObject {

	public EvriParcelDetailsPage() {
		PageFactory.initElements(getDriver(), this);
	}

	@FindBy(id = "parcel-contents")
	protected WebElement parcelContents;

	@FindBy(id = "parcel-value")
	protected WebElement parcelValue;

    @FindBy(id = "e-input__helper-parcel-contents")
    protected WebElement prohibitedText;

    @FindBy(css = "button[data-test-id='continue-button'][aria-disabled='true']")
    protected WebElement continueButton;

    /**
     * Enter text into the parcel contents field
     * @param parcelContent The content description to enter
     */
    @When("I enter {string} into the parcel contents field")
    public void iEnterIntoThePARCELCONTENTSField(String parcelContent) {
        try {
            enterByElement(parcelContents, parcelContent);
            reportStep("Entered parcel contents: '" + parcelContent + "'", "PASS");
        } catch (Exception e) {
            reportStep("Failed to enter parcel contents: " + e.getMessage(), "FAIL");
            throw new RuntimeException("Failed to enter parcel contents", e);
        }
    }

    /**
     * Enter the declared value of the parcel
     * @param value The monetary value of the parcel contents
     */
    @And("I enter {string} into the how much it is worth field")
    public void iEnterIntoTheHOWMUCHITISWORTHField(String value) {
        try {
            enterByElement(parcelValue, value);
            reportStep("Entered parcel value: '" + value + "'", "PASS");
        } catch (Exception e) {
            reportStep("Failed to enter parcel value: " + e.getMessage(), "FAIL");
            throw new RuntimeException("Failed to enter parcel value", e);
        }
    }

    /**
     * Verify error message is displayed for prohibited items
     * @param expectedErrorMessage The expected error message text
     */
    @Then("I should see an error message {string} under the parcel contents field")
    public void iShouldSeeAnErrorMessageUnderTheParcelContentsField(String expectedErrorMessage) {
        try {
            verifyTextByEle(prohibitedText, expectedErrorMessage);
        } catch (Exception e) {
            reportStep("Error message verification failed: " + e.getMessage(), "FAIL");
            throw new RuntimeException("Expected error message not found", e);
        }
    }

    /**
     * Verify that the continue button is disabled
     * Checks for aria-disabled attribute with fallback handling
     */
    @And("the continue button should be disabled")
    public void theContinueButtonShouldBeDisabled() {
        try {
            verifyElementDisplayed(continueButton);
            reportStep("Continue button is disabled as expected", "PASS");
        } catch (Exception e) {
            reportStep("Failed to verify button is disabled: " + e.getMessage(), "FAIL");
            throw new RuntimeException("Button disabled verification failed", e);
        }
    }
}
