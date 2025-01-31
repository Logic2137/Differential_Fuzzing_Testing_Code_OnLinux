

import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;


public class Path2DGrow {

    public static final int N = 1000 * 1000;

    public static boolean verbose = false;
    public static boolean force = false;

    static void echo(String msg) {
        System.out.println(msg);
    }

    static void log(String msg) {
        if (verbose || force) {
            echo(msg);
        }
    }

    public static void main(String argv[]) {
        verbose = (argv.length != 0);

        testEmptyDoublePaths();
        testDoublePaths();

        testEmptyFloatPaths();
        testFloatPaths();

        testEmptyGeneralPath();
        testGeneralPath();
    }

    static void testEmptyDoublePaths() {
        echo("\n - Test(Path2D.Double[0]) ---");
        test(() -> new Path2D.Double(Path2D.WIND_NON_ZERO, 0));
    }

    static void testDoublePaths() {
        echo("\n - Test(Path2D.Double) ---");
        test(() -> new Path2D.Double());
    }

    static void testEmptyFloatPaths() {
        echo("\n - Test(Path2D.Float[0]) ---");
        test(() -> new Path2D.Float(Path2D.WIND_NON_ZERO, 0));
    }

    static void testFloatPaths() {
        echo("\n - Test(Path2D.Float) ---");
        test(() -> new Path2D.Float());
    }

    static void testEmptyGeneralPath() {
        echo("\n - Test(GeneralPath[0]) ---");
        test(() -> new GeneralPath(Path2D.WIND_NON_ZERO, 0));
    }

    static void testGeneralPath() {
        echo("\n - Test(GeneralPath) ---");
        test(() -> new GeneralPath());
    }

    interface PathFactory {
        Path2D makePath();
    }

    static void test(PathFactory pf) {
        long start, end;

        for (int n = 1; n <= N; n *= 10) {
            force = (n == N);

            start = System.nanoTime();
            testAddMoves(pf.makePath(), n);
            end = System.nanoTime();
            log("testAddMoves[" + n + "] duration= "
                + (1e-6 * (end - start)) + " ms.");

            start = System.nanoTime();
            testAddLines(pf.makePath(), n);
            end = System.nanoTime();
            log("testAddLines[" + n + "] duration= "
                + (1e-6 * (end - start)) + " ms.");

            start = System.nanoTime();
            testAddQuads(pf.makePath(), n);
            end = System.nanoTime();
            log("testAddQuads[" + n + "] duration= "
                + (1e-6 * (end - start)) + " ms.");

            start = System.nanoTime();
            testAddCubics(pf.makePath(), n);
            end = System.nanoTime();
            log("testAddCubics[" + n + "] duration= "
                + (1e-6 * (end - start)) + " ms.");

            start = System.nanoTime();
            testAddMoveAndCloses(pf.makePath(), n);
            end = System.nanoTime();
            log("testAddMoveAndCloses[" + n + "] duration= "
                + (1e-6 * (end - start)) + " ms.");
        }
    }

    static void addMove(Path2D p2d, int i) {
        p2d.moveTo(1.0 * i, 0.5 * i);
    }

    static void addLine(Path2D p2d, int i) {
        p2d.lineTo(1.1 * i, 2.3 * i);
    }

    static void addCubic(Path2D p2d, int i) {
        p2d.curveTo(1.1 * i, 1.2 * i, 1.3 * i, 1.4 * i, 1.5 * i, 1.6 * i);
    }

    static void addQuad(Path2D p2d, int i) {
        p2d.quadTo(1.1 * i, 1.2 * i, 1.3 * i, 1.4 * i);
    }

    static void addClose(Path2D p2d) {
        p2d.closePath();
    }

    static void testAddMoves(Path2D pathA, int n) {
        for (int i = 0; i < n; i++) {
            addMove(pathA, i);
        }
    }

    static void testAddLines(Path2D pathA, int n) {
        addMove(pathA, 0);
        for (int i = 0; i < n; i++) {
            addLine(pathA, i);
        }
    }

    static void testAddQuads(Path2D pathA, int n) {
        addMove(pathA, 0);
        for (int i = 0; i < n; i++) {
            addQuad(pathA, i);
        }
    }

    static void testAddCubics(Path2D pathA, int n) {
        addMove(pathA, 0);
        for (int i = 0; i < n; i++) {
            addCubic(pathA, i);
        }
    }

    static void testAddMoveAndCloses(Path2D pathA, int n) {
        for (int i = 0; i < n; i++) {
            addMove(pathA, i);
            addClose(pathA);
        }
    }
}
