
package jaxp.library;

import java.net.URL;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.security.SecurityPermission;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyPermission;
import java.util.Set;
import java.util.StringJoiner;

public class JAXPPolicyManager {

    private Policy policyBackup;

    private SecurityManager smBackup;

    private TestPolicy policy = new TestPolicy();

    private static JAXPPolicyManager policyManager = null;

    private JAXPPolicyManager() {
        policyBackup = Policy.getPolicy();
        smBackup = System.getSecurityManager();
        setDefaultPermissions();
        Policy.setPolicy(policy);
        System.setSecurityManager(new SecurityManager());
    }

    static synchronized JAXPPolicyManager getJAXPPolicyManager(boolean createIfNone) {
        if (policyManager == null & createIfNone)
            policyManager = new JAXPPolicyManager();
        return policyManager;
    }

    private void teardown() throws Exception {
        System.setSecurityManager(smBackup);
        Policy.setPolicy(policyBackup);
    }

    static synchronized void teardownPolicyManager() throws Exception {
        if (policyManager != null) {
            policyManager.teardown();
            policyManager = null;
        }
    }

    private void setDefaultPermissions() {
        addPermission(new SecurityPermission("getPolicy"));
        addPermission(new SecurityPermission("setPolicy"));
        addPermission(new RuntimePermission("setSecurityManager"));
        addPermission(new PropertyPermission("test.src", "read"));
    }

    void addPermission(Permission p) {
        policy.addPermission(p);
    }

    int addTmpPermission(Permission p) {
        return policy.addTmpPermission(p);
    }

    void setAllowAll(boolean allow) {
        policy.setAllowAll(allow);
    }

    void removeTmpPermission(int index) {
        policy.removeTmpPermission(index);
    }
}

class TestPolicy extends Policy {

    private final static Set<String> TEST_JARS = Set.of("jtreg.jar", "javatest.jar", "testng.jar", "jcommander.jar");

    private final PermissionCollection permissions = new Permissions();

    private ThreadLocal<Map<Integer, Permission>> transientPermissions = new ThreadLocal<>();

    private ThreadLocal<Boolean> allowAll = new ThreadLocal<>();

    private static Policy defaultPolicy = Policy.getPolicy();

    void addPermission(Permission p) {
        permissions.add(p);
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("\n", "policy: ", "");
        Enumeration<Permission> perms = permissions.elements();
        while (perms.hasMoreElements()) {
            sj.add(perms.nextElement().toString());
        }
        return sj.toString();
    }

    @Override
    public PermissionCollection getPermissions(ProtectionDomain domain) {
        return permissions;
    }

    @Override
    public PermissionCollection getPermissions(CodeSource codesource) {
        return permissions;
    }

    private boolean isTestMachineryDomain(ProtectionDomain domain) {
        CodeSource cs = (domain == null) ? null : domain.getCodeSource();
        URL loc = (cs == null) ? null : cs.getLocation();
        String path = (loc == null) ? null : loc.getPath();
        return path != null && TEST_JARS.stream().filter(path::endsWith).findAny().isPresent();
    }

    @Override
    public boolean implies(ProtectionDomain domain, Permission perm) {
        if (allowAll())
            return true;
        if (defaultPolicy.implies(domain, perm))
            return true;
        if (permissions.implies(perm))
            return true;
        if (isTestMachineryDomain(domain))
            return true;
        return tmpImplies(perm);
    }

    int addTmpPermission(Permission p) {
        Map<Integer, Permission> tmpPermissions = transientPermissions.get();
        if (tmpPermissions == null)
            tmpPermissions = new HashMap<>();
        int id = tmpPermissions.size();
        tmpPermissions.put(id, p);
        transientPermissions.set(tmpPermissions);
        return id;
    }

    void removeTmpPermission(int index) {
        try {
            Map<Integer, Permission> tmpPermissions = transientPermissions.get();
            tmpPermissions.remove(index);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new RuntimeException("Tried to delete a non-existent temporary permission", e);
        }
    }

    private boolean tmpImplies(Permission perm) {
        Map<Integer, Permission> tmpPermissions = transientPermissions.get();
        if (tmpPermissions != null) {
            for (Permission p : tmpPermissions.values()) {
                if (p.implies(perm))
                    return true;
            }
        }
        return false;
    }

    private boolean allowAll() {
        Boolean allow = allowAll.get();
        if (allow != null) {
            return allow;
        }
        return false;
    }

    void setAllowAll(boolean allow) {
        allowAll.set(allow);
    }
}
