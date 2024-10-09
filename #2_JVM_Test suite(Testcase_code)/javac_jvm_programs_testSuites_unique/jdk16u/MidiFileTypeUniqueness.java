

import javax.sound.midi.MidiSystem;


public class MidiFileTypeUniqueness {

    public static void main(String[] args) throws Exception {
        boolean foundDuplicates = false;
        int[]   aTypes = MidiSystem.getMidiFileTypes();
        for (int i = 0; i < aTypes.length; i++)
        {
            for (int j = 0; j < aTypes.length; j++)
            {
                if (aTypes[i] == aTypes[j] && i != j) {
                    foundDuplicates = true;
                }
            }
        }
        if (foundDuplicates) {
            throw new Exception("Test failed");
        } else {
            System.out.println("Test passed");
        }
    }
}
