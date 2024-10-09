

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.request.MethodEntryRequest;

import java.security.AlgorithmParameters;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;





public class ECDSAJavaVerify {

    static final String[] ALL_ALGS = new String[] {
            "SHA1withECDSA", "SHA256withECDSA", "SHA384withECDSA", "SHA512withECDSA"};

    static final String[] ALL_CURVES = new String[] {
            "secp256r1", "secp384r1", "secp521r1"};

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            
            debug();
        } else if (args.length == 3) {
            
            new Test().run(Integer.parseInt(args[0]), args[1], args[2]);
        } else {
            
            Test t = new Test();
            Random r = new Random();

            for (String sigAlg : ALL_ALGS) {
                for (String curve : ALL_CURVES) {
                    t.run(r.nextInt(1000000), sigAlg, curve);
                }
            }
        }
    }

    static void debug() throws Exception {

        LaunchingConnector launchingConnector = Bootstrap
                .virtualMachineManager().defaultConnector();

        Map<String, Connector.Argument> arguments
                = launchingConnector.defaultArguments();
        arguments.get("main").setValue(ECDSAJavaVerify.class.getName());
        arguments.get("options").setValue(
                "-cp " + System.getProperty("test.classes"));
        VirtualMachine vm = launchingConnector.launch(arguments);

        MethodEntryRequest req = vm.eventRequestManager()
                .createMethodEntryRequest();
        req.addClassFilter("sun.security.ec.ECDSASignature");
        req.enable();

        int numberOfTests = ALL_ALGS.length * ALL_CURVES.length * 2;

        
        char[] expected = new char[numberOfTests];

        int pos = 0;
        for (String dummy : ALL_ALGS) {
            for (String curve : ALL_CURVES) {
                char caller = 'J';
                
                expected[pos++] = caller;
                expected[pos++] = caller;
            }
        }

        
        
        
        
        
        
        
        char[] result = new char[numberOfTests];
        Arrays.fill(result, '.');

        String stdout, stderr;

        try {
            EventSet eventSet;
            pos = -1; 
            while ((eventSet = vm.eventQueue().remove()) != null) {
                for (Event event : eventSet) {
                    if (event instanceof MethodEntryEvent) {
                        MethodEntryEvent e = (MethodEntryEvent)event;
                        switch (e.method().name()) {
                            case "engineVerify":
                                result[++pos] = '-';
                                break;
                            case "verifySignedDigestImpl": 
                                result[pos] = expected[pos] != 'J' ? 'x' : 'v';
                                break;
                        }
                    }
                    vm.resume();
                }
            }
        } catch (VMDisconnectedException e) {
            System.out.println("Virtual Machine is disconnected.");
        } finally {
            stderr = new String(vm.process().getErrorStream().readAllBytes());
            stdout = new String(vm.process().getInputStream().readAllBytes());
        }

        int exitCode = vm.process().waitFor();
        System.out.println("  exit: " + exitCode);
        System.out.println("stderr:\n" + stderr);
        System.out.println("stdout:\n" + stdout);

        String sResult = new String(result);

        System.out.println(" Cases: " + new String(expected));
        System.out.println("Result: " + sResult);

        if (pos != numberOfTests - 1 || sResult.contains("x")
                || sResult.contains(".")) {
            throw new Exception("Unexpected result");
        }

        if (stdout.contains("fail") || exitCode != 0) {
            throw new Exception("Test failed");
        }
    }

    static class Test {

        public boolean run(int seed, String sigAlg, String curve)
                throws Exception {

            
            
            Random r = new Random(seed);
            SecureRandom rand = new SecureRandom() {
                @Override
                public void nextBytes(byte[] bytes) {
                    r.nextBytes(bytes);
                }
            };

            AlgorithmParameters ap = AlgorithmParameters.getInstance("EC", "SunEC");
            ap.init(new ECGenParameterSpec(curve));
            ECParameterSpec spec = ap.getParameterSpec(ECParameterSpec.class);

            KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", "SunEC");
            kpg.initialize(spec, rand);
            KeyPair kp = kpg.generateKeyPair();
            ECPrivateKey ecPrivateKey = (ECPrivateKey) kp.getPrivate();
            ECPublicKey ecPublicKey = (ECPublicKey) kp.getPublic();

            Signature s1 = Signature.getInstance(sigAlg, "SunEC");
            s1.initSign(ecPrivateKey, rand);
            byte[] msg = new byte[1234];
            rand.nextBytes(msg);
            s1.update(msg);
            byte[] sig = s1.sign();

            Signature s2 = Signature.getInstance(sigAlg, "SunEC");
            s2.initVerify(ecPublicKey);
            s2.update(msg);

            boolean result1 = s2.verify(sig);

            s2.initVerify(ecPublicKey);
            
            if (rand.nextInt(10) < 8) {
                sig[rand.nextInt(10000) % sig.length]
                        = (byte) rand.nextInt(10000);
            } else {
                int newLength = rand.nextInt(100);
                if (newLength == sig.length) {
                    newLength += 1 + rand.nextInt(2);
                }
                sig = Arrays.copyOf(sig, newLength);
            }

            boolean result2;
            try {
                result2 = s2.verify(sig);
            } catch (SignatureException se) {
                result2 = false;
            }

            boolean finalResult = result1 && !result2;
            System.out.printf("%10d %20s %20s -- %5s %5s -- %s\n",
                    seed, sigAlg, curve, result1, result2,
                    finalResult ? "succeed" : "fail");

            return finalResult;
        }
    }
}
