@Baskar #Author
Feature: Order checkout

  @smoke
  Scenario: Order a T-Shirt and verify in 'Order History'

    When I login to the portal
    And I choose my T-shirt as
      |Faded Short Sleeve T-shirts|
    And Complete the checkout process
      |Faded Short Sleeve T-shirts|
    Then I should see order confirmation

    When I go to my order history
    Then I should see my order number


