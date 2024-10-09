
package test;

import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainGC {

    private static final Path MODS_DIR = Paths.get(System.getProperty("jdk.module.path"));

    static final String MODULE_NAME = "jdk.translet";

    public static void main(String[] args) throws Exception {
        ModuleFinder finder = ModuleFinder.of(MODS_DIR);
        ModuleLayer layerBoot = ModuleLayer.boot();
        Configuration cf = layerBoot.configuration().resolve(ModuleFinder.of(), finder, Set.of(MODULE_NAME));
        Module testModule = MainGC.class.getModule();
        ClassLoader scl = ClassLoader.getSystemClassLoader();
        Callable<Void> task = new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                ModuleLayer layer = ModuleLayer.boot().defineModulesWithOneLoader(cf, scl);
                Module transletModule = layer.findModule(MODULE_NAME).get();
                testModule.addExports("test", transletModule);
                testModule.addReads(transletModule);
                Class<?> c = layer.findLoader(MODULE_NAME).loadClass("translet.MainGC");
                Method method = c.getDeclaredMethod("go");
                method.invoke(null);
                return null;
            }
        };
        List<Future<Void>> results = new ArrayList<>();
        ExecutorService pool = Executors.newFixedThreadPool(Math.min(100, Runtime.getRuntime().availableProcessors() * 10));
        try {
            for (int i = 0; i < 10000; i++) {
                results.add(pool.submit(task));
                if (i == 3000 || i == 6000 || i == 9000) {
                    System.gc();
                }
            }
        } finally {
            pool.shutdown();
        }
        int passed = 0;
        int failed = 0;
        for (Future<Void> result : results) {
            try {
                result.get();
                passed++;
            } catch (Throwable x) {
                x.printStackTrace();
                failed++;
            }
        }
        System.out.println("passed: " + passed);
        System.out.println("failed: " + failed);
    }

    public static void callback() {
    }
}
