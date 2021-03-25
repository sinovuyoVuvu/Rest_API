package utils;

import static alm.qc.restapi.XmlDomParser.convertFileToXmlDocument;
import static alm.qc.restapi.XmlDomParser.getNodeByAttributes;
import static alm.qc.restapi.XmlDomParser.getNodesByTagName;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class SettingsUtils {

	// -----------------------------------------------------------------------------
	// DEBUG METHODS
	/*
	 * public static void main(String[] args) {
	 * 
	 * initSettings();
	 * 
	 * // System.out.println("Mud ID = " + getMudId()); //
	 * System.out.println("Password = " + getPassword()); //
	 * System.out.println("DomainName = " + getDomainName()); //
	 * System.out.println("ProjectName = " + getProjectName()); //
	 * System.out.println("ServerName = " + getServerName()); //
	 * System.out.println("ServerName = " + getPortNumber()); //
	 * System.out.println("Test Artifact folder = " + getArtifactFolderPath()); //
	 * System.out.println(getFolderStructure());
	 * 
	 * 
	 * // System.out.println(getUrl()); System.out.println(getMudId());
	 * 
	 * }
	 */
	// -----------------------------------------------------------------------------

	// Settings document
	private static Document SettingsDocument = null;
	private final static String SettingsFile = ".//src//main//java//config//Settings.xml";	
	public static void initSettings() {

		// Craig Adam
		// initialise settings document
		SettingsDocument = convertFileToXmlDocument(SettingsFile);

	}
	
	public static String getTagNameValue(String TagName) {

		/*
		 * Author : Craig Adam Modified : 07-10-2019 Description : Returns first
		 * matching node matching TagName
		 */

		// Get nodes by tag name
		String sValue = SettingsDocument.getElementsByTagName(TagName).item(0).getTextContent();
		return sValue;

	}

	public static String getNodeValueByName(String ChildTagName, String ParentTagName, String GeneralNodeName) {
		
		/*
		 * Author : Craig Adam Modified : 28-10-2019 
		 * Description : Returns node value from setting xml file, based on value of settings general section node name 
		 */
		
		// Example
		// ParentTagName = "testEnvironment"
		// ChildTagName = "url"
		// GeneralNodeName = "testEnv"
		// will return the url value for the "testEnv" specified in the general setting node -- ie if 'testEnv' = 'dev' will return url where 'testEnvironment' name = 'dev'

		// Get all parent nodes
		NodeList nCollectionNodes = getNodesByTagName(SettingsDocument, ParentTagName);

		// build map of criteria to match
		Map<String, String> mMatchCriteriaAttr = new HashMap<String, String>();
		mMatchCriteriaAttr.put("name", SettingsDocument.getElementsByTagName(GeneralNodeName).item(0).getTextContent());
		
		// find node with attributes looking for ie basic authentification with
		// qcAccountName as per in the xml general node
		Node nodeMatchedByAttributes = getNodeByAttributes(nCollectionNodes, mMatchCriteriaAttr);

		return ((Element) nodeMatchedByAttributes).getElementsByTagName(ChildTagName).item(0).getTextContent().trim();

	}
	
	
	

	public static String getBrowserLocation() {
		
		// Craig Adam
		// shortcut for value : url of current testEnv
		
		String ChildTagName = "browserlocation";
		String ParentTagName = "Browser";
		String GeneralNodeName = "testBrowser";
		
		return getNodeValueByName(ChildTagName, ParentTagName, GeneralNodeName);
		
		
	}
	
	public static String getUrl() {
		
		// Craig Adam
		// shortcut for value : url of current testEnv
		
		String ChildTagName = "url";
		String ParentTagName = "testEnvironment";
		String GeneralNodeName = "testEnv";
		
		return getNodeValueByName(ChildTagName, ParentTagName, GeneralNodeName);
		
		
	}


	public static String getMudId() {

		/*
		 * Author : Craig Adam Modified : 07-10-2019 
		 * Description : Returns username from setting xml file, based on simple account name in general settings
		 */
		
		// requires additional search criterial basic_authentication

		// Get credential nodes
		String sTagName = "credential";
		NodeList nCollectionNodes = getNodesByTagName(SettingsDocument, sTagName);

		// build map of criteria to match
		Map<String, String> mMatchCriteriaAttr = new HashMap<String, String>();
		mMatchCriteriaAttr.put("type", "basic_authentication");
		mMatchCriteriaAttr.put("account",
				SettingsDocument.getElementsByTagName("qcAccountName").item(0).getTextContent());

		// find node with attributes looking for ie basic authentification with
		// qcAccountName as per in the xml general node
		Node nodeMatchedByAttributes = getNodeByAttributes(nCollectionNodes, mMatchCriteriaAttr);

		return ((Element) nodeMatchedByAttributes).getElementsByTagName("mudid").item(0).getTextContent().trim();

	}

	public static String getPassword() {

		/*
		 * Author : Craig Adam Modified : 07-10-2019 Description : Returns encrypted
		 * password from setting xml file, based on simple account name in general
		 * settings
		 */

		// Get credential nodes
		NodeList nCollectionNodes = getNodesByTagName(SettingsDocument, "credential");

		// build map of criteria to match
		Map<String, String> mMatchCriteriaAttr = new HashMap<String, String>();
		mMatchCriteriaAttr.put("type", "basic_authentication");
		mMatchCriteriaAttr.put("account",
				SettingsDocument.getElementsByTagName("qcAccountName").item(0).getTextContent());

		// find node with attributes
		Node nodeMatchedByAttributes = getNodeByAttributes(nCollectionNodes, mMatchCriteriaAttr);

		return ((Element) nodeMatchedByAttributes).getElementsByTagName("password256").item(0).getTextContent().trim();

	}

	public static String getDomainName() {

		/*
		 * Author : Craig Adam Modified : 07-10-2019 Description : Returns domain name
		 * from setting xml file, based on simple project name in general settings
		 */

		// Get credential nodes
		NodeList nCollectionNodes = getNodesByTagName(SettingsDocument, "project");

		// build map of criteria to match
		Map<String, String> mMatchCriteriaAttr = new HashMap<String, String>();
		mMatchCriteriaAttr.put("name", SettingsDocument.getElementsByTagName("qcProject").item(0).getTextContent());

		// find node with attributes
		Node nodeMatchedByAttributes = getNodeByAttributes(nCollectionNodes, mMatchCriteriaAttr);

		return ((Element) nodeMatchedByAttributes).getElementsByTagName("domainName").item(0).getTextContent();

	}

	public static String getProjectName() {

		/*
		 * Author : Craig Adam Modified : 07-10-2019 Description : Returns project name
		 * from setting xml file, based on simple project name in general settings
		 */

		// Get credential nodes
		NodeList nCollectionNodes = getNodesByTagName(SettingsDocument, "project");

		// build map of criteria to match
		Map<String, String> mMatchCriteriaAttr = new HashMap<String, String>();
		mMatchCriteriaAttr.put("name", SettingsDocument.getElementsByTagName("qcProject").item(0).getTextContent());

		// find node with attributes
		Node nodeMatchedByAttributes = getNodeByAttributes(nCollectionNodes, mMatchCriteriaAttr);

		return ((Element) nodeMatchedByAttributes).getElementsByTagName("projectName").item(0).getTextContent();

	}

	public static String getServerName() {

		/*
		 * Author : Craig Adam Modified : 07-10-2019 Description : Returns server name
		 * from setting xml file, based on simple server name in general settings
		 */

		// Get credential nodes
		NodeList nCollectionNodes = getNodesByTagName(SettingsDocument, "server");

		// build map of criteria to match
		Map<String, String> mMatchCriteriaAttr = new HashMap<String, String>();
		mMatchCriteriaAttr.put("name", SettingsDocument.getElementsByTagName("qcServer").item(0).getTextContent());

		// find node with attributes
		Node nodeMatchedByAttributes = getNodeByAttributes(nCollectionNodes, mMatchCriteriaAttr);

		return ((Element) nodeMatchedByAttributes).getElementsByTagName("serverName").item(0).getTextContent();

	}

	public static String getPortNumber() {

		/*
		 * Author : Craig Adam Modified : 07-10-2019 Description : Returns port number
		 * from setting xml file, based on simple server name in general settings
		 */

		// Get credential nodes
		NodeList nCollectionNodes = getNodesByTagName(SettingsDocument, "server");

		// build map of criteria to match
		Map<String, String> mMatchCriteriaAttr = new HashMap<String, String>();
		mMatchCriteriaAttr.put("name", SettingsDocument.getElementsByTagName("qcServer").item(0).getTextContent());

		// find node with attributes
		Node nodeMatchedByAttributes = getNodeByAttributes(nCollectionNodes, mMatchCriteriaAttr);

		return ((Element) nodeMatchedByAttributes).getElementsByTagName("portNumber").item(0).getTextContent();

	}
	
	public static ArrayList<String> getFolderStructure() {

		// Craig Adam

		ArrayList<String> lFolderHeirachy = new ArrayList<String>();

		NodeList lNodes = SettingsDocument.getElementsByTagName("reportingFolder");

//		System.out.println("l = " + lNodes.getLength());

		for (int iNode = 0; iNode < lNodes.getLength(); iNode++) {

			Node nCurrent = lNodes.item(iNode);
			String sTextContent = nCurrent.getTextContent();

//ADD functionality / regex to allow for multiple and different types of formula - Craig 09-10-2019			
// Check if need to evaluate formula or method

			if (!sTextContent.contains("evaluateLocalTime") && !sTextContent.contains("evaluateLocalDate")) {

				lFolderHeirachy.add(sTextContent.trim());

			}

			String sPart1 = "";
			String sPart2 = "";
			if (sTextContent.contains("evaluateLocalTime")) {

				sPart1 = sTextContent.replaceAll("evaluateLocalTime", "");
				sPart2 = LocalTime.now().toString();
				lFolderHeirachy.add((sPart1 + sPart2).trim());

			}

			if (sTextContent.contains("evaluateLocalDate")) {

				sPart1 = sTextContent.replaceAll("evaluateLocalDate", "");
				sPart2 = LocalDate.now().toString();
				lFolderHeirachy.add((sPart1 + sPart2).trim());
			}

		} // end for

//			lFolderHeirachy.add("AutomationIII" + LocalTime.now());

		return lFolderHeirachy;

	}

}
