package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.qameta.allure.Allure;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract Reporter class for test execution reporting
 * Integrates Extent Reports and Allure for comprehensive test reporting
 * Uses ThreadLocal pattern to support parallel test execution
 * 
 * Status Levels:
 * - PASS: Test step passed (includes screenshot)
 * - FAIL: Test step failed (includes screenshot, throws exception)
 * - INFO: Informational message (no screenshot)
 * - WARN: Warning message (includes screenshot)
 * 
 * @author Baskar
 */
public abstract class Reporter {
	public static ExtentReports extent;
	private static Map<RemoteWebDriver,ExtentTest> testDriver;


	public void reportStep(String desc, String status) {

		long snapNumber = 100000l;
		String screenshotPath = null;
		
		if(!status.equalsIgnoreCase("INFO")) {
			try {
				snapNumber= takeSnap();
				screenshotPath = "images/" + snapNumber + ".jpg";
				byte[] screenshot = captureScreen();
				Allure.addAttachment(desc,new ByteArrayInputStream(screenshot));
			} catch (Exception e) {
				// Screenshot capture failed - log to console but don't fail test
				System.out.println("Failed to capture screenshot: " + e.getMessage());
			}
		}

		// Check if testDriver and ExtentTest are initialized
		if(testDriver == null || getDriver() == null || testDriver.get(getDriver()) == null) {
			System.out.println("[" + status + "] " + desc);
			return;
		}

		// Write log with inline screenshot
		try {
			if(status.equalsIgnoreCase("PASS")){
				if(screenshotPath != null) {
					testDriver.get(getDriver()).log(Status.PASS, desc, 
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
				} else {
					testDriver.get(getDriver()).log(Status.PASS, desc);
				}
			}else if(status.equalsIgnoreCase("FAIL")){
				if(screenshotPath != null) {
					testDriver.get(getDriver()).log(Status.FAIL, desc, 
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
				} else {
					testDriver.get(getDriver()).log(Status.FAIL, desc);
				}
				throw new RuntimeException("FAILED");
			}else if(status.equalsIgnoreCase("INFO")){
				testDriver.get(getDriver()).log(Status.INFO, desc);
			}else if(status.equalsIgnoreCase("WARN")){
				if(screenshotPath != null) {
					testDriver.get(getDriver()).log(Status.WARNING, desc, 
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
				} else {
					testDriver.get(getDriver()).log(Status.WARNING, desc);
				}
			}
		} catch (Exception e) {
			if(!status.equalsIgnoreCase("FAIL")) {
				System.out.println("Error in reportStep: " + e.getMessage());
			} else {
				throw new RuntimeException("FAILED");
			}
		}
	}

	/**
	 * Take screenshot and save to file
	 * @return Random number used as screenshot filename
	 */
	public abstract long takeSnap();
	
	/**
	 * Capture screenshot as byte array for Allure
	 * @return Screenshot as byte array
	 */
	public abstract byte[] captureScreen();

	/**
	 * Initialize Extent Reports
	 * @return ExtentReports instance
	 */
	public ExtentReports startResult(){
		// Initialize testDriver map only once
		if (testDriver == null) {
			testDriver = new HashMap<RemoteWebDriver, ExtentTest>();
		}
		
		// Initialize ExtentReports only once
		if (extent == null) {
			ExtentSparkReporter sparkReporter = new ExtentSparkReporter("./target/reports/extent-report/result.html");
			sparkReporter.config().setTheme(Theme.STANDARD);
			sparkReporter.config().setDocumentTitle("Automation Test Report");
			sparkReporter.config().setReportName("Test Execution Report");
			extent = new ExtentReports();
			extent.attachReporter(sparkReporter);
		}
		return extent;
	}

	public synchronized ExtentTest startTestCase(String testCaseName, String testDescription){
		// Ensure testDriver is initialized
		if (testDriver == null) {
			testDriver = new HashMap<RemoteWebDriver, ExtentTest>();
		}
		return testDriver.put(getDriver(),extent.createTest(testCaseName, testDescription));
	}

	public void endResult(){		
		extent.flush();
	}

	public void endTestcase(){
		// In ExtentReports 5.x, tests are automatically ended when flush() is called
		// No explicit endTest() method needed
	}


	public abstract RemoteWebDriver getDriver();


}