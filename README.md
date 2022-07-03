# CucumberFramework
This framework has been designed as Maven project with BDD approach using Cucumber & TestNG.

# How to use this framework 
-Clone the git hub link to your IntelliJ

-Perform mvn clean install Or Right click on the project root folder -> Maven -> Reload projects

-Execute mvn clean verify -Dsurefire.suiteXmlFiles=testngParallel.xml or Configure testngParallel.xml to run as TestNG suite.


# Extent Reporting

Extent reports can be found at, target/reports/extent-reports (Mac)

This gives screenshot for each and every step of test. 

HTML report can be found at /reports/result.html

# Cucumber Reporting

Cucumber reports can be found at, target/reports/cucumber-report/cucumber-html-reports

Please note that, cucumber report can be generated thorugh command line run only.

Both the reporting files have been uploaded to git for the reference.

# Allure Reporting

Allure report can be found at, target/reports/allure-report

# Parallel Run

On the file, src/main/java/runner/TestNgRunner.java:

In the snippet below,

@DataProvider(name = "scenarios",parallel=false)
    public Object[][] features() {
        return testNGCucumberRunner.provideScenarios();
    }
    
Make 'parallel=true'to enable parallel run up to 10 browsers at a time.

# Improvements:

Android/iOS drivers can be added to enable the framework compatible with Mobile automation as well. 
