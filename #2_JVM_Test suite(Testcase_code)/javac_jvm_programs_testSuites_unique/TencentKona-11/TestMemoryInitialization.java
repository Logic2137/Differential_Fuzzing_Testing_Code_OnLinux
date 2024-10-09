




public class TestMemoryInitialization {
    final static int LOOP_LENGTH = 10;
    final static int CHUNK_SIZE = 1500000;

    public static byte[] buffer;

    public static void main(String args[]) {

        for (int i = 0; i < LOOP_LENGTH; i++) {
            for (int j = 0; j < LOOP_LENGTH; j++) {
                buffer = new byte[CHUNK_SIZE];
                buffer = null;
            }
        }
    }
}
