

package pkg1;

import java.io.*;



class NestedInnerClass {

    private static class InnerClass {

        protected static class ProNestedInnerClass implements java.io.Serializable {

            public final int SERIALIZABLE_CONSTANT2 = 1;

            
            private void readObject(ObjectInputStream s) throws IOException {
            }

            
            private void writeObject(ObjectOutputStream s) throws IOException {
            }
        }
    }
}
