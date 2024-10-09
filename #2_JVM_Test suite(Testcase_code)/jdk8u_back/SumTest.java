public class SumTest {

    private static class Sum {

        double[] sums;

        public Sum() {
            sums = new double[0];
        }

        final public double getSum() {
            double sum = 0;
            for (final double s : sums) {
                sum += s;
            }
            return sum;
        }

        final public void add(double a) {
            try {
                sums[sums.length] = -1;
            } catch (final IndexOutOfBoundsException e) {
                final double[] oldSums = sums;
                sums = new double[oldSums.length + 1];
                System.arraycopy(oldSums, 0, sums, 0, oldSums.length);
                sums[oldSums.length] = a;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        final Sum sum = new Sum();
        for (int i = 1; i <= 10000; ++i) {
            sum.add(1);
            double ii = sum.getSum();
            if (i != ii) {
                throw new Exception("Failure: computed = " + ii + ", expected = " + i);
            }
        }
    }
}
