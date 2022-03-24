package pages;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import wrappers.LeafTapsWrappers;

public class Hooks extends LeafTapsWrappers{
	
	@Before
	public void launchBrowser(Scenario sc) throws InterruptedException {
		invokeApp(sBrowser);
		startTestCase(sc.getName(), sc.getId());
	}
	
	@After
	public void executeAfterScenario() {
		quitBrowser();
	}
}
