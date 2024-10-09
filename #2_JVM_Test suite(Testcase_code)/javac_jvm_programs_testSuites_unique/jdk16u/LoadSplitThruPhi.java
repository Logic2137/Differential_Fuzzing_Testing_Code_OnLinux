



public class LoadSplitThruPhi {

    public static void getPermutations(byte[] inputArray, byte[][] outputArray) {
        int[] indexes = new int[]{0, 2};

        for (int a = 0; a < inputArray.length; a++) {
            int oneIdx = indexes[0]++;
            for (int b = a + 1; b < inputArray.length; b++) {
                int twoIdx = indexes[1]++;
                outputArray[twoIdx][0] = inputArray[a];
                outputArray[twoIdx][1] = inputArray[b];
            }
        }
    }

    public static void main(String[] args) {

        final byte[] inputArray = new byte[]{0, 1};
        final byte[][] outputArray = new byte[3][2];

        for (int i = 0; i < 1000000; i++) {
            getPermutations(inputArray, outputArray);
        }
    }

}
