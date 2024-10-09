

package pkg1;

import java.io.*;



class PrivateIncludeInnerClass {

    
    private static class PriInnerClass implements java.io.Serializable {

        public final int SERIALIZABLE_CONSTANT = 1;

        
        private void readObject(ObjectInputStream s) throws IOException {
        }

        
        private void writeObject(ObjectOutputStream s) throws IOException {
        }
    }
}
