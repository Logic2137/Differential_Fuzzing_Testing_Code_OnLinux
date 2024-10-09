

package nsk.share;

import java.io.*;


public class Failure extends RuntimeException {
        
        public Failure(Throwable throwable) {
                super(throwable);
        }

        
        public Failure(String message) {
                super(message);
        }

        public Failure(String message, Throwable cause) {
                super(message, cause);
        }
}
