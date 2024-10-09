
package com.oracle.java.testlibrary;

import com.sun.management.HotSpotDiagnosticMXBean;
import java.lang.management.ManagementFactory;

public class DynamicVMOption {

    private final HotSpotDiagnosticMXBean mxBean;

    public final String name;

    public DynamicVMOption(String name) {
        this.name = name;
        mxBean = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);
    }

    public final void setValue(String newValue) {
        mxBean.setVMOption(name, newValue);
    }

    public final String getValue() {
        return mxBean.getVMOption(name).getValue();
    }

    public final boolean isWriteable() {
        return mxBean.getVMOption(name).isWriteable();
    }

    public boolean isValidValue(String value) {
        boolean isValid = true;
        String oldValue = getValue();
        try {
            setValue(value);
        } catch (NullPointerException e) {
            if (value == null) {
                isValid = false;
            }
        } catch (IllegalArgumentException e) {
            isValid = false;
        } finally {
            setValue(oldValue);
        }
        return isValid;
    }

    public static String getString(String name) {
        return new DynamicVMOption(name).getValue();
    }

    public static int getInt(String name) {
        return Integer.parseInt(getString(name));
    }

    public static void setString(String name, String value) {
        new DynamicVMOption(name).setValue(value);
    }

    public static void setInt(String name, int value) {
        new DynamicVMOption(name).setValue(Integer.toString(value));
    }
}
