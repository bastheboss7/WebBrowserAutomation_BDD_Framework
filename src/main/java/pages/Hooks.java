package pages;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import wrappers.SkyWrappers;

public class Hooks extends SkyWrappers{

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
}
