
package util;

import java.lang.reflect.AccessibleObject;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import static util.MemberFactory.Kind.CONSTRUCTOR;
import static util.MemberFactory.Kind.FIELD;
import static util.MemberFactory.Kind.METHOD;

public enum MemberFactory implements Function<Class<?>, AccessibleObject> {

    PRIVATE_INSTANCE_FIELD(FIELD, "privateInstance"),
    PACKAGE_INSTANCE_FIELD(FIELD, "packageInstance"),
    PROTECTED_INSTANCE_FIELD(FIELD, "protectedInstance"),
    PUBLIC_INSTANCE_FIELD(FIELD, "publicInstance"),
    PRIVATE_INSTANCE_METHOD(METHOD, "privateInstance"),
    PACKAGE_INSTANCE_METHOD(METHOD, "packageInstance"),
    PROTECTED_INSTANCE_METHOD(METHOD, "protectedInstance"),
    PUBLIC_INSTANCE_METHOD(METHOD, "publicInstance"),
    PRIVATE_STATIC_FIELD(FIELD, "privateStatic"),
    PACKAGE_STATIC_FIELD(FIELD, "packageStatic"),
    PROTECTED_STATIC_FIELD(FIELD, "protectedStatic"),
    PUBLIC_STATIC_FIELD(FIELD, "publicStatic"),
    PRIVATE_STATIC_METHOD(METHOD, "privateStatic"),
    PACKAGE_STATIC_METHOD(METHOD, "packageStatic"),
    PROTECTED_STATIC_METHOD(METHOD, "protectedStatic"),
    PUBLIC_STATIC_METHOD(METHOD, "publicStatic"),
    PRIVATE_CONSTRUCTOR(CONSTRUCTOR, null, Void.class, Void.class, Void.class),
    PACKAGE_CONSTRUCTOR(CONSTRUCTOR, null, Void.class, Void.class),
    PROTECTED_CONSTRUCTOR(CONSTRUCTOR, null, Void.class),
    PUBLIC_CONSTRUCTOR(CONSTRUCTOR, null);

    final Kind kind;

    final String name;

    final Class<?>[] parameterTypes;

    MemberFactory(Kind kind, String name, Class<?>... parameterTypes) {
        this.kind = kind;
        this.name = name;
        this.parameterTypes = parameterTypes;
    }

    @Override
    public AccessibleObject apply(Class<?> declaringClass) {
        return kind.apply(declaringClass, this);
    }

    public static EnumSet<MemberFactory> asSet(MemberFactory... members) {
        return members.length == 0 ? EnumSet.noneOf(MemberFactory.class) : EnumSet.copyOf(Arrays.asList(members));
    }

    public static EnumSet<Group> membersToGroupsOrNull(EnumSet<MemberFactory> members) {
        EnumSet<MemberFactory> mSet = members.clone();
        EnumSet<Group> gSet = EnumSet.allOf(Group.class);
        Iterator<Group> gIter = gSet.iterator();
        while (gIter.hasNext()) {
            Group g = gIter.next();
            if (mSet.containsAll(g.members)) {
                mSet.removeAll(g.members);
            } else {
                gIter.remove();
            }
        }
        return mSet.isEmpty() ? gSet : null;
    }

    public static EnumSet<MemberFactory> groupsToMembers(EnumSet<Group> groups) {
        EnumSet<MemberFactory> mSet = EnumSet.noneOf(MemberFactory.class);
        for (Group g : groups) {
            mSet.addAll(g.members);
        }
        return mSet;
    }

    enum Kind implements BiFunction<Class<?>, MemberFactory, AccessibleObject> {

        FIELD {

            @Override
            public AccessibleObject apply(Class<?> declaringClass, MemberFactory factory) {
                assert factory.kind == this;
                try {
                    return declaringClass.getDeclaredField(factory.name);
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        , METHOD {

            @Override
            public AccessibleObject apply(Class<?> declaringClass, MemberFactory factory) {
                assert factory.kind == this;
                try {
                    return declaringClass.getDeclaredMethod(factory.name, factory.parameterTypes);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        , CONSTRUCTOR {

            @Override
            public AccessibleObject apply(Class<?> declaringClass, MemberFactory factory) {
                assert factory.kind == this;
                try {
                    return declaringClass.getDeclaredConstructor(factory.parameterTypes);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }

    }

    public enum Group {

        ALL(MemberFactory.values()),
        PRIVATE_MEMBERS(PRIVATE_INSTANCE_FIELD, PRIVATE_INSTANCE_METHOD, PRIVATE_STATIC_FIELD, PRIVATE_STATIC_METHOD, PRIVATE_CONSTRUCTOR),
        PACKAGE_MEMBERS(PACKAGE_INSTANCE_FIELD, PACKAGE_INSTANCE_METHOD, PACKAGE_STATIC_FIELD, PACKAGE_STATIC_METHOD, PACKAGE_CONSTRUCTOR),
        PROTECTED_MEMBERS(PROTECTED_INSTANCE_FIELD, PROTECTED_INSTANCE_METHOD, PROTECTED_STATIC_FIELD, PROTECTED_STATIC_METHOD, PROTECTED_CONSTRUCTOR),
        PUBLIC_MEMBERS(PUBLIC_INSTANCE_FIELD, PUBLIC_INSTANCE_METHOD, PUBLIC_STATIC_FIELD, PUBLIC_STATIC_METHOD, PUBLIC_CONSTRUCTOR),
        PRIVATE_INSTANCE_F_M(PRIVATE_INSTANCE_FIELD, PRIVATE_INSTANCE_METHOD),
        PACKAGE_INSTANCE_F_M(PACKAGE_INSTANCE_FIELD, PACKAGE_INSTANCE_METHOD),
        PROTECTED_INSTANCE_F_M(PROTECTED_INSTANCE_FIELD, PROTECTED_INSTANCE_METHOD),
        PUBLIC_INSTANCE_F_M(PUBLIC_INSTANCE_FIELD, PUBLIC_INSTANCE_METHOD),
        PRIVATE_STATIC_F_M(PRIVATE_STATIC_FIELD, PRIVATE_STATIC_METHOD),
        PACKAGE_STATIC_F_M(PACKAGE_STATIC_FIELD, PACKAGE_STATIC_METHOD),
        PROTECTED_STATIC_F_M(PROTECTED_STATIC_FIELD, PROTECTED_STATIC_METHOD),
        PUBLIC_STATIC_F_M(PUBLIC_STATIC_FIELD, PUBLIC_STATIC_METHOD),
        PRIVATE_C(PRIVATE_CONSTRUCTOR),
        PACKAGE_C(PACKAGE_CONSTRUCTOR),
        PROTECTED_C(PROTECTED_CONSTRUCTOR),
        PUBLIC_C(PUBLIC_CONSTRUCTOR);

        final EnumSet<MemberFactory> members;

        Group(MemberFactory... members) {
            this.members = EnumSet.copyOf(Arrays.asList(members));
        }

        public static EnumSet<Group> asSet(Group... groups) {
            return groups.length == 0 ? EnumSet.noneOf(Group.class) : EnumSet.copyOf(Arrays.asList(groups));
        }
    }
}
