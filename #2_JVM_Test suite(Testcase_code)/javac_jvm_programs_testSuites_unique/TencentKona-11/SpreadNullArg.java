



package compiler.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class SpreadNullArg {

  public static void main(String args[]) {

    MethodType mt_ref_arg = MethodType.methodType(int.class, Integer.class);
    MethodHandle mh_spreadInvoker = MethodHandles.spreadInvoker(mt_ref_arg, 0);
    MethodHandle mh_spread_target;
    int result = 42;

    try {
      mh_spread_target =
          MethodHandles.lookup().findStatic(SpreadNullArg.class, "target_spread_arg", mt_ref_arg);
      result = (int) mh_spreadInvoker.invokeExact(mh_spread_target, (Object[]) null);
      throw new Error("Expected NullPointerException was not thrown");
    } catch (NullPointerException e) {
      System.out.println("Expected exception : " + e);
    } catch (Throwable e) {
      throw new Error(e);
    }

    if (result != 42) {
      throw new Error("result [" + result
          + "] != 42 : Expected NullPointerException was not thrown?");
    }
  }

  public static int target_spread_arg(Integer i1) {
    return i1.intValue();
  }

}
