
import java.io.ByteArrayInputStream;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;


public final class CertPathEncodingTest {
    
    private static final String cert1 =
            "-----BEGIN CERTIFICATE-----\n" +
            "MIICzTCCAougAwIBAgIEN8GnNDALBgcqhkjOOAQDBQAwSTELMAkGA1UEBhMCdXMx\n" +
            "DDAKBgNVBAoTA3N1bjENMAsGA1UECxMEZWFzdDEMMAoGA1UECxMDYmNuMQ8wDQYD\n" +
            "VQQDEwZ5YXNzaXIwHhcNOTkwODIzMTk1NTMyWhcNMDAwODIyMTk1NTMyWjA4MQsw\n" +
            "CQYDVQQGEwJ1czEMMAoGA1UEChMDc3VuMQ0wCwYDVQQLEwRlYXN0MQwwCgYDVQQL\n" +
            "EwNiY24wggG1MIIBLAYHKoZIzjgEATCCAR8CgYEA/X9TgR11EilS30qcLuzk5/YR\n" +
            "t1I870QAwx4/gLZRJmlFXUAiUftZPY1Y+r/F9bow9subVWzXgTuAHTRv8mZgt2uZ\n" +
            "UKWkn5/oBHsQIsJPu6nX/rfGG/g7V+fGqKYVDwT7g/bTxR7DAjVUE1oWkTL2dfOu\n" +
            "K2HXKu/yIgMZndFIAccCFQCXYFCPFSMLzLKSuYKi64QL8Fgc9QKBgQD34aCF1ps9\n" +
            "3su8q1w2uFe5eZSvu/o66oL5V0wLPQeCZ1FZV4661FlP5nEHEIGAtEkWcSPoTCgW\n" +
            "E7fPCTKMyKbhPBZ6i1R8jSjgo64eK7OmdZFuo38L+iE1YvH7YnoBJDvMpPG+qFGQ\n" +
            "iaiD3+Fa5Z8GkotmXoB7VSVkAUw7/s9JKgOBggACf2NHT/Yp5ZiiIf3al54/yrAX\n" +
            "SY2KpwYNpniXOVkzcqKldNU67+Z8B9eOjtFmc5kUBJb1MdZy7tJT+JC188PxZLoa\n" +
            "nsAK2pJIxdOEfkgJZtlRunRWWneKjJqc9oQSYRJR3MZPhJTsy3hRg4wgilN70rY2\n" +
            "31A1lR/LUFWLP/vid8ujEzARMA8GA1UdDwEB/wQFAwMHpAAwCwYHKoZIzjgEAwUA\n" +
            "Ay8AMCwCFFKAUissPQJmWLTc71ImcBtTyrN9AhRiA7KrPhgqZgm2ztQFpY6leg1V\n" +
            "Zw==\n" +
            "-----END CERTIFICATE-----\n" +
            "";

    
    private static final String cert2 =
            "-----BEGIN CERTIFICATE-----\n" +
            "MIIC9TCCArKgAwIBAgIEN7xtdzALBgcqhkjOOAQDBQAwSTELMAkGA1UEBhMCdXMx\n" +
            "DDAKBgNVBAoTA3N1bjENMAsGA1UECxMEZWFzdDEMMAoGA1UECxMDYmNuMQ8wDQYD\n" +
            "VQQDEwZ5YXNzaXIwHhcNOTkwODE5MjA0NzUxWhcNMDAwODE4MjA0NzUxWjBJMQsw\n" +
            "CQYDVQQGEwJ1czEMMAoGA1UEChMDc3VuMQ0wCwYDVQQLEwRlYXN0MQwwCgYDVQQL\n" +
            "EwNiY24xDzANBgNVBAMTBnlhc3NpcjCCAbcwggEsBgcqhkjOOAQBMIIBHwKBgQD9\n" +
            "f1OBHXUSKVLfSpwu7OTn9hG3UjzvRADDHj+AtlEmaUVdQCJR+1k9jVj6v8X1ujD2\n" +
            "y5tVbNeBO4AdNG/yZmC3a5lQpaSfn+gEexAiwk+7qdf+t8Yb+DtX58aophUPBPuD\n" +
            "9tPFHsMCNVQTWhaRMvZ1864rYdcq7/IiAxmd0UgBxwIVAJdgUI8VIwvMspK5gqLr\n" +
            "hAvwWBz1AoGBAPfhoIXWmz3ey7yrXDa4V7l5lK+7+jrqgvlXTAs9B4JnUVlXjrrU\n" +
            "WU/mcQcQgYC0SRZxI+hMKBYTt88JMozIpuE8FnqLVHyNKOCjrh4rs6Z1kW6jfwv6\n" +
            "ITVi8ftiegEkO8yk8b6oUZCJqIPf4VrlnwaSi2ZegHtVJWQBTDv+z0kqA4GEAAKB\n" +
            "gArMpOzWiEXCJGsNePGC814+MV37ZNUGXjkW8QqF0f/RpHTF5rC6kxzuaVG+O6Zm\n" +
            "RFC08F4O3Z8Icf6hkS7UnmuywII8kWwYsNm8o0iRP4tZAWEAAqsiMbx8bA2f7b4z\n" +
            "5lxEnmIwlfhtItflhUywmG6tzMo7rcv69583E/fK4iK6oycwJTAPBgNVHQ8BAf8E\n" +
            "BQMDB6QAMBIGA1UdEwEB/wQIMAYBAf8CAQUwCwYHKoZIzjgEAwUAAzAAMC0CFC+I\n" +
            "RjeUkrICB1uNduWBI4V/vI25AhUAi9dB+hHHqyeSXQoDmFY2Ql/1H50=\n" +
            "-----END CERTIFICATE-----\n" +
            "";

