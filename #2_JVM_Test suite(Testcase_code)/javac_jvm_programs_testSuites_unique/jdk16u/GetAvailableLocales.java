

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

class GetAvailableLocales {

    public static void main(String[] args) {
        Set<String> expected = Set.of(args);
        Set<String> actual =
            Arrays.stream(Locale.getAvailableLocales())
                  
                  .map(loc -> loc.equals(Locale.ROOT) ? "(root)" : loc.toString())
                  .collect(Collectors.toSet());

        if (!expected.equals(actual)) {
            diff(expected, actual);
            System.exit(1);
        }
    }

    private static void diff(Set<String> expected, Set<String> actual) {
        Set<String> s1 = new TreeSet<>(expected);
        s1.removeAll(actual);
        if (!s1.isEmpty()) {
            System.out.println("\tMissing locale(s): " + s1);
        }
        Set<String> s2 = new TreeSet<>(actual);
        s2.removeAll(expected);
        if (!s2.isEmpty()) {
            System.out.println("\tExtra locale(s): " + s2);
        }
    }
}
