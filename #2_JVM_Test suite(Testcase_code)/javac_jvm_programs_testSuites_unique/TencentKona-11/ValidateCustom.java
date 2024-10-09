


import java.awt.*;
import java.awt.print.*;
import java.awt.geom.*;
import javax.swing.*;

public class ValidateCustom implements Pageable, Printable{

  private static double PIXELS_PER_INCH = 72.0;
  private static double WIDTH = 17.0; 
  private static double LENGTH = 24.0; 
  private static boolean VALIDATE = true;

  private PrinterJob printerJob;
  private PageFormat pageFormat;

  ValidateCustom(){
    printerJob = PrinterJob.getPrinterJob();
    createPageFormat();
  }

  private void createPageFormat(){
    pageFormat = new PageFormat();
    Paper p = new Paper();
    double width   = WIDTH*PIXELS_PER_INCH;
    double height  = LENGTH*PIXELS_PER_INCH;
    double ix      = PIXELS_PER_INCH;
    double iy      = PIXELS_PER_INCH;
    double iwidth  = width  - 2.0*PIXELS_PER_INCH;
    double iheight = height - 2.0*PIXELS_PER_INCH;
    p.setSize(width, height);
    p.setImageableArea(ix, iy, iwidth, iheight);
    pageFormat.setPaper(p);
  }

  public Printable getPrintable(int index){
    return this;
  }

  public PageFormat getPageFormat(int index){
    return pageFormat;
  }

  public int getNumberOfPages(){
    return 1;
  }

  private void printPaperSize(PageFormat pf){
    Paper p = pf.getPaper();
    System.out.println("paper size = ("+p.getWidth()+", "+p.getHeight()+")");
  }

  public void print(){
    
    {
      try{
        
        if(VALIDATE){
            this.pageFormat = printerJob.validatePage(this.pageFormat);
        }
        printPaperSize(pageFormat);
        
        
      }catch(Exception e){e.printStackTrace();}
    }
  }

  public int print(Graphics g, PageFormat pf, int pageIndex){
    if(pageIndex == 0){
      Graphics2D g2 = (Graphics2D)g;
      Rectangle2D r = new Rectangle2D.Double(PIXELS_PER_INCH, PIXELS_PER_INCH, PIXELS_PER_INCH, PIXELS_PER_INCH);
      g2.setStroke(new BasicStroke(1.0f));
      g2.draw(r);
      return PAGE_EXISTS;
    }else{
      return NO_SUCH_PAGE;
    }
  }

  public static void main(String[] args){
        System.out.println("-----------------instructions--------------------");
        System.out.println("You must have a printer installed in your system \nthat supports custom paper sizes in order to run this test.");
        System.out.println("Passing test will display the correct width & height\nof custom paper in 1/72nds of an inch.\n");
        System.out.println("-------------------------------------------------");
    ValidateCustom pt = new ValidateCustom();
    pt.print();
    try{
      System.in.read();
    }catch(Exception e){}
  }

}
