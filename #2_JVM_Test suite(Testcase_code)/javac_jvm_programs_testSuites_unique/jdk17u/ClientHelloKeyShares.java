import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.*;
import java.nio.ByteBuffer;
import java.util.*;

public class ClientHelloKeyShares {

    private static final int TLS_REC_HANDSHAKE = 22;

    private static final int HELLO_EXT_SUPP_GROUPS = 10;

    private static final int HELLO_EXT_SUPP_VERS = 43;

    private static final int HELLO_EXT_KEY_SHARE = 51;

    private static final int TLS_PROT_VER_13 = 0x0304;

    private static final int NG_SECP256R1 = 0x0017;

    private static final int NG_SECP384R1 = 0x0018;

    private static final int NG_X25519 = 0x001D;

    private static final int NG_X448 = 0x001E;

    public static void main(String[] args) throws Exception {
        List<Integer> expectedKeyShares = new ArrayList<>();
        Arrays.stream(args).forEach(arg -> expectedKeyShares.add(Integer.valueOf(arg)));
        SSLContext sslCtx = SSLContext.getDefault();
        SSLEngine engine = sslCtx.createSSLEngine();
        engine.setUseClientMode(true);
        SSLSession session = engine.getSession();
        ByteBuffer clientOut = ByteBuffer.wrap("I'm a Client".getBytes());
        ByteBuffer cTOs = ByteBuffer.allocateDirect(session.getPacketBufferSize());
        SSLEngineResult clientResult = engine.wrap(clientOut, cTOs);
        logResult("client wrap: ", clientResult);
        if (clientResult.getStatus() != SSLEngineResult.Status.OK) {
            throw new RuntimeException("Client wrap got status: " + clientResult.getStatus());
        }
        cTOs.flip();
        System.out.println(dumpHexBytes(cTOs));
        checkClientHello(cTOs, expectedKeyShares);
    }

    private static void logResult(String str, SSLEngineResult result) {
        HandshakeStatus hsStatus = result.getHandshakeStatus();
        System.out.println(str + result.getStatus() + "/" + hsStatus + ", " + result.bytesConsumed() + "/" + result.bytesProduced() + " bytes");
        if (hsStatus == HandshakeStatus.FINISHED) {
            System.out.println("\t...ready for application data");
        }
    }

    private static String dumpHexBytes(ByteBuffer data) {
        StringBuilder sb = new StringBuilder();
        if (data != null) {
            int i = 0;
            data.mark();
            while (data.hasRemaining()) {
                if (i % 16 == 0 && i != 0) {
                    sb.append("\n");
                }
                sb.append(String.format("%02X ", data.get()));
                i++;
            }
            data.reset();
        }
        return sb.toString();
    }

    private static void checkClientHello(ByteBuffer data, List<Integer> expectedKeyShares) {
        Objects.requireNonNull(data);
        data.mark();
        int type = Byte.toUnsignedInt(data.get());
        int ver_major = Byte.toUnsignedInt(data.get());
        int ver_minor = Byte.toUnsignedInt(data.get());
        int recLen = Short.toUnsignedInt(data.getShort());
        if (type != 22) {
            throw new RuntimeException("Not a handshake: Type = " + type);
        } else if (recLen > data.remaining()) {
            throw new RuntimeException("Incomplete record in buffer: " + "Record length = " + recLen + ", Remaining = " + data.remaining());
        }
        int msgHdr = data.getInt();
        int msgType = (msgHdr >> 24) & 0x000000FF;
        int msgLen = msgHdr & 0x00FFFFFF;
        if (msgType != 1) {
            throw new RuntimeException("Not a ClientHello: Type = " + msgType);
        }
        data.position(data.position() + 34);
        int sessLen = Byte.toUnsignedInt(data.get());
        if (sessLen != 0) {
            data.position(data.position() + sessLen);
        }
        int csLen = Short.toUnsignedInt(data.getShort());
        if (csLen != 0) {
            data.position(data.position() + csLen);
        }
        int compLen = Byte.toUnsignedInt(data.get());
        if (compLen != 0) {
            data.position(data.position() + compLen);
        }
        boolean foundSupVer = false;
        boolean foundKeyShare = false;
        int extsLen = Short.toUnsignedInt(data.getShort());
        List<Integer> supGrpList = new ArrayList<>();
        List<Integer> chKeyShares = new ArrayList<>();
        while (data.hasRemaining()) {
            int extType = Short.toUnsignedInt(data.getShort());
            int extLen = Short.toUnsignedInt(data.getShort());
            boolean foundTLS13 = false;
            switch(extType) {
                case HELLO_EXT_SUPP_GROUPS:
                    int supGrpLen = Short.toUnsignedInt(data.getShort());
                    for (int remain = supGrpLen; remain > 0; remain -= 2) {
                        supGrpList.add(Short.toUnsignedInt(data.getShort()));
                    }
                    break;
                case HELLO_EXT_SUPP_VERS:
                    foundSupVer = true;
                    int supVerLen = Byte.toUnsignedInt(data.get());
                    for (int remain = supVerLen; remain > 0; remain -= 2) {
                        foundTLS13 |= (Short.toUnsignedInt(data.getShort()) == TLS_PROT_VER_13);
                    }
                    if (!foundTLS13) {
                        throw new RuntimeException("Missing TLS 1.3 Protocol " + "Version in supported_groups");
                    }
                    break;
                case HELLO_EXT_KEY_SHARE:
                    foundKeyShare = true;
                    int ksListLen = Short.toUnsignedInt(data.getShort());
                    while (ksListLen > 0) {
                        chKeyShares.add(Short.toUnsignedInt(data.getShort()));
                        int ksLen = Short.toUnsignedInt(data.getShort());
                        data.position(data.position() + ksLen);
                        ksListLen -= (4 + ksLen);
                    }
                    break;
                default:
                    data.position(data.position() + extLen);
                    break;
            }
        }
        if ((foundSupVer && foundKeyShare && !supGrpList.isEmpty()) == false) {
            throw new RuntimeException("Missing one or more of key_share, " + "supported_versions and/or supported_groups extensions");
        }
        if (!expectedKeyShares.equals(chKeyShares)) {
            StringBuilder sb = new StringBuilder("Expected and Actual key_share lists differ: ");
            sb.append("Expected: ");
            expectedKeyShares.forEach(ng -> sb.append(ng).append(" "));
            sb.append(", Actual: ");
            chKeyShares.forEach(ng -> sb.append(ng).append(" "));
            throw new RuntimeException(sb.toString());
        }
        int prevChNg = -1;
        for (int ng : chKeyShares) {
            int chNgPos = supGrpList.indexOf(ng);
            if (chNgPos <= prevChNg) {
                throw new RuntimeException("Order of precedence violation " + "for NamedGroup " + ng + " between key_share and " + "supported_groups extensions");
            }
            prevChNg = chNgPos;
        }
        data.reset();
    }
}
