


public class StringIndexOfChar {
    public static void main(String[] args) throws Exception {
        String emptyString = "";
        for (int i = 0; i < 100; i++) {
            for(int c = 0; c < 0xFFFF; c++) {
                int result = emptyString.indexOf((char)c, -1);
                if (result != -1) {
                    throw new Exception("new String(\"\").indexOf(char, -1) must be -1, but got " + result);
                }
            }
        }
    }
}
