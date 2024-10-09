





import java.awt.event.*;
import java.awt.*;
import java.lang.*;
import java.applet.Applet;


public class FrameStateTest extends Applet implements ActionListener, ItemListener{

   Button btnCreate = new Button("Create Frame");
   Button btnDispose = new Button("Dispose Frame");
   CheckboxGroup cbgState = new CheckboxGroup();
   CheckboxGroup cbgResize = new CheckboxGroup();
   Checkbox cbIconState = new Checkbox("Frame state ICONIFIED",cbgState,false);
   Checkbox cbNormState = new Checkbox("Frame state NORMAL",cbgState,true);
   Checkbox cbNonResize = new Checkbox("Frame Nonresizable",cbgResize,false);
   Checkbox cbResize = new Checkbox("Frame Resizable",cbgResize,true);
   int iState = 0;
   boolean bResize = true;
   CreateFrame icontst;

   public void init() {
      this.setLayout (new BorderLayout ());

      String[] instructions =
       {
        "Steps to try to reproduce this problem:",
        "When this test is run an Applet Viewer window will display. In the",
        "Applet Viewer window select the different options for the Frame (i.e.",
        "{Normal, Non-resizalbe}, {Normal, Resizable}, {Iconified, Resizable},",
        "{Iconified, Non-resizalbe}). After chosing the Frame's state click the",
        "Create Frame button. After the Frame (Frame State Test (Window2)) comes",
        "up make sure the proper behavior occurred (Frame shown in proper state).",
        "Click the Dispose button to close the Frame. Do the above steps for all",
        "the different Frame state combinations available. If you observe the",
        "proper behavior the test has passed, Press the Pass button. Otherwise",
        "the test has failed, Press the Fail button.",
        "Note: In Frame State Test (Window2) you can also chose the different",
        "buttons to see different Frame behavior. An example of a problem that",
        "has been seen, With the Frame nonresizable you can not iconify the Frame."
       };
      Sysout.createDialogWithInstructions( instructions );

      btnDispose.setEnabled(false);
      add(btnCreate, BorderLayout.NORTH);
      add(btnDispose, BorderLayout.SOUTH);

      Panel p = new Panel(new GridLayout(0,1));
      p.add(cbIconState);
      p.add(cbResize);
      add(p, BorderLayout.WEST);

      p = new Panel(new GridLayout(0,1));
      p.add(cbNormState);
      p.add(cbNonResize);
      add(p, BorderLayout.EAST);

      
      btnDispose.addActionListener(this);
      btnCreate.addActionListener(this);
      cbNormState.addItemListener(this);
      cbResize.addItemListener(this);
      cbIconState.addItemListener(this);
      cbNonResize.addItemListener(this);

      resize(600, 200);

   }

   public void actionPerformed(ActionEvent evt) {


        if (evt.getSource() == btnCreate) {
            btnCreate.setEnabled(false);
            btnDispose.setEnabled(true);
            icontst = new CreateFrame(iState, bResize);
            icontst.show();
        } else if (evt.getSource() == btnDispose) {
            btnCreate.setEnabled(true);
            btnDispose.setEnabled(false);
            icontst.dispose();
        }
    }

    public void itemStateChanged(ItemEvent evt) {

        if (cbNormState.getState()) iState = 0;
        if (cbIconState.getState()) iState = 1;
        if (cbResize.getState()) bResize = true;
        if (cbNonResize.getState()) bResize = false;

    }

}


class CreateFrame extends Frame implements ActionListener , WindowListener {

  static int e=0;
  static int u=0;
  static int p=0;
  static int i=0;
  static int v=0;

  Button b1, b2, b3, b4, b5, b6, b7;
  boolean resizable = true;
  boolean iconic = false;
  String name = "Frame State Test";

