public class RecursiveSystemLoader extends ClassLoader {

    public static void main(String[] args) {
        ClassLoader sys = ClassLoader.getSystemClassLoader();
        if (!(sys instanceof RecursiveSystemLoader)) {
            throw new RuntimeException("Unexpected system classloader: " + sys);
        }
    }

    public RecursiveSystemLoader(ClassLoader classLoader) {
        super("RecursiveSystemLoader", classLoader);
        try {
            ClassLoader.getSystemClassLoader();
        } catch (IllegalStateException ise) {
            System.err.println("Caught expected exception:");
            ise.printStackTrace();
            return;
        }
        throw new RuntimeException("Expected IllegalStateException was not thrown.");
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }
}
