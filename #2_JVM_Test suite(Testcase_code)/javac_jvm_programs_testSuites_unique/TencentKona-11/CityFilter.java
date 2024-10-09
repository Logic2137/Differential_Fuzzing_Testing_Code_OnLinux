
package test.rowset.filteredrowset;

import java.sql.SQLException;
import javax.sql.RowSet;
import javax.sql.rowset.Predicate;


public class CityFilter implements Predicate {

    private final String[] cities;
    private String colName = null;
    private int colNumber = -1;

    public CityFilter(String[] cities, String colName) {
        this.cities = cities;
        this.colName = colName;
    }

    public CityFilter(String[] cities, int colNumber) {
        this.cities = cities;
        this.colNumber = colNumber;
    }

    public boolean evaluate(Object value, String colName) {

        if (colName.equalsIgnoreCase(this.colName)) {
            for (String city : cities) {
                if (city.equalsIgnoreCase((String) value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean evaluate(Object value, int colNumber) {

        if (colNumber == this.colNumber) {
            for (String city : this.cities) {
                if (city.equalsIgnoreCase((String) value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean evaluate(RowSet rs) {

        boolean result = false;

        if (rs == null) {
            return false;
        }

        try {
            for (String city : cities) {

                String val = "";
                if (colNumber > 0) {
                    val = (String) rs.getObject(colNumber);
                } else if (colName != null) {
                    val = (String) rs.getObject(colName);
                }

                if (val.equalsIgnoreCase(city)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            result = false;
        }
        return result;
    }
}
