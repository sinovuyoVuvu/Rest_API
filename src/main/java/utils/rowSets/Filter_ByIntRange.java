package utils.rowSets;

import java.sql.SQLException;

import javax.sql.RowSet;
import javax.sql.rowset.FilteredRowSet;
import javax.sql.rowset.Predicate;

// modified from "
// http://www.java2s.com/Code/Java/Database-SQL-JDBC/FilteredRowSetDemo.htm

public class Filter_ByIntRange implements Predicate {

	private int lowInt;

	private int highInt;

	private int columnNo;

	private String columnName;

	// TODO update to search for column index based on name

	public Filter_ByIntRange(int lowInt, int highInt, int columnNo, String columnMatchName) {

		//
		// Column no is base 1
		// less than or equal to both upper and lower int

		this.lowInt = lowInt;
		this.highInt = highInt;
		this.columnName = columnMatchName;
		this.columnNo = columnNo;
	}

	public Filter_ByIntRange(int lowInt, int highInt, int columnIndex) {

		this(lowInt, highInt, columnIndex, ""); // the 4th parameter does not alter results but cannot be removed
		// investigate and fix this - Craig Adam 04-02-2020

	}

	public boolean evaluate(RowSet rs) {
		if (rs == null) {
			return false;
		}

		FilteredRowSet frs = (FilteredRowSet) rs;

		boolean evaluation = false;

		try {
			int columnValue = frs.getInt(this.columnNo);
			if ((columnValue >= this.lowInt) && (columnValue <= this.highInt)) {
				evaluation = true;
			}
		} catch (SQLException e) {
			return false;
		}
		return evaluation;
	}

	/*
	 * Other methods, like evaluate(Object value, int column) and evaluate(Object
	 * value, String columnName), are called when you are inserting new rows to a
	 * FilteredRowSet instance.
	 */

	public boolean evaluate(Object value, String columnName) {
		boolean evaluation = true;
		if (columnName.equalsIgnoreCase(this.columnName)) {
			int columnValue = ((Integer) value).intValue();
			if ((columnValue >= this.lowInt) && (columnValue <= this.highInt)) {
				evaluation = true;
			} else {
				evaluation = false;
			}
		}
		return evaluation;
	}

	public boolean evaluate(Object value, int columnNumber) {
		boolean evaluation = true;
		if (columnNo == columnNumber) {
			int columnValue = ((Integer) value).intValue();
			if ((columnValue >= this.lowInt) && (columnValue <= this.highInt)) {
				evaluation = true;
			} else {
				evaluation = false;
			}
		}
		return evaluation;
	}

}
