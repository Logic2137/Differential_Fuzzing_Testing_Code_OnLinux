

import java.util.prefs.Preferences;



public class CheckUserPrefFirst {

    public static void main(String[] args) throws Exception {
        Preferences prefs = Preferences.userNodeForPackage(CheckUserPrefFirst.class);
        prefs.put("Check", "Success");
        prefs.flush();
    }
}

