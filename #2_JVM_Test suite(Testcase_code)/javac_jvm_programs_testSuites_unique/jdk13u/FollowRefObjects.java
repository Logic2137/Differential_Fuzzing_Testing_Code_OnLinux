
package nsk.jvmti.unit.FollowReferences;

import java.lang.ref.*;
import java.lang.reflect.*;

interface Cartier {

    public static final int c21 = 21;

    static final int c22 = 22;
}

interface Bresson {

    public static final int c21 = 21;

    static final int c22 = 22;
}

class HenriCartierBresson implements Cartier, Bresson {

    private final int c31 = 31;

    protected final int c32 = 32;

    public final int c33 = 33;
}

class AlexWebb extends HenriCartierBresson {

    public static final int aw = 50;
}

public class FollowRefObjects {

    static final int ARRAY_SIZE = 3;

    static final int JVMTI_HEAP_REFERENCE_CLASS = 1;

    static final int JVMTI_HEAP_REFERENCE_FIELD = 2;

    static final int JVMTI_HEAP_REFERENCE_ARRAY_ELEMENT = 3;

    static final int JVMTI_HEAP_REFERENCE_CLASS_LOADER = 4;

    static final int JVMTI_HEAP_REFERENCE_SIGNERS = 5;

    static final int JVMTI_HEAP_REFERENCE_PROTECTION_DOMAIN = 6;

    static final int JVMTI_HEAP_REFERENCE_INTERFACE = 7;

    static final int JVMTI_HEAP_REFERENCE_STATIC_FIELD = 8;

    static final int JVMTI_HEAP_REFERENCE_CONSTANT_POOL = 9;

    static final int JVMTI_HEAP_REFERENCE_SUPERCLASS = 10;

    static final int JVMTI_HEAP_REFERENCE_JNI_GLOBAL = 21;

    static final int JVMTI_HEAP_REFERENCE_SYSTEM_CLASS = 22;

    static final int JVMTI_HEAP_REFERENCE_MONITOR = 23;

    static final int JVMTI_HEAP_REFERENCE_STACK_LOCAL = 24;

    static final int JVMTI_HEAP_REFERENCE_JNI_LOCAL = 25;

    static final int JVMTI_HEAP_REFERENCE_THREAD = 26;

    static final int JVMTI_HEAP_REFERENCE_OTHER = 27;

    public boolean[] _boolArr;

    public byte[] _byteArr;

    public short[] _shortArr;

    public int[] _intArr;

    public long[] _longArr;

    public float[] _floatArr;

    public double[] _doubleArr;

    public String[] _strArr;

    public Object[] _objArr;

    public Object[][] _objArrArr;

    class FRHandler implements InvocationHandler {

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    public SoftReference<Object> _softRef;

    public Object _softReferree;

    public WeakReference<Object> _weakRef;

    public Object _weakReferree;

    public PhantomReference<Object> _phantomRef;

    public Object _phantomReferree;

    public FollowRefObjects _selfRef1, _selfRef2, _selfRef3;

    public Thread _thread;

    public ClassLoader _classLoader;

    public Object _loadedObject;

    class NextRef {

        Object _next;
    }

    public NextRef[] _nextRef;

    class BrunoBarbey extends HenriCartierBresson {

        public static final int bb = 60;
    }

    public AlexWebb _aw;

    public BrunoBarbey _bb;

    public Class _reflectClass;

    public Cartier _cartierInAMirror;

    public void createObjects() {
        resetCurTag();
        tag(this, "this");
        _boolArr = new boolean[ARRAY_SIZE];
        tag(_boolArr, "_boolArr");
        _byteArr = new byte[ARRAY_SIZE];
        tag(_byteArr, "_byteArr");
        _shortArr = new short[ARRAY_SIZE];
        tag(_shortArr, "_shortArr");
        _intArr = new int[ARRAY_SIZE];
        tag(_intArr, "_intArr");
        _longArr = new long[ARRAY_SIZE];
        tag(_longArr, "_longArr");
        _floatArr = new float[ARRAY_SIZE];
        tag(_floatArr, "_floatArr");
        _doubleArr = new double[ARRAY_SIZE];
        tag(_doubleArr, "_doubleArr");
        _objArrArr = new Object[ARRAY_SIZE][ARRAY_SIZE];
        tag(_objArrArr, "_objArrArr");
        _objArr = new Object[ARRAY_SIZE];
        tag(_objArr, "_objArr");
        _objArr[0] = new Object();
        tag(_objArr[0], "_objArr[0]");
        _objArr[1] = _objArr[2] = _objArr[0];
        _selfRef1 = _selfRef2 = _selfRef3 = this;
        _nextRef = new NextRef[2];
        tag(_nextRef, "_nextRef");
        _nextRef[0] = new NextRef();
        tag(_nextRef[0], "_nextRef[0]");
        _nextRef[1] = new NextRef();
        tag(_nextRef[1], "_nextRef[1]");
        _nextRef[0]._next = _nextRef[1];
        _nextRef[1]._next = _nextRef[0];
        _strArr = new String[ARRAY_SIZE];
        tag(_strArr, "_strArr");
        
        _strArr[0] = "CTAKAH OT CAXAPA HE TPECHET";
        tag(_strArr[0], "_strArr[0]");
        
        _strArr[1] = "?????? ?? ??????? ???????!";
        tag(_strArr[1], "_strArr[1]");
        
        _strArr[2] = getClass().getCanonicalName();
        tag(_strArr[2], "_strArr[2]");
        _reflectClass = java.lang.reflect.Proxy.getProxyClass(getClass().getClassLoader(), new Class[] { Cartier.class });
        tag(_reflectClass, "_reflectClass");
        _cartierInAMirror = (Cartier) java.lang.reflect.Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { Cartier.class }, new FRHandler());
        tag(_cartierInAMirror, "_cartierInAMirror");
        _softRef = new SoftReference<Object>(_softReferree = new Object());
        tag(_softRef, "_softRef");
        tag(_softReferree, "_softReferree");
        _weakRef = new WeakReference<Object>(_weakReferree = new Object());
        tag(_weakRef, "_weakRef");
        tag(_weakReferree, "_weakReferree");
        _phantomRef = new PhantomReference<Object>(_phantomReferree = new Object(), null);
        tag(_phantomRef, "_phantomRef");
        tag(_phantomReferree, "_phantomReferree");
        _thread = new Thread();
        tag(_thread, "_thread");
        _classLoader = new java.net.URLClassLoader(new java.net.URL[] {});
        tag(_classLoader, "_classLoader");
        try {
            _loadedObject = _classLoader.loadClass("java/lang/SecurityManager");
            tag(_loadedObject, "_loadedObject");
        } catch (ClassNotFoundException e) {
        }
        _aw = new AlexWebb();
        tag(_aw, "_aw");
        _bb = new BrunoBarbey();
        tag(_bb, "_bb");
    }

    public void dropObjects() {
        _boolArr = null;
        _byteArr = null;
        _shortArr = null;
        _intArr = null;
        _longArr = null;
        _floatArr = null;
        _doubleArr = null;
        _objArrArr = null;
        _objArr = null;
        _selfRef1 = _selfRef2 = _selfRef3 = null;
        _nextRef = null;
        _strArr = null;
        _reflectClass = null;
        _cartierInAMirror = null;
        _softRef = null;
        _softReferree = null;
        _weakRef = null;
        _weakReferree = null;
        _phantomRef = null;
        _phantomReferree = null;
        _thread = null;
        _classLoader = null;
        _loadedObject = null;
        _aw = null;
        _bb = null;
    }

    private long _curTag;

    private void resetCurTag() {
        _curTag = 1;
        resetTags();
    }

    private void tag(Object o) {
        setTag(o, _curTag++, "(" + ((o == null) ? "null" : o.getClass().getCanonicalName()) + ")");
    }

    private void tag(Object o, String fieldName) {
        setTag(o, _curTag++, fieldName + "(" + ((o == null) ? "null" : o.getClass().getCanonicalName()) + ")");
    }

    private void ref(Object from, Object to, int jvmtiRefKind, int count) {
        addRefToVerify(from, to, jvmtiRefKind, count);
    }

    public static native void resetTags();

    public static native boolean setTag(Object o, long tag, String sInfo);

    public static native long getTag(Object o);

    public static native void resetRefsToVerify();

    public static native boolean addRefToVerify(Object from, Object to, int jvmtiRefKind, int count);
}
