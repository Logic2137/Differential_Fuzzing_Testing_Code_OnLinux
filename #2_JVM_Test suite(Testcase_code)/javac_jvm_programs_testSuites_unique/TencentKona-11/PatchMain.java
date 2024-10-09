



public class PatchMain {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            Class.forName(args[i]);
        }
    }
}
