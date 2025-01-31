




import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.*;
import javax.print.attribute.ResolutionSyntax;

public class TestUnsupportedResolution implements Printable
{
public static void main(String[] args)
{
    System.out.println("USAGE: default or no args: it will test 300 dpi\n       args is \"600\" : it will test 600 dpi\n------------------------------------------------------\n");
    TestUnsupportedResolution pt=new TestUnsupportedResolution();
    pt.printWorks(args);
}

public void printWorks(String[] args)
{
    PrinterJob job=PrinterJob.getPrinterJob();
    job.setPrintable(this);
    PrintRequestAttributeSet settings=new HashPrintRequestAttributeSet();
    PrinterResolution pr = new PrinterResolution(300, 300, ResolutionSyntax.DPI);
    if (args.length > 0 && (args[0].compareTo("600") == 0)) {
        pr = new PrinterResolution(600, 600, ResolutionSyntax.DPI);
        System.out.println("Adding 600 Dpi attribute");
    } else {
        System.out.println("Adding 300 Dpi attribute");
    }
    PrintService ps = job.getPrintService();
    boolean resolutionSupported = ps.isAttributeValueSupported(pr, null, null);
    System.out.println("Is "+pr+" supported by "+ps+"?    "+resolutionSupported);
    if (resolutionSupported) {
        System.out.println("Resolution is supported.\nTest is not applicable, PASSED");
    }
    settings.add(pr);
    if (args.length > 0 && (args[0].equalsIgnoreCase("fidelity"))) {
        settings.add(Fidelity.FIDELITY_TRUE);
        System.out.println("Adding Fidelity.FIDELITY_TRUE attribute");
   }

   if (job.printDialog(settings))
   {
        try {
            job.print(settings);
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }
}

public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException
{
    if (pageIndex>0)
    {
        return NO_SUCH_PAGE;
    }

    StringBuffer s=new StringBuffer();
    for (int i=0;i<10;i++)
    {
        s.append("1234567890ABCDEFGHIJ");
    }

    int x=(int) pageFormat.getImageableX();
    int y=(int) (pageFormat.getImageableY()+50);
    graphics.drawString(s.toString(), x, y);

    return PAGE_EXISTS;
}
}
