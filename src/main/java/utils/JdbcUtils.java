package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
//import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.sql.rowset.WebRowSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.FileFolderUtils;

import utils.JdbcUtils;
import utils.customException.CustomException;


public class JdbcUtils {

	public static void main(String[] args) {

		// Craig Adam
		// 20-01-2020
//		int iFK_Component_PKID = 2;
//		int iFK_Keyword_PKID = 26;
//		String sDatabaseName = "gsa_auto_training_db";
//		System.out.println(queryRunValueTable(iFK_Component_PKID, iFK_Keyword_PKID, sDatabaseName));

	}
	
	
	
	
	public static WebRowSet filteredComponentData(String sComponentName, WebRowSet rRowSetFromExcel) {

		// Craig Adam
		// 30-11-2019
		
		
		
		// Filter the WebRowSet by component name
		// Drop nodes that do not require for component
		Document docRowSetFiltered = JdbcUtils.extractComponentRowsetDoc(sComponentName, rRowSetFromExcel);
		
		// Convert doc back to xml
		String xmlRowSetFiltered = JdbcUtils.convertRowSetDocToXml(docRowSetFiltered);

		// Save output xml string for ease of DEBUG
		//String sOutputFilePath = ".//src//main//java//rowSetHelper//" + "sample_auto_write"
			//	+ ".xml";
		
		String sOutputFilePath = System.getProperty("user.dir") +"sample_auto_write"
		+ ".xml";
		JdbcUtils.saveStringToFile(xmlRowSetFiltered, sOutputFilePath);

		// Read xml string back to RowSet
		StringReader stringReader = new StringReader(xmlRowSetFiltered);
		WebRowSet rWebRowSetExcelFiltered = null;
		try {

			// Save ResultSet into a CachedRowSet
			RowSetFactory oRowSetFactory;

			oRowSetFactory = RowSetProvider.newFactory();
			rWebRowSetExcelFiltered = oRowSetFactory.createWebRowSet();

			rWebRowSetExcelFiltered.readXml(stringReader);
			rWebRowSetExcelFiltered.beforeFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		print(rRowSetFromExcel);

//		print(rWebRowSetExcelFiltered);

//		printValuesExample(rRowSetFromExcel);
//		System.out.print(xmlRowSet);
		
		return rWebRowSetExcelFiltered;
		
	}
	
	
	public static String getRowSetValueAsString(String objectName, ResultSet webRowSet) {
		
		
		// Craig Adam
		// get field value as String for the current row
		
		
        // TODO add generic method to retrieve from Table - so do not have to try catch	
        try {
        	
			return webRowSet.getString(objectName);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return null;
        
	}
	
	
	/**
	 * Get the column number of the given column name
	 * 
	 * @param matchColumnName - name of the column name to find
	 * @param webRowSet       - webRowSet to searcu
	 * @return column number (int) that matches the given column. Column Number has
	 *         base 1. Returns null if no match found
	 * @author Craig Adam - Modified 04-02-2020
	 * 
	 */
	public static int getColumnNo(String matchColumnName, WebRowSet webRowSet) {

		int noColumns = 0;
		int matchedColumnNo = -1;

		try {

			noColumns = JdbcUtils.getMetaData(webRowSet).getColumnCount();

			for (int i = 1; i < noColumns + 1; i++) {

				String currentColumnName = JdbcUtils.getMetaData(webRowSet).getColumnName(i);
				if (currentColumnName.contentEquals(matchColumnName)) {

					matchedColumnNo = i;
					break;

				}
				
				if(i == noColumns) {
					
					// Reached end of search and no match
					
					System.out.println("[JdbcUtils Ln 134 ] NO Match found for column : " + matchColumnName);
					
				}
				

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return matchedColumnNo;

	}


	
	
	/**
	 * 
	 * NB should put cursor back to original position 
	 * 
	 * 
	 * @param webRowSet
	 * @return
	 */
	public static int getLastRowNumber(WebRowSet webRowSet) {

		int originalRowNo = -1;
				
		try {
			
			originalRowNo = webRowSet.getRow();
			
//			System.out.println("originalRowNo = [JdbcUtils ln 372] " + originalRowNo);
				
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
		int iCurrentRowNo = 1;
		
		try {
			
			if(!webRowSet.isLast()) {
				
				try {
					
					do {

						webRowSet.absolute(iCurrentRowNo);
						
//			cachedRowSet.moveToCurrentRow();			
//			System.out.println("cachedRowSet.isLast()[ln 92]= " + cachedRowSet.isLast());
//			System.out.println("iCurrentRowNo = [ln 110] " + iCurrentRowNo);
//			System.out.println("getRow = [ln 111] " + cachedRowSet.getRow());
//			System.out.print(cachedRowSet.getString(3) + " || ");

						iCurrentRowNo = iCurrentRowNo + 1;

					} while (!webRowSet.isLast());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		iCurrentRowNo = iCurrentRowNo - 1;
		

		// Reset back to original
		try {
			webRowSet.absolute(originalRowNo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return iCurrentRowNo;

	}

	

	public static WebRowSet readLastModifiedComponentXml_WebRowSet(String outputFileFolder, String folderName,
			String componentName) {

		// Craig Adam
		// 03-02-2020
		// Return WebRowSet of the the last modified file in the folderName located
		// inside outputFileFolder that filename contains componentName

		String folderLocation = FileFolderUtils.convertRelToAbsPath((outputFileFolder + "//" + folderName));
		String locationlastModifiedBaselineFile = FileFolderUtils.findLastModified(folderLocation, componentName, ".xml")
				.getAbsolutePath();
		WebRowSet webRowSet = JdbcUtils.readFromXmlFile_WebRowSet(locationlastModifiedBaselineFile);

		return webRowSet;

	}


	public static WebRowSet readFromXmlFile_WebRowSet(String inputFilePath) {

		// Craig Adam
		// 29-01-2020
		// Read a correctly formated xml doc into a WebRowSet

		// Read file back into an xml String
		String xmlRowSet = null;
		WebRowSet webRowSetFromFile = null;

		try {

			xmlRowSet = new String(Files.readAllBytes(Paths.get(inputFilePath)));
			webRowSetFromFile = JdbcUtils.convertXmlRowSetToRowSet(xmlRowSet);

			webRowSetFromFile.first();

//			System.out.println("Column Count = " + webRowSetFromFile.getMetaData().getColumnCount());
//			System.out.println("Value of column 3 = " + webRowSetFromFile.getString(3));

		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (SQLException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return webRowSetFromFile;

	}


	
	public static ResultSetMetaData getMetaData(ResultSet resultSet) {

		// Craig Adam
		// 28-01-2020

		try {
			return resultSet.getMetaData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Write ResultSet to output text file using built in Sap methods will create
	 * parent folder structure as required
	 * 
	 * @param resultSet      : ResultSet to be saved to file
	 * @param outputFilePath : Absolute filename and path to save the ResultSet
	 * @author Craig Adam - Modified 31-01-2020
	 * 
	 */
	public static void writeToFile(ResultSet resultSet, String outputFilePath) {

		// built in printResultSet for ResultSet from SAP db

		String outputFolderPath = FilenameUtils.getFullPathNoEndSeparator(outputFilePath);

		// create output folder if does not exist
		// https://stackoverflow.com/questions/28947250/create-a-directory-if-it-does-not-exist-and-then-create-the-files-in-that-direct
		File directory = new File(outputFolderPath);
		if (!directory.exists()) {
			directory.mkdirs();

		}

		try {

			/*
			 * PrintStream printStream = new PrintStream(new
			 * FileOutputStream(reusableMethods.FilePathUtility.convertRelToAbsPath(
			 * outputFilePath), true)); append to end of existing file append to existing
			 * file false - ie new create new file
			 * 
			 */

			PrintStream printStream = new PrintStream(
					new FileOutputStream(FileFolderUtils.convertRelToAbsPath(outputFilePath), false));
			com.sap.db.jdbc.Driver.printResultSet(printStream, resultSet);
			printStream.flush();

		} catch (SQLException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static ArrayList<String> getComponentList(WebRowSet testCaseWebRowSet) {

		// Craig Adam
		// 21-01-2020
		// Get a collection of all the rows in the components column
		// the blanks will be populated with the component name above during reading to
		// xlsx method "createKeyValueRowSetFromExcel"
		
		ArrayList<String> lComponentList = new ArrayList<String>();
		String sComponentNameCurrent = null;
		String sComponentNamePrevious = "";

		try {

			testCaseWebRowSet.absolute(1); // move to 1st row : is reserved for the component names
			int iTotalCols = testCaseWebRowSet.getMetaData().getColumnCount();
			for (int iCurrentColNo = 1; iCurrentColNo <= iTotalCols; iCurrentColNo++) {

				sComponentNameCurrent = testCaseWebRowSet.getString(iCurrentColNo);
//				System.out.println("sComponentNameCurrent [ReadExcel Ln 54]= " + sComponentNameCurrent);
				
				if (!(sComponentNameCurrent.contentEquals(sComponentNamePrevious))) {

					lComponentList.add(sComponentNameCurrent.trim().toString());

				}

				sComponentNamePrevious = sComponentNameCurrent;

			}
			
			testCaseWebRowSet.beforeFirst();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lComponentList;

	}
	
	public static WebRowSet filterTestWebRowSetByComponent(String componentName, WebRowSet testCaseWebRowSet) {

		// Craig Adam
		// TODO add catch if no match found

//		String sComponentName = "Comp_Name_Two[0]";  //ok index 0
//		String sComponentName = "Comp_Name_One[0]"; // ok index 0
//		String sComponentName = "Comp_Name_Two";  //ok index 0
//		String sComponentName = "Comp_Name_One";  //ok index 0
//		String sComponentName = "Comp_Name_One[1]"; // ok index 1

		// Filter the WebRowSet by component name
		// Drop nodes that do not require for component
		Document docRowSetFiltered = JdbcUtils.extractComponentRowsetDoc(componentName, testCaseWebRowSet);

		// Convert doc back to xml
		String xmlRowSetFiltered = JdbcUtils.convertRowSetDocToXml(docRowSetFiltered);

		// DEbug
//		// Save output xml string for ease of DEBUG
//		String sOutputFilePath = ".//src//test//java//codeExamples//JDBC//MsSql//Output//" + "sample_auto_write"
//				+ ".xml";
//		JDBCSupport.saveStringToFile(xmlRowSetFiltered, sOutputFilePath);
		// DEbug
		
		// Read xml string back to RowSet
		StringReader stringReader = new StringReader(xmlRowSetFiltered);
		WebRowSet rWebRowSetExcelFiltered = null;
		try {

			// Save ResultSet into a CachedRowSet
			RowSetFactory oRowSetFactory;

			oRowSetFactory = RowSetProvider.newFactory();
			rWebRowSetExcelFiltered = oRowSetFactory.createWebRowSet();

			rWebRowSetExcelFiltered.readXml(stringReader);
			rWebRowSetExcelFiltered.beforeFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		System.out.println("rRowSetFromExcel [ReadExcel Ln 78]");
//		printObject(rRowSetFromExcel);

//		System.out.println("rRowSetFromExcel [ReadExcel Ln 81]");
//		printObject(rWebRowSetExcelFiltered);

//		printValuesExample(rRowSetFromExcel);

		return rWebRowSetExcelFiltered;

	}

	public static Document extractComponentRowsetDoc(String sComponentName, WebRowSet RowSetFromExcel) {

		// Craig Adam
		// 20-01-2020
		// update for filtering - fix match middle index as well
		// Get total number of columns

		int iTotalCols = -1;
		int iTotalRows = -1;

		try {

			iTotalCols = RowSetFromExcel.getMetaData().getColumnCount();
			iTotalRows = RowSetFromExcel.size();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get columns that match the required component name and index
		ArrayList<Integer> lComponentColumnNos = getComponentColumnNos(sComponentName, RowSetFromExcel);

		// Debug
//		System.out.print("[JDBCSupport ln 88]");
//		System.out.print("Matching Col Nos = ");
//		for (int i = 0; i < lComponentColumnNos.size(); i++) {
//			System.out.print(lComponentColumnNos.get(i) + " || ");
//		}
//		System.out.println(System.lineSeparator());
		// Debug

		// Convert rowset into a document
		Document docRowSet = JdbcUtils.convertRowSetToDocument(RowSetFromExcel);
		// do something to document to check ok
//		
//		docRowSet = JDBCSupport.setTextContent("9999", "data#currentRow#columnValue[1]", docRowSet);

		// Remove data not relevant to this component in reverse order
		for (int iColNo = iTotalCols; iColNo > 0; iColNo -= 1) {

			if (!lComponentColumnNos.contains(iColNo)) {

				// Remove the metadata node
				docRowSet = JdbcUtils.removeElement("metadata#column-definition[" + (iColNo - 1) + "]", docRowSet);

				// Remove the data nodes from each data set
				for (int iRowNo = iTotalRows; iRowNo > 0; iRowNo -= 1) {

					docRowSet = JdbcUtils.removeElement(
							"data#currentRow[" + (iRowNo - 1) + "]#columnValue[" + (iColNo - 1) + "]", docRowSet);

				}

			}

		}

		// Set the new column count
		docRowSet = JdbcUtils.setTextContent(lComponentColumnNos.size() + "", "metadata#column-count", docRowSet);

		// Remove 1st data row - contains component names
		removeElement("data#currentRow[0]", docRowSet);

		return docRowSet;
	}
	
	public static Document convertRowSetToDocument(ResultSet ExcelRowSet) {

		// Craig Adam
		// 30/11/2019

		Document docRowSet = null;
		HashMap<String, String> mElementValues = new HashMap<String, String>();

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();

			// New document
			docRowSet = builder.newDocument();

			// Create webRowSet element
			Element eWebRowSet = docRowSet.createElement("webRowSet");
			eWebRowSet.setAttribute("xmlns", "http://java.sun.com/xml/ns/jdbc");
			eWebRowSet.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			eWebRowSet.setAttribute("xsi:schemaLocation",
					"http://java.sun.com/xml/ns/jdbc http://java.sun.com/xml/ns/jdbc/webrowset.xsd");
			docRowSet.appendChild(eWebRowSet);

			// Create properties element
			Element eProperties = docRowSet.createElement("properties");
			eWebRowSet.appendChild(eProperties);

			// Create metadata element
			Element eMetadata = docRowSet.createElement("metadata");
			eWebRowSet.appendChild(eMetadata);

			// Create column-count element
			Element eColumnCount = docRowSet.createElement("column-count");
			eMetadata.appendChild(eColumnCount);
			eColumnCount.appendChild(docRowSet.createTextNode("1"));

			// Build column metadata for each column in the resultset
			int iNoColumns = ExcelRowSet.getMetaData().getColumnCount();
			for (int iColNo = 1; iColNo <= iNoColumns; iColNo++) {

				mElementValues.clear();
				mElementValues.put("column-index", iColNo + "");
				mElementValues.put("column-name", ExcelRowSet.getMetaData().getColumnName(iColNo));
				mElementValues.put("column-type", "12"); // generic database type
				addNode_ColumnDef(docRowSet, eMetadata, mElementValues);

			}

			// Create data element
			Element eData = docRowSet.createElement("data");
			eWebRowSet.appendChild(eData);

			ResultSetMetaData rsmd = ExcelRowSet.getMetaData();
			int colCount = rsmd.getColumnCount();

			// create currentRow for each result in resultset
			ExcelRowSet.beforeFirst();
			while (ExcelRowSet.next()) {

				// Create currentRow in data
				Element currentRow = docRowSet.createElement("currentRow");
				eData.appendChild(currentRow);

				// Populate for each column
				for (int i = 1; i <= colCount; i++) {

//					String columnName = rsmd.getColumnName(i);
					Object value = ExcelRowSet.getObject(i);
					Element nodeName = docRowSet.createElement("columnValue");
					nodeName.appendChild(docRowSet.createTextNode(value.toString()));
					currentRow.appendChild(nodeName);
				}
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return docRowSet;

	}	
		
	public static Document convertExcelRowSetToDocument_V0(ResultSet ExcelRowSet) {

		// Craig Adam
		// 30/11/2019
		// before add check if data rows

		Document docRowSet = null;
		HashMap<String, String> mElementValues = new HashMap<String, String>();

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();

			// New document
			docRowSet = builder.newDocument();

			// Create webRowSet element
			Element eWebRowSet = docRowSet.createElement("webRowSet");
			eWebRowSet.setAttribute("xmlns", "http://java.sun.com/xml/ns/jdbc");
			eWebRowSet.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			eWebRowSet.setAttribute("xsi:schemaLocation",
					"http://java.sun.com/xml/ns/jdbc http://java.sun.com/xml/ns/jdbc/webrowset.xsd");
			docRowSet.appendChild(eWebRowSet);

			// Create properties element
			Element eProperties = docRowSet.createElement("properties");
			eWebRowSet.appendChild(eProperties);

			// Create metadata element
			Element eMetadata = docRowSet.createElement("metadata");
			eWebRowSet.appendChild(eMetadata);

			// Create column-count element
			Element eColumnCount = docRowSet.createElement("column-count");
			eMetadata.appendChild(eColumnCount);
			eColumnCount.appendChild(docRowSet.createTextNode("9"));

			// Build column metadata for each column in the resultset
			int iNoColumns = ExcelRowSet.getMetaData().getColumnCount();
			for (int iColNo = 1; iColNo <= iNoColumns; iColNo++) {

				mElementValues.clear();
				mElementValues.put("column-index", iColNo + "");
				mElementValues.put("column-name", ExcelRowSet.getMetaData().getColumnName(iColNo));
				mElementValues.put("column-type", "12"); // generic database type
				addNode_ColumnDef(docRowSet, eMetadata, mElementValues);

			}

			// Create data element
			Element eData = docRowSet.createElement("data");
			eWebRowSet.appendChild(eData);

			ResultSetMetaData rsmd = ExcelRowSet.getMetaData();
			int colCount = rsmd.getColumnCount();

			// create currentRow for each result in resultset
			ExcelRowSet.beforeFirst();
			while (ExcelRowSet.next()) {

				// Create currentRow in data
				Element currentRow = docRowSet.createElement("currentRow");
				eData.appendChild(currentRow);

				// Populate for each column
				for (int i = 1; i <= colCount; i++) {

//					String columnName = rsmd.getColumnName(i);
					Object value = ExcelRowSet.getObject(i);
					Element nodeName = docRowSet.createElement("columnValue");
					nodeName.appendChild(docRowSet.createTextNode(value.toString()));
					currentRow.appendChild(nodeName);
				}
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return docRowSet;

	}

	public static String convertRowSetDocToXml(Document docWebRowSet) {

		StringWriter sw = null;

		try {

			DOMSource domSource = new DOMSource(docWebRowSet);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer;

			transformer = tf.newTransformer();

			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");

			// all methods

//			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "");
//			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "");
//			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
//			transformer.setOutputProperty(OutputKeys.ENCODING,null);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

//			transformer.setOutputProperty(OutputKeys.MEDIA_TYPE,"");
//			transformer.setOutputProperty(OutputKeys.METHOD,"");
//			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//			transformer.setOutputProperty(OutputKeys.STANDALONE, "");
//			transformer.setOutputProperty(OutputKeys.VERSION, "1.0");

//			transformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS  , "");

			sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);
			transformer.transform(domSource, sr);

		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.print(sw.toString());

		return sw.toString();

	}

	public static WebRowSet convertXmlRowSetToRowSet(String xmlRowSet) {

		// Craig Adam
		// 02-12-2019
		//

		// Read xml string back to RowSet
		StringReader stringReader = new StringReader(xmlRowSet);
		WebRowSet rWebRowSet = null;
		try {

			// Save ResultSet into a CachedRowSet
			RowSetFactory oRowSetFactory;

			oRowSetFactory = RowSetProvider.newFactory();
			rWebRowSet = oRowSetFactory.createWebRowSet();

			rWebRowSet.readXml(stringReader);
			rWebRowSet.beforeFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rWebRowSet;

	}

	public static int getColumnDefsTotal(Document docWebRowSet) {

		// Craig Adam
		// 02-12-2019
		// return total column definitions in current WebRowSet document

		Element eMetadata = (Element) docWebRowSet.getElementsByTagName("metadata").item(0); // there is only one grab
																								// it
		NodeList lColDefs = eMetadata.getElementsByTagName("column-definition"); // Get all col defs

//		System.out.println("lColDefs.getLength() = " + lColDefs.getLength());

		return lColDefs.getLength();

	}

	public static int getColumnIndexByName(String sColumnName, Document docWebRowSet) {

		// Craig Adam
		// 02-12-2019
		// find the 1st column index that matches name
		
//		System.out.println("ln444 : " + JDBCSupport.convertRowSetDocToXml(docWebRowSet));
		
		Element eMetadata = (Element) docWebRowSet.getElementsByTagName("metadata").item(0); // there is only one grab
		// it
		NodeList lColDefs = eMetadata.getElementsByTagName("column-definition"); // Get all col defs

		// iterate over all col defs looking for one with column name want to delete
		@SuppressWarnings("unused")
		int iMatchedColumnIndex = -1;
		int iLengthColDefs = lColDefs.getLength();
		
//		System.out.println("iLengthColDefs = " + iLengthColDefs);
		
		
		
		
		for (int iColumnIndex = 0; iColumnIndex < iLengthColDefs; iColumnIndex++) {

//			System.out.println("For 460 ... iColumnIndex = " + iColumnIndex);
			
			
//			Element nCurrentColumnNode = (Element) lColDefs.item(iColumnIndex);
//			String sCurrentColumnName = nCurrentColumnNode.getElementsByTagName("column-name").item(0).getTextContent(); // Current
	
			
			Element nCurrentColumnNode = (Element) JdbcUtils.getNodeByXpath("metadata#column-definition" + "[" + iColumnIndex + "]" +"#column-name", docWebRowSet);
			String sCurrentColumnName = nCurrentColumnNode.getTextContent();
			
			
//			System.out.println("sColumnName = " + sColumnName);
//			System.out.println("sCurrentColumnName = " + sCurrentColumnName);
//			System.out.println("sCurrentColumnName.contentEquals(sColumnName) : " + sCurrentColumnName.contentEquals(sColumnName));
//			System.out.println("iColumnIndex = " + iColumnIndex);
			
			// Column
			// Name
			if (sCurrentColumnName.contentEquals(sColumnName)) {

				// Early exit - found a matching column
				
				
				iMatchedColumnIndex = iColumnIndex;
				
//				System.out.println("iMatchedColumnIndex  487= " + iMatchedColumnIndex);
				
				
				break;

			}

			if (iColumnIndex == lColDefs.getLength() - 1) {

				iMatchedColumnIndex = -1; // NO iterated through all and match found - should break on found

//				System.out.println("iMatchedColumnIndex  497= " + iMatchedColumnIndex);
				
			}

		}

		
//		System.out.println("iMatchedColumnIndex  505= " + iMatchedColumnIndex);
		
		return iMatchedColumnIndex;

	}


	public static Document removeColumnByName(String sColumnName, int iCurrentRowIndex, Document docWebRowSet) {

		// Craig Adam
		// 02-12-2019
		// removes column metadata and column row data from an RowSet document

		// get matching column definition
		int iMatchedColumnIndex = -1;
		iMatchedColumnIndex = getColumnIndexByName(sColumnName, docWebRowSet);
		
		if(iMatchedColumnIndex != -1) {
			
			// remove matched column definition
			docWebRowSet = JdbcUtils.removeElement("metadata#column-definition[" + (iMatchedColumnIndex) + "]",
					docWebRowSet);

			// update column count
			int iNewColCount = Integer.parseInt(JdbcUtils.getTextContent("metadata#column-count", docWebRowSet)) - 1; // Column
																														// count
																														// has
																														// base
																														// 1
			docWebRowSet = JdbcUtils.setTextContent(iNewColCount + "", "metadata#column-count", docWebRowSet);

			// update the new column numbers
			for (int iColumnIndex = 0; iColumnIndex <= iNewColCount - 1; iColumnIndex++) {

				docWebRowSet = JdbcUtils.setTextContent((iColumnIndex + 1) + "",
						"metadata#column-definition[" + (iColumnIndex) + "]#column-index", docWebRowSet); // Column index
																											// has base 0

			}

			// remove current row for this column from
//			Element eData = (Element) docWebRowSet.getElementsByTagName("data").item(0); // there is only one grab it"
//			NodeList lCurrentRows = eData.getElementsByTagName("currentRow"); // Get all col defs
			docWebRowSet = JdbcUtils.removeElement("data#currentRow[" + (iMatchedColumnIndex) + "]" + "#columnValue[" + iCurrentRowIndex + "]", docWebRowSet);

		}

		return docWebRowSet;

	}

	
	public static String getKeyValuePair(String KeyName, WebRowSet rWebRowSet) {
		
		// Craig Adam
		// 09-12-2019
		// Returns value of key from KeyValueWebRowSet
		// TODO currently defaults to return string - update to read type of database 
		// parameter type and return as that using generic return object - Craig Adam
			
		try {
			
			return rWebRowSet.getString(KeyName);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static WebRowSet setKeyValuePair(String KeyName, String ParameterValue, WebRowSet rWebRowSet) {

		// Craig Adam
		// 02-12-2019
		// Add parameter value to RowSet
		// If column (key) does not exist will create the key
		
		// Create an empty WebRowSet if does not exist (ie set to null)
		if(rWebRowSet == null) {
			
			rWebRowSet = JdbcUtils.createEmptyWebRowSet();
			
		}
		
		// Covert RowSet to Document - so can manipulate using document methods
		Document docWebRowSet = JdbcUtils.convertRowSetToDocument(rWebRowSet);

//		// DEBUG
//		// --- DO something to see working ---
//		// Set the new column count
//		docWebRowSet = JDBCSupport.setTextContent("9", "metadata#column-count", docWebRowSet);
//		// Check that has been set
//		System.out.println("getTextContent = " + JDBCSupport.getTextContent("metadata#column-count", docWebRowSet));
//		// Reset to 1
//		docWebRowSet = JDBCSupport.setTextContent("1", "metadata#column-count", docWebRowSet);
//		// --- DO something ---

		// Find index of key
		int iColumnIndex = JdbcUtils.getColumnIndexByName(KeyName, docWebRowSet);
		int iCurrentRowIndex = 0; // This is a key/value pair so will only have a single row
		
		// If column (key) does not exist create it
		if (iColumnIndex == -1) {

			// Create a new column
			iColumnIndex = JdbcUtils.getColumnDefsTotal(docWebRowSet) + 1; // Column index for a WebRowSet is base 1
			HashMap<String, String> mElementValues = new HashMap<String, String>();
			mElementValues.clear();
			mElementValues.put("column-index", (iColumnIndex) + "");
			mElementValues.put("column-name", KeyName);
			mElementValues.put("column-type", "12"); // generic database type - 12 == varchar
			docWebRowSet = JdbcUtils.addNode_ColumnDef(docWebRowSet, mElementValues);

//			System.out.println(JDBCSupport.convertRowSetDocToXml(docWebRowSet));

			// Add currentValue Node
			docWebRowSet = JdbcUtils.addNode_CurrentValue(docWebRowSet, iCurrentRowIndex);
//			System.out.println("ln 86 = " + JDBCSupport.convertRowSetDocToXml(docWebRowSet));
			
		}

		// Update column-count		
		iColumnIndex = JdbcUtils.getColumnDefsTotal(docWebRowSet) - 1; // index is a base 0
		docWebRowSet = JdbcUtils.setTextContent((iColumnIndex + 1) + "", "metadata#column-count", docWebRowSet); // column-count is base 1
		
		// setTextContent
		docWebRowSet = JdbcUtils.setParameterValue(KeyName, ParameterValue, docWebRowSet, iCurrentRowIndex);
//		System.out.println("ln92 " + JDBCSupport.convertRowSetDocToXml(docWebRowSet));

		
		// if have 2 columns one of them could be the placeholder
		// Column index is base 0
		if(iColumnIndex == 1) {
						
			// Remove Placeholder Column
			docWebRowSet = JdbcUtils.removeColumnByName("Placeholder" , iCurrentRowIndex, docWebRowSet);
			
		}

		


		// Convert document back to xml
		String sXmlWebRowSet = JdbcUtils.convertRowSetDocToXml(docWebRowSet);
//		System.out.println("sXmlWebRowSet ln92 = " + sXmlWebRowSet);
		// Save output xml string for ease of DEBUG
		//String sOutputFilePath = ".//src//main//java//rowSetHelper//" + "sample_auto_write"
			//	+ ".xml";
	//	String sOutputFilePath = ".//src//main//java//rowSetHelper//" + "sample_auto_write"
		//		+ ".xml";
		
		String sOutputFilePath = System.getProperty("user.dir") +"sample_auto_write"
				+ ".xml";
		
		JdbcUtils.saveStringToFile(sXmlWebRowSet, sOutputFilePath);		
		
		// Convert xml back to RowSet
		rWebRowSet = JdbcUtils.convertXmlRowSetToRowSet(sXmlWebRowSet);



		// Debug
//		JDBCSupport.print(rWebRowSet);
		

		

		return rWebRowSet;

	}
	
	public static WebRowSet createEmptyWebRowSet() {

		// Craig Adam
		// 02-12-2019
		// Create blank WebRowSet (with placeholder column)

		// Reusable
		Document docWebRowSet = null;

		@SuppressWarnings("unused")
		String sCellValue = null;
		@SuppressWarnings("unused")
		String sColumnDataType = null;

		Element eWebRowSet = null;
		Element eProperties = null;
		Element eMetadata = null;
		Element eData = null;

		@SuppressWarnings("unused")
		FormulaEvaluator CellFormulaEvaluator = null;

		try {

			// New document
			DocumentBuilderFactory oDocBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder oDocBuilder;
			oDocBuilder = oDocBuilderFactory.newDocumentBuilder();
			docWebRowSet = oDocBuilder.newDocument();

			// Build basic schema
			// Create webRowSet element
			eWebRowSet = docWebRowSet.createElement("webRowSet");
			eWebRowSet.setAttribute("xmlns", "http://java.sun.com/xml/ns/jdbc");
			eWebRowSet.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			eWebRowSet.setAttribute("xsi:schemaLocation",
					"http://java.sun.com/xml/ns/jdbc http://java.sun.com/xml/ns/jdbc/webrowset.xsd");
			docWebRowSet.appendChild(eWebRowSet);

			// Create properties node
			eProperties = docWebRowSet.createElement("properties");
			eWebRowSet.appendChild(eProperties);

			// Create metadata element
			eMetadata = docWebRowSet.createElement("metadata");
			eWebRowSet.appendChild(eMetadata);

			// Create column count element (so in the correct location)
			JdbcUtils.addMetadata_ColumnCount("1", docWebRowSet, eMetadata);

			// Create data element
			eData = docWebRowSet.createElement("data");
			eWebRowSet.appendChild(eData);

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Create placeholder column in metadata node
		JdbcUtils.addNode_ColumnDef(docWebRowSet, eMetadata, "1", "Placeholder", "12");

		// TODO
		// only for generic build
		// a placeholder row is not required for an xml read to WebRowSet - Craig Adam
		// 02-12-2019
		// Add holder for cell value
		ArrayList<String> lColumnValues = new ArrayList<String>();
		lColumnValues.add("CellHolder");
		
		docWebRowSet = JdbcUtils.addNode_CurrentRow(docWebRowSet, eData, lColumnValues);

//		System.out.println("ln591 = " + JDBCSupport.convertRowSetDocToXml(docWebRowSet));
		
		
		// Transform to DOM
		DOMSource domSource = new DOMSource(docWebRowSet);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		String sDocXml = null;
		try {
			transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			StringWriter sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);
			transformer.transform(domSource, sr);
			sDocXml = sw.toString();

		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WebRowSet rWebRowSetEmpty = null;
		try {

			RowSetFactory oRowSetFactory = RowSetProvider.newFactory();
			rWebRowSetEmpty = oRowSetFactory.createWebRowSet();

			StringReader stringReader = new StringReader(sDocXml);
			rWebRowSetEmpty.readXml(stringReader);

		} catch (SQLException e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}

		// TODO - Debug
		// write to file
		try {
			System.out.println(System.getProperty("user.dir") +"\\sample_auto_write"
					+ ".xml");
			String sOutputFilePath = System.getProperty("user.dir") +"sample_auto_write"
					+ ".xml";
			//String sOutputFilePath = ".//src//main//java//rowSetHelper//" + "sample_auto_write"
				//	+ ".xml";
			FileWriter fw;
			fw = new FileWriter(sOutputFilePath);
			fw.write(sDocXml);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rWebRowSetEmpty;

	}

	private static ArrayList<Integer> getComponentColumnNos(String ComponentName, WebRowSet RowSetFromExcel) {

		// Craig Adam
		// 20-01-2020

		// ComponentOccuranceIndex - index (base 0) of the component input data set to
		// retrieve
		// This should follow the ComponentName in square brackets ie navMenu[2] would
		// be the 3rd occurance of navMenu in this test case

		// the 2nd time calling this component would be 1.
		// eg fpr a navigate menu component - will only have a single parameter value
		// data set per instance but may occur multiple times in a Test Case
		// ie will never data drive
		


		ArrayList<Integer> lComponentNameColumnNos = new ArrayList<Integer>();
		try {

//			int iTotalRows = RowSetFromExcel.size();

			@SuppressWarnings("unused")
			boolean bRequiredComponentOccurance = false;
			String sCellValue = null;
			@SuppressWarnings("unused")
			String sPreviousComponentName = null;
			int iTotalColumns = RowSetFromExcel.getMetaData().getColumnCount();
			
			// Move to the 1st row - this contains the component names
			RowSetFromExcel.absolute(1);

			// Iterate through all columns
			lComponentNameColumnNos.clear();

			for (int iCurrentColumnNo = 1; iCurrentColumnNo <= iTotalColumns; iCurrentColumnNo++) {

				// Get current component name
				sCellValue = RowSetFromExcel.getString(iCurrentColumnNo); // Current Component Name

				
				
				if (sCellValue.trim().contentEquals(ComponentName.trim())) {

					lComponentNameColumnNos.add(iCurrentColumnNo);
//					System.out.println("[JBDCSupport Ln 823]");
//					System.out.println("sCellValue = " + sCellValue);
//					System.out.println("ComponentName = " + ComponentName);
//					System.out.println("---------");
					
				} else {
					
//					System.out.println("[JBDCSupport Ln 823]");
//					System.out.println("sCellValue = " + sCellValue);
//					System.out.println("ComponentName = " + ComponentName);
//					System.out.println("No match : " + ComponentName + " not equal " + sCellValue);
//					System.out.println("---------");
					
				}
				
				
				
				
				

			}

			// Catch error if no match found
			if(!(lComponentNameColumnNos.size() > 0)) {
				
				throw new CustomException(
						"ComponentName = " + ComponentName + " Not matched in WebRowSet [JDBCSupport Ln 822]");
				
			}
		
			
			
			
			
			// Debug
//			System.out.print("[JDBCSupport Ln 804]");
//			System.out.print("Matching Col Nos = ");
//			
//			for (int i = 0; i < lComponentNameColumnNos.size(); i++) {
//				System.out.print(lComponentNameColumnNos.get(i) + " || ");
//			}
//			
//			System.out.println(System.lineSeparator());
			// Debug

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lComponentNameColumnNos;
	}
 
	

	public static Document removeElement(String Xpath, Document docWebRowSet) {

		// Craig Adam
		// 30-11-2019

		// Removes the given node from the document
		// returns modified document

		Node nDelete = getNodeByXpath(Xpath, docWebRowSet);
		nDelete.getParentNode().removeChild(nDelete);

		return docWebRowSet;

	}

	public static Document setTextContent(String sTextValue, String Xpath, Document docWebRowSet) {

		// Craig Adam
		// 21-11-2019
		// find node by xpath of tag names. set the final nodes textContent

		Node nNode = getNodeByXpath(Xpath, docWebRowSet);
		if (nNode == null) {

			System.out.println("Xpath not valid : " + Xpath);

		} else {

			nNode.setTextContent(sTextValue);

		}

		return docWebRowSet;

	}

	public static String getTextContent(String Xpath, Document docWebRowSet) {

		// Craig Adam
		// 02-12-2019
		// find node by xpath of tag names. set the final nodes textContent

		return getNodeByXpath(Xpath, docWebRowSet).getTextContent();

	}

	public static Node getNodeByXpath(String Xpath, Document docWebRowSet) {

		// Craig Adam
		// 30-11-2019
		// xpath delimiter between nodes "#"

		// TODO ADD validations if node not found

		// Reusable
		Node nCurrentParent = null;
		Node nCurrentChild = null;

		// Split xpath into node tag names
		String[] aXpath = StringUtils.stringSplit(Xpath, "#");

		// Get document root as the 1st parent
		nCurrentParent = docWebRowSet.getDocumentElement();

		// Iterate through dom
		int iTagnameCounter = -1;
		for (int i = 0; i < aXpath.length; i++) {

			// Get all child nodes
			NodeList lMetadataNodes = nCurrentParent.getChildNodes();

			// iterate to find required child node by tag name
			iTagnameCounter = -1; // reset counter for each tier
			for (int j = 0; j < lMetadataNodes.getLength(); j++) {

				nCurrentChild = lMetadataNodes.item(j);
//				System.out.println("nCurrentChild.getNodeName() = " + nCurrentChild.getNodeName());

				String NodeTagName = aXpath[i];

				// Extract NodeIndex from NodeTagName
				int NodeIndex = -1;
				if (NodeTagName.lastIndexOf("[") > 0 && NodeTagName.lastIndexOf("]") > 0
						&& NodeTagName.lastIndexOf("[") < NodeTagName.lastIndexOf("]")) {

					// Extract index
					NodeIndex = Integer.parseInt(NodeTagName.substring(NodeTagName.lastIndexOf("[")).replace("[", "")
							.replace("]", "").trim());
					NodeTagName = NodeTagName.replace("[" + NodeIndex + "]", "");

				} else {

					NodeIndex = 0;

				}

				if (nCurrentChild.getNodeName().contentEquals(NodeTagName)) {

					iTagnameCounter = iTagnameCounter + 1;

					if (iTagnameCounter == NodeIndex) {

//					System.out.println("nCurrentChild.getNodeName() = " + nCurrentChild.getNodeName());

						// This is the node you have been looking for
						// It is the new parent
						nCurrentParent = nCurrentChild;
						break;
					}
				}

				// TODO - Add not found exception!!!
				// TODO - Add not found exception!!!
				// TODO - Add not found exception!!!
				// TODO - Add not found exception!!!
				// TODO - Add not found exception!!!
				// TODO - Add not found exception!!!
				// TODO - Add not found exception!!!
				// TODO - Add not found exception!!!

				// Looped through all available child nodes and match has not been found
				if (j == lMetadataNodes.getLength() - 1) {

					System.out.print("[JDBC Support Ln 1185]");
					System.out.println("NodeTagName = " + NodeTagName + " NOT found");
					
					nCurrentParent = null;
					break;

				}

			}

			if (nCurrentParent == null) {

				break;

			}

		}

		return nCurrentParent;

	}

	public static WebRowSet createKeyValueRowSetFromExcel(String ComponentNameHeader, String KeyHeader,
			String ValueHeader, String ExcelSheetName, String ExcelFilePath) {

		// Craig Adam
		// 1st row reserved for column headers
		// fix auto index of component for reise of same component
		// TODO handle space for index between name and component currently removes space during write
		// 20-01-2020
		// This works with the component index added manually

		// Reusable
		Document docWebRowSet = null;
		FileInputStream fisExcelFile = null;
		Workbook oExcelWorkbook = null;
		Sheet oExcelSheet = null;
		Row oHeaderRow = null;
		Iterator<Cell> cellIterator = null;
		Iterator<Row> rowIterator = null;
		@SuppressWarnings("unused")
		String sCellValue = null;
		@SuppressWarnings("unused")
		String sColumnDataType = null;
		@SuppressWarnings("unused")
		int iColumnIndexCurrent = -1;

		Element eWebRowSet = null;
		Element eProperties = null;
		Element eMetadata = null;
		Element eData = null;

		@SuppressWarnings("unused")
		FormulaEvaluator CellFormulaEvaluator = null;

		try {

			// New document
			DocumentBuilderFactory oDocBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder oDocBuilder;
			oDocBuilder = oDocBuilderFactory.newDocumentBuilder();
			docWebRowSet = oDocBuilder.newDocument();

			// Build basic schema
			// Create webRowSet element
			eWebRowSet = docWebRowSet.createElement("webRowSet");
			eWebRowSet.setAttribute("xmlns", "http://java.sun.com/xml/ns/jdbc");
			eWebRowSet.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			eWebRowSet.setAttribute("xsi:schemaLocation",
					"http://java.sun.com/xml/ns/jdbc http://java.sun.com/xml/ns/jdbc/webrowset.xsd");
			docWebRowSet.appendChild(eWebRowSet);

			// Create properties node
			eProperties = docWebRowSet.createElement("properties");
			eWebRowSet.appendChild(eProperties);

			// Create metadata element
			eMetadata = docWebRowSet.createElement("metadata");
			eWebRowSet.appendChild(eMetadata);

			// Create column count element
			addMetadata_ColumnCount("??", docWebRowSet, eMetadata);

			// Create data element
			eData = docWebRowSet.createElement("data");
			eWebRowSet.appendChild(eData);

			// New excel connection
			fisExcelFile = new FileInputStream(new File(ExcelFilePath));
			oExcelWorkbook = new XSSFWorkbook(fisExcelFile);
//			CellFormulaEvaluator = oExcelWorkbook.getCreationHelper().createFormulaEvaluator();
			oExcelSheet = oExcelWorkbook.getSheet(ExcelSheetName); // Get sheet by name
			oHeaderRow = oExcelSheet.getRow(oExcelSheet.getFirstRowNum()); // Header row

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get key header index
		@SuppressWarnings("unused")
		int iKeyHeaderIndex = -1;
		@SuppressWarnings("unused")
		int iComponentNameHeaderIndex = -1;

		ArrayList<Integer> lValueHeaderIndices = new ArrayList<Integer>();
		cellIterator = oHeaderRow.iterator(); // Header cell iterator
		while (cellIterator.hasNext()) {

			// Get cell value as string
			Cell currentCell = cellIterator.next(); // Header cell
			iColumnIndexCurrent = currentCell.getColumnIndex();
			String sColumnNameCurrent = getCellValueAsString(currentCell, oExcelWorkbook);

//			System.out.println("sColumnNameCurrent = " + sColumnNameCurrent);

			// TODO add more validations on header names
			// TODO add error messages if not found
			// Should not be empty
			// Should not start with a number
			if (sColumnNameCurrent.trim().isEmpty()) {

				break;

			} else if (Character.isDigit(sColumnNameCurrent.charAt(0))) {

				break;

			} else if (sColumnNameCurrent.contentEquals(KeyHeader)) {

				iKeyHeaderIndex = currentCell.getColumnIndex();

			} else if (sColumnNameCurrent.contentEquals(ComponentNameHeader)) {

				iComponentNameHeaderIndex = currentCell.getColumnIndex();
				// Add a row for the headers
				lValueHeaderIndices.add(currentCell.getColumnIndex());

			} else if (sColumnNameCurrent.contentEquals(ValueHeader)) {
				// Add a row for the parameter value (there will be additional ParameterValue
				// headers for data driving a component)
				lValueHeaderIndices.add(currentCell.getColumnIndex());

			}

		} // Header cell iterator

		// Iterate over all excel rows - the values in the key column are to be
		// transposed into the column values (ie parameter names) of the row set

		rowIterator = oExcelSheet.iterator();
		String sColumnName = null;
//		String sCellType = null;
		int iColumnIndexMax = -1;
		while (rowIterator.hasNext()) {

			@SuppressWarnings("unused")
			Row currentRow = rowIterator.next();
			int iCurrentRowIndex = currentRow.getRowNum();

			// if not header row
			if (!currentRow.equals(oHeaderRow)) {

//				System.out.println("iCurrentRowIndex = " + iCurrentRowIndex);

				// Add column to RowSet for each row in the sheet until 1st empty cell value in
				// the key column

				// Get column name
				sColumnName = getCellValueAsString(currentRow.getCell(iKeyHeaderIndex), oExcelWorkbook);

//				System.out.println("sColumnName 241 = " + sColumnName);
//				System.out.println("iCurrentRowIndex 242 = " + iCurrentRowIndex);

				// Check not empty
				if (sColumnName.trim().isEmpty()) {

					iColumnIndexMax = iCurrentRowIndex - 1;
					break;

				}

				// Add node to RowSet xml as data type varchar (12)
				// TODO return type based on cell type and style - based on 1st value column
//				sCellType = getCellType(currentRow.getCell(lValueHeaderIndices.get(0)), oExcelWorkbook);
				// Unable to get read to work with date or time data types
				// Rather format the string when creating data node, based on the cell type and
				// read / write as a string.
				// Can then convert back when read the string from the RowSet
				JdbcUtils.addNode_ColumnDef(docWebRowSet, eMetadata, (iCurrentRowIndex) + "", sColumnName, "12");

				if (!rowIterator.hasNext()) {

					iColumnIndexMax = iCurrentRowIndex;

				}

			}

		}

		// Set column-count
		docWebRowSet = JdbcUtils.setTextContent(iColumnIndexMax + "", "metadata#column-count", docWebRowSet);

//		System.out.print("[JDBCSupport ln 1290]");
//		PrintUtils.printObject(lValueHeaderIndices);

		ArrayList<String> lColumnValues = new ArrayList<String>();

		// for each Value column read all list of string values into an array and write
		// to node
		int iValueColumnIndex = -1;
		for (int i = 0; i < lValueHeaderIndices.size(); i++) {
//			for (int i = 0; i <  - 1; i++) {

//			System.out.println("i ln284 = " + i);

			lColumnValues.clear();
			iValueColumnIndex = lValueHeaderIndices.get(i);

			rowIterator = oExcelSheet.iterator();
			int iCurrentRowIndex = -1;
			while (rowIterator.hasNext()) {

				@SuppressWarnings("unused")
				Row currentRow = rowIterator.next();
				iCurrentRowIndex = currentRow.getRowNum();
				if (!currentRow.equals(oHeaderRow)) {

					if (iCurrentRowIndex <= iColumnIndexMax) {

						// if writing the component name
						if (iValueColumnIndex == iComponentNameHeaderIndex) {

							int iDeltaRow = 0;
							int iCompNameRow = -1;
							;

							// add column value to list
							// handle when blank - step up a row (deltaRow) until get a value it not empty
							do {
								iCompNameRow = iCurrentRowIndex - iDeltaRow;

								// oExcelSheet.getRow(iCompNameRow);
								sCellValue = getCellValueAsString(
										oExcelSheet.getRow(iCompNameRow).getCell(iValueColumnIndex), oExcelWorkbook);

								iDeltaRow = iDeltaRow + 1;

							} while (sCellValue.trim().isEmpty());

// NO - this iterates on every blank cell
//							// Add suffix to component name if was not specifically set ie had to read up
//							// column to get value
//							// This is required to allow for differentiating between two occurrences of a
//							// component that
//							// directly follow each other
//							// used when filtering / extracting the data sets for a specific component index
//							if (iDeltaRow > 1) {
//
//								sCellValue = sCellValue + "[" + (iDeltaRow - 1) + "]";
//
//							}

							lColumnValues.add(sCellValue);
							
						} else {

							// add column value to list
//						System.out.println("value ln298 = "	+ getCellValueAsString(currentRow.getCell(iValueColumnIndex), oExcelWorkbook));
							lColumnValues
									.add(getCellValueAsString(currentRow.getCell(iValueColumnIndex), oExcelWorkbook));

						}

					}

				}

			}

			addNode_CurrentRow(docWebRowSet, eData, lColumnValues);

		}

		// Close workbook
		try {
			oExcelWorkbook.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Transform to DOM
		DOMSource domSource = new DOMSource(docWebRowSet);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		String sDocXml = null;
		try {
			transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			StringWriter sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);
			transformer.transform(domSource, sr);
			sDocXml = sw.toString();

		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Debug
		// write to file
		/*
		 * try { String sOutputFilePath =
		 * ".//src//test//java//codeExamples//JDBC//MsSql//Output//" +
		 * "sample_auto_excel_write" + ".xml"; FileWriter fw; fw = new
		 * FileWriter(sOutputFilePath); fw.write(sDocXml); fw.close(); } catch
		 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */

		WebRowSet rWebRowSetExcel = null;
		try {

			RowSetFactory oRowSetFactory = RowSetProvider.newFactory();
			rWebRowSetExcel = oRowSetFactory.createWebRowSet();

			StringReader stringReader = new StringReader(sDocXml);
			rWebRowSetExcel.readXml(stringReader);

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

//		System.out.print(sDocXml);
//		System.out.print(sRowSetXml);
//		print(rWebRowSetExcel);

		return rWebRowSetExcel;

	}

	
	private static String getCellValueAsString(Cell CellToEvaluate, Workbook ExcelWorkbook) {

		// Craig Adam
		// 26-11-2019

		if (CellToEvaluate != null) {

			FormulaEvaluator CellFormulaEvaluator = ExcelWorkbook.getCreationHelper().createFormulaEvaluator();
			CellType ctCellType = CellFormulaEvaluator.evaluateInCell(CellToEvaluate).getCellType();

			String sCellValue = null;

			switch (ctCellType) {

			case NUMERIC:

				if (DateUtil.isCellDateFormatted(CellToEvaluate)) {

					// Format the return value based on the format of the excel cell
					sCellValue = formatExcelDateTime(CellToEvaluate, ExcelWorkbook);

					if (sCellValue.isEmpty()) {

						sCellValue = " ";
					}

					return sCellValue + "";

				} else {

					sCellValue = CellToEvaluate.getNumericCellValue() + "";
					if (sCellValue.isEmpty()) {

						sCellValue = " ";
					}
					return sCellValue;

				}

			case FORMULA:

				String sCellFormula = CellFormulaEvaluator.evaluateInCell(CellToEvaluate).getCellFormula();
				System.out.println("sCellFormula = " + sCellFormula);

				sCellValue = CellFormulaEvaluator.evaluateFormulaCell(CellToEvaluate) + "";
				if (sCellValue.isEmpty()) {

					sCellValue = " ";
				}
				return sCellValue;

			case BOOLEAN:

				sCellValue = CellToEvaluate.getBooleanCellValue() + "";
				if (sCellValue.isEmpty()) {

					sCellValue = " ";
				}
				return sCellValue;

			case ERROR:

				sCellValue = CellToEvaluate.getErrorCellValue() + "";
				if (sCellValue.isEmpty()) {

					sCellValue = " ";
				}
				return sCellValue;

			case _NONE:
			case BLANK:
			case STRING:

				sCellValue = CellToEvaluate.getStringCellValue();
				if (sCellValue.isEmpty()) {

					sCellValue = " ";
				}
				return sCellValue;

			default:

				// For numeric cells we throw an exception. For blank cells we return an empty
				// string.For formulaCells that are not string Formulas, we throw an exception.
				sCellValue = CellToEvaluate.getStringCellValue();
				if (sCellValue.isEmpty()) {

					sCellValue = " ";
				}
				return sCellValue;

			}

		}

		return " ";

	}

	
	
	
//	public static void print(CachedRowSet CachedRowSet) {
//
//		int iColCount;
//		try {
//			iColCount = CachedRowSet.getMetaData().getColumnCount();
//
//			for (int iColNo = 1; iColNo <= iColCount; iColNo++) {
//
//				System.out.print(CachedRowSet.getMetaData().getColumnName(iColNo) + " || ");
//
//			}
//
//			System.out.print("---End Headers---");
//			System.out.println(System.lineSeparator());
//
//			int iRowCount = (CachedRowSet).size();
//			CachedRowSet.beforeFirst();
//			for (int iRowNo = 1; iRowNo <= iRowCount; iRowNo++) {
//
//				CachedRowSet.next();
//
//				for (int iColNo = 1; iColNo <= iColCount; iColNo++) {
//
//					System.out.print(CachedRowSet.getString(iColNo) + " || ");
//
//				}
//
//				System.out.print("---End Row---");
//				System.out.println(System.lineSeparator());
//			}
//
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
	
	
	
	
	
	private static String formatExcelDateTime(Cell CellToEvaluate, Workbook ExcelWorkbook) {

		// Craig Adam
		// 27-11-2019

//		Date dtCellValue = CellToEvaluate.getDateCellValue(); // instance in time with milli seconds
		LocalDateTime ldtCellValue = CellToEvaluate.getLocalDateTimeCellValue();

		FormulaEvaluator CellFormulaEvaluator = ExcelWorkbook.getCreationHelper().createFormulaEvaluator();
		String cfCellFormat = CellFormulaEvaluator.evaluateInCell(CellToEvaluate).getCellStyle().getDataFormatString();

		String sFormatedDate = null;
		switch (cfCellFormat) {

		case "m/d/yy": // 26/11/2019

			return ldtCellValue.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		case "[$-F800]dddd\\,\\ mmmm\\ dd\\,\\ yyyy": // 26 November 2019

			return ldtCellValue.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

		case "yyyy/mm/dd;@": // 2019/11/26

			return ldtCellValue.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

		case "yy/mm/dd;@": // 19/11/26

			return ldtCellValue.format(DateTimeFormatter.ofPattern("yy/MM/dd"));

		case "yyyy\\-mm\\-dd;@": // 2019-11-26

			return ldtCellValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		case "[$-1C09]dd\\ mmmm\\ yyyy;@": // 26 November 2019

			return ldtCellValue.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

		case "[$-F400]h:mm:ss\\ AM/PM": // 18:45:22

			return ldtCellValue.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

		case "[$-409]hh:mm:ss\\ AM/PM;@": // 06:45:22 PM

			return ldtCellValue.format(DateTimeFormatter.ofPattern("hh:mm:ss"));

		case "hh:mm:ss;@": // 18:45:22

			return ldtCellValue.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

		default:

			System.out.println("The following data format is not mapped - Ln 881 : " + cfCellFormat);
			sFormatedDate = ldtCellValue.format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HHmmss"));

		}

		return sFormatedDate;

	}

	public static Document addMetadata_ColumnCount(String ColumnCount, Document docWebRowSet, Element eMetadata) {

		// Craig Adam
		// 22-11-2019

		// Create column-count element
		Node nColumnCount = docWebRowSet.createElement("column-count");
		eMetadata.appendChild(nColumnCount);
		nColumnCount.appendChild(docWebRowSet.createTextNode(ColumnCount));

		return docWebRowSet;

	}

	public static Document setParameterValue(String KeyName, String ParameterValue, Document docWebRowSet, int iRowIndex) {

		// Craig Adam
		// 02-12-2019
		// set the cell value based on input keyname (column name) and row number
		// iRowNo is base 1

		int iColIndex = JdbcUtils.getColumnIndexByName(KeyName, docWebRowSet);
		JdbcUtils.setTextContent(ParameterValue,
				"data#currentRow[" + (iRowIndex) + "]#columnValue[" + (iColIndex) + "]", docWebRowSet);

		return docWebRowSet;

	}

	public static Document addNode_CurrentRow(Document docWebRowSet) {

		// Craig Adam
		// 02-12-2019
		// Adds currentRow node with currentValue "RowPlaceholder" value

		// Grab the data node and cast to an element
//		Element eData = (Element) docWebRowSet.getElementsByTagName("data").item(0); // only one grab it
		Element eData = (Element) getNodeByXpath("data", docWebRowSet);
		
 		// Create new currentRow in data node
		Element currentRow = docWebRowSet.createElement("currentRow");
		eData.appendChild(currentRow);

		return docWebRowSet;

	}
	
	public static Document addNode_CurrentValue(Document docWebRowSet, int iCurrentRowIndex) {

		// Craig Adam
		// 02-12-2019
		// Adds currentRow node

		// Grab the data node and cast to an element
//		Element eData = (Element) docWebRowSet.getElementsByTagName("data").item(0); // only one grab it
//		Element eData = (Element) getNodeByXpath("data", docWebRowSet);
		
 		// Grab the currentRow in data node
		Element currentRow = (Element) getNodeByXpath("data#currentRow" + "[" + iCurrentRowIndex + "]", docWebRowSet);
		
		Element nodeName = docWebRowSet.createElement("columnValue");
		nodeName.appendChild(docWebRowSet.createTextNode("RowPlaceholder"));
		currentRow.appendChild(nodeName);

		return docWebRowSet;

	}

	public static void addNode_CurrentRow(Document docWebRowSet, ArrayList<String> lColumnValues) {

		// Craig Adam
		// 22-11-2019
		// Adds currentRow node

		// Grab the data node and cast to an element
		Element eData = (Element) docWebRowSet.getElementsByTagName("data").item(0); // only one grab it

		// Create new currentRow in data node
		Element currentRow = docWebRowSet.createElement("currentRow");
		eData.appendChild(currentRow);

		// Create empty node for each column in the row
		for (int i = 0; i <= lColumnValues.size() - 1; i++) {

			Element nodeName = docWebRowSet.createElement("columnValue");
			nodeName.appendChild(docWebRowSet.createTextNode(lColumnValues.get(i)));
			currentRow.appendChild(nodeName);
		}

	}

	public static Document addNode_CurrentRow(Document docWebRowSet, Element eData, ArrayList<String> lColumnValues) {

		// Depreciated - grab

		// Craig Adam
		// 22-11-2019
		// Adds placeholder currentRow and columnValue nodes

		// Create new currentRow in data node
		Element currentRow = docWebRowSet.createElement("currentRow");
		eData.appendChild(currentRow);

		// Create empty node for each column in the row
		for (int i = 0; i <= lColumnValues.size() - 1; i++) {

			Element nodeName = docWebRowSet.createElement("columnValue");
			nodeName.appendChild(docWebRowSet.createTextNode(lColumnValues.get(i)));
			currentRow.appendChild(nodeName);
		}
		return docWebRowSet;

	}

	public static Document addNode_ColumnDef(Document docWebRowSet, HashMap<String, String> ElementValues) {

		// Craig Adam
		// 30-11-2019
		// Adds column-definition node and its children
		// Version 2: updated to include the following as element values : String
		// iColumnIndex, String sColumnName,String sColumnTypeInt
		// Version 3: remove Element eMetadata as an input ... grab the element from the
		// xml document
		// Version 2: add return type as document so updates as you go

		// TODO catch mandatory inputs in HashMap

		// Example input
//		HashMap<String, String> mElementValues = new HashMap<String, String>();
//		mElementValues.clear();
//		mElementValues.put("column-index", 3);
//		mElementValues.put("column-name", Formula15);
//		mElementValues.put("column-type", "12"); // generic database type
//		addNode_ColumnDef(docRowSet, eMetadata, mElementValues);

//		// Example output
//		<column-definition>
//		<column-index>3</column-index>
//		<auto-increment>false</auto-increment>
//		<case-sensitive>false</case-sensitive>
//		<column-name>Formula15</column-name>
//		<column-type>12</column-type>

		// Grab the metadata node and case to an element
		Element eMetadata = (Element) docWebRowSet.getElementsByTagName("metadata").item(0); // only one grab it

		// Create column-definition element
		Element eColumnDef = docWebRowSet.createElement("column-definition");
		eMetadata.appendChild(eColumnDef);

		// Create column-index element - Mandatory
		Element eColumnIndex = docWebRowSet.createElement("column-index");
		eColumnDef.appendChild(eColumnIndex);
		eColumnIndex.appendChild(docWebRowSet.createTextNode(ElementValues.get("column-index")));

		// Auto increment - false for excel - Optional
		Element eAutoIncrement = docWebRowSet.createElement("auto-increment");
		eColumnDef.appendChild(eAutoIncrement);
		eAutoIncrement.appendChild(docWebRowSet.createTextNode("false"));

		// Case sensitive - Optional
		Element eCaseSensitive = docWebRowSet.createElement("case-sensitive");
		eColumnDef.appendChild(eCaseSensitive);
		eCaseSensitive.appendChild(docWebRowSet.createTextNode("false"));

		// Create column-name element - Mandatory
		Element eColumnName = docWebRowSet.createElement("column-name");
		eColumnDef.appendChild(eColumnName);
		eColumnName.appendChild(docWebRowSet.createTextNode(ElementValues.get("column-name")));

		// Create column-type element - Mandatory
		Element eColumnType = docWebRowSet.createElement("column-type");
		eColumnDef.appendChild(eColumnType);

//		// Map column type name to column type (integer representing generic Database type)
//		String sColumnTypeInt = mapTypeNameToTypeInt(sColumnTypeName);
		eColumnType.appendChild(docWebRowSet.createTextNode(ElementValues.get("column-type")));

		return docWebRowSet;

	}

	public static void addNode_ColumnDef(Document docWebRowSet, Element eMetadata,
			HashMap<String, String> ElementValues) {

// Depreciated

		// Craig Adam
		// 30-11-2019
		// Adds column-definition node and its children
		// Version 2: updated to include the following as element values : String
		// iColumnIndex, String sColumnName,String sColumnTypeInt

		// TODO catch mandatory inputs

		// Example input
//		HashMap<String, String> mElementValues = new HashMap<String, String>();
//		mElementValues.clear();
//		mElementValues.put("column-index", 3);
//		mElementValues.put("column-name", Formula15);
//		mElementValues.put("column-type", "12"); // generic database type
//		addNode_ColumnDef(docRowSet, eMetadata, mElementValues);

//		// Example output
//		<column-definition>
//		<column-index>3</column-index>
//		<auto-increment>false</auto-increment>
//		<case-sensitive>false</case-sensitive>
//		<column-name>Formula15</column-name>
//		<column-type>12</column-type>

		// Create column-definition element
		Element eColumnDef = docWebRowSet.createElement("column-definition");
		eMetadata.appendChild(eColumnDef);

		// Create column-index element - Mandatory
		Element eColumnIndex = docWebRowSet.createElement("column-index");
		eColumnDef.appendChild(eColumnIndex);
		eColumnIndex.appendChild(docWebRowSet.createTextNode(ElementValues.get("column-index")));

		// Auto increment - false for excel - Optional
		Element eAutoIncrement = docWebRowSet.createElement("auto-increment");
		eColumnDef.appendChild(eAutoIncrement);
		eAutoIncrement.appendChild(docWebRowSet.createTextNode("false"));

		// Case sensitive - Optional
		Element eCaseSensitive = docWebRowSet.createElement("case-sensitive");
		eColumnDef.appendChild(eCaseSensitive);
		eCaseSensitive.appendChild(docWebRowSet.createTextNode("false"));

		// Create column-name element - Mandatory
		Element eColumnName = docWebRowSet.createElement("column-name");
		eColumnDef.appendChild(eColumnName);
		eColumnName.appendChild(docWebRowSet.createTextNode(ElementValues.get("column-name")));

		// Create column-type element - Mandatory
		Element eColumnType = docWebRowSet.createElement("column-type");
		eColumnDef.appendChild(eColumnType);

//		// Map column type name to column type (integer representing generic Database type)
//		String sColumnTypeInt = mapTypeNameToTypeInt(sColumnTypeName);
		eColumnType.appendChild(docWebRowSet.createTextNode(ElementValues.get("column-type")));

	}

	public static void addNode_ColumnDef(Document docWebRowSet, Element eMetadata, String iColumnIndex,
			String sColumnName, String sColumnTypeInt) {

// Depreciated

		// Craig Adam
		// 22-11-2019
		// Adds column-definition node and its children
		// TODO change input to a HashMap to cater for optional

		// Create column-definition element
		Element eColumnDef = docWebRowSet.createElement("column-definition");
		eMetadata.appendChild(eColumnDef);

		// Create column-index element - Mandatory
		Element eColumnIndex = docWebRowSet.createElement("column-index");
		eColumnDef.appendChild(eColumnIndex);
		eColumnIndex.appendChild(docWebRowSet.createTextNode(iColumnIndex));

		// Auto increment - false for excel - Optional
		Element eAutoIncrement = docWebRowSet.createElement("auto-increment");
		eColumnDef.appendChild(eAutoIncrement);
		eAutoIncrement.appendChild(docWebRowSet.createTextNode("false"));

		// Case sensitive - Optional
		Element eCaseSensitive = docWebRowSet.createElement("case-sensitive");
		eColumnDef.appendChild(eCaseSensitive);
		eCaseSensitive.appendChild(docWebRowSet.createTextNode("false"));

		// Create column-name element - Mandatory
		Element eColumnName = docWebRowSet.createElement("column-name");
		eColumnDef.appendChild(eColumnName);
		eColumnName.appendChild(docWebRowSet.createTextNode(sColumnName));

		// Create column-type element - Mandatory
		Element eColumnType = docWebRowSet.createElement("column-type");
		eColumnDef.appendChild(eColumnType);

//		// Map column type name to column type (integer representing generic Database type)
//		String sColumnTypeInt = mapTypeNameToTypeInt(sColumnTypeName);
		eColumnType.appendChild(docWebRowSet.createTextNode(sColumnTypeInt));

	}

	public static ResultSet executeQuery(String QueryString, Connection conn) {

		// Craig Adam
		ResultSet rs = null;

		try {
			rs = conn.createStatement().executeQuery(QueryString);

//			// Iterate through the data in the result set and print to console
//			while (rs.next()) {
//				System.out.println(rs.getString("Object_Name") + " " + rs.getString("sValue"));
//			}

//			System.out.println("getHoldability = " + rs.getHoldability());

//			ResultSetMetaData oResultSetMetaDataObject = rs.getMetaData();
			
			
//			int iNoColumns = oResultSetMetaDataObject.getColumnCount();
//			for (int iCurrentColumn = 1; iCurrentColumn <= iNoColumns; iCurrentColumn++) {

//				System.out.println("i = " + iCurrentColumn);
//				System.out.println("getColumnName = " + oResultSetMetaDataObject.getColumnName(iCurrentColumn));
//				System.out.println("getColumnTypeName = " + oResultSetMetaDataObject.getColumnTypeName(iCurrentColumn));

//				System.out.println("getColumnLabel = " + oResultSetMetaDataObject.getColumnLabel(iCurrentColumn));

//				System.out.println("getColumnClassName = " + oResultSetMetaDataObject.getColumnClassName(iCurrentColumn));
//				System.out.println("getColumnType = " + oResultSetMetaDataObject.getColumnType(iCurrentColumn));

//				System.out.println("getSchemaName = " + oResultSetMetaDataObject.getSchemaName(iCurrentColumn));
//				System.out.println("getTableName = " + oResultSetMetaDataObject.getTableName(iCurrentColumn));

//			System.out.println("");
//			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rs;

	}

	public static Connection getDbConnection(HashMap<String, Object> ConnectionDetails) {

		// Craig Adam
		// returns connection object based on details provided

//	Example of input required for windows authentication
//		HashMap<String, Object> mConnectionDetails = new HashMap<String, Object>();
//		mConnectionDetails.put("ServerType", "sqlserver"); // Mandatory eg "sqlserver" --> MS Server
//		mConnectionDetails.put("ServerUrl", "//srv007403"); // Mandatory eg "//srv007403" or "//srv006575"
//		mConnectionDetails.put("DatabaseName", "gsa_auto_training_db"); // Mandatory	
//		mConnectionDetails.put("IntegratedSecurity", true); // Optional Boolean - set true for windows authentication
//		mConnectionDetails.put("FolderPathSqlAuth", ".//src//test//java//codeExamples//JDBC//MsSql//Sql_AuthX64"); // Mandatory if using windows authentication - can be absolute or relative to current project src
//		Connection conn = getDbConnection(mConnectionDetails);

		Connection connObj = null;

		String connectionUrl = ""; // start with clear connection string
		connectionUrl += "jdbc:"; //
		connectionUrl += ConnectionDetails.get("ServerType").toString(); //
		connectionUrl += ":"; //
		connectionUrl += ConnectionDetails.get("ServerUrl").toString(); //
		connectionUrl += ";"; //
		connectionUrl += "databaseName=";
		connectionUrl += ConnectionDetails.get("DatabaseName").toString(); //

		if (ConnectionDetails.containsKey("IntegratedSecurity")) {

			if (!ConnectionDetails.get("IntegratedSecurity").toString().trim().isEmpty()) {

				connectionUrl += ";"; //
				connectionUrl += "integratedSecurity=";
				connectionUrl += ConnectionDetails.get("IntegratedSecurity").toString(); //

			}

			// Before using windows authentication need to add sqljdbc_auth.dll to file path
			if ((boolean) ConnectionDetails.get("IntegratedSecurity")) {

				final String sLocationOfDll = convertRelToAbsPath(
						".//src//test//java//codeExamples//JDBC//MsSql//Sql_AuthX64");
				appendToPath(sLocationOfDll);
				// appendToPath(convertRelToAbsPath(ConnectionDetails.get("FolderPathSqlAuth").toString()));

			}

		}

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connObj = DriverManager.getConnection(connectionUrl);
			if (connObj != null) {
//				DatabaseMetaData metaObj = (DatabaseMetaData) connObj.getMetaData();

//				System.out.println("Driver Name = " + metaObj.getDriverName());
//				System.out.println("Driver Version = " + metaObj.getDriverVersion());
//				System.out.println("Product Name = " + metaObj.getDatabaseProductName());
//				System.out.println("Product Version = " + metaObj.getDatabaseProductVersion());

			}
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		}

		return connObj;
	}

	public static String queryRunValueTable(int FK_Component_PKID, int FK_Keyword_PKID, String DatabaseName) {

		// Craig Adam
		// 12-11-2019

//		SELECT TOP 1000 [FK_Keyword_PKID]
//	      ,[FK_Component_PKID]
//	    	      ,[FK_Component_Index]
//	    	      ,[FK_Component_Sequence]
//	    	      ,[FK_Object_PKID]
//	    	      ,[Object_Name]
//	    	      ,[ObjectName_Description]
//	    	      ,[Object_Sequence]
//	    	      ,[sValue]
//	    	  FROM [gsa_auto_training_db].[dbo].[RN_Value_tbl]
//	    	  WHERE FK_Component_PKID = 2 AND	FK_Keyword_PKID = 26
//		
//		/****** WHERE FK_Component_PKID = 2 AND FK_Keyword_PKID = 26 ******/
//		/****** WHERE FK_Keyword_PKID = 26 ******/
//		/****** WHERE FK_Component_PKID = 2 ******/

		// Build query string
		String strSQL = ""; // Start with a clean string
		strSQL += "Select"; // Action
		strSQL += " "; //
		strSQL += "All"; // Which records to retrieve eg "Top 1000" OR "All"
		strSQL += " "; //
		strSQL += "*"; // Columns to include "*" ==> All
		strSQL += " "; //
		strSQL += "FROM"; //
		strSQL += " "; //
		strSQL += "["; //
		strSQL += DatabaseName;
		strSQL += "]"; //
		strSQL += ".[dbo]."; //
		strSQL += "["; //
		strSQL += "RN_Value_tbl"; // 'Add name of table
		strSQL += "]"; //

		// Add Where conditions if there are additional selection conditions
		if (FK_Component_PKID > 0 || FK_Keyword_PKID > 0) {

			strSQL += " "; //
			strSQL += "WHERE"; //
			strSQL += " "; //
		}

		// Add match condition for Component ID
		if (FK_Component_PKID > 0) {

			strSQL += "FK_Component_PKID"; // Add database column name
			strSQL += " = ";
			strSQL += FK_Component_PKID; // Add input parameter for component ID that should be matched
		}

		// Add match condition for Keyword ID
		if (FK_Keyword_PKID > 0) {

			// Check if already added a condition
			if (FK_Component_PKID > 0) {

				// Include AND
				strSQL += " "; //
				strSQL += "AND"; //
				strSQL += " "; //

			}

			strSQL += "FK_Keyword_PKID"; // Add database column name"
			strSQL += " = "; //
			strSQL += FK_Keyword_PKID; //

		}

		return strSQL;

	}

	/////////////////////////////////////////////
	// check if add to reusable method library
	/////////////////////////////////////////////
	public static void saveStringToFile(String InputString, String OutputFilePath) {

		// Craig Adam
		// 30-11-2019

		// TODO desc
//		System.out.print("[JDBC Support Ln 2170]");
//		System.out.println("File saved to : " + System.lineSeparator() + OutputFilePath);
//		System.out.println("");
		
		try {

			FileWriter fw;
			fw = new FileWriter(OutputFilePath);
			fw.write(InputString);
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/////////////////////////////////////////////
	// already in reusable method library
	/////////////////////////////////////////////
	private static void appendToPath(String dir) {

		// add a path to the sys path
		// use to add the .dll required for windows authentication when connection to MS
		// SQL Server
		// https://stackoverflow.com/questions/11707056/no-sqljdbc-auth-in-java-library-path

		String path = System.getProperty("java.library.path");
		path = dir + ";" + path;
		System.setProperty("java.library.path", path);

		try {

			final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
			sysPathsField.setAccessible(true);
			sysPathsField.set(null, null);

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

	}

	private static String convertRelToAbsPath(String RelativeFilePath) {

		// Craig Adam

		// Starts with "." then is a relative path
		if (RelativeFilePath.substring(0, 1).contentEquals(".")) {

			String sAbsPath = (System.getProperty("user.dir") + RelativeFilePath.substring(1, RelativeFilePath.length())
					.replaceFirst(".", "").replace("//", System.getProperty("file.separator")))
							.replace(System.getProperty("file.separator"), "/");

			return sAbsPath;
		}

		// Already an absolute path return
		return RelativeFilePath;

	}

	
	
	/////////////////////////////////////////////
	/////////////////////////////////////////////
	// Pankaj Methods
	/////////////////////////////////////////////
	/////////////////////////////////////////////
	
	/*
	 * This Method is to check if component steps needs to be executed by checking the status of the component
	 * Returns Yes if the status of the component is Pass
	 */
		public static String doProceed(CachedRowSet CachedRowSet) {

//			int iColCount;
			
			try {
				  CachedRowSet.beforeFirst();  
				  CachedRowSet.next();
				//  String componentName = CachedRowSet.getString("ComponentName").trim(); //Get the name of the Component which is running, initialised in Driver class
				  String Status = CachedRowSet.getString("ComponentStatus").trim();
				  
			    if (Status.equals("Pass")) {
			    	return "Yes";				
			  }
					
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "No";
		}
		
		/*
		 * This Method is to check if component needs to be iterated through.
		 * Returns Yes if there is any single value in any of the column for that row  	
		 */
			public static String doIterate(CachedRowSet CachedRowSet) {

				int iColCount;
				
				try {
			
					iColCount = CachedRowSet.getMetaData().getColumnCount();		
				  for (int iColNo = 1; iColNo <= iColCount; iColNo++) {
					String testData = CachedRowSet.getString(iColNo).trim();	
				    if (testData.length()>0) {
				    	
				    	// TODO desc
//				    	System.out.println("TestData [JDBC Support Ln 82] : " + testData);
				    	return "Yes";
				    }
					
				  }
						
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "No";
			}	
		
}
