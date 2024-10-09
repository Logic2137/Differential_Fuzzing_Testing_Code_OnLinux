import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Locale.IsoCountryCode;
import java.util.Set;
import java.util.stream.Collectors;

public class Bug8071929 {

    private static final List<String> ISO3166_1_ALPHA2_OBSOLETE_CODES = List.of("AN", "BU", "CS", "NT", "SF", "TP", "YU", "ZR");

    private static final Set<String> ISO3166_3EXPECTED = Set.of("AIDJ", "ANHH", "BQAQ", "BUMM", "BYAA", "CSHH", "CSXX", "CTKI", "DDDE", "DYBJ", "FQHH", "FXFR", "GEHH", "HVBF", "JTUM", "MIUM", "NHVU", "NQAQ", "NTHH", "PCHH", "PUUM", "PZPA", "RHZW", "SKIN", "SUHH", "TPTL", "VDVN", "WKUM", "YDYE", "YUCS", "ZRCD");

    private static final Set<String> ISO3166_1_ALPHA3_EXPECTED = Set.of("ABW", "AFG", "AGO", "AIA", "ALA", "ALB", "AND", "ARE", "ARG", "ARM", "ASM", "ATA", "ATF", "ATG", "AUS", "AUT", "AZE", "BDI", "BEL", "BEN", "BES", "BFA", "BGD", "BGR", "BHR", "BHS", "BIH", "BLM", "BLR", "BLZ", "BMU", "BOL", "BRA", "BRB", "BRN", "BTN", "BVT", "BWA", "CAF", "CAN", "CCK", "CHE", "CHL", "CHN", "CIV", "CMR", "COD", "COG", "COK", "COL", "COM", "CPV", "CRI", "CUB", "CUW", "CXR", "CYM", "CYP", "CZE", "DEU", "DJI", "DMA", "DNK", "DOM", "DZA", "ECU", "EGY", "ERI", "ESH", "ESP", "EST", "ETH", "FIN", "FJI", "FLK", "FRA", "FRO", "FSM", "GAB", "GBR", "GEO", "GGY", "GHA", "GIB", "GIN", "GLP", "GMB", "GNB", "GNQ", "GRC", "GRD", "GRL", "GTM", "GUF", "GUM", "GUY", "HKG", "HMD", "HND", "HRV", "HTI", "HUN", "IDN", "IMN", "IND", "IOT", "IRL", "IRN", "IRQ", "ISL", "ISR", "ITA", "JAM", "JEY", "JOR", "JPN", "KAZ", "KEN", "KGZ", "KHM", "KIR", "KNA", "KOR", "KWT", "LAO", "LBN", "LBR", "LBY", "LCA", "LIE", "LKA", "LSO", "LTU", "LUX", "LVA", "MAC", "MAF", "MAR", "MCO", "MDA", "MDG", "MDV", "MEX", "MHL", "MKD", "MLI", "MLT", "MMR", "MNE", "MNG", "MNP", "MOZ", "MRT", "MSR", "MTQ", "MUS", "MWI", "MYS", "MYT", "NAM", "NCL", "NER", "NFK", "NGA", "NIC", "NIU", "NLD", "NOR", "NPL", "NRU", "NZL", "OMN", "PAK", "PAN", "PCN", "PER", "PHL", "PLW", "PNG", "POL", "PRI", "PRK", "PRT", "PRY", "PSE", "PYF", "QAT", "REU", "ROU", "RUS", "RWA", "SAU", "SDN", "SEN", "SGP", "SGS", "SHN", "SJM", "SLB", "SLE", "SLV", "SMR", "SOM", "SPM", "SRB", "SSD", "STP", "SUR", "SVK", "SVN", "SWE", "SWZ", "SXM", "SYC", "SYR", "TCA", "TCD", "TGO", "THA", "TJK", "TKL", "TKM", "TLS", "TON", "TTO", "TUN", "TUR", "TUV", "TWN", "TZA", "UGA", "UKR", "UMI", "URY", "USA", "UZB", "VAT", "VCT", "VEN", "VGB", "VIR", "VNM", "VUT", "WLF", "WSM", "YEM", "ZAF", "ZMB", "ZWE");

    private static void checkISO3166_1_Alpha2ObsoleteCodes() {
        Set<String> unexpectedCodes = ISO3166_1_ALPHA2_OBSOLETE_CODES.stream().filter(Set.of(Locale.getISOCountries())::contains).collect(Collectors.toSet());
        if (!unexpectedCodes.isEmpty()) {
            throw new RuntimeException("Obsolete ISO3166-1 alpha2 two letter" + " country Codes " + unexpectedCodes + " in output of getISOCountries() method");
        }
    }

    private static void checkISO3166_3Codes() {
        Set<String> iso3166_3Codes = Locale.getISOCountries(IsoCountryCode.PART3);
        if (!iso3166_3Codes.equals(ISO3166_3EXPECTED)) {
            reportDifference(iso3166_3Codes, ISO3166_3EXPECTED);
        }
    }

    private static void checkISO3166_1_Alpha3Codes() {
        Set<String> iso3166_1_Alpha3Codes = Locale.getISOCountries(IsoCountryCode.PART1_ALPHA3);
        if (!iso3166_1_Alpha3Codes.equals(ISO3166_1_ALPHA3_EXPECTED)) {
            reportDifference(iso3166_1_Alpha3Codes, ISO3166_1_ALPHA3_EXPECTED);
        }
    }

    private static void checkISO3166_1_Alpha2Codes() {
        Set<String> iso3166_1_Alpha2Codes = Locale.getISOCountries(IsoCountryCode.PART1_ALPHA2);
        Set<String> ISO3166_1_ALPHA2_EXPECTED = Set.of(Locale.getISOCountries());
        if (!iso3166_1_Alpha2Codes.equals(ISO3166_1_ALPHA2_EXPECTED)) {
            reportDifference(iso3166_1_Alpha2Codes, ISO3166_1_ALPHA2_EXPECTED);
        }
    }

    private static void reportDifference(Set<String> retrievedCountrySet, Set<String> expectedCountrySet) {
        Set<String> retrievedSet = new HashSet<>(retrievedCountrySet);
        Set<String> expectedSet = new HashSet<>(expectedCountrySet);
        retrievedSet.removeAll(expectedCountrySet);
        expectedSet.removeAll(retrievedCountrySet);
        if ((retrievedSet.size() > 0) && (expectedSet.size() > 0)) {
            throw new RuntimeException("Retrieved country codes set contains extra codes " + retrievedSet + " and missing codes " + expectedSet);
        }
        if (retrievedSet.size() > 0) {
            throw new RuntimeException("Retrieved country codes set contains extra codes " + retrievedSet);
        }
        if (expectedSet.size() > 0) {
            throw new RuntimeException("Retrieved country codes set is missing codes " + expectedSet);
        }
    }

    public static void main(String[] args) {
        checkISO3166_1_Alpha2ObsoleteCodes();
        checkISO3166_1_Alpha2Codes();
        checkISO3166_1_Alpha3Codes();
        checkISO3166_3Codes();
    }
}
