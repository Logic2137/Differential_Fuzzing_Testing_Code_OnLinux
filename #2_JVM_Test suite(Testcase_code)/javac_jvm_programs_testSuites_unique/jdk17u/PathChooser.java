
package org.netbeans.jemmy.drivers;

public interface PathChooser {

    public boolean checkPathComponent(int depth, Object component);

    public int getDepth();
}
