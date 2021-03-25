package coreDrivers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.sql.rowset.WebRowSet;

import org.testng.Assert;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import utils.ExcelUtils;
import utils.FileFolderUtils;
import utils.JdbcUtils;

import utils.ReflectionSupportUtils;
import utils.SettingsUtils;




public class ComponentDriver  extends WebBase  {
	
	public static String Reportingdirectorynew = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()); // To Save Results
	public static String Reportingdirectory = Reportingdirectorynew;	
	public static final String sKeyHeader = "Object";
	public static final String sValueHeader = "ParameterValue";
	public static final String sComponentNameHeader = "Component";
	
	// Create the object of the excel sheet
	ExcelUtils xlsx = new ExcelUtils(System.getProperty("user.dir") + "\\src\\main\\java\\resources\\inputData\\"
			+ SettingsUtils.getTagNameValue("ProjectName") + ".xlsx");	
	// public static final String sExcelFilePath =   PathUtils.convertRelToAbsPath(".//src//main//java//resources//inputData//" +SettingsUtils.getTagNameValue("ProjectName")+ ".xlsx");
	 
	
	 public ComponentDriver() {
			ReportingUtils.setup();
		}
	
	 
		public static void cleanResultDirectory() throws IOException {		 
			FileFolderUtils.copyFolderContent(System.getProperty("user.dir") + "\\artefacts\\",
					System.getProperty("user.dir") + "\\artefactsPreviousRun\\");
			FileFolderUtils.deleteDirectoryContents(System.getProperty("user.dir") + "\\artefacts\\");
		}
	 

	 
	 public void testing() {
			System.out.println();
		}
	// Get the list of the test cases from KeywordSummary ExcelSheet
	
	  public static void driver(String TestCasenames,String testCaseNameQC, String TestIDs) throws Exception {
		  
			try {
				
				System.out.println("****************EXECUTING TEST CASE***********"+TestCasenames +"********** [component driver ln 57]");
				System.out.println("");				
				String sExcelFilePath = FileFolderUtils.convertRelToAbsPath(".//src//main//java//resources//inputData//" +SettingsUtils.getTagNameValue("ProjectName")+ ".xlsx");
				String TestCasename = TestCasenames;
				String TestID = TestIDs;
				rWebRowKeyValueOutput = JdbcUtils.setKeyValuePair("TestCaseName", "TestCase",
						rWebRowKeyValueOutput); // Setting up TestCase column
				rWebRowKeyValueOutput = JdbcUtils.setKeyValuePair("TestCaseNameQC", testCaseNameQC,
						rWebRowKeyValueOutput); // Setting up TestCase column
				
				rWebRowKeyValueOutput = JdbcUtils.setKeyValuePair("TestCaseStatus", "Pass", rWebRowKeyValueOutput); // Setting
																																// up
																																// TestCase
																																// column
				rWebRowKeyValueOutput = JdbcUtils.setKeyValuePair("ComponentName", "ComponentName",
						rWebRowKeyValueOutput); // Setting up ComponentName column with dummy ComponentName as in 1st Row
				rWebRowKeyValueOutput = JdbcUtils.setKeyValuePair("ComponentStatus", "Pass",
						rWebRowKeyValueOutput); // Setting up ComponentName column with dummy ComponentName as in 1st Row
				rWebRowKeyValueOutput = JdbcUtils.setKeyValuePair("TestID", "TestID", rWebRowKeyValueOutput); // Setting
																														// up
																														// TestCase
																														// column
				rWebRowKeyValueOutput = JdbcUtils.setKeyValuePair("TestCaseName", TestCasename,
						rWebRowKeyValueOutput); // Setting up TestCase column
				rWebRowKeyValueOutput = JdbcUtils.setKeyValuePair("TestCaseStatus", "Pass", rWebRowKeyValueOutput); // Setting
																																// up
																																// TestCase
																																// Status
				rWebRowKeyValueOutput = JdbcUtils.setKeyValuePair("TestID", TestID, rWebRowKeyValueOutput); // Setting
																														// up
																														// TestCase
																														// column
				rWebRowKeyValueOutput = JdbcUtils.setKeyValuePair("ComponentStatus", "Pass",
						rWebRowKeyValueOutput); // Setting up ComponentName column with dummy ComponentName as in 1st Column
				parentTest = extent.createTest("Test Case  - " + TestCasename); // For Extent Report
				WebRowSet testCaseWebRowSet = JdbcUtils.createKeyValueRowSetFromExcel(sComponentNameHeader,
						sKeyHeader, sValueHeader, TestCasename, sExcelFilePath);
				
				
//				System.out.println("Component driver ln 87");
//				PrintUtils.printObject(testCaseWebRowSet);
				
				// Extract list of components to run from the test case WebRowSet
				ArrayList<String> lComponents = JdbcUtils.getComponentList(testCaseWebRowSet);
				// Get list of components to iterate through
				// for (int rownum = 2; rownum <= xlsx.getRowCount(TestCasename); rownum++) { //
				// Iterate through Testcase Sheet
				for (int iCompIndex = 0; iCompIndex <= lComponents.size() - 1; iCompIndex++) { // Number of componenets
																								// to be iterated
//					System.out.println("Component index [ComponentDriver ln 92] : " + iCompIndex);
//					System.out.println("Component name [ComponentDriver ln 93] : " + lComponents.get(iCompIndex));
//					System.out.println("Component size [ComponentDriver ln 92]" + lComponents.size());

					String ComponentName = lComponents.get(iCompIndex); // Iterate through to get component name
					rWebRowKeyValueOutput = JdbcUtils.setKeyValuePair("ComponentName", ComponentName,
							rWebRowKeyValueOutput); // Setting up ComponentName column with dummy ComponentName as in
													// 1st Row
					rWebRowKeyValueOutput.beforeFirst();
					rWebRowKeyValueOutput.next(); // Getting position of the cursor to 1st position i.e 1st Row

					if ((ComponentName.length() > 0)
							&& (rWebRowKeyValueOutput.getString("ComponentStatus").equals("Pass"))) { // check if component status of Previous compnent is still pass
																									
						// WebRowSet testCaseWebRowSet =
						// ReadExcel_V7_reflection.createRowSetExcelExample(TestCasename);

// iRowCount = testCaseWebRowSet.size();

						// Get the test Data for component

						WebRowSet filteredRowset = JdbcUtils.filteredComponentData(ComponentName,
								testCaseWebRowSet);
						
//						System.out.println("Component Driver ln 121");
//						JdbcUtils.print(filteredRowset);
						
						System.out.println("[Component Driver ln 126] " + filteredRowset.size());
						filteredRowset.beforeFirst(); // Will move the cursor before 1st row

						System.out.println("[Component Driver ln 129] Value of NewColumn1 = " + rWebRowKeyValueOutput.getString("ComponentName")); // Print
																															// the
																															// value
																															// of
																															// the
																															// column
						// System.out.println("Value of NewColumn2 = " +
						// JDBCSupport.getKeyValuePair("NewColumn2", rWebRowKeyValueOutput));

						// Below for loop is to iterate through components with multiple set of data
						for (int j = 1; j <= filteredRowset.size(); j++) {
							
							//TODO this should come from a settings node Yes / No discuss
							
							String sQualifiedClassName = "components." + "Components";
							ArrayList<Object> lArgs = new ArrayList<Object>();
							lArgs.add(TestCasename);
							lArgs.add(filteredRowset);
							filteredRowset.next();
							// JDBCSupport.print(rWebRowKeyValueOutput);

							if (JdbcUtils.doIterate(filteredRowset).equals("Yes")) {
								System.out.println("Iteration number " + j);
								childTest = parentTest.createNode("Component -" + ComponentName);
								try {
									ReflectionSupportUtils.invokeMethodGeneric(
											ComponentName.substring(0, ComponentName.length() - 3), sQualifiedClassName, lArgs);
								}catch (Exception e) {
									
									//Mark Component Status as Fail as Component is not found
									childTest.log(Status.FAIL, MarkupHelper
											.createLabel("Component " + ComponentName + " Not found "+e, ExtentColor.RED));
									extent.flush();
									rWebRowKeyValueOutput = JdbcUtils.setKeyValuePair("ComponentStatus", "Fail", rWebRowKeyValueOutput); // Setting up ComponentStatus column with dummy ComponentName as in 1stRow
									rWebRowKeyValueOutput.next();
									System.out.println(e);
								}
								// Invoke the method
								
								if (rWebRowKeyValueOutput.getString("ComponentStatus").equals("Pass")) {
									childTest.log(Status.PASS, MarkupHelper
											.createLabel("Component " + ComponentName + " Passed ", ExtentColor.GREEN));
									extent.flush();
									//if ((iCompIndex==lComponents.size()-1) && testPhase.equals("Before") || testPhase.equals("After") ) {
									if ((iCompIndex==lComponents.size()-1)) {
								//		ReflectionSupportUtils.invokeMethodGeneric(
									//			"moveTxtToQcReportingFolder", sQualifiedClassName, lArgs);
										//ReportingUtils.moveTxtToQcReportingFolder(TestCasename,testPhase );
										
										ReportingUtils.QcExecution(testCaseNameQC, TestID, "Passed");
									}
									
								} else {
									
									childTest.log(Status.FAIL, MarkupHelper
											.createLabel("Component " + ComponentName + " Failed ", ExtentColor.RED));
									extent.flush();
					//				ReflectionSupportUtils.invokeMethodGeneric(
						//					"moveTxtToQcReportingFolder", sQualifiedClassName, lArgs);
									//ReportingUtils.moveTxtToQcReportingFolder(TestCasename,filteredRowset );
									ReportingUtils.QcExecution(testCaseNameQC, TestID, "Failed");
									extent.flush();
									Assert.fail();
								}
								//String temp = WebBase.CaptureScreenshot("test");
							//	childTest.addScreenCaptureFromPath(temp);
								extent.flush();
							}
						}
					}
					extent.flush();
				}
			} catch (Exception e) {
               System.out.println(e);
			}
		}
		}

