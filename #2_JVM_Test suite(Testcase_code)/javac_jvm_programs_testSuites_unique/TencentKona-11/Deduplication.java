
package com.sun.tools.javac.comp;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Deduplication {

    void group(Object... xs) {
    }

    void test() {
        group((Runnable) () -> {
            ((Runnable) () -> {
            }).run();
        }, (Runnable) () -> {
            ((Runnable) () -> {
            }).run();
        });
        group((Runnable) () -> {
            Deduplication.class.toString();
        }, (Runnable) () -> {
            Deduplication.class.toString();
        });
        group((Runnable) () -> {
            Integer[].class.toString();
        }, (Runnable) () -> {
            Integer[].class.toString();
        });
        group((Runnable) () -> {
            char.class.toString();
        }, (Runnable) () -> {
            char.class.toString();
        });
        group((Runnable) () -> {
            Void.class.toString();
        }, (Runnable) () -> {
            Void.class.toString();
        });
        group((Runnable) () -> {
            void.class.toString();
        }, (Runnable) () -> {
            void.class.toString();
        });
        group((Function<String, Integer>) x -> x.hashCode());
        group((Function<Object, Integer>) x -> x.hashCode());
        {
            int x = 1;
            group((Supplier<Integer>) () -> x + 1);
        }
        {
            int x = 1;
            group((Supplier<Integer>) () -> x + 1);
        }
        group((BiFunction<Integer, Integer, ?>) (x, y) -> x + ((y)), (BiFunction<Integer, Integer, ?>) (x, y) -> x + (y), (BiFunction<Integer, Integer, ?>) (x, y) -> x + y, (BiFunction<Integer, Integer, ?>) (x, y) -> (x) + ((y)), (BiFunction<Integer, Integer, ?>) (x, y) -> (x) + (y), (BiFunction<Integer, Integer, ?>) (x, y) -> (x) + y, (BiFunction<Integer, Integer, ?>) (x, y) -> ((x)) + ((y)), (BiFunction<Integer, Integer, ?>) (x, y) -> ((x)) + (y), (BiFunction<Integer, Integer, ?>) (x, y) -> ((x)) + y);
        group((Function<Integer, Integer>) x -> x + (1 + 2 + 3), (Function<Integer, Integer>) x -> x + 6);
        group((Function<Integer, Integer>) x -> x + 1, (Function<Integer, Integer>) y -> y + 1);
        group((Consumer<Integer>) x -> this.f(), (Consumer<Integer>) x -> this.f());
        group((Consumer<Integer>) y -> this.g());
        group((Consumer<Integer>) x -> f(), (Consumer<Integer>) x -> f());
        group((Consumer<Integer>) y -> g());
        group((Function<Integer, Integer>) x -> this.i, (Function<Integer, Integer>) x -> this.i);
        group((Function<Integer, Integer>) y -> this.j);
        group((Function<Integer, Integer>) x -> i, (Function<Integer, Integer>) x -> i);
        group((Function<Integer, Integer>) y -> j);
        group((Function<Integer, Integer>) y -> {
            while (true) {
                break;
            }
            return 42;
        }, (Function<Integer, Integer>) y -> {
            while (true) {
                break;
            }
            return 42;
        });
        group((Function<Integer, Integer>) x -> {
            int y = x;
            return y;
        }, (Function<Integer, Integer>) x -> {
            int y = x;
            return y;
        });
        group((Function<Integer, Integer>) x -> {
            int y = 0, z = x;
            return y;
        });
        group((Function<Integer, Integer>) x -> {
            int y = 0, z = x;
            return z;
        });
        class Local {

            int i;

            void f() {
            }

            {
                group((Function<Integer, Integer>) x -> this.i);
                group((Consumer<Integer>) x -> this.f());
                group((Function<Integer, Integer>) x -> Deduplication.this.i);
                group((Consumer<Integer>) x -> Deduplication.this.f());
            }
        }
    }

    void f() {
    }

    void g() {
    }

    int i;

    int j;
}
