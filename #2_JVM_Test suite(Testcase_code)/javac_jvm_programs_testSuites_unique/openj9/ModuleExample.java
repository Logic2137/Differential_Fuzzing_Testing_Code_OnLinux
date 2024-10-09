
package mods.modulea.package1;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

public class ModuleExample {
	public static Lookup getLookup() {
		return MethodHandles.lookup();
	}
	
	public static Lookup getPublicLookup() {
		return MethodHandles.publicLookup();
	}
}
