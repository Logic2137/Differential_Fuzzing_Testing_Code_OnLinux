



public class TestStackOverflow {
    final static int LOOP_LENGTH = 1000000;
    final static int LOGGING_STEP = 10000;

    public static void main(String args[]) {
        Object object = null;

        for (int i = 0; i < LOOP_LENGTH; i++) {

            
            if (i % LOGGING_STEP == 0) {
                System.out.println(i);
            }
            try {
                Object array[] = {object, object, object, object, object};
                object = array;
            } catch (OutOfMemoryError e) {
                object = null;
                System.out.println("Caught OutOfMemoryError.");
                return;
            } catch (StackOverflowError e) {
                object = null;
                System.out.println("Caught StackOverflowError.");
                return;
            }
        }
    }
}

