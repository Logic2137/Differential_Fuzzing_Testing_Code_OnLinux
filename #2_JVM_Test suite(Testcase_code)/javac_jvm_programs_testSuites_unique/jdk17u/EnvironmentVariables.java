public class EnvironmentVariables {

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new RuntimeException("ERROR: two command line arguments expected");
        }
        String name = args[0];
        String expect = args[1];
        String key = null;
        if (!name.endsWith("*")) {
            key = name;
        } else {
            name = name.split("\\*")[0];
            for (String s : System.getenv().keySet()) {
                if (s.startsWith(name)) {
                    if (key == null) {
                        key = s;
                    } else {
                        System.err.println("WARNING: more variables match: " + s);
                    }
                }
            }
            if (key == null) {
                throw new RuntimeException("ERROR: unable to find a match for: " + name);
            }
        }
        System.err.println("Will check the variable named: '" + key + "' expecting the value: '" + expect + "'");
        if (!System.getenv().containsKey(key)) {
            throw new RuntimeException("ERROR: the variable '" + key + "' is not present in the environment");
        }
        if (!expect.equals(System.getenv().get(key))) {
            throw new RuntimeException("ERROR: expected: '" + expect + "', got: '" + System.getenv().get(key) + "'");
        }
        for (String x : args) {
            System.err.print(x + " ");
        }
        System.err.println("-----> Passed!");
    }
}
