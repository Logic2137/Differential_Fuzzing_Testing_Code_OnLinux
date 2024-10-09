import java.util.*;
import java.io.*;

public class TestResource_de extends PropertyResourceBundle {

    public TestResource_de() throws IOException, FileNotFoundException {
        super(new FileInputStream("TestResource_de"));
    }
}
