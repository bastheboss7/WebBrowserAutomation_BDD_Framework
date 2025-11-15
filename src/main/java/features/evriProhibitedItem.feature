@Evri @ParcelDelivery
Feature: Parcel Delivery and Shop Finder
  As an Evri customer
  I want to send parcels and find nearby shops
  So that I can choose the most convenient delivery option

  Background:
    Given I am on the evri.com homepage

  @ProhibitedItems @Validation
  Scenario: User cannot send prohibited items
    When I fill in from postcode "BD2 3FG" to postcode "LS2 7EP"
    And I choose weight "1kg - 2kg" and click send a parcel button
    And I choose delivery options as standard, courier collection and click continue
    Then I should be on "Evri | Cheap Parcel Delivery & Courier Service UK" page
    When I enter "gun" into the parcel contents field
    And I enter "100" into the how much it is worth field
    Then I should see an error message "Parcel contents are prohibited" under the parcel contents field
    And the continue button should be disabled

  @ParcelShopFilter @Search
  Scenario: User can filter ParcelShops by location
    When I select the "Find a ParcelShop" link from the "ParcelShops" menu
    And I search for ParcelShops in "Edinburgh"
    And I select "ParcelShops" from the filters
    Then I should see only ParcelShops with postcodes starting with "EH" in the list


