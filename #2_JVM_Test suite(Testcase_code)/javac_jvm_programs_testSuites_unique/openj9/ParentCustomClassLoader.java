
package com.ibm.j9.jsr292;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;


public class ParentCustomClassLoader extends ClassLoader {
	
	public ParentCustomClassLoader( ClassLoader parent ) {
		super( parent );
	}
	
	@Override
	public Class<?> loadClass( String className ) throws ClassNotFoundException {
		if ( className.equals( "com.ibm.j9.jsr292.CustomLoadedClass1" ) ) {
			return getClass( className );
		}
		return super.loadClass( className );
	}
	
	private Class<?> getClass( String className )throws ClassNotFoundException {
		String classFile = className.replace(".", "/" )+ ".class";
		try {
			InputStream classStream = getClass().getClassLoader().getResourceAsStream( classFile );
			if ( classStream == null ) {
				throw new ClassNotFoundException( "Error loading class : " + classFile );
			}
	        int size = classStream.available();
	        byte classRep[] = new byte[size];
	        DataInputStream in = new DataInputStream( classStream );
	        in.readFully( classRep );
	        in.close();
	        Class<?> clazz = defineClass( className, classRep, 0, classRep.length );
			return clazz;
		} catch ( IOException e ) {
			throw new ClassNotFoundException( e.getMessage() );
		} 
	}
}	
