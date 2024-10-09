

package pkg;

import java.io.Serializable;


public class X implements Serializable {

    private int f;

    private X(){}

    private void m() {}

    
    private static class P implements Serializable {}

    
    public void foo() {}
}
