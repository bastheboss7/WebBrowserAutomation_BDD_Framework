package wrappers;

import org.openqa.selenium.remote.RemoteWebDriver;


public interface Wrappers {

		
		/**
		 * This method will launch the given browser and maximize the browser and set the
		 * wait for 30 seconds and load the url
		 * @author Baskar
		 * @param browser - The browser of the application to be launched
		 * 
		 */
		RemoteWebDriver invokeApp(String browser);

		/**
		 * This method will enter the value to the text field using id attribute to locate
		 * 
		 * @param idValue - id of the webelement
		 * @param data - The data to be sent to the webelement
		 * @author Baskar
		 */
	/**
		 * This method will close all the browsers
		 * @author Baskar
		 */
	void quitBrowser();
		

}
