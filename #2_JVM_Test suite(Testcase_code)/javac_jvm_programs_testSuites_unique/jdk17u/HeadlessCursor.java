import java.awt.*;

public class HeadlessCursor {

    public static void main(String[] args) {
        Cursor c;
        c = new Cursor(Cursor.CROSSHAIR_CURSOR);
        c.getType();
        c.getName();
        c = new Cursor(Cursor.DEFAULT_CURSOR);
        c.getType();
        c.getName();
        c = new Cursor(Cursor.E_RESIZE_CURSOR);
        c.getType();
        c.getName();
        c = new Cursor(Cursor.HAND_CURSOR);
        c.getType();
        c.getName();
        c = new Cursor(Cursor.N_RESIZE_CURSOR);
        c.getType();
        c.getName();
        c = new Cursor(Cursor.NE_RESIZE_CURSOR);
        c.getType();
        c.getName();
        c = new Cursor(Cursor.NW_RESIZE_CURSOR);
        c.getType();
        c.getName();
        c = new Cursor(Cursor.S_RESIZE_CURSOR);
        c.getType();
        c.getName();
        c = new Cursor(Cursor.SE_RESIZE_CURSOR);
        c.getType();
        c.getName();
        c = new Cursor(Cursor.SW_RESIZE_CURSOR);
        c.getType();
        c.getName();
        c = new Cursor(Cursor.TEXT_CURSOR);
        c.getType();
        c.getName();
        c = new Cursor(Cursor.W_RESIZE_CURSOR);
        c.getType();
        c.getName();
        c = new Cursor(Cursor.WAIT_CURSOR);
        c.getType();
        c.getName();
        c = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
        c = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        c = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
        c = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        c = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
        c = Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
        c = Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
        c = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
        c = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
        c = Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
        c = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
        c = Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
        c = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        c = Cursor.getDefaultCursor();
        c.getType();
        c.getName();
    }
}
