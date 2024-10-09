



import java.awt.print.PrinterJob;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class Margins implements Printable {

    public static void main(String args[]) throws Exception {
        Robot robot = new Robot();
        Thread t = new Thread (() -> {
            robot.waitForIdle();
            robot.delay(5000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.waitForIdle();
            robot.delay(5000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.waitForIdle();
            robot.delay(5000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        });

        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pageFormat = job.defaultPage();
        Paper paper = pageFormat.getPaper();
        double wid = paper.getWidth();
        double hgt = paper.getHeight();
        paper.setImageableArea(0, -10, wid, hgt);
        t.start();

        pageFormat = job.pageDialog(pageFormat);
        pageFormat.setPaper(paper);
        job.setPrintable(new Margins(), pageFormat);
        try {
            job.print();
        } catch (PrinterException e) {
        }

        paper.setImageableArea(0, 0, wid, hgt + 72);
        pageFormat = job.pageDialog(pageFormat);
        pageFormat.setPaper(paper);

        job.setPrintable(new Margins(), pageFormat);
        try {
           job.print();
        } catch (PrinterException e) {
        }

        pageFormat = job.defaultPage();
        paper = pageFormat.getPaper();
        wid = paper.getWidth();
        hgt = paper.getHeight();

        paper.setImageableArea(0, -10, -wid, hgt);
        pageFormat = job.pageDialog(pageFormat);
        pageFormat.setPaper(paper);

        job.setPrintable(new Margins(), pageFormat);
        try {
           job.print();
        } catch (PrinterException e) {
        }
    }

   public int print(Graphics g, PageFormat pf, int page)
       throws PrinterException {

       if (page > 0) {
           return NO_SUCH_PAGE;
       }
       int ix = (int)pf.getImageableX();
       int iy = (int)pf.getImageableY();
       int iw = (int)pf.getImageableWidth();
       int ih = (int)pf.getImageableHeight();
       System.out.println("ix="+ix+" iy="+iy+" iw="+iw+" ih="+ih);
       if ((ix < 0) || (iy < 0)) {
           throw new RuntimeException("Imageable x or y is a negative value.");
       }


       Paper paper = pf.getPaper();
       int wid = (int)paper.getWidth();
       int hgt = (int)paper.getHeight();
       System.out.println("wid="+wid+" hgt="+hgt);
       
       if ((wid - iw > 72) || (hgt - ih > 72)) {
           throw new RuntimeException("Imageable width or height is negative value");
       }
       if ((ix+iw > wid) || (iy+ih > hgt)) {
           throw new RuntimeException("Printable width or height "
                   + "exceeds paper width or height.");
       }
       
       
       
       if (ix == 0 && iy == 0 && (ix+iw == wid) && (iy+ih == hgt)) {
           return PAGE_EXISTS;
       }

       Graphics2D g2d = (Graphics2D)g;
       g2d.translate(ix, iy);
       g2d.setColor(Color.black);
       g2d.drawRect(1, 1, iw-2, ih-2);

       return PAGE_EXISTS;
   }
}
