

import java.lang.invoke.MethodHandle;

class T8013179 {
    static MethodHandle getNamedMember;
    public static Object getMember(String name, Object rec) throws Throwable {
        return getNamedMember.invoke(rec, name);
    }
}
