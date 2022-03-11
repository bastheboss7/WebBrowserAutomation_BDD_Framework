@Baskar #Author
Feature: Brains App for Intellisense

  @smoke
  Scenario: Intellisense dashboard - Assert search table

    When I login to intellisense web
    And I set 'From' date as
      |Nov 2, 2020 11:35 AM GMT|
    And I set 'To' date as
      |Feb 14, 2022 12:35 PM GMT|
    Then I see search results in the table

#  @debug
  Scenario: Intellisense dashboard - Adding a singular record

    When I login to intellisense web
    And I set 'From' date as
      |Nov 2, 2020 11:35 AM GMT|
    And I set 'To' date as
      |Feb 14, 2022 12:35 PM GMT|

    When I add a record under singular
    |100|
    Then I confirm the record updated with the value
    |100|

  @debug
  Scenario: Intellisense dashboard - Deleting a singular record

    When I login to intellisense web
    And I set 'From' date as
      |Nov 2, 2020 11:35 AM GMT|
    And I set 'To' date as
      |Feb 14, 2022 12:35 PM GMT|

    When I click delete record of singular value
      |100|
    Then I confirm the record deleted successfully
      |100|

  @smoke
  Scenario: Intellisense dashboard - Maximize the table and assert it

    When I login to intellisense web
    And I set 'From' date as
      |Nov 2, 2020 11:35 AM GMT|
    And I set 'To' date as
      |Feb 14, 2022 12:35 PM GMT|

    When I click 'Maximize' table
    Then I click 'Close' to minimize the table


