

import java.lang.invoke.*;
import java.util.*;

public class MethodReferenceTestVarHandle_neg {

  interface Setter {
      int apply(int[] arr, int idx, int val);
  }

  public static void main(String[] args) {
      VarHandle vh = MethodHandles.arrayElementVarHandle(int[].class);

      
      Setter f = vh::set;
  }
}
