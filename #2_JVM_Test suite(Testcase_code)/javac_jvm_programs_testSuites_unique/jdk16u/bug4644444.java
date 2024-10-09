

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;



public class bug4644444 extends JApplet {

        JPanel panel;
        JButton button;

        public bug4644444() throws Exception {
            java.awt.EventQueue.invokeLater( () -> {
                panel = new JPanel();
                button = new JButton("whooo");
                button.setToolTipText("Somthing really long 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890");
                panel.add(button);
                getContentPane().add(panel);
            });
    }

    public void init() {
                String[][] instructionsSet =
                {
                        {
                                " Note : Incase of Assertion failure,user can enter",
                                " remarks by pressing 'Assertion Fail Remarks ' button",
                                " ",
                                " You would see a testframe with a Button",
                        " ",
                                " ON ALL PLATFORMS",
                        "1. Move the mouse on the button, ",
                        "   so that the tooltip attached to it comes up ",
                        "2. Tool tip should get adjusted it-self to show ",
                        "       its full length of text. ",
                        "3. If tooltip  text gets cut, ",
                        "   press 'Assertion Fail' else press 'Assertion Pass'",
                        "4. Similarly, move the applet to different locations of the screen, ",
                        "   & see if tooltip works properly everywhere. "
                        }
                };

                String[] exceptionsSet =
                {
                        "JToolTip is shown partially when placed very close to screen boundaries",
                };

                Sysout.setInstructionsWithExceptions(instructionsSet,exceptionsSet);

        }

        public void start (){}

        public void destroy(){
            if(Sysout.failStatus())    {
                String failMsg = Sysout.getFailureMessages();
                failMsg = failMsg.replace('\n',' ');
                throw new RuntimeException(failMsg);
            }
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

   public static void setInstructionsWithExceptions(String instructionsSet[][],
                                                    String exceptionsSet[]) {
       createDialogWithInstructions(instructionsSet[0]);
       dialog.setInstructions(instructionsSet);
       dialog.setExceptionMessages(exceptionsSet);
   }

   public static String getFailureMessages()   {
       return dialog.failureMessages;
   }

   public static boolean failStatus()  {
       return dialog.failStatus;
   }

 }


class TestDialog extends Dialog
 {

   TextArea instructionsText;
   TextArea messageText;
   int maxStringLength = 70;

   Panel assertPanel;
   Button assertPass,assertFail,remarks;
   HandleAssert handleAssert;
   boolean failStatus=false;
   int instructionCounter=0;
   String instructions[][];
   int exceptionCounter=0;
   String exceptionMessages[];
   String failureMessages="<br>";
   String remarksMessage=null;
   RemarksDialog remarksDialog;

   
   public TestDialog( Frame frame, String name )
    {
      super( frame, name );
      int scrollBoth = TextArea.SCROLLBARS_BOTH;
      instructionsText = new TextArea( "", 14, maxStringLength, scrollBoth );
      add( "North", instructionsText );

      messageText = new TextArea( "", 3, maxStringLength, scrollBoth );
      add("Center", messageText);

      assertPanel = new Panel(new FlowLayout());
      assertPass=new Button("Assertion Pass");
      assertPass.setName("Assertion Pass");
      assertFail=new Button("Assertion Fail");
      assertFail.setName("Assertion Fail");
      remarks = new Button("Assertion Fail Remarks");
      remarks.setEnabled(false);
      remarks.setName("Assertion Remarks");
      assertPanel.add(assertPass);
      assertPanel.add(assertFail);
      assertPanel.add(remarks);
      handleAssert = new HandleAssert();
      assertPass.addActionListener(handleAssert);
      assertFail.addActionListener(handleAssert);
      remarks.addActionListener(handleAssert);
      add("South",assertPanel);
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

   public void emptyMessage()   {
       messageText.setText("");
   }

   public void setInstructions(String insStr[][])    {
       instructions=insStr;
   }

   public void setExceptionMessages(String exceptionMessages[])   {
       this.exceptionMessages=exceptionMessages;
   }

   class HandleAssert implements ActionListener   {
        public void actionPerformed(ActionEvent ae)    {
           if(ae.getSource()==remarks)  {
               remarksDialog = new RemarksDialog(TestDialog.this,
                                        "Assertion Remarks Dialog",true);
               remarks.setEnabled(false);
               if(remarksMessage!=null)
                    failureMessages+=". User Remarks : "+remarksMessage;
           }
           else {
               if(instructionCounter<instructions.length-1) {
                   emptyMessage();
                   instructionCounter++;
                   printInstructions(instructions[instructionCounter]);
               }
               else {
                   emptyMessage();
                   displayMessage("Testcase Completed");
                   displayMessage("Press 'Done' button in the "+
                                                    "BaseApplet to close");
                   assertPass.setEnabled(false);
                   assertFail.setEnabled(false);
               }

               if(ae.getSource()==assertPass)    {
                   
               }
               else if(ae.getSource()==assertFail)   {
                   remarks.setEnabled(true);
                   if(!failStatus)
                       failStatus=true;
                   if(exceptionCounter<exceptionMessages.length)   {
                        failureMessages = failureMessages + "<br>"+
                                    exceptionMessages[exceptionCounter];
                   }
               }
               exceptionCounter++;
           }
        }
    }

    class RemarksDialog extends Dialog  implements ActionListener{
        Panel rootPanel,remarksPanel;
        TextArea textarea;
        Button addRemarks,cancelRemarks;
        public RemarksDialog(Dialog owner,String title,boolean modal)  {
            super(owner,title,modal);
            rootPanel = new Panel(new BorderLayout());
            remarksPanel = new Panel(new FlowLayout());
            textarea = new TextArea(5,30);
            addRemarks=new Button("Add Remarks");
            addRemarks.addActionListener(this);
            cancelRemarks = new Button("Cancel Remarks");
            cancelRemarks.addActionListener(this);
            remarksPanel.add(addRemarks);
            remarksPanel.add(cancelRemarks);
            rootPanel.add(textarea,"Center");
            rootPanel.add(remarksPanel,"South");
            add(rootPanel);
            setBounds(150,150,400,200);
            setVisible(true);
        }

        public void actionPerformed(ActionEvent ae) {
            remarksMessage=null;
            if(ae.getSource()==addRemarks)  {
                String msg = textarea.getText().trim();
                if (msg.length()>0)
                    remarksMessage=msg;
            }
            dispose();
        }

    }

}
