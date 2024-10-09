
package org.openj9.test.attachAPI;

import java.io.IOException;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

@SuppressWarnings("nls")
public class TestLoadAgentPath {
	
	public static void main(String[] args) {
		String agent = "";
		String target = "";
		String options = null;
		if (args.length < 1) {
			fail("TestLoadAgent target [agent [options]]");
		} else {
			target = args[0];
		}
		if (args.length > 1) {
			agent = args[1];
		}
		if (args.length > 2) {
			options = args[2];
		}
		try {
			VirtualMachine vm = VirtualMachine.attach(target);
			if (null == options) {
				vm.loadAgentPath(agent);
			} else {
				vm.loadAgentPath(agent, options);
			}
			vm.detach();
		} catch (AttachNotSupportedException e) {
			fail("AttachNotSupportedException");
		} catch (IOException e) {
			fail("IOException");
		} catch (AgentLoadException e) {
			fail(e.toString());
		} catch (AgentInitializationException e) {
			fail(e.toString());
		}
	}

	private static void fail(String msg) {
		System.err.println(msg);
		java.lang.System.exit(1);
	}

}
