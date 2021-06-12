@Baskar #Author
Feature: Update Myaccount

  @smoke
  Scenario: Update first name in My account

    When I login to the portal
    And I go to My account
    Then I should see my details

    When I change my first name
    |OldName|
    Then I should see the change at my account
