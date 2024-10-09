import java.io.ByteArrayInputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.security.Permissions;
import java.util.Base64;

public class DeserializeInvalidPermissions {

    private static final String BASE = System.getProperty("test.src", ".");

    private static String INVALID_PERMISSIONS = "rO0ABXNyABlqYXZhLnNlY3VyaXR5LlBlcm1pc3Npb25zQ21LTdLID1ADAAJMAA1hbGxQ" + "ZXJtaXNzaW9udAAkTGphdmEvc2VjdXJpdHkvUGVybWlzc2lvbkNvbGxlY3Rpb247TAAF" + "cGVybXN0ABVMamF2YS91dGlsL0hhc2h0YWJsZTt4cgAiamF2YS5zZWN1cml0eS5QZXJt" + "aXNzaW9uQ29sbGVjdGlvbqKk2tZqGAkpAgABWgAIcmVhZE9ubHl4cABwc3IAE2phdmEu" + "dXRpbC5IYXNodGFibGUTuw8lIUrkuAMAAkYACmxvYWRGYWN0b3JJAAl0aHJlc2hvbGR4" + "cD9AAAAAAAADdwgAAAAEAAAAAnZyABZqYXZhLmlvLkZpbGVQZXJtaXNzaW9ubg+fk/TA" + "qbsDAAFMAAdhY3Rpb25zdAASTGphdmEvbGFuZy9TdHJpbmc7eHIAGGphdmEuc2VjdXJp" + "dHkuUGVybWlzc2lvbrHG4T8oV1F+AgABTAAEbmFtZXEAfgAIeHBzcgAnamF2YS5zZWN1" + "cml0eS5CYXNpY1Blcm1pc3Npb25Db2xsZWN0aW9uCkKHBI3t48cDAANaAAthbGxfYWxs" + "b3dlZEwACXBlcm1DbGFzc3QAEUxqYXZhL2xhbmcvQ2xhc3M7TAALcGVybWlzc2lvbnNx" + "AH4AAnhxAH4AAwAAdnIAIGphdmEuc2VjdXJpdHkuU2VjdXJpdHlQZXJtaXNzaW9uSKpm" + "PrGHHSYCAAB4cgAdamF2YS5zZWN1cml0eS5CYXNpY1Blcm1pc3Npb25XJQvcz06megIA" + "AHhxAH4ACXNxAH4ABT9AAAAAAAABdwgAAAACAAAAAXQAA2Zvb3NxAH4ADnEAfgASeHhx" + "AH4AEHNyACBqYXZhLmlvLkZpbGVQZXJtaXNzaW9uQ29sbGVjdGlvbh6SeX3UjlWpAwAB" + "TAALcGVybWlzc2lvbnN0ABJMamF2YS91dGlsL1ZlY3Rvcjt4cQB+AAMAc3IAEGphdmEu" + "dXRpbC5WZWN0b3LZl31bgDuvAQMAA0kAEWNhcGFjaXR5SW5jcmVtZW50SQAMZWxlbWVu" + "dENvdW50WwALZWxlbWVudERhdGF0ABNbTGphdmEvbGFuZy9PYmplY3Q7eHAAAAAAAAAA" + "AXVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAAAAFzcQB+AAd0AANi" + "YXJ0AARyZWFkeHh4eHg=";

