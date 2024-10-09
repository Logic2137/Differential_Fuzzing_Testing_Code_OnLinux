


import java.awt.*;
import java.awt.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Destination;
import java.util.Locale;

import javax.print.*;

class PrintDlgApp implements Printable {
        
        public PrintDlgApp() {
                super();
        }
        
        public static void main(java.lang.String[] args) {
                PrintDlgApp pd = new PrintDlgApp();
                PrinterJob pj = PrinterJob.getPrinterJob();
                System.out.println(pj);
                PrintRequestAttributeSet pSet = new HashPrintRequestAttributeSet();
                pSet.add(new Copies(1));
                
                PageFormat pf = new PageFormat();
                System.out.println("Setting Printable...pf = "+pf);
                if (pf == null) {
                    return;
                }
                pj.setPrintable(pd,pf);

                
                pSet.add(new Destination(new java.io.File("./out.prn").toURI()));
                System.out.println("open PrintDialog..");
                for (int i=0; i<2; i++) {
                if (pj.printDialog(pSet)) {
                        try {
                                System.out.println("About to print the data ...");
                                pj.print(pSet);
                                System.out.println("Printed");
                        }
                        catch (PrinterException pe) {
                                pe.printStackTrace();
                        }
                }
                }

        }

        
        public int print(Graphics g, PageFormat pf, int pi) throws
PrinterException {

                if (pi > 0) {
                        System.out.println("pi is greater than 0");
                        return Printable.NO_SUCH_PAGE;
                }
                
                Graphics2D g2 = (Graphics2D)g;
                g2.setColor(Color.black);
                g2.translate(pf.getImageableX(), pf.getImageableY());
                g2.drawRect(1,1,200,300);
                g2.drawRect(1,1,25,25);
                System.out.println("print method called "+pi);
                return Printable.PAGE_EXISTS;
        }
}
