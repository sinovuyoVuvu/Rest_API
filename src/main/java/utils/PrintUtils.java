package utils;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

public class PrintUtils {

	
	// TODO try simplify using <generic return type example> instead of separate method for each
	public static void main(String[] args) {
		

	}
	

	
	public static void printObject(List<String> List) {

		
		// TODO desc
		System.out.println("[PrintUtils Ln 20]");
		for (int i = 0; i < List.size(); i++) {

			
			System.out.println(List.get(i).toString());
			System.out.println(System.lineSeparator());
		}

	}

	public static void printObject(ArrayList<String> lMethodParameterTypes) {

		// Craig Adam
		// TODO desc
		System.out.println("[PrintUtils Ln 30]");
		for (int i = 0; i < lMethodParameterTypes.size(); i++) {

			System.out.println(lMethodParameterTypes.get(i));
//			System.out.println(System.lineSeparator());

		}

	}
	

	
	public static void printObject(Class<?>[] ClassArray) {

		// Craig Adam
		// TODO desc
		System.out.println("[PrintUtils Ln 46]");
		for (int i = 0; i < ClassArray.length; i++) {

			System.out.println(ClassArray[i].toString());
			System.out.println(System.lineSeparator());
		}

	};

	public static void printObject(Method[] MethodArray) {

		// Craig Adam
		// TODO desc
		System.out.println("[PrintUtils Ln 58]");
		for (int i = 0; i < MethodArray.length; i++) {

			System.out.println(MethodArray[i].toString());
//			System.out.println(System.lineSeparator());
		}

	};

	public static void printObject(String[] aStringArray) {

		// Craig Adam
		// TODO desc
		System.out.println("[PrintUtils Ln 70]");
		for (int i = 0; i < aStringArray.length; i++) {
			// System.out.println("i = " + i);
			System.out.println(aStringArray[i].toString());
			// System.out.println(System.lineSeparator());
		}

	};
	
	public static void printObject(CachedRowSet CachedRowSet) {

		int iColCount;
		try {
			iColCount = CachedRowSet.getMetaData().getColumnCount();

			System.out.print("[PrintUtils Ln 130]");
			for (int iColNo = 1; iColNo <= iColCount; iColNo++) {

				System.out.print(CachedRowSet.getMetaData().getColumnName(iColNo) + " || ");

			}

			System.out.print("---End Headers---");
			System.out.println(System.lineSeparator());

			int iRowCount = (CachedRowSet).size();
			CachedRowSet.beforeFirst();
			
			System.out.print("[JDBC Support Ln 143]");
			for (int iRowNo = 1; iRowNo <= iRowCount; iRowNo++) {

				CachedRowSet.next();

				for (int iColNo = 1; iColNo <= iColCount; iColNo++) {

					System.out.print(CachedRowSet.getString(iColNo) + " || ");

				}

				System.out.print("---End Row---");
				System.out.println(System.lineSeparator());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.print("remove this print");

	}


}
