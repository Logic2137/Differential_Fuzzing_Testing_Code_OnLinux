
package com.ibm.trace.tests.apptrace;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TestJavaCoreAndSnap { 

    private static final String XEHSTTYPE = "3XEHSTTYPE     ";

	public static void main(String[] args) {

        System.out.println("TestJavaCoreAndSnap: started");

        if( args.length != 2 ) {
        	System.err.println("Expected two arguments, javacore and formatted snap trace.");
        	exit(1);
        }

        String javacoreFileName = args[0];
        String traceFileName = args[1];
        
        System.out.printf("Comparing current thread trace in %s and %s\n", javacoreFileName, traceFileName);
        
        String[] jcTrace = extractJCTrace(javacoreFileName);
        
        String lastTPid = getTracePointId(jcTrace[0]);
        
        
        
        String[] snapTrace = extractFormattedTrace(traceFileName);
        
        int jcTraceIndex = 0;
        int snapTraceIndex = 0;
        boolean found = false;
        
        
        for( ; snapTraceIndex < snapTrace.length; snapTraceIndex++) {
        	
        	if( "j9dmp.9".equals(getTracePointId(snapTrace[snapTraceIndex])) &&
        			snapTrace[snapTraceIndex].contains(javacoreFileName) ) {
        		found = true;
        		
        		break;
        	}
        }
        
        if( found == false ) {
        	System.err.println("Couldn't match start of trace in java core to start of trace in snap.");
        	exit(2);
        }
        
        for( ; snapTraceIndex <  snapTrace.length && jcTraceIndex < jcTrace.length; snapTraceIndex++, jcTraceIndex++) {
        	String snapTraceLine = snapTrace[snapTraceIndex];
        	String jcTraceLine = jcTrace[jcTraceIndex];
        	String snapTraceID = getTracePointId(snapTraceLine);
        	String jcTraceID = getTracePointId(jcTraceLine);
        	if( !snapTraceID.equals(jcTraceID) ) {
        		System.err.println("Trace point lines did not match:" + jcTraceID + " vs " + snapTraceID );
        		System.err.println("Javacore: " + jcTraceLine);
        		System.err.println("Snap trace: " + snapTraceLine);
        		
        		exit(3);
        	} else {

        	}
        	
        	

        }
        
        exit(0);
        
    }

    private static void exit(int code) {
    	if( code == 0 ) {
    		System.out.println("TestJavaCoreAndSnap: passed");
    	} else {
    		System.out.println("TestJavaCoreAndSnap: failed");
    	}
    	System.out.println("TestJavaCoreAndSnap: finished");
    	System.exit(code);
    }
    
    private static String[] extractJCTrace(String javacoreFileName) {

    	BufferedReader in = null;
    	List<String> traceLines = new LinkedList<String>();
    			
    	try {
    		try {
    			in = new BufferedReader(new FileReader(javacoreFileName));
    		} catch (FileNotFoundException e) {
    			System.err.println("File: " + javacoreFileName + " not found.");
    			return null;
    		}

    		String str;
    		try {
    			while( (str = in.readLine()) != null ) {
    				if( str.startsWith(XEHSTTYPE) ) {
    					traceLines.add(str.substring(XEHSTTYPE.length()));
    				}
    			}
    		} catch (IOException e) {
    			System.err.println("Failed to read from file: " + javacoreFileName);
    			return null;
    		}
    	} finally {
    		try {
    			in.close();
    		} catch (IOException e) {
    			
    		}
    	}
    	
    	return traceLines.toArray(new String[0]);

    }

    private static String[] extractFormattedTrace(String traceFileName) {

    	BufferedReader in = null;
    	List<String> traceLines = new LinkedList<String>();
    			
    	try {
    		try {
    			in = new BufferedReader(new FileReader(traceFileName));
    		} catch (FileNotFoundException e) {
    			System.err.println("File: " + traceFileName + " not found.");
    			return null;
    		}

    		String str;
    		try {
    			while( (str = in.readLine()) != null ) {
    				if( str.startsWith("Time") ) {
    					
    					
    					break;
    				}
    			}
    			while( (str = in.readLine()) != null ) {
    				
    				
    				traceLines.add(0, str);
    			}
    		} catch (IOException e) {
    			System.err.println("Failed to read from file: " + traceFileName);
    			return null;
    		}
    	} finally {
    		try {
    			in.close();
    		} catch (IOException e) {
    			
    		}
    	}
    	
    	return traceLines.toArray(new String[0]);

    }
    
    
    
    private static String getTracePointId(String traceLine) {
    	return traceLine.split("\\s+")[2];
    }
    
    private static String getTracePointDetail(String traceLine) {
    	return traceLine.split("\\s+",5)[4].trim();
    }
}

