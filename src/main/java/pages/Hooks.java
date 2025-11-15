package pages;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import wrappers.BasePageObject;

public class Hooks extends BasePageObject{

	@BeforeAll
	public static void setupExtentReports() {
		// Initialize ExtentReports once for all scenarios
		BasePageObject instance = new BasePageObject() {};
		instance.startResult();
	}

	@Before
	public void launchBrowser(Scenario sc) {
		invokeApp(sBrowser);
		startTestCase(sc.getName(), sc.getId());
	}
	
	@After
	public void executeAfterScenario(Scenario scenario) {
		if (scenario.isFailed()) {
			scenario.attach(captureScreen(),"image/png",scenario.getName());
		}
		quitBrowser();
	}
	
	@AfterAll
	public static void teardownExtentReports() {
		// Flush Extent Reports once after all scenarios
		if (extent != null) {
			extent.flush();
		}
	}
}
