package runner;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
//import org.junit.runner.RunWith;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
//import cucumber.api.junit.Cucumber;
import wrappers.LeafTapsWrappers;

//@RunWith(Cucumber.class)

@CucumberOptions(
//        tags can be used while debugging/need basis
        tags = ("@smoke"),
        features = {"src/main/java/features/IntelliSense.feature"},
                glue 	  = {"pages"},
        plugin = {
        "summary",
        "pretty" ,
        "html:target/reports/cucumber-report/cucumber-pretty/",
        "json:target/reports/cucumber-report/CucumberTestReport.json"
},
        monochrome = true)

public class TestNgRunner extends LeafTapsWrappers{

    private TestNGCucumberRunner testNGCucumberRunner;

    @BeforeClass(alwaysRun = true)
    public void setUpClass() throws Exception {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @Test(groups = "cucumber", description = "Runs Cucumber Feature", dataProvider = "scenarios")
    public void scenario(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) throws Throwable {
        testNGCucumberRunner.runScenario(pickleWrapper.getPickle());
    }

    @DataProvider(name = "scenarios",parallel=false)
    public Object[][] features() {
        return testNGCucumberRunner.provideScenarios();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() throws Exception {
        testNGCucumberRunner.finish();
    }
}