  CreateFrame (int iFrameState, boolean bFrameResizable) {

    setTitle("Frame State Test (Window 2)");

    if (iFrameState == 1) {
        iconic = true;
    }

    if (!(bFrameResizable)) {
        resizable = false;
    }

    System.out.println("CREATING FRAME - Initially "+
        ((iconic) ? "ICONIFIED" : "NORMAL (NON-ICONIFIED)") + " and " +
        ((resizable) ? "RESIZABLE" : "NON-RESIZABLE") );

    Sysout.println("CREATING FRAME - Initially "+
        ((iconic) ? "ICONIFIED" : "NORMAL (NON-ICONIFIED)") + " and " +
        ((resizable) ? "RESIZABLE" : "NON-RESIZABLE") );

    setLayout(new FlowLayout() );
    b1 = new Button("resizable");
    add(b1);
    b2 = new Button("resize");
    add(b2);
    b3 = new Button("iconify");
    add(b3);
    b4 = new Button("iconify and restore");
    add(b4);
    b5 = new Button("hide and show");
    add(b5);
    b6 = new Button("hide, iconify and show");
    add(b6);
    b7 = new Button("hide, iconify, show, and restore");
    add(b7);
    b1.addActionListener(this);
    b2.addActionListener(this);
    b3.addActionListener(this);
    b4.addActionListener(this);
    b5.addActionListener(this);
    b6.addActionListener(this);
    b7.addActionListener(this);
    addWindowListener(this);

    setBounds(100,2,200, 200);
    setState(iconic ? Frame.ICONIFIED: Frame.NORMAL);
    setResizable(resizable);
    pack();
    setVisible(true);

  }

  public void actionPerformed ( ActionEvent e )
  {
    if ( e.getSource() == b2 ) {
        Rectangle r = this.getBounds();
        r.width += 10;
        System.out.println(" - button pressed - setting bounds on Frame to: "+r);
        setBounds(r);
        validate();
    } else if ( e.getSource() == b1 ) {
        resizable = !resizable;
        System.out.println(" - button pressed - setting Resizable to: "+resizable);
        ((Frame)(b1.getParent())).setResizable(resizable);
    } else if ( e.getSource() == b3 ) {
        System.out.println(" - button pressed - setting Iconic: ");
        dolog();
        ((Frame)(b1.getParent())).setState(Frame.ICONIFIED);
        dolog();
    } else if ( e.getSource() == b4 ) {
        System.out.println(" - button pressed - setting Iconic: ");
        dolog();
        ((Frame)(b1.getParent())).setState(Frame.ICONIFIED);
        dolog();
        try {
                Thread.sleep(1000);
        } catch (Exception ex) {};
        System.out.println(" - now restoring: ");
        ((Frame)(b1.getParent())).setState(Frame.NORMAL);
        dolog();
    } else if ( e.getSource() == b5 ) {
        System.out.println(" - button pressed - hiding : ");
        dolog();
        ((Frame)(b1.getParent())).setVisible(false);
        dolog();
        try {
                Thread.sleep(1000);
        } catch (Exception ex) {};
        System.out.println(" - now reshowing: ");
        ((Frame)(b1.getParent())).setVisible(true);
        dolog();
    } else if ( e.getSource() == b6 ) {
        System.out.println(" - button pressed - hiding : ");
        dolog();
        ((Frame)(b1.getParent())).setVisible(false);
        dolog();
        try {
                Thread.sleep(1000);
        } catch (Exception ex) {};
        System.out.println(" - setting Iconic: ");
        dolog();
        ((Frame)(b1.getParent())).setState(Frame.ICONIFIED);
        try {
                Thread.sleep(1000);
        } catch (Exception ex) {};
        System.out.println(" - now reshowing: ");
        ((Frame)(b1.getParent())).setVisible(true);
        dolog();
    } else if ( e.getSource() == b7 ) {
        System.out.println(" - button pressed - hiding : ");
        dolog();
        ((Frame)(b1.getParent())).setVisible(false);
        dolog();
        try {
                Thread.sleep(1000);
        } catch (Exception ex) {};
        System.out.println(" - setting Iconic: ");
        dolog();
        ((Frame)(b1.getParent())).setState(Frame.ICONIFIED);
        try {
                Thread.sleep(1000);
        } catch (Exception ex) {};
        System.out.println(" - now reshowing: ");
        ((Frame)(b1.getParent())).setVisible(true);
        dolog();
        try {
                Thread.sleep(1000);
        } catch (Exception ex) {};
        System.out.println(" - now restoring: ");
        ((Frame)(b1.getParent())).setState(Frame.NORMAL);
        dolog();
    }
  }

