



import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ReflectPermission;
import java.security.Permission;
import java.util.HashSet;
import java.util.Iterator;
import javax.management.Descriptor;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanPermission;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeMBeanException;


public class DcmdMBeanPermissionsTest {

    private static String HOTSPOT_DIAGNOSTIC_MXBEAN_NAME =
        "com.sun.management:type=DiagnosticCommand";

    static public class CustomSecurityManager extends SecurityManager {

        private HashSet<Permission> grantedPermissions;

        public CustomSecurityManager() {
            grantedPermissions = new HashSet<Permission>();
        }

        public final void grantPermission(final Permission perm) {
            grantedPermissions.add(perm);
        }

        public final void denyPermission(final Permission perm) {
            Iterator<Permission> it = grantedPermissions.iterator();
            while (it.hasNext()) {
                Permission p = it.next();
                if (p.equals(perm)) {
                    it.remove();
                }
            }
        }

        public final void checkPermission(final Permission perm) {
            for (Permission p : grantedPermissions) {
                if (p.implies(perm)) {
                    return;
                }
            }
            throw new SecurityException(perm.toString());
        }
    };

    static Permission createPermission(String classname, String name,
            String action) {
        Permission permission = null;
        try {
            Class c = Class.forName(classname);
            if (action == null) {
                try {
                    Constructor constructor = c.getConstructor(String.class);
                    permission = (Permission) constructor.newInstance(name);

                } catch (InstantiationException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException ex) {
                    ex.printStackTrace();
                    throw new RuntimeException("TEST FAILED");
                }
            }
            if (permission == null) {
                try {
                    Constructor constructor = c.getConstructor(String.class,
                            String.class);
                    permission = (Permission) constructor.newInstance(
                            name,
                            action);
                } catch (InstantiationException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException ex) {
                    ex.printStackTrace();
                    throw new RuntimeException("TEST FAILED");
                }
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
                    throw new RuntimeException("TEST FAILED");
        }
        if (permission == null) {
            throw new RuntimeException("TEST FAILED");
        }
        return permission;
    }

    
    static boolean invokeOperation(MBeanServer mbs, ObjectName on,
            MBeanOperationInfo opInfo) {
        try {
            if (opInfo.getSignature().length == 0) {
                mbs.invoke(on, opInfo.getName(),
                        new Object[0], new String[0]);
            } else {
                mbs.invoke(on, opInfo.getName(),
                        new Object[1], new String[]{ String[].class.getName()});
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
            return true;
        } catch (RuntimeMBeanException ex) {
            if (ex.getCause() instanceof SecurityException) {
                
                return true;
            }
        } catch (MBeanException | InstanceNotFoundException
                | ReflectionException ex) {
            throw new RuntimeException("TEST FAILED");
        }
        return false;
    }

    static void testOperation(MBeanServer mbs, CustomSecurityManager sm,
            ObjectName on, MBeanOperationInfo opInfo) {
        System.out.println("Testing " + opInfo.getName());
        Descriptor desc = opInfo.getDescriptor();
        if (desc.getFieldValue("dcmd.permissionClass") == null) {
        
        
            if (invokeOperation(mbs, on, opInfo)) {
                throw new RuntimeException("TEST FAILED");
            }
        } else {
            
            Permission reqPerm = createPermission(
                    (String)desc.getFieldValue("dcmd.permissionClass"),
                    (String)desc.getFieldValue("dcmd.permissionName"),
                    (String)desc.getFieldValue("dcmd.permissionAction"));
            
            
            sm.denyPermission(reqPerm);
            
            
            
            if(!invokeOperation(mbs, on, opInfo)) {
                throw new RuntimeException("TEST FAILED");
            }
            
            sm.grantPermission(reqPerm);
            if(invokeOperation(mbs, on, opInfo)) {
                throw new RuntimeException("TEST FAILED");
            }
            
            sm.denyPermission(reqPerm);
        }
    }

    public static void main(final String[] args) {
        final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName on = null;
        try {
            on = new ObjectName(HOTSPOT_DIAGNOSTIC_MXBEAN_NAME);
        } catch (MalformedObjectNameException ex) {
            ex.printStackTrace();
            throw new RuntimeException("TEST FAILED");
        }
        MBeanInfo info = null;
        try {
            info = mbs.getMBeanInfo(on);
        } catch (InstanceNotFoundException | IntrospectionException
                | ReflectionException ex) {
            ex.printStackTrace();
            throw new RuntimeException("TEST FAILED");
        }
        CustomSecurityManager sm = new CustomSecurityManager();
        System.setSecurityManager(sm);
        
        
        
        
        
        
        
        
        
        
        sm.grantPermission(new RuntimePermission("createClassLoader"));
        sm.grantPermission(new ReflectPermission("suppressAccessChecks"));
        sm.grantPermission(new java.util.logging.LoggingPermission("control", ""));
        sm.grantPermission(new java.lang.RuntimePermission("exitVM.*"));
        sm.grantPermission(new java.lang.RuntimePermission("modifyThreadGroup"));
        sm.grantPermission(new java.lang.RuntimePermission("modifyThread"));
        sm.grantPermission(new java.security.SecurityPermission("getProperty.jdk.jar.disabledAlgorithms"));
        for(MBeanOperationInfo opInfo : info.getOperations()) {
            Permission opPermission = new MBeanPermission(info.getClassName(),
                    opInfo.getName(),
                    on,
                    "invoke");
            sm.grantPermission(opPermission);
            testOperation(mbs, sm, on, opInfo);
            sm.denyPermission(opPermission);
        }
        System.out.println("TEST PASSED");
    }
}
