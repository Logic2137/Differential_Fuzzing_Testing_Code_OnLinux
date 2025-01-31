


import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;

public class TestHebrewMark {

    public static void main(String args[]) {
       FontRenderContext frc = new FontRenderContext(null,false,false);
       final String fonts[] = { "Arial", "Arial Hebrew", "Arial Unicode" };
       final char ALEF = '\u05D0';  
       final char QAMATS = '\u05B8';  
       final String string1 = "\u05DE\u05B8\u05E9\u05C1\u05B0\u05DB\u05B5\u05E0\u05B4\u05D9\u05D0\u05B7\u05D7\u05B2\u05E8\u05B6\u05D9\u05DA\u05B8\u05E0\u05BC\u05B8\u05E8\u05D5\u05BC\u05E6\u05B8\u05D4\u05D4\u05B1\u05D1\u05B4\u05D9\u05D0\u05B7\u05E0\u05B4\u05D9\u05D4\u05B7\u05DE\u05BC\u05B6\u05DC\u05B6\u05DA\u05B0\u05D7\u05B2\u05D3\u05B8\u05E8\u05B8\u05D9\u05D5\u05E0\u05B8\u05D2\u05B4\u05D9\u05DC\u05B8\u05D4\u05D5\u05B0\u05E0\u05B4\u05E9\u05C2\u05B0\u05DE\u05B0\u05D7\u05B8\u05D4\u0020\u05D1\u05BC\u05B8\u05DA\u05B0\u05E0\u05B7\u05D6\u05B0\u05DB\u05BC\u05B4\u05D9\u05E8\u05B8\u05D4\u05D3\u05B9\u05D3\u05B6\u05D9\u05DA\u05B8\u05DE\u05B4\u05D9\u05BC\u05B7\u05D9\u05B4\u05DF\u05DE\u05B5\u05D9\u05E9\u05C1\u05B8\u05E8\u05B4\u05D9\u05DD\u05D0\u05B2\u05D4\u05B5\u05D1\u05D5\u05BC\u05DA\u05B8";
       final String string2 = string1.replaceAll("\u05B8", ""); 
       int string1len = string1.length();
       int string2len = string2.length();
       System.out.println("String1 has " + string1len+" chars, and string2 (without the QAMATS) has " + string2.length());
       if(string1len == string2len) {
           throw new RuntimeException("Hey, string1 and string2 are both " + string1len + " chars long - shouldn't happen.");
       }
       Font f = null;
       
       for(String fontname : fonts ) {
          System.err.println("trying: " +fontname);
           Font afont = new Font(fontname,Font.PLAIN,18);
           if(!afont.getFontName().equals(fontname)) {
             System.out.println(fontname + ": is actually  " + afont.getFontName() + " - skipping this font.");
             continue;
           }
           if(!afont.canDisplay(ALEF) || !afont.canDisplay(QAMATS)) {
             System.out.println(fontname + ": can't display ALEF or QAMATS - skipping this font");
             continue;
           }
           f = afont;
        System.err.println("Might be OK: " + fontname);
        System.out.println("Using font " + f.getFontName());
        TextLayout tl = new TextLayout(string1, f, frc);
        TextLayout tl2 = new TextLayout(string2, f, frc);
        Rectangle2D tlBounds = tl.getBounds();
        Rectangle2D tlBounds2 = tl2.getBounds();
        System.out.println("tlbounds="+tlBounds);
        System.out.println("tl.getAdvance()="+tl.getAdvance());
        System.out.println("tl2bounds="+tlBounds2);
        System.out.println("tl2.getAdvance()="+tl2.getAdvance());

        if(tl.getAdvance() != tl2.getAdvance()) {
          throw new RuntimeException("Advance of string with and without QAMATS differs: " + tl.getAdvance() + " vs. " + tl2.getAdvance());
        } else {
          System.out.println("6529141 OK, widths are same.");
        }
       }
       
       if(f == null) {
           System.out.println("Could not find a suitable font - skipping this test.");
           return;
       }
   }
}
