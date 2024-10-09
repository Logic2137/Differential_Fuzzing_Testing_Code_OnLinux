



import java.util.prefs.Preferences;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;

public class RemoveNullKeyCheck {

    private static boolean failed = false;

    public static void main(String[] args) throws Exception {
        checkPreferencesRemove();
        checkAbstractPreferencesRemove();
        if (failed) {
            throw new RuntimeException("Expected NullPointerException " +
                                       "not thrown");
        }
    }

    public static void checkPreferencesRemove() {
        try {
            Preferences node = Preferences.userRoot().node("N1");
            node.remove(null);
            failed = true;
        } catch (NullPointerException npe) {
        }
    }

    public static void checkAbstractPreferencesRemove() {

        Preferences abstrPrefs = new AbstractPreferences(null, "") {
            @Override
            protected void putSpi(String key, String value) {
            }
            @Override
            protected String getSpi(String key) {
                return null;
            }
            @Override
            protected void removeSpi(String key) {
            }
            @Override
            protected void removeNodeSpi() throws BackingStoreException {
            }
            @Override
            protected String[] keysSpi() throws BackingStoreException {
                return new String[0];
            }
            @Override
            protected String[] childrenNamesSpi() throws BackingStoreException {
                return new String[0];
            }
            @Override
            protected AbstractPreferences childSpi(String name) {
                return null;
            }
            @Override
            protected void syncSpi() throws BackingStoreException {
            }
            @Override
            protected void flushSpi() throws BackingStoreException {
            }
        };

        try {
            abstrPrefs.remove(null);
            failed = true;
        } catch(NullPointerException npe) {
        }
    }
}
