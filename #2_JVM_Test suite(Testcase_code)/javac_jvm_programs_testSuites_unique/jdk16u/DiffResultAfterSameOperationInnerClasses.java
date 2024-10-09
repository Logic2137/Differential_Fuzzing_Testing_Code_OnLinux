



public class DiffResultAfterSameOperationInnerClasses {
    public int i = 1;
    private int j = 1;
    public String s1 = "Hi, ";
    private String s2 = "Hi, ";
    public int arr1[] = new int[]{1};
    public int arr2[] = new int[]{1};

    public static void main(String[] args) {
        DiffResultAfterSameOperationInnerClasses theTest =
                new DiffResultAfterSameOperationInnerClasses();
        InnerClass inner = theTest.new InnerClass();
        if (!inner.test1()) {
            throw new AssertionError("Different results after same calculation");
        }

        theTest.resetVars();
        if (!inner.test2()) {
            throw new AssertionError("Different results after same calculation");
        }
    }

    void resetVars() {
        i = 1;
        j = 1;
        s1 = "Hi, ";
        s2 = "Hi, ";
        arr1[0] = 1;
        arr2[0] = 1;
    }

    class InnerClass {
        public boolean test1() {
            i += i += 1;
            j += j += 1;

            arr1[0] += arr1[0] += 1;
            arr2[0] += arr2[0] += 1;

            s1 += s1 += "dude";
            s2 += s2 += "dude";

            return (i == j && i == 3 &&
                    arr1[0] == arr2[0] && arr2[0] == 3 &&
                    s1.equals(s2) && s1.endsWith("Hi, Hi, dude"));
        }

        public boolean test2() {
            (i) += (i) += 1;
            (j) += (j) += 1;

            (arr1[0])+= (arr1[0]) += 1;
            (arr2[0])+= (arr2[0]) += 1;

            (s1) += (s1) += "dude";
            (s2) += (s2) += "dude";

            return (i == j && i == 3 &&
                    arr1[0] == arr2[0] && arr2[0] == 3 &&
                    s1.equals(s2) && s1.endsWith("Hi, Hi, dude"));
        }
    }
}
