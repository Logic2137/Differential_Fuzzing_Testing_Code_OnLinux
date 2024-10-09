


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.Locale.LanguageRange;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Locale.LanguageRange.MAX_WEIGHT;
import static java.util.Locale.LanguageRange.MIN_WEIGHT;

public class LSRDataTest {

    private static final char HYPHEN = '-';
    private static final Map<String, String> singleLangEquivMap = new HashMap<>();
    private static final Map<String, List<String>> multiLangEquivsMap = new HashMap<>();
    private static final Map<String, String> regionVariantEquivMap = new HashMap<>();

    
    
    
    private static final String LSR_FILE_PATH = System.getProperty("test.src", ".")
                + "/../../../../../make/data/lsrdata/language-subtag-registry.txt";

    public static void main(String[] args) throws IOException {

        loadLSRData(Paths.get(LSR_FILE_PATH).toRealPath());

        
        String ranges = "Accept-Language: aam, adp, aue, bcg, cqu, ema,"
                + " en-gb-oed, gti, koj, kwq, kxe, lii, lmm, mtm, ngv,"
                + " oyb, phr, pub, suj, taj;q=0.9, yug;q=0.5, gfx;q=0.4";
        List<LanguageRange> expected = parse(ranges);
        List<LanguageRange> actual = LanguageRange.parse(ranges);
        checkEquality(actual, expected);

        
        ranges = generateLangRanges();
        expected = parse(ranges);
        actual = LanguageRange.parse(ranges);
        checkEquality(actual, expected);

        
        ranges = generateRegionRanges();
        expected = parse(ranges);
        actual = LanguageRange.parse(ranges);
        checkEquality(actual, expected);

    }

    
    private static String generateLangRanges() {
        return Stream.concat(singleLangEquivMap.keySet().stream(), multiLangEquivsMap
                .keySet().stream()).collect(Collectors.joining(","));
    }

    
    private static String generateRegionRanges() {
        return regionVariantEquivMap.keySet().stream()
                .map(r -> "en".concat(r)).collect(Collectors.joining(", "));
    }

    
    private static void loadLSRData(Path path) throws IOException {
        String type = null;
        String tag = null;
        String preferred = null;
        String prefix = null;

        for (String line : Files.readAllLines(path, Charset.forName("UTF-8"))) {
            line = line.toLowerCase(Locale.ROOT);
            int index = line.indexOf(' ') + 1;
            if (line.startsWith("type:")) {
                type = line.substring(index);
            } else if (line.startsWith("tag:") || line.startsWith("subtag:")) {
                tag = line.substring(index);
            } else if (line.startsWith("preferred-value:")) {
                preferred = line.substring(index);
            } else if (line.startsWith("prefix:")) {
                prefix = line.substring(index);
            } else if (line.equals("%%")) {
                processDataAndGenerateMaps(type, tag, preferred, prefix);
                type = null;
                tag = null;
                preferred = null;
                prefix = null;
            }
        }

        
        processDataAndGenerateMaps(type, tag, preferred, prefix);
    }

    private static void processDataAndGenerateMaps(String type,
            String tag,
            String preferred,
            String prefix) {

        if (type == null || tag == null || preferred == null) {
            return;
        }

        if (type.equals("extlang") && prefix != null) {
            tag = prefix + "-" + tag;
        }

        if (type.equals("region") || type.equals("variant")) {
            if (!regionVariantEquivMap.containsKey(preferred)) {
                String tPref = HYPHEN + preferred;
                String tTag = HYPHEN + tag;
                regionVariantEquivMap.put(tPref, tTag);
                regionVariantEquivMap.put(tTag, tPref);
            } else {
                throw new RuntimeException("New case, need implementation."
                        + " A region/variant subtag \"" + preferred
                        + "\" is registered for more than one subtags.");
            }
        } else { 
            if (!singleLangEquivMap.containsKey(preferred)
                    && !multiLangEquivsMap.containsKey(preferred)) {
                
                singleLangEquivMap.put(preferred, tag);
                singleLangEquivMap.put(tag, preferred);
            } else if (singleLangEquivMap.containsKey(preferred)
                    && !multiLangEquivsMap.containsKey(preferred)) {
                String value = singleLangEquivMap.get(preferred);
                List<String> subtags = List.of(preferred, value, tag);
                
                singleLangEquivMap.keySet().removeAll(subtags);
                addEntriesToMultiEquivsMap(subtags);
            } else if (multiLangEquivsMap.containsKey(preferred)
                    && !singleLangEquivMap.containsKey(preferred)) {
                List<String> subtags = multiLangEquivsMap.get(preferred);
                
                
                subtags.add(0, preferred);
                subtags.add(tag);
                addEntriesToMultiEquivsMap(subtags);
            }
        }
    }

    
    private static void addEntriesToMultiEquivsMap(List<String> subtags) {
        
        
        
        
        subtags.forEach(subtag -> multiLangEquivsMap.put(subtag, subtags.stream()
                .filter(t -> !t.equals(subtag))
                .collect(Collectors.toList())));
    }

