
package org.openjdk.tests.java.util.stream;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CollectorExample {

    class Widget {
    }

    class Employee {

        public int getSalary() {
            return 0;
        }

        public Department getDepartment() {
            return new Department();
        }
    }

    class Department {
    }

    <T, A, R> void testSnippet1(Collector<T, A, R> collector, T t1, T t2) {
        Supplier<A> supplier = collector.supplier();
        BiConsumer<A, T> accumulator = collector.accumulator();
        BinaryOperator<A> combiner = collector.combiner();
        Function<A, R> finisher = collector.finisher();
        A a1 = supplier.get();
        accumulator.accept(a1, t1);
        accumulator.accept(a1, t2);
        R r1 = finisher.apply(a1);
        A a2 = supplier.get();
        accumulator.accept(a2, t1);
        A a3 = supplier.get();
        accumulator.accept(a3, t2);
        R r2 = finisher.apply(combiner.apply(a2, a3));
    }

    void testSnippet2() {
        Collector<Widget, ?, TreeSet<Widget>> intoSet = Collector.of(TreeSet::new, TreeSet::add, (left, right) -> {
            left.addAll(right);
            return left;
        });
    }

    <T, A, R> void testSnippet3(Collector<T, A, R> collector, Collection<T> data) {
        A container = collector.supplier().get();
        for (T t : data) collector.accumulator().accept(container, t);
        collector.finisher().apply(container);
    }

    void testSnippet4and5() {
        Collector<Employee, ?, Integer> summingSalaries = Collectors.summingInt(Employee::getSalary);
        Collector<Employee, ?, Map<Department, Integer>> summingSalariesByDept = Collectors.groupingBy(Employee::getDepartment, summingSalaries);
    }
}
