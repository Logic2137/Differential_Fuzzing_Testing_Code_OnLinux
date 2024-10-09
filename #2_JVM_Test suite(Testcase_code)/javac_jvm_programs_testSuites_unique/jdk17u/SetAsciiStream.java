import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;

public class SetAsciiStream {

    public static void main(String[] args) throws Exception {
        SerialClob clob = new SerialClob(new char[0]);
        try {
            clob.setAsciiStream(0);
        } catch (SerialException e) {
            System.out.println("Test PASSED");
        }
    }
}
