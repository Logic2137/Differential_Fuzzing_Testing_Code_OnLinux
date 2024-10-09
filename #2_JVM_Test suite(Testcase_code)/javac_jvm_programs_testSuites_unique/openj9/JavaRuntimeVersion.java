

public class JavaRuntimeVersion {

	
	public static void main(String[] args) {
        String runTimeVersion = System.getProperty("java.runtime.version");

        System.out.println("java.runtime.version is:" + runTimeVersion);

        String[] properties = runTimeVersion.split("\\s|-");
        String[] versionParts = properties[0].split("\\+");
        if (versionParts.length > 2) {
        	throw new IllegalArgumentException ("invalid version: " + properties[0]);
        }
        String intialVersion = versionParts[0];
        if (intialVersion.startsWith("1.8") || intialVersion.startsWith("8.")) {
        	
            if (!intialVersion.matches("[1-9][0-9\\.]+") && !intialVersion.matches("[1-9][0-9\\.]+_[1-9][0-9]*")) {
            	throw new IllegalArgumentException ("invalid version: " + intialVersion);
            }
        } else {
	        if (!intialVersion.matches("[1-9][0-9\\.]+")) {
	        	throw new IllegalArgumentException ("invalid version: " + intialVersion);
	        }
        }
        		
        if ((versionParts.length > 1) && !versionParts[1].matches("[1-9][0-9]*")) {
        	throw new IllegalArgumentException ("invalid build: " + versionParts[1]);
        }
 
        System.out.println("Version:  " + properties[0]);
        for (int i = 1; i < properties.length; i++) {
        	System.out.println("optional: " + properties[i]);
        }

        System.out.println("JavaRuntimeVersion Test OK");
	}

}
