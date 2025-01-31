




import java.awt.*;
import java.awt.event.*;

public class bug8077409Test extends Frame {
  ScrollPane pane;
  MyCanvas myCanvas;

  class MyCanvas extends Canvas {
    public Dimension getPreferredSize() {
      return new Dimension(400, 800);
    }

    public void paint(Graphics g) {
      g.setColor(Color.BLACK);
      g.drawLine(0, 0, 399, 0);
      g.setColor(Color.RED);
      g.drawLine(0, 1, 399, 1);
      g.setColor(Color.BLUE);
      g.drawLine(0, 2, 399, 2);
      g.setColor(Color.GREEN);
      g.drawLine(0, 3, 399, 3);
    }

  }

  public bug8077409Test() {
    super();
    setLayout(new BorderLayout());
    pane = new ScrollPane();

    myCanvas = new MyCanvas();
    pane.add(myCanvas);

    add(pane, BorderLayout.CENTER);
    setSize(320, 480);

  }

  @Override
  protected void processKeyEvent(KeyEvent e) {
    super.processKeyEvent(e);

  }

  public static void main(String[] args) throws AWTException, InterruptedException {
    final bug8077409Test obj = new bug8077409Test();
    obj.setVisible(true);
    Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
      @Override
      public void eventDispatched(AWTEvent e) {
        KeyEvent keyEvent = (KeyEvent) e;
        if(keyEvent.getID() == KeyEvent.KEY_RELEASED) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_1) {
              System.out.println(obj.pane.toString());
              System.out.println("obj.myCanvas.pos: " + obj.myCanvas.getBounds());
              System.out.println(obj.myCanvas.toString());
            }  else if (keyEvent.getKeyCode() == KeyEvent.VK_2) {
              obj.repaint();
           } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
              Point scrollPosition = obj.pane.getScrollPosition();
              scrollPosition.translate(0, 1);
              obj.pane.setScrollPosition(scrollPosition);
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
              Point scrollPosition = obj.pane.getScrollPosition();
              scrollPosition.translate(0, -1);
              obj.pane.setScrollPosition(scrollPosition);
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
              obj.pane.validate();
            }
          }
        }
    }, AWTEvent.KEY_EVENT_MASK);
      Point scrollPosition = obj.pane.getScrollPosition();
      scrollPosition.translate(0, 1);
      obj.pane.setScrollPosition(scrollPosition);

      int y = obj.pane.getComponent(0).getLocation().y;
      obj.pane.validate();
      if(y != obj.pane.getComponent(0).getLocation().y){
          throw new RuntimeException("Wrong position of component in ScrollPane");
      }
  }

}
