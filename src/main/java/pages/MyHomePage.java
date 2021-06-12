package pages;

import io.cucumber.java.en.*;
import wrappers.LeafTapsWrappers;
import static pages.CheckoutPage.orderNumber;

public class MyHomePage extends LeafTapsWrappers {

	@When("I choose my T-shirt as(.*)")
	public void setTypeOfCostume(String typeOfCostume){
		reportStep("Clicking 'T-Shirt' as the type", "INFO");
		clickByXpath("(//a[text()='T-shirts'])[2]");

		reportStep("Clicking 'Faded Short Sleeve T-shirts' as checkout item", "INFO");
		clickByXpath("//h5/a[@title='"+typeOfCostume+"']");

		reportStep("Verify the cart details", "INFO");
		verifyTextByXpath("//*[@id=\"center_column\"]//h1",typeOfCostume);
	}

	@When("I go to my order history")
	public void navigateToOrderHistory(){
		reportStep("Navigating to order history page", "INFO");
		clickByXpath("//*[@title=\"Back to orders\"]");
	}

	@When("I should see my order number")
	public void orderHistory(){
		reportStep("Verifying order history table", "INFO");
		verifyTextByXpath("//table//td/a[contains(text(),"+orderNumber+")]",orderNumber);
	}
}
