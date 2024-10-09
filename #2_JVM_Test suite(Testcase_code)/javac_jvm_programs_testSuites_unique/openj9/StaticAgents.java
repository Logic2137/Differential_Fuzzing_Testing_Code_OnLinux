

package com.ibm.j9.tests.jeptests;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;


public class StaticAgents {
	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("--attach")) {
			com.sun.tools.attach.VirtualMachine vm = null;
			RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
			String jvmName = bean.getName();
			String agentA = new String("testjvmtiA");
			String agentB = new String("testjvmtiB");

			try {
				
				vm = com.sun.tools.attach.VirtualMachine.attach(jvmName.split("@")[0]);
				System.out.println("[MSG] Attaching native agent testjvmtiA");
				vm.loadAgentLibrary(agentA, null);
				System.out.println("[MSG] Attaching native agent testjvmtiB");
				vm.loadAgentLibrary(agentB, null);

				System.out.println("[MSG] Testing jep178 for native agents during Live phase (OnAttach)");

				vm.detach();
			} catch(com.sun.tools.attach.AttachNotSupportedException except) {
			} catch(com.sun.tools.attach.AgentInitializationException except) {
			} catch(com.sun.tools.attach.AgentLoadException except) {
			} catch(IOException except) {
			}
		} else {
			System.out.println("[MSG] Testing jep178 for native agents at JVM startup (OnLoad)");
		}
	}
}