    private static List<LanguageRange> parse(String ranges) {
        ranges = ranges.replace(" ", "").toLowerCase(Locale.ROOT);
        if (ranges.startsWith("accept-language:")) {
            ranges = ranges.substring(16);
        }
        String[] langRanges = ranges.split(",");
        List<LanguageRange> priorityList = new ArrayList<>(langRanges.length);
        int numOfRanges = 0;
        for (String range : langRanges) {
            int wIndex = range.indexOf(";q=");
            String tag;
            double weight = 0.0;
            if (wIndex == -1) {
                tag = range;
                weight = MAX_WEIGHT;
            } else {
                tag = range.substring(0, wIndex);
                try {
                    weight = Double.parseDouble(range.substring(wIndex + 3));
                } catch (RuntimeException ex) {
                    throw new IllegalArgumentException("weight= " + weight + " for"
                            + " language range \"" + tag + "\", should be"
                            + " represented as a double");
                }

                if (weight < MIN_WEIGHT || weight > MAX_WEIGHT) {
                    throw new IllegalArgumentException("weight=" + weight
                            + " for language range \"" + tag
                            + "\", must be between " + MIN_WEIGHT
                            + " and " + MAX_WEIGHT + ".");
                }
            }

            LanguageRange entry = new LanguageRange(tag, weight);
            if (!priorityList.contains(entry)) {

                int index = numOfRanges;
                
                
                for (int i = 0; i < priorityList.size(); i++) {
                    if (priorityList.get(i).getWeight() < weight) {
                        index = i;
                        break;
                    }
                }
                priorityList.add(index, entry);
                numOfRanges++;

                String equivalent = getEquivalentForRegionAndVariant(tag);
                if (equivalent != null) {
                    LanguageRange equivRange = new LanguageRange(equivalent, weight);
                    if (!priorityList.contains(equivRange)) {
                        priorityList.add(index + 1, equivRange);
                        numOfRanges++;
                    }
                }

                List<String> equivalents = getEquivalentsForLanguage(tag);
                if (equivalents != null) {
                    for (String equiv : equivalents) {
                        LanguageRange equivRange = new LanguageRange(equiv, weight);
                        if (!priorityList.contains(equivRange)) {
                            priorityList.add(index + 1, equivRange);
                            numOfRanges++;
                        }

                        equivalent = getEquivalentForRegionAndVariant(equiv);
                        if (equivalent != null) {
                            equivRange = new LanguageRange(equivalent, weight);
                            if (!priorityList.contains(equivRange)) {
                                priorityList.add(index + 1, equivRange);
                                numOfRanges++;
                            }
                        }
                    }
                }
            }
        }
        return priorityList;
    }

    
    private static String replaceFirstSubStringMatch(String range,
            String substr, String replacement) {
        int pos = range.indexOf(substr);
        if (pos == -1) {
            return range;
        } else {
            return range.substring(0, pos) + replacement
                    + range.substring(pos + substr.length());
        }
    }

    private static List<String> getEquivalentsForLanguage(String range) {
        String r = range;

        while (r.length() > 0) {
            if (singleLangEquivMap.containsKey(r)) {
                String equiv = singleLangEquivMap.get(r);
                
                
                return List.of(replaceFirstSubStringMatch(range, r, equiv));
            } else if (multiLangEquivsMap.containsKey(r)) {
                List<String> equivs = multiLangEquivsMap.get(r);
                List<String> result = new ArrayList(equivs.size());
                for (int i = 0; i < equivs.size(); i++) {
                    result.add(i, replaceFirstSubStringMatch(range,
                            r, equivs.get(i)));
                }
                return result;
            }

            
            int index = r.lastIndexOf(HYPHEN);
            if (index == -1) {
                break;
            }
            r = r.substring(0, index);
        }

        return null;
    }

    private static String getEquivalentForRegionAndVariant(String range) {
        int extensionKeyIndex = getExtentionKeyIndex(range);

        for (String subtag : regionVariantEquivMap.keySet()) {
            int index;
            if ((index = range.indexOf(subtag)) != -1) {
                
                if (extensionKeyIndex != Integer.MIN_VALUE
                        && index > extensionKeyIndex) {
                    continue;
                }

                int len = index + subtag.length();
                if (range.length() == len || range.charAt(len) == HYPHEN) {
                    return replaceFirstSubStringMatch(range, subtag,
                            regionVariantEquivMap.get(subtag));
                }
            }
        }

        return null;
    }

    private static int getExtentionKeyIndex(String s) {
        char[] c = s.toCharArray();
        int index = Integer.MIN_VALUE;
        for (int i = 1; i < c.length; i++) {
            if (c[i] == HYPHEN) {
                if (i - index == 2) {
                    return index;
                } else {
                    index = i;
                }
            }
        }
        return Integer.MIN_VALUE;
    }

    private static void checkEquality(List<LanguageRange> expected,
            List<LanguageRange> actual) {

        int expectedSize = expected.size();
        int actualSize = actual.size();

        if (expectedSize != actualSize) {
            throw new RuntimeException("[FAILED: Size of the priority list"
                    + " does not match, Expected size=" + expectedSize + "]");
        } else {
            for (int i = 0; i < expectedSize; i++) {
                LanguageRange lr1 = expected.get(i);
                LanguageRange lr2 = actual.get(i);

                if (!lr1.getRange().equals(lr2.getRange())
                        || lr1.getWeight() != lr2.getWeight()) {
                    throw new RuntimeException("[FAILED: Ranges at index "
                            + i + " do not match Expected: range=" + lr1.getRange()
                            + ", weight=" + lr1.getWeight() + ", Actual: range="
                            + lr2.getRange() + ", weight=" + lr2.getWeight() + "]");
                }
            }
        }
    }
}
