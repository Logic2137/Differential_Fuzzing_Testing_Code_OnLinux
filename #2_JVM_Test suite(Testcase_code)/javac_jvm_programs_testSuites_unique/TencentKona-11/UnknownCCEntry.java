


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import sun.security.krb5.internal.ccache.CCacheInputStream;
import sun.security.krb5.internal.ccache.CredentialsCache;

public class UnknownCCEntry {
    public static void main(String[] args) throws Exception {
        
        
        
        
        
        
        byte[] krb5cc = {
            (byte)0x05, (byte)0x04, (byte)0x00, (byte)0x0C,
            (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x08,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFA,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x09,
            (byte)0x4D, (byte)0x41, (byte)0x58, (byte)0x2E,
            (byte)0x4C, (byte)0x4F, (byte)0x43, (byte)0x41,
            (byte)0x4C, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x05, (byte)0x64, (byte)0x75, (byte)0x6D,
            (byte)0x6D, (byte)0x79, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x09, (byte)0x4D, (byte)0x41,
            (byte)0x58, (byte)0x2E, (byte)0x4C, (byte)0x4F,
            (byte)0x43, (byte)0x41, (byte)0x4C, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x05, (byte)0x64,
            (byte)0x75, (byte)0x6D, (byte)0x6D, (byte)0x79,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x02,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x09,
            (byte)0x4D, (byte)0x41, (byte)0x58, (byte)0x2E,
            (byte)0x4C, (byte)0x4F, (byte)0x43, (byte)0x41,
            (byte)0x4C, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x06, (byte)0x6B, (byte)0x72, (byte)0x62,
            (byte)0x74, (byte)0x67, (byte)0x74, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x09, (byte)0x4D,
            (byte)0x41, (byte)0x58, (byte)0x2E, (byte)0x4C,
            (byte)0x4F, (byte)0x43, (byte)0x41, (byte)0x4C,
            (byte)0x00, (byte)0x11, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x10, (byte)0x92, (byte)0x1D,
            (byte)0x1A, (byte)0x0C, (byte)0x7F, (byte)0xB8,
            (byte)0x01, (byte)0x2E, (byte)0xC9, (byte)0xF5,
            (byte)0x7B, (byte)0x92, (byte)0x81, (byte)0xCA,
            (byte)0x49, (byte)0xC5, (byte)0x4C, (byte)0x73,
            (byte)0x30, (byte)0x68, (byte)0x4C, (byte)0x73,
            (byte)0x30, (byte)0x68, (byte)0x4C, (byte)0x74,
            (byte)0x81, (byte)0xE8, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x40,
            (byte)0x41, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x01, (byte)0x29, (byte)0x61,
            (byte)0x82, (byte)0x01, (byte)0x25, (byte)0x30,
            (byte)0x82, (byte)0x01, (byte)0x21, (byte)0xA0,
            (byte)0x03, (byte)0x02, (byte)0x01, (byte)0x05,
            (byte)0xA1, (byte)0x0B, (byte)0x1B, (byte)0x09,
            (byte)0x4D, (byte)0x41, (byte)0x58, (byte)0x2E,
            (byte)0x4C, (byte)0x4F, (byte)0x43, (byte)0x41,
            (byte)0x4C, (byte)0xA2, (byte)0x1E, (byte)0x30,
            (byte)0x1C, (byte)0xA0, (byte)0x03, (byte)0x02,
            (byte)0x01, (byte)0x00, (byte)0xA1, (byte)0x15,
            (byte)0x30, (byte)0x13, (byte)0x1B, (byte)0x06,
            (byte)0x6B, (byte)0x72, (byte)0x62, (byte)0x74,
            (byte)0x67, (byte)0x74, (byte)0x1B, (byte)0x09,
            (byte)0x4D, (byte)0x41, (byte)0x58, (byte)0x2E,
            (byte)0x4C, (byte)0x4F, (byte)0x43, (byte)0x41,
            (byte)0x4C, (byte)0xA3, (byte)0x81, (byte)0xEC,
            (byte)0x30, (byte)0x81, (byte)0xE9, (byte)0xA0,
            (byte)0x03, (byte)0x02, (byte)0x01, (byte)0x12,
            (byte)0xA1, (byte)0x03, (byte)0x02, (byte)0x01,
            (byte)0x01, (byte)0xA2, (byte)0x81, (byte)0xDC,
            (byte)0x04, (byte)0x81, (byte)0xD9, (byte)0xFB,
            (byte)0x4B, (byte)0xD2, (byte)0x55, (byte)0x33,
            (byte)0xA8, (byte)0x1A, (byte)0xE6, (byte)0xB5,
            (byte)0x3D, (byte)0x67, (byte)0x46, (byte)0x69,
            (byte)0x6F, (byte)0x0A, (byte)0x64, (byte)0xE7,
            (byte)0x3D, (byte)0xEF, (byte)0x22, (byte)0xBE,
            (byte)0x81, (byte)0x32, (byte)0xF3, (byte)0x72,
            (byte)0xB4, (byte)0x50, (byte)0xE3, (byte)0xC3,
            (byte)0xDB, (byte)0xE5, (byte)0x38, (byte)0x3C,
            (byte)0x60, (byte)0xC8, (byte)0x08, (byte)0x53,
            (byte)0x44, (byte)0x6F, (byte)0xDF, (byte)0x55,
            (byte)0x67, (byte)0x32, (byte)0x02, (byte)0xDD,
            (byte)0x6B, (byte)0xFB, (byte)0x23, (byte)0x1A,
            (byte)0x88, (byte)0x71, (byte)0xE0, (byte)0xF8,
            (byte)0xBB, (byte)0x51, (byte)0x1E, (byte)0x76,
            (byte)0xC9, (byte)0x1F, (byte)0x45, (byte)0x9B,
            (byte)0xA0, (byte)0xA5, (byte)0x61, (byte)0x45,
            (byte)0x9E, (byte)0x65, (byte)0xB8, (byte)0xD6,
            (byte)0x0E, (byte)0x3C, (byte)0xD9, (byte)0x56,
            (byte)0xD6, (byte)0xA6, (byte)0xDD, (byte)0x36,
            (byte)0x21, (byte)0x25, (byte)0x0E, (byte)0xE6,
            (byte)0xAD, (byte)0xA0, (byte)0x3A, (byte)0x9B,
            (byte)0x21, (byte)0x87, (byte)0xE2, (byte)0xAF,
            (byte)0x3A, (byte)0xEF, (byte)0x75, (byte)0x85,
            (byte)0xA8, (byte)0xD7, (byte)0xE5, (byte)0x46,
            (byte)0xD8, (byte)0x5C, (byte)0x17, (byte)0x4E,
            (byte)0x64, (byte)0x51, (byte)0xDB, (byte)0x38,
            (byte)0x8E, (byte)0x6B, (byte)0x02, (byte)0x05,
            (byte)0x46, (byte)0x77, (byte)0xD0, (byte)0x75,
            (byte)0x8A, (byte)0xE0, (byte)0x42, (byte)0x5E,
            (byte)0x8D, (byte)0x49, (byte)0x86, (byte)0xDE,
            (byte)0x6C, (byte)0xBC, (byte)0xAF, (byte)0x10,
            (byte)0x9A, (byte)0x97, (byte)0x64, (byte)0xA6,
            (byte)0xBD, (byte)0xDB, (byte)0x01, (byte)0x40,
            (byte)0xA9, (byte)0x3D, (byte)0x74, (byte)0x99,
            (byte)0xDC, (byte)0x63, (byte)0x34, (byte)0x40,
            (byte)0x31, (byte)0x57, (byte)0xC7, (byte)0x70,
            (byte)0x9F, (byte)0xCE, (byte)0xC6, (byte)0x7B,
            (byte)0x00, (byte)0x5B, (byte)0x02, (byte)0x5C,
            (byte)0xC7, (byte)0x81, (byte)0x40, (byte)0x4D,
            (byte)0xA7, (byte)0xB1, (byte)0xD2, (byte)0xEA,
            (byte)0x8E, (byte)0xEC, (byte)0xA0, (byte)0xB3,
            (byte)0x03, (byte)0x29, (byte)0xB8, (byte)0x44,
            (byte)0xD7, (byte)0xA1, (byte)0x2B, (byte)0x37,
            (byte)0x9D, (byte)0x19, (byte)0x11, (byte)0x1D,
            (byte)0x58, (byte)0xE8, (byte)0x06, (byte)0xE7,
            (byte)0x06, (byte)0xE3, (byte)0xF7, (byte)0xEF,
            (byte)0x05, (byte)0xA9, (byte)0x05, (byte)0x93,
            (byte)0x42, (byte)0x94, (byte)0x5A, (byte)0xD6,
            (byte)0xA0, (byte)0x24, (byte)0x3A, (byte)0x52,
            (byte)0x92, (byte)0xA3, (byte)0x79, (byte)0x98,
            (byte)0x3C, (byte)0x68, (byte)0x55, (byte)0x1B,
            (byte)0x6A, (byte)0xC5, (byte)0x83, (byte)0x89,
            (byte)0x5A, (byte)0x79, (byte)0x5C, (byte)0x52,
            (byte)0xBA, (byte)0xB8, (byte)0xF7, (byte)0x72,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x09,
            (byte)0x4D, (byte)0x41, (byte)0x58, (byte)0x2E,
            (byte)0x4C, (byte)0x4F, (byte)0x43, (byte)0x41,
            (byte)0x4C, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x05, (byte)0x64, (byte)0x75, (byte)0x6D,
            (byte)0x6D, (byte)0x79, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x03, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x0C, (byte)0x58, (byte)0x2D,
            (byte)0x43, (byte)0x41, (byte)0x43, (byte)0x48,
            (byte)0x45, (byte)0x43, (byte)0x4F, (byte)0x4E,
            (byte)0x46, (byte)0x3A, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x15, (byte)0x6B, (byte)0x72,
            (byte)0x62, (byte)0x35, (byte)0x5F, (byte)0x63,
            (byte)0x63, (byte)0x61, (byte)0x63, (byte)0x68,
            (byte)0x65, (byte)0x5F, (byte)0x63, (byte)0x6F,
            (byte)0x6E, (byte)0x66, (byte)0x5F, (byte)0x64,
            (byte)0x61, (byte)0x74, (byte)0x61, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x0A, (byte)0x66,
            (byte)0x61, (byte)0x73, (byte)0x74, (byte)0x5F,
            (byte)0x61, (byte)0x76, (byte)0x61, (byte)0x69,
            (byte)0x6C, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x1A, (byte)0x6B, (byte)0x72, (byte)0x62,
            (byte)0x74, (byte)0x67, (byte)0x74, (byte)0x2F,
            (byte)0x4D, (byte)0x41, (byte)0x58, (byte)0x2E,
            (byte)0x4C, (byte)0x4F, (byte)0x43, (byte)0x41,
            (byte)0x4C, (byte)0x40, (byte)0x4D, (byte)0x41,
            (byte)0x58, (byte)0x2E, (byte)0x4C, (byte)0x4F,
            (byte)0x43, (byte)0x41, (byte)0x4C, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x03, (byte)0x79, (byte)0x65,
            (byte)0x73, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00,
        };

        File f = File.createTempFile("ccache", "cc", new File("."));
        FileOutputStream fout = new FileOutputStream(f);
        fout.write(krb5cc);
        fout.close();

        CredentialsCache cc = CredentialsCache.getInstance(f.getPath());
        if (!cc.getDefaultCreds().getServicePrincipal().getNameStrings()[0]
                .equals("krbtgt")) {
            throw new Exception("No TGT found");
        }
    }
}
