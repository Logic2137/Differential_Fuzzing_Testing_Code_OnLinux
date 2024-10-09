
package test.auctionportal;

import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMError;


public class MyDOMErrorHandler implements DOMErrorHandler {
    
    private volatile boolean errorOccured = false;

    
    @Override
    public boolean handleError (DOMError error) {
        System.err.println( "ERROR" + error.getMessage());
        System.err.println( "ERROR" + error.getRelatedData());
        errorOccured = true;
        return true;
    }

    
    public boolean isError() {
        return errorOccured;
    }
}
