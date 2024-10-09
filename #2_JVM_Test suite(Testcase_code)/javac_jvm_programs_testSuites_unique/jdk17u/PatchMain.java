public class PatchMain {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("cdsutils.DynamicDumpHelper")) {
                break;
            }
            Class.forName(args[i]);
        }
    }
}
