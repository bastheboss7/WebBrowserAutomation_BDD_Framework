package utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public abstract class Reporter {
	public static ExtentReports extent;
	public String testCaseName, testDescription, category, authors;
	private static Map<RemoteWebDriver,ExtentTest> testDriver;

	public void reportStep(String desc, String status) {

		long snapNumber = 100000l;
		if(!status.toUpperCase().equals("INFO")) {
			try {
				snapNumber= takeSnap();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Write if it is successful or failure or information
		if(status.toUpperCase().equals("PASS")){
			testDriver.get(getDriver()).log(LogStatus.PASS, desc+testDriver.get(getDriver()).addScreenCapture("./../reports/images/"+snapNumber+".jpg"));
		}else if(status.toUpperCase().equals("FAIL")){
			testDriver.get(getDriver()).log(LogStatus.FAIL, desc+testDriver.get(getDriver()).addScreenCapture("./../reports/images/"+snapNumber+".jpg"));
			throw new RuntimeException("FAILED");
		}else if(status.toUpperCase().equals("INFO")){
			testDriver.get(getDriver()).log(LogStatus.INFO, desc);
		}else if(status.toUpperCase().equals("WARN")){
			testDriver.get(getDriver()).log(LogStatus.WARNING, desc+testDriver.get(getDriver()).addScreenCapture("./../reports/images/"+snapNumber+".jpg"));
		}
	}

	public abstract long takeSnap();


	public ExtentReports startResult(){
		testDriver = new HashMap<RemoteWebDriver, ExtentTest>();
		extent = new ExtentReports("./reports/result.html", true);
		extent.loadConfig(new File("./extent.xml"));
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