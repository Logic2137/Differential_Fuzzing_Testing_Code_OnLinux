
package org.netbeans.jemmy.drivers.text;

abstract class NavigationKey {

    private int keyCode;

    private int mods;

    public NavigationKey(int keyCode, int mods) {
        this.keyCode = keyCode;
        this.mods = mods;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public int getModifiers() {
        return mods;
    }

    public abstract int getDirection();
}
