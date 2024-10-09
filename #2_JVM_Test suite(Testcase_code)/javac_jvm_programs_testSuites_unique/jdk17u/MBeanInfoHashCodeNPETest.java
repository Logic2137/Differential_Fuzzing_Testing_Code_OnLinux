import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.modelmbean.DescriptorSupport;
import javax.management.openmbean.SimpleType;

public class MBeanInfoHashCodeNPETest {

    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("---MBeanInfoHashCodeNPETest-main ...");
        System.out.println("\n---Testing on MBeanAttributeInfo...");
        MBeanAttributeInfo mbeanAttributeInfo = new MBeanAttributeInfo(null, SimpleType.INTEGER.getClassName(), "description", true, true, false);
        test(mbeanAttributeInfo, "class name");
        mbeanAttributeInfo = new MBeanAttributeInfo("name", null, "description", true, true, false);
        test(mbeanAttributeInfo, "type");
        mbeanAttributeInfo = new MBeanAttributeInfo("name", SimpleType.INTEGER.getClassName(), null, true, true, false);
        test(mbeanAttributeInfo, "description");
        System.out.println("\n---Testing on MBeanConstructorInfo...");
        MBeanConstructorInfo mbeanConstructorInfo = new MBeanConstructorInfo(null, "", new MBeanParameterInfo[] {}, new DescriptorSupport());
        test(mbeanConstructorInfo, "name");
        mbeanConstructorInfo = new MBeanConstructorInfo("", null, new MBeanParameterInfo[] {}, new DescriptorSupport());
        test(mbeanConstructorInfo, "description");
        mbeanConstructorInfo = new MBeanConstructorInfo("", "", null, new DescriptorSupport());
        test(mbeanConstructorInfo, "MBeanParameterInfo");
        mbeanConstructorInfo = new MBeanConstructorInfo("", "", new MBeanParameterInfo[] {}, null);
        test(mbeanConstructorInfo, "descriptor");
        System.out.println("\n---Testing on MBeanOperationInfo...");
        MBeanOperationInfo mbeanOperationInfo = new MBeanOperationInfo(null, "description", new MBeanParameterInfo[] {}, "type", 1, new DescriptorSupport());
        test(mbeanOperationInfo, "name");
        mbeanOperationInfo = new MBeanOperationInfo("name", null, new MBeanParameterInfo[] {}, "type", 1, new DescriptorSupport());
        test(mbeanOperationInfo, "description");
        mbeanOperationInfo = new MBeanOperationInfo("name", "description", null, "type", 1, new DescriptorSupport());
        test(mbeanOperationInfo, "MBeanParameterInfo");
        mbeanOperationInfo = new MBeanOperationInfo("name", "description", new MBeanParameterInfo[] {}, null, 1, new DescriptorSupport());
        test(mbeanOperationInfo, "type");
        mbeanOperationInfo = new MBeanOperationInfo("name", "description", new MBeanParameterInfo[] {}, "type", 1, null);
        test(mbeanOperationInfo, "Descriptor");
        System.out.println("\n---Testing on MBeanParameterInfo...");
        MBeanParameterInfo mbeanParameterInfo = new MBeanParameterInfo(null, "type", "description", new DescriptorSupport());
        test(mbeanParameterInfo, "name");
        mbeanParameterInfo = new MBeanParameterInfo("name", null, "description", new DescriptorSupport());
        test(mbeanParameterInfo, "description");
        mbeanParameterInfo = new MBeanParameterInfo("name", "type", null, new DescriptorSupport());
        test(mbeanParameterInfo, "description");
        mbeanParameterInfo = new MBeanParameterInfo("name", "type", "description", null);
        test(mbeanParameterInfo, "Descriptor");
        System.out.println("\n---Testing on MBeanInfo...");
        String className = "toto";
        String description = "titi";
        MBeanAttributeInfo[] attrInfos = new MBeanAttributeInfo[] {};
        MBeanConstructorInfo[] constrInfos = new MBeanConstructorInfo[] {};
        MBeanOperationInfo[] operaInfos = new MBeanOperationInfo[] {};
        MBeanNotificationInfo[] notifInfos = new MBeanNotificationInfo[] {};
        MBeanInfo minfo = new MBeanInfo(null, description, attrInfos, constrInfos, operaInfos, notifInfos);
        test(minfo, "class name");
        minfo = new MBeanInfo(className, description, attrInfos, constrInfos, operaInfos, notifInfos);
        test(minfo, "name");
        minfo = new MBeanInfo(className, null, attrInfos, constrInfos, operaInfos, notifInfos);
        test(minfo, "description");
        minfo = new MBeanInfo(className, description, null, constrInfos, operaInfos, notifInfos);
        test(minfo, "attrInfos");
        minfo = new MBeanInfo(className, description, attrInfos, constrInfos, null, notifInfos);
        test(minfo, "operaInfos");
        minfo = new MBeanInfo(className, description, attrInfos, constrInfos, operaInfos, null);
        test(minfo, "notifInfos");
        Thread.sleep(100);
        if (failed > 0) {
            throw new RuntimeException("Test failed: " + failed);
        } else {
            System.out.println("---Test: PASSED");
        }
    }

    private static void test(Object obj, String param) {
        try {
            obj.hashCode();
            System.out.println("OK: " + obj.getClass().getSimpleName() + ".hashCode worked with a null " + param);
        } catch (NullPointerException npe) {
            System.out.println("--->KO!!! " + obj.getClass().getSimpleName() + ".hashCode got NPE with a null " + param);
            failed++;
        }
        try {
            obj.toString();
            System.out.println("OK: " + obj.getClass().getSimpleName() + ".toString worked with a null " + param);
        } catch (NullPointerException npe) {
            System.out.println("--->KO!!! " + obj.getClass().getSimpleName() + ".toString got NPE.");
            failed++;
        }
    }
}
