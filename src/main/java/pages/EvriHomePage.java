package pages;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import wrappers.BasePageObject;

public class EvriHomePage extends BasePageObject {

	public EvriHomePage() {
		PageFactory.initElements(getDriver(), this);
	}

	@FindBy(id = "onetrust-accept-btn-handler")
	protected WebElement acceptCookies;

	@FindBy(id = "from-postcode")
	protected WebElement fromPostcode;

	@FindBy(id = "to-postcode")
	protected WebElement toPostcode;

	@FindBy(id = "weight-choice-select")
	protected WebElement weightChoiceDrpdwn;

    @FindBy(css = "button[data-test-id='send-entry-submit']")
    protected WebElement sendParcelButton;

    /**
     * Navigate to Evri homepage and handle cookie consent banner
     * Uses explicit wait to handle cases where banner may not appear
     */
	@Given("I am on the evri.com homepage")
	public void cookiesHomePage(){
		try {
			// Wait for cookie banner with shorter timeout
			waitForElementToBeClickable(acceptCookies);
			acceptCookies.click();
			reportStep("Cookie banner accepted", "PASS");
		} catch (org.openqa.selenium.TimeoutException e) {
			// Cookie banner not present or already accepted - this is OK
			reportStep("Cookie banner not found (may already be accepted)", "INFO");
		} catch (Exception e) {
			reportStep("Error handling cookie banner: " + e.getMessage(), "WARN");
		}
	}    /**
     * Fill in postcode fields for parcel delivery
     * Uses fallback mechanism if primary element interaction fails
     * @param fromPostcode The sender's postcode
     * @param toPostcode The recipient's postcode
     */
    @When("I fill in from postcode {string} to postcode {string}")
    public void iFillInFromPostcodeToPostcode(String fromPostcode, String toPostcode) {
        try {
            enterByElement(this.fromPostcode, fromPostcode);
            enterByElement(this.toPostcode, toPostcode);
            reportStep("Entered postcodes: from '" + fromPostcode + "' to '" + toPostcode + "'", "PASS");
        } catch (Exception e) {
            reportStep("Failed to enter postcodes: " + e.getMessage(), "FAIL");
            throw new RuntimeException("Failed to enter postcodes", e);
        }
    }

    /**
     * Select parcel weight from dropdown and click send button
     * Waits for button to be enabled before clicking with fallback mechanism
     * @param weight The weight range to select (e.g., "1kg - 2kg")
     */
    @And("I choose weight {string} and click send a parcel button")
    public void iChooseWeightAndClickSendAParcelButton(String weight) {
        try {
            // Select weight from dropdown
            selectDropdownWithEvents(weightChoiceDrpdwn, weight);
            
            // Wait for send button to be enabled
            if (!waitForElementEnabled(sendParcelButton, 10)) {
                reportStep("Send button did not become enabled within timeout", "WARN");
            }
            
            // Click send button
            clickByElement(sendParcelButton);
            reportStep("Selected weight '" + weight + "' and clicked send parcel button", "PASS");
        } catch (Exception e) {
            reportStep("Failed to select weight or click button: " + e.getMessage(), "FAIL");
            throw new RuntimeException("Failed to complete weight selection and button click", e);
        }
    }
}
