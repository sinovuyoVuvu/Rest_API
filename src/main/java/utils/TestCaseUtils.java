package utils;

import java.util.Hashtable;

public class TestCaseUtils {
	
	public static Object[][] getdata(String testcase,ExcelUtils xls){
				
		//
		
		int totalrows=xls.getRowCount(testcase);
	//	System.out.println("Total number of rows "+totalrows);
		
		//Object table using Array
		Object testData[][] = new Object[1][1];
//		int index = 0;
		
		Hashtable<String,String> table = null;
		
		
		int dataStartRowNum = 2;
		table = new Hashtable<String,String>(); //Init for every row
		// Extract Data
		for(int rNum = dataStartRowNum;rNum<=totalrows;rNum++){
							
				String key   = xls.getCellData(testcase,"Object",rNum);
				String value = xls.getCellData(testcase,"ParameterValue",rNum);
				
				System.out.println(key);	
				System.out.println(value);
				//Fill the Data in Hash Table
				 table.put(key,value);
				
				// System.out.println("The value of url has been put as "+table.get("Url"));
			
				 testData[0][0]= table;		
			//put table in Array
			//System.out.println("");
			
			//index++;
			
			}
		
		return testData;
		//System.out.println("Size of table is "+table.size());
		
	}
	
	
	public static Object[][] getTestData(String KeywordSummary,ExcelUtils xls){
		
		//
		
		int totalrows=xls.getRowCount(KeywordSummary);
	//	System.out.println("Total number of rows "+totalrows);
		
		//Object table using Array
		Object testData[][] = new Object[1][1];
//		int index = 0;
		
		Hashtable<Integer,String> table = null;
		
		
		int dataStartRowNum = 2;
		table = new Hashtable<Integer,String>(); //Init for every row
		// Extract Data
		for(int rNum = dataStartRowNum;rNum<=totalrows;rNum++){
							
				String key = xls.getCellData(KeywordSummary,"Index",rNum);
				int i = Integer.parseInt(key);
				String value = xls.getCellData(KeywordSummary,"Keyword",rNum);
				
				System.out.println(key);	
				System.out.println(value);
				//Fill the Data in Hash Table
				 table.put(i,value);
				 testData[0][0]= table;		
						
			}
		
		return testData;
		//System.out.println("Size of table is "+table.size());		
	}
	
	
	
	
	public static Object[][] getTestCases(String KeywordSummary,ExcelUtils xls){
		
		//
		
		int totalrows=xls.getRowCount(KeywordSummary);
	//	System.out.println("Total number of rows "+totalrows);
		
		//Object table using Array
		Object testData[][] = new Object[1][1];
//		int index = 0;
		
		Hashtable<Integer,String> table = null;
		
		
		int dataStartRowNum = 2;
		table = new Hashtable<Integer,String>(); //Init for every row
		// Extract Data
		for(int rNum = dataStartRowNum;rNum<=totalrows;rNum++){
							
				String key = xls.getCellData(KeywordSummary,"Index",rNum);
				int i = Integer.parseInt(key);
				String value = xls.getCellData(KeywordSummary,"Keyword",rNum);
				
				System.out.println(key);	
				System.out.println(value);
				//Fill the Data in Hash Table
				 table.put(i,value);
				 testData[0][0]= table;		
						
			}
		
		return testData;
		//System.out.println("Size of table is "+table.size());		
	}
	
	
	
	
	
	
	 public static boolean getRunmode(String testName, ExcelUtils xls) {
			
		   for(int rNum=2;rNum<=xls.getRowCount("KeywordSummary");rNum++) {
			   String testcaseName = xls.getCellData("KeywordSummary", "Keyword", rNum);
		              if(testcaseName.equals(testName)) {
		            	  //Check Runmode
		            	  if(xls.getCellData("KeywordSummary","Runmode", rNum).equals("Y")) {
		            		 
		            		  return true;
		            	  }else{
		            		  return false;
		            	  }
		            	 }
		            	  
		              }
		   
		   
		
	    return false;
		   
	   }
	 
	 public static String getTestCaseID(String testName, ExcelUtils xls) {
		 
		   for(int rNum=2;rNum<=xls.getRowCount("KeywordSummary");rNum++) {
			   String testcaseName = xls.getCellData("KeywordSummary", "Keyword", rNum);
		         if(testcaseName.equals(testName)) {
		           String getTestCaseID=xls.getCellData("KeywordSummary","TestID", rNum); 
		           return getTestCaseID;
		          }		   
	   }
		return null;
		
		
	 
	 } 
	 
}
