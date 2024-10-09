public class AnalyzerMandatoryWarnings {

    private void test() {
        Runnable r = new Runnable() {

            public void run() {
                Depr r;
            }
        };
    }
}

@Deprecated
class Depr {
}
