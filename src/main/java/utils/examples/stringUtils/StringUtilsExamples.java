package utils.examples.stringUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import utils.StringUtils;

public class StringUtilsExamples {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
//	public static void split() {
//
//		// TODO - check that by stream is working as expected for Json and if so make sure that only working parameters are accepted
////		exampleJson();
//		
//		
//		// Craig Adam
//		String sStringToSplit = null;
//		String sDelimiter = null;		
////		String[] aParts = null;
//		int iNoParts = -1;
//		
//
//
////		// OK
////		sStringToSplit = "004-034.556";
////		sDelimiter = "4-0";
////		aParts = stringSplit(sStringToSplit, sDelimiter);
////		
////		// OK
////		sStringToSplit = "00ab4-034.55.6";
////		sDelimiter = "-";
////		aParts = stringSplit(sStringToSplit, sDelimiter);
////		printObject(aParts);
////		
////		// OK
////		sStringToSplit = "00ab4-034.55.6";
////		sDelimiter = ".";
////		aParts = stringSplit(sStringToSplit, sDelimiter);
////		printObject(aParts);
////		
////		// OK
////		sStringToSplit = "00ab4-034.55.6";
////		sDelimiter = "?";
////		aParts = stringSplit(sStringToSplit, sDelimiter);
////		printObject(aParts);	
////		
////		// OK
////		sStringToSplit = "00ab4-034.55.6";
////		sDelimiter = "b4";
////		aParts = stringSplit(sStringToSplit, sDelimiter);
////		printObject(aParts);	
////		
////		// OK
////		sStringToSplit = "int,java.lang-eng.String";
////		sDelimiter = ",";
////		aParts = stringSplit(sStringToSplit, sDelimiter);
////		printObject(aParts);	
////		
////		// OK
////		sStringToSplit = "int,java.lang-eng.String,String,Float";
////		sDelimiter = ",";
////		aParts = stringSplit(sStringToSplit, sDelimiter);
////		printObject(aParts);
////		
////		// OK
////		sStringToSplit = "int,java.lang-eng.String,String,Float";
////		sDelimiter = ",";
////		iNoParts = 6;
////		aParts = stringSplit(sStringToSplit, sDelimiter,iNoParts);
////		printObject(aParts);
////	
////		// OK
////		sStringToSplit = "int,java.lang-eng.String,String,Float";
////		sDelimiter = ",";
////		iNoParts = 6;
////		aParts = stringSplitAppendLeft(sStringToSplit, sDelimiter);
////		printObject(aParts);	
////		
////		// OK
////		sStringToSplit = "int,java.lang-eng.String,String,Float";
////		sDelimiter = ",";
////		iNoParts = 6;
////		aParts = stringSplitAppendRight(sStringToSplit, sDelimiter,iNoParts);
////		
////		System.out.println("aParts.length = " + aParts.length);
////		
////		// OK
////		sStringToSplit = "int,java.lang-eng.String,String,Float";
////		sDelimiter = ",";
////		iNoParts = 6;
////		aParts = stringSplitAppendLeft(sStringToSplit, sDelimiter, iNoParts);
////		printObject(aParts);	
////		
////		// OK
////		sStringToSplit = "int,java.lang-eng.String,String,Float";
////		sDelimiter = ",";
////		iNoParts = 6;
////		aParts = stringSplitAppendRight(sStringToSplit, sDelimiter);
////		
//		// OK
//		sStringToSplit = "java.lang.String,int";
//		sDelimiter = ",";
////		aParts = stringSplit(sStringToSplit, sDelimiter);
//		
//	
//		
//		
//	}
	
	public static void exampleJson_Stream() {

		// Craig Adam

		// Read from file for demo purposes
		// This is response receive d
		String sResponse;
		try {
			String sInputFileLocation = ".//src//test//java//filesTest//ResponseBody_F1_2017_Circuits.json";
			sResponse = StringUtils.readStringFromFile(sInputFileLocation);

			// Split string by delimiter
			String sDelimiter = "*}";
			List<String> lstSplitString = StringUtils.splitByDelimiter_Stream(sResponse, sDelimiter);

			// Filter list by stream
			List<String> lstFiltered = lstSplitString.stream() // convert list to stream
					.filter(line -> line.contains("<0718532440@sc-sms.sanlam.co.za>")) // add to collection if contains
																						// xxx
					.collect(Collectors.toList()); // collect the output and convert streams to a List

			// Print Filtered list

			for (int i = 0; i < lstFiltered.size(); i++) {

				// Extract date

				System.out.println(i);
				System.out.println(lstFiltered.get(i));
				System.out.println("");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
