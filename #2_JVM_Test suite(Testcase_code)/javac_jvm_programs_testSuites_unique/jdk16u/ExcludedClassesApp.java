

public class ExcludedClassesApp {
    interface NotLinkedInterface {}
    static class NotLinkedSuper {

    }

    static class NotLinkedChild extends NotLinkedSuper implements NotLinkedInterface {

    }

    public static NotLinkedSuper notUsedMethod() {
        return new NotLinkedChild();
    }

    public static void main(String args[]) {

    }
}
