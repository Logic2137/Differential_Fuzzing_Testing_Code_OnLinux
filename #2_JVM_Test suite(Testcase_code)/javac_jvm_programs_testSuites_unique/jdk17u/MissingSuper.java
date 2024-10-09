public class MissingSuper {

    public static void main(String[] args) {
        try {
            new MissingSuperSub();
        } catch (NoClassDefFoundError e) {
            System.out.println("Expected NoClassDefFoundError:");
            e.printStackTrace(System.out);
        }
        try {
            new MissingSuperImpl();
        } catch (NoClassDefFoundError e) {
            System.out.println("Expected NoClassDefFoundError:");
            e.printStackTrace(System.out);
        }
    }
}

class MissingSuperSup {
}

class MissingSuperSub extends MissingSuperSup {
}

interface MissingSuperIntf {
}

class MissingSuperImpl implements MissingSuperIntf {
}
