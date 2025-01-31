



package compiler.intrinsics.klass;

import java.util.concurrent.Callable;

public class TestIsPrimitive {
    static final int ITERS = Integer.getInteger("iters", 1);

    public static void main(String... args) throws Exception {
        testOK(true,  InlineConstants::testBoolean);
        testOK(true,  InlineConstants::testByte);
        testOK(true,  InlineConstants::testShort);
        testOK(true,  InlineConstants::testChar);
        testOK(true,  InlineConstants::testInt);
        testOK(true,  InlineConstants::testFloat);
        testOK(true,  InlineConstants::testLong);
        testOK(true,  InlineConstants::testDouble);
        testOK(false, InlineConstants::testObject);
        testOK(false, InlineConstants::testArray);

        testOK(true,  StaticConstants::testBoolean);
        testOK(true,  StaticConstants::testByte);
        testOK(true,  StaticConstants::testShort);
        testOK(true,  StaticConstants::testChar);
        testOK(true,  StaticConstants::testInt);
        testOK(true,  StaticConstants::testFloat);
        testOK(true,  StaticConstants::testLong);
        testOK(true,  StaticConstants::testDouble);
        testOK(false, StaticConstants::testObject);
        testOK(false, StaticConstants::testArray);
        testNPE(      StaticConstants::testNull);

        testOK(true,  NoConstants::testBoolean);
        testOK(true,  NoConstants::testByte);
        testOK(true,  NoConstants::testShort);
        testOK(true,  NoConstants::testChar);
        testOK(true,  NoConstants::testInt);
        testOK(true,  NoConstants::testFloat);
        testOK(true,  NoConstants::testLong);
        testOK(true,  NoConstants::testDouble);
        testOK(false, NoConstants::testObject);
        testOK(false, NoConstants::testArray);
        testNPE(      NoConstants::testNull);
    }

    public static void testOK(boolean expected, Callable<Object> test) throws Exception {
        for (int c = 0; c < ITERS; c++) {
            Object res = test.call();
            if (!res.equals(expected)) {
                throw new IllegalStateException("Wrong result: expected = " + expected + ", but got " + res);
            }
        }
    }

    static volatile Object sink;

    public static void testNPE(Callable<Object> test) throws Exception {
        for (int c = 0; c < ITERS; c++) {
            try {
               sink = test.call();
               throw new IllegalStateException("Expected NPE");
            } catch (NullPointerException iae) {
               
            }
        }
    }

    static volatile Class<?> classBoolean = boolean.class;
    static volatile Class<?> classByte    = byte.class;
    static volatile Class<?> classShort   = short.class;
    static volatile Class<?> classChar    = char.class;
    static volatile Class<?> classInt     = int.class;
    static volatile Class<?> classFloat   = float.class;
    static volatile Class<?> classLong    = long.class;
    static volatile Class<?> classDouble  = double.class;
    static volatile Class<?> classObject  = Object.class;
    static volatile Class<?> classArray   = Object[].class;
    static volatile Class<?> classNull    = null;

    static final Class<?> staticClassBoolean = boolean.class;
    static final Class<?> staticClassByte    = byte.class;
    static final Class<?> staticClassShort   = short.class;
    static final Class<?> staticClassChar    = char.class;
    static final Class<?> staticClassInt     = int.class;
    static final Class<?> staticClassFloat   = float.class;
    static final Class<?> staticClassLong    = long.class;
    static final Class<?> staticClassDouble  = double.class;
    static final Class<?> staticClassObject  = Object.class;
    static final Class<?> staticClassArray   = Object[].class;
    static final Class<?> staticClassNull    = null;

    static class InlineConstants {
        static boolean testBoolean() { return boolean.class.isPrimitive();  }
        static boolean testByte()    { return byte.class.isPrimitive();     }
        static boolean testShort()   { return short.class.isPrimitive();    }
        static boolean testChar()    { return char.class.isPrimitive();     }
        static boolean testInt()     { return int.class.isPrimitive();      }
        static boolean testFloat()   { return float.class.isPrimitive();    }
        static boolean testLong()    { return long.class.isPrimitive();     }
        static boolean testDouble()  { return double.class.isPrimitive();   }
        static boolean testObject()  { return Object.class.isPrimitive();   }
        static boolean testArray()   { return Object[].class.isPrimitive(); }
    }

    static class StaticConstants {
        static boolean testBoolean() { return staticClassBoolean.isPrimitive(); }
        static boolean testByte()    { return staticClassByte.isPrimitive();    }
        static boolean testShort()   { return staticClassShort.isPrimitive();   }
        static boolean testChar()    { return staticClassChar.isPrimitive();    }
        static boolean testInt()     { return staticClassInt.isPrimitive();     }
        static boolean testFloat()   { return staticClassFloat.isPrimitive();   }
        static boolean testLong()    { return staticClassLong.isPrimitive();    }
        static boolean testDouble()  { return staticClassDouble.isPrimitive();  }
        static boolean testObject()  { return staticClassObject.isPrimitive();  }
        static boolean testArray()   { return staticClassArray.isPrimitive();   }
        static boolean testNull()    { return staticClassNull.isPrimitive();    }
    }

    static class NoConstants {
        static boolean testBoolean() { return classBoolean.isPrimitive(); }
        static boolean testByte()    { return classByte.isPrimitive();    }
        static boolean testShort()   { return classShort.isPrimitive();   }
        static boolean testChar()    { return classChar.isPrimitive();    }
        static boolean testInt()     { return classInt.isPrimitive();     }
        static boolean testFloat()   { return classFloat.isPrimitive();   }
        static boolean testLong()    { return classLong.isPrimitive();    }
        static boolean testDouble()  { return classDouble.isPrimitive();  }
        static boolean testObject()  { return classObject.isPrimitive();  }
        static boolean testArray()   { return classArray.isPrimitive();   }
        static boolean testNull()    { return classNull.isPrimitive();    }
    }

}

