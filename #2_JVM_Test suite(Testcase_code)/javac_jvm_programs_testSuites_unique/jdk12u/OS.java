

package jdk.test.failurehandler.jtreg;


class OS {
    public final String family;

    private static OS current;

    public static OS current() {
        if (current == null) {
            String name = System.getProperty("os.name");
            current = new OS(name);
        }
        return current;
    }

    private OS(String name) {
        if (name.startsWith("AIX")) {
            family = "aix";
        } else if (name.startsWith("Linux")) {
            family = "linux";
        } else if (name.startsWith("Mac") || name.startsWith("Darwin")) {
            family = "mac";
        } else if (name.startsWith("SunOS") || name.startsWith("Solaris")) {
            family = "solaris";
        } else if (name.startsWith("Windows")) {
            family = "windows";
        } else {
            
            family = name.replaceFirst("^([^ ]+).*", "$1");
        }
    }
}


