


public class Livelock {

    public static double elim;

    public static void main(String[] args) {
        System.out.printf(
                "%24s  %24s  %24s  %24s  %24s  %24s%n",
                "n", "n!", "e = lim(...)", "e = taylor series",
                "err e-lim", "err e-taylor");

        while (true) {
            double esum = 2;
            double nfac = 1;
            double iter = 1;
            for (double n = 1; !Double.isInfinite(n) && !Double.isNaN(n) ; n = n * 2) {
                elim = Math.pow(1 + 1 / n, n);

                iter += 1;
                nfac *= iter;
                esum += 1 / nfac;

                System.out.printf("% 24.16e  % 24.16e  % 24.16e  % 24.16e"
                                + "%- 24.16e  %- 24.16e%n",
                        n, nfac, elim, esum, (Math.E - elim), (Math.E - esum));
            }
        }
    }
}
