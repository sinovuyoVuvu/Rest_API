package utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Pattern;



public class ReflectionSupportUtils {

	@SuppressWarnings("unchecked") // unchecked class cast
	public static <Generic> Generic invokeMethodGeneric(String MethodName, String QualifiedClassName,
			ArrayList<Object> lArgs) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {

		// Craig Adam
		// invokes a specific method in the qualified class
		// NB this uses a generic return type and as a result will throw a class
		// exception error if the
		// incorrect return type is specified
		// 08-11-2019

		// convert ArrayList into object list that .invoke is expecting
		int iSize = lArgs.size();
		Object arglist[] = new Object[iSize];

		for (int i = 0; i < iSize; i++) {

			arglist[i] = castToObjectType(lArgs.get(i));

		}

		// build parameter type object that .getMethod is expecting
		Class<?> partypes[] = new Class[iSize];
		for (int i = 0; i < iSize; i++) {

			partypes[i] = getObjectType(lArgs.get(i));

		}

		// get class by fully qualified name
		Class<?> cDynamicClass = Class.forName(QualifiedClassName);

		// get method by name and parameter types
		Method mDynamicMethod = cDynamicClass.getMethod(MethodName, partypes);

		// return invoked method return value / object
		return ((Generic) ((Generic) (mDynamicMethod.invoke(cDynamicClass.newInstance(), arglist))));

	}
	
	public static String invokeMethodString(String MethodName, String QualifiedClassName, ArrayList<Object> lArgs)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InstantiationException {

		// Craig Adam
		// invokes a specific method in the qualified class
		// return type will be cast to String Will throw error if object returned from
		// method cannot be cast to a string.
		// 08-11-2019

		// convert ArrayList into object list that .invoke is expecting
		int iSize = lArgs.size();
		Object arglist[] = new Object[iSize];

		for (int i = 0; i < iSize; i++) {

			// cast to mapped object types
			arglist[i] = castToObjectType(lArgs.get(i));
		}

		// build parameter type object that .getMethod is expecting
		Class<?> partypes[] = new Class[iSize];
		for (int i = 0; i < iSize; i++) {

			partypes[i] = getObjectType(lArgs.get(i));

		}

		// get class by fully qualified name
		Class<?> cDynamicClass = Class.forName(QualifiedClassName);

		// get method by name and parameter types
		Method mDynamicMethod = cDynamicClass.getMethod(MethodName, partypes);

		// return invoked method return value / object
		return (String) mDynamicMethod.invoke(cDynamicClass.newInstance(), arglist);

	}


	
	private static Class<?> getObjectType(Object CurrentObj) throws ClassNotFoundException {

        // Craig Adam

        // integer type
        if (CurrentObj instanceof Integer) {
             return Integer.TYPE;
        }
        // byte type
        else if (CurrentObj instanceof Byte) {
             return Byte.TYPE;
        }
        // short type
        else if (CurrentObj instanceof Short) {
             return Short.TYPE;
        }
        // long type
        else if (CurrentObj instanceof Long) {
             return Long.TYPE;
        }
        // float type
        else if (CurrentObj instanceof Float) {
             return Float.TYPE;
        }
        // double type
        else if (CurrentObj instanceof Double) {
             return Double.TYPE;
        }
        // char type
        else if (CurrentObj instanceof Character) {
             return Character.TYPE;
        }
        // long type
        else if (CurrentObj instanceof Boolean) {
             return Boolean.TYPE;
        // is an object type  
        } else {
             
             Class<?> cClass = Class.forName(CurrentObj.getClass().getTypeName());
             
             // Map WebRowSetImpl to WebRowSet 
             if(cClass.toString().contentEquals("class com.sun.rowset.WebRowSetImpl")) {
                        
                  return Class.forName("javax.sql.rowset.WebRowSet");
                  
             } 
                  
             return cClass;
        }

  }

	
	
	
   


	public static Class<?> getMethodReturnType(String MethodName, String QualifiedClassName, ArrayList<Object> lArgs)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException {

		// Craig Adam
		// returns return type of a specific method in the qualified class
		// 08-11-2019

		int iSize = lArgs.size();

		// build parameter type object that .getMethod is expecting
		Class<?> partypes[] = new Class[iSize];
		for (int i = 0; i < iSize; i++) {

			partypes[i] = getObjectType(lArgs.get(i));

		}

		// Get class
		Class<?> cDynamicClass = getClass_ByName(QualifiedClassName);

		// Get method
		Method mDynamicMethod = cDynamicClass.getMethod(MethodName, partypes);

		return mDynamicMethod.getReturnType();

	}

