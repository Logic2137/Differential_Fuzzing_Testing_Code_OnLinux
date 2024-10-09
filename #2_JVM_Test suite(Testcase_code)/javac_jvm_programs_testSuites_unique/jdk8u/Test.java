public class Test {

    public static void main(String[] args) throws Exception {
        StringBuffer builder = new StringBuffer();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 100; i++) builder.append("I am the very model of a modern major general\n");
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int j = 0; j < builder.length(); j++) {
            previousSpaceIndex(builder, j);
        }
    }

    private static final int previousSpaceIndex(CharSequence sb, int seek) {
        seek--;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        while (seek > 0) {
            if (sb.charAt(seek) == ' ') {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
                while (seek > 0 && sb.charAt(seek - 1) == ' ') seek--;
                return seek;
            }
            seek--;
        }
        return 0;
    }
}
