

package org.openj9.test.java_lang_invoke.helpers;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;


public interface Jep334MHHelper {
    public abstract void virtualTest();

    default void specialTest() {}
    
    static void staticTest() {}
}
