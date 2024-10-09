
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;


public class SetCharacterStream {

    public static void main(String[] args) throws Exception {
        SerialClob clob = new SerialClob(new char[0]);
        try {
            clob.setCharacterStream(0);
        } catch (SerialException e) {
             System.out.println("Test PASSED");
        }
    }

}
