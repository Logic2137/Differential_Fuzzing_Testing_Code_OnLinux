
package gc.g1.unloading.configuration;


public enum WhatToKeep {

    CLASSLOADER, CLASS, OBJECT;

    public Object decideUponRefToKeep(Class<?> clazz, ClassLoader classloader, Object object) {
        switch (this) {
            case OBJECT:
                return object;
            case CLASS:
                return clazz;
            case CLASSLOADER:
                return classloader != null ? classloader : object;
            default:
                return null;
        }
    }


}