    public void windowActivated(WindowEvent e) {
        System.out.println(name + " Activated");
        dolog();
    }
    public void windowClosed(WindowEvent e) {
        System.out.println(name + " Closed");
        dolog();
    }
    public void windowClosing(WindowEvent e) {
        ((Window)(e.getSource())).dispose();
        System.out.println(name + " Closing");
        dolog();
    }
    public void windowDeactivated(WindowEvent e) {
        System.out.println(name + " Deactivated");
        dolog();
    }
    public void windowDeiconified(WindowEvent e) {
        System.out.println(name + " Deiconified");
        dolog();
    }
    public void windowIconified(WindowEvent e) {
        System.out.println(name + " Iconified");
        dolog();
    }
    public void windowOpened(WindowEvent e) {
        System.out.println(name + " Opened");
        dolog();
    }

    public void dolog() {
        System.out.println(" getState returns: "+getState());
    }
}







class Sysout
 {
   private static TestDialog dialog;

   public static void createDialogWithInstructions( String[] instructions )
    {
      dialog = new TestDialog( new Frame(), "Instructions" );
      dialog.printInstructions( instructions );
      dialog.show();
      println( "Any messages for the tester will display here." );
    }

   public static void createDialog( )
    {
      dialog = new TestDialog( new Frame(), "Instructions" );
      String[] defInstr = { "Instructions will appear here. ", "" } ;
      dialog.printInstructions( defInstr );
      dialog.show();
      println( "Any messages for the tester will display here." );
    }


   public static void printInstructions( String[] instructions )
    {
      dialog.printInstructions( instructions );
    }


   public static void println( String messageIn )
    {
      dialog.displayMessage( messageIn );
    }

 }


class TestDialog extends Dialog
 {

   TextArea instructionsText;
   TextArea messageText;
   int maxStringLength = 80;

   
   public TestDialog( Frame frame, String name )
    {
      super( frame, name );
      int scrollBoth = TextArea.SCROLLBARS_BOTH;
      int scrollNone = TextArea.SCROLLBARS_NONE;
      instructionsText = new TextArea( "", 15, maxStringLength, scrollBoth );
      add( "North", instructionsText );

      messageText = new TextArea( "", 10, maxStringLength, scrollBoth );
      add("South", messageText);

      pack();

      show();
    }

   
   public void printInstructions( String[] instructions )
    {
      
      instructionsText.setText( "" );

      

      String printStr, remainingStr;
      for( int i=0; i < instructions.length; i++ )
       {
         
         remainingStr = instructions[ i ];
         while( remainingStr.length() > 0 )
          {
            
            if( remainingStr.length() >= maxStringLength )
             {
               
               int posOfSpace = remainingStr.
                  lastIndexOf( ' ', maxStringLength - 1 );

               if( posOfSpace <= 0 ) posOfSpace = maxStringLength - 1;

               printStr = remainingStr.substring( 0, posOfSpace + 1 );
               remainingStr = remainingStr.substring( posOfSpace + 1 );
             }
            
            else
             {
               printStr = remainingStr;
               remainingStr = "";
             }

            instructionsText.append( printStr + "\n" );

          }

       }

    }

   
   public void displayMessage( String messageIn )
    {
      messageText.append( messageIn + "\n" );
    }


 }
