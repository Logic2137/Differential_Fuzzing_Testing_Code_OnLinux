
package pkg1;

import java.io.*;

class ProtectedInnerClass {

    protected static class ProInnerClass implements java.io.Serializable {

        public final int SERIALIZABLE_CONSTANT1 = 1;

        private void readObject(ObjectInputStream s) throws IOException {
        }

        private void writeObject(ObjectOutputStream s) throws IOException {
        }
    }
}
