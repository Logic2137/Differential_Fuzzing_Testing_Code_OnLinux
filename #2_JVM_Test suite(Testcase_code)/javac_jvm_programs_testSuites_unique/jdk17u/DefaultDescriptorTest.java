import java.lang.reflect.*;
import javax.management.*;
import javax.management.modelmbean.*;
import javax.management.openmbean.*;

public class DefaultDescriptorTest {

    public static void main(String[] args) throws Exception {
        final Descriptor empty = ImmutableDescriptor.EMPTY_DESCRIPTOR;
        final Class thisClass = DefaultDescriptorTest.class;
        final Method getWhatever = thisClass.getMethod("getWhatever");
        final Method setWhatever = thisClass.getMethod("setWhatever", int.class);
        final Method doWhatever = thisClass.getMethod("doWhatever", String.class);
        final Constructor<?> con = thisClass.getConstructor();
        final OpenMBeanParameterInfoSupport ombpi1 = new OpenMBeanParameterInfoSupport("x", "y", SimpleType.STRING);
        final OpenMBeanParameterInfoSupport ombpi2 = new OpenMBeanParameterInfoSupport("x", "y", SimpleType.STRING, (Descriptor) null);
        final OpenMBeanParameterInfoSupport ombpi3 = new OpenMBeanParameterInfoSupport("x", "y", SimpleType.STRING, empty);
        final OpenMBeanParameterInfoSupport ombpi4 = new OpenMBeanParameterInfoSupport("x", "y", SimpleType.STRING, "defaultString");
        final OpenMBeanParameterInfoSupport ombpi5 = new OpenMBeanParameterInfoSupport("x", "y", SimpleType.INTEGER, null, 3, 5);
        final OpenMBeanParameterInfoSupport ombpi6 = new OpenMBeanParameterInfoSupport("x", "y", SimpleType.LONG, 53L, new Long[] { 28L, 53L });
        final ModelMBeanInfoSupport mmbi1 = new ModelMBeanInfoSupport("yo", "yo", null, null, null, null);
        final ModelMBeanInfoSupport mmbi2 = new ModelMBeanInfoSupport("yo", "yo", null, null, null, null, (Descriptor) null);
        final ModelMBeanAttributeInfo mmbai1 = new ModelMBeanAttributeInfo("yo", "yo", getWhatever, setWhatever);
        final ModelMBeanAttributeInfo mmbai2 = new ModelMBeanAttributeInfo("yo", "yo", getWhatever, setWhatever, (Descriptor) null);
        final ModelMBeanAttributeInfo mmbai3 = new ModelMBeanAttributeInfo("yo", "yo", "yo", true, true, false);
        final ModelMBeanAttributeInfo mmbai4 = new ModelMBeanAttributeInfo("yo", "yo", "yo", true, true, false, (Descriptor) null);
        final ModelMBeanConstructorInfo mmbci1 = new ModelMBeanConstructorInfo("yo", con);
        final ModelMBeanConstructorInfo mmbci2 = new ModelMBeanConstructorInfo("yo", con, (Descriptor) null);
        final ModelMBeanConstructorInfo mmbci3 = new ModelMBeanConstructorInfo("yo", "yo", null);
        final ModelMBeanConstructorInfo mmbci4 = new ModelMBeanConstructorInfo("yo", "yo", null, (Descriptor) null);
        final ModelMBeanNotificationInfo mmbni1 = new ModelMBeanNotificationInfo(new String[] { "x.y.z" }, "yo", "yo");
        final ModelMBeanNotificationInfo mmbni2 = new ModelMBeanNotificationInfo(new String[] { "x.y.z" }, "yo", "yo", (Descriptor) null);
        final ModelMBeanOperationInfo mmboi1 = new ModelMBeanOperationInfo("yo", doWhatever);
        final ModelMBeanOperationInfo mmboi2 = new ModelMBeanOperationInfo("yo", doWhatever, (Descriptor) null);
        final ModelMBeanOperationInfo mmboi3 = new ModelMBeanOperationInfo("yo", "yo", null, "typo", MBeanOperationInfo.ACTION);
        final ModelMBeanOperationInfo mmboi4 = new ModelMBeanOperationInfo("yo", "yo", null, "typo", MBeanOperationInfo.ACTION, (Descriptor) null);
        final DescriptorRead[] infos = { new MBeanInfo("a.b.c", "blah", null, null, null, null), new MBeanInfo("a.b.c", "blah", null, null, null, null, (Descriptor) null), new MBeanInfo("a.b.c", "blah", null, null, null, null, empty), new MBeanAttributeInfo("blah", "blah", getWhatever, setWhatever), new MBeanAttributeInfo("blah", "a.b.c", "blah", true, true, false), new MBeanAttributeInfo("blah", "a.b.c", "blah", true, true, false, (Descriptor) null), new MBeanAttributeInfo("blah", "a.b.c", "blah", true, true, false, empty), new MBeanConstructorInfo("blah", con), new MBeanConstructorInfo("blah", "blah", null), new MBeanConstructorInfo("blah", "blah", null, (Descriptor) null), new MBeanConstructorInfo("blah", "blah", null, empty), new MBeanFeatureInfo("blah", "blah"), new MBeanFeatureInfo("blah", "blah", (Descriptor) null), new MBeanFeatureInfo("blah", "blah", empty), new MBeanNotificationInfo(new String[] { "a.b.c" }, "blah", "blah"), new MBeanNotificationInfo(new String[] { "a.b.c" }, "blah", "blah", (Descriptor) null), new MBeanNotificationInfo(new String[] { "a.b.c" }, "blah", "blah", empty), new MBeanOperationInfo("blah", doWhatever), new MBeanOperationInfo("blah", "blah", null, "a.b.c", MBeanOperationInfo.ACTION_INFO), new MBeanOperationInfo("blah", "blah", null, "a.b.c", MBeanOperationInfo.ACTION, (Descriptor) null), new MBeanOperationInfo("blah", "blah", null, "a.b.c", MBeanOperationInfo.INFO, empty), new MBeanParameterInfo("blah", "a.b.c", "blah"), new MBeanParameterInfo("blah", "a.b.c", "blah", (Descriptor) null), new MBeanParameterInfo("blah", "a.b.c", "blah", empty), new OpenMBeanInfoSupport("x", "y", null, null, null, null), new OpenMBeanInfoSupport("x", "y", null, null, null, null, (Descriptor) null), new OpenMBeanInfoSupport("x", "y", null, null, null, null, empty), ombpi1, ombpi2, ombpi3, ombpi4, ombpi5, ombpi6, new OpenMBeanAttributeInfoSupport("x", "y", SimpleType.STRING, true, true, false), new OpenMBeanAttributeInfoSupport("x", "y", SimpleType.STRING, true, true, false, (Descriptor) null), new OpenMBeanAttributeInfoSupport("x", "y", SimpleType.STRING, true, true, false, empty), new OpenMBeanAttributeInfoSupport("x", "y", SimpleType.STRING, true, true, false, "defaultString"), new OpenMBeanAttributeInfoSupport("x", "y", SimpleType.INTEGER, true, true, false, null, 3, 5), new OpenMBeanAttributeInfoSupport("x", "y", SimpleType.LONG, true, true, false, 53L, new Long[] { 28L, 53L }), new OpenMBeanConstructorInfoSupport("x", "y", new OpenMBeanParameterInfo[] { ombpi1, ombpi2 }), new OpenMBeanConstructorInfoSupport("x", "y", new OpenMBeanParameterInfo[] { ombpi1, ombpi2 }, (Descriptor) null), new OpenMBeanConstructorInfoSupport("x", "y", new OpenMBeanParameterInfo[] { ombpi1, ombpi2 }, empty), new OpenMBeanOperationInfoSupport("x", "y", new OpenMBeanParameterInfo[] { ombpi1, ombpi2 }, SimpleType.DATE, MBeanOperationInfo.ACTION), new OpenMBeanOperationInfoSupport("x", "y", new OpenMBeanParameterInfo[] { ombpi1, ombpi2 }, SimpleType.DATE, MBeanOperationInfo.ACTION, (Descriptor) null), new OpenMBeanOperationInfoSupport("x", "y", new OpenMBeanParameterInfo[] { ombpi1, ombpi2 }, SimpleType.DATE, MBeanOperationInfo.ACTION, empty), mmbi1, mmbi2, new ModelMBeanInfoSupport(mmbi1), new ModelMBeanInfoSupport(mmbi2), (DescriptorRead) mmbi1.clone(), (DescriptorRead) mmbi2.clone(), mmbai1, mmbai2, mmbai3, mmbai4, new ModelMBeanAttributeInfo(mmbai1), new ModelMBeanAttributeInfo(mmbai2), new ModelMBeanAttributeInfo(mmbai3), new ModelMBeanAttributeInfo(mmbai4), (DescriptorRead) mmbai1.clone(), (DescriptorRead) mmbai2.clone(), (DescriptorRead) mmbai3.clone(), (DescriptorRead) mmbai4.clone(), mmbci1, mmbci2, mmbci3, mmbci4, (DescriptorRead) mmbci1.clone(), (DescriptorRead) mmbci2.clone(), (DescriptorRead) mmbci3.clone(), (DescriptorRead) mmbci4.clone(), mmbni1, mmbni2, new ModelMBeanNotificationInfo(mmbni1), new ModelMBeanNotificationInfo(mmbni2), (DescriptorRead) mmbni1.clone(), (DescriptorRead) mmbni2.clone(), mmboi1, mmboi2, mmboi3, mmboi4, new ModelMBeanOperationInfo(mmboi1), new ModelMBeanOperationInfo(mmboi2), new ModelMBeanOperationInfo(mmboi3), new ModelMBeanOperationInfo(mmboi4), (DescriptorRead) mmboi1.clone(), (DescriptorRead) mmboi2.clone(), (DescriptorRead) mmboi3.clone(), (DescriptorRead) mmboi4.clone() };
        System.out.println("Testing that a default descriptor is always " + "supplied");
        for (DescriptorRead info : infos) {
            System.out.println(info);
            Descriptor d = info.getDescriptor();
            if (d == null)
                throw new Exception("getDescriptor returned null: " + info);
        }
        System.out.println("Test passed");
    }

    public int getWhatever() {
        return 0;
    }

    public void setWhatever(int x) {
    }

    public void doWhatever(String x) {
    }
}
