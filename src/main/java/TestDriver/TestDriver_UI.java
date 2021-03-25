package TestDriver;

import java.io.IOException;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import coreDrivers.ComponentDriver;
import coreDrivers.ReportingUtils;



/**
 * 
 * Test driver Method names can be any name The name of the TestCase should be
 * same as ExcelSheet and the same as the TestCase in Quality Centre The name of
 * the TestCase should not contain spaces " " The name of the TestCase should
 * not contain period "."
 * 
 * @author Pankaj Sharma
 *
 */
public class TestDriver_UI  {

	private static String testCaseNameQC;
	private static String dataTableName;
	private static String testId;
	private static String testPhase;
        
	ComponentDriver report = new ComponentDriver();    //This will initialise Constructor of WebBase class which will initilaise settings file
	
	   public TestDriver_UI () throws IOException {
           ComponentDriver.cleanResultDirectory();
           ReportingUtils.setup();
     }
     
	
	
	   @Test
		  public static void AccesCard_Deductions() throws Exception {
		  dataTableName  = "AccesCard_Deductions";
		  testCaseNameQC = "AccesCard_Deductions"; 
		  testId = "2310";
		  ComponentDriver.driver(dataTableName,testCaseNameQC, testId);
		  
		  }
	   
	   
	   @Test(priority = 1)
		  public static void Year25LongService() throws Exception {
		  dataTableName  = "Year25LongService";
		  testCaseNameQC = "Year25LongService"; 
		  testId = "2311";
		  ComponentDriver.driver(dataTableName,testCaseNameQC, testId);
		  
		  }
	   
	   

	   @Test(priority = 2)
		  public static void Baby_Birth_Certificate() throws Exception {
		  dataTableName  = "Baby_Birth_Certificate";
		  testCaseNameQC = "Baby_Birth_Certificate"; 
		  testId = "2312";
		  ComponentDriver.driver(dataTableName,testCaseNameQC, testId);
		  
		  }
	   
	   
		  	
	// (System.getProperty("user.dir") + "\\src\\main\\java\\HRSS_Files\\Access Card deduction  Candice Liebenberg #950146 .msg")
	  
	 
//	@AfterSuite
//    public void OpenReport() {
//          if(SettingsUtils.getTagNameValue("DisplayReport").equalsIgnoreCase("ON")) {
//                 DisplayReport();  
//          }
//          
//	}

}
