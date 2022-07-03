@Baskar #Author
Feature: This feature will make sure that the shop page is navigable and usable.

  Background:
  Given I am on the home page

  @Scenario1
  Scenario: User navigates to shop page

    When I navigate to ‘Deals’
    Then the user should be on the below page
      |https://www.sky.com/deals|

  @Scenario2
  Scenario: User sees tiles on the shop page

    When I try to sign in with invalid credentials
    Then I should see a box with the text ‘Create your My Sky password’

  @Scenario3
  Scenario: User sees a list of deals on the deals page

    When I navigate to ‘Deals’
    And I select deals for broadband
    Then I see a list of deals with a price to it


