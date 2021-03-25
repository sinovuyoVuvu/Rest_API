package resources.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import coreDrivers.WebBase;

public class ApplicationBase extends WebBase {
	public static int getPositionOfWord(String searchword) {
		/*
		 * This will return the starting position of the word and Line Number
		 * 
		 */
		// Check to see if they supplied the search phrase as a parameter.
		// If so, set our searchword from the parameter passed and begin searching.
		// if (args.length > 0) {

		// Set the searchword to our parameter
		// (eg if they type 'java searchfile "hello"' then the search word would be
		// "hello")

		try {

			// Keep track of the line we are on and what the line is.
			int LineCount = 0;
			String line = "";

			// Create a reader which reads our file. In this example searchfile.txt is the
			// file we are searching.
			BufferedReader bReader = new BufferedReader(new FileReader("\\\\srv006552\\Git_Repo\\output.txt"));

			// While we loop through the file, read each line until there is nothing left to
			// read.
			// This assumes we have carriage returns ending each text line.

			while ((line = bReader.readLine()) != null) {
				LineCount++;

				// See if the searchword is in this line, if it is, it will return a position.
				int posFound = line.indexOf(searchword);

				// If we found the word, print the position and our current line.
				if (posFound > -1) {
					System.out.println("Search word found at position " + posFound + " on line " + LineCount);

					return LineCount;
				}
			}

			// Close the reader.
			bReader.close();

		} catch (IOException e) {
			// We encountered an error with the file, print it to the user.
			System.out.println("Error: " + e.toString());
		}
		return 0;
	}
	// else {
	// They obviosly didn't provide a search term when starting the program.
	// System.out.println("Please provide a word to search the file for.");
	// }

	// else {
	// They obviosly didn't provide a search term when starting the program.
	// System.out.println("Please provide a word to search the file for.");
	// }

	public static String CheckIfStringPresents() throws IOException {
		// TODO Auto-generated method stub
		BufferedReader bReader = new BufferedReader(new FileReader("\\\\srv006552\\Git_Repo\\output.txt"));

     	File file = new File("C:\\Users\\G987321\\NewWorkspce\\QA_Servicedesk11\\src\\test\\java\\caUnicentre\\config\\validatingService");

		BufferedReader br = new BufferedReader(new FileReader(file));
		 String line1 = "";String line2=" ";int LineCount = 0;String lines = "";
		 
		 while ((lines = bReader.readLine()) != null) {
				LineCount++;
		
				if(LineCount>=5)
				{
		
		while ((line1 = br.readLine()) != null)
		{
			String lolo =line1.trim();
			System.out.println(lolo);
			System.out.println(LineCount);
			
			while ((line2 = bReader.readLine()) != null) 
			{
				String lili =line2.substring(0, 40).trim();
				if(lili.contains(lolo))
				{
				System.out.println(lili);
				childTest.log(Status.PASS,MarkupHelper.createLabel("The status of the Service is "+lili+"is Expected", ExtentColor.GREEN));
				}
				else
				{
				childTest.log(Status.FAIL,MarkupHelper.createLabel("The status of the Service is"+lili+"was Not Expected", ExtentColor.RED));	
				}
			
				break;
			}
			
		}
		break;	
		}
		}
		return " ";
	}
	
	
	
	public static String CheckIfStringPresent() throws IOException {
		// TODO Auto-generated method stub
		BufferedReader bReader = new BufferedReader(new FileReader("\\\\srv006552\\Git_Repo\\output_webstat.txt"));

     	File file = new File("C:\\Users\\G987321\\NewWorkspce\\QA_Servicedesk11\\src\\test\\java\\caUnicentre\\config\\pdmWebstatus");

		BufferedReader br = new BufferedReader(new FileReader(file));
		 String line1 = "";String line2=" ";int LineCount = 0;String lines = "";
		 
		 while ((lines = bReader.readLine()) != null)
		 {
				LineCount++;
		
				if(LineCount==3)
				{
		
					while ((line1 = bReader.readLine()) != null)
					{
						String lolo =line1.substring(0, 21).trim();
						System.out.println(lolo);
						System.out.println(LineCount);
			
						while ((line2 = br.readLine()) != null) 
						{
							String lili =line2.substring(0, 21).trim();
							if(lili.contains(lolo))
							{
								System.out.println(lili);
								childTest.log(Status.PASS,MarkupHelper.createLabel("This Service is up "+lili+"is Expected", ExtentColor.GREEN));
							}
							else
							{
								childTest.log(Status.FAIL,MarkupHelper.createLabel("This Service is not up"+lili+"was Not Expected", ExtentColor.RED));	
							}
			
							break;
						}
						break;
					}
						
				}
				if(LineCount>=4)
				{
				break;
				}
		}
		return " ";
	}
	
	
	
	
	
	
	
	
}