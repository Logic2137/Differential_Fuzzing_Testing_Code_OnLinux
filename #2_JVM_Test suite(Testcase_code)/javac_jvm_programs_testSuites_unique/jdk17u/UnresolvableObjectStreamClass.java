import java.io.*;

public class UnresolvableObjectStreamClass {

    public static void main(String[] args) throws Throwable {
        if (args.length > 0 && args[0].equals("serialize")) {
            try (FileOutputStream fos = new FileOutputStream("temp1.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                ObjectStreamClass osc = ObjectStreamClass.lookup(MySerializable.class);
                oos.writeObject(osc);
            }
        } else if (args.length > 0 && args[0].equals("deserialize")) {
            try (FileInputStream fis = new FileInputStream("temp1.ser");
                ObjectInputStream ois = new ObjectInputStream(fis);
                FileOutputStream fos = new FileOutputStream("temp2.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos) {
                }) {
                ObjectStreamClass osc = (ObjectStreamClass) ois.readObject();
                try {
                    oos.writeObject(osc);
                } catch (NullPointerException e) {
                    throw new RuntimeException("Failed to write" + " unresolvable ObjectStreamClass", e);
                }
            }
        } else {
            throw new RuntimeException("The command line option must be" + " one of: serialize or deserialize");
        }
    }
}

class MySerializable implements Serializable {

    private static final long serialVersionUID = 1L;
}
