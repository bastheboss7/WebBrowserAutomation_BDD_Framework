package pages;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import wrappers.BasePageObject;

public class EvriDeliveryOptionsPage extends BasePageObject {

    public EvriDeliveryOptionsPage() {
        PageFactory.initElements(getDriver(), this);
    }

    @FindBy(id = "standard-speed")
    protected WebElement standardSpeed;

    @FindBy(id = "courier-method")
    protected WebElement courierMethod;

    @FindBy(css = "button[data-test-id='continue-button']")
    protected WebElement continueButton;

    /**
     * Select delivery options and proceed to next step
     * Chooses standard speed and courier collection method with fallback handling
     */
    @And("I choose delivery options as standard, courier collection and click continue")
    public void iChooseDeliveryOptionsAsStandardCourierCollectionAndClickContinue() {
        try {
            // Select standard speed
            clickByElement(standardSpeed);
            
            // Select courier method
            clickByElement(courierMethod);
            
            // Click continue button
            clickByElement(continueButton);
            
            reportStep("Selected delivery options and clicked continue", "PASS");
        } catch (Exception e) {
            reportStep("Failed to select delivery options: " + e.getMessage(), "FAIL");
            throw new RuntimeException("Failed to select delivery options and continue", e);
        }
    }

    /**
     * Verify page title matches expected value
     * @param expectedTitle The expected page title
     */
    @Then("I should be on {string} page")
    public void iShouldBeOnPage(String expectedTitle) {
        try {
            verifyTitle(expectedTitle);
        } catch (Exception e) {
            reportStep("Title verification failed: " + e.getMessage(), "FAIL");
            throw new RuntimeException("Page title does not match expected", e);
        }
    }
}
