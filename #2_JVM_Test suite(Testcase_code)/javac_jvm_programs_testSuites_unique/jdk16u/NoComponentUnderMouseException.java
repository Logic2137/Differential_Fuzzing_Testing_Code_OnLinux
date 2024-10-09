
package org.netbeans.jemmy;


public class NoComponentUnderMouseException extends RuntimeException {

    private static final long serialVersionUID = 42L;

    
    public NoComponentUnderMouseException() {
        super("No component under the mouse!");
    }

}
