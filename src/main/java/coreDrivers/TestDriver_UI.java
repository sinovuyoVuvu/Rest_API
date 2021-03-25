package coreDrivers;

import org.testng.annotations.Test;

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
public class TestDriver_UI extends ComponentDriver {

	private static String testCaseNameQC;
	private static String dataTableName;
	private static String testId;
//	private static String testPhase;

	 
	
	
//	  @Test
//	  public static void Login_Sucessful() throws Exception {
//	  
//	  testCaseName = "Login_Sucessful"; testId = "1";
//	  ComponentDriver.driver(testCaseName, testId);
//	  
//	  }
	  
	   @Test(priority=0)
		  public static void Login_Sucessful() throws Exception {
		  dataTableName  = "Login_Sucessful";
		  testCaseNameQC = "LoginSucessfultoClinePortal"; 
		  testId = "1";
		  ComponentDriver.driver(dataTableName,testCaseNameQC, testId);
		  
		  }

	  
	/*
	 * @Test(priority = 1) public static void Selecting_Incident() throws Exception
	 * {
	 * 
	 * testCaseName = "Selecting_Incident"; testId = "24";
	 * ComponentDriver.driver(testCaseName, testId);
	 * 
	 * }
	 */
	  
	/*
	 * @Test(priority = 0) public static void SCHEMA_NAME_on_TABLES_Before() throws
	 * Exception { testCaseName = "ViewingQuickProfile"; testId = "4";
	 * ComponentDriver.driver(testCaseName, testId);
	 * 
	 * }
	 * 
	 * @Test(priority = 1) public static void SCHEMA_NAME_on_TABLES_After() throws
	 * Exception { testCaseName = "CreatingNewIncident"; testId = "5";
	 * ComponentDriver.driver(testCaseName, testId);
	 * 
	 * } // //
	 * 
	 * @Test(priority = 0) public static void PRIV_on_GRANTED_PRIV_Before() throws
	 * Exception {
	 * 
	 * testCaseName = "Checkingincedent"; testId = "6";
	 * ComponentDriver.driver(testCaseName, testId);
	 * 
	 * } //
	 * 
	 * @Test(priority = 1) public static void PRIV_on_GRANTED_PRIV_After() throws
	 * Exception {
	 * 
	 * testCaseName = "CheckingSLA_AttachingDoc"; testId = "28";
	 * ComponentDriver.driver(testCaseName, testId);
	 * 
	 * 
	 * 
	 * }
	 * 
	 * 
	 * @Test(priority = 0) public static void ROLE_NAME_on_GRNTD_ROLES_Before()
	 * throws Exception {
	 * 
	 * testCaseName = "Adding_Comment"; testId = "29";
	 * ComponentDriver.driver(testCaseName, testId);
	 * 
	 * }
	 * 
	 * @Test(priority = 1) public static void ROLE_NAME_on_GRNTD_ROLES_After()
	 * throws Exception {
	 * 
	 * testCaseName = "TransferingIncident"; testId = "30";
	 * ComponentDriver.driver(testCaseName, testId);
	 * 
	 * }
	 * 
	 * @Test(priority = 0) public static void Cnt_OBJECT_TYPE_on_OBJS_Before()
	 * throws Exception {
	 * 
	 * testCaseName = "Updating_Status"; testId = "31";
	 * ComponentDriver.driver(testCaseName, testId);
	 * 
	 * }
	 * 
	 * @Test(priority = 1) public static void Cnt_OBJECT_TYPE_on_OBJS_After() throws
	 * Exception {
	 * 
	 * testCaseName = "Closing_Incident"; testId = "32";
	 * ComponentDriver.driver(testCaseName, testId);
	 * 
	 * }
	 * 
	 * @Test(priority = 0) public static void SCHEMA_NAME_on_OBJECTS_Before() throws
	 * Exception {
	 * 
	 * testCaseName = "AdministrationInterface"; testId = "58";
	 * ComponentDriver.driver(testCaseName, testId);
	 * 
	 * }
	 * 
	 * @Test(priority = 1) public static void SCHEMA_NAME_on_OBJECTS_After() throws
	 * Exception {
	 * 
	 * testCaseName = "Runpdmstatus"; testId = "46";
	 * ComponentDriver.driver(testCaseName, testId);
	 * 
	 * }
	 * 
	 * @Test(priority = 0) public static void Cnt_SchNmeObjType_on_OBJ_Before()
	 * throws Exception {
	 * 
	 * testCaseName = "APISVCDeskManager"; testId = "43";
	 * ComponentDriver.driver(testCaseName, testId);
	 * 
	 * }
	 * 
	 * @Test(priority = 1) public static void Cnt_SchNmeObjType_on_OBJ_After()
	 * throws Exception {
	 * 
	 * testCaseName = "APISVCDeskManagerSRV004781"; testId = "59";
	 * ComponentDriver.driver(testCaseName, testId);
	 * 
	 * }
	 * 
	 * @Test(priority = 0) public static void Teradata_Integration_Before() throws
	 * Exception {
	 * 
	 * testCaseName = "Runpdmwebstatus"; testId = "60";
	 * ComponentDriver.driver(testCaseName, testId);
	 * 
	 * }
	 */
	  
//	@AfterSuite
//    public void OpenReport() {
//          if(SettingsUtils.getTagNameValue("DisplayReport").equalsIgnoreCase("ON")) {
//                 DisplayReport();  
//          }
//          
//	}

}
