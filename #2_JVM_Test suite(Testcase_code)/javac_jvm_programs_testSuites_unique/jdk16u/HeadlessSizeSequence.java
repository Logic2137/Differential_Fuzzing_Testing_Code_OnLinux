

import javax.swing.*;



public class HeadlessSizeSequence {
    public static void main(String args[]) {
        SizeSequence ss;
        ss = new SizeSequence();
        ss = new SizeSequence(10);
        ss = new SizeSequence(10, 10);
        ss = new SizeSequence(new int[]{1, 2, 3, 4, 5, 6, 76, 9});
    }
}
