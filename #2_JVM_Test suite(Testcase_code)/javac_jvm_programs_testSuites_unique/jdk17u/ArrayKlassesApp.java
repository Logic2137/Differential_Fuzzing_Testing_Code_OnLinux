public class ArrayKlassesApp {

    public static void main(String[] args) {
        ArrayKlassesApp[][] array = new ArrayKlassesApp[1][2];
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 2; j++) {
                array[i][j] = new ArrayKlassesApp();
            }
        }
    }
}
