


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class PrintManualTest_FitWidthMultiple extends JTable implements Runnable {

    static boolean testPassed;
    static JFrame fr = null;
    static JFrame instructFrame = null;
    private final CountDownLatch latch;

    public PrintManualTest_FitWidthMultiple(CountDownLatch latch){
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            createUIandTest();
        } catch (Exception ex) {
            dispose();
            latch.countDown();
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void createUIandTest() throws Exception {
        
        final MessageFormat header=new MessageFormat("JTable Printing Header {0}");
        final MessageFormat footer = new MessageFormat("JTable Printing Footer {0}");

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                
                String info =
                    " \nThis test case brings up JTable with more Columns and Rows \n"+
                    "Press the Print Button. It Prints in PRINT_MODE_FIT_WIDTH \n" +
                    "It Pops up the Print Dialog. Check if Job/Print Attributes in the\n" +
                    "Print Dialog are configurable. Default Print out will be in Landscape \n"+
                    "The Print out should have JTable Centered on the Print out with thin borders \n"+
                    "Prints out with Header and Footer. \n"+
                    "The JTable should have all columns printed within border";

                instructFrame=new JFrame("PrintManualTest_NormalSingle");
                JPanel panel=new JPanel(new BorderLayout());
                JButton button1 = new JButton("Pass");
                JButton button2 = new JButton("Fail");
                button1.addActionListener((e) -> {
                    testPassed = true;
                    dispose();
                    latch.countDown();
                });
                button2.addActionListener((e) -> {
                    testPassed = false;
                    dispose();
                    latch.countDown();
                });
                JPanel btnpanel1 = new JPanel();
                btnpanel1.add(button1);
                btnpanel1.add(button2);
                panel.add(addInfo(info),BorderLayout.CENTER);
                panel.add(btnpanel1, BorderLayout.SOUTH);
                instructFrame.getContentPane().add(panel);
                instructFrame.setBounds(600,100,350,350);

                
                final JButton printButton=new JButton("Print");

                
                final TableModel datamodel=new AbstractTableModel(){
                    @Override
                    public int getColumnCount() { return 50;}
                    @Override
                    public int getRowCount() { return 50; }
                    @Override
                    public Object getValueAt(int row, int column){ return new Integer(row*column);}
                };

                
                final JTable table=new JTable(datamodel);

                
                JScrollPane scrollpane=new JScrollPane(table);
                fr = new JFrame("PrintManualTest_FitWidthMultiple");
                fr.getContentPane().add(scrollpane);

                
                JPanel btnpanel=new JPanel();
                btnpanel.add(printButton);
                fr.getContentPane().add(btnpanel,BorderLayout.SOUTH);
                fr.setBounds(0,0,400,400);
                fr.setSize(500,500);

                
                fr.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl P"), "printButton");
                fr.getRootPane().getActionMap().put("printButton", new AbstractAction(){
                    @Override
                    public void actionPerformed(ActionEvent e){
                        printButton.doClick();
                    }
                });

                
                fr.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        dispose();
                        if (testPassed == false) {
                            throw new RuntimeException(" User has not executed the test");
                        }
                    }
                });

                final PrintRequestAttributeSet prattr=new HashPrintRequestAttributeSet();
                prattr.add(javax.print.attribute.standard.OrientationRequested.LANDSCAPE);

                printButton.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae){
                        try{
                            table.print(JTable.PrintMode.FIT_WIDTH, header,footer,true,prattr,true);
                        } catch(Exception e){}
                    }
                });
                instructFrame.setVisible(true);
                fr.setVisible(true);
            }
        });
    }

    public void dispose() {
        instructFrame.dispose();
        fr.dispose();
    }

    public JScrollPane addInfo(String info) {
        JTextArea jta = new JTextArea(info,8,20);
        jta.setEditable(false);
        jta.setLineWrap(true);
        JScrollPane sp = new JScrollPane(jta);
        return sp;

    }

    

    public static void main(String[] argv) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        PrintManualTest_FitWidthMultiple test = new PrintManualTest_FitWidthMultiple(latch);
        Thread T1 = new Thread(test);
        T1.start();

        
        boolean ret = false;
        try {
            ret = latch.await(60, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            throw ie;
        }
        if (!ret) {
            test.dispose();
            throw new RuntimeException(" User has not executed the test");
        }
        if (test.testPassed == false) {
            throw new RuntimeException("printed contents is beyond borders");
        }
    }
}
