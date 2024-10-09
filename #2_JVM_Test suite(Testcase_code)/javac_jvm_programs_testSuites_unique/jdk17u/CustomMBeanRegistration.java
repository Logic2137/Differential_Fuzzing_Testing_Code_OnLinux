
package nsk.monitoring.share;

import javax.management.*;

class CustomMBeanRegistration implements MBeanRegistration {

    public void postDeregister() {
    }

    public void postRegister(Boolean registrationDone) {
    }

    public void preDeregister() {
    }

    public ObjectName preRegister(MBeanServer server, ObjectName name) {
        return name;
    }
}
