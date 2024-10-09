

package org.netbeans.jemmy.util;

import javax.swing.UIManager;


public class LookAndFeel {

    
    public static boolean isMetal() {
        return isLookAndFeel("Metal");
    }

    
    public static boolean isNimbus() {
        return isLookAndFeel("Nimbus");
    }

    
    public static boolean isMotif() {
        return isLookAndFeel("Motif");
    }

    
    public static boolean isGTK() {
        return isLookAndFeel("GTK");
    }

    
    public static boolean isAqua() {
        return isLookAndFeel("Aqua");
    }

    
    public static boolean isWindows() {
        return UIManager.getLookAndFeel().getClass().
                getSimpleName().equals("WindowsLookAndFeel");
    }

    
    public static boolean isWindowsClassic() {
        return UIManager.getLookAndFeel().getClass().
                getSimpleName().equals("WindowsClassicLookAndFeel");
    }

    private static boolean isLookAndFeel(String id) {
        return UIManager.getLookAndFeel().getID().equals(id);
    }
}
