package pages;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import wrappers.LeafTapsWrappers;

public class CheckoutPage extends LeafTapsWrappers {
    public static String orderNumber;
    WebDriverWait wait = new WebDriverWait(getDriver(), 20);

    @When("Complete the checkout process(.*)")
    public void checkOut(String checkOutItem){
        reportStep("Clicking 'Add to cart'", "INFO");
        clickByXpath("//*[@id=\"add_to_cart\"]/button");
        reportStep("Verify the cart success message and click 'Proceed to Checkout'", "INFO");

        WebDriverWait wait = new WebDriverWait(getDriver(), 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@title=\"Proceed to checkout\"]")));
        verifyTextByXpath("//*[@id=\"layer_cart\"]//h2","Product successfully added to your shopping cart");
        clickByXpath("//*[@title=\"Proceed to checkout\"]");

        reportStep("Verify the shopping cart summary", "INFO");
        verifyTextByXpath("//*/td[2]//a",checkOutItem);
        clickByXpath("(//*[@title=\"Proceed to checkout\"])[2]");

        reportStep("Verify the billing country in the summary", "INFO");
        verifyTextByXpath("//*[@id=\"address_invoice\"]/li[5]","United States");
        clickByXpath("//*[@id=\"center_column\"]/form/p/button");

        reportStep("Agreeing terms & conditions at the shipping page", "INFO");
        clickByXpath("//*[@id=\"cgv\"]");
        clickByXpath("//*[@id=\"form\"]/p/button");

        reportStep("Choosing payment method as 'Pay by check' at the payment page", "INFO");
        clickByXpath("//*[@title=\"Pay by check.\"]");

        reportStep("Confirming order by clicking the order confirmation button", "INFO");
        clickByXpath("//*[@id=\"cart_navigation\"]/button");
    }

    @When("I should see order confirmation")
    public void confirmOrder() {
        reportStep("Verify order confirmation message", "INFO");
        verifyTextByXpath("//*[@id=\"center_column\"]/p[1]", "Your order on My Store is complete.");

        orderNumber = getTextByXpath("//*[@id=\"center_column\"]/div").substring(146,155);
        reportStep("Order confirmation number is :"+orderNumber+"", "INFO");
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
