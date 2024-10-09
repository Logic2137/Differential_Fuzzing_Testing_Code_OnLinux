
package com.ibm.j9.numa;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;


public class LinuxNumaTest {

	
	private static final int ERR_BAD_PID_FORMAT = -1;
	private static final int ERR_FAILED_TO_SEND_KILL_SIGNAL = -2;
	private static final int ERR_NUMA_MAPS_NOT_FOUND = -3;
	private static final int ERR_NUMA_MAPS_IO_EXCEPTION = -4;
	private static final int ERR_JAVA_DUMP_NOT_FOUND = -5;
	private static final int ERR_CANT_PARSE_HEAP_ADDRESS = -6;
	private static final int ERR_INTERRUPTED = -7;
	private static final int ERR_JAVA_DUMP_IO_EXCEPTION = -8;
	private static final int ERR_UNKNOWN_HEAP_ADDRESS = -9;
	private static final int ERR_UNKNOWN_HEAP_POLICY = -10;
	private static final int ERR_PARSE_NUMA_REGION = -11;
	private static final int ERR_BAD_COMMAND_LINE = -12;
	private static final int ERR_PARSE_RANGE = -13;
	private static final int ERR_PARSE_NODE_ALLOCATED_PAGES = -14;
	private static final int ERR_CANT_READ_PID = -15;
	private static final int ERR_NUMA_MAPS_EXCEPTION = -16;
	private static final int ERR_PARSE_REGION_START_ADDR = -17;
	private static final int ERR_FAILED_TO_PARSE_SRC_NODE = -18;
	private static final int ERR_BAD_MULTIPLIER = -19;
	private static final int ERR_FAILED_TO_PARSE_DISTANCE = -20;
	public static final int ERR_FAILED_PARSE_POLICY_INFO = -21;
	private static final int ERR_TOO_FEW_NODES = -22;
	
	
	private static final String JAVACORE_TXT_FILENAME = "javacore.txt";

	
	private Long heapStart;

	
	private Long heapSize;

	
	public static class NumaNode {

		
		int id;

		
		private final List<Integer> cpuIds;

		
		private final Map<Integer, Integer> distances;

		
		private long totalMemory;

		
		private long freeMemory;

		
		public NumaNode() {
			cpuIds = new ArrayList<Integer>();
			distances = new HashMap<Integer, Integer>();
		}

		
		public int getDistance(int node) {
			return distances.get(node);
		}

		
		public long getTotalMemory() {
			return totalMemory;
		}

		
		public long getFreeMemory() {
			return freeMemory;
		}

		
		public int getId() {
			return id;
		}

		
		public void parse(BufferedReader br) throws IOException {

			
			
			String[] parts = br.readLine().split(" ");
			id = Integer.parseInt(parts[1]);
			for (int part = 3; part < parts.length; part++) {
				cpuIds.add(Integer.parseInt(parts[part]));
			}

			
			
			parts = br.readLine().split(" ");
			totalMemory = Integer.parseInt(parts[3]) * getSuffix(parts[4]);

			
			
			parts = br.readLine().split(" ");
			freeMemory = Integer.parseInt(parts[3]) * getSuffix(parts[4]);
		}

		
		private long getSuffix(String suffix) {

			long multiplier = 0;

			
			if (suffix.equalsIgnoreCase("GB")) {
				multiplier = 1024 * 1024 * 1024;
			} else if (suffix.equalsIgnoreCase("MB")) {
				multiplier = 1024 * 1024;
			} else if (suffix.equalsIgnoreCase("KB")) {
				multiplier = 1024;
			} else {
				System.err.println("Unknown metric multiplier " + suffix);
				System.exit(ERR_BAD_MULTIPLIER);
			}
			return multiplier;
		}

		
		public void parseDistances(String[] parts, List<Integer> srcNodeIds) {
			
			int part = 1;
			for (Integer srcNodeId : srcNodeIds) {
				String distStr = parts[part];
				if (!distStr.equals("")) {
					try {
						int distance = Integer.parseInt(distStr);
						distances.put(srcNodeId, distance);
					} catch (NumberFormatException nfe) {
						System.err.println("Failed to parse distance from '"
								+ distStr + "'");
						System.exit(ERR_FAILED_TO_PARSE_DISTANCE);
					}
				}
				part++;
			}
		}
	}

	
	public static class NumaMachine {

		
		private final Map<Integer, NumaNode> nodeMap;

		
		public NumaMachine() {
			nodeMap = new HashMap<Integer, NumaNode>();
		}

		
		public void dump(PrintWriter out) {
			out.print("nodes: " + nodeMap.size());
		}

		
		public void parse(BufferedReader br) throws IOException {

			
			String strLine = br.readLine();
			
			
			int numNodes = Integer.parseInt(strLine.split(" ")[1]);
			for (int i = 0; i < numNodes; i++) {

				
				NumaNode node = new NumaNode();
				node.parse(br);
				nodeMap.put(node.getId(), node);
			}

			strLine = br.readLine(); 

			
			
			strLine = br.readLine();
			String[] parts = strLine.split(" ");

			
			List<Integer> srcNodeIds = new ArrayList<Integer>();
			for (int part = 1; part < parts.length; part++) {
				
				if (!parts[part].equals("")) {
					String idStr = parts[part];
					try {
						int srcNodeId = Integer.parseInt(idStr);
						srcNodeIds.add(srcNodeId);
					} catch (NumberFormatException nfe) {
						System.err
								.println("Couldn't parse source nodeID from: '"
										+ idStr + "'");
						System.err.println("on line: " + strLine);
						System.err.println("part=" + part);
						System.exit(ERR_FAILED_TO_PARSE_SRC_NODE);
					}
				}
			}

			
			
			while ((strLine = br.readLine()) != null) {
				strLine = strLine.trim();
				parts = strLine.split(":");
				int destNodeId = Integer.parseInt(parts[0]);
				NumaNode numaNode = nodeMap.get(destNodeId);
				numaNode.parseDistances(parts[1].split(" "), srcNodeIds);
			}
		}
	}

	
	private static NumaMachine getHardware() {

		NumaMachine machine = new NumaMachine();
		String cmdLine = "numactl --hardware";
		try {
			System.out.println("Executing another process: " + cmdLine);
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec(cmdLine);
			InputStream in = pr.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			machine.parse(br);
			in.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.err.print("Failed to execute" + cmdLine + ", IOException");
			System.exit(ERR_FAILED_TO_SEND_KILL_SIGNAL);
		}
		return machine;
	}

	
	public static class NumaPolicy {

		
		private String policy;

		
		private String preferredNode;

		
		private final Set<Integer> physCpuBind = new HashSet<Integer>();

