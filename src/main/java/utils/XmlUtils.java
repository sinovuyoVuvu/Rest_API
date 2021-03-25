package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtils {

	// Craig Adam

	public static void main(String[] args) {

		// Debug

	}

	public static Document convertFileToXmlDocument(String XmlFullFilePath) {
		// https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
		try {
			File xmlFile = new File(XmlFullFilePath);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			return doc;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Node getNodeByAttributes(NodeList Nodes, Map<String, String> MatchCriteria) {

		// Craig Adam
		// will return null if match NOT found
		// will return 1st matched node if found

		// Split HashMap into individual arrays
		ArrayList<String> lNames = new ArrayList<String>(MatchCriteria.keySet());
		ArrayList<String> lValues = new ArrayList<String>(MatchCriteria.values());

		for (int iNodeNo = 0; iNodeNo < Nodes.getLength(); iNodeNo++) {

			// set current node
			Node currentNode = Nodes.item(iNodeNo);

			// Check node.type
//			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {

			// Check that the node has attributes
			if (currentNode.hasAttributes()) {

				// iterate through the attributes and look for a match
				NamedNodeMap attributes = currentNode.getAttributes();

				// iterate through each name and check that condition is met
				Boolean isMatched = null;
				for (int iMatchNo = 0; iMatchNo < lNames.size(); iMatchNo++) {

					// Compare value of current attribute against required
					String sAttributeValue = attributes.getNamedItem(lNames.get(iMatchNo)).getNodeValue().trim()
							.toLowerCase();
					if (sAttributeValue.compareTo(lValues.get(iMatchNo).trim().toLowerCase()) == 0) {

						// This condition is matched
						isMatched = true;

					} else {

						// This condition is matched
						isMatched = false;

						// early exit -- match failure for this node
						break;

					}

				}

				// Exit search if all conditions have been met for this node
				if (isMatched) {

					// if all conditions are matched
					return currentNode; // return will exit the method with the returned value

				} else {

					// this node is NOT a match

				}

			} else {

				// FAIL trying to match by attribute and this element does not contain any
				// attributes
				// System.out.println("This element does not have any attributes - ln 64");

			}

//			} else {

			// this node is not an element node

//			}

		}

		// If no match if found will NOT return with node above --- return null to show
		// that no match found
		return null;

	}
	
	public static NodeList getNodesByTagName(Document XmlDocument, String TagName) {

		NodeList nList = XmlDocument.getElementsByTagName(TagName);

		return nList;

	}


}
