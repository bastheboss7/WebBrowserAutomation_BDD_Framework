@Babu
Feature: Create Lead

@smoke 
Scenario Outline: Create Lead Using Mandatory Fields(Positive)
When Click CRMSFA
And Click Leads
And Click Create Lead Link
And Enter Company Name <company name>
And Enter First Name <first name>
And Enter Last Name <last name>
And Click Create Lead Button
Then Verify the First Name <first name>

Examples:
|company name	|first name  |last name  |
|TestLeaf       |Sethu       |Mathavan   |

#@sanity
#Scenario Outline: Create Lead Using Mandatory + Optional Fields(Positive)
#When Click CRMSFA
#And Click Leads
#And Click Create Lead Link
#And Enter Company Name <company name>
#And Enter First Name <first name>
#And Enter Last Name <last name>
#And Select Source <data source>
#And Select Marketing Campaign <marketing campaign>
#And Enter Phone Number <phone>
#And Click Create Lead Button
#Then Verify the First Name <first name>
#
#Examples:
#|company name	|first name  |last name  |data source |marketing campaign|phone      |
#|TestLeaf       |Babu        |Mathavan   |Conference  |Automobile        |9234834838 |
