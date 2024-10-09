

package jdk.experimental.bytecode;

import java.util.function.Consumer;
import java.util.function.ToIntBiFunction;


public interface PoolHelper<S, T, E> {
    int putClass(S symbol);

    int putFieldRef(S owner, CharSequence name, T type);

    int putMethodRef(S owner, CharSequence name, T type, boolean isInterface);

    int putUtf8(CharSequence s);

    int putInt(int i);

    int putFloat(float f);

    int putLong(long l);

    int putDouble(double d);

    int putString(String s);

    int putType(T t);

    int putMethodType(T t);

    int putHandle(int refKind, S owner, CharSequence name, T type);

    int putHandle(int refKind, S owner, CharSequence name, T type, boolean isInterface);

    int putInvokeDynamic(CharSequence invokedName, T invokedType, S bsmClass, CharSequence bsmName, T bsmType, Consumer<StaticArgListBuilder<S, T, E>> staticArgs);

    int putDynamicConstant(CharSequence constName, T constType, S bsmClass, CharSequence bsmName, T bsmType, Consumer<StaticArgListBuilder<S, T, E>> staticArgs);

    int size();

    E entries();

    interface StaticArgListBuilder<S, T, E> {
        StaticArgListBuilder<S, T, E> add(int i);
        StaticArgListBuilder<S, T, E> add(float f);
        StaticArgListBuilder<S, T, E> add(long l);
        StaticArgListBuilder<S, T, E> add(double d);
        StaticArgListBuilder<S, T, E> add(String s);
        StaticArgListBuilder<S, T, E> add(int refKind, S owner, CharSequence name, T type);
        <Z> StaticArgListBuilder<S, T, E> add(Z z, ToIntBiFunction<PoolHelper<S, T, E>, Z> poolFunc);
        StaticArgListBuilder<S, T, E> add(CharSequence constName, T constType, S bsmClass, CharSequence bsmName, T bsmType, Consumer<StaticArgListBuilder<S, T, E>> staticArgList);
    }
}