	public static Method[] getMethods_ByClassName(String QualifiedClassName) {

		// Craig Adam
		// get all methods of a class of QualifiedClassName
		// 08-11-2019

		Class<?> cDynamicClass = null;
		try {
			cDynamicClass = Class.forName(QualifiedClassName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Method[] aMethods = cDynamicClass.getMethods();

		return aMethods;

	}

	public static ArrayList<String> getReturnTypes_ByClassName(String QualifiedClassName) {

		// Craig Adam
		// get return types of all methods in a Class
		// 08-11-2019

		Class<?> cDynamicClass = null;
		try {
			cDynamicClass = Class.forName(QualifiedClassName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Method[] aMethods = cDynamicClass.getMethods();

		ArrayList<String> lReturnTypes = new ArrayList<String>();
		for (int i = 0; i < aMethods.length - 1; i++) {

			lReturnTypes.add(aMethods[i].getReturnType().toString());

		}

		return lReturnTypes;

	}

	
	
	
	
	//////////////////////////////////////
	///// REFACTOR BELOW
	//////////////////////////////////////

	@SuppressWarnings("unused")
	private static Method getMethod_ByName_ByClass_ByArgs(String MethodName, String QualifiedClassName,
			ArrayList<Object> lArgs) throws ClassNotFoundException, NoSuchMethodException, SecurityException {

		// Craig Adam

		// convert ArrayList into object list that .invoke is expecting
		int iSize = lArgs.size();
		Object arglist[] = new Object[iSize];

		for (int i = 0; i < iSize; i++) {

			arglist[i] = castToObjectType(lArgs.get(i));

		}

		// build parameter type object that .getMethod is expecting
		Class<?> partypes[] = new Class[iSize];
		for (int i = 0; i < iSize; i++) {

			partypes[i] = getObjectType(lArgs.get(i));

		}

		// Get class
		Class<?> cDynamicClass = getClass_ByName(QualifiedClassName);

		// Get method
		Method mDynamicMethod = cDynamicClass.getMethod(MethodName, partypes);

	
		return mDynamicMethod;

	}

	@SuppressWarnings("unused")
	private static String getParameterTypes_ByClassName(String QualifiedClassName) {

		// Craig Adam

		Class<?> cDynamicClass = null;
		try {
			cDynamicClass = Class.forName(QualifiedClassName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Method[] aMethods = cDynamicClass.getMethods();

		ArrayList<String> lMethodParameterTypes = new ArrayList<String>();

		for (int i = 0; i < aMethods.length - 1; i++) {

			System.out.println("Method details : " + aMethods[i].toString());
			String InputString = aMethods[i].toString();
			String LowerBound = "(";
			String UpperBound = ")";

			String sParameterTypes = extractRev(InputString, LowerBound, UpperBound).toString();

			String[] lParameterTypes = stringSplit(sParameterTypes, ",");

			

			System.out.println("");

		}

		return null;

	}

	private static Class<?> getClass_ByName(String QualifiedClassName) {

		// Craig Adam

		Class<?> cDynamicClass = null;
		try {
			cDynamicClass = Class.forName(QualifiedClassName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cDynamicClass;

	}




	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO - update to reusable methods library
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Already in reusable methods library
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static Object castToObjectType(Object CurrentObj) {

		// Craig Adam
		// https://www.baeldung.com/java-primitives-vs-objects
		// for an explanation of why

		Object oOut = null;

		// string
		if (CurrentObj instanceof String) {
			oOut = new String((String) CurrentObj);
		}
		// integer type
		else if (CurrentObj instanceof Integer) {
			oOut = new Integer((int) CurrentObj);
		}
		// byte type
		else if (CurrentObj instanceof Byte) {
			oOut = new Byte((byte) CurrentObj);
		}
		// short type
		else if (CurrentObj instanceof Short) {
			oOut = new Short((short) CurrentObj);
		}
		// long type
		else if (CurrentObj instanceof Long) {
			oOut = new Long((long) CurrentObj);
		}
		// float type
		else if (CurrentObj instanceof Float) {
			oOut = new Float((float) CurrentObj);
		}
		// double type
		else if (CurrentObj instanceof Double) {
			oOut = new Double((double) CurrentObj);
		}
		// char type
		else if (CurrentObj instanceof Character) {
			oOut = new Character((char) CurrentObj);
		}
		// long type
		else if (CurrentObj instanceof Boolean) {
			oOut = new Boolean((boolean) CurrentObj);
		}
		// object type
		else if (CurrentObj instanceof Object) {

			oOut = CurrentObj;
		}

		return oOut;
	}

	
	private static String[] stringSplit(String StringToSplit, String Delimiter) {

		// Craig Adam
		// will split into all available parts by delimiter
		// https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#quote-java.lang.String-
		// Handle metacharacter by escaping entire string
		return StringToSplit.split(stringEscape(Delimiter)); // Split on delimiter.;

	}

	private static String stringEscape(String InputString) {

		// Craig Adam

		// https://stackoverflow.com/questions/3481828/how-to-split-a-string-in-java

		/*
		 * 12 characters with special meanings: the backslash \, the caret ^, the dollar
		 * sign $, the period or dot ., the vertical bar or pipe symbol |, the question
		 * mark ?, the asterisk or star *, the plus sign +, the opening parenthesis (,
		 * the closing parenthesis ), and the opening square bracket [, the opening
		 * curly brace {, These special characters are often called "metacharacters".
		 * 
		 * "metacharacters" \ ^ $ . | ? * + ( ) [ {
		 */

		// Handle metacharacter by escaping entire string
		// option use Regex pattern quote to escape string
		// https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#quote-java.lang.String-

		return Pattern.quote(InputString);

	}

	private static String extractRev(String InputString, String LowerBound, String UpperBound) {

		// Craig Adam
		// Extract string starting from end (ie will return last occurrence)

		Integer iIndexLower = InputString.lastIndexOf(LowerBound) + 1;
		Integer iIndexUpper = InputString.lastIndexOf(UpperBound);
		String sResult = InputString.substring(iIndexLower, iIndexUpper);
		return sResult;

	}

}