    private static String INVALID_PERMISSIONS_HASH = "rO0ABXNyABlqYXZhLnNlY3VyaXR5LlBlcm1pc3Npb25zQ21LTdLID1ADAAJMAA1hbGxQ" + "ZXJtaXNzaW9udAAkTGphdmEvc2VjdXJpdHkvUGVybWlzc2lvbkNvbGxlY3Rpb247TAAF" + "cGVybXN0ABVMamF2YS91dGlsL0hhc2h0YWJsZTt4cgAiamF2YS5zZWN1cml0eS5QZXJt" + "aXNzaW9uQ29sbGVjdGlvbqKk2tZqGAkpAgABWgAIcmVhZE9ubHl4cAFwc3IAE2phdmEu" + "dXRpbC5IYXNodGFibGUTuw8lIUrkuAMAAkYACmxvYWRGYWN0b3JJAAl0aHJlc2hvbGR4" + "cD9AAAAAAAAEdwgAAAAGAAAAA3ZyABZqYXZhLmlvLkZpbGVQZXJtaXNzaW9ubg+fk/TA" + "qbsDAAFMAAdhY3Rpb25zdAASTGphdmEvbGFuZy9TdHJpbmc7eHIAGGphdmEuc2VjdXJp" + "dHkuUGVybWlzc2lvbrHG4T8oV1F+AgABTAAEbmFtZXEAfgAIeHBzcgAgamF2YS5pby5G" + "aWxlUGVybWlzc2lvbkNvbGxlY3Rpb24eknl91I5VqQMAAUwAC3Blcm1pc3Npb25zdAAS" + "TGphdmEvdXRpbC9WZWN0b3I7eHEAfgADAHNyABBqYXZhLnV0aWwuVmVjdG9y2Zd9W4A7" + "rwEDAANJABFjYXBhY2l0eUluY3JlbWVudEkADGVsZW1lbnRDb3VudFsAC2VsZW1lbnRE" + "YXRhdAATW0xqYXZhL2xhbmcvT2JqZWN0O3hwAAAAAAAAAAF1cgATW0xqYXZhLmxhbmcu" + "T2JqZWN0O5DOWJ8QcylsAgAAeHAAAAABc3EAfgAHdAADYmFydAAEcmVhZHh4eHZyAC9q" + "YXZheC5zZWN1cml0eS5hdXRoLlByaXZhdGVDcmVkZW50aWFsUGVybWlzc2lvbklV3Hd7" + "UH9MAgADWgAHdGVzdGluZ0wAD2NyZWRlbnRpYWxDbGFzc3EAfgAITAAKcHJpbmNpcGFs" + "c3QAD0xqYXZhL3V0aWwvU2V0O3hxAH4ACXNyAB1qYXZhLnNlY3VyaXR5LlBlcm1pc3Np" + "b25zSGFzaIomZbSmPV1AAwABTAAFcGVybXNxAH4AAnhxAH4AAwBzcQB+AAU/QAAAAAAA" + "AXcIAAAAAgAAAAFzcQB+ABZ0ACphLmIuUHJpdmF0ZUNyZWRlbnRpYWwgYS5iLlByaW5j" + "aXBhbCAiZHVrZSIAdAAVYS5iLlByaXZhdGVDcmVkZW50aWFscHNyACBqYXZhLnNlY3Vy" + "aXR5LlNlY3VyaXR5UGVybWlzc2lvbkiqZj6xhx0mAgAAeHIAHWphdmEuc2VjdXJpdHku" + "QmFzaWNQZXJtaXNzaW9uVyUL3M9OpnoCAAB4cQB+AAl0AANmb294eHZxAH4AH3NyACdq" + "YXZhLnNlY3VyaXR5LkJhc2ljUGVybWlzc2lvbkNvbGxlY3Rpb24KQocEje3jxwMAA1oA" + "C2FsbF9hbGxvd2VkTAAJcGVybUNsYXNzdAARTGphdmEvbGFuZy9DbGFzcztMAAtwZXJt" + "aXNzaW9uc3EAfgACeHEAfgADAABxAH4AI3NxAH4ABT9AAAAAAAABdwgAAAACAAAAAXEA" + "fgAic3EAfgAfcQB+ACJ4eHh4";

    public static void main(String[] args) throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();
        deserialize(decoder.decode(INVALID_PERMISSIONS));
        deserialize(decoder.decode(INVALID_PERMISSIONS_HASH));
    }

    private static void deserialize(byte[] serialBytes) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serialBytes))) {
            try {
                Permissions p = (Permissions) ois.readObject();
                throw new Exception("expected InvalidObjectException");
            } catch (InvalidObjectException ioe) {
            }
        }
    }
}
