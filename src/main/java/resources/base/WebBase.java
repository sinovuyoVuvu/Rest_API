package resources.base;



import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.sql.rowset.WebRowSet;
import org.apache.commons.io.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import utils.FileFolderUtils;
import utils.JdbcUtils;
import utils.ReportingUtils;
import utils.SettingsUtils;

import org.openqa.selenium.By;

public class WebBase extends ReportingUtils{

	
	public static WebDriver driver;// Create global level driver object to use for chromeBrowser, firefoxBrowser or IE										
	private static final int WAITTIMEOUT = 3; // Declare time for global wait		
	final static String SettingsFile = ".//src//main//java//config//Settings.xml";
	public static WebRowSet rWebRowKeyValueOutput;

	/*
	 * Initialise constructor to create directory for reporting and Initialise OR
	 * properties file
	 */
	public WebBase() {
		
	
		
		try {
			
//			System.out.println("Intitialised WebBase [WebBase ln76] ");
			
			SettingsUtils.initSettings();			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void reporting_Failure(String reportType, String text) {

		switch (reportType) {

		case "ObjectNotFound":
			childTest.log(Status.FAIL, MarkupHelper.createLabel("Object Not Found" + text, ExtentColor.RED));
			break;
		case "validateText":
			System.out.println("Reporting Failure for validateText");
			break;
		case "Click":
			System.out.println("Click failed");
			break;
		case "input":
			System.out.println("input failed");
			break;
		case "validateTextvalue":
			System.out.println("Validation Failed");
			break;
		case "switchtoFrame":
			System.out.println("Frame not found");
			break;
		case "isRadioButtonSelected":
			System.out.println("Radio Button is not selected");
			break;
		case "selectFromDropdown":
			System.out.println("Radio Button is not selected");
			break;
		case "validateValueFromDropDown":
			childTest.log(Status.FAIL,
					MarkupHelper.createLabel(
							"Expected value ==  " + text + "  does not match from any of actual values in drop down.",
							ExtentColor.GREEN));
			break;
		case "validateTitle":
			System.out.println("ValidateTitle Failed");
			break;

		}
//After executing any of above case
//1.Take screenshot
		String temp = WebBase.CaptureScreenshot("test");
		try {
			childTest.addScreenCaptureFromPath(temp);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 2.Setting up component status as Fail so that can abort further steps from
		// execution
		rWebRowKeyValueOutput = JdbcUtils.setKeyValuePair("ComponentStatus", "Fail", rWebRowKeyValueOutput); // Setting up ComponentStatus column with dummy ComponentName as in 1stRow

		// 3. Updating Test Case status in QC
		try {
			rWebRowKeyValueOutput.next();
			// TODO Replace below cache.getproperty with output table by adding another
			// column in output table
			System.out.println(rWebRowKeyValueOutput.getString("TestCaseName"));
			System.out.println(rWebRowKeyValueOutput.getString("TestID"));
			ReportingUtils.QcExecution(rWebRowKeyValueOutput.getString("TestCaseName"), rWebRowKeyValueOutput.getString("TestID"),
					"Failed");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		extent.flush();
	}
	
	/**
	 * This will initialise the webdriver class 
	 * 
	 * @return Webdriver
	 * @throws IOException
	 * 
	 */
	public static WebDriver initialiseDriver() throws IOException {

		// Craig Adam
		SettingsUtils.initSettings();
		String browserName = SettingsUtils.getTagNameValue("testBrowser");

		if (browserName.toLowerCase().equals("chrome")) {
			ChromeOptions cLocalOptions = new ChromeOptions();
			cLocalOptions.setAcceptInsecureCerts(true);
			System.out.println(SettingsUtils.getBrowserLocation());
			System.setProperty("webdriver.chrome.driver", FileFolderUtils.convertRelToAbsPath(SettingsUtils.getBrowserLocation()));
			driver = new ChromeDriver(cLocalOptions); // New chromeDriver with merged local options
		}

		// FIREFOX
		else if (browserName.equals("firefox")) {
			// execute firefoxBrowser
			driver = new FirefoxDriver();

		}

		// IE
		else if (browserName.equals("ie")) {
			// execute ieBrowser
		}

		// DRIVER TIMEOUT
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// Report
		logName.info("driver is initialized");

		// Return webDriver object
		return driver;

	}

	/*
	 * Method Name :: openBrowser Description :: TO Launch the Browser. Input
	 * Parameters:: testname, table. Output Parameter::"".
	 */

	public String openBrowser(String testname, Hashtable<String, String> table)
			throws AWTException, InterruptedException {// Open Browser
		String result = "Pass";
		try {
			SettingsUtils.initSettings();
//			String browserName = SettingsUtils.getTagNameValue("testBrowser");
			ChromeOptions cLocalOptions = new ChromeOptions();
			cLocalOptions.setAcceptInsecureCerts(true);
			System.setProperty("webdriver.chrome.driver", FileFolderUtils.convertRelToAbsPath(SettingsUtils.getBrowserLocation()));
			driver = new ChromeDriver(cLocalOptions);
			System.out.println(SettingsUtils.getUrl());
			driver.get(SettingsUtils.getUrl());
			childTest.log(Status.INFO, MarkupHelper.createLabel("Browser Launched sucessfully", ExtentColor.INDIGO));
			result = "Pass";
		}

		catch (Exception e) {
			childTest.log(Status.FAIL, MarkupHelper.createLabel("Failed to launch the browser" + e, ExtentColor.RED));
			result = "Fail";
		}
		return result;

	}


	/*
	 * validateText is used to validate the runtime values Input Parameters
	 * (webElement,expected value from input data)
	 * 
	 */
	public static void validateText(WebElement webElement, String expectedText) throws Exception {
		if (rWebRowKeyValueOutput.getString("ComponentStatus").equals("Pass")) { // Execute only if Component status is
																					// till Pass. Was marked as fail if
																					// object was not found, so it won't
																					// execute if object for this step
																					// was not found

			try {

				String actualText = webElement.getText();
				if (actualText.trim().equals(expectedText.trim())) {
					childTest.log(Status.PASS, MarkupHelper.createLabel("Validation Pass --Actual result--" + actualText
							+ " Equal To Expected result--" + expectedText, ExtentColor.GREEN));
					extent.flush();
				} else {
					childTest.log(Status.PASS, MarkupHelper.createLabel("Validation Pass --Actual result--" + actualText
							+ " Equal To Expected result--" + expectedText, ExtentColor.GREEN));
					reporting_Failure("validateText", "null");
				}

			} catch (Exception e) {

			}
		}
	}

	/*
	 * validateTextvalue is used to validate the actual value to expected Text value
	 * Input Parameters (String actualText,String expectedText)
	 */
	public static void validateTextvalue(String actualText, String expectedText) throws Exception {
		if (rWebRowKeyValueOutput.getString("ComponentStatus").equals("Pass")) { // Execute only if Component status is
																					// till Pass. Was marked as fail if
																					// object was not found, so it won't
																					// execute if object for this step
																					// was not found
			try {
				if ((actualText.trim()).equals(expectedText.trim())) {
					childTest.log(Status.PASS, MarkupHelper.createLabel("Validation Pass --Actual result--" + actualText
							+ " Equal To Expected result--" + expectedText, ExtentColor.GREEN));
					extent.flush();
				} else {
					childTest
							.log(Status.FAIL,
									MarkupHelper.createLabel(
											"Validation Fail --Actual result--" + actualText
													+ " NOT Equal To Expected result--" + expectedText,
											ExtentColor.RED));
					reporting_Failure("validateTextvalue", "null");
				}
			} catch (Exception e) {
				childTest.log(Status.FAIL, MarkupHelper.createLabel("Unable to validate ", ExtentColor.RED));
				reporting_Failure("validateTextvalue", "null");
			}
		}
	}



	/*
	 * getText is to return the runtime value; Input Parameters webElement;
	 */
	public static String getText(WebElement webElement) {

		try {
			if (rWebRowKeyValueOutput.getString("ComponentStatus").equals("Pass")) { // Execute only if Component status
																						// is till Pass. Was marked as
																						// fail if object was not found,
																						// so it won't execute if object
																						// for this step was not found
				String result = "";
				result = webElement.getText();
				return result;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



	/*
	 * closewindow is to close the window Input Parameters (String actualText,String
	 * expectedText)
	 */
	public String closewindow() {
		try {
			driver.close();
			return "Pass";
		} catch (Exception e) {
			return "Fail";
		}
	}

	/*
	 * closewindow is to close the window Input Parameters (String actualText,String
	 * expectedText)
	 */
	public String getAttribute(WebElement webElement, String attribute) throws Exception {
		try {
			webElement.getAttribute(attribute);
		} catch (Exception e) {
			String temp = WebBase.CaptureScreenshot("test");
			childTest.addScreenCaptureFromPath(temp);
			childTest.log(Status.FAIL, MarkupHelper.createLabel("Unable to validate ", ExtentColor.RED));
			extent.flush();
			Assert.fail("Validate Text Value failed - " + e.getMessage());
			return "Had error while switching to default frame" + e;
		}
		return "Pass";
	}

	/*
	 * 
	 * public static void checkDisplayed(WebElement WebElement, String
	 * DescriptionObject) throws InterruptedException {
	 * 
	 * // Craig Adam if (WebElement.isDisplayed()) {
	 * 
	 * outlineElement(WebElement, driver);
	 * 
	 * }
	 * 
	 * assertTrue(WebElement.isDisplayed(), "Checkpoint : " + DescriptionObject +
	 * " object is displayed");
	 * 
	 * }
	 */

	/*
	 * switchtoDefaultFrame is to switch to default frame
	 */
	public static String switchtoDefaultFrame() {
		try {
			if (JdbcUtils.doProceed(rWebRowKeyValueOutput).equals("Yes")) {
				driver.switchTo().defaultContent();
				return "Pass";
			}
			;

		} catch (Exception e) {
			return "Had error while switching to default frame" + e;
		}
		return "Fail";
	}

	// get text method
	public static String getText(By locator) {
		String result = "";
		try {
			result = driver.findElement(locator).getText();
			System.out.println("text retrieved was:" + result);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("unable to get text");

		}
		return result;
	}

	/*
	 * switchtoParentFrame is to switch to parent frame
	 */
	public static String switchtoParentFrame() throws Exception {
		try {
			driver.switchTo().parentFrame();
		} catch (Exception e) {

			return "Had error while switching to default frame" + e;
		}
		return "Pass";
	}

	/*
	 * switchwindow is to switch between the windows
	 */
	public static void switchwindow() {
		for (String winHandle : driver.getWindowHandles()) {

			String winHandleBeforel = driver.getWindowHandle();

			if ((!winHandle.equalsIgnoreCase(winHandleBeforel))) {
//				boolean str = driver.switchTo().activeElement().isDisplayed();
				driver.switchTo().window(winHandle);
				driver.manage().window().maximize();
				// break;
			}
		}

	}

	/*
	 * switchback is to switch back to the window
	 */
	public String switchback() {
		Set<String> allWindows = driver.getWindowHandles();

		for (String curWindow : allWindows) {

			driver.switchTo().window(curWindow);
			driver.manage().window().maximize();
		}

		return "Pass";

	}

	/*
	 * validateTitle is to validate the title of the window Input Parameter:
	 * expectedTitleKey
	 */
	public void validateTitle(String expectedTitle) throws Exception {
		if (rWebRowKeyValueOutput.getString("ComponentStatus").equals("Pass")) { // Execute only if Component status is
																					// till Pass. Was marked as fail if
																					// object was not found, so it won't
																					// execute if object for this step
																					// was not found
			try {

				// String expectedTitle = OR.getProperty(expectedTitleKey);
				String actualTitle = driver.getTitle();

				if (expectedTitle.equals(actualTitle)) {
					childTest
							.log(Status.PASS,
									MarkupHelper.createLabel(
											"validateTitle Pass --Actual result--" + actualTitle
													+ " Equal To Expected result--" + expectedTitle,
											ExtentColor.GREEN));
					extent.flush();
				} else {
					childTest
							.log(Status.FAIL,
									MarkupHelper.createLabel(
											"Validation Fail --Actual result--" + actualTitle
													+ " NOT Equal To Expected result--" + expectedTitle,
											ExtentColor.RED));
					reporting_Failure("validateTitle", "text");
				}
			} catch (Exception e) {

				childTest.log(Status.FAIL,
						MarkupHelper.createLabel("Validate Title failed due to " + e.getMessage(), ExtentColor.RED));
				extent.flush();
				reporting_Failure("validateTitle", "text");
				// QcExecution(testname,cache.getProperty("TestID"),"Failed");
			}
		}
	}
	
	/*
	 * ImplicitlyWait is to wait for particular time
	 */
	public String ImplicitlyWait() {
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		return "Pass";
	}

	/*
	 * Capture Screenshot
	 */
	public static String CaptureScreenshot(String screenshotName) {
		try {
			String dateName = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date());
			TakesScreenshot ts = (TakesScreenshot) driver;
			File srcFile = ts.getScreenshotAs(OutputType.FILE);
			String dest = System.getProperty("user.dir") + "//artefacts//" + Reportingdirectory + screenshotName
					+ "_" + dateName + ".png";
			FileUtils.copyFile(srcFile, new File(dest));
			return dest;
		} catch (Throwable t) {
			t.getMessage();
		}
		return "Pass";
	}
	
	

	/*
	 * Method Name :: isRadioButtonSelected Description :: TO check if RadioButton
	 * is selected. Input Parameters:: webElement of Type WebElement.
	 */
	public static void isRadioButtonSelected(WebElement webElement, String objectname) { //
		try {
			if (rWebRowKeyValueOutput.getString("ComponentStatus").equals("Pass")) { // Execute only if Component status
																						// is till Pass. Was marked as
																						// fail if object was not found,
																						// so it won't execute if object
																						// for this step was not found
				try {
					boolean result = webElement.isSelected();

					if (result == true) {
						childTest.log(Status.INFO,
								MarkupHelper.createLabel(objectname + "  is selected ", ExtentColor.INDIGO));
					} else {
						childTest.log(Status.INFO,
								MarkupHelper.createLabel(objectname + "  is not selected ", ExtentColor.INDIGO));
						reporting_Failure("isRadioButtonSelected", objectname);
					}

					extent.flush();

				} catch (Exception e) {
					childTest.log(Status.INFO, MarkupHelper.createLabel(
							"Selecting radiobutton failed " + objectname + " Failed--" + e, ExtentColor.RED));
					reporting_Failure("isRadioButtonSelected", "null");
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@SuppressWarnings("unused")
	private static void reportCheckpoint(String StepName) {

		// Craig Adam
		// Placeholder for generic method
		// This is only for example can pass any values or none and hardcode inside this
		// method

		// do other stuff here

		//

		// Report step
		Map<String, String> mCriteria = new HashMap<String, String>();
		// Add a info / checkpoint
		mCriteria.put("name", StepName);
		mCriteria.put("actual", "This was the actual value found"); // Optional empty if not given.
		mCriteria.put("expected", "This was the expected value found"
				+ DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss", Locale.ENGLISH).format(LocalDateTime.now())); // Optional
																													// empty
																													// if
																													// not
																													// given.
		mCriteria.put("status", "Passed"); // Optional empty if not given. "Passed", "Failed", "N/A"
		ArrayTestSteps.add(mCriteria);
	}

	@SuppressWarnings("unused")
	private static void reportInput(String StepName) {

		// Craig Adam
		// Placeholder for generic method
		// This is only for example can pass any values or none and hardcode inside this
		// method

		// do other stuff here

		//

		// Report step
		Map<String, String> mCriteria = new HashMap<String, String>();
		// Add a input
		mCriteria.put("name", StepName);
		mCriteria.put("actual", "Value to input"); // Optional empty if not given.
		mCriteria.put("expected", "Actual field value after input"
				+ DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss", Locale.ENGLISH).format(LocalDateTime.now())); // Optional
																													// empty
																													// if
																													// not
																													// given.
		mCriteria.put("status", "Passed"); // Optional empty if not given. "Passed", "Failed", "N/A"
		ArrayTestSteps.add(mCriteria);

	}

	@SuppressWarnings("unused")
	private static void reportFailure(String StepName, String StepArtifactFilePath) {

		// Craig Adam
		// Placeholder for generic method
		// This is only for example can pass any values or none and hardcode inside this
		// method

		// do other stuff here

		//

		// Report step
		Map<String, String> mCriteria = new HashMap<String, String>();
		// Add a input
		mCriteria.put("name", StepName);
		mCriteria.put("actual", "Expected value"); // Optional empty if not given.
		mCriteria.put("expected", "Actual value"
				+ DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss", Locale.ENGLISH).format(LocalDateTime.now())); // Optional
																													// empty
																													// if
																													// not
																													// given.
		mCriteria.put("status", "Failed"); // Optional empty if not given. "Passed", "Failed", "N/A"

		if (StepArtifactFilePath != null) {
			mCriteria.put("artifact-path", StepArtifactFilePath); // Optional empty if not given. "Passed", "Failed",
																	// "N/A"
		}

		ArrayTestSteps.add(mCriteria);

	}

	/*
	 * Method Name :: click Description :: TO click Object. Input Parameters::
	 * webElement of Type WebElement.
	 */
	public static void click(WebElement webElement, String objectname) { //
		try {
			if (rWebRowKeyValueOutput.getString("ComponentStatus").equals("Pass")) { // Execute only if Component status
																						// is till Pass. Was marked as
																						// fail if object was not found,
																						// so it won't execute if object
																						// for this step was not found
				try {
					webElement.click();
					Thread.sleep(1000);
					childTest.log(Status.INFO,
							MarkupHelper.createLabel("Clicked on " + objectname, ExtentColor.INDIGO));
					extent.flush();

				} catch (Exception e) {
					childTest.log(Status.INFO,
							MarkupHelper.createLabel("Click on " + objectname + "Failed--" + e, ExtentColor.RED));
					reporting_Failure("Click", "null");
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Method Name :: input Description :: TO input Parameters in text field. It
	 * doesnot clear the field before entering the value. Input Parameters::
	 * webElement of Type WebElement ,input text of Type String.
	 */

	public static void input(WebElement webElement, String inputtext, String inputfield) throws Exception {
		if (rWebRowKeyValueOutput.getString("ComponentStatus").equals("Pass")) {
			try {

				// if (JDBCSupport.doProceed(rWebRowKeyValueOutput).equals("Yes")){
				webElement.clear();
				webElement.sendKeys(inputtext);
				childTest.log(Status.INFO, MarkupHelper.createLabel(
						"Entered the " + inputtext + " in " + inputfield + " Succesfully", ExtentColor.INDIGO));
				extent.flush();
				// }

			} catch (Exception e) {
				childTest.log(Status.FAIL, MarkupHelper.createLabel(
						"Unable to enter the text " + inputtext + " in " + inputfield + " field due to issue " + e,
						ExtentColor.RED));
				reporting_Failure("input", "null");
			}
		}
	}

	/*
	 * Method Name :: selectFromDropdown Description :: TO select value from
	 * dropdown. Input Parameters:: webElement of Type WebElement ,object of Type
	 * String. Output Parameter::"".
	 */
	public static void selectFromDropdown(WebElement webElement, String object) throws Exception {
		if (rWebRowKeyValueOutput.getString("ComponentStatus").equals("Pass")) {
			try {

				Select Select1 = new Select(webElement);
				Select1.selectByVisibleText(object);
				childTest.log(Status.INFO,
						MarkupHelper.createLabel("Value selected from the drop down is " + object, ExtentColor.INDIGO));
				extent.flush();
			} catch (Exception e) {
				reporting_Failure("selectFromDropdown", "test");
			}
		}
	}

	/*
	 * Method Name :: validateValueFromDropDown Description :: TO validate value
	 * from dropdown, It will get size of all the values of dropdown and iterate
	 * through untill it finds expected. Input Parameters:: webElement of Type
	 * WebElement ,expected of Type String. Output Parameter::"".
	 */
	public static void validateValueFromDropDown(WebElement webElement, String expected) throws Exception {
		if (rWebRowKeyValueOutput.getString("ComponentStatus").equals("Pass")) {
			try {
				Select Select1 = new Select(webElement);
				// Get all the values from RHS and validates
				for (int i = 0; i <= Select1.getOptions().size() - 1; i++) {
					String Actualtext = Select1.getOptions().get(i).getText();
					if ((Actualtext.trim()).equals(expected.trim())) {
						childTest.log(Status.PASS,
								MarkupHelper.createLabel(
										"Expected value ==  " + expected + " matched actual value == " + Actualtext,
										ExtentColor.GREEN));
						extent.flush();
						break;

					}
					reporting_Failure("validateValueFromDropDown", expected);
				}

			} catch (Exception e) {
				reporting_Failure("validateValueFromDropDown", "test");
			}
		}
	}

	/*
	 * Method Name :: switchtoFrame Description :: TO switch between the Frames.
	 * Input Parameters:: name of the Frame. Output Parameter::"".
	 */
	public static String switchtoFrame(String result, String framename) throws Exception {
		try {
			if (result.equals("Pass")) {
				childTest.log(Status.INFO,
						MarkupHelper.createLabel("Switched to frame  " + framename, ExtentColor.INDIGO));
			} else {
				childTest.log(Status.INFO,
						MarkupHelper.createLabel("Unable to Switch to frame  " + framename, ExtentColor.RED));
				extent.flush();
			}

		} catch (Exception e) {

			String temp = WebBase.CaptureScreenshot("test");
			childTest.addScreenCaptureFromPath(temp);
		}
		return "Pass";

	}

	/*
	 * getScreenshot is to get the screenshot of the page
	 * 
	 */
	public void getScreenshot(String testDescr) throws IOException {
		// Build unique suffix for destination file name
		String uniqueSuffix = "AfterTest " + Integer.toString(LocalDateTime.now().getHour()) + "-"
				+ Integer.toString(LocalDateTime.now().getMinute()) + "-"
				+ Integer.toString(LocalDateTime.now().getSecond()) + " " + LocalDate.now();
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE); // Store screenshot in variable
																					// srcFile
		File destFile = new File("./logs" + "//" + "screenshot" + " - " + uniqueSuffix + ".png"); // Destination File
																									// destFile
		FileUtils.copyFile(srcFile, destFile); // Copy source to destination

	}

	// Dynamic wait for the elements
	public static WebDriver getDriver() {
		return driver;
	}

	public static void pause(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static String WaitForElement(By by, String objectName) throws Exception {
		if (rWebRowKeyValueOutput.getString("ComponentStatus").equals("Pass")) {
			System.out.println(objectName + " entered WaitForElement");
			boolean elementFound = false;
			int counter = 0;
			try {
				while (!elementFound && counter < WAITTIMEOUT) {
					try {

						WebDriverWait wait = new WebDriverWait(getDriver(), 1);
						wait.until(ExpectedConditions.presenceOfElementLocated(by));
						if (wait.until(ExpectedConditions.presenceOfElementLocated(by)) != null) {
							elementFound = true;
							System.out.println(" Found element by Xpath : " + by.toString());
							return "Pass";
						}
					} catch (Exception e) {

						elementFound = false;
						counter++;
						System.out.println(counter);

					}
					pause(1000);
				}
				reporting_Failure("ObjectNotFound", objectName); // Replace
				System.out.println("Element not found");

				// return elementFound;
			} catch (Exception ex) {
				System.out.println("[ERROR] -in wait for Element " + ex.getMessage());
			}
			return "Fail";
		}
		return "Fail";
	}

	public static String WaitForFrame(String framename, String objectName) throws Exception {
		if (rWebRowKeyValueOutput.getString("ComponentStatus").equals("Pass")) {
			System.out.println("Entered the WaitForFrame " + framename);
			boolean elementFound = false;
			int counter = 0;
			try {
				while (!elementFound && counter < WAITTIMEOUT) {
					try {
						WebDriverWait wait = new WebDriverWait(getDriver(), 1);
						// wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(framename));//Commented
						// becoz once we switched to frame in above loc,and try again will through error
						if (wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(framename)) != null) {
							elementFound = true;
							System.out.println(" Found Frame : " + framename.toString());
							return framename;
							// break;
						}
					} catch (Exception e) {

						elementFound = false;
						counter++;
						System.out.println(counter);

					}
					pause(1000);
				}
				System.out.println(framename + "Frame not found");

				// return elementFound;
			} catch (Exception ex) {
				System.out.println("[ERROR] -in wait for frame " + ex.getMessage());

				// return false;
			}
			return framename;
		}
		return framename;

	}

	/*
	 * Method Name :: switchtoFrame Description :: TO switch between the Frames.
	 * Input Parameters:: name of the Frame. Output Parameter::"".
	 */
	public static void switchtoFrame(String frames) throws Exception {
		if (rWebRowKeyValueOutput.getString("ComponentStatus").equals("Pass")) {
			try {
				String[] frame = frames.split("@");
				WebDriverWait waitforframe = new WebDriverWait(getDriver(), 10);

				for (int i = 0; i <= frame.length - 1; i++) {
					waitforframe.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame[i]));
					Thread.sleep(2000);
					System.out.println("Frame found" + frame[i]);

				}
				// System.out.println("Switching to frame "+a);

			} catch (Exception e) {
				reporting_Failure("switchtoFrame", "null");
				System.out.println("Unable to switch to frame" + e);
			}
		}
	}

}
