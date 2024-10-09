
package pkg1;

import java.io.*;

class PublicExcludeInnerClass {

    public static class PubInnerClass implements java.io.Serializable {

        public final int SERIALIZABLE_CONSTANT3 = 1;

        private void readObject(ObjectInputStream s) throws IOException {
        }

        private void writeObject(ObjectOutputStream s) throws IOException {
        }
    }
}
