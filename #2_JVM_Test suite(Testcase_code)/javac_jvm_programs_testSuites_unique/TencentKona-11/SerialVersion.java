



import java.security.*;
import java.io.*;

public class SerialVersion {

    public static void main(String[] args) throws Exception {
        String dir = System.getProperty("test.src");
        File  sFile =  new File (dir,"SerialVersion.1.2.1");
        
        try (FileInputStream fis = new FileInputStream(sFile);
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            PermissionCollection pc = (PermissionCollection)ois.readObject();
            System.out.println("1.2.1 collection = " + pc);
        }

        
        sFile =  new File (dir,"SerialVersion.1.3.1");

        try (FileInputStream fis = new FileInputStream(sFile);
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            PermissionCollection pc = (PermissionCollection)ois.readObject();
            System.out.println("1.3.1 collection = " + pc);
        }

        
        sFile =  new File (dir,"SerialVersion.1.4");
        try (FileInputStream fis = new FileInputStream(sFile);
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            PermissionCollection pc = (PermissionCollection)ois.readObject();
            System.out.println("1.4 collection = " + pc);
        }

        
        MyPermission mp = new MyPermission("SerialVersionTest");
        PermissionCollection bpc = mp.newPermissionCollection();
        sFile =  new File (dir,"SerialVersion.current");
        try (FileOutputStream fos = new FileOutputStream("SerialVersion.current");
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(bpc);
        }

        
        try (FileInputStream fis = new FileInputStream("SerialVersion.current");
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            PermissionCollection pc = (PermissionCollection)ois.readObject();
            System.out.println("current collection = " + pc);
        }
    }
}

class MyPermission extends BasicPermission {
    public MyPermission(String name) {
        super(name);
    }
}
