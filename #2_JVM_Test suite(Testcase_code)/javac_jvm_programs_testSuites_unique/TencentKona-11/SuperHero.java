
package util;

import java.io.Serializable;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class SuperHero implements SQLData, Serializable {

    private String first;
    private String last;
    private String type = "SUPERHERO";
    private Integer firstYear;
    private String secretIdentity;

    public SuperHero() {

    }

    public SuperHero(String sqlType, String fname, String lname, Integer year,
            String identity) {
        first = fname;
        last = lname;
        type = sqlType;
        firstYear = year;
        secretIdentity = identity;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return type;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        first = stream.readString();
        last = stream.readString();
        firstYear = stream.readInt();
        secretIdentity = stream.readString();
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(first);
        stream.writeString(last);
        stream.writeInt(firstYear);
        stream.writeString(secretIdentity);
    }

    @Override
    public String toString() {
        return "[ name =" + first + " " + last + " "
                + firstYear + " " + secretIdentity + " ]";
    }

    public void setIdentity(String identity) {
        secretIdentity = identity;
    }

    public String getIdentity() {
        return secretIdentity;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SuperHero) {
            SuperHero ss = (SuperHero) obj;
            return first.equals(ss.first) && last.equals(ss.last)
                    && firstYear.equals(ss.firstYear)
                    && type.equals(ss.type)
                    && secretIdentity.equals(ss.secretIdentity);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ((31 + first.hashCode()) * 31) * 31
                + ((31 + last.hashCode()) * 31) * 31
                + ((31 + firstYear.hashCode()) * 31) * 31
                + ((31 + type.hashCode()) * 31) * 31
                + secretIdentity.hashCode();
    }
}
