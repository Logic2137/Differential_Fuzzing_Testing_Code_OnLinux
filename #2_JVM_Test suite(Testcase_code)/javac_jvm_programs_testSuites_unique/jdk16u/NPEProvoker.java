



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class NPEProvoker implements java.io.Externalizable {
    private static final long serialVersionUID = 1L;

    private String test = "test";

    public void readExternal(ObjectInput in) throws IOException,
        ClassNotFoundException
    {
        throw new IOException(); 
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(test);
    }

    public static void main(String[] args) {
        System.err.println("\n Regression test for bug 6541870\n");
        try {
            ArrayList<NPEProvoker> list = new ArrayList<>();
            list.add(new NPEProvoker());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(list);

            ObjectInputStream ois =
                new ObjectInputStream(new ByteArrayInputStream(
                baos.toByteArray()));
            ois.readObject();
            throw new Error();
        } catch (IOException e) {
            System.err.println("\nTEST PASSED");
        } catch (ClassNotFoundException e) {
            throw new Error();
        } catch (NullPointerException e) {
            throw new Error();
        }
    }
}
