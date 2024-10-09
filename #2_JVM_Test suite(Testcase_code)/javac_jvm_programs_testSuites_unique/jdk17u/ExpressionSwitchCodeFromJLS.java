public class ExpressionSwitchCodeFromJLS {

    static void howMany(int k) {
        switch(k) {
            case 1:
                System.out.print("one ");
            case 2:
                System.out.print("too ");
            case 3:
                System.out.println("many");
        }
    }

    static void howManyRule(int k) {
        switch(k) {
            case 1:
                System.out.println("one");
            case 2:
                System.out.println("two");
            case 3:
                System.out.println("many");
        }
    }

    static void howManyGroup(int k) {
        switch(k) {
            case 1:
                System.out.println("one");
                break;
            case 2:
                System.out.println("two");
                break;
            case 3:
                System.out.println("many");
                break;
        }
    }

    public static void main(String[] args) {
        howMany(3);
        howMany(2);
        howMany(1);
        howManyRule(1);
        howManyRule(2);
        howManyRule(3);
        howManyGroup(1);
        howManyGroup(2);
        howManyGroup(3);
    }
}
