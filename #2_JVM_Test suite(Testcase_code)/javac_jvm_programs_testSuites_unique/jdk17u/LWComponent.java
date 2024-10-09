
package test.java.awt.event.helpers.lwcomponents;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

public abstract class LWComponent extends Component {

    protected static Color ncBackgroundColor;

    protected static Color focusColor;

    protected static Color focusWrongColor;

    protected static Color mouseOverColor;

    static {
        ncBackgroundColor = Color.white;
        focusColor = Color.black;
        focusWrongColor = Color.magenta;
        mouseOverColor = Color.blue;
    }

    protected boolean _shouldHaveFocus = false;

    protected boolean _shouldBeShowing = false;

    protected boolean mouseB1Pressed = false;

    protected boolean mouseB2Pressed = false;

    protected boolean mouseB3Pressed = false;

    protected boolean mouseInside = false;

    protected static boolean tracingOn = false;

    protected static PrintStream traceOutput = null;

    public LWComponent() {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK | AWTEvent.COMPONENT_EVENT_MASK);
    }

    public static void errorMsg(String msg) {
        System.err.println("ERROR: " + msg);
    }

    public static void traceMsg(String msg) {
        if (LWComponent.tracingOn) {
            LWComponent.traceOutput.println(msg);
        }
    }

    static boolean bIgnFocus = false;

    static {
        String ignFocus = System.getProperty("javasoft.awtsqe.lw.IGNORE_FOCUS_KVETCH", "false");
        bIgnFocus = ignFocus.trim().toLowerCase().equals("true");
    }

    public String kvetch() {
        String ret = this.toString();
        boolean errors = false;
        if (!bIgnFocus) {
            if (hasFocus()) {
                if (!shouldHaveFocus()) {
                    ret += "\nERROR: hasFocus indicates we have Focus, when we shouldn't.";
                    errors = true;
                }
            } else {
                if (shouldHaveFocus()) {
                    ret += "\nERROR: (see bug#4233658) hasFocus does not indicate we have Focus, when we should.";
                    errors = true;
                }
            }
        }
        if (errors) {
            return ret;
        } else {
            return null;
        }
    }

    public void kvetch(PrintStream out) {
        if (out != null) {
            String s = kvetch();
            if (s != null) {
                LWComponent.errorMsg(s);
            }
        }
    }

    public static void startTracing(PrintStream out) {
        tracingOn = true;
        traceOutput = out;
    }

    public static void stopTracing() {
        tracingOn = false;
        traceOutput = null;
    }

    public boolean shouldHaveFocus() {
        return _shouldHaveFocus;
    }

    public boolean shouldBeShowing() {
        return _shouldBeShowing;
    }

    @Override
    protected void processFocusEvent(FocusEvent e) {
        super.processFocusEvent(e);
        LWComponent.traceMsg("processFocusEvent " + e.toString());
        switch(e.getID()) {
            case FocusEvent.FOCUS_GAINED:
                _shouldHaveFocus = true;
                repaint();
                break;
            case FocusEvent.FOCUS_LOST:
                _shouldHaveFocus = false;
                repaint();
                break;
        }
    }

    @Override
    protected void processComponentEvent(ComponentEvent e) {
        super.processComponentEvent(e);
        LWComponent.traceMsg("processComponentEvent " + e.toString());
        switch(e.getID()) {
            case ComponentEvent.COMPONENT_MOVED:
                break;
            case ComponentEvent.COMPONENT_RESIZED:
                break;
            case ComponentEvent.COMPONENT_SHOWN:
                _shouldBeShowing = true;
                break;
            case ComponentEvent.COMPONENT_HIDDEN:
                _shouldBeShowing = false;
                break;
        }
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        int mod = e.getModifiers();
        super.processMouseEvent(e);
        LWComponent.traceMsg("processMouseEvent " + e.toString());
        switch(e.getID()) {
            case MouseEvent.MOUSE_PRESSED:
                if ((mod & MouseEvent.BUTTON1_MASK) != 0) {
                    if (mouseB1Pressed) {
                        errorMsg("ERROR: MOUSE_PRESSED for B1 when already pressed, on " + this.toString());
                    }
                    mouseB1Pressed = true;
                    break;
                }
                if ((mod & MouseEvent.BUTTON2_MASK) != 0) {
                    if (mouseB2Pressed) {
                        errorMsg("ERROR: MOUSE_PRESSED for B2 when already pressed, on " + this.toString());
                    }
                    mouseB2Pressed = true;
                    break;
                }
                if ((mod & MouseEvent.BUTTON3_MASK) != 0) {
                    if (mouseB3Pressed) {
                        errorMsg("ERROR: MOUSE_PRESSED for B3 when already pressed, on " + this.toString());
                    }
                    mouseB3Pressed = true;
                    break;
                }
                repaint();
                break;
            case MouseEvent.MOUSE_RELEASED:
                if ((mod & MouseEvent.BUTTON1_MASK) != 0) {
                    if (!mouseB1Pressed) {
                        errorMsg("ERROR: MOUSE_RELEASED for B1 when not pressed, on " + this.toString());
                    }
                    mouseB1Pressed = false;
                    break;
                }
                if ((mod & MouseEvent.BUTTON2_MASK) != 0) {
                    if (!mouseB2Pressed) {
                        errorMsg("ERROR: MOUSE_RELEASED for B2 when not pressed, on " + this.toString());
                    }
                    mouseB2Pressed = false;
                    break;
                }
                if ((mod & MouseEvent.BUTTON3_MASK) != 0) {
                    if (!mouseB3Pressed) {
                        errorMsg("ERROR: MOUSE_RELEASED for B3 when not pressed, on " + this.toString());
                    }
                    mouseB3Pressed = false;
                    break;
                }
                repaint();
                break;
            case MouseEvent.MOUSE_CLICKED:
                break;
            case MouseEvent.MOUSE_ENTERED:
                if (mouseInside) {
                    errorMsg("ERROR: MOUSE_ENTERED when mouse already inside component, on " + this.toString());
                }
                mouseInside = true;
                repaint();
                break;
            case MouseEvent.MOUSE_EXITED:
                if (!mouseInside) {
                    errorMsg("ERROR: MOUSE_EXITED when mouse not inside component, on " + this.toString());
                }
                mouseInside = false;
                repaint();
                break;
            case MouseEvent.MOUSE_MOVED:
                break;
            case MouseEvent.MOUSE_DRAGGED:
                break;
        }
    }

    public Point getClientLocation() {
        return new Point(5, 5);
    }

    public Dimension getClientSize() {
        Dimension dim = getSize();
        dim.width -= 10;
        dim.height -= 10;
        return dim;
    }

    public Rectangle getClientBounds() {
        Dimension dim = getClientSize();
        return new Rectangle(5, 5, dim.width, dim.height);
    }

    public int getClientX() {
        return 5;
    }

    public int getClientY() {
        return 5;
    }

    public void setNonClientColor(Color c) {
        LWComponent.ncBackgroundColor = c;
    }

    @Override
    public void paint(Graphics g) {
        Dimension dim = getSize();
        kvetch(System.err);
        Color saveColor = g.getColor();
        super.paint(g);
        g.setColor(getBackground());
        g.fillRect(0, 0, dim.width, dim.height);
        g.setColor(ncBackgroundColor);
        g.fillRect(0, 0, dim.width, 5);
        g.fillRect(0, 5, 5, dim.height - 10);
        g.fillRect(dim.width - 5, 5, 5, dim.height - 10);
        g.fillRect(0, dim.height - 5, dim.width, 5);
        if (shouldHaveFocus() || hasFocus()) {
            g.setColor(shouldHaveFocus() && hasFocus() ? focusColor : focusWrongColor);
            g.drawRect(1, 1, dim.width - 3, dim.height - 3);
        }
        if (mouseInside) {
            g.setColor(mouseOverColor);
            g.drawRect(3, 3, dim.width - 7, dim.height - 7);
        }
        if (!isEnabled()) {
            g.setColor(getBackground());
            Dimension size = getSize();
            int borderThickness = 0;
            int startX = borderThickness;
            int startY = borderThickness;
            int endX = startX + size.width - 2 * borderThickness - 2;
            int endY = startY + size.height - 2 * borderThickness - 2;
            int x, y;
            for (y = startY; y <= endY; y += 1) {
                for (x = startX + (y % 2); x <= endX; x += 2) {
                    g.fillRect(x, y, 1, 1);
                }
            }
        }
        g.setColor(saveColor);
    }

    public void restrictGraphicsToClientArea(Graphics g) {
        Dimension dim = getSize();
        g.translate(5, 5);
        g.setClip(0, 0, dim.width - 10, dim.height - 10);
    }

    public void unrestrictGraphicsFromClientArea(Graphics g) {
        g.translate(-5, -5);
        Dimension dim = getSize();
        g.setClip(0, 0, dim.width, dim.height);
    }
}