		private final Set<Integer> cpuBind = new HashSet<Integer>();
		private final Set<Integer> nodeBind = new HashSet<Integer>();
		private final Set<Integer> memBind = new HashSet<Integer>();

		
		public void parse(BufferedReader br) {
			try {
				
				
				policy = br.readLine().split(":")[1].trim();

				
				
				preferredNode = br.readLine().split(":")[1].trim();

				
				
				
				
				
				
				String[] parts = br.readLine().split(":")[1].trim().split(" ");
				for (String part : parts) {
					physCpuBind.add(Integer.parseInt(part));
				}

				
				
				parts = br.readLine().split(":")[1].trim().split(" ");
				for (String part : parts) {
					cpuBind.add(Integer.parseInt(part));
				}

				
				
				parts = br.readLine().split(":")[1].trim().split(" ");
				for (String part : parts) {
					nodeBind.add(Integer.parseInt(part));
				}

				
				
				parts = br.readLine().split(":")[1].trim().split(" ");
				for (String part : parts) {
					int node = Integer.parseInt(part);
					System.out.println("mem node: " + node);
					memBind.add(node);
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
				System.err.print("Failed to parse numa policy information.");
				System.exit(ERR_FAILED_PARSE_POLICY_INFO);
			}
		}

		
		public String getPolicy() {
			return policy;
		}

		
		public String getPreferredNode() {
			return preferredNode;
		}

		
		public Set<Integer> getPhysCpuBind() {
			return physCpuBind;
		}

		
		public Set<Integer> getCpuBind() {
			return cpuBind;
		}

		
		public Set<Integer> getNodeBind() {
			return nodeBind;
		}

		
		public Set<Integer> getMemBind() {
			return memBind;
		}
	}

	
	private static NumaPolicy getPolicy() {

		NumaPolicy policy = new NumaPolicy();
		String cmdLine = "numactl --show";
		try {
			System.out.println("Executing another process: " + cmdLine);
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec(cmdLine);
			InputStream in = pr.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			policy.parse(br);
			in.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.err.print("Failed to execute" + cmdLine + ", IOException");
			System.exit(ERR_FAILED_TO_SEND_KILL_SIGNAL);
		}
		return policy;
	}

	
	public static class VMRegion {

		
		private long startAddress;

		
		private String policy;

		
		private String policyRange;

		
		private final String line;

		
		private final Map<String, String> attributes;

		
		private final Set<String> flags;

		
		private long length;

		
		private final Set<Integer> policyNodeSet;

		
		private final Map<Integer, Integer> allocatedNodeMap;

		
		VMRegion(final String line) {

			policyNodeSet = new HashSet<Integer>();
			allocatedNodeMap = new HashMap<Integer, Integer>();

			this.line = line;

			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			

			
			String[] tokens = line.split(" ");

			try {
				
				startAddress = Long.parseLong(tokens[0].trim(), 16);
			} catch (NumberFormatException nfe) {
				System.err.println("Can't parse vm region start address: "
						+ tokens[0].trim());
				System.exit(ERR_PARSE_REGION_START_ADDR);
			}

			
			
			parsePolicyAndNodeMask(tokens[1].trim());

			
			attributes = new HashMap<String, String>();
			flags = new HashSet<String>();
			for (int i = 2; i < tokens.length; i++) {

				
				String[] pieces = tokens[i].trim().split("=");
				if (pieces.length == 1) {
					addFlag(pieces[0]);
				} else if (pieces.length == 2) {
					addAttribute(pieces[0], pieces[1]);
				} else {
					System.err
							.println("Error while parsing numa_maps, don't understand token:"
									+ tokens[i]);
					System.exit(ERR_PARSE_NUMA_REGION);
				}
			}
		}

		
		private void parsePolicyAndNodeMask(String token) {
			
			
			
			String[] policyTokens;
			if (token.contains("=")) {
				policyTokens = token.split("=");
			} else {
				policyTokens = token.split(":");
			}
			policy = policyTokens[0].trim();
			if (policyTokens.length < 2) {
				policyRange = "";
			} else {
				policyRange = policyTokens[1].trim();
				policyNodeSet.addAll(parseRange(policyRange));
			}
		}

		
		private void addAttribute(String name, String value) {

			
			if (('N' == name.charAt(0)) && Character.isDigit(name.charAt(1))) {
				try {
					int node = Integer.parseInt(name.substring(1), 10);
					int allocatedPages = Integer.parseInt(value, 10);
					addAllocatedNode(node, allocatedPages);
				} catch (NumberFormatException nfe) {
					System.err.println("Can't parse node range: " + name + "="
							+ value);
					System.exit(ERR_PARSE_NODE_ALLOCATED_PAGES);
				}
			}
			
			attributes.put(name, value);
		}

		
		private void addAllocatedNode(int node, int allocatedPages) {
			allocatedNodeMap.put(node, allocatedPages);
		}

		
		private boolean isNodeInPolicyRange(int node) {
			if (policyNodeSet.isEmpty()) {
				
				return true;
			}
			return policyNodeSet.contains(node);
		}

		
		public boolean isPolicyRespected() {

			if (policyNodeSet.isEmpty()) {
				
				return true;
			}

			
			for (int node : allocatedNodeMap.keySet()) {
				if (!isNodeInPolicyRange(node)) {
					return false;
				}
			}

			return true;
		}

		
		private void addFlag(String flag) {
			
			flags.add(flag);
		}

		
		@Override
		public String toString() {
			return getLine();
		}

		
		public boolean isAnon() {
			String value = attributes.get("anon");
			return value != null;
		}

		
		public boolean isFile() {
			String value = attributes.get("file");
			return value != null;
		}

		
		public long getStartAddress() {
			return startAddress;
		}

		
		public String getLine() {
			return line;
		}

		
		public long getLength() {
			return length;
		}

		
		public void setLength(long length) {
			this.length = length;
		}

		
		public String getPolicy() {
			return policy;
		}

		
		public String getPolicyRange() {
			return policyRange;
		}

