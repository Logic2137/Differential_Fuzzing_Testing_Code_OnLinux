public class Test7009359 {

    public static void main(String[] args) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 100000; i++) {
            if (!stringmakerBUG(null).equals("NPE")) {
                System.out.println("StringBuffer(null) does not throw NPE");
                System.exit(97);
            }
        }
    }

    public static String stringmakerBUG(String str) {
        try {
            return new StringBuffer(str).toString();
        } catch (NullPointerException e) {
            return "NPE";
        }
    }
}
