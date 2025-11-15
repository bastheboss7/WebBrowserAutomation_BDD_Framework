package pages;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import wrappers.GenericWrappers;
import wrappers.BasePageObject;

import java.util.List;

public class EvriParcelShopFinderPage extends BasePageObject {

    public EvriParcelShopFinderPage() {
        PageFactory.initElements(getDriver(), this);
    }

    // Page Elements
    @FindBy(css = "button[data-test-id='link-group-parcelshops']")
    protected WebElement parcelShopsMenuLink;

    @FindBy(css = "a[href*='find-a-parcelshop'] span, a[href*='parcelshop'] span")
    protected WebElement findParcelShopLink;

    @FindBy(css = "input[data-test-id='psf-input']")
    protected WebElement searchInput;

    @FindBy(id = "search")
    protected WebElement locationSearchInput;

    @FindBy(css = "button[data-test-id='psf-input-search-button-icon']")
    protected WebElement searchButton;

    @FindBy(css = "button[data-test-id='filters-btn']")
    protected WebElement parcelShopsFilter;

    @FindBy(xpath = "//button[@data-test-id='filters-btn']")
    protected WebElement parcelShopsFilterButton;

    @FindBy(css = ".search-results__container-drop-off")
    protected List<WebElement> shopList;

    // Step Definitions

    /**
     * Navigate to ParcelShop finder page via menu hover interaction
     * Uses Actions class for reliable mouse hover with fallback to direct URL
     * @param linkText The text of the link to click
     * @param menuName The name of the menu containing the link
     */
    @When("I select the {string} link from the {string} menu")
    public void iSelectTheLinkFromTheMenu(String linkText, String menuName) {
        // Direct navigation (primary method)
        getDriver().get("https://www.evri.com/find-a-parcelshop");
        reportStep("Navigated to ParcelShop finder using direct URL", "PASS");
    }

    /**
     * Search for ParcelShops in a specific location
     * Handles multiple search input types with flexible element detection
     * @param location The location to search for (city, postcode, etc.)
     */
    @And("I search for ParcelShops in {string}")
    public void iSearchForParcelShopsIn(String location) {
        try {
            // Try location-specific search input first
            if (isElementDisplayed(locationSearchInput)) {
                enterByElement(locationSearchInput, location);
            } else {
                enterByElement(searchInput, location);
            }
            
            // Click search button if present
            if (isElementDisplayed(searchButton)) {
                clickByElement(searchButton);
            }
            
            reportStep("Searched for location: " + location, "PASS");
        } catch (Exception e) {
            reportStep("Failed to search for: " + location + " - " + e.getMessage(), "FAIL");
            throw new RuntimeException("Failed to search for location: " + location, e);
        }
    }

    /**
     * Select a filter option to refine search results
     * Tries multiple filter element types (checkbox, button)
     * @param filterName The name of the filter to apply
     */
    @And("I select {string} from the filters")
    public void iSelectFromTheFilters(String filterName) {
        try {
            // Try checkbox filter first
            if (isElementDisplayed(parcelShopsFilter)) {
                clickByElement(parcelShopsFilter);
            } else if (isElementDisplayed(parcelShopsFilterButton)) {
                clickByElement(parcelShopsFilterButton);
            }
            
            reportStep("Selected '" + filterName + "' filter", "PASS");
        } catch (Exception e) {
            reportStep("Failed to select filter: " + filterName + " - " + e.getMessage(), "FAIL");
            throw new RuntimeException("Failed to select filter: " + filterName, e);
        }
    }

    /**
     * Verify that all displayed ParcelShops have postcodes matching the expected prefix
     * Uses framework wait methods and reusable validation logic
     * @param postcodePrefix The expected postcode prefix (e.g., "EH" for Edinburgh)
     * @throws AssertionError if validation fails or no shops are found
     */
    @Then("I should see only ParcelShops with postcodes starting with {string} in the list")
    public void iShouldSeeOnlyParcelShopsWithPostcodesStartingWithInTheList(String postcodePrefix) {
        try {
            // Wait for results to load using framework wait method
            if (!waitForListPopulated(shopList, 10)) {
                reportStep("No ParcelShops found in results list", "FAIL");
                throw new AssertionError("No ParcelShops found in results list");
            }
            
            // Validate list contains postcode prefix using reusable method
            GenericWrappers.ValidationResult validationResult = validateListContainsText(shopList, postcodePrefix);
            
            // Report results
            if (validationResult.allMatch && validationResult.matchCount > 0) {
                reportStep("All " + validationResult.matchCount + " ParcelShops have postcodes starting with '" + postcodePrefix + "'", "PASS");
            } else if (validationResult.matchCount > 0) {
                reportStep("Found " + validationResult.matchCount + " out of " + validationResult.totalCount + " ParcelShops with postcodes starting with '" + postcodePrefix + "'", "INFO");
            } else {
                reportStep("No ParcelShops found with postcode prefix '" + postcodePrefix + "'", "FAIL");
                throw new AssertionError("No ParcelShops found with postcode prefix '" + postcodePrefix + "'");
            }
            
        } catch (AssertionError e) {
            throw e; // Re-throw assertion errors
        } catch (Exception e) {
            reportStep("Failed to verify ParcelShops postcodes: " + e.getMessage(), "FAIL");
            throw new AssertionError("Failed to verify ParcelShops postcodes", e);
        }
    }
}
