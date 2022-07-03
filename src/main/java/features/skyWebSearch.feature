@Baskar #Author
Feature: This feature will make the search show the results that are determined by editorial, as well as generic searches.

  Background:
  Given I am on the home page

  @Scenario4
  Scenario: User performs web search

    When I search ‘sky’ in the search bar
    Then I should see an editorial section
