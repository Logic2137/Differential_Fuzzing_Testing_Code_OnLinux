


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.event.*;

public class Example
{
    public static void main(String [] args)
    {
        if(args.length != 1)
        {
            System.err.println("Usage: Example num_sections");
            return;
        }

        try{
            int stream_sections      = Integer.parseInt(args[0]);
            DocFlavor flavor         = DocFlavor.INPUT_STREAM.AUTOSENSE;
            PrintService [] services = PrintServiceLookup.lookupPrintServices
(flavor, null);

            if(services.length > 0)
            {
                PrintRequestAttributeSet attbs = new
HashPrintRequestAttributeSet();
                PrintService service = ServiceUI.printDialog(null, 100, 100,
services, null, flavor, attbs);

                if(service != null)
                {
                    InputStream stream = createInputStream(stream_sections);
                    Doc doc            = new SimpleDoc(stream, flavor, null);
                    DocPrintJob job    = service.createPrintJob();
                    job.addPrintJobListener(new PrintJobListener(){
                        public void printJobCanceled(PrintJobEvent e)
                        {
                            finish("Canceled");
                        }

                        public void printJobCompleted(PrintJobEvent e)
                        {
                            finish("Complete");
                        }

                        public void printJobFailed(PrintJobEvent e)
                        {
                            finish("Failed");
                        }

                        public void printDataTransferCompleted(PrintJobEvent
pje)
                        {
                            System.out.println("data transfered");
                        }

                        public void printJobNoMoreEvents(PrintJobEvent pje)
                        {
                            finish("Complete");
                        }
                        public void printJobRequiresAttention(PrintJobEvent pje)
{}

                    });
                    System.out.println("Printing...");
                    job.print(doc, attbs);
                }

            }else
            {
                System.out.println("no printers found");
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void finish(String message)
    {
        System.out.println("Printing " + message);
        System.out.flush();
    }

    private static InputStream createInputStream(int num_sections)
    {
        byte [] bytes = "Returns the number of bytes that can be read (or skipped over)\nfrom this input stream without blocking by the next caller of\na method for this input stream. The next caller might be the same thread or\nanother thread. ".getBytes();

        return new TestInputStream(bytes, num_sections);
    }

    private static class TestInputStream extends ByteArrayInputStream
    {
        public TestInputStream(byte [] bytes, int sections)
        {
            super(bytes);
            int avail  = super.available();
            block_size = avail / sections;
        }

        public int available()
        {
            int true_avail = super.available();
            return true_avail == 0 ? 0 : block_size;
        }

        private int block_size;
    }
}
