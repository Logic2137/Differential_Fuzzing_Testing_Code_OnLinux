
package mods.modulec.package1;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

public class DifferentModuleExample2 {
	public static Lookup getLookup() {
		return MethodHandles.lookup();
	}
}
