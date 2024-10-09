
package org.netbeans.jemmy.util;

import java.awt.Component;


public interface DumpController {

    public boolean onComponentDump(Component comp);

    public boolean onPropertyDump(Component comp, String name, String value);

}
