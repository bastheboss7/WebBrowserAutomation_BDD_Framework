# CucumberFramework
This framework has been designed as Maven project with BDD approach using Cucumber & TestNG.

# How to use this framework 
-Clone the git hub link to your IntelliJ

-Perform mvn clean install Or Right click on the project root folder -> Maven -> Reload projects

-Execute mvn clean verify -Dsurefire.suiteXmlFiles=testngParallel.xml or Configure testngParallel.xml to run as TestNG suite.


# Extent Reporting

Extent reports can be found at, /Users/**/***/CucumberFramework/reports (Mac)

This gives screenshot for each and every step of test. 

HTML report can be found at /reports/result.html

# Cucumber Reporting

Cucumber reports can be found at, target/cucumber-reports/cucumber-html-reports

Please note that, cucumber report can be generated thorugh command line run only.

Both the reporting files have been uploaded to git for the reference.
