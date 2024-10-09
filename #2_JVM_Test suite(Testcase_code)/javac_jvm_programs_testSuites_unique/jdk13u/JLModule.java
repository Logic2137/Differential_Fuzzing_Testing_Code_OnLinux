
package com.oracle.mxtool.junit;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

class JLModule {

    private final Object realModule;

    JLModule(Object module) {
        this.realModule = module;
    }

    private static final Class<?> moduleClass;

    private static final Class<?> layerClass;

    private static final Method bootMethod;

    private static final Method modulesMethod;

    private static final Method getModuleMethod;

    private static final Method getUnnamedModuleMethod;

    private static final Method getNameMethod;

    private static final Method getPackagesMethod;

    private static final Method isExportedMethod;

    private static final Method isExported2Method;

    private static final Method addExportsMethod;

    private static final Method addOpensMethod;

    static {
        try {
            moduleClass = findModuleClass();
            Class<?> modulesClass = Class.forName("jdk.internal.module.Modules");
            layerClass = findModuleLayerClass();
            bootMethod = layerClass.getMethod("boot");
            modulesMethod = layerClass.getMethod("modules");
            getModuleMethod = Class.class.getMethod("getModule");
            getUnnamedModuleMethod = ClassLoader.class.getMethod("getUnnamedModule");
            getNameMethod = moduleClass.getMethod("getName");
            getPackagesMethod = moduleClass.getMethod("getPackages");
            isExportedMethod = moduleClass.getMethod("isExported", String.class);
            isExported2Method = moduleClass.getMethod("isExported", String.class, moduleClass);
            addExportsMethod = modulesClass.getDeclaredMethod("addExports", moduleClass, String.class, moduleClass);
            addOpensMethod = getDeclaredMethodOptional(modulesClass, "addOpens", moduleClass, String.class, moduleClass);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    protected static Class<?> findModuleClass() throws ClassNotFoundException {
        try {
            return Class.forName("java.lang.Module");
        } catch (ClassNotFoundException e) {
            return Class.forName("java.lang.reflect.Module");
        }
    }

    protected static Class<?> findModuleLayerClass() throws ClassNotFoundException {
        try {
            return Class.forName("java.lang.ModuleLayer");
        } catch (ClassNotFoundException e) {
            return Class.forName("java.lang.reflect.Layer");
        }
    }

    private static Method getDeclaredMethodOptional(Class<?> declaringClass, String name, Class<?>... parameterTypes) {
        try {
            return declaringClass.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static JLModule fromClass(Class<?> cls) {
        try {
            return new JLModule(getModuleMethod.invoke(cls));
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static JLModule find(String name) {
        try {
            Object bootLayer = bootMethod.invoke(null);
            Set<Object> modules = (Set<Object>) modulesMethod.invoke(bootLayer);
            for (Object m : modules) {
                JLModule module = new JLModule(m);
                String mname = module.getName();
                if (mname.equals(name)) {
                    return module;
                }
            }
        } catch (Exception e) {
            throw new InternalError(e);
        }
        return null;
    }

    public static JLModule getUnnamedModuleFor(ClassLoader cl) {
        try {
            return new JLModule(getUnnamedModuleMethod.invoke(cl));
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public String getName() {
        try {
            return (String) getNameMethod.invoke(realModule);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public void exportAllPackagesTo(JLModule module) {
        if (this != module) {
            for (String pkg : getPackages()) {
                if (!isExported(pkg, module)) {
                    addExports(pkg, module);
                    addOpens(pkg, module);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Iterable<String> getPackages() {
        try {
            Object res = getPackagesMethod.invoke(realModule);
            if (res instanceof String[]) {
                return Arrays.asList((String[]) res);
            }
            return (Set<String>) res;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public boolean isExported(String pn) {
        try {
            return (Boolean) isExportedMethod.invoke(realModule, pn);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public boolean isExported(String pn, JLModule other) {
        try {
            return (Boolean) isExported2Method.invoke(realModule, pn, other.realModule);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public void addExports(String pn, JLModule other) {
        try {
            addExportsMethod.invoke(null, realModule, pn, other.realModule);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public void addOpens(String pn, JLModule other) {
        if (addOpensMethod != null) {
            try {
                addOpensMethod.invoke(null, realModule, pn, other.realModule);
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }
    }

    @Override
    public String toString() {
        return realModule.toString();
    }
}
