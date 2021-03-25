package resources.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import coreDrivers.WebBase;



 

//login objects
public class PageObjects extends WebBase {
	
		


	// This is how WebElement have to be written as below example
	 public static WebElement lnk_Login_signout() throws Exception {
		 String sLocator = "//span[contains(text(),'Log Out')]"; 
		 try {		
			 WebBase.WaitForElement(By.xpath(sLocator),"lnk_Login_signout"); 
			 return driver.findElement(By.xpath(sLocator));		    	
	    }catch (Exception e) {
	    	 return null;	
	 	 }		
		 
	    }
	 
  
     
	    }
	 

