


import javax.print.*;
import javax.print.attribute.*;
import javax.print.event.*;
import javax.print.attribute.standard.*;

public class RemoveListener {
    public static void main(String[] args){
        PrintService[] pservices = PrintServiceLookup.lookupPrintServices(null, null);
        if (pservices.length == 0){
            return;
        }
        DocPrintJob pj = pservices[0].createPrintJob();
        PrintJobAttributeSet aset = new HashPrintJobAttributeSet();
        aset.add(JobState.PROCESSING);
        PrintJobAttributeListener listener = new PJAListener();
        pj.addPrintJobAttributeListener(listener, aset);
        pj.removePrintJobAttributeListener(listener);
        return;
    }
}

class PJAListener implements PrintJobAttributeListener {
    public void attributeUpdate(PrintJobAttributeEvent pjae){
        return;
    }
}
