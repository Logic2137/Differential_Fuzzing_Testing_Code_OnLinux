
package vm.share.gc;


public class HeapOOMEException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public HeapOOMEException(String string) {
        super(string);
    }

}
