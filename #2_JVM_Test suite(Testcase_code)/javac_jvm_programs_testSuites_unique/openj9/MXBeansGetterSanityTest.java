package j9vm.test.mxbeans;



import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class MXBeansGetterSanityTest {
	private static void testMXBeanGetters(Object mxBean) {
		if (mxBean != null) {
			Class<?> mxBeanClass = mxBean.getClass();
			for (Method method : mxBeanClass.getMethods()) {
				String methodName = method.getName();
				if (methodName.startsWith("get") && method.getParameterTypes().length == 0) {
					try {
						System.out.println("Calling " + methodName + " on " + mxBeanClass.getName());
						method.invoke(mxBean);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						if (mxBeanClass.getName().equals("com.ibm.lang.management.internal.MemoryPoolMXBeanImpl")
								&& (methodName.equals("getCollectionUsageThreshold")
										|| methodName.equals("getCollectionUsageThresholdCount")
										|| methodName.equals("getUsageThreshold")
										|| methodName.equals("getUsageThresholdCount"))) {
							
						} else if (mxBeanClass.getName().equals("com.ibm.lang.management.internal.UnixExtendedOperatingSystem")
								&& methodName.equals("getHardwareModel")) {
							
						} else if (mxBeanClass.getName().equals("com.ibm.lang.management.internal.ExtendedRuntimeMXBeanImpl")
								&& methodName.equals("getBootClassPath")) {
							
						} else {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		testMXBeanGetters(ManagementFactory.getClassLoadingMXBean());
		testMXBeanGetters(ManagementFactory.getCompilationMXBean());
		for (GarbageCollectorMXBean mxBean : ManagementFactory.getGarbageCollectorMXBeans()) {
			testMXBeanGetters(mxBean);
		}
		for (MemoryManagerMXBean mxBean : ManagementFactory.getMemoryManagerMXBeans()) {
			testMXBeanGetters(mxBean);
		}
		testMXBeanGetters(ManagementFactory.getMemoryMXBean());
		for (MemoryPoolMXBean mxBean : ManagementFactory.getMemoryPoolMXBeans()) {
			testMXBeanGetters(mxBean);
		}
		testMXBeanGetters(ManagementFactory.getOperatingSystemMXBean());
		testMXBeanGetters(ManagementFactory.getRuntimeMXBean());
		testMXBeanGetters(ManagementFactory.getThreadMXBean());
	}
}
