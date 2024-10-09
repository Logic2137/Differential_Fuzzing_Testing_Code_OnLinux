
package mods.modulea.package2;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

public class SameModuleExample {
	public static Lookup getLookup() {
		return MethodHandles.lookup();
	}
}
