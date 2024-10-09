
package jit.test.jitt.codecache;


public class MethodObj {
	String optLevel ;
	String className ;
	String methodSignature ;
	String ccAddressRange ;
	String queueSize;
	String gc;
	String atlas;
	String time;
	String compThread;
	String mem;
	String profiling;
	boolean sync;
	boolean reclaimed;
	boolean recompiled;
	int codeCacheIndicator;
	public MethodObj(String optLevel, String className,
			String methodSignature,
			String ccAddressRange, String queueSize, String gc, String atlas,
			String time, String compThread, String mem, String profiling, boolean sync,boolean reclaimed,boolean recompiled) {
		this.optLevel = optLevel;
		this.className = className;
		this.methodSignature = methodSignature;
		this.ccAddressRange = ccAddressRange;
		this.queueSize = queueSize;
		this.gc = gc;
		this.atlas = atlas;
		this.time = time;
		this.compThread = compThread;
		this.mem = mem;
		this.sync = sync;
		this.profiling = profiling;
		this.reclaimed = reclaimed;
		this.recompiled = recompiled;
	}
	public boolean isRecompiled() {
		return recompiled;
	}
	public void setRecompiled(boolean recompiled) {
		this.recompiled = recompiled;
	}
	public boolean isReclaimed() {
		return reclaimed;
	}
	public void setReclaimed(boolean reclaimed) {
		this.reclaimed = reclaimed;
	}
	public String getOptLevel() {
		return optLevel;
	}
	public String getClassName() {
		return className;
	}
	public String getMethodSignature() {
		return methodSignature;
	}
	public String getCcAddressRange() {
		return ccAddressRange;
	}
	public String getQueueSize() {
		return queueSize;
	}
	public String getGc() {
		return gc;
	}
	public String getAtlas() {
		return atlas;
	}
	public String getTime() {
		return time;
	}
	public String getCompThread() {
		return compThread;
	}
	public String getMem() {
		return mem;
	}
	public boolean isSync() {
		return sync;
	}
	public String getProfiling() {
		return profiling;
	}

	public boolean isJitted() {
		return optLevel != null;
	}


	public int getCodeCacheIndicator() {
		return codeCacheIndicator;
	}

	public void setCodeCacheIndicator(int codeCacheIndicator) {
		this.codeCacheIndicator = codeCacheIndicator;
	}
	@Override
	public String toString() {
		return "LogMethodEntry [optLevel=" + optLevel + ", className="
				+ className + ", methodSignature=" + methodSignature
				+ ", ccAddressRange=" + ccAddressRange + ", queueSize="
				+ queueSize + ", gc=" + gc + ", atlas=" + atlas + ", time="
				+ time + ", compThread=" + compThread + ", mem=" + mem
				+ ", profiling=" + profiling + ", sync=" + sync
				+ ", reclaimed=" + reclaimed + ", recompiled=" + recompiled
				+ ", codeCacheIndicator=" + codeCacheIndicator + "]";
	}
}
