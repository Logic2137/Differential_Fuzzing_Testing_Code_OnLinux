import java.security.AccessController;
import java.security.Permission;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.security.auth.Subject;

public class Modules {

    private final static Permission[] perms = new Permission[] { new java.io.SerializablePermission("enableSubstitution"), new java.lang.reflect.ReflectPermission("suppressAccessChecks"), new java.nio.file.LinkPermission("hard"), new javax.net.ssl.SSLPermission("getSSLSessionContext"), new javax.security.auth.AuthPermission("doAsPrivileged"), new javax.security.auth.PrivateCredentialPermission("* * \"*\"", "read"), new java.awt.AWTPermission("createRobot"), new javax.sound.sampled.AudioPermission("play"), new java.util.logging.LoggingPermission("control", ""), new java.lang.management.ManagementPermission("control"), new javax.management.MBeanPermission("*", "getAttribute"), new javax.management.MBeanServerPermission("createMBeanServer"), new javax.management.MBeanTrustPermission("register"), new javax.management.remote.SubjectDelegationPermission("*"), new javax.security.auth.kerberos.DelegationPermission("\"*\" \"*\""), new javax.security.auth.kerberos.ServicePermission("*", "accept"), new java.sql.SQLPermission("setLog"), new javax.smartcardio.CardPermission("*", "*"), new com.sun.tools.attach.AttachPermission("attachVirtualMachine"), new com.sun.jdi.JDIPermission("virtualMachineManager"), new com.sun.security.jgss.InquireSecContextPermission("*") };

    private final static Principal[] princs = new Principal[] { new javax.security.auth.x500.X500Principal("CN=Duke"), new javax.management.remote.JMXPrincipal("Duke"), new javax.security.auth.kerberos.KerberosPrincipal("duke@openjdk.org"), new com.sun.security.auth.UserPrincipal("Duke"), new com.sun.security.auth.NTDomainPrincipal("openjdk.org"), new com.sun.security.auth.NTSid("S-1-5-21-3623811015-3361044348-30300820-1013"), new com.sun.security.auth.NTUserPrincipal("Duke"), new com.sun.security.auth.UnixNumericUserPrincipal("0"), new com.sun.security.auth.UnixPrincipal("duke") };

    public static void main(String[] args) throws Exception {
        for (Permission perm : perms) {
            AccessController.checkPermission(perm);
        }
        Permission princPerm = new java.util.PropertyPermission("user.home", "read");
        Set<Principal> princSet = new HashSet<>(Arrays.asList(princs));
        Subject subject = new Subject(true, princSet, Collections.emptySet(), Collections.emptySet());
        PrivilegedAction<Void> pa = () -> {
            AccessController.checkPermission(princPerm);
            return null;
        };
        Subject.doAsPrivileged(subject, pa, null);
    }
}
