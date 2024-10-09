


package compiler.c2;

import java.util.ArrayList;

public class TestLiveNodeLimit {

    public static ArrayList<String> arrList = new ArrayList<String>();

    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            testNodeLimitInBuildCfg();
            testNodeLimitInGlobalCodeMotion();
        }
    }

    
    
    public static int testNodeLimitInBuildCfg() {
        
        
        String someString = "as@df#as@df" + "fdsa";
        String[] split = someString.split("#");
        String[] tmp1 = split[0].split("@");
        String[] tmp2 = split[0].split("@");
        String concat1 = tmp1[0] + tmp2[0] + split[0];
        String concat2 = tmp1[0] + tmp2[0] + split[0];
        String concat3 = tmp1[0] + tmp2[0];
        String concat4 = tmp1[0] + tmp2[0];
        String string1 = "string1";
        String[] stringArr1 = arrList.toArray(new String[4]);
        String[] stringArr2 = new String[3];
        String[] stringArr3 = new String[3];
        if (stringArr1.length > 1) {
            stringArr2 = new String[3 * stringArr1.length];
            stringArr3 = new String[3 * stringArr1.length];
            for (int i = 0; i < stringArr1.length; i++) {
                stringArr2[3 * i    ] = string1 + concat1 + concat3 + stringArr1[i];
                stringArr2[3 * i + 1] = string1 + concat1 + concat3 + stringArr1[i];
                stringArr2[3 * i + 2] = string1 + concat1 + concat3 + stringArr1[i];
                stringArr3[3 * i    ] = string1 + concat2 + concat3 + concat4 + stringArr1[i];
                stringArr3[3 * i + 1] = string1 + concat2 + concat3 + concat4 + stringArr1[i];
                stringArr3[3 * i + 2] = string1 + concat2 + concat3 + concat4 + stringArr1[i];
            }
        }
        return stringArr1.length + stringArr2.length + stringArr3.length;
    }

    
    
    public static int testNodeLimitInGlobalCodeMotion() {
        
        
        String someString = "as@df#as@df" + "fdsa";
        String[] split = someString.split("#");
        String[] tmp1 = split[0].split("@");
        String[] tmp2 = split[0].split("@");
        String concat1 = tmp1[0] + tmp2[0] + split[0];
        String concat2 = tmp1[0] + tmp2[0] + split[0];
        String concat3 = tmp1[0] + tmp2[0] + split[0];
        String concat4 = tmp1[0] + tmp2[0] + split[0];
        String string1 = "string1";
        String[] stringArr1 = arrList.toArray(new String[4]);
        String[] stringArr2 = new String[3];
        String[] stringArr3 = new String[3];
        if (stringArr1.length > 1) {
            stringArr2 = new String[3 * stringArr1.length];
            stringArr3 = new String[3 * stringArr1.length];
            for (int i = 0; i < stringArr1.length; i++) {
                stringArr2[3 * i    ] = string1 + concat1 + concat3 + stringArr1[i];
                stringArr2[3 * i + 1] = string1 + concat1 + concat3 + stringArr1[i];
                stringArr2[3 * i + 2] = string1 + concat1 + concat3 + stringArr1[i];
                stringArr3[3 * i    ] = string1 + concat2 + concat4 + stringArr1[i];
                stringArr3[3 * i + 1] = string1 + concat2 + concat4 + stringArr1[i];
                stringArr3[3 * i + 2] = string1 + concat2 + stringArr1[i];
            }
        }
        return stringArr1.length + stringArr2.length + stringArr3.length;
    }
}

