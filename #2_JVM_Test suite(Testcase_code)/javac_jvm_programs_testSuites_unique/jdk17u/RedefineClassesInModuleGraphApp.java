public class RedefineClassesInModuleGraphApp {

    public static void main(String[] args) {
        Module m = Object.class.getModule();
        ModuleLayer ml = m.getLayer();
        System.out.println(m);
        System.out.println(ml);
    }
}
