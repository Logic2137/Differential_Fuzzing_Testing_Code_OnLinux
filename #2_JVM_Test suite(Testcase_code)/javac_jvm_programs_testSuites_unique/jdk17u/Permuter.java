
package com.sun.swingset3.demos.list;

class Permuter {

    private int modulus;

    private int multiplier;

    private static final int ADDEND = 22;

    public Permuter(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        modulus = n;
        if (n == 1) {
            return;
        }
        multiplier = (int) Math.sqrt(n);
        while (gcd(multiplier, n) != 1) {
            if (++multiplier == n) {
                multiplier = 1;
            }
        }
    }

    public int map(int i) {
        return (multiplier * i + ADDEND) % modulus;
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int tmp = a % b;
            a = b;
            b = tmp;
        }
        return a;
    }

    public static void main(String[] args) {
        int modulus = Integer.parseInt(args[0]);
        Permuter p = new Permuter(modulus);
        for (int i = 0; i < modulus; i++) {
            System.out.print(p.map(i) + " ");
        }
        System.out.println();
    }
}
