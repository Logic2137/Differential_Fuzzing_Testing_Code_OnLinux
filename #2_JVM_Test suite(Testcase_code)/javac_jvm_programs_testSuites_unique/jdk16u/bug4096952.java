



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TimeZone;

public class bug4096952 {

    public static void main(String[] args) {
        int errors = 0;
        String[] ZONES = { "GMT", "MET", "IST" };
        for (String id : ZONES) {
            TimeZone zone = TimeZone.getTimeZone(id);
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try (ObjectOutputStream ostream = new ObjectOutputStream(baos)) {
                    ostream.writeObject(zone);
                }
                try (ObjectInputStream istream
                        = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
                    if (!zone.equals(istream.readObject())) {
                        errors++;
                        System.out.println("Time zone " + id + " are not equal to serialized/deserialized one.");
                    } else {
                        System.out.println("Time zone " + id + " ok.");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                errors++;
                System.out.println(e);
            }
        }
        if (errors > 0) {
            throw new RuntimeException("test failed");
        }
    }
}
