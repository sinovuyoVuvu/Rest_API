package utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.apache.logging.log4j.LogManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import alm.qc.restapi.ExternalTests;
import alm.qc.restapi.Instances;
import resources.base.WebBase;

public class ReportingUtils  {

	public static String Reportingdirectorynew = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()); // To save results																																																										 
	public static String Reportingdirectory = Reportingdirectorynew;
	public static ArrayList<Map<String, String>> ArrayTestSteps = new ArrayList<Map<String, String>>();	
	private final static String SettingsFile = ".//src//main//java//config//Settings.xml";
	public final static String ArtefactsFolder = System.getProperty("user.dir") + "\\artefacts\\" + Reportingdirectorynew;
	
	 //TODO remove hard code of artefacts folder
//	public static String ArtefactsFolder = null;
//	ArtefactsFolder = FilePathUtils.convertRelToAbsPath(SettingsUtils.getTagNameValue("artifactFolder")) + "//" + Reportingdirectorynew;	
	public static ExtentHtmlReporter htmlReports;
	public static ExtentReports extent;
	public static ExtentTest parentTest;
	public static ExtentTest childTest;
	
	
	
	
//	String filename1 = System.getProperty("user.dir") + "//artefacts//";
 	public static Logger logName = LogManager.getLogger(WebBase.class.getName());// Log4J interface
 	

	/*
	 * Method Name :: setup Description :: TO initialise the Extent report and
	 * update the system info.
	 */
	
	public static void setup() {
		System.out.println("setup Initialised");
		
		// System.out.println("[ReportingUtils Ln 163] Reportingdirectory = " + WebBase.Reportingdirectory);
	//	if(extent  !=null) {
		
		String filename = FileFolderUtils.convertRelToAbsPath(SettingsUtils.getTagNameValue("artifactFolder")) + "//";
		htmlReports = new ExtentHtmlReporter(filename + WebBase.Reportingdirectory + "//ExtentReport//Report.html");
		htmlReports.loadXMLConfig(System.getProperty("user.dir") + "/ReportsConfig.xml");
		htmlReports.config().setAutoCreateRelativePathMedia(true);
		extent = new ExtentReports();
		extent.attachReporter(htmlReports);
		extent.setSystemInfo("User Name", "Automation User");
		extent.setSystemInfo("OS", "Windows 10");
		extent.setSystemInfo("Environment", "QA");
		extent.setSystemInfo("Browser", "Chrome");
		extent.setSystemInfo("Machine", "LAP06589");
	//	}
		
		
	}
	/**
	 * Displays the report after the execution is done. For now it displays in Chrome specific Browser
	 * 
	 * @author Pankaj Sharma
	 * @modified 12-02-2020
	 * @return Void
	 * 
	 **/		
	public static void DisplayReport() {
		WebDriver driver;		
		ChromeOptions cLocalOptions = new ChromeOptions();
		cLocalOptions.setAcceptInsecureCerts(true);
		System.setProperty("webdriver.chrome.driver", FileFolderUtils.convertRelToAbsPath(SettingsUtils.getBrowserLocation()));
		driver = new ChromeDriver(cLocalOptions);
		System.out.println(SettingsUtils.getUrl());
		driver.get(System.getProperty("user.dir")+"\\artefacts\\"+Reportingdirectory+"\\ExtentReport\\Report.html");
	}
	
 	
	public static void QcExecution(String TestName, String TestID, String status) throws Exception {
		
//		String testID = "testID";
		String sSettingsFilePath = SettingsFile;
		
		


		// TODO why 2nd variable?
		String sArtefactsFilePath = ArtefactsFolder;
		
		
		
		
//		System.out.println("[ReportingUtils Ln 54] sArtefactsFilePath = " + sArtefactsFilePath);
		
		String sTestSetName = SettingsUtils.getTagNameValue("qcTestSuite");
	
		String sTestName = TestName;
		String TestStatus = status; 		
		String sReportFilePath = System.getProperty("user.dir")+"\\artefacts\\" + Reportingdirectory + "\\ExtentReport\\Report.html";		
	  
//		System.out.println("[ReportingUtils Ln 63] sReportFilePath = " + sReportFilePath);
		
		
		Map<String, String> mTestRunCriteria = new HashMap<String, String>();
	    mTestRunCriteria.put("SettingsFilePath", sSettingsFilePath); // MANDATORY 
	    mTestRunCriteria.put("ArtefactsFilePath", sArtefactsFilePath); // MANDATORY
	    mTestRunCriteria.put("TestSetName", sTestSetName); // MANDATORY 
	    mTestRunCriteria.put("TestName", sTestName); // MANDATORY needs to be unique test name in Test Plan	    
	    mTestRunCriteria.put("TestId", TestID); //mandatory
   //   mTestRunCriteria.put("parent-id", "1002");    // Optional Folder id that contains the test. This may be required to ensure unique match of test in test plan to link to. ie if there are 2 different tests with the same name then include the id of the parent folder.  This combination has to be unique in QC.  	    
	    mTestRunCriteria.put("status", TestStatus); // MANDATORY 
	    mTestRunCriteria.put("execution-date", LocalDate.now().toString()); // Optional eg format : LocalDate.now().toString();
	    mTestRunCriteria.put("execution-time", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))); // Optional eg format : LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")); 
	    mTestRunCriteria.put("duration", "5"); // Optional  
	    mTestRunCriteria.put("results-files-network-path", sReportFilePath);    // Optional  
	    
	  
	   ExternalTests.uploadTestRun(mTestRunCriteria, ArrayTestSteps);
	    
	    
