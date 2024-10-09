

package sun.security.testlibrary;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.CRLReason;
import java.security.cert.X509Certificate;
import java.security.cert.Extension;
import java.security.cert.CertificateException;
import java.security.cert.CertificateEncodingException;
import java.security.Signature;
import java.util.*;
import java.util.concurrent.*;
import java.text.SimpleDateFormat;
import java.math.BigInteger;

import sun.security.x509.*;
import sun.security.x509.PKIXExtensions;
import sun.security.provider.certpath.ResponderId;
import sun.security.provider.certpath.CertId;
import sun.security.provider.certpath.OCSPResponse;
import sun.security.provider.certpath.OCSPResponse.ResponseStatus;
import sun.security.util.Debug;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;



public class SimpleOCSPServer {
    private final Debug debug = Debug.getInstance("oserv");
    private static final ObjectIdentifier OCSP_BASIC_RESPONSE_OID =
            ObjectIdentifier.newInternal(
                    new int[] { 1, 3, 6, 1, 5, 5, 7, 48, 1, 1});
    private static final SimpleDateFormat utcDateFmt =
            new SimpleDateFormat("MMM dd yyyy, HH:mm:ss z");

    static final int FREE_PORT = 0;

    
    public static enum CertStatus {
        CERT_STATUS_GOOD,
        CERT_STATUS_REVOKED,
        CERT_STATUS_UNKNOWN,
    }

    
    private ServerSocket servSocket;
    private InetAddress listenAddress;
    private int listenPort;

    
    private KeyStore keystore;
    private X509Certificate issuerCert;
    private X509Certificate signerCert;
    private PrivateKey signerKey;

    
    private boolean logEnabled = false;
    private ExecutorService threadPool;
    private volatile boolean started = false;
    private volatile boolean serverReady = false;
    private volatile boolean receivedShutdown = false;
    private volatile boolean acceptConnections = true;
    private volatile long delayMsec = 0;

    
    private long nextUpdateInterval = -1;
    private Date nextUpdate = null;
    private ResponderId respId;
    private AlgorithmId sigAlgId;
    private Map<CertId, CertStatusInfo> statusDb =
            Collections.synchronizedMap(new HashMap<>());

    
    public SimpleOCSPServer(KeyStore ks, String password, String issuerAlias,
            String signerAlias) throws GeneralSecurityException, IOException {
        this(null, FREE_PORT, ks, password, issuerAlias, signerAlias);
    }

    
    public SimpleOCSPServer(InetAddress addr, int port, KeyStore ks,
            String password, String issuerAlias, String signerAlias)
            throws GeneralSecurityException, IOException {
        Objects.requireNonNull(ks, "Null keystore provided");
        Objects.requireNonNull(issuerAlias, "Null issuerName provided");

        utcDateFmt.setTimeZone(TimeZone.getTimeZone("GMT"));

        keystore = ks;
        issuerCert = (X509Certificate)ks.getCertificate(issuerAlias);
        if (issuerCert == null) {
            throw new IllegalArgumentException("Certificate for alias " +
                    issuerAlias + " not found");
        }

        if (signerAlias != null) {
            signerCert = (X509Certificate)ks.getCertificate(signerAlias);
            if (signerCert == null) {
                throw new IllegalArgumentException("Certificate for alias " +
                    signerAlias + " not found");
            }
            signerKey = (PrivateKey)ks.getKey(signerAlias,
                    password.toCharArray());
            if (signerKey == null) {
                throw new IllegalArgumentException("PrivateKey for alias " +
                    signerAlias + " not found");
            }
        } else {
            signerCert = issuerCert;
            signerKey = (PrivateKey)ks.getKey(issuerAlias,
                    password.toCharArray());
            if (signerKey == null) {
                throw new IllegalArgumentException("PrivateKey for alias " +
                    issuerAlias + " not found");
            }
        }

        sigAlgId = AlgorithmId.get("Sha256withRSA");
        respId = new ResponderId(signerCert.getSubjectX500Principal());
        listenAddress = addr;
        listenPort = port;
    }

    
    public synchronized void start() throws IOException {
        
        if (started) {
            log("Server has already been started");
            return;
        } else {
            started = true;
        }

        
        threadPool = Executors.newFixedThreadPool(32, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = Executors.defaultThreadFactory().newThread(r);
                t.setDaemon(true);
                return t;
            }
        });

        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                try (ServerSocket sSock = new ServerSocket()) {
                    servSocket = sSock;
                    servSocket.setReuseAddress(true);
                    servSocket.setSoTimeout(500);
                    servSocket.bind(new InetSocketAddress(listenAddress,
                            listenPort), 128);
                    log("Listening on " + servSocket.getLocalSocketAddress());

                    
                    serverReady = true;

                    
                    
                    
                    listenPort = servSocket.getLocalPort();

                    
                    while (!receivedShutdown) {
                        try {
                            Socket newConnection = servSocket.accept();
                            if (!acceptConnections) {
                                try {
                                    log("Reject connection");
                                    newConnection.close();
                                } catch (IOException e) {
                                    
                                }
                                continue;
                            }
                            threadPool.submit(new OcspHandler(newConnection));
                        } catch (SocketTimeoutException timeout) {
                            
                            
                            
                        } catch (IOException ioe) {
                            
                            log("Unexpected Exception: " + ioe);
                            stop();
                        }
                    }

                    log("Shutting down...");
                    threadPool.shutdown();
                } catch (IOException ioe) {
                    err(ioe);
                } finally {
                    
                    receivedShutdown = false;
                    started = false;
                    serverReady = false;
                }
            }
        });
    }

    
    public synchronized void rejectConnections() {
        log("Reject OCSP connections");
        acceptConnections = false;
    }

    
    public synchronized void acceptConnections() {
        log("Accept OCSP connections");
        acceptConnections = true;
    }


    
    public synchronized void stop() {
        if (started) {
            receivedShutdown = true;
            log("Received shutdown notification");
        }
    }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OCSP Server:\n");
        sb.append("----------------------------------------------\n");
        sb.append("issuer: ").append(issuerCert.getSubjectX500Principal()).
                append("\n");
        sb.append("signer: ").append(signerCert.getSubjectX500Principal()).
                append("\n");
        sb.append("ResponderId: ").append(respId).append("\n");
        sb.append("----------------------------------------------");

        return sb.toString();
    }

    
    private static String dumpHexBytes(byte[] data) {
        return dumpHexBytes(data, 16, "\n", " ");
    }

    
    private static String dumpHexBytes(byte[] data, int itemsPerLine,
            String lineDelim, String itemDelim) {
        StringBuilder sb = new StringBuilder();
        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                if (i % itemsPerLine == 0 && i != 0) {
                    sb.append(lineDelim);
                }
                sb.append(String.format("%02X", data[i])).append(itemDelim);
            }
        }

        return sb.toString();
    }

    
    public void enableLog(boolean enable) {
        if (!started) {
            logEnabled = enable;
        }
    }

    
    public synchronized void setNextUpdateInterval(long interval) {
        if (!started) {
            if (interval <= 0) {
                nextUpdateInterval = -1;
                nextUpdate = null;
                log("nexUpdate support has been disabled");
            } else {
                nextUpdateInterval = interval * 1000;
                nextUpdate = new Date(System.currentTimeMillis() +
                        nextUpdateInterval);
                log("nextUpdate set to " + nextUpdate);
            }
        }
    }

    
    private synchronized Date getNextUpdate() {
        if (nextUpdate != null && nextUpdate.before(new Date())) {
            long nuEpochTime = nextUpdate.getTime();
            long currentTime = System.currentTimeMillis();

            
            
            while (currentTime >= nuEpochTime) {
                nuEpochTime += nextUpdateInterval;
            }

            
            nextUpdate = new Date(nuEpochTime);
            log("nextUpdate updated to new value: " + nextUpdate);
        }
        return nextUpdate;
    }

    
    public void updateStatusDb(Map<BigInteger, CertStatusInfo> newEntries)
            throws IOException {
         if (newEntries != null) {
            for (BigInteger serial : newEntries.keySet()) {
                CertStatusInfo info = newEntries.get(serial);
                if (info != null) {
                    CertId cid = new CertId(issuerCert,
                            new SerialNumber(serial));
                    statusDb.put(cid, info);
                    log("Added entry for serial " + serial + "(" +
                            info.getType() + ")");
                }
            }
        }
    }

    
    private Map<CertId, CertStatusInfo> checkStatusDb(
            List<LocalOcspRequest.LocalSingleRequest> reqList) {
        
        Map<CertId, CertStatusInfo> returnMap = new HashMap<>();

        for (LocalOcspRequest.LocalSingleRequest req : reqList) {
            CertId cid = req.getCertId();
            CertStatusInfo info = statusDb.get(cid);
            if (info != null) {
                log("Status for SN " + cid.getSerialNumber() + ": " +
                        info.getType());
                returnMap.put(cid, info);
            } else {
                log("Status for SN " + cid.getSerialNumber() +
                        " not found, using CERT_STATUS_UNKNOWN");
                returnMap.put(cid,
                        new CertStatusInfo(CertStatus.CERT_STATUS_UNKNOWN));
            }
        }

        return Collections.unmodifiableMap(returnMap);
    }

    
    public void setSignatureAlgorithm(String algName)
            throws NoSuchAlgorithmException {
        if (!started) {
            sigAlgId = AlgorithmId.get(algName);
        }
    }

    
    public int getPort() {
        if (serverReady) {
            InetSocketAddress inetSock =
                    (InetSocketAddress)servSocket.getLocalSocketAddress();
            return inetSock.getPort();
        } else {
            return -1;
        }
    }

    
    public boolean isServerReady() {
        return serverReady;
    }

    
    public void setDelay(long delayMillis) {
        delayMsec = delayMillis > 0 ? delayMillis : 0;
        if (delayMsec > 0) {
            log("OCSP latency set to " + delayMsec + " milliseconds.");
        } else {
            log("OCSP latency disabled");
        }
    }

    
    private synchronized void log(String message) {
        if (logEnabled || debug != null) {
            System.out.println("[" + Thread.currentThread().getName() + "]: " +
                    message);
        }
    }

    
    private static synchronized void err(String message) {
        System.out.println("[" + Thread.currentThread().getName() + "]: " +
                message);
    }

    
    private static synchronized void err(Throwable exc) {
        System.out.print("[" + Thread.currentThread().getName() +
                "]: Exception: ");
        exc.printStackTrace(System.out);
    }

    
    public static class CertStatusInfo {
        private CertStatus certStatusType;
        private CRLReason reason;
        private Date revocationTime;

        
        public CertStatusInfo(CertStatus statType) {
            this(statType, null, null);
        }

        
        public CertStatusInfo(CertStatus statType, Date revDate) {
            this(statType, revDate, null);
        }

        
        public CertStatusInfo(CertStatus statType, Date revDate,
                CRLReason revReason) {
            Objects.requireNonNull(statType, "Cert Status must be non-null");
            certStatusType = statType;
            switch (statType) {
                case CERT_STATUS_GOOD:
                case CERT_STATUS_UNKNOWN:
                    revocationTime = null;
                    break;
                case CERT_STATUS_REVOKED:
                    revocationTime = revDate != null ? (Date)revDate.clone() :
                            new Date();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown status type: " +
                            statType);
            }
        }

        
        public CertStatus getType() {
            return certStatusType;
        }

        
        public Date getRevocationTime() {
            return (revocationTime != null ? (Date)revocationTime.clone() :
                    null);
        }

        
        public CRLReason getRevocationReason() {
            return reason;
        }
    }

    
    private class OcspHandler implements Runnable {
        private final Socket sock;
        InetSocketAddress peerSockAddr;

        
        private OcspHandler(Socket incomingSocket) {
            sock = incomingSocket;
        }

        
        @Override
        public void run() {
            
            
            try {
                if (delayMsec > 0) {
                    Thread.sleep(delayMsec);
                }
            } catch (InterruptedException ie) {
                
                log("Delay of " + delayMsec + " milliseconds was interrupted");
            }

            try (Socket ocspSocket = sock;
                    InputStream in = ocspSocket.getInputStream();
                    OutputStream out = ocspSocket.getOutputStream()) {
                peerSockAddr =
                        (InetSocketAddress)ocspSocket.getRemoteSocketAddress();
                log("Received incoming connection from " + peerSockAddr);
                String[] headerTokens = readLine(in).split(" ");
                LocalOcspRequest ocspReq = null;
                LocalOcspResponse ocspResp = null;
                ResponseStatus respStat = ResponseStatus.INTERNAL_ERROR;
                try {
                    if (headerTokens[0] != null) {
                        switch (headerTokens[0]) {
                            case "POST":
                                    ocspReq = parseHttpOcspPost(in);
                                break;
                            case "GET":
                                
                                
                                throw new IOException("GET method unsupported");
                            default:
                                respStat = ResponseStatus.MALFORMED_REQUEST;
                                throw new IOException("Not a GET or POST");
                        }
                    } else {
                        respStat = ResponseStatus.MALFORMED_REQUEST;
                        throw new IOException("Unable to get HTTP method");
                    }

                    if (ocspReq != null) {
                        log(ocspReq.toString());
                        
                        Map<CertId, CertStatusInfo> statusMap =
                                checkStatusDb(ocspReq.getRequests());
                        if (statusMap.isEmpty()) {
                            respStat = ResponseStatus.UNAUTHORIZED;
                        } else {
                            ocspResp = new LocalOcspResponse(
                                    ResponseStatus.SUCCESSFUL, statusMap,
                                    ocspReq.getExtensions());
                        }
                    } else {
                        respStat = ResponseStatus.MALFORMED_REQUEST;
                        throw new IOException("Found null request");
                    }
                } catch (IOException | RuntimeException exc) {
                    err(exc);
                }
                if (ocspResp == null) {
                    ocspResp = new LocalOcspResponse(respStat);
                }
                sendResponse(out, ocspResp);
            } catch (IOException | CertificateException exc) {
                err(exc);
            }
        }

        
        public void sendResponse(OutputStream out, LocalOcspResponse resp)
                throws IOException {
            StringBuilder sb = new StringBuilder();

            byte[] respBytes;
            try {
                respBytes = resp.getBytes();
            } catch (RuntimeException re) {
                err(re);
                return;
            }

            sb.append("HTTP/1.0 200 OK\r\n");
            sb.append("Content-Type: application/ocsp-response\r\n");
            sb.append("Content-Length: ").append(respBytes.length);
            sb.append("\r\n\r\n");

            out.write(sb.toString().getBytes("UTF-8"));
            out.write(respBytes);
            log(resp.toString());
        }

        
        private LocalOcspRequest parseHttpOcspPost(InputStream inStream)
                throws IOException, CertificateException {
            boolean endOfHeader = false;
            boolean properContentType = false;
            int length = -1;

            while (!endOfHeader) {
                String[] lineTokens = readLine(inStream).split(" ");
                if (lineTokens[0].isEmpty()) {
                    endOfHeader = true;
                } else if (lineTokens[0].equalsIgnoreCase("Content-Type:")) {
                    if (lineTokens[1] == null ||
                            !lineTokens[1].equals(
                                    "application/ocsp-request")) {
                        log("Unknown Content-Type: " +
                                (lineTokens[1] != null ?
                                        lineTokens[1] : "<NULL>"));
                        return null;
                    } else {
                        properContentType = true;
                        log("Content-Type = " + lineTokens[1]);
                    }
                } else if (lineTokens[0].equalsIgnoreCase("Content-Length:")) {
                    if (lineTokens[1] != null) {
                        length = Integer.parseInt(lineTokens[1]);
                        log("Content-Length = " + length);
                    }
                }
            }

            
            
            if (properContentType && length >= 0) {
                byte[] ocspBytes = new byte[length];
                inStream.read(ocspBytes);
                return new LocalOcspRequest(ocspBytes);
            } else {
                return null;
            }
        }

        
        private String readLine(InputStream is) throws IOException {
            PushbackInputStream pbis = new PushbackInputStream(is);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            boolean done = false;
            while (!done) {
                byte b = (byte)pbis.read();
                if (b == '\r') {
                    byte bNext = (byte)pbis.read();
                    if (bNext == '\n' || bNext == -1) {
                        done = true;
                    } else {
                        pbis.unread(bNext);
                        bos.write(b);
                    }
                } else if (b == -1) {
                    done = true;
                } else {
                    bos.write(b);
                }
            }

            return new String(bos.toByteArray(), "UTF-8");
        }
    }


    
    public class LocalOcspRequest {

        private byte[] nonce;
        private byte[] signature = null;
        private AlgorithmId algId = null;
        private int version = 0;
        private GeneralName requestorName = null;
        private Map<String, Extension> extensions = Collections.emptyMap();
        private final List<LocalSingleRequest> requestList = new ArrayList<>();
        private final List<X509Certificate> certificates = new ArrayList<>();

        
        private LocalOcspRequest(byte[] requestBytes) throws IOException,
                CertificateException {
            Objects.requireNonNull(requestBytes, "Received null input");

            DerInputStream dis = new DerInputStream(requestBytes);

            
            
            DerValue[] topStructs = dis.getSequence(2);
            for (DerValue dv : topStructs) {
                if (dv.tag == DerValue.tag_Sequence) {
                    parseTbsRequest(dv);
                } else if (dv.isContextSpecific((byte)0)) {
                    parseSignature(dv);
                } else {
                    throw new IOException("Unknown tag at top level: " +
                            dv.tag);
                }
            }
        }

        
        private void parseSignature(DerValue sigSequence)
                throws IOException, CertificateException {
            DerValue[] sigItems = sigSequence.data.getSequence(3);
            if (sigItems.length != 3) {
                throw new IOException("Invalid number of signature items: " +
                        "expected 3, got " + sigItems.length);
            }

            algId = AlgorithmId.parse(sigItems[0]);
            signature = sigItems[1].getBitString();

            if (sigItems[2].isContextSpecific((byte)0)) {
                DerValue[] certDerItems = sigItems[2].data.getSequence(4);
                int i = 0;
                for (DerValue dv : certDerItems) {
                    X509Certificate xc = new X509CertImpl(dv);
                    certificates.add(xc);
                }
            } else {
                throw new IOException("Invalid tag in signature block: " +
                    sigItems[2].tag);
            }
        }

        
        private void parseTbsRequest(DerValue tbsReqSeq) throws IOException {
            while (tbsReqSeq.data.available() > 0) {
                DerValue dv = tbsReqSeq.data.getDerValue();
                if (dv.isContextSpecific((byte)0)) {
                    
                    version = dv.data.getInteger();
                } else if (dv.isContextSpecific((byte)1)) {
                    
                    requestorName = new GeneralName(dv.data.getDerValue());
                } else if (dv.isContextSpecific((byte)2)) {
                    
                    DerValue[] extItems = dv.data.getSequence(2);
                    extensions = parseExtensions(extItems);
                } else if (dv.tag == DerValue.tag_Sequence) {
                    while (dv.data.available() > 0) {
                        requestList.add(new LocalSingleRequest(dv.data));
                    }
                }
            }
        }

        
        private Map<String, Extension> parseExtensions(DerValue[] extDerItems)
                throws IOException {
            Map<String, Extension> extMap = new HashMap<>();

            if (extDerItems != null && extDerItems.length != 0) {
                for (DerValue extDerVal : extDerItems) {
                    sun.security.x509.Extension ext =
                            new sun.security.x509.Extension(extDerVal);
                    extMap.put(ext.getId(), ext);
                }
            }

            return extMap;
        }

        
        private List<LocalSingleRequest> getRequests() {
            return Collections.unmodifiableList(requestList);
        }

        
        private List<X509Certificate> getCertificates() {
            return Collections.unmodifiableList(certificates);
        }

        
        private Map<String, Extension> getExtensions() {
            return Collections.unmodifiableMap(extensions);
        }

        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append(String.format("OCSP Request: Version %d (0x%X)",
                    version + 1, version)).append("\n");
            if (requestorName != null) {
                sb.append("Requestor Name: ").append(requestorName).
                        append("\n");
            }

            int requestCtr = 0;
            for (LocalSingleRequest lsr : requestList) {
                sb.append("Request [").append(requestCtr++).append("]\n");
                sb.append(lsr).append("\n");
            }
            if (!extensions.isEmpty()) {
                sb.append("Extensions (").append(extensions.size()).
                        append(")\n");
                for (Extension ext : extensions.values()) {
                    sb.append("\t").append(ext).append("\n");
                }
            }
            if (signature != null) {
                sb.append("Signature: ").append(algId).append("\n");
                sb.append(dumpHexBytes(signature)).append("\n");
                int certCtr = 0;
                for (X509Certificate cert : certificates) {
                    sb.append("Certificate [").append(certCtr++).append("]").
                            append("\n");
                    sb.append("\tSubject: ");
                    sb.append(cert.getSubjectX500Principal()).append("\n");
                    sb.append("\tIssuer: ");
                    sb.append(cert.getIssuerX500Principal()).append("\n");
                    sb.append("\tSerial: ").append(cert.getSerialNumber());
                }
            }

            return sb.toString();
        }

        
        public class LocalSingleRequest {
            private final CertId cid;
            private Map<String, Extension> extensions = Collections.emptyMap();

            private LocalSingleRequest(DerInputStream dis)
                    throws IOException {
                DerValue[] srItems = dis.getSequence(2);

                
                if (srItems.length == 1 || srItems.length == 2) {
                    
                    cid = new CertId(srItems[0].data);
                    if (srItems.length == 2) {
                        if (srItems[1].isContextSpecific((byte)0)) {
                            DerValue[] extDerItems = srItems[1].data.getSequence(2);
                            extensions = parseExtensions(extDerItems);
                        } else {
                            throw new IOException("Illegal tag in Request " +
                                    "extensions: " + srItems[1].tag);
                        }
                    }
                } else {
                    throw new IOException("Invalid number of items in " +
                            "Request (" + srItems.length + ")");
                }
            }

            
            private CertId getCertId() {
                return cid;
            }

            
            private Map<String, Extension> getExtensions() {
                return Collections.unmodifiableMap(extensions);
            }

            
            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("CertId, Algorithm = ");
                sb.append(cid.getHashAlgorithm()).append("\n");
                sb.append("\tIssuer Name Hash: ");
                sb.append(dumpHexBytes(cid.getIssuerNameHash(), 256, "", ""));
                sb.append("\n");
                sb.append("\tIssuer Key Hash: ");
                sb.append(dumpHexBytes(cid.getIssuerKeyHash(), 256, "", ""));
                sb.append("\n");
                sb.append("\tSerial Number: ").append(cid.getSerialNumber());
                if (!extensions.isEmpty()) {
                    sb.append("Extensions (").append(extensions.size()).
                            append(")\n");
                    for (Extension ext : extensions.values()) {
                        sb.append("\t").append(ext).append("\n");
                    }
                }

                return sb.toString();
            }
        }
    }

    
    public class LocalOcspResponse {
        private final int version = 0;
        private final OCSPResponse.ResponseStatus responseStatus;
        private final Map<CertId, CertStatusInfo> respItemMap;
        private final Date producedAtDate;
        private final List<LocalSingleResponse> singleResponseList =
                new ArrayList<>();
        private final Map<String, Extension> responseExtensions;
        private byte[] signature;
        private final List<X509Certificate> certificates;
        private final byte[] encodedResponse;

        
        public LocalOcspResponse(OCSPResponse.ResponseStatus respStat)
                throws IOException {
            this(respStat, null, null);
        }

        
        public LocalOcspResponse(OCSPResponse.ResponseStatus respStat,
                Map<CertId, CertStatusInfo> itemMap,
                Map<String, Extension> reqExtensions) throws IOException {
            responseStatus = Objects.requireNonNull(respStat,
                    "Illegal null response status");
            if (responseStatus == ResponseStatus.SUCCESSFUL) {
                respItemMap = Objects.requireNonNull(itemMap,
                        "SUCCESSFUL responses must have a response map");
                producedAtDate = new Date();

                
                
                for (CertId id : itemMap.keySet()) {
                    singleResponseList.add(
                            new LocalSingleResponse(id, itemMap.get(id)));
                }

                responseExtensions = setResponseExtensions(reqExtensions);
                certificates = new ArrayList<>();
                if (signerCert != issuerCert) {
                    certificates.add(signerCert);
                }
                certificates.add(issuerCert);
            } else {
                respItemMap = null;
                producedAtDate = null;
                responseExtensions = null;
                certificates = null;
            }
            encodedResponse = this.getBytes();
        }

        
        private Map<String, Extension> setResponseExtensions(
                Map<String, Extension> reqExts) {
            Map<String, Extension> respExts = new HashMap<>();
            String ocspNonceStr = PKIXExtensions.OCSPNonce_Id.toString();

            if (reqExts != null) {
                for (String id : reqExts.keySet()) {
                    if (id.equals(ocspNonceStr)) {
                        
                        Extension ext = reqExts.get(id);
                        if (ext != null) {
                            respExts.put(id, ext);
                            log("Added OCSP Nonce to response");
                        } else {
                            log("Error: Found nonce entry, but found null " +
                                    "value.  Skipping");
                        }
                    }
                }
            }

            return respExts;
        }

        
        private byte[] getBytes() throws IOException {
            DerOutputStream outerSeq = new DerOutputStream();
            DerOutputStream responseStream = new DerOutputStream();
            responseStream.putEnumerated(responseStatus.ordinal());
            if (responseStatus == ResponseStatus.SUCCESSFUL &&
                    respItemMap != null) {
                encodeResponseBytes(responseStream);
            }

            
            outerSeq.write(DerValue.tag_Sequence, responseStream);
            return outerSeq.toByteArray();
        }

        private void encodeResponseBytes(DerOutputStream responseStream)
                throws IOException {
            DerOutputStream explicitZero = new DerOutputStream();
            DerOutputStream respItemStream = new DerOutputStream();

            respItemStream.putOID(OCSP_BASIC_RESPONSE_OID);

            byte[] basicOcspBytes = encodeBasicOcspResponse();
            respItemStream.putOctetString(basicOcspBytes);
            explicitZero.write(DerValue.tag_Sequence, respItemStream);
            responseStream.write(DerValue.createTag(DerValue.TAG_CONTEXT,
                    true, (byte)0), explicitZero);
        }

        private byte[] encodeBasicOcspResponse() throws IOException {
            DerOutputStream outerSeq = new DerOutputStream();
            DerOutputStream basicORItemStream = new DerOutputStream();

            
            byte[] tbsResponseBytes = encodeTbsResponse();
            basicORItemStream.write(tbsResponseBytes);

            try {
                sigAlgId.derEncode(basicORItemStream);

                
                Signature sig = Signature.getInstance(sigAlgId.getName());
                sig.initSign(signerKey);
                sig.update(tbsResponseBytes);
                signature = sig.sign();
                basicORItemStream.putBitString(signature);
            } catch (GeneralSecurityException exc) {
                err(exc);
                throw new IOException(exc);
            }

            
            try {
                DerOutputStream certStream = new DerOutputStream();
                ArrayList<DerValue> certList = new ArrayList<>();
                if (signerCert != issuerCert) {
                    certList.add(new DerValue(signerCert.getEncoded()));
                }
                certList.add(new DerValue(issuerCert.getEncoded()));
                DerValue[] dvals = new DerValue[certList.size()];
                certStream.putSequence(certList.toArray(dvals));
                basicORItemStream.write(DerValue.createTag(DerValue.TAG_CONTEXT,
                        true, (byte)0), certStream);
            } catch (CertificateEncodingException cex) {
                err(cex);
                throw new IOException(cex);
            }

            
            outerSeq.write(DerValue.tag_Sequence, basicORItemStream);
            return outerSeq.toByteArray();
        }

        private byte[] encodeTbsResponse() throws IOException {
            DerOutputStream outerSeq = new DerOutputStream();
            DerOutputStream tbsStream = new DerOutputStream();

            
            tbsStream.write(respId.getEncoded());
            tbsStream.putGeneralizedTime(producedAtDate);

            
            encodeSingleResponses(tbsStream);

            
            encodeExtensions(tbsStream);

            outerSeq.write(DerValue.tag_Sequence, tbsStream);
            return outerSeq.toByteArray();
        }

        private void encodeSingleResponses(DerOutputStream tbsStream)
                throws IOException {
            DerValue[] srDerVals = new DerValue[singleResponseList.size()];
            int srDvCtr = 0;

            for (LocalSingleResponse lsr : singleResponseList) {
                srDerVals[srDvCtr++] = new DerValue(lsr.getBytes());
            }

            tbsStream.putSequence(srDerVals);
        }

        private void encodeExtensions(DerOutputStream tbsStream)
                throws IOException {
            DerOutputStream extSequence = new DerOutputStream();
            DerOutputStream extItems = new DerOutputStream();

            for (Extension ext : responseExtensions.values()) {
                ext.encode(extItems);
            }
            extSequence.write(DerValue.tag_Sequence, extItems);
            tbsStream.write(DerValue.createTag(DerValue.TAG_CONTEXT, true,
                    (byte)1), extSequence);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("OCSP Response: ").append(responseStatus).append("\n");
            if (responseStatus == ResponseStatus.SUCCESSFUL) {
                sb.append("Response Type: ").
                        append(OCSP_BASIC_RESPONSE_OID.toString()).append("\n");
                sb.append(String.format("Version: %d (0x%X)", version + 1,
                        version)).append("\n");
                sb.append("Responder Id: ").append(respId.toString()).
                        append("\n");
                sb.append("Produced At: ").
                        append(utcDateFmt.format(producedAtDate)).append("\n");

                int srCtr = 0;
                for (LocalSingleResponse lsr : singleResponseList) {
                    sb.append("SingleResponse [").append(srCtr++).append("]\n");
                    sb.append(lsr);
                }

                if (!responseExtensions.isEmpty()) {
                    sb.append("Extensions (").append(responseExtensions.size()).
                            append(")\n");
                    for (Extension ext : responseExtensions.values()) {
                        sb.append("\t").append(ext).append("\n");
                    }
                } else {
                    sb.append("\n");
                }

                if (signature != null) {
                    sb.append("Signature: ").append(sigAlgId).append("\n");
                    sb.append(dumpHexBytes(signature)).append("\n");
                    int certCtr = 0;
                    for (X509Certificate cert : certificates) {
                        sb.append("Certificate [").append(certCtr++).append("]").
                                append("\n");
                        sb.append("\tSubject: ");
                        sb.append(cert.getSubjectX500Principal()).append("\n");
                        sb.append("\tIssuer: ");
                        sb.append(cert.getIssuerX500Principal()).append("\n");
                        sb.append("\tSerial: ").append(cert.getSerialNumber());
                        sb.append("\n");
                    }
                }
            }

            return sb.toString();
        }

        private class LocalSingleResponse {
            private final CertId certId;
            private final CertStatusInfo csInfo;
            private final Date thisUpdate;
            private final Date lsrNextUpdate;
            private final Map<String, Extension> singleExtensions;

            public LocalSingleResponse(CertId cid, CertStatusInfo info) {
                certId = Objects.requireNonNull(cid, "CertId must be non-null");
                csInfo = Objects.requireNonNull(info,
                        "CertStatusInfo must be non-null");

                
                
                thisUpdate = producedAtDate;
                lsrNextUpdate = getNextUpdate();

                
                singleExtensions = Collections.emptyMap();
            }

            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("Certificate Status: ").append(csInfo.getType());
                sb.append("\n");
                if (csInfo.getType() == CertStatus.CERT_STATUS_REVOKED) {
                    sb.append("Revocation Time: ");
                    sb.append(utcDateFmt.format(csInfo.getRevocationTime()));
                    sb.append("\n");
                    if (csInfo.getRevocationReason() != null) {
                        sb.append("Revocation Reason: ");
                        sb.append(csInfo.getRevocationReason()).append("\n");
                    }
                }

                sb.append("CertId, Algorithm = ");
                sb.append(certId.getHashAlgorithm()).append("\n");
                sb.append("\tIssuer Name Hash: ");
                sb.append(dumpHexBytes(certId.getIssuerNameHash(), 256, "", ""));
                sb.append("\n");
                sb.append("\tIssuer Key Hash: ");
                sb.append(dumpHexBytes(certId.getIssuerKeyHash(), 256, "", ""));
                sb.append("\n");
                sb.append("\tSerial Number: ").append(certId.getSerialNumber());
                sb.append("\n");
                sb.append("This Update: ");
                sb.append(utcDateFmt.format(thisUpdate)).append("\n");
                if (lsrNextUpdate != null) {
                    sb.append("Next Update: ");
                    sb.append(utcDateFmt.format(lsrNextUpdate)).append("\n");
                }

                if (!singleExtensions.isEmpty()) {
                    sb.append("Extensions (").append(singleExtensions.size()).
                            append(")\n");
                    for (Extension ext : singleExtensions.values()) {
                        sb.append("\t").append(ext).append("\n");
                    }
                }

                return sb.toString();
            }

            public byte[] getBytes() throws IOException {
                byte[] nullData = { };
                DerOutputStream responseSeq = new DerOutputStream();
                DerOutputStream srStream = new DerOutputStream();

                
                certId.encode(srStream);

                
                CertStatus csiType = csInfo.getType();
                switch (csiType) {
                    case CERT_STATUS_GOOD:
                        srStream.write(DerValue.createTag(DerValue.TAG_CONTEXT,
                                false, (byte)0), nullData);
                        break;
                    case CERT_STATUS_REVOKED:
                        DerOutputStream revInfo = new DerOutputStream();
                        revInfo.putGeneralizedTime(csInfo.getRevocationTime());
                        CRLReason revReason = csInfo.getRevocationReason();
                        if (revReason != null) {
                            byte[] revDer = new byte[3];
                            revDer[0] = DerValue.tag_Enumerated;
                            revDer[1] = 1;
                            revDer[2] = (byte)revReason.ordinal();
                            revInfo.write(DerValue.createTag(
                                    DerValue.TAG_CONTEXT, true, (byte)0),
                                    revDer);
                        }
                        srStream.write(DerValue.createTag(
                                DerValue.TAG_CONTEXT, true, (byte)1),
                                revInfo);
                        break;
                    case CERT_STATUS_UNKNOWN:
                        srStream.write(DerValue.createTag(DerValue.TAG_CONTEXT,
                                false, (byte)2), nullData);
                        break;
                    default:
                        throw new IOException("Unknown CertStatus: " + csiType);
                }

                
                srStream.putGeneralizedTime(thisUpdate);
                if (lsrNextUpdate != null) {
                    DerOutputStream nuStream = new DerOutputStream();
                    nuStream.putGeneralizedTime(lsrNextUpdate);
                    srStream.write(DerValue.createTag(DerValue.TAG_CONTEXT,
                            true, (byte)0), nuStream);
                }

                

                
                responseSeq.write(DerValue.tag_Sequence, srStream);
                return responseSeq.toByteArray();
            }
        }
    }
}
