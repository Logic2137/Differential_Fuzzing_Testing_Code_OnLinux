



import java.text.*;
import java.util.*;
import java.io.*;

public class Bug4932583 {
    public static void main(String[] args) {
        BreakIterator iterator = BreakIterator.getCharacterInstance();
        iterator.setText("\uDB40\uDFFF");
        int boundary = iterator.next();
    }
}
