package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringUtils {


	/**
	 * Escapes the string and then splits it based on the specified delimiter
	 * 
	 * @param stringToSplit
	 * @param delimiter
	 * @return string array of individual strings
	 * @author Craig Adam
	 * 
	 */
	public static String[] stringSplit(String stringToSplit, String delimiter) {

		return stringToSplit.split(escapeString(delimiter)); // Split on delimiter.;

	}

	/**
	 * Escapes the string and then splits it based on the specified delimiter up until a max number of parts.   The balance of string (left to right) will form the last part.
	 * 
	 * @param stringToSplit
	 * @param delimiter
	 * @param numberOfParts
	 * @return string array of individual strings
	 * @author Craig Adam
	 * 
	 */
	public static String[] stringSplit(String stringToSplit, String delimiter, int numberOfParts) {


		return stringToSplit.split(escapeString(delimiter), numberOfParts); // Split on delimiter.;

	}

	/**
	 * Escapes the string and then splits it based on the specified delimiter.   The delimiter will be appended to the left part. ie to the end of the previous part
	 * 
	 * @param stringToSplit
	 * @param delimiter
	 * @return string array of individual strings
	 * @author Craig Adam
	 *  
	 */
	public static String[] stringSplitAppendLeft(String stringToSplit, String delimiter) {

		// Craig Adam
		// add lookbehind to delimiter
		// will split into a max number of parts as per int NoParts, balance of string
		// (left to right) will form the last part
		// https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#quote-java.lang.String-
		// Handle metacharacter by escaping entire string
		return stringToSplit.split("(?<=" + escapeString(delimiter) + ")"); // Split on delimiter.;

	}

	/**
	 * Escapes the string and then splits it based on the specified delimiter up until a max number of parts.   The balance of string (left to right) will form the last part.   
	 * The delimiter will be appended to the left part. ie to the end of the previous part
	 * 
	 * @param stringToSplit
	 * @param delimiter
	 * @param numberOfParts
	 * @return string array of individual strings
	 * @author Craig Adam
	 *  
	 */
	public static String[] stringSplitAppendLeft(String stringToSplit, String delimiter, int numberOfParts) {

		// Craig Adam
		// add lookbehind to delimiter
		// will split into a max number of parts as per int NoParts, balance of string
		// (left to right) will form the last part
		// https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#quote-java.lang.String-
		// Handle metacharacter by escaping entire string
		return stringToSplit.split("(?<=" + escapeString(delimiter) + ")", numberOfParts); // Split on delimiter.;

	}

	/**
	 * Escapes the string and then splits it based on the specified delimiter.   The delimiter will be appended to the right part. ie to the start of the next part
	 * 
	 * @param stringToSplit
	 * @param delimiter
	 * @return string array of individual strings
	 * @author Craig Adam
	 *  
	 */	
	public static String[] stringSplitAppendRight(String stringToSplit, String delimiter) {

		// Craig Adam
		// add lookahead to delimiter
		// will split into a max number of parts as per int NoParts, balance of string
		// (left to right) will form the last part
		// https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#quote-java.lang.String-
		// Handle metacharacter by escaping entire string
		return stringToSplit.split("(?=" + escapeString(delimiter) + ")"); // Split on delimiter.;

	}

	/**
	 * Escapes the string and then splits it based on the specified delimiter up until a max number of parts.   The balance of string (left to right) will form the last part.   
	 * The delimiter will be appended to the right part. ie to the start of the next part
	 * 
	 * @param stringToSplit
	 * @param delimiter
	 * @param numberOfParts
	 * @return string array of individual strings
	 * @author Craig Adam
	 *  
	 */
	public static String[] stringSplitAppendRight(String stringToSplit, String delimiter, int numberOfParts) {

		// Craig Adam
		// add lookahead to delimiter
		// will split into a max number of parts as per int NoParts, balance of string
		// (left to right) will form the last part
		// https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#quote-java.lang.String-
		// Handle metacharacter by escaping entire string
		return stringToSplit.split("(?=" + escapeString(delimiter) + ")", numberOfParts); // Split on delimiter.;

	}
	
	/**
	 * Splits a string using StreaAPI in Java 8
	 * 
	 * @param stringToSplit : string that should be split
	 * @param delimiter : delimiter to split by
	 * @return list of strings
	 * @author Craig Adam
	 * <br>
	 */
	public static List<String> splitByDelimiter_Stream(String stringToSplit, String delimiter) {

		return Stream.of(stringToSplit.split("\\s" + delimiter + ",\\s*")).map(elem -> new String(elem))
				.collect(Collectors.toList());
		
	}

	/**
	 * Escapes meta characters in a string
	 * 
	 * @param unescapedString
	 * @return escapedString
	 * @author Craig Adam
	 * <br>
	 *         https://stackoverflow.com/questions/3481828/how-to-split-a-string-in-java <br>
	 * <br>
	 *         12 characters with special meanings: the backslash \, the caret ^,<br>
	 *         the dollar sign $, the period or dot ., the vertical bar or pipe
	 *         symbol |, the question mark ?, the asterisk or star *, the plus sign
	 *         +, the opening parenthesis (, the closing parenthesis ), and the
	 *         opening square bracket [, the opening curly brace {, These special
	 *         characters are often called "meta characters".<br>
	 * <br>
	 *         "meta characters" \ ^ $ . | ? * + ( ) [ {<br>
	 * <br>
	 */
	public static String escapeString(String unescapedString) {

		// Craig Adam

		// https://stackoverflow.com/questions/3481828/how-to-split-a-string-in-java

		/*
		 * 12 characters with special meanings: the backslash \, the caret ^, the dollar
		 * sign $, the period or dot ., the vertical bar or pipe symbol |, the question
		 * mark ?, the asterisk or star *, the plus sign +, the opening parenthesis (,
		 * the closing parenthesis ), and the opening square bracket [, the opening
		 * curly brace {, These special characters are often called "meta characters".
		 * 
		 * "metacharacters" \ ^ $ . | ? * + ( ) [ {
		 */

		// Handle metacharacter by escaping entire string
		// option use Regex pattern quote to escape string
		// https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#quote-java.lang.String-

		return Pattern.quote(unescapedString);

	}

	/**
	 * Reads bytes from file into a string
	 * 
	 * @param filePath : full absolute path to the file, including the extension
	 * @return an string with contents of specified file
	 * @throws IOException
	 * @author Craig Adam
	 * <br>
	 * 
	 */
	public static String readStringFromFile(String filePath) throws IOException {

		return new String(Files.readAllBytes(Paths.get(filePath)));

	}

}