		public Set<Integer> getPolicyNodeSet() {
			return policyNodeSet;
		}

		
		public Map<String, String> getAttributes() {
			return attributes;
		}

		
		public Set<String> getFlags() {
			return flags;
		}

		
		public Map<Integer, Integer> getAllocatedNodeMap() {
			return allocatedNodeMap;
		}
	}

	
	public static class NumaMap {

		
		private final SortedMap<Long, VMRegion> regionMap = new TreeMap<Long, VMRegion>();

		
		private VMRegion last = null;

		
		public NumaMap() {
			clearAndResetMap();
		}

		
		public void clearAndResetMap() {
			last = null;
			regionMap.clear();
		}

		
		public void readNumaMapsFile(File numa_maps_file) throws Exception {

			clearAndResetMap();

			FileInputStream fstream_numaMaps = new FileInputStream(
					numa_maps_file);
			DataInputStream in_numaMaps = new DataInputStream(fstream_numaMaps);
			BufferedReader br_numaMaps = new BufferedReader(
					new InputStreamReader(in_numaMaps));

			
			String strLine;
			while ((strLine = br_numaMaps.readLine()) != null) {
				try {
					
					VMRegion vmr = new VMRegion(strLine);
					addRegion(vmr);
					if (!vmr.isPolicyRespected()) {
						
						
						
						
						
						System.out.println("WARNING: policy not respected - "
								+ vmr.getLine());
					}
				} catch (Exception e) {
					System.err
							.println("Couldn't parse this line in numa_maps:");
					System.err.println(strLine);
					throw e;
				}
			}
			in_numaMaps.close();
		}

		
		public void addRegion(VMRegion region) {
			regionMap.put(region.getStartAddress(), region);
			if (last != null) {
				
				long length = region.getStartAddress() - last.getStartAddress();
				last.setLength(length);
			}
			last = region;
		}

		
		public Collection<VMRegion> getRegions() {
			return regionMap.values();
		}

		
		public VMRegion getRegionByStartAddress(long startAddr) {
			VMRegion region = regionMap.get(startAddr);
			if (region == null) {
				throw new IllegalArgumentException(
						"Can't find region starting at 0x"
								+ Long.toHexString(startAddr));
			}
			return region;
		}

	}

	
	public static void main(String[] args) {

		
		if (args.length == 0) {
			System.out.println("for usage, use --help");
			System.out.println("Using default arguments.");
		}

		if ((args.length == 1) && (args[0].equalsIgnoreCase("--help"))) {
			printUsage();
			System.exit(0);
		}

		System.out.println("Begining heap memory policy test...");

		
		
		String expectedHeapPolicy = null;
		String expectedPolicyNodeRange = null;
		if (args.length >= 1) {
			expectedHeapPolicy = args[0];
		}
		if (args.length >= 2) {
			expectedPolicyNodeRange = args[1];
		}
		new LinuxNumaTest().run(expectedHeapPolicy, expectedPolicyNodeRange);
	}

	
	private static void printUsage() {
		System.out.println("Use:");
		System.out
				.println("    java -Xdump:java:events=user,file=javacore.txt LinuxNumaTest [expectedHeapPolicy] [expectedPolicyNodeRange]");
		System.out.println("            or");
		System.out
				.println("    java LinuxNumaTest --help           (displays this message)");
		System.out.println();
		System.out.println("    [expectedHeapPolicy]");
		System.out
				.println("        Optional. The expected NUMA policy for the heap as");
		System.out.println("        found by parsing numa_maps.");
		System.out
				.println("        If not specified, default policy from numactl --show is used.");
		System.out.println("        Possible values:");
		System.out
				.println("            default    - use existing process or system policy.");
		System.out
				.println("            preferred  - 1st from preferred node, others if necessary.");
		System.out
				.println("            bind       - restrict allocation to available nodes.");
		System.out
				.println("            interleave - round robin allocation from available nodes.");
		System.out.println();
		System.out.println("    [expectedPolicyNodeRange]");
		System.out
				.println("        Optional. The expected range of nodes for the heap policy, e.g '0'");
		System.out
				.println("        for'interleave:0', or '0-3' for 'interleave:0-3'. Use 'NONE' for");
		System.out
				.println("        no range, e.g. 'default' Use commas to separate range bounds,");
		System.out.println("        e.g. '0,2-3'");
		System.out
				.println("        If not specified, default node range from numactl --show is used.");
		System.out.println();
	}

	
	private synchronized void run(String expectedHeapPolicy,
			String expectedPolicyNodeRange) {

		
		System.out.println("Checking for old java dump: "
				+ JAVACORE_TXT_FILENAME);
		File oldJavaDumpFile = new File(JAVACORE_TXT_FILENAME);
		if (oldJavaDumpFile != null) {
			System.out.println("deleting old java dump: "
					+ JAVACORE_TXT_FILENAME);
			oldJavaDumpFile.delete();
		}

		
		int pid = getPidOfCurrentProc();

		
		NumaMap numa_map = parseNumaMapsFile(pid);

		
		System.out.println("Sending kill signal to generate java dump");
		sendKillSignal(pid);

		
		NumaPolicy policy = getPolicy();
		if (policy.getNodeBind().size() < 2) {
			System.err.println("Machine CAPABILITY error, this test only runs on NUMA machines with 2 or more nodes.");
			System.err.println("Confirm number of nodes with numactl --show or numactl --hardware");
			System.err.println("or remove capability 'numa' from this machine");
			System.exit(ERR_TOO_FEW_NODES);
		}
		if (expectedHeapPolicy == null) {
			
			expectedHeapPolicy = policy.getPolicy();
		}

		
		
		System.out
				.println("Sleeping to allow exec'ed process to send signal and generate java dump");
		sleep(5 * 1000);

		
		parseJavaDump(JAVACORE_TXT_FILENAME);

		
		if (heapStart == null) {
			System.err.println("heap address unknown, aborting");
			System.exit(ERR_UNKNOWN_HEAP_ADDRESS);
		}

		System.out.println("----------------------------------------------");
		System.out.println("preferred node: " + policy.getPreferredNode());
		System.out.println("policy: " + policy.getPolicy());
		for (Integer memnode : policy.getMemBind()) {
			System.out.println("MemNode " + memnode);
		}
		System.out.println("----------------------------------------------");
		System.out.println("Comparing expected vs. actual results");

		
		VMRegion heapRegion = numa_map.getRegionByStartAddress(heapStart);
		if (heapRegion == null) {
			System.err.println("heap not found in numa_maps, aborting");
			System.exit(ERR_UNKNOWN_HEAP_POLICY);
		}
		System.out.println("numa_maps line for heap:");
		System.out.println(heapRegion.getLine());

		System.out.println("Heap memory    size, from java dump: 0x"
				+ Long.toHexString(heapSize));
		System.out.println("Heap VM region size, from numa_maps: 0x"
				+ Long.toHexString(heapRegion.getLength()));

		
		String heapPolicy = heapRegion.getPolicy();
		System.out.println("Actual   policy: " + heapPolicy);
		System.out.println("Expected policy: " + expectedHeapPolicy);
		boolean policyTestPassed = comparePolicies(expectedHeapPolicy,
				heapPolicy);

		
		String heapPolicyRange = heapRegion.getPolicyRange();
		System.out.println("Actual   range: " + heapPolicyRange);

		
		Set<Integer> expectedPolicyNodeSet;
		if (expectedPolicyNodeRange == null) {
			
			expectedPolicyNodeSet = policy.getMemBind();
		} else {
			
			expectedPolicyNodeSet = parseRange(expectedPolicyNodeRange);
		}

		
		boolean nodemaskTestPassed = false;
		Set<Integer> policyNodeSet = heapRegion.getPolicyNodeSet();
		if (expectedHeapPolicy.equalsIgnoreCase("bind")
				|| expectedHeapPolicy.equalsIgnoreCase("interleave")) {
			System.out.println("Expected range: " + expectedPolicyNodeRange);

			System.out.println("heap     policy nodes = "+ policyNodeSet);
			System.out.println("expected policy nodes = "+ expectedPolicyNodeSet);
			if (expectedPolicyNodeSet.equals(policyNodeSet)) {
				nodemaskTestPassed = true;
			} else {
				System.out.println("POLICY NODEMASK MISMATCH");
			}
		} else if (expectedHeapPolicy.equalsIgnoreCase("default")){
			System.out.println("heap     policy nodes = "+ policyNodeSet);
			if ((null == policyNodeSet) || (policyNodeSet.size() == 0)) {
				nodemaskTestPassed = true;
			}
		}

		
		if (!heapRegion.isPolicyRespected()) {
			System.out.println("WARNING: heap policy not respected - "
					+ heapRegion.getLine());
		}

		
		if (policyTestPassed && nodemaskTestPassed) {
			System.out.println("TEST PASSED");
		} else {
			System.out.println("TEST FAILED");
		}
	}

	
	private boolean comparePolicies(String expectedHeapPolicy,
			String actualHeapPolicy) {

		
		if (actualHeapPolicy.equals(expectedHeapPolicy)) {
			return true;
		}

		
		
		if (expectedHeapPolicy.equalsIgnoreCase("bind")
				&& actualHeapPolicy.equalsIgnoreCase("interleave")) {
			return true;
		}

		System.out.println("POLICY MISMATCH");
		return false;
	}

	
	private void parseJavaDump(String javacore_txt_filename) {
		String strLine = "<initialized>";
		try {
			System.out.println("Parsing java dump: " + javacore_txt_filename);
			File javacore = new File(javacore_txt_filename);
			FileInputStream fstream_javacore = new FileInputStream(javacore);
			DataInputStream in_javacore = new DataInputStream(fstream_javacore);
			BufferedReader br_javacore = new BufferedReader(
					new InputStreamReader(in_javacore));

			
			while ((strLine = br_javacore.readLine()) != null) {

				
				String[] tokens = strLine.split(" ");

				
				if (tokens[0].equals("1STHEAPREGION")) {
					System.out.println(javacore_txt_filename
							+ " line describing heap:");
					System.out.println(strLine);

					if (heapStart == null) {
						heapStart = Long.parseLong(tokens[3].substring(2), 16);
						System.out.println("heap start="
								+ Long.toHexString(heapStart));
					}

					
					continue;

				} else if (tokens[0].equals("1STHEAPTOTAL")) {

					System.out.println(javacore_txt_filename
							+ " line describing heap size:");
					System.out.println(strLine);

					if (heapSize == null) {
						String heapSizeStr = strLine.substring(30).trim()
								.split(" ")[0];
						heapSize = Long.parseLong(heapSizeStr);
						System.out.println("heap size="
								+ Long.toHexString(heapStart));
					}

					
					break;
				}
			}
			in_javacore.close();
			System.out.println("Completed parsing java dump");
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			System.err.println("Can't find file " + javacore_txt_filename);
			System.exit(ERR_JAVA_DUMP_NOT_FOUND);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			System.err.println("Can't parse heap address");
			System.err.println(strLine);
			System.exit(ERR_CANT_PARSE_HEAP_ADDRESS);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.err.println("Can't read file " + javacore_txt_filename);
			System.exit(ERR_JAVA_DUMP_IO_EXCEPTION);
		}
	}

	
	private NumaMap parseNumaMapsFile(int pid) {
		NumaMap numa_map = new NumaMap();
		try {
			String numa_maps_path = "/proc/" + pid + "/numa_maps";
			File numa_maps_file = new File(numa_maps_path);
			numa_map.readNumaMapsFile(numa_maps_file);
			System.out.println("Completed parsing " + numa_maps_path);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			System.err.println("Can't find numa_maps file");
			System.exit(ERR_NUMA_MAPS_NOT_FOUND);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.err.println("Can't read numa_maps file");
			System.exit(ERR_NUMA_MAPS_IO_EXCEPTION);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Can't parse numa_maps file");
			System.exit(ERR_NUMA_MAPS_EXCEPTION);
		}
		return numa_map;
	}

	
	private int getPidOfCurrentProc() {
		int pid = 0;
		try {
			System.out.println("Getting PID of current process");
			pid = Integer.parseInt(new File("/proc/self").getCanonicalFile()
					.getName());
			System.out.println("JVM process pid=" + pid);
			return pid;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.err
					.println("Can't read PID from /proc/self canonical file name");
			System.exit(ERR_CANT_READ_PID);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			System.err
					.println("Can't parse PID from /proc/self canonical file name");
			System.exit(ERR_BAD_PID_FORMAT);
		}
		return pid;
	}

	
	private void sleep(int delayInMillis) {
		try {
			System.out.println("Sleeping for " + delayInMillis
					+ " milliseconds");
			wait(delayInMillis);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.err
					.println("Interrupted while waiting for kill process signal to be sent");
			System.exit(ERR_INTERRUPTED);
		}
	}

	
	private void sendKillSignal(int pid) {

		try {
			
			
			String cmdLine = "kill -QUIT " + pid;
			System.out.println("Executing another process: " + cmdLine);
			Runtime rt = Runtime.getRuntime();
			@SuppressWarnings("unused")
			Process pr = rt.exec(cmdLine);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.err.print("Failed to send kill signal, IOException");
			System.exit(ERR_FAILED_TO_SEND_KILL_SIGNAL);
		}
	}

	
	private static Set<Integer> parseRange(String rangeStr) {

		Set<Integer> result = new HashSet<Integer>();

		
		String[] tokens = rangeStr.trim().split(",");

		
		for (String token : tokens) {

			
			String[] rangeBounds = token.split("-");
			try {
				switch (rangeBounds.length) {
				case 1: 
					result.add(Integer.parseInt(rangeBounds[0], 10));
					break;
				case 2: 
					int startNode = Integer.parseInt(rangeBounds[0], 10);
					int endNode = Integer.parseInt(rangeBounds[1], 10);
					for (int node = startNode; node <= endNode; node++) {
						result.add(node);
					}
					break;
				default: 
					throw new NumberFormatException("Can't parse range: "
							+ token + " from " + rangeStr);
				}
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
				System.err.println("Can't parse range: " + token + " from "
						+ rangeStr);
				System.exit(ERR_PARSE_RANGE);
			}
		}
		return result;
	}
}
