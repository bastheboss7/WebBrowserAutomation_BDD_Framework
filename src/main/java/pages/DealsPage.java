package pages;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import wrappers.SkyWrappers;

public class DealsPage extends SkyWrappers {

    public DealsPage() {
        PageFactory.initElements(getDriver(), this);
    }

    @FindBy(xpath = "//*[@id=\"deals-filter\"]//*[contains(text(),'Broadband')][2]")
    protected WebElement filterBroadband;

    @FindBy(xpath = "//*[text()='Superfast Broadband']")
    protected WebElement offerName1;

    @FindBy(xpath = "//*[@id=\"price.offer.blt7cf81cc6d337a8fd\"]")
    protected WebElement offerPrice1;

    @FindBy(xpath = "//*[text()='Q Lite - £26 Fibre Broadband & £5 Sky Q']")
    protected WebElement offerName2;

    @FindBy(xpath = "//*[@id=\"price.offer.blt400a25464b8ee620\"]")
    protected WebElement offerPrice2;

    @FindBy(xpath = "//*[text()='Ultrafast Broadband']")
    protected WebElement offerName3;

    @FindBy(xpath = "//*[@id=\"price.offer.blt03778c4e45b27910\"]")
    protected WebElement offerPrice3;

    @And("I select deals for broadband")
    public void broadbandDeals() {
        try {
            if (sBrowser.contains("chrome")){
            filterBroadband.click();
            reportStep("Clicked broadband deals", "PASS");}
            else{//Specific method for Safari browser
                clickByJsUntillProChg(filterBroadband,prop.getProperty("Deals.OfferAttribute.Value"));
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            reportStep("Element not found", "FAIL");
        } catch (Exception e) {
            e.printStackTrace();
            reportStep("Unknown error occurred", "FAIL");
        }
    }

    @Then("I see a list of deals with a price to it")
    public void assertDeals() {
        try {
            Assert.assertTrue(offerName1.isDisplayed());
            Assert.assertEquals(offerPrice1.getText().substring(6), prop.getProperty("Deals.OfferPrice1.Data"));
            reportStep(offerName1.getText() + " -name and price asserted", "PASS");

            Assert.assertTrue(offerName2.isDisplayed());
            Assert.assertEquals(offerPrice2.getText().substring(6), prop.getProperty("Deals.OfferPrice2.Data"));
            reportStep(offerName2.getText() + " -name and price asserted", "PASS");

            Assert.assertTrue(offerName3.isDisplayed());
            Assert.assertEquals(offerPrice3.getText().substring(6), prop.getProperty("Deals.OfferPrice3.Data"));
            reportStep(offerName3.getText() + " -name and price asserted", "PASS");
        } catch (Exception e) {
            e.printStackTrace();
            reportStep("Error occured while asserting the offers", "FAIL");
        }
    }
}
