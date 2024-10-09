
package com.sun.javafx.application;

public class LauncherImpl {

    public static void launchApplication(final String launchName, final String launchMode, final String[] args) {
        System.out.println("LaunchName: " + launchName);
        System.out.println("LaunchMode: " + launchMode);
        System.out.println("Parameters:");
        for (String arg : args) {
            System.out.println("parameter: " + arg);
        }
        System.exit(0);
    }

    private LauncherImpl() {
        throw new InternalError("should not get here");
    }
}
