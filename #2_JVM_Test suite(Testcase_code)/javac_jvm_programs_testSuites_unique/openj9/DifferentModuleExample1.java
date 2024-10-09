
package mods.moduleb.package1;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

public class DifferentModuleExample1 {
	public static Lookup getLookup() {
		return MethodHandles.lookup();
	}
}
