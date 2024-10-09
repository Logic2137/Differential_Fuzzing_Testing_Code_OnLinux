



import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toUnmodifiableMap;


public class RandomizedIteration {
    
    static void writeFiles(int suffix) throws IOException {
        try (PrintStream setOut = new PrintStream("set." + suffix)) {
            Set.of(WORDS)
               .forEach(setOut::println);
        }

        try (PrintStream mapOut = new PrintStream("map." + suffix)) {
            var map = Map.ofEntries(Arrays.stream(WORDS)
                                          .map(word -> Map.entry(word, ""))
                                          .toArray(Map.Entry<?, ?>[]::new));
            map.keySet()
               .forEach(mapOut::println);
        }
    }

    
    static Set<Integer> readFiles(String prefix, int count) throws IOException {
        Set<Integer> hashes = new HashSet<>();
        for (int suffix = 0; suffix < count; suffix++) {
            String name = prefix + suffix;
            int hash = Files.readAllLines(Paths.get(name)).hashCode();
            System.out.println(name + ": " + hash);
            hashes.add(hash);
        }
        return hashes;
    }

    
    public static void main(String[] args) throws IOException {
        if ("verify".equals(args[0])) {
            int count = Integer.parseInt(args[1]);
            System.out.println("Verifying " + count + " files.");
            Set<Integer> setHashes = readFiles("set.", count);
            Set<Integer> mapHashes = readFiles("map.", count);
            if (setHashes.size() > 1 && mapHashes.size() > 1) {
                System.out.println("Passed: differing iteration orders were detected.");
            } else {
                throw new AssertionError("FAILED: iteration order not randomized!");
            }
        } else {
            int suffix = Integer.parseInt(args[0]);
            System.out.println("Generating files: " + suffix);
            writeFiles(suffix);
        }
    }

    
    static final String[] WORDS = {
        "anatomicophysiological",
        "anthropomorphologically",
        "aquopentamminecobaltic",
        "blepharoconjunctivitis",
        "blepharosphincterectomy",
        "cholecystenterorrhaphy",
        "cholecystoduodenostomy",
        "choledochoduodenostomy",
        "counterexcommunication",
        "dacryocystoblennorrhea",
        "dacryocystosyringotomy",
        "deanthropomorphization",
        "duodenocholecystostomy",
        "electroencephalography",
        "electrotelethermometer",
        "epididymodeferentectomy",
        "formaldehydesulphoxylate",
        "formaldehydesulphoxylic",
        "gastroenteroanastomosis",
        "hematospectrophotometer",
        "hexamethylenetetramine",
        "hexanitrodiphenylamine",
        "historicocabbalistical",
        "hydropneumopericardium",
        "hyperconscientiousness",
        "laparocolpohysterotomy",
        "lymphangioendothelioma",
        "macracanthrorhynchiasis",
        "microcryptocrystalline",
        "naphthylaminesulphonic",
        "nonrepresentationalism",
        "omnirepresentativeness",
        "pancreaticoduodenostomy",
        "pancreaticogastrostomy",
        "pathologicohistological",
        "pathologicopsychological",
        "pericardiomediastinitis",
        "phenolsulphonephthalein",
        "philosophicohistorical",
        "philosophicotheological",
        "photochronographically",
        "photospectroheliograph",
        "pneumohydropericardium",
        "pneumoventriculography",
        "polioencephalomyelitis",
        "Prorhipidoglossomorpha",
        "Pseudolamellibranchiata",
        "pseudolamellibranchiate",
        "pseudomonocotyledonous",
        "pyopneumocholecystitis",
        "scientificogeographical",
        "scientificophilosophical",
        "scleroticochorioiditis",
        "stereophotomicrography",
        "tetraiodophenolphthalein",
        "theologicoastronomical",
        "theologicometaphysical",
        "thymolsulphonephthalein",
        "thyroparathyroidectomize",
        "thyroparathyroidectomy",
        "transubstantiationalist",
        "ureterocystanastomosis",
        "zoologicoarchaeologist"
    };
}
