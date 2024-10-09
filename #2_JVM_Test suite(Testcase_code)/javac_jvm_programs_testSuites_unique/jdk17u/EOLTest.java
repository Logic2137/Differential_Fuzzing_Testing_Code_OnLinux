import java.awt.Frame;
import java.awt.TextField;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class EOLTest {

    private Frame mainFrame;

    private TextField textField;

    private String testStrEOL;

    private boolean isTestFail;

    private int testFailCount;

    StringBuilder testFailMessage;

    private String expectedString = "Row1 Row2 Row3";

    public EOLTest() {
        mainFrame = new Frame();
        mainFrame.setSize(200, 200);
        mainFrame.setVisible(true);
        testFailMessage = new StringBuilder();
        testStrEOL = "Row1" + System.lineSeparator() + "Row2\nRow3";
    }

    private void testConstructor1() {
        textField = new TextField(testStrEOL);
        textField.setSize(200, 100);
        mainFrame.add(textField);
        checkTest();
        mainFrame.remove(textField);
    }

    private void testConstructor2() {
        textField = new TextField(30);
        textField.setSize(200, 100);
        mainFrame.add(textField);
        textField.setText(testStrEOL);
        checkTest();
        mainFrame.remove(textField);
    }

    private void testConstructor3() {
        textField = new TextField(testStrEOL, 30);
        textField.setSize(200, 100);
        mainFrame.add(textField);
        checkTest();
        mainFrame.remove(textField);
    }

    private void testSetText() {
        textField = new TextField();
        textField.setSize(200, 100);
        textField.setText(testStrEOL);
        mainFrame.add(textField);
        checkTest();
        mainFrame.remove(textField);
    }

    private void testDeserialization() {
        TextField textFieldToSerialize = new TextField(testStrEOL);
        textFieldToSerialize.setSize(200, 100);
        mainFrame.add(textFieldToSerialize);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutput outStream = new ObjectOutputStream(baos);
            outStream.writeObject(textFieldToSerialize);
            byte[] streamedBytes = baos.toByteArray();
            int foundLoc = 0;
            for (int i = 0; i < streamedBytes.length; ++i) {
                if (streamedBytes[i] == expectedString.charAt(0)) {
                    foundLoc = i;
                    int j = 1;
                    for (; j < expectedString.length(); ++j) {
                        if (streamedBytes[i + j] != expectedString.charAt(j)) {
                            break;
                        }
                    }
                    if (j == expectedString.length()) {
                        break;
                    }
                }
                foundLoc = -1;
            }
            if (foundLoc == -1) {
                throw new Exception("Could not find text data in serialized " + "object stream.");
            }
            String EOLChar = System.lineSeparator();
            String newExpectedString = "";
            for (int i = foundLoc, j = 0; j < expectedString.length(); ++i, ++j) {
                newExpectedString += (char) (streamedBytes[i]);
                if (streamedBytes[i] == ' ') {
                    int k = 0;
                    for (; k < EOLChar.length(); ++k) {
                        streamedBytes[i + k] = (byte) EOLChar.charAt(k);
                    }
                    i += k - 1;
                    j += k - 1;
                }
            }
            expectedString = newExpectedString;
            ByteArrayInputStream bais = new ByteArrayInputStream(streamedBytes);
            ObjectInput inStream = new ObjectInputStream(bais);
            textField = (TextField) inStream.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
            textField = new TextField();
        }
        checkTest();
        mainFrame.remove(textFieldToSerialize);
    }

    private void checkTest() {
        if (!textField.getText().equals(expectedString)) {
            testFailMessage.append("TestFail line : ");
            testFailMessage.append(Thread.currentThread().getStackTrace()[2].getLineNumber());
            testFailMessage.append(" TextField.getText() : \"");
            testFailMessage.append(textField.getText());
            testFailMessage.append("\" does not match expected string : \"");
            testFailMessage.append(expectedString).append("\"");
            testFailMessage.append(System.getProperty("line.separator"));
            testFailCount++;
            isTestFail = true;
        }
    }

    private void checkFailures() {
        if (isTestFail) {
            testFailMessage.insert(0, "Test Fail count : " + testFailCount + System.getProperty("line.separator"));
            dispose();
            throw new RuntimeException(testFailMessage.toString());
        }
    }

    private void dispose() {
        if (mainFrame != null) {
            mainFrame.dispose();
        }
    }

    public static void main(String[] args) {
        EOLTest testEOL = new EOLTest();
        testEOL.testConstructor1();
        testEOL.testConstructor2();
        testEOL.testConstructor3();
        testEOL.testSetText();
        testEOL.testDeserialization();
        testEOL.checkFailures();
        testEOL.dispose();
    }
}
