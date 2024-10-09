

package org.openj9.test.lambdatests;

import java.util.Comparator;



public class Test2 {
        
    public void func() {
        Comparator<String> strComparator = (String o1, String o2) -> o1.compareTo(o2);
        int strComparison = strComparator.compare("coffee", "cup");
        System.out.println(strComparison);
            
        Comparator<String> strComparator2 = (String o1, String o2) -> o2.compareTo(o1) + 1523;
        int strComparison2 = strComparator2.compare("a_cup_of", "tea");
        System.out.println(strComparison2);
    }
}

