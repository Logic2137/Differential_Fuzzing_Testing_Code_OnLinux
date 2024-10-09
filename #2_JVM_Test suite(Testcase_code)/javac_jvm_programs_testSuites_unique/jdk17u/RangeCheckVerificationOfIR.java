
package compiler.c1;

public class RangeCheckVerificationOfIR {

    int a;

    int i1;

    int i2;

    int i3;

    public static void main(String[] args) {
        RangeCheckVerificationOfIR instance = new RangeCheckVerificationOfIR();
        instance.resetValues();
        for (int i = 0; i < 1000; i++) {
            instance.testSimple();
            instance.resetValues();
            instance.testDominatedByXhandler();
            instance.resetValues();
            instance.testThrowOneException();
            instance.resetValues();
            instance.testNestedExceptions();
            instance.resetValues();
            instance.testTriplyNestedExceptions();
            instance.resetValues();
            instance.testTriplyNestedExceptions2();
            instance.resetValues();
            instance.testTriplyNestedMultipleHandlers();
            instance.resetValues();
            instance.testTriplyNestedNoExceptionThrown();
            instance.resetValues();
        }
    }

    private void resetValues() {
        i1 = 0;
        i2 = 0;
        i3 = 0;
    }

    public void testSimple() {
        int[] iArr = new int[8];
        for (int i = 0; i < 8; i++) {
            iArr[0] = 4;
        }
        while (true) {
            try {
                throwException();
                break;
            } catch (Exception ex1) {
                i1++;
            }
        }
        for (int i = 0; i < 10; i++) {
            a = 5;
        }
    }

    public void testDominatedByXhandler() {
        int[] iArr = new int[8];
        for (int i = 0; i < 8; i++) {
            iArr[0] = 4;
        }
        while (true) {
            try {
                throwException();
                break;
            } catch (Exception ex1) {
                if (i1 < i2) {
                    a = 3;
                } else {
                    a = 4;
                }
                i1++;
            }
        }
        for (int i = 0; i < 10; i++) {
            a = 5;
        }
    }

    public void testThrowOneException() {
        int[] iArr = new int[8];
        for (int i = 0; i < 8; i++) {
            iArr[0] = 4;
        }
        try {
            for (int i = 0; i < iArr[4]; i++) {
                throwException();
            }
        } catch (Exception ex) {
            a = 345;
        }
        try {
            while (true) {
                throwException();
                break;
            }
        } catch (Exception e) {
            a = 45;
        }
        for (int i = 0; i < 10; i++) {
            a = 5;
        }
    }

    public void testNestedExceptions() {
        int[] iArr = new int[8];
        for (int i = 0; i < 8; i++) {
            iArr[0] = 4;
        }
        while (true) {
            try {
                throwException();
                break;
            } catch (Exception ex1) {
                i1++;
                try {
                    throwException2();
                } catch (Exception ex2) {
                    if (i1 < i2) {
                        a = 3;
                    } else {
                        a = 4;
                    }
                    i2++;
                }
                if (i1 < i2) {
                    a = 3;
                } else {
                    a = 4;
                }
                i1++;
            }
        }
        for (int i = 0; i < 10; i++) {
            a = 5;
        }
    }

    public void testTriplyNestedExceptions() {
        int[] iArr = new int[8];
        for (int i = 0; i < 8; i++) {
            iArr[0] = 4;
        }
        while (true) {
            try {
                throwException();
                break;
            } catch (Exception ex1) {
                i1++;
                try {
                    throwException2();
                } catch (Exception ex2) {
                    if (i1 < i2) {
                        a = 3;
                    } else {
                        a = 4;
                    }
                    try {
                        throwException3();
                    } catch (Exception ex3) {
                        i3++;
                    }
                    try {
                        throwException3();
                    } catch (Exception ex3) {
                        i3++;
                    }
                    i2++;
                }
                if (i1 < i2) {
                    a = 3;
                } else {
                    a = 4;
                }
                i1++;
            }
        }
        for (int i = 0; i < 10; i++) {
            a = 5;
        }
    }

