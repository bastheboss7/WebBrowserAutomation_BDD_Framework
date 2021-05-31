@Babu
Feature: Delete Lead

@regression
Scenario Outline: Delete Lead (Positive)
When Click CRMSFA
And Click Leads
And Click Find Leads Link
And In Find Leads, Enter First Name <first name>
And Click Find Leads Button
And Click First Matching Lead
And Click Delete Lead Button

Examples:
|first name  |
|Sethu       |
|Babu        |