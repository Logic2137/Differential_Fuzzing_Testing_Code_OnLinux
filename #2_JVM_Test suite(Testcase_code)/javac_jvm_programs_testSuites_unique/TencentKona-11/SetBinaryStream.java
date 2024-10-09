
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;


public class SetBinaryStream {

    public static void main(String[] args) throws Exception {
        SerialBlob blob = new SerialBlob(new byte[0]);
        try {
            blob.setBinaryStream(0);
        } catch (SerialException e) {
            System.out.println("Test PASSED");
        }
    }

}
