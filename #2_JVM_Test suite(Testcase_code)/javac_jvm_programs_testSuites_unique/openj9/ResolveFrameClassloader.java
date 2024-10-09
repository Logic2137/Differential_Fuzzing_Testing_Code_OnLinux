
package com.ibm.jvmti.tests.decompResolveFrame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class ResolveFrameClassloader extends ClassLoader 
{
    private static final int BUFFER_SIZE = 8192;
    private String classFilter = null;
    
    
    static public native boolean checkFrame();
    
    public ResolveFrameClassloader(String classFilter)
    {
    	this.classFilter = classFilter;
    }
    
    protected synchronized Class loadClass(String className, boolean resolve) throws ClassNotFoundException 
    {
        log("Loading class: " + className + ", resolve: " + resolve);

        
        Class cls = findLoadedClass(className);
        if (cls != null) {
        	log("\tAlready loaded");
            return cls;
        }
          
        if (className.equals(classFilter)) {       
        	checkFrame();
        } 
        
        String clsFile = className.replace('.', '/') + ".class";

        
        byte[] classBytes = null;
        try {
            InputStream in = getResourceAsStream(clsFile);
            byte[] buffer = new byte[BUFFER_SIZE];
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int n = -1;
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
            }
            classBytes = out.toByteArray();
        }
        catch (IOException e) {
            log("ERROR loading class file: " + e);
        }

                
        if (classBytes == null) {
            throw new ClassNotFoundException("Cannot load class: " + className);
        }

        try {
            cls = defineClass(className, classBytes, 0, classBytes.length);
            if (resolve) {
                resolveClass(cls);
            }
        }
        catch (SecurityException e) { 
            cls = super.loadClass(className, resolve);
        }
 
        return cls;
    }

    private static void log(String s) {
        System.out.println(s);
    }
}


