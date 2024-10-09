
package org.netbeans.jemmy;

import java.awt.Component;

public interface ComponentChooser {

    public boolean checkComponent(Component comp);

    public default String getDescription() {
        return toString();
    }
}
