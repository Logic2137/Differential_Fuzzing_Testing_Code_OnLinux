

public class AttemptOOM {
    private static MyObj[] data;

    public static void main(String[] args) throws Exception {
        System.out.println("Entering AttemptOOM main");

        
        int sizeInMb = Integer.parseInt(args[0]);
        data = new MyObj[sizeInMb*1024];

        System.out.println("data.length = " + data.length);

        for (int i=0; i < data.length; i++) {
            data[i] = new MyObj(1024);
        }

        System.out.println("AttemptOOM allocation successful");
    }

    private static class MyObj {
        private byte[] myData;
        MyObj(int size) {
            myData = new byte[size];
        }
    }
}
