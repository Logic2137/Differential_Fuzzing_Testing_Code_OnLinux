import java.beans.ConstructorProperties;
import javax.management.ConstructorParameters;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

public class LegacyConstructorPropertiesTest {

    public static class CustomType {

        private String name;

        private int value;

        @ConstructorProperties({ "name", "value" })
        public CustomType(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @ConstructorProperties("noname")
        @ConstructorParameters("name")
        public CustomType(String name) {
            this.name = name;
            this.value = -1;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public static interface CustomMXBean {

        public CustomType getProp();

        public void setProp(CustomType prop);
    }

    public static final class Custom implements CustomMXBean {

        private CustomType prop;

        @Override
        public CustomType getProp() {
            return prop;
        }

        @Override
        public void setProp(CustomType prop) {
            this.prop = prop;
        }
    }

    public static void main(String[] args) throws Exception {
        MBeanServer mbs = MBeanServerFactory.createMBeanServer();
        CustomMXBean mbean = new Custom();
        mbs.registerMBean(mbean, ObjectName.getInstance("test:type=Custom"));
    }
}
