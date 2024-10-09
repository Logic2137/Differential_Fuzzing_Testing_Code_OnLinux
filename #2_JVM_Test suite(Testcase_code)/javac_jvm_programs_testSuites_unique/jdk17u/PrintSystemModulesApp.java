public class PrintSystemModulesApp {

    public static void main(String[] args) throws Exception {
        String modules = ModuleLayer.boot().toString();
        System.out.println(modules + ", ");
    }
}