    private static final String pkcs7path =
            "MIIF9QYJKoZIhvcNAQcCoIIF5jCCBeICAQExADALBgkqhkiG9w0BBwGgggXKMIICzTCCAougAwIB\n" +
            "AgIEN8GnNDALBgcqhkjOOAQDBQAwSTELMAkGA1UEBhMCdXMxDDAKBgNVBAoTA3N1bjENMAsGA1UE\n" +
            "CxMEZWFzdDEMMAoGA1UECxMDYmNuMQ8wDQYDVQQDEwZ5YXNzaXIwHhcNOTkwODIzMTk1NTMyWhcN\n" +
            "MDAwODIyMTk1NTMyWjA4MQswCQYDVQQGEwJ1czEMMAoGA1UEChMDc3VuMQ0wCwYDVQQLEwRlYXN0\n" +
            "MQwwCgYDVQQLEwNiY24wggG1MIIBLAYHKoZIzjgEATCCAR8CgYEA/X9TgR11EilS30qcLuzk5/YR\n" +
            "t1I870QAwx4/gLZRJmlFXUAiUftZPY1Y+r/F9bow9subVWzXgTuAHTRv8mZgt2uZUKWkn5/oBHsQ\n" +
            "IsJPu6nX/rfGG/g7V+fGqKYVDwT7g/bTxR7DAjVUE1oWkTL2dfOuK2HXKu/yIgMZndFIAccCFQCX\n" +
            "YFCPFSMLzLKSuYKi64QL8Fgc9QKBgQD34aCF1ps93su8q1w2uFe5eZSvu/o66oL5V0wLPQeCZ1FZ\n" +
            "V4661FlP5nEHEIGAtEkWcSPoTCgWE7fPCTKMyKbhPBZ6i1R8jSjgo64eK7OmdZFuo38L+iE1YvH7\n" +
            "YnoBJDvMpPG+qFGQiaiD3+Fa5Z8GkotmXoB7VSVkAUw7/s9JKgOBggACf2NHT/Yp5ZiiIf3al54/\n" +
            "yrAXSY2KpwYNpniXOVkzcqKldNU67+Z8B9eOjtFmc5kUBJb1MdZy7tJT+JC188PxZLoansAK2pJI\n" +
            "xdOEfkgJZtlRunRWWneKjJqc9oQSYRJR3MZPhJTsy3hRg4wgilN70rY231A1lR/LUFWLP/vid8uj\n" +
            "EzARMA8GA1UdDwEB/wQFAwMHpAAwCwYHKoZIzjgEAwUAAy8AMCwCFFKAUissPQJmWLTc71ImcBtT\n" +
            "yrN9AhRiA7KrPhgqZgm2ztQFpY6leg1VZzCCAvUwggKyoAMCAQICBDe8bXcwCwYHKoZIzjgEAwUA\n" +
            "MEkxCzAJBgNVBAYTAnVzMQwwCgYDVQQKEwNzdW4xDTALBgNVBAsTBGVhc3QxDDAKBgNVBAsTA2Jj\n" +
            "bjEPMA0GA1UEAxMGeWFzc2lyMB4XDTk5MDgxOTIwNDc1MVoXDTAwMDgxODIwNDc1MVowSTELMAkG\n" +
            "A1UEBhMCdXMxDDAKBgNVBAoTA3N1bjENMAsGA1UECxMEZWFzdDEMMAoGA1UECxMDYmNuMQ8wDQYD\n" +
            "VQQDEwZ5YXNzaXIwggG3MIIBLAYHKoZIzjgEATCCAR8CgYEA/X9TgR11EilS30qcLuzk5/YRt1I8\n" +
            "70QAwx4/gLZRJmlFXUAiUftZPY1Y+r/F9bow9subVWzXgTuAHTRv8mZgt2uZUKWkn5/oBHsQIsJP\n" +
            "u6nX/rfGG/g7V+fGqKYVDwT7g/bTxR7DAjVUE1oWkTL2dfOuK2HXKu/yIgMZndFIAccCFQCXYFCP\n" +
            "FSMLzLKSuYKi64QL8Fgc9QKBgQD34aCF1ps93su8q1w2uFe5eZSvu/o66oL5V0wLPQeCZ1FZV466\n" +
            "1FlP5nEHEIGAtEkWcSPoTCgWE7fPCTKMyKbhPBZ6i1R8jSjgo64eK7OmdZFuo38L+iE1YvH7YnoB\n" +
            "JDvMpPG+qFGQiaiD3+Fa5Z8GkotmXoB7VSVkAUw7/s9JKgOBhAACgYAKzKTs1ohFwiRrDXjxgvNe\n" +
            "PjFd+2TVBl45FvEKhdH/0aR0xeawupMc7mlRvjumZkRQtPBeDt2fCHH+oZEu1J5rssCCPJFsGLDZ\n" +
            "vKNIkT+LWQFhAAKrIjG8fGwNn+2+M+ZcRJ5iMJX4bSLX5YVMsJhurczKO63L+vefNxP3yuIiuqMn\n" +
            "MCUwDwYDVR0PAQH/BAUDAwekADASBgNVHRMBAf8ECDAGAQH/AgEFMAsGByqGSM44BAMFAAMwADAt\n" +
            "AhQviEY3lJKyAgdbjXblgSOFf7yNuQIVAIvXQfoRx6snkl0KA5hWNkJf9R+dMQA=\n" +
            "";

    
    public static void main(String[] args) throws Exception {
        
        CertificateFactory certFac = CertificateFactory.getInstance("X509");

        final List<Certificate> certs = new ArrayList<>();
        certs.add(certFac.generateCertificate(new ByteArrayInputStream(cert1.getBytes())));
        certs.add(certFac.generateCertificate(new ByteArrayInputStream(cert2.getBytes())));

        CertPath cp = certFac.generateCertPath(certs);

        
        byte[] encoded = cp.getEncoded("PKCS7");

        
        if (!Arrays.equals(encoded, Base64.getMimeDecoder().decode(pkcs7path.getBytes()))) {
            throw new RuntimeException("PKCS#7 encoding doesn't match stored value");
        }

        
        
        CertPath decodedCP = certFac.generateCertPath(new ByteArrayInputStream(encoded), "PKCS7");
        if (!decodedCP.equals(cp)) {
            throw new RuntimeException("CertPath decoded from PKCS#7 isn't equal to original");
        }
    }
}
