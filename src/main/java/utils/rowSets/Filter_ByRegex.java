package utils.rowSets;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.RowSet;
import javax.sql.rowset.FilteredRowSet;
import javax.sql.rowset.Predicate;

// Craig Adam
// Modified from :
// https://blog.pavelsklenar.com/using-filteredrowset-simple-example/

/**
 * Search Filter for {@link FilteredRowSet}
 *
 * @author pavel.sklenar
 *
 */
public class Filter_ByRegex implements Predicate {

	private Pattern pattern;
	private int columnNo;

	public Filter_ByRegex(String searchRegex, int matchColumnNumber) {

		this.columnNo = matchColumnNumber;
//    	 System.out.println("Filter_ByRegex.Filter_ByRegex called ");

		if (searchRegex != null && !searchRegex.isEmpty()) {
			pattern = Pattern.compile(searchRegex);
		}

	}

	public boolean evaluate(RowSet rs) {

		// System.out.println("Filter_ByRegex.evaluate called ");

		try {
			if (!rs.isAfterLast()) {
				// String name = rs.getString("name");

				// Replace hardcoded values with column number to search
				String stringValue = rs.getString(columnNo);

				// System.out.println("Ln 58");
				// System.out.println(String.format("Searching for pattern '%s' in %s",
				// pattern.toString(), stringValue));

				Matcher matcher = pattern.matcher(stringValue);
				return matcher.matches();
			} else
				return false;
		} catch (Exception e) {

			e.printStackTrace();
			return false;

		}

	}

	/*
	 * Other methods, like evaluate(Object value, int column) and evaluate(Object
	 * value, String columnName), are called when you are inserting new rows to a
	 * FilteredRowSet instance.
	 */

	public boolean evaluate(Object value, int column) throws SQLException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean evaluate(Object value, String columnName) throws SQLException {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
