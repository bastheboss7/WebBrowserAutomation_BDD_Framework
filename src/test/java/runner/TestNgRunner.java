package runner;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import wrappers.BasePageObject;

/**
 * TestNG runner for Cucumber BDD tests
 * Enables parallel execution of scenarios using TestNG DataProvider
 * Integrates Cucumber features with TestNG test framework
 * 
 * Report outputs:
 * - Cucumber HTML: target/reports/cucumber-report/cucumber-pretty/
 * - Cucumber JSON: target/reports/cucumber-report/CucumberTestReport.json
 * - Allure: target/allure-results/
 * 
 * Tag filtering examples (uncomment to use):
 * - tags = "@ProhibitedItems"              // Run only prohibited items tests
 * - tags = "@ParcelShopFilter"             // Run only parcel shop filter tests
 * - tags = "@Validation"                   // Run all validation tests
 * - tags = "@Evri"                         // Run all Evri tests
 * - tags = "@ProhibitedItems or @ParcelShopFilter"  // Run either tag
 * - tags = "@Evri and @Validation"         // Run tests with both tags
 * - tags = "not @ParcelShopFilter"         // Exclude parcel shop tests
 * 
 * @author Baskar
 */
@CucumberOptions(
//        tags = "@ParcelShopFilter",  // Uncomment and modify to filter scenarios by tags
        features = {"src/main/java/features"},
        glue = {"pages", "runner", "models", "utils", "wrappers"},
        plugin = {
        "summary",
        "pretty" ,
        "html:target/reports/cucumber-report/cucumber-pretty/",
        "json:target/reports/cucumber-report/CucumberTestReport.json",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
},
        monochrome = true)

public class TestNgRunner extends BasePageObject {

    private TestNGCucumberRunner testNGCucumberRunner;

    @BeforeClass(alwaysRun = true)
    public void setUpClass() throws Exception {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @Test(groups = "cucumber", description = "Runs Cucumber Feature", dataProvider = "scenarios")
    public void scenario(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) throws Throwable {
        testNGCucumberRunner.runScenario(pickleWrapper.getPickle());
    }

    @DataProvider(name = "scenarios", parallel = false)
    public Object[][] features() {
        return testNGCucumberRunner.provideScenarios();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() throws Exception {
        testNGCucumberRunner.finish();
    }
}
