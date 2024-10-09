



public interface Private05 {

    private static String staticPrivate() {
        return "static private";
    }

    private String instancePrivate() {
        return "instance private";
    }

    public static void main(String [] args) {
        String result  = staticPrivate();
        if (!result.equals("static private"))
            throw new AssertionError("Incorrect result for static private interface method");
        Private05 pvt = new Private05() {};
        result = pvt.instancePrivate();
        if (!result.equals("instance private"))
            throw new AssertionError("Incorrect result for instance private interface method");
    }
}
