
package org.openj9.test.vmArguments;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Properties;
import java.util.Set;

@SuppressWarnings("nls")
public class ArgumentDumper {
	public static final String USERARG_TAG = "2CIUSERARG";
	public static void main(String[] args) {

		System.out.println(USERARG_TAG);
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		for (String a: bean.getInputArguments()) {
			System.out.println(a);
		}
		System.out.print("\n===============\n\nSystem properties\n\n");
		Properties props = System.getProperties();
		Set<String> pns = props.stringPropertyNames();
		for (String pn: pns) {
			String p = props.getProperty(pn).replaceAll("\n", "\\n");
			System.out.print(pn);
			System.out.print("=");
			System.out.println(p);
		}
	}

}
