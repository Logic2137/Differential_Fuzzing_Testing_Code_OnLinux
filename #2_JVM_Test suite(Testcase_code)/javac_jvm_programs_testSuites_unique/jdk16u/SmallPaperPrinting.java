

   import java.awt.*;
   import java.awt.print.*;

   public class SmallPaperPrinting
   {
      public static void main(String args[])
      {
        System.out.println("----------------- Instructions --------------------");
        System.out.println("Arguments: (none)  - paper width=1,     height=.0001");
        System.out.println("              1    - paper width=.0001, height=1");
        System.out.println("              2    - paper width=-1,    height=1");
        System.out.println("A passing test should catch a PrinterException");
        System.out.println("and should display \"Print error: (exception msg)\".");
        System.out.println("---------------------------------------------------\n");
         PrinterJob job = PrinterJob.getPrinterJob();
         PageFormat format = job.defaultPage();
         Paper paper = format.getPaper();

         double w = 1, h = .0001;  
         if(args.length > 0 && args[0].equals("1")) {
            w = .0001;  h = 1; }  
         else if(args.length > 0 && args[0].equals("2")) {
            w = -1;  h = 1; }  
         paper.setSize(w, h);
         paper.setImageableArea(0, 0, w, h);
         format.setPaper(paper);
         job.setPrintable(
               new Printable() {
                  public int print(Graphics g, PageFormat page_format, int page) {
                     return NO_SUCH_PAGE;
                  }
               }, format);

         try {
            job.print(); }
            catch(PrinterException e) {
               System.err.println("Print error:\n" + e.getMessage()); 
            }
      }
   }
