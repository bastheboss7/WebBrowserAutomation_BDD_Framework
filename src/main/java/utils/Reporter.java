package utils;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.qameta.allure.Allure;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class Reporter {
	public static ExtentReports extent;
	private static Map<RemoteWebDriver,ExtentTest> testDriver;


	public void reportStep(String desc, String status) {

		long snapNumber = 100000l;
		if(!status.equalsIgnoreCase("INFO")) {
			try {
				snapNumber= takeSnap();
				byte[] screenshot = captureScreen();
				Allure.addAttachment(desc,new ByteArrayInputStream(screenshot));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Write if it is successful or failure or information
		if(status.equalsIgnoreCase("PASS")){
			testDriver.get(getDriver()).log(LogStatus.PASS, desc+testDriver.get(getDriver()).addScreenCapture("images/"+snapNumber+".jpg"));
		}else if(status.equalsIgnoreCase("FAIL")){
			testDriver.get(getDriver()).log(LogStatus.FAIL, desc+testDriver.get(getDriver()).addScreenCapture("images/"+snapNumber+".jpg"));
			throw new RuntimeException("FAILED");
		}else if(status.equalsIgnoreCase("INFO")){
			testDriver.get(getDriver()).log(LogStatus.INFO, desc);
		}else if(status.equalsIgnoreCase("WARN")){
			testDriver.get(getDriver()).log(LogStatus.WARNING, desc+testDriver.get(getDriver()).addScreenCapture("images/"+snapNumber+".jpg"));
		}
	}

	public abstract long takeSnap();
	public abstract byte[] captureScreen();


	public ExtentReports startResult(){
		testDriver = new HashMap<RemoteWebDriver, ExtentTest>();
		extent = new ExtentReports("./target/reports/extent-report/result.html", true);
		extent.loadConfig(new File("src/main/resources/extent-config.xml"));
		return extent;
	}

	public synchronized ExtentTest startTestCase(String testCaseName, String testDescription){
		return testDriver.put(getDriver(),extent.startTest(testCaseName, testDescription));
	}

	public void endResult(){		
		extent.flush();
	}

	public void endTestcase(){
		extent.endTest(testDriver.get(getDriver()));
	}


	public abstract RemoteWebDriver getDriver();


}