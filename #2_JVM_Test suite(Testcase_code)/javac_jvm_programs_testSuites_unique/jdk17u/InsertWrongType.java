import java.util.*;

public class InsertWrongType {

    public static void main(String[] args) throws Exception {
        addIllTypedElt(Test32.class, Test33.T1);
        addIllTypedElt(Test127.class, Test128.T1);
        addIllTypedElt(Test32.class, Test33.T33);
        addIllTypedElt(Test127.class, Test128.T128);
        addAllIllTypedElt(Test32.class, Test33.T1);
        addAllIllTypedElt(Test127.class, Test128.T1);
        addAllIllTypedElt(Test32.class, Test33.T33);
        addAllIllTypedElt(Test127.class, Test128.T128);
        addAllEmptyMistypedEnumSet(Test32.class, Test33.class);
        addAllEmptyMistypedEnumSet(Test127.class, Test128.class);
        heterogeneousCopyOf(Test32.T1, Test33.T2);
        heterogeneousCopyOf(Test127.T1, Test128.T2);
        heterogeneousOf2(Test32.T1, Test33.T2);
        heterogeneousOf2(Test127.T1, Test128.T2);
        heterogeneousOf3(Test32.T1, Test33.T2);
        heterogeneousOf3(Test127.T1, Test128.T2);
        heterogeneousOf4(Test32.T1, Test33.T2);
        heterogeneousOf4(Test127.T1, Test128.T2);
        heterogeneousOf5(Test32.T1, Test33.T2);
        heterogeneousOf5(Test127.T1, Test128.T2);
        heterogeneousOfVar(Test32.T1, Test33.T2);
        heterogeneousOfVar(Test127.T1, Test128.T2);
        putIllTypedKey(Test32.class, Test33.T1);
        putIllTypedKey(Test32.class, Test33.T33);
        putAllIllTypedKey(Test32.class, Test33.T1);
        putAllIllTypedKey(Test32.class, Test33.T33);
        putAllIllTypedKeyEnumMap(Test32.class, Test33.T1);
        putAllIllTypedKeyEnumMap(Test32.class, Test33.T33);
        putAllEmptyMistypedEnumMap(Test32.class, Test33.class);
    }

    static void addIllTypedElt(Class enumClass, Enum elt) {
        EnumSet set = EnumSet.noneOf(enumClass);
        try {
            set.add(elt);
        } catch (ClassCastException e) {
            return;
        }
        throw new RuntimeException("Incorrectly typed add succeeded");
    }

    static void addAllIllTypedElt(Class enumClass, Enum elt) {
        EnumSet set = EnumSet.noneOf(enumClass);
        try {
            set.addAll(Collections.singleton(elt));
        } catch (ClassCastException e) {
            return;
        }
        throw new RuntimeException("Incorrectly typed addAll succeeded");
    }

    static void addAllEmptyMistypedEnumSet(Class destClass, Class srcClass) {
        EnumSet dest = EnumSet.noneOf(destClass);
        EnumSet src = EnumSet.noneOf(srcClass);
        dest.addAll(src);
    }

    static void heterogeneousCopyOf(Enum e1, Enum e2) {
        List list = new ArrayList();
        list.add(e1);
        list.add(e2);
        try {
            EnumSet.copyOf(list);
        } catch (ClassCastException e) {
            return;
        }
        throw new RuntimeException("heterogeneous copyOf succeeded");
    }

    static void heterogeneousOf2(Enum e1, Enum e2) {
        try {
            EnumSet.of(e1, e2);
        } catch (ClassCastException e) {
            return;
        }
        throw new RuntimeException("heterogeneous of (2 args) succeeded");
    }

    static void heterogeneousOf3(Enum e1, Enum e2) {
        try {
            EnumSet.of(e1, e1, e2);
        } catch (ClassCastException e) {
            return;
        }
        throw new RuntimeException("heterogeneous of (3 args) succeeded");
    }

    static void heterogeneousOf4(Enum e1, Enum e2) {
        try {
            EnumSet.of(e1, e1, e1, e2);
        } catch (ClassCastException e) {
            return;
        }
        throw new RuntimeException("heterogeneous of (4 args) succeeded");
    }

    static void heterogeneousOf5(Enum e1, Enum e2) {
        try {
            EnumSet.of(e1, e1, e1, e1, e2);
        } catch (ClassCastException e) {
            return;
        }
        throw new RuntimeException("heterogeneous of (5 args) succeeded");
    }

    static void heterogeneousOfVar(Enum e1, Enum e2) {
        try {
            EnumSet.of(e1, e1, e1, e1, e1, e2);
        } catch (ClassCastException e) {
            return;
        }
        throw new RuntimeException("heterogeneous of (Var args) succeeded");
    }

    static void putIllTypedKey(Class enumClass, Enum elt) {
        EnumMap map = new EnumMap(enumClass);
        try {
            map.put(elt, "foofy");
        } catch (ClassCastException e) {
            return;
        }
        throw new RuntimeException("Incorrectly typed put succeeded");
    }

    static void putAllIllTypedKey(Class enumClass, Enum elt) {
        EnumMap dest = new EnumMap(enumClass);
        Map src = new HashMap();
        src.put(elt, "goofy");
        try {
            dest.putAll(src);
        } catch (ClassCastException e) {
            return;
        }
        throw new RuntimeException("Incorrectly typed putAll succeeded");
    }

    static void putAllIllTypedKeyEnumMap(Class enumClass, Enum elt) {
        EnumMap dest = new EnumMap(enumClass);
        Map src = new EnumMap(elt.getClass());
        src.put(elt, "goofy");
        try {
            dest.putAll(src);
        } catch (ClassCastException e) {
            return;
        }
        throw new RuntimeException("Incorrectly typed enum put All succeeded");
    }

    static void putAllEmptyMistypedEnumMap(Class destClass, Class srcClass) {
        EnumMap dest = new EnumMap(destClass);
        EnumMap src = new EnumMap(srcClass);
        dest.putAll(src);
    }

    enum Test32 {

        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        T21,
        T22,
        T23,
        T24,
        T25,
        T26,
        T27,
        T28,
        T29,
        T30,
        T31,
        T32
    }

    enum Test33 {

        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        T21,
        T22,
        T23,
        T24,
        T25,
        T26,
        T27,
        T28,
        T29,
        T30,
        T31,
        T32,
        T33
    }

    enum Test127 {

        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        T21,
        T22,
        T23,
        T24,
        T25,
        T26,
        T27,
        T28,
        T29,
        T30,
        T31,
        T32,
        T33,
        T34,
        T35,
        T36,
        T37,
        T38,
        T39,
        T40,
        T41,
        T42,
        T43,
        T44,
        T45,
        T46,
        T47,
        T48,
        T49,
        T50,
        T51,
        T52,
        T53,
        T54,
        T55,
        T56,
        T57,
        T58,
        T59,
        T60,
        T61,
        T62,
        T63,
        T64,
        T65,
        T66,
        T67,
        T68,
        T69,
        T70,
        T71,
        T72,
        T73,
        T74,
        T75,
        T76,
        T77,
        T78,
        T79,
        T80,
        T81,
        T82,
        T83,
        T84,
        T85,
        T86,
        T87,
        T88,
        T89,
        T90,
        T91,
        T92,
        T93,
        T94,
        T95,
        T96,
        T97,
        T98,
        T99,
        T100,
        T101,
        T102,
        T103,
        T104,
        T105,
        T106,
        T107,
        T108,
        T109,
        T110,
        T111,
        T112,
        T113,
        T114,
        T115,
        T116,
        T117,
        T118,
        T119,
        T120,
        T121,
        T122,
        T123,
        T124,
        T125,
        T126,
        T127
    }

    enum Test128 {

        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        T21,
        T22,
        T23,
        T24,
        T25,
        T26,
        T27,
        T28,
        T29,
        T30,
        T31,
        T32,
        T33,
        T34,
        T35,
        T36,
        T37,
        T38,
        T39,
        T40,
        T41,
        T42,
        T43,
        T44,
        T45,
        T46,
        T47,
        T48,
        T49,
        T50,
        T51,
        T52,
        T53,
        T54,
        T55,
        T56,
        T57,
        T58,
        T59,
        T60,
        T61,
        T62,
        T63,
        T64,
        T65,
        T66,
        T67,
        T68,
        T69,
        T70,
        T71,
        T72,
        T73,
        T74,
        T75,
        T76,
        T77,
        T78,
        T79,
        T80,
        T81,
        T82,
        T83,
        T84,
        T85,
        T86,
        T87,
        T88,
        T89,
        T90,
        T91,
        T92,
        T93,
        T94,
        T95,
        T96,
        T97,
        T98,
        T99,
        T100,
        T101,
        T102,
        T103,
        T104,
        T105,
        T106,
        T107,
        T108,
        T109,
        T110,
        T111,
        T112,
        T113,
        T114,
        T115,
        T116,
        T117,
        T118,
        T119,
        T120,
        T121,
        T122,
        T123,
        T124,
        T125,
        T126,
        T127,
        T128
    }
}