//	    System.out.println("[ReportingUtils Ln 141] Run");
	    
	    
	}
	
	
	@SuppressWarnings("unused")
	private static void reportClick(String ObjectName) {

		// Craig Adam
		// Placeholder for generic method
		// This is only for example can pass any values or none and hardcode inside this
		// method

		// do other stuff here

		//

		// Report step
		Map<String, String> mCriteria = new HashMap<String, String>();
		mCriteria.put("name", "Click");
		mCriteria.put("description", "clicked object : " + ObjectName); // Optional empty if not given
		ArrayTestSteps.add(mCriteria);

	}

	
	@SuppressWarnings("unused")
	private static void reportStart(String StepName) {

		// Craig Adam
		// Placeholder for generic method
		// This is only for example can pass any values or none and hardcode inside this
		// method

		Map<String, String> mCriteria = new HashMap<String, String>();
		mCriteria.put("name", StepName);
		mCriteria.put("execution-date",
				DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH).format(LocalDateTime.now())); // Optional will
																										// default to
																										// current date
																										// if empty
		mCriteria.put("execution-time",
				DateTimeFormatter.ofPattern("HH-mm-ss", Locale.ENGLISH).format(LocalDateTime.now())); // Optional will
																										// default to
																										// current time
																										// if empty
		ArrayTestSteps.add(mCriteria);

	}

	
	
	public String GetFolderDetails() throws Exception {
		// Craig Adam
		// used to get id of parent folder (in test plan)
		String SettingsFile = ".//src//main//java//config//Settings.xml";
		Map<String, String> mTestRunCriteria = new HashMap<String, String>();
		mTestRunCriteria.put("SettingsFilePath", SettingsFile); // MANDATORY
		// Settings
		SettingsUtils.initSettings();
		// Login
		Instances.connectionLogin();
		// Print test folder details

		// Logout
		Instances.connectionLogout();
		System.out.println("[ReportingUtils Ln 150] End of print test folder details Example - Craig "
				+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
		return SettingsFile;
	}


	
	
	public static void moveTxtToQcReportingFolder(String testname, String testPhase) {
       
		String folderNameAfter = null;
		String folderNameBefore = null;
		String folderLocationAfter = null;
		String folderLocationBefore = null;

		String beforeComponentName = null;

		File fileLastModifiedBefore = null;
		File fileLastModifiedAfter = null;

		String outputFileFolder = SettingsUtils.getTagNameValue("artifactFolderDB");
		childTest.log(Status.INFO, MarkupHelper.createLabel("outputFileFolder is "+outputFileFolder, ExtentColor.INDIGO));
		String locationReportingFolder = FileFolderUtils.convertRelToAbsPath(SettingsUtils.getTagNameValue("artifactFolder") + "//" + Reportingdirectory + "//" + "Output");
		childTest.log(Status.INFO, MarkupHelper.createLabel("locationReportingFolder is "+locationReportingFolder, ExtentColor.INDIGO));
		//String testPhase = JdbcUtils.getRowSetValueAsString("testPhase", inputDataSheet);
		
		childTest.log(Status.INFO, MarkupHelper.createLabel("testPhase is "+testPhase, ExtentColor.INDIGO));
		
		folderNameAfter = "After";
		folderNameBefore = "Before";

		if (testPhase.toLowerCase().contentEquals("before")) {

			beforeComponentName = testname;

		}

		// Copy after file
		if (testPhase.toLowerCase().contentEquals("after")) {

			beforeComponentName = testname.replace(folderNameAfter, "") + folderNameBefore;

			try {

				// After

				folderLocationAfter = FileFolderUtils.convertRelToAbsPath((outputFileFolder + "//" + folderNameAfter));
				fileLastModifiedAfter = FileFolderUtils.findLastModified(folderLocationAfter, testname, ".txt");

				

				// After
				

//				System.out.println("[SapUtils ln 1214] folderLocationAfter = " + folderLocationAfter);
//				System.out.println("[SapUtils ln 1215] locationDestinationFolder = " + locationDestinationFolder);

				String sourceFile = fileLastModifiedAfter.getAbsolutePath();
//				String destinationFile = FilePathUtils.convertRelToAbsPath(locationReportingFolder + "//" + fileLastModifiedAfter.getName());
				String destinationFile = FileFolderUtils.convertRelToAbsPath(locationReportingFolder + "//" + "After.txt");
				FileFolderUtils.copyFolderContent(sourceFile,destinationFile);

//				// Before
//				DirectoryUtils.copyFolderContent(folderLocationBefore + "//" + fileLastModifiedBefore.getName(),
//						FilePathUtils.convertRelToAbsPath(locationReportingFolderRoot + Reportingdirectory + fileLastModifiedBefore.getName()));

				// DirectoryUtils.copyFolderContent(System.getProperty("user.dir") +
				// "\\artefactDB\\Before\\ClouderaIntegrationBefore_ResultSet_12022020_101540.txt",
				// System.getProperty("user.dir") +
				// "\\artefacts\\"+Reportingdirectory+"\\Output\\ClouderaIntegrationBefore_ResultSet_12022020_101540.txt");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// Copy Before file
		if (testPhase.toLowerCase().contentEquals("before") || testPhase.toLowerCase().contentEquals("after")) {

			// Before
			folderLocationBefore = FileFolderUtils.convertRelToAbsPath((outputFileFolder + "//" + folderNameBefore));

			fileLastModifiedBefore = FileFolderUtils.findLastModified(folderLocationBefore, beforeComponentName,
					".txt");

			try {

				

				// Before
				String sourceFile = fileLastModifiedBefore.getAbsolutePath();
//				String destinationFile = FilePathUtils.convertRelToAbsPath(locationReportingFolder + "//" + fileLastModifiedBefore.getName());
				String destinationFile = FileFolderUtils.convertRelToAbsPath(locationReportingFolder + "//" + "Before.txt");
				FileFolderUtils.copyFolderContent(sourceFile,destinationFile);

				// DirectoryUtils.copyFolderContent(System.getProperty("user.dir") +
				// "\\artefactDB\\Before\\ClouderaIntegrationBefore_ResultSet_12022020_101540.txt",
				// System.getProperty("user.dir") +
				// "\\artefacts\\"+Reportingdirectory+"\\Output\\ClouderaIntegrationBefore_ResultSet_12022020_101540.txt");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		childTest.log(Status.INFO, MarkupHelper.createLabel("Moved the file to upload to QC", ExtentColor.INDIGO));
	}
	
	
}
