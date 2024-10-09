
package test.rowset.filteredrowset;

import javax.sql.RowSet;
import javax.sql.rowset.Predicate;

public class PrimaryKeyFilter implements Predicate {

    private final int lo;

    private final int hi;

    private String colName = null;

    private int colNumber = -1;

    public PrimaryKeyFilter(int lo, int hi, int colNumber) {
        this.lo = lo;
        this.hi = hi;
        this.colNumber = colNumber;
    }

    public PrimaryKeyFilter(int lo, int hi, String colName) {
        this.lo = lo;
        this.hi = hi;
        this.colName = colName;
    }

    public boolean evaluate(Object value, String columnName) {
        boolean result = false;
        if (columnName.equalsIgnoreCase(this.colName)) {
            int columnValue = ((Integer) value);
            result = (columnValue >= this.lo) && (columnValue <= this.hi);
        }
        return result;
    }

    public boolean evaluate(Object value, int columnNumber) {
        boolean result = false;
        if (this.colNumber == columnNumber) {
            int columnValue = (Integer) value;
            result = (columnValue >= this.lo) && (columnValue <= this.hi);
        }
        return result;
    }

    public boolean evaluate(RowSet rs) {
        boolean result = false;
        try {
            int columnValue = -1;
            if (this.colNumber > 0) {
                columnValue = rs.getInt(this.colNumber);
            } else if (this.colName != null) {
                columnValue = rs.getInt(this.colName);
            }
            if ((columnValue >= this.lo) && (columnValue <= this.hi)) {
                result = true;
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            result = false;
        }
        return result;
    }
}
