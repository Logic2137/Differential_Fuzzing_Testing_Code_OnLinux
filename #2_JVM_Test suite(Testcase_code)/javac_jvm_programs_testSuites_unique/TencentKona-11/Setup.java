



import java.io.*;

class A implements Serializable {
    private static final long serialVersionUID = 0L;
    int i;
}

class B implements Serializable {
    private static final long serialVersionUID = 0L;
    int i;
}

public class Setup {
    public static void main(String[] args) throws Exception {
        ObjectOutputStream oout =
            new ObjectOutputStream(new FileOutputStream("a.ser"));
        oout.writeObject(new A());
        oout.close();

        oout = new ObjectOutputStream(new FileOutputStream("b.ser"));
        oout.writeObject(new B());
        oout.close();
    }
}
