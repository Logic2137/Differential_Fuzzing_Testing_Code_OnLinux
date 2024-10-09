
package mods.modulea.package1;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

public class AnotherModuleExample {
	public static Lookup getLookup() {
		return MethodHandles.lookup();
	}
}
