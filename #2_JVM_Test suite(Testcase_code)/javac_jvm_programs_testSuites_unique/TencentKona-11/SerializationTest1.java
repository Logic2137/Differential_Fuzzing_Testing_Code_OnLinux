



import java.io.*;
import javax.management.*;

public class SerializationTest1 {
    public static void main(String[] args) throws Exception {
        MBeanInfo mi1 = new MBeanInfo("",
                                      "",
                                      new MBeanAttributeInfo[]{},
                                      new MBeanConstructorInfo[]{},
                                      new MBeanOperationInfo[]{},
                                      new MBeanNotificationInfo[]{},
                                      ImmutableDescriptor.EMPTY_DESCRIPTOR);

        test(mi1);

        MBeanFeatureInfo mfi2 = new MBeanFeatureInfo("",
                                                     "",
                                                     ImmutableDescriptor.EMPTY_DESCRIPTOR);

        test(mfi2);
    }

    public static void test(Object obj) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);

        final boolean[] failed = new boolean[]{false};
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        final ObjectInputStream ois = new ObjectInputStream(bais) {
            protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                System.out.println("*** " + desc.getName());
                if (desc.getName().equals("[Ljava.lang.Object;")) { 
                    Thread.dumpStack();
                    for(StackTraceElement e : Thread.currentThread().getStackTrace()) { 
                        if (e.getMethodName().equals("skipCustomData")) { 
                            failed[0] = true;
                        }
                    }
                }
                return super.resolveClass(desc); 
            }
        };
        Object newObj = ois.readObject();

        if (failed[0]) {
            throw new RuntimeException("Zero-length descriptor not read back");
        }
    }
}
