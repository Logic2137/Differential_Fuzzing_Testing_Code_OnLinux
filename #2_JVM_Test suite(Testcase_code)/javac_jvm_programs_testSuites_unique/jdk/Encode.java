

import sun.security.x509.*;
import sun.security.util.*;


public class Encode {

    public static void main(String[] args) throws Exception {

        GeneralName gn = new GeneralName(new X500Name("cn=john"));
        DerOutputStream dos = new DerOutputStream();
        gn.encode(dos);
        DerValue dv = new DerValue(dos.toByteArray());
        short tag = (byte)(dv.tag & 0x1f);
        if (tag != GeneralNameInterface.NAME_DIRECTORY) {
            throw new Exception("Invalid tag for Directory name");
        }
        if (!dv.isContextSpecific() || !dv.isConstructed()) {
            throw new Exception("Invalid encoding of Directory name");
        }
        DerInputStream data = dv.getData();
        DerValue[] seq = data.getSequence(5);
    }
}
