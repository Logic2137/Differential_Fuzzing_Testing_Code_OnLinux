

import java.util.*;
import java.io.*;


public class ParentClassLoader extends ClassLoader {
    public ParentClassLoader() { super(); }
    public ParentClassLoader(String name, ClassLoader l) { super(name, l); }
}
