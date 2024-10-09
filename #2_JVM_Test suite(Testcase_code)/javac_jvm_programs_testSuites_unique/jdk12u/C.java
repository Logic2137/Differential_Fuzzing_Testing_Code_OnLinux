



import java.io.*;

public class C implements Serializable {
    Object writeReplace() throws ObjectStreamException {
        throw new Error("package-private writeReplace called");
    }

    Object readResolve() throws ObjectStreamException {
        throw new Error("package-private readResolve called");
    }
}
