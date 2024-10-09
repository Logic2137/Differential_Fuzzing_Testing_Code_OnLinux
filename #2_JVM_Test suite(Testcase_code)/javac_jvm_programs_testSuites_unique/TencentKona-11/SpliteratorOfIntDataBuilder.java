

package java.util;

import java.util.function.Supplier;

public class SpliteratorOfIntDataBuilder {
        List<Object[]> data;

        List<Integer> exp;

        public SpliteratorOfIntDataBuilder(List<Object[]> data, List<Integer> exp) {
            this.data = data;
            this.exp = exp;
        }

        public void add(String description, List<Integer> expected, Supplier<Spliterator.OfInt> s) {
            description = joiner(description).toString();
            data.add(new Object[]{description, expected, s});
        }

        public void add(String description, Supplier<Spliterator.OfInt> s) {
            add(description, exp, s);
        }

        StringBuilder joiner(String description) {
            return new StringBuilder(description).
                    append(" {").
                    append("size=").append(exp.size()).
                    append("}");
        }
    }

