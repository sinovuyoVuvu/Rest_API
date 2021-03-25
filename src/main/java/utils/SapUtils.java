package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

import javax.sql.rowset.FilteredRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.sql.rowset.WebRowSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import utils.FileFolderUtils;

import utils.SapUtils;
import utils.rowSets.Filter_ByIntRange;
import utils.rowSets.Filter_ByRegex;


public class SapUtils extends ReportingUtils {

	final static String SettingsFile = ".//src//main//java//config//Settings.xml";
	public static WebRowSet rWebRowKeyValueOutput;
     
	public SapUtils() {
		
	
		
		try {
			
//			System.out.println("Intitialised WebBase [WebBase ln76] ");
			
			SettingsUtils.initSettings();			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	////////////////////
	////////////////////
	// PUBLIC METHODS
	////////////////////
	////////////////////

	public static Connection connectSap() {

		// Craig Adam
		// 22-01-2020

		Connection connHanaDb = null;

		try {

		//	SettingsUtils.initSettings(); // initialise settings.xml
			System.out.println(rWebRowKeyValueOutput.getString("ComponentStatus"));
//			System.out.println("[SapUtils ln 73 : " + SettingsUtils.getNodeValueByName("url", "testEnvironment", "testEnv"));
//			System.out.println("[SapUtils ln 74 : " + SettingsUtils.getNodeValueByName("port", "testEnvironment", "testEnv"));

			String connectionString = "jdbc:sap:"
					+ SettingsUtils.getNodeValueByName("url", "testEnvironment", "testEnv") + ":"
					+ SettingsUtils.getNodeValueByName("port", "testEnvironment", "testEnv");


			connHanaDb = DriverManager.getConnection(connectionString, SettingsUtils.getMudId(),
					utils.MaskUtils.unmaskString((SettingsUtils.getPassword().trim())));
			
			
			
			// Check if connection is valid
			if (connHanaDb.isValid(0)) {

//				System.out.println("SAP_Connection_JDBC to HANA successful");

			} else {
				rWebRowKeyValueOutput = JdbcUtils.setKeyValuePair("ComponentStatus", "Fail", rWebRowKeyValueOutput); // Setting up ComponentStatus column with dummy ComponentName as in 1stRow
				System.out.println("report error [ Ln 66]");
				rWebRowKeyValueOutput.next();
			}

		} catch (SQLException e) {

			System.err.println("SAP_Connection_JDBC Failed. User/Passwd Error? Message: " + e.getMessage());

			return null;
		}

		return connHanaDb;

	}

	public static void disconnectSap(Connection connHanaDb) {

		// Craig Adam
		// 22-01-2020

		try {

			connHanaDb.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String getValueAsString(int ColumnNo, WebRowSet webRowSet) {

		// TODO updated to read all as String Craig Adam - 10-02-2020

		// Craig Adam
		// 28-02-2020
		// Map Sap Hana data types to and return as string
		// https://www.cis.upenn.edu/~bcpierce/courses/629/jdkdocs/guide/jdbc/getstart/mapping.doc.html

		
		
		
		String returnString = null;
		
		
		/*
		 * try { webRowSet.first(); } catch (SQLException e1) { // TODO Auto-generated
		 * catch block e1.printStackTrace(); }
		 */

//		ResultSetMetaData rsmd = JdbcUtils.getMetaData(webRowSetOriginal);



//		try {

//			System.out.println("Ln123");
//			int originalCursorPostion = webRowSetOriginal.getRow();
//			System.out.println("originalCursorPostion = " + originalCursorPostion);

			// TODO trying to fix cursor issue when calling this method in a while loop that
			// also uses the original WebRowSet.
			// If does not work / is not the reason for the error can remove - Craig Adam
			// Example is SAP_Hana_Count_Schema_Name_on_Tables

			// Create a copy so do not mess with cursor position - Craig Adam 04-02-2020
//			WebRowSet webRowSet = (WebRowSet) webRowSetOriginal.createCopyNoConstraints(); // Clone failed: codeExamples.JDBC.FilteredRowSet.Filter_ByRegex
//			WebRowSet webRowSet = (WebRowSet) webRowSetOriginal.createCopy(); // Clone failed: codeExamples.JDBC.FilteredRowSet.Filter_ByRegex
//			RowSetFactory oRowSetFactory = RowSetProvider.newFactory();
//			WebRowSet webRowSet = oRowSetFactory.createFilteredRowSet();
//			webRowSet.populate(webRowSetOriginal);
//			webRowSet.absolute(originalCursorPostion);

//			switch (JdbcUtils.getMetaData(webRowSet).getColumnTypeName(ColumnNo).toString()) {
//
//			String switchType = JdbcUtils.getMetaData(webRowSet).getColumnTypeName(ColumnNo).toString();
//
//			switch (switchType) {
//
//			case "TINYINT":
//			case "SMALLINT":
//
//				returnString = ("" + webRowSet.getShort(ColumnNo));
//				break;
//
//			case "INTEGER":
//
//				returnString = ("" + webRowSet.getInt(ColumnNo));
//				break;
//
//			case "BIGINT":
//
//				returnString = ("" + webRowSet.getLong(ColumnNo));
//				
//				// Try return sting getting failure here
//			//	returnString = ("" + webRowSet.getString(ColumnNo));
//				
//				break;
//
//			case "FLOAT":
//			case "REAL":
//
//				returnString = ("" + webRowSet.getFloat(ColumnNo));
//				break;
//
//			case "DECIMAL":
//			case "NUMERIC":
//
//				returnString = ("" + webRowSet.getBigDecimal(ColumnNo));
//				break;
//
//			case "CHAR":
//			case "VARCHAR":
//			case "LONGVARCHAR":
//			case "NVARCHAR":
//				
//				returnString = ("" + webRowSet.getString(ColumnNo));
//				break;
//
//			case "BINARY":
//			case "VARBINARY":
//
//				returnString = ("" + webRowSet.getByte(ColumnNo));
//				break;
//
//			case "LONGVARBINARY":
//
//				returnString = ("" + webRowSet.getBinaryStream(ColumnNo));
//				break;
//
//			case "BIT":
//
//				returnString = ("" + webRowSet.getBoolean(ColumnNo));
//				break;
//
//			case "DATE":
//
//				// returnString = ("" + webRowSet.getDate(ColumnNo)).trim();
//				// getDate Failed on value ( 2000-08-08 ) in column 4 no conversion available
//				// will need to read these dates as a String
//				returnString = ("" + webRowSet.getString(ColumnNo));
//				break;
//
//			case "TIME":
//
//				// returnString = ("" + webRowSet.getTime(ColumnNo));
//				// in column xxx no conversion available
//				// will need to read these dates as a String
//				returnString = ("" + webRowSet.getString(ColumnNo));
//
//				break;
//
//			case "TIMESTAMP":
//
//				 returnString = ("" + webRowSet.getTimestamp(ColumnNo));
//				// getTimestamp Failed on value xyz in column abc no conversion available
//				// will need to read these dates as a String
//				// returnString = ("" + webRowSet.getString(ColumnNo));
//
//				break;
//				
//			case "DOUBLE":
//
//				 returnString = ("" + webRowSet.getDouble(ColumnNo));
//
//
//				break;	
//
//			default:
//
//				returnString = ("" + webRowSet.getString(ColumnNo));
//
//				// System.out.println("Unmapped data type [SapUtils Ln 249] ");
//
//				break;
//			}
//
//		} catch (SQLException e) {

			// TODO Auto-generated catch block
//			try {
//
//
//				
//			} catch (SQLException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//
//			e.printStackTrace();
//		}
//
//		if (!(returnString.length() > 0)) {
//
//			System.out.println("length space = " + (" ").length());
//			System.out.println("returnString.length() = " + returnString.length());
//			returnString = " ";
//
//		}

		try {
			
	//		System.out.println("switchType [SapUtils Ln 291] = + JdbcUtils.getMetaData(webRowSet).getColumnTypeName(ColumnNo).toString());
			returnString = ("" + webRowSet.getString(ColumnNo));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return returnString;

//		 generic SQL data types as follows
//		https://alvinalexander.com/java/edu/pj/jdbc/recipes/ResultSet-ColumnType.shtml

//				-7	BIT
//				-6	TINYINT
//				-5	BIGINT
//				-4	LONGVARBINARY 
//				-3	VARBINARY
//				-2	BINARY
//				-1	LONGVARCHAR
//				0	NULL
//				1	CHAR
//				2	NUMERIC
//				3	DECIMAL
//				4	INTEGER
//				5	SMALLINT
//				6	FLOAT
//				7	REAL
//				8	DOUBLE
//				12	VARCHAR
//				91	DATE
//				92	TIME
//				93	TIMESTAMP
//				1111 	OTHER

	}

	// TODO reuse the read back in a separate method so do not have to change in 2
	// places
	public static String getValueAsString(int RowNo, int ColumnNo, WebRowSet webRowSetOriginal) {

		// TODO updated to read all as String Craig Adam - 10-02-2020

		// Craig Adam
		// 28-02-2020
		// Map Sap Hana data types to and return as string
		// https://www.cis.upenn.edu/~bcpierce/courses/629/jdkdocs/guide/jdbc/getstart/mapping.doc.html

		/*
		 * try { webRowSet.first(); } catch (SQLException e1) { // TODO Auto-generated
		 * catch block e1.printStackTrace(); }
		 */

//		ResultSetMetaData rsmd = JdbcUtils.getMetaData(webRowSetOriginal);

		String returnString = null;

		try {

//			System.out.println("Ln123");
//			int originalCursorPostion = webRowSetOriginal.getRow();
//			System.out.println("originalCursorPostion = " + originalCursorPostion);

			// TODO trying to fix cursor issue when calling this method in a while loop that
			// also uses the original WebRowSet.
			// If does not work / is not the reason for the error can remove - Craig Adam
			// Example is SAP_Hana_Count_Schema_Name_on_Tables

			// Create a copy so do not mess with cursor position - Craig Adam 04-02-2020
//			WebRowSet webRowSet = (WebRowSet) webRowSetOriginal.createCopyNoConstraints(); // Clone failed: codeExamples.JDBC.FilteredRowSet.Filter_ByRegex
//			WebRowSet webRowSet = (WebRowSet) webRowSetOriginal.createCopy(); // Clone failed: codeExamples.JDBC.FilteredRowSet.Filter_ByRegex
			RowSetFactory oRowSetFactory = RowSetProvider.newFactory();
			WebRowSet webRowSet = oRowSetFactory.createFilteredRowSet();
			webRowSet.populate(webRowSetOriginal);
			webRowSet.absolute(RowNo);

			switch (JdbcUtils.getMetaData(webRowSet).getColumnTypeName(ColumnNo).toString()) {

//			case "TINYINT":
//			case "SMALLINT":
//
//				returnString = ("" + webRowSet.getShort(ColumnNo));
//				break;
//
//			case "INTEGER":
//
//				returnString = ("" + webRowSet.getInt(ColumnNo));
//				break;
//
//			case "BIGINT":
//
//				returnString = ("" + webRowSet.getLong(ColumnNo));
//				break;
//
//			case "FLOAT":
//			case "REAL":
//
//				returnString = ("" + webRowSet.getFloat(ColumnNo));
//				break;
//
//			case "DECIMAL":
//			case "NUMERIC":
//
//				returnString = ("" + webRowSet.getBigDecimal(ColumnNo));
//				break;
//
//			case "CHAR":
//			case "VARCHAR":
//			case "LONGVARCHAR":
//			case "NVARCHAR":
//
//				returnString = ("" + webRowSet.getString(ColumnNo));
//				break;
//
//			case "BINARY":
//			case "VARBINARY":
//
//				returnString = ("" + webRowSet.getByte(ColumnNo));
//				break;
//
//			case "LONGVARBINARY":
//
//				returnString = ("" + webRowSet.getBinaryStream(ColumnNo));
//				break;
//
//			case "BIT":
//
//				returnString = ("" + webRowSet.getBoolean(ColumnNo));
//				break;
//
//			case "DATE":
//
//				// returnString = ("" + webRowSet.getDate(ColumnNo)).trim();
//				// getDate Failed on value ( 2000-08-08 ) in column 4 no conversion available
//				// will need to read these dates as a String
//				returnString = ("" + webRowSet.getString(ColumnNo));
//				break;
//
//			case "TIME":
//
//				// returnString = ("" + webRowSet.getTime(ColumnNo));
//				// in column xxx no conversion available
//				// will need to read these dates as a String
//				returnString = ("" + webRowSet.getString(ColumnNo));
//
//				break;
//
//			case "TIMESTAMP":
//
//				// returnString = ("" + webRowSet.getTimestamp(ColumnNo));
//				// getTimestamp Failed on value xyz in column abc no conversion available
//				// will need to read these dates as a String
//				returnString = ("" + webRowSet.getString(ColumnNo));
//
//				break;
//
//			case "DOUBLE":
//
//				 returnString = ("" + webRowSet.getDouble(ColumnNo));
//
//
//				break;		

			default:

				returnString = ("" + webRowSet.getString(ColumnNo));
				// System.out.println("Unmapped data type [SAPSupport Ln 421] ");

				break;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!(returnString.length() > 0)) {

//			System.out.println("length space = " + (" ").length());
//			System.out.println("returnString.length() = " + returnString.length());
			returnString = " ";

		}

		return returnString;

//		 generic SQL data types as follows
//		https://alvinalexander.com/java/edu/pj/jdbc/recipes/ResultSet-ColumnType.shtml

//				-7	BIT
//				-6	TINYINT
//				-5	BIGINT
//				-4	LONGVARBINARY 
//				-3	VARBINARY
//				-2	BINARY
//				-1	LONGVARCHAR
//				0	NULL
//				1	CHAR
//				2	NUMERIC
//				3	DECIMAL
//				4	INTEGER
//				5	SMALLINT
//				6	FLOAT
//				7	REAL
//				8	DOUBLE
//				12	VARCHAR
//				91	DATE
//				92	TIME
//				93	TIMESTAMP
//				1111 	OTHER

	}



	
	public static ResultSet querySap_ResultSet(String sQuery, Connection conn) {

		// Craig Adam
		// 22-01-2020
		// execute query against database
		// returns result set

		ResultSet resultSet = null;

		if (sQuery != null && sQuery.trim().length() > 0) {

			if (conn != null) {
				try {

					Statement stmt = conn.createStatement();
					resultSet = stmt.executeQuery(sQuery);

				} catch (SQLException e) {

					System.err.println("Query failed!");
				}

			} else {

				System.out.println("connection is null [ Ln 130]");

			}

		} else {

			System.out.println("query is null [ Ln 136]");

		}

		return resultSet;

	}

	public static ResultSet querySap_ResultSet(String sQuery) {

		// Craig Adam
		// 22-01-2020
		// execute query against database
		// returns result set

		ResultSet resultSet = null;

		Connection conn = SapUtils.connectSap();

		if (sQuery != null && sQuery.trim().length() > 0) {

			if (conn != null) {
				try {

					Statement stmt = conn.createStatement();
					resultSet = stmt.executeQuery(sQuery);

				} catch (SQLException e) {

					System.err.println("Query failed!");
				}

			} else {

				System.out.println("connection is null [ Ln 130]");

			}

		} else {

			System.out.println("query is null [ Ln 136]");

		}

		return resultSet;

	}

	public static WebRowSet querySap_WebRowSet(String queryString) {

		// Craig Adam
		// 22-01-2020
		// returns disconnected WebRowSet based on result of queryString
		// will connect to database using details in settings.xml
		// returns disconnected WebRowSet

		ResultSet resultSet = null;
		WebRowSet webRowSet = null;
		RowSetFactory oRowSetFactory;

		// Connect to Hana based on settings xml file
		Connection connHanaDb = connectSap();

		if (queryString != null && queryString.trim().length() > 0) {

			if (connHanaDb != null) {

				try {

					// Get ResultSet
					resultSet = connHanaDb.createStatement().executeQuery(queryString);

					// Create WebRowSet from ResultsSet object
					oRowSetFactory = RowSetProvider.newFactory();
					webRowSet = oRowSetFactory.createWebRowSet();
					webRowSet.populate(resultSet);
//					webRowSet.beforeFirst();

//					System.out.println("getWarnings [JdbcUtils Ln 194] = " + webRowSet.getWarnings().getMessage());

//					Statement stmt = connHanaDb.createStatement();
//					stmt.getWarnings();
//					webRowSet.getCommand();
//					webRowSet.getStatement();
//					webRowSet.setType(1);
//					webRowSet.setCommand(queryString);
//					webRowSet.execute(connHanaDb);

				} catch (SQLException e) {

					System.err.println("Query failed! [ Ln 356 ]");
				}

			} else {

				System.out.println("connection is null [ Ln 130]");

			}

		} else {

			System.out.println("query is null [ Ln 136]");

		}

//		disconnectHana(connHanaDb);

		return webRowSet;

	}

	public static WebRowSet querySap_WebRowSet(String queryString, Connection connHanaDb) {

		// Craig Adam
		// 22-01-2020
		// returns disconnected WebRowSet based on result of queryString against a SAP
		// Hana connection
		// returns disconnected WebRowSet

		ResultSet resultSet = null;
		WebRowSet webRowSet = null;
		RowSetFactory oRowSetFactory;

		if (queryString != null && queryString.trim().length() > 0) {

			if (connHanaDb != null) {

				try {

					// Get ResultSet
					resultSet = connHanaDb.createStatement().executeQuery(queryString);

					// Create WebRowSet from ResultsSet object
					oRowSetFactory = RowSetProvider.newFactory();
					webRowSet = oRowSetFactory.createWebRowSet();
					webRowSet.populate(resultSet);
//					webRowSet.beforeFirst();

					System.out.println("getWarnings [JdbcUtils Ln 194] = " + webRowSet.getWarnings().getMessage());

//					Statement stmt = connHanaDb.createStatement();
//					stmt.getWarnings();
//					webRowSet.getCommand();
//					webRowSet.getStatement();
//					webRowSet.setType(1);
//					webRowSet.setCommand(queryString);
//					webRowSet.execute(connHanaDb);

				} catch (SQLException e) {

					System.err.println("Query failed! [ Ln 356 ]");
				}

			} else {

				System.out.println("connection is null [ Ln 130]");

			}

		} else {

			System.out.println("query is null [ Ln 136]");

		}

//		disconnectHana(connHanaDb);

		return webRowSet;

	}

	public static Document convertSapWebRowSet_Document(WebRowSet webRowSet) {

		// Craig Adam
		// 30/11/2019
		// Converts SAP WebRowSet to an in memory xml document that can be save out to
		// an xml file and then read back into a WebRowSet later or in a different test

		// TODO need to write properties node exactly as the built in node - else can
		// compare without properties by dropping node
		// TODO check if properties node build below that the hard coded null nodes are
		// in fact null and handle if not

		Document docRowSet = null;
		// HashMap<String, String> mElementValues = new HashMap<String, String>();
		// LinkedHashMap will maintain add order
		// This is required as metadata child nodes are expected in a specific order
		// https://stackoverflow.com/questions/683518/java-class-that-implements-map-and-keeps-insertion-order
		LinkedHashMap<String, String> mElementValues = new LinkedHashMap<String, String>();

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

			// Create and insert processing instruction before docRowSet element
			ProcessingInstruction oPI = docRowSet.createProcessingInstruction("xml", "version=\"1.0\"");
			docRowSet.insertBefore(oPI, docRowSet.getFirstChild());

			// Create properties element
			Element eProperties = docRowSet.createElement("properties");
			eWebRowSet.appendChild(eProperties);

			// Build properties node
			String valueString = null;

			Element eCommand = docRowSet.createElement("command");
			Element eNull1 = docRowSet.createElement("null");
			eCommand.appendChild(eNull1);
			eProperties.appendChild(eCommand);

			Element eConcurrency = docRowSet.createElement("concurrency");
			eConcurrency.appendChild(docRowSet.createTextNode("" + webRowSet.getConcurrency()));
			eProperties.appendChild(eConcurrency);

			Element eDatasource = docRowSet.createElement("datasource");
			Element eNull2 = docRowSet.createElement("null");
			eDatasource.appendChild(eNull2);
			eProperties.appendChild(eDatasource);

			Element eEscapeProcessing = docRowSet.createElement("escape-processing");
			eEscapeProcessing.appendChild(docRowSet.createTextNode("" + webRowSet.getEscapeProcessing()));
			eProperties.appendChild(eEscapeProcessing);

			Element eFetchDirection = docRowSet.createElement("fetch-direction");
			eFetchDirection.appendChild(docRowSet.createTextNode("" + webRowSet.getFetchDirection()));
			eProperties.appendChild(eFetchDirection);

			Element eFetchSize = docRowSet.createElement("fetch-size");
			eFetchSize.appendChild(docRowSet.createTextNode("" + webRowSet.getFetchSize()));
			eProperties.appendChild(eFetchSize);

			Element eIsolationLevel = docRowSet.createElement("isolation-level");
			eIsolationLevel.appendChild(docRowSet.createTextNode("" + webRowSet.getTransactionIsolation()));
			eProperties.appendChild(eIsolationLevel);

			Element eKeyColumns = docRowSet.createElement("key-columns");
			if (webRowSet.getKeyColumns() == null) {

				valueString = " ";

			}
			eKeyColumns.appendChild(docRowSet.createTextNode("" + valueString));
			eProperties.appendChild(eKeyColumns);

			Element eMap = docRowSet.createElement("map");
			if (webRowSet.getKeyColumns() == null) {

				valueString = " ";

			}

			eMap.appendChild(docRowSet.createTextNode("" + valueString));
			eProperties.appendChild(eMap);

			Element eMaxFieldSize = docRowSet.createElement("max-field-size");
			eMaxFieldSize.appendChild(docRowSet.createTextNode("" + webRowSet.getMaxFieldSize()));
			eProperties.appendChild(eMaxFieldSize);

			Element eMaxRows = docRowSet.createElement("max-rows");
			eMaxRows.appendChild(docRowSet.createTextNode("" + webRowSet.getMaxFieldSize()));
			eProperties.appendChild(eMaxRows);

			Element eQueryTimeOut = docRowSet.createElement("query-timeout");
			eQueryTimeOut.appendChild(docRowSet.createTextNode("" + webRowSet.getQueryTimeout()));
			eProperties.appendChild(eQueryTimeOut);

			Element eReadOnly = docRowSet.createElement("read-only");
			eReadOnly.appendChild(docRowSet.createTextNode("" + webRowSet.isReadOnly()));
			eProperties.appendChild(eReadOnly);

			// Map row set integer to row set name
			String rowSetTypeName = null;
			switch (webRowSet.getType()) {
			case 1004:

				rowSetTypeName = "ResultSet.TYPE_SCROLL_INSENSITIVE";

			}

			Element eRowsetType = docRowSet.createElement("rowset-type");
			eRowsetType.appendChild(docRowSet.createTextNode(rowSetTypeName));
			eProperties.appendChild(eRowsetType);

			Element eShowDeleted = docRowSet.createElement("show-deleted");
//			eShowDeleted.appendChild(docRowSet.createTextNode("" + webRowSet.getShowDeleted()));
			// hardcode to match the rowset
			eShowDeleted.appendChild(docRowSet.createTextNode("" + false));
			eProperties.appendChild(eShowDeleted);

			// TODO sort out this check - Craig Adam 30-01-2020
//			System.out.println("SapUtils ln 564 table name = " + (" " + webRowSet.getTableName()));
			Element eTableName = docRowSet.createElement("table-name");
//			if().contentEquals("null")) {

			Element eNull4 = docRowSet.createElement("null");
			eTableName.appendChild(eNull4);

//			} else {
//				
//				eTableName.appendChild(docRowSet.createTextNode("" + webRowSet.getTableName()));
//				
//			}

			eProperties.appendChild(eTableName);

			Element eUrl = docRowSet.createElement("url");
			Element eNull3 = docRowSet.createElement("null");
			eUrl.appendChild(eNull3);
			eProperties.appendChild(eUrl);

			Element eSyncProvider = docRowSet.createElement("sync-provider");
			eProperties.appendChild(eSyncProvider);

			Element eSyncProviderName = docRowSet.createElement("sync-provider-name");
			eSyncProviderName
					.appendChild(docRowSet.createTextNode("" + "com.sun.rowset.providers.RIOptimisticProvider"));
			eSyncProvider.appendChild(eSyncProviderName);

			Element eSyncProviderVendor = docRowSet.createElement("sync-provider-vendor");
			eSyncProviderVendor.appendChild(docRowSet.createTextNode("" + webRowSet.getSyncProvider().getVendor()));
			eSyncProvider.appendChild(eSyncProviderVendor);

			Element eSyncProviderVersion = docRowSet.createElement("sync-provider-version");
			eSyncProviderVersion.appendChild(docRowSet.createTextNode("" + webRowSet.getSyncProvider().getVersion()));
			eSyncProvider.appendChild(eSyncProviderVersion);

			Element eSyncProviderGrade = docRowSet.createElement("sync-provider-grade");
			eSyncProviderGrade
					.appendChild(docRowSet.createTextNode("" + webRowSet.getSyncProvider().getProviderGrade()));
			eSyncProvider.appendChild(eSyncProviderGrade);

			Element eDataSourceLock = docRowSet.createElement("data-source-lock");
			eDataSourceLock.appendChild(docRowSet.createTextNode("" + webRowSet.getSyncProvider().getDataSourceLock()));
			eSyncProvider.appendChild(eDataSourceLock);

			// Create metadata element
			Element eMetadata = docRowSet.createElement("metadata");
			eWebRowSet.appendChild(eMetadata);

			// Create column-count element
			Element eColumnCount = docRowSet.createElement("column-count");
			eMetadata.appendChild(eColumnCount);
			int iNoColumns = webRowSet.getMetaData().getColumnCount();
			eColumnCount.appendChild(docRowSet.createTextNode("" + iNoColumns));

			String ValueString = null;
			// Build column metadata for each column in the result set
			for (int iColNo = 1; iColNo <= iNoColumns; iColNo++) {

				mElementValues.clear();

				mElementValues.put("column-index", iColNo + "");
				mElementValues.put("auto-increment", webRowSet.getMetaData().isAutoIncrement(iColNo) + "");
				mElementValues.put("case-sensitive", webRowSet.getMetaData().isCaseSensitive(iColNo) + "");
				mElementValues.put("currency", webRowSet.getMetaData().isCurrency(iColNo) + "");
				mElementValues.put("nullable", webRowSet.getMetaData().isNullable(iColNo) + "");
				mElementValues.put("signed", webRowSet.getMetaData().isSigned(iColNo) + "");
				mElementValues.put("searchable", webRowSet.getMetaData().isSearchable(iColNo) + "");
				mElementValues.put("column-display-size", webRowSet.getMetaData().getColumnDisplaySize(iColNo) + "");
				mElementValues.put("column-label", webRowSet.getMetaData().getColumnLabel(iColNo) + "");
				mElementValues.put("column-name", webRowSet.getMetaData().getColumnName(iColNo) + "");

				ValueString = webRowSet.getMetaData().getSchemaName(iColNo);
				if (!(ValueString.length() > 0)) {

					ValueString = " ";

				}

				mElementValues.put("schema-name", ValueString);
				mElementValues.put("column-precision", webRowSet.getMetaData().getPrecision(iColNo) + "");
				mElementValues.put("column-scale", webRowSet.getMetaData().getScale(iColNo) + "");
				mElementValues.put("table-name", webRowSet.getMetaData().getTableName(iColNo) + "");

				ValueString = webRowSet.getMetaData().getCatalogName(iColNo);
				if (!(ValueString.length() > 0)) {

					ValueString = " ";

				}
				mElementValues.put("catalog-name", ValueString);

				// Need a generic database type for built in reader
				// TODO investigate solution - not reading date correctly - most likely need to
				// convert into a standard date format
				// as work around change date type 91 to a string - can fix when need to compare
				// dates in future
				// Get column type as integer
				int columnDataType = SapUtils.getDataTypeIntSap(iColNo, webRowSet);
				mElementValues.put("column-type", "" + columnDataType);

				mElementValues.put("column-type-name", webRowSet.getMetaData().getColumnTypeName(iColNo) + "");
//				<column-type-name>INTEGER</column-type-name>

				JdbcUtils.addNode_ColumnDef(docRowSet, eMetadata, mElementValues);

			}

			// Create data element
			Element eData = docRowSet.createElement("data");
			eWebRowSet.appendChild(eData);

			ResultSetMetaData rsmd = webRowSet.getMetaData();
			int colCount = rsmd.getColumnCount();

			// create currentRow for each result in result set
			webRowSet.beforeFirst();
			while (webRowSet.next()) {

				// Create currentRow in data
				Element currentRow = docRowSet.createElement("currentRow");
				eData.appendChild(currentRow);

				// Populate for each column
				for (int i = 1; i <= colCount; i++) {

//					String columnName = rsmd.getColumnName(i);
//					Object value = resultSet.getObject(i); // this was for reading from excel
					String value = SapUtils.getValueAsString(i, webRowSet);
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

	public static WebRowSet writeResultSet(String queryString, String componentName, String testPhase,
			String outputFileFolder) throws SQLException {
	//	  if (rWebRowKeyValueOutput.getString("ComponentStatus").equals("Pass")) {
          // try {
    	  
    			// Craig Adam
    			// 31-01-2020
    			// create db connection to sap based on settings file
    			// will write query and write both .txt and .xml representations of the
    			// ResultSet from the query
    			// returns xml string of WebRowSet which can be converted back to WebRowSet
    			// object and checked

    			Connection connHanaDb = null;
    			ResultSet resultSet = null;
    			WebRowSet webRowSet = null;
    			Document document = null;

//    			String folderName = null;

    			String uniqueSuffix = null;

    			// Unique filename for these test files
    			uniqueSuffix = FileFolderUtils.uniqueSuffixDTN();

    			// TODO update to retrieve via settings file
//    			String outpuFileFolder = null;
//    			outpuFileFolder = ".//src//main//java//Output";

//    			switch (testPhase.toLowerCase()) {
//    			case "before":
    	//
//    				folderName = "Before";
//    				break;
    	//
//    			case "after":
    	//
//    				folderName = "After";
//    				break;
    	//
//    			case "root":
    	//
//    				folderName = "";
//    				break;
    	//
//    			default:
    	//
//    				System.out.println("writeResultSet Ln 162 : testPhase not defined");
    	//
//    			}

    			// Open live connection to the DB
    			connHanaDb = SapUtils.connectSap();

    			childTest.log(Status.INFO, MarkupHelper.createLabel("Connection to DB Sucessful", ExtentColor.INDIGO));
    			extent.flush();

    			// Retrieve ResultSet and write to a file
    			resultSet = SapUtils.querySap_ResultSet(queryString, connHanaDb);
    			JdbcUtils.writeToFile(resultSet, outputFileFolder + "//" + testPhase + "//" + componentName + "_" + "ResultSet" + uniqueSuffix + ".txt");

 
    			// Retrieve WebRowSet, write to a file and print
    			webRowSet = SapUtils.querySap_WebRowSet(queryString);

    			// Convert WebRowSet to Document
    			document = SapUtils.convertSapWebRowSet_Document(webRowSet);
    	
    			// Convert document into xml String and save to file
    			String xmlString = JdbcUtils.convertRowSetDocToXml(document);
    			FileFolderUtils.saveStringToFile(xmlString, outputFileFolder + "//" + testPhase + "//" + componentName + "_" + "WebRowSet" + uniqueSuffix + ".xml");
    			
    			try {
    				
    				File tempFile = new File(outputFileFolder + "//" + testPhase + "//" + componentName + "_" + "ResultSet" + uniqueSuffix + ".txt");	
    			
    				if(!tempFile.exists()) {
        				childTest.log(Status.FAIL, MarkupHelper.createLabel("Unable to generate the file "+outputFileFolder+ "//" + testPhase + "//" + componentName + "_" + "ResultSet" + uniqueSuffix + ".txt", ExtentColor.RED));
        				reporting_Failure(); 
        			} else {
        				childTest.log(Status.PASS, MarkupHelper.createLabel("File Generated sucessfuly "+outputFileFolder+ "//" + testPhase + "//" + componentName + "_" + "ResultSet" + uniqueSuffix + ".txt", ExtentColor.GREEN));
        				
        			}
    			}catch (Exception e) {
    				
    				System.out.println(e);
    				System.out.println("tests");
    				
    			}
    			

    			
    			
    						
				/*
				 * childTest.log(Status.INFO,MarkupHelper.createLabel("testPhase = " + testPhase
				 * + " The text file generated at output folder " + outpuFileFolder+ "//" +
				 * testPhase + "//" + componentName + "_" + "ResultSet" + uniqueSuffix + ".txt",
				 * ExtentColor.INDIGO));
				 * 
				 * childTest.log(Status.INFO, MarkupHelper.createLabel( "testPhase = " +
				 * testPhase + " The xml file generated at output folder " + outpuFileFolder +
				 * "//" + testPhase + "//" + componentName + "_" + "ResultSet" + uniqueSuffix +
				 * ".xml", ExtentColor.INDIGO));
				 */
    			
    			extent.flush();

    			// Close live connection to Hana data base
    			SapUtils.disconnectSap(connHanaDb);
		  
    			return webRowSet;
		
	}
        
		
	

	
	public static void reporting_Failure()  {
		try {
			rWebRowKeyValueOutput = JdbcUtils.setKeyValuePair("ComponentStatus", "Fail", rWebRowKeyValueOutput); // Setting up ComponentStatus column with dummy ComponentName as in 1stRow
			rWebRowKeyValueOutput.next();
			ReportingUtils.QcExecution(rWebRowKeyValueOutput.getString("TestCaseName"), rWebRowKeyValueOutput.getString("TestID"),"Failed");	
		}catch (Exception e) {
			
		}
		
	}
	
	
	
	
	public static boolean isEqualSapWebRowSet_ByString(WebRowSet sapWebRowSet1, WebRowSet sapWebRowSet2) {

		// Craig Adam
		// 30-01-2020
		// converts WebRowSets to xml string values and then checks if they are exactly
		// equal
		// ie properties, metadata and data rows match exactly

		Document sapDocument2 = SapUtils.convertSapWebRowSet_Document(sapWebRowSet2); // Convert to document
		String sapXmlString2 = JdbcUtils.convertRowSetDocToXml(sapDocument2); // Convert document into xml
																				// string

		Document sapDocument1 = SapUtils.convertSapWebRowSet_Document(sapWebRowSet1); // Convert to document
		String sapXmlString1 = JdbcUtils.convertRowSetDocToXml(sapDocument1); // Convert document into xml
																				// string

		// Validate that result xml strings are exactly the same (ie text == text)
		if (sapXmlString2.contentEquals(sapXmlString1)) {

			// System.out.println("post implementation result set matches prior
			// implementation result set BY toString");

			return true;

		} else {

			// System.out.println("No toString exact match");
			return false;

		}

	}

	public static void validationExactXmlMatch(String outputFileFolder, String componentName) {

		// Craig Adam
		// 03-02-2020
		// Validate that result xml strings are exactly the same (ie text == text)

		String folderName = null;

		WebRowSet beforeWebRowSet = null;
		WebRowSet currentWebRowSet = null;

		// Read WebRowSet from the baseline WebRowSet .xml file
		// from prior (baseline) file
		folderName = "Before";
		beforeWebRowSet = JdbcUtils.readLastModifiedComponentXml_WebRowSet(outputFileFolder, folderName, componentName);

		// Read WebRowSet from the after implementation WebRowSet .xml file

		folderName = "After";
		currentWebRowSet = JdbcUtils.readLastModifiedComponentXml_WebRowSet(outputFileFolder, folderName, componentName);

		SapUtils.validationRowCount(beforeWebRowSet, currentWebRowSet);

		// Validate that result xml strings are exactly the same (ie text == text)
		if (SapUtils.isEqualSapWebRowSet_ByString(beforeWebRowSet, currentWebRowSet)) {

			System.out.println("After implementation result set matches Before implementation result set BY toString");

		} else {

			System.out.println("No toString exact match");

		}

	}

	public static void validationExactTxtMatch(String outputFileFolder, String componentName) throws SQLException {
	  if(rWebRowKeyValueOutput.getString("ComponentStatus").equals("Pass")) {
		//System.out.println(rWebRowKeyValueOutput.getString("ComponentStatus"));
		// TODO add output for the number of rows and fail if zero - need to parse text
		// doc
		// parse txt into a list split by line separator extract last row, verify that
		// is contains "rows." , extract int.
		// Craig Adam
		// 03-02-2020
		// Validate that result txt SAPFile strings are exactly the same (ie text ==
		// text)

		String folderNameAfter = null;
		String folderNameBefore = null;
		String folderLocationAfter = null;
		String folderLocationBefore = null;	
		String afterFileString = null;
		String beforeFileString = null;
		String beforeComponentName = null;
		
		File fileLastModifiedBefore = null;
		File fileLastModifiedAfter = null;
		
		try {

			// After
			folderNameAfter = "After";
			folderLocationAfter = FileFolderUtils.convertRelToAbsPath((outputFileFolder + "//" + folderNameAfter));
			fileLastModifiedAfter = FileFolderUtils.findLastModified(folderLocationAfter, componentName, ".txt");
			afterFileString = new String(Files.readAllBytes(Paths.get(fileLastModifiedAfter.getAbsolutePath())));

			// Before
			folderNameBefore = "Before";
			folderLocationBefore = FileFolderUtils.convertRelToAbsPath((outputFileFolder + "//" + folderNameBefore));
			beforeComponentName = componentName.replace(folderNameAfter, "") + folderNameBefore;
			fileLastModifiedBefore = FileFolderUtils.findLastModified(folderLocationBefore, beforeComponentName, ".txt");
			beforeFileString = new String(Files.readAllBytes(Paths.get(fileLastModifiedBefore.getAbsolutePath())));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Validate that result xml strings are exactly the same (ie text == text)
		// if (afterFile.contentEquals(beforeFile) && afterFile != null && beforeFile !=
		// null) {
		if (afterFileString.contentEquals(beforeFileString)) {
	
				childTest.log(Status.PASS, MarkupHelper.createLabel("After implementation result set matches Before implementation result set BY toString", ExtentColor.GREEN));
				
			

			System.out.println("------");
			System.out.println("------");
			System.out.println("------");
			System.out.println("------");
			System.out.println("------");
			System.out.println("After implementation result set matches Before implementation result set BY toString");
			System.out.println("------");
			System.out.println("------");
			System.out.println("------");
			System.out.println("------");
			System.out.println("------");
		}
			else {
			childTest.log(Status.FAIL, MarkupHelper.createLabel("*****NO MATCH**** After implementation result set matches didnot match Before implementation result set BY toString", ExtentColor.RED));
			reporting_Failure(); 
			System.out.println("No toString exact match");
			System.out.println("No toString exact match");
			System.out.println("No toString exact match");
			System.out.println("No toString exact match");
			System.out.println("No toString exact match");
			System.out.println("No toString exact match");
			System.out.println("No toString exact match");
			System.out.println("No toString exact match");
			rWebRowKeyValueOutput = JdbcUtils.setKeyValuePair("ComponentStatus", "Fail", rWebRowKeyValueOutput); // Setting up ComponentStatus column with dummy ComponentName as in 1stRow
			rWebRowKeyValueOutput.next();  
			}
		}
	}	
		
		// MOVED into separate component that can use for before tests as well - Craig - 12-02-2020
//		try {
//			
//			String locationReportingFolderRoot = SettingsUtils.getTagNameValue("artifactFolder") + "//";
//			
//			// After
//			String locationDestinationFolder = locationReportingFolderRoot + Reportingdirectory + "//" + "Output"
//					+ fileLastModifiedAfter.getName();
//			
//			System.out.println("[SapUtils ln 1214] folderLocationAfter = " + folderLocationAfter);
//			System.out.println("[SapUtils ln 1215] locationDestinationFolder = " + locationDestinationFolder);
//			
//			DirectoryUtils.copyFolderContent(folderLocationAfter + "//" + fileLastModifiedAfter.getName(), FilePathUtils.convertRelToAbsPath(locationDestinationFolder));
//			
//			// Before
//			DirectoryUtils.copyFolderContent(folderLocationBefore + "//" + fileLastModifiedBefore.getName(), locationReportingFolderRoot + Reportingdirectory
//					+ fileLastModifiedBefore.getName());
//			
//			//	  DirectoryUtils.copyFolderContent(System.getProperty("user.dir") + "\\artefactDB\\Before\\ClouderaIntegrationBefore_ResultSet_12022020_101540.txt", System.getProperty("user.dir") + "\\artefacts\\"+Reportingdirectory+"\\Output\\ClouderaIntegrationBefore_ResultSet_12022020_101540.txt"); 
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	

	/**
	 * 
	 * Checks that the two RowSets have the same row count and are not zer0
	 * 
	 * @param WebRowSet1
	 * 
	 * @author Craig Adam - modified 10-02-2020
	 * 
	 */
	public static boolean validationRowCountNotZero(WebRowSet webRowSet) {

		int rowCount = JdbcUtils.getLastRowNumber(webRowSet);

		if (rowCount != 0) {

			System.out.println("rowCount has row length of [SapUtils Ln 1105] " + rowCount);
			return true;

		} else {

			System.out.println("WebRowSet has zero rows");
			return false;

		}

	}

	/**
	 * 
	 * Checks that the two RowSets have the same row count and are not zer0
	 * 
	 * @param WebRowSet1
	 * @param WebRowSet2
	 * @author Craig Adam - modified 10-02-2020
	 * 
	 */
	public static void validationRowCount(WebRowSet WebRowSet1, WebRowSet WebRowSet2) {

//		String folderName = null;

		int rowCountBefore = JdbcUtils.getLastRowNumber(WebRowSet1);
		int rowCountAfter = JdbcUtils.getLastRowNumber(WebRowSet2);

		if (rowCountBefore == 0) {

			System.out.println("beforeRowSet has row length of zero!");

		}

		if (rowCountAfter == 0) {

			System.out.println("afterRowSet has row length of zero!");

		}

		if (rowCountAfter != rowCountBefore) {

			System.out.println("afterRowSet has row length of : " + rowCountAfter + " does NOT match "
					+ "beforeRowSet row length of : " + rowCountBefore);

		} else {

			System.out.println("RowSets have the same row count = " + rowCountBefore);

		}

	}

	/**
	 * 
	 * @param outpuFileFolder :
	 * @param componentName
	 * @param ColumnName
	 * @param RowNumber       @
	 * 
	 */
	// Remove need for passing the row number?? - Craig Adam 06-02-2020
	public static void validationFieldMatch(String outpuFileFolder, String componentName, String ColumnName,
			int RowNumber) {

		// Craig Adam
		// 03-02-2020
		// Validate field value of row and column name - this can be used when for
		// example
		// a single result field is returned eg (Count(*))

		String folderName = null;

		WebRowSet beforeWebRowSet = null;
		WebRowSet currentWebRowSet = null;

		// Read WebRowSet from the baseline WebRowSet .xml file
		// from prior (baseline) file
		folderName = "Before";
		beforeWebRowSet = JdbcUtils.readLastModifiedComponentXml_WebRowSet(outpuFileFolder, folderName, componentName);

		// Read WebRowSet from the after implementation WebRowSet .xml file

		folderName = "After";
		currentWebRowSet = JdbcUtils.readLastModifiedComponentXml_WebRowSet(outpuFileFolder, folderName, componentName);

//		PrintUtils.printSapObject(beforeWebRowSet); // ok

		// Move to row number
		try {
			beforeWebRowSet.absolute(RowNumber);
			currentWebRowSet.absolute(RowNumber);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		PrintUtils.printSapObject(beforeWebRowSet); // ok

		// Validate result
		String fieldValueBefore = SapUtils.getValueAsString(JdbcUtils.getColumnNo(ColumnName, beforeWebRowSet),
				beforeWebRowSet);
		String fieldValueAfter = SapUtils.getValueAsString(JdbcUtils.getColumnNo(ColumnName, currentWebRowSet),
				currentWebRowSet);

		if (fieldValueBefore.contentEquals(fieldValueAfter)) {

			System.out.println("before value = " + fieldValueBefore + " Equals " + "after value : " + fieldValueAfter);

		} else {

			System.out.println(
					"before value = " + fieldValueBefore + " DOES NOT equal " + "after value : " + fieldValueAfter);

		}

	}

	/**
	 * 
	 * Compares each field value in the given column name by default sort (ie no
	 * sort applied, just row 1 Before to row 1 After)
	 * 
	 * Reads a previously saved .xml file from the "Before" and "After" sub
	 * directories of the outputFileFolder Iterates through the column of name
	 * "ColumnName" Used to compare baseline vs current ResultSets
	 * 
	 * @param outputFileFolder : Absolute location of output folder. This folder
	 *                        contains the "Before" and "After" folders.
	 * @param componentName   : Name of the current component - this should match
	 *                        the filename (excluding the unique suffix) of the
	 *                        exported WebRowSet .xml
	 * @param ColumnName      : Name of the column to compare between ResultSet
	 *                        "Before" and ResultSet "After"
	 * @author Craig Adam - Modified 03-02-2020
	 * @return Will report pass to extent report if are equal else will fail
	 * 
	 *         For example : The column name "Count(*)" contains multiple rows. Each
	 *         row should be compared against the baseline row field value.
	 * @throws SQLException 
	 * 
	 */
	public static void validationColumnValues(String outputFileFolder, String componentName, String ColumnName) throws SQLException {
		if(rWebRowKeyValueOutput.getString("ComponentStatus").equals("Pass")) {
		// Craig Adam
		// 03-02-2020
		// Validate each field value of a column name - this can be used when for
		// example
		// the column name (Count(*)) contains multiple rows and all should be checked

		



		// Location of saved files ...
//		FileFolderUtils.saveStringToFile(xmlString, outputFileFolder + "//" + testPhase + "//" + componentName + "_" + "WebRowSet" + uniqueSuffix + ".xml");
		
		
		// Read WebRowSet from the after implementation WebRowSet .xml file

		String testPhaseA = "After";
		WebRowSet afterWebRowSet = JdbcUtils.readLastModifiedComponentXml_WebRowSet(outputFileFolder + "//", testPhaseA, componentName);
	
		// Read WebRowSet from the baseline WebRowSet .xml file
		// from prior (baseline) file
		
		String componentNameBefore = componentName;
		
		// if the suffix is "After" remove and replace with "Before"
		String testPhaseB = "Before";
		String[] listDelimited = StringUtils.stringSplit(componentName, "_");
		if(listDelimited[listDelimited.length - 1].contentEquals(testPhaseA)) {
			
			componentNameBefore = componentName.replace(testPhaseA, testPhaseB);
			
		}

		WebRowSet beforeWebRowSet = JdbcUtils.readLastModifiedComponentXml_WebRowSet(outputFileFolder + "//", testPhaseB, componentNameBefore);
		
		
		int totalRows = JdbcUtils.getLastRowNumber(beforeWebRowSet);
		// for each row in the WebRowSet
		for (int iRowNo = 1; iRowNo < totalRows; iRowNo++) {

			// Move to row number
			try {
				beforeWebRowSet.absolute(iRowNo);
				afterWebRowSet.absolute(iRowNo);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Validate result
			String fieldValueBefore = SapUtils.getValueAsString(JdbcUtils.getColumnNo(ColumnName, beforeWebRowSet),
					beforeWebRowSet);
			String fieldValueAfter = SapUtils.getValueAsString(JdbcUtils.getColumnNo(ColumnName, afterWebRowSet),
					afterWebRowSet);
			if (fieldValueBefore.contentEquals(fieldValueAfter)) {

				System.out.println("Row : " + iRowNo + " before value = " + fieldValueBefore + " Equals "
						+ "after value : " + fieldValueAfter);

			} else {

				System.out.println("Row : " + iRowNo + " NO MATCH " + "before value = " + fieldValueBefore
						+ " after value : " + fieldValueAfter);

			}

		}
		}
	}
	
	public static void validationColumnValues(String outputFileFolder, String componentName, int ColumnNumber) throws SQLException {
        String TCStatus = "Passed";
		// Craig Adam
		// 11-02-2020
		// Validate each field value of a column name - this can be used when for
		// example
		// the column name (Count(*)) contains multiple rows and all should be checked

	

		

		// Location of saved files ...
//		FileFolderUtils.saveStringToFile(xmlString, outputFileFolder + "//" + testPhase + "//" + componentName + "_" + "WebRowSet" + uniqueSuffix + ".xml");
		
		
		// Read WebRowSet from the after implementation WebRowSet .xml file

		String testPhaseA = "After";
		WebRowSet afterWebRowSet = JdbcUtils.readLastModifiedComponentXml_WebRowSet(outputFileFolder + "//", testPhaseA, componentName);
	
		// Read WebRowSet from the baseline WebRowSet .xml file
		// from prior (baseline) file
		
		String componentNameBefore = componentName;
		
		// if the suffix is "After" remove and replace with "Before"
		String testPhaseB = "Before";
		String[] listDelimited = StringUtils.stringSplit(componentName, "_");
		if(listDelimited[listDelimited.length - 1].contentEquals(testPhaseA)) {
			
			componentNameBefore = componentName.replace(testPhaseA, testPhaseB);
			
		}

		WebRowSet beforeWebRowSet = JdbcUtils.readLastModifiedComponentXml_WebRowSet(outputFileFolder + "//", testPhaseB, componentNameBefore);

		int totalRows = JdbcUtils.getLastRowNumber(beforeWebRowSet);
		// for each row in the WebRowSet
		for (int iRowNo = 1; iRowNo <= totalRows; iRowNo++) {

			// Move to row number
			try {
				beforeWebRowSet.absolute(iRowNo);
				afterWebRowSet.absolute(iRowNo);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Validate result
			String fieldValueBefore = SapUtils.getValueAsString(ColumnNumber, beforeWebRowSet);
			String fieldValueAfter = SapUtils.getValueAsString(ColumnNumber, afterWebRowSet);
			if (fieldValueBefore.contentEquals(fieldValueAfter)) {

				System.out.println("Row : " + iRowNo + " before value = " + fieldValueBefore + " Equals "
						+ "after value : " + fieldValueAfter);

			} else {

				System.out.println("Row : " + iRowNo + " NO MATCH " + "before value = " + fieldValueBefore
						+ " after value : " + fieldValueAfter);
				childTest.log(Status.FAIL, MarkupHelper.createLabel("Row : " + iRowNo + " NO MATCH " + "before value = " + fieldValueBefore + " after value : " + fieldValueAfter, ExtentColor.RED));
				System.out.println("Row : " + iRowNo + " NO MATCH " + "before value = " + fieldValueBefore + " after value : " + fieldValueAfter);
				TCStatus="Fail"; 
				rWebRowKeyValueOutput = JdbcUtils.setKeyValuePair("ComponentStatus", "Fail", rWebRowKeyValueOutput); // Setting up ComponentStatus column with dummy ComponentName as in 1stRow
				rWebRowKeyValueOutput.next();
				extent.flush();
				break;
			}
			if (TCStatus.equals("Passed") && (iRowNo==totalRows )) {
			 childTest.log(Status.PASS, MarkupHelper.createLabel("Total Number of Rows compared are "+totalRows+" which are Equal" , ExtentColor.GREEN));
			 extent.flush();
			}
		}

	}

	/**
	 * 
	 * First matches the row by the match column, then compares the field value in
	 * the compare column <br>
	 * ie For each row in "Before" RowSet the field value in the compare column will
	 * be checked against against 1st row in "After" RowSet that matches the field
	 * value in it's match column) - see example below for more explanation.<br>
	 * <br>
	 * Also checks the total row count to check have not introduced additional rows
	 * during implementation (ie between Before and After implementation
	 * RowSets)<br>
	 * NOTE: if the match column field value is not unique (Primary key) this will
	 * fail as will find 1st match in After when checking the 2nd, 3rd etc
	 * occurrence in Before.<br>
	 * <br>
	 * Reads the last modified previously saved .xml file from the "Before" and
	 * "After" sub-directories of the outputFileFolder Converts each of these files
	 * to a WebRowSet object <br>
	 * <br>
	 * Required to handle shuffle of data between sql calls to sap DB ie When sort
	 * by a column that has multiple field values that are equal
	 * 
	 * @param outpuFileFolder   : Absolute location of output folder. This folder
	 *                          contains the "Before" and "After" folders.
	 * @param componentName     : Name of the current component - this should match
	 *                          the filename (excluding the unique suffix) of the
	 *                          exported WebRowSet .xml
	 * @param matchColumnName   : Name of the column to use as the primary key
	 *                          between the RowSets - will compare the 1st row in
	 *                          "After" that matches the field value of "Before" in
	 *                          this column
	 * @param compareColumnName : Name of the column to compare between ResultSet
	 *                          "Before" and ResultSet "After". Once the row is
	 *                          found from matchColumnName this field will be the
	 *                          checked that are equal.
	 * @author Craig Adam - Modified 04-02-2020
	 * @param matchColumnName
	 * @param compareColumnName
	 * @return Nothing returned - will report pass to extent report if are equal
	 *         else will fail
	 *         <p>
	 * 
	 *         Example : <br>
	 *         <br>
	 *         validationColumnValues(String outpuFileFolder, String componentName,
	 *         String matchColumnName, String compareColumnName)<br>
	 *         where the .xml files in the Before and After sub directories are as
	 *         below:<br>
	 *         these RowSets are sorted by Age during the query - but are not exact
	 *         row by row (See row 2 & 5)<br>
	 *         <br>
	 *         matchColumnName = "Name"<br>
	 *         compareColumnName = "Age"<br>
	 *         during the iteration for Before row 2 will 1st find the 1st match of
	 *         Alice (ie Alice is stored in After row 4) and then check the "Age"
	 *         value in that row<br>
	 *         <br>
	 *         BeforeRowSet <br>
	 *         <br>
	 *         Row || Name || Age || Gender<br>
	 *         --------||--------------||------||--------<br>
	 *         1 || Bob || 55 || Male<br>
	 *         2 || Alice || 40 || Female<br>
	 *         3 || Joe || 40 || Male<br>
	 *         4 || Betty || 40 || Female<br>
	 *         5 || Claire || 35 || Female<br>
	 *         6 || Shaun || 35 || Male<br>
	 *         <br>
	 *         AfterRowSet <br>
	 *         <br>
	 *         Row || Name || Age || Gender<br>
	 *         --------||--------------||------||--------<br>
	 *         1 || Bob || 55 || Male<br>
	 *         2 || Joe || 40 || Male<br>
	 *         3 || Betty || 40 || Female<br>
	 *         4 || Alice || 40 || Female<br>
	 *         5 || Shaun || 35 || Male<br>
	 *         6 || Claire || 35 || Female<br>
	 * 
	 * 
	 * 
	 * 
	 *         <br>
	 *         Used to compare Before vs After ResultSets<br>
	 *         <br>
	 *         <br>
	 *         For example : The column name "Count(*)" contains multiple rows. Each
	 *         row should be compared against the same baseline field value ie that
	 *         has the same schema name.
	 * 
	 *         The rows can be in different order but should still pass as long as
	 *         all rows in the "Before" have a corresponding row in "After" where
	 *         the field value that want to compare is equal.
	 * 
	 *         Also checks that the row count is the same between "Before" and
	 *         "After" else could have additional rows in "After" that do not want!!
	 * 
	 *         Type “nbsp” to add a single space Type “ensp” to add 2 spaces Type
	 *         “emsp” to add 4 spaces Use the non-breaking space (nbsp) 4 times to
	 *         insert a tab Use “br” to add a line break
	 */
	public static void validationColumnValues_SortByKeyMathchColumn(String outpuFileFolder, String componentName,
			String matchColumnName, String compareColumnName) {

		String folderName = null;

		WebRowSet beforeWebRowSet = null;
		WebRowSet afterWebRowSet = null;

		// Read WebRowSet from the baseline WebRowSet .xml file
		// from prior (baseline) file
		folderName = "Before";
		beforeWebRowSet = JdbcUtils.readLastModifiedComponentXml_WebRowSet(outpuFileFolder, folderName, componentName);

		SapUtils.validationRowCountNotZero(beforeWebRowSet);

		// Read WebRowSet from the after implementation WebRowSet .xml file

		folderName = "After";
		afterWebRowSet = JdbcUtils.readLastModifiedComponentXml_WebRowSet(outpuFileFolder, folderName, componentName);

		SapUtils.validationRowCountNotZero(afterWebRowSet);
		SapUtils.validationRowCount(beforeWebRowSet, afterWebRowSet);

//		String matchColumnName = "SCHEMA_NAME";
//		String compareColumnName = "COUNT(*)";

		// For each row in the baseline RowSet
		// Get the baseline field value in a given column name
		// Filter the current ResultSet based on the baseline field value in a given
		// column name
		// Get the field value from columnName2 in baseline and current and compare. If
		// equal = pass else fail

		try {

//			ResultSetMetaData rsmd = JdbcUtils.getMetaData(beforeWebRowSet);

			int matchColumnNo = JdbcUtils.getColumnNo(matchColumnName, beforeWebRowSet);
			int compareColumnNo = JdbcUtils.getColumnNo(compareColumnName, beforeWebRowSet);

			// JdbcUtils.getLastRowNumber(beforeWebRowSet);
			String matchFieldValueBefore = null;
			String compareFieldValueBefore = null; // value to match in the after WebRowSet column = column name

			String matchFieldValueAfter = null;
			String compareFieldValueAfter = null;
			FilteredRowSet filteredRowSet = null;

			// iterate all rows in the before ResultSet
//			afterWebRowSet.beforeFirst();	// NO!! java.sql.SQLException: absolute : Invalid cursor position -  Craig Adam - use absolute to set to required row number inside the loop
			int RowNo = 0;
			while (beforeWebRowSet.next()) {

				// iterate cursor position
				RowNo = RowNo + 1;

				System.out.println("SapUtils Ln 1480" + RowNo);

				if (RowNo > 70) {

					System.out.println(RowNo);

				}

				beforeWebRowSet.absolute(RowNo);

				// Get values from the Before WebRowSet that need to match and compare
				matchFieldValueBefore = SapUtils.getValueAsString(matchColumnNo, beforeWebRowSet);
				compareFieldValueBefore = SapUtils.getValueAsString(compareColumnNo, beforeWebRowSet);

				// Filter the after WebRowSet based on match column (should only retrieve one -
				// else NOT a primary key!)
				filteredRowSet = null;
				Filter_ByRegex filterRegex = new Filter_ByRegex(matchFieldValueBefore, matchColumnNo); // OK
				filteredRowSet = filterWebRowSet_ByRegex(afterWebRowSet, filterRegex); // OK

				// Get the field value of the compareColumn in the After ResultSet
				filteredRowSet.first(); // use 1st matched result
				// TODO do we need to add a check that is unique?? - Craig Adam
				matchFieldValueAfter = SapUtils.getValueAsString(matchColumnNo, filteredRowSet);
				compareFieldValueAfter = SapUtils.getValueAsString(compareColumnNo, filteredRowSet);

				if (compareFieldValueBefore.contentEquals(compareFieldValueAfter)) {

					System.out.println("Row : " + RowNo + " before match field = " + matchFieldValueBefore
							+ " before compare value = " + compareFieldValueBefore + " Equals " + "after match field = "
							+ matchFieldValueAfter + " after compare value : " + compareFieldValueAfter
							+ " after row no = " + filteredRowSet.getRow());

				} else {

					System.out.println("Row : " + RowNo + "NO MATCH" + " before match field = " + matchFieldValueBefore
							+ " before compare value = " + compareFieldValueBefore + " Equals " + "after match field = "
							+ matchFieldValueAfter + " after compare value : " + compareFieldValueAfter
							+ " after row no = " + filteredRowSet.getRow());

				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static FilteredRowSet filterWebRowSet_ByRegex(WebRowSet webRowSet, Filter_ByRegex filter) {

		// Craig Adam
		// 03-02-2020

		FilteredRowSet filteredRowSet = null;

		try {

			int originalRowNumber = webRowSet.getRow();

//			System.out.println("filterWebRowSet_ByRegex Ln 1369");
//			System.out.println("webRowSet.getRow() Ln 1386 = " + webRowSet.getRow());

			webRowSet.beforeFirst(); // must set cursor position to before 1st as this is where it starts filter
			RowSetFactory oRowSetFactory = RowSetProvider.newFactory();
			filteredRowSet = oRowSetFactory.createFilteredRowSet();
			filteredRowSet.populate(webRowSet);
			filteredRowSet.setFilter(filter);
			filteredRowSet.beforeFirst();
			webRowSet.absolute(originalRowNumber);

//			System.out.println("webRowSet.getRow() Ln 1399 = " + webRowSet.getRow());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return filteredRowSet;

	}

	public static FilteredRowSet filterWebRowSet_ByIntRange(WebRowSet webRowSet, Filter_ByIntRange filter) {

		// Craig Adam
		// 03-02-2020

		FilteredRowSet filteredRowSet = null;

		try {

			int originalRowNumber = webRowSet.getRow();

			webRowSet.beforeFirst(); // must set cursor position to before 1st as this is where it starts filter
			RowSetFactory oRowSetFactory = RowSetProvider.newFactory();
			filteredRowSet = oRowSetFactory.createFilteredRowSet();
			filteredRowSet.populate(webRowSet);
			filteredRowSet.setFilter(filter);
			filteredRowSet.beforeFirst();
			webRowSet.absolute(originalRowNumber);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return filteredRowSet;

	}

	////////////////////
	////////////////////
	// PRIVATE METHODS
	////////////////////
	////////////////////

	private static int getDataTypeIntSap(int ColumnNo, WebRowSet webRowSet) {

		// Craig Adam
		// 28-02-2020
		// Map Sap Hana data types
		// https://www.cis.upenn.edu/~bcpierce/courses/629/jdkdocs/guide/jdbc/getstart/mapping.doc.html

		//

		ResultSetMetaData rsmd = JdbcUtils.getMetaData(webRowSet);

		int returnInt = -1;

		try {

			// System.out.println("Type [JDBSSupport Ln 802] = " +
			// rsmd.getColumnTypeName(ColumnNo).toString().trim());

			switch (rsmd.getColumnTypeName(ColumnNo).toString().trim()) {

			case "TINYINT":
			case "SMALLINT":

				returnInt = webRowSet.getMetaData().getColumnType(ColumnNo);
				returnInt = 12; // overwrite with string type - handle if ever need to compare can convert back

				break;

			case "INTEGER":

				returnInt = webRowSet.getMetaData().getColumnType(ColumnNo); // this is ok for generic WebRowSet read
																				// from file
				returnInt = 12; // overwrite with string type - handle if ever need to compare can convert back

				break;

			case "BIGINT":

				returnInt = webRowSet.getMetaData().getColumnType(ColumnNo);
				returnInt = 12; // overwrite with string type - handle if ever need to compare can convert back

				break;

			case "FLOAT":
			case "REAL":

				returnInt = webRowSet.getMetaData().getColumnType(ColumnNo);
				returnInt = 12; // overwrite with string type - handle if ever need to compare can convert back

				break;

			case "DECIMAL":
			case "NUMERIC":

				returnInt = webRowSet.getMetaData().getColumnType(ColumnNo);
				returnInt = 12; // overwrite with string type - handle if ever need to compare can convert back

				break;

			case "CHAR":
			case "VARCHAR":
			case "LONGVARCHAR":
			case "NVARCHAR":

				returnInt = webRowSet.getMetaData().getColumnType(ColumnNo);
				returnInt = 12; // This should be the same as string so knows how to read back with built in
								// method

				break;

			case "BINARY":
			case "VARBINARY":

				returnInt = webRowSet.getMetaData().getColumnType(ColumnNo);
				returnInt = 12; // overwrite with string type - handle if ever need to compare can convert back

				break;

			case "LONGVARBINARY":

				returnInt = webRowSet.getMetaData().getColumnType(ColumnNo);
				returnInt = 12; // overwrite with string type - handle if ever need to compare can convert back

				break;

			case "BIT":

				returnInt = webRowSet.getMetaData().getColumnType(ColumnNo);
				returnInt = 12; // overwrite with string type - handle if ever need to compare can convert back

				break;

			case "DATE":

				returnInt = 12; // overwrite with string type - handle if ever need to compare can convert back
								// to date - Craig 28-01-2020

				break;

			case "TIME":

				// returnInt = webRowSet.getMetaData().getColumnType(ColumnNo);
				returnInt = 12; // overwrite with string type - handle if ever need to compare can convert back
				// to date - Craig 03-02-2020

				break;

			case "TIMESTAMP":

				// returnInt = webRowSet.getMetaData().getColumnType(ColumnNo);
				returnInt = 12; // overwrite with string type - handle if ever need to compare can convert back
				// to date - Craig 03-02-2020

				break;

			case "DOUBLE":

				// returnInt = webRowSet.getMetaData().getColumnType(ColumnNo);
				returnInt = 12; // overwrite with string type - handle if ever need to compare can convert back

				break;

			default:

				System.out.println("Unmapped data type [JdbcUtils Ln 1692] ");
				returnInt = 12; // overwrite with string type - handle if ever need to compare can convert back

				break;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnInt;

//		 generic SQL data types as follows
//		https://alvinalexander.com/java/edu/pj/jdbc/recipes/ResultSet-ColumnType.shtml

//				-7	BIT
//				-6	TINYINT
//				-5	BIGINT
//				-4	LONGVARBINARY 
//				-3	VARBINARY
//				-2	BINARY
//				-1	LONGVARCHAR
//				0	NULL
//				1	CHAR
//				2	NUMERIC
//				3	DECIMAL
//				4	INTEGER
//				5	SMALLINT
//				6	FLOAT
//				7	REAL
//				8	DOUBLE
//				12	VARCHAR
//				91	DATE
//				92	TIME
//				93	TIMESTAMP
//				1111 	OTHER

	}

}
