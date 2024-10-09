import java.util.prefs.AbstractPreferences;

public class StubPreferences extends AbstractPreferences {

    public StubPreferences() {
        super(null, "");
    }

    public String getSpi(String x) {
        return null;
    }

    public void putSpi(String x, String y) {
    }

    public void removeSpi(String x) {
    }

    public AbstractPreferences childSpi(String x) {
        return null;
    }

    public void removeNodeSpi() {
    }

    public String[] keysSpi() {
        return null;
    }

    public String[] childrenNamesSpi() {
        return null;
    }

    public void syncSpi() {
    }

    public void flushSpi() {
    }
}
