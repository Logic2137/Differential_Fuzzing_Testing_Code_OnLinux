



import java.util.*;

public class NullKeyAtResize {
    public static void main(String[] args) throws Exception {
        List<Object> old_order = new ArrayList<>();
        Map<Object,Object> m = new HashMap<>(16);
        int number = 0;
        while(number < 100000) {
            m.put(null,null); 
            m.remove(null); 
            Integer adding = (number += 100);
            m.put(adding, null); 
            List<Object> new_order = new ArrayList<>();
            new_order.addAll(m.keySet());
            new_order.remove(adding);
            if(!old_order.equals(new_order)) {
                
                System.out.println("Encountered resize after " + (number / 100) + " iterations");
                break;
            }
            
            old_order.clear();
            old_order.addAll(m.keySet());
        }
        if(number == 100000) {
            throw new Error("Resize never occurred");
        }
    }
}
