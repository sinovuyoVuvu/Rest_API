//Test
package components;

import static io.restassured.RestAssured.given;

import java.awt.Component;
import java.io.File;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.sql.rowset.WebRowSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import io.restassured.response.Response;
import utils.JdbcUtils;
import utils.ReportingUtils;
import utils.SapUtils;
import utils.SettingsUtils;
import utils.StringUtils;

public class Components_BI extends SapUtils {

	public static Logger logName = LogManager.getLogger(Component.class.getName());

	// TODO can this be deleted??
	String testcompname;

	// Start time of the test case
	LocalDateTime timeStart = LocalDateTime.now();
	
	
	
	Response rsp;
	
	
	
	public void Sending_Request(String testname, WebRowSet table) throws Exception {
		
		
			
			// Send the request and getting back the response
			 rsp = 	given().multiPart("file", new File(table.getString("attachment")))
		        .expect().statusCode(200).when()
		        .put("http://content-services-tst.sanlam.co.za/content/v1/classification/decide/SLM_HR_EMAIL");
			 
			 System.out.println(rsp);
			
		
	}
	
	

	
	

}
