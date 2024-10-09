

import java.util.*;



public class ArrayListTest {
    public static void main(String args[]) throws Exception {
        
        
        
        List<String> a = new ArrayList<>();
        a.add("hello world.");
        a.forEach(str -> System.out.println(str));

        System.out.println(Class.forName("boot.append.Foo"));    
    }
}