    public void testTriplyNestedExceptions2() {
        int[] iArr = new int[8];
        for (int i = 0; i < 8; i++) {
            iArr[0] = 4;
        }
        try {
            for (int i = 0; i < iArr[4]; i++) {
                throwException();
            }
        } catch (Exception ex) {
            a = 345;
        }
        while (true) {
            try {
                throwException();
                break;
            } catch (Exception ex1) {
                i1++;
                try {
                    throwException2();
                } catch (Exception ex2) {
                    if (i1 < i2) {
                        a = 3;
                    } else {
                        a = 4;
                    }
                    try {
                        throwException3();
                    } catch (Exception ex3) {
                        i3++;
                    }
                    try {
                        throwException3();
                    } catch (Exception ex3) {
                        i3++;
                    }
                    i2++;
                }
                if (i1 < i2) {
                    a = 3;
                } else {
                    a = 4;
                }
                i1++;
            }
        }
        for (int i = 0; i < 10; i++) {
            a = 5;
        }
    }

    public void testTriplyNestedMultipleHandlers() {
        int[] iArr = new int[8];
        for (int i = 0; i < 8; i++) {
            iArr[0] = 4;
        }
        try {
            for (int i = 0; i < iArr[4]; i++) {
                throwException();
            }
        } catch (Exception ex) {
            a = 345;
        }
        try {
            while (true) {
                try {
                    throwException();
                    break;
                } catch (MyInnerException ie) {
                    i1++;
                    try {
                        throwException2();
                    } catch (Exception ex2) {
                        if (i1 < i2) {
                            a = 3;
                        } else {
                            a = 4;
                        }
                        try {
                            throwException3();
                        } catch (Exception ex3) {
                            i3++;
                        }
                        try {
                            throwException3();
                        } catch (Exception ex3) {
                            i3++;
                        }
                        i2++;
                    }
                    if (i1 < i2) {
                        a = 3;
                    } else {
                        a = 4;
                    }
                    i1++;
                }
            }
        } catch (MyOuterException oe) {
            a = 45;
        }
        for (int i = 0; i < 10; i++) {
            a = 5;
        }
    }

    public void testTriplyNestedNoExceptionThrown() {
        int[] iArr = new int[8];
        for (int i = 0; i < 8; i++) {
            iArr[0] = 4;
        }
        try {
            for (int i = 0; i < iArr[4]; i++) {
                throwException();
            }
        } catch (Exception ex) {
            a = 345;
        }
        try {
            while (true) {
                try {
                    a = 4;
                    break;
                } catch (RuntimeException ie) {
                    i1++;
                    try {
                        throwException2();
                    } catch (Exception ex2) {
                        if (i1 < i2) {
                            a = 3;
                        } else {
                            a = 4;
                        }
                        try {
                            throwException3();
                        } catch (Exception ex3) {
                            i3++;
                        }
                        try {
                            throwException3();
                        } catch (Exception ex3) {
                            i3++;
                        }
                        i2++;
                    }
                    if (i1 < i2) {
                        a = 3;
                    } else {
                        a = 4;
                    }
                    i1++;
                }
            }
        } catch (Exception e) {
            a = 45;
        }
        for (int i = 0; i < 10; i++) {
            a = 5;
        }
    }

    void throwException() throws MyInnerException, MyOuterException {
        if (i1 < 3) {
            throw new MyInnerException();
        }
        if (i1 < 5) {
            throw new MyOuterException();
        }
    }

    public void throwException2() throws Exception {
        if (i2 < 3) {
            throw new RuntimeException();
        }
    }

    public void throwException3() throws Exception {
        if (i3 < 2) {
            throw new RuntimeException();
        }
    }

    class MyInnerException extends Exception {
    }

    class MyOuterException extends Exception {
    }
}
