
package test.astro;

import org.xml.sax.InputSource;


public interface InputSourceFactory {
    
    InputSource newInputSource(String file) throws Exception;
}
