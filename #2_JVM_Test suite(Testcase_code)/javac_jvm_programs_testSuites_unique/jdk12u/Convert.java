

package jdk.test.lib;

import java.math.BigInteger;



public class Convert {

    
    public static String byteArrayToHexString(byte[] arr) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < arr.length; ++i) {
            byte curVal = arr[i];
            result.append(Character.forDigit(curVal >> 4 & 0xF, 16));
            result.append(Character.forDigit(curVal & 0xF, 16));
        }
        return result.toString();
    }

    
    public static byte[] byteToByteArray(byte v, int length) {
        byte[] result = new byte[length];
        result[0] = v;
        return result;
    }

    
    public static byte[] hexStringToByteArray(String str) {
        byte[] result = new byte[str.length() / 2];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) Character.digit(str.charAt(2 * i), 16);
            result[i] <<= 4;
            result[i] += Character.digit(str.charAt(2 * i + 1), 16);
        }
        return result;
    }

    
    public static
    BigInteger hexStringToBigInteger(boolean clearHighBit, String str) {
        BigInteger result = BigInteger.ZERO;
        for (int i = 0; i < str.length() / 2; i++) {
            int curVal = Character.digit(str.charAt(2 * i), 16);
            curVal <<= 4;
            curVal += Character.digit(str.charAt(2 * i + 1), 16);
            if (clearHighBit && i == str.length() / 2 - 1) {
                curVal &= 0x7F;
            }
            result = result.add(BigInteger.valueOf(curVal).shiftLeft(8 * i));
        }
        return result;
    }
}


