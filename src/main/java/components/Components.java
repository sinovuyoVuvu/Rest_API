//Test
package components;


import static io.restassured.RestAssured.given;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.sql.rowset.WebRowSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import coreDrivers.WebBase;
import io.restassured.response.Response;
import resources.base.ApplicationBase;
import resources.pageObjects.*;
//import rowSetHelper.JDBCSupport;
import utils.FileFolderUtils;
import utils.JdbcUtils;
import utils.SettingsUtils;

//import APItest.jira;

public class Components extends WebBase {
//testing git
	//Test
	

		private static final String String = null;
		public static Logger logName = LogManager.getLogger(Component.class.getName());
		String testcompname;
		
		
		
		
		public static Response rsp;
		public static String Response_Path = "";
		public static String Expected_Path = "";
		
		int statusCode = 0;
		
		
		
		
  public void Sending_Request(String testname, WebRowSet table) throws Exception {
			
			
			
			// Send the request and getting back the response
			 rsp = 	given().multiPart("file", new File(System.getProperty("user.dir") + table.getString("attachment")))
		        .expect().statusCode(200).when()
		        .put("http://content-services-tst.sanlam.co.za/content/v1/classification/decide/SLM_HR_EMAIL");
			 
			 System.out.println(rsp.asString());
			 
			statusCode = rsp.getStatusCode();
			
			if(statusCode == 200) {
				
				childTest.log(Status.PASS, MarkupHelper.createLabel("Successfully got the response back-- The status code is: " + statusCode, ExtentColor.GREEN));
			}else {
				
				childTest.log(Status.FAIL, MarkupHelper.createLabel("Failed to get the response back -- The status code is: " + statusCode, ExtentColor.RED));
				extent.flush();
		        rWebRowKeyValueOutput=  JdbcUtils.setKeyValuePair("ComponentStatus", "Fail", rWebRowKeyValueOutput);
		        rWebRowKeyValueOutput.next();
							
			}
		}
		
		
		
		
		
		

  public void Saving_Response_In_JsonFile(String testname, WebRowSet table) throws IOException, Exception {
			
			
			
			// Saving the response is a json file
	           Expected_Path = System.getProperty("user.dir")+  table.getString("ExpectedJsonFile");
			   Response_Path = System.getProperty("user.dir") + table.getString("JsonResponseFile");
				JSONObject countryObj = new JSONObject(rsp.asString());  
				File file=new File(Response_Path);  
			    file.createNewFile();  
			    FileWriter fileWriter = new FileWriter(file); 
			    fileWriter.write(countryObj.toString());  
			    fileWriter.flush();  
			    fileWriter.close();
			    
			    childTest.info("<a href='"+Response_Path+"'>Click to view the JSON Response</a>");
			    childTest.info("<a href='"+Expected_Path+"'>Click to view the Expected JSON</a>");
				
			}
		
		
		
		
		
		
   public void Comparing_Response_With_Expected(String testname, WebRowSet table) throws Exception ,FileNotFoundException, IOException, ParseException {
			
	// When we parse JSON, it means we are converting the string into a JSON object by following the specification, where we can subsequently use in whatever way we want.
	   
	
	// Getting values from the response that is saved in the json file
	   JSONParser parser = new JSONParser();
	   Object obj = parser.parse(new FileReader(Response_Path));
	   JSONObject jsonObject = new JSONObject(obj.toString());
	   String WorkRequestType_Response = jsonObject.optString("WorkRequestType");
	   String TaskTypeDescription_Response = jsonObject.optString("TaskTypeDescription");
	   
	   
    // Getting values from the expected json file
	   Expected_Path = System.getProperty("user.dir")+  table.getString("ExpectedJsonFile");
	   Object obj1 = parser.parse(new FileReader(Expected_Path));
	   JSONObject jsonObject1 = new JSONObject(obj1.toString());
	   String WorkRequestType_Expected = jsonObject1.optString("WorkRequestType");
	   String TaskTypeDescription_Expeted = jsonObject1.optString("TaskTypeDescription");
	   
	   
	   
	  // Comparing the values extracted from the json response file to the expected json file
	   if (WorkRequestType_Response.equals(WorkRequestType_Expected)) {
		   
		   childTest.log(Status.PASS, MarkupHelper.createLabel("Actual work request type from the response: " + WorkRequestType_Response + "Equals to expected: " + WorkRequestType_Expected, ExtentColor.GREEN));
		   
	   }else {
		   
		   childTest.log(Status.FAIL, MarkupHelper.createLabel("Actual work request type from the response: " + WorkRequestType_Response + "Not equals to expected: " + WorkRequestType_Expected, ExtentColor.RED));
		   extent.flush();
           rWebRowKeyValueOutput=  JdbcUtils.setKeyValuePair("ComponentStatus", "Fail", rWebRowKeyValueOutput);
           rWebRowKeyValueOutput.next();
	   }

	     
		
	   if (TaskTypeDescription_Response.equals(TaskTypeDescription_Expeted)) {
		   
		   childTest.log(Status.PASS, MarkupHelper.createLabel("Actual task type description from the response: " + TaskTypeDescription_Response + "Equals to expected: " + TaskTypeDescription_Expeted, ExtentColor.GREEN));
		   
	   }else {
		   
		   childTest.log(Status.FAIL, MarkupHelper.createLabel("Actual work request type from the response: " + TaskTypeDescription_Response + "Not equals to expected: " + TaskTypeDescription_Expeted, ExtentColor.RED));
		   extent.flush();
           rWebRowKeyValueOutput=  JdbcUtils.setKeyValuePair("ComponentStatus", "Fail", rWebRowKeyValueOutput);
           rWebRowKeyValueOutput.next();
	   }
			
		}
		
		
		
		
		

	 
	}
                

