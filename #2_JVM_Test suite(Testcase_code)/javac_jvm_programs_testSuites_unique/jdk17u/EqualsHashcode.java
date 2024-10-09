import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Vector;

public class EqualsHashcode {

    public static final int NUMTESTS = 1000;

    public static void main(String[] argv) {
        new FloatRectangleTester().test();
        new DoubleRectangleTester().test();
        new FloatEllipseTester().test();
        new DoubleEllipseTester().test();
        new FloatArcTester().test();
        new DoubleArcTester().test();
        new FloatRoundRectTester().test();
        new DoubleRoundRectTester().test();
    }

    public static abstract class Val {

        protected String name;

        protected Val(String name) {
            this.name = name;
        }

        public abstract Object save();

        public abstract void restore(Object save);

        public abstract void randomize();

        public abstract void perturb();
    }

    public static abstract class SpecialVal extends Val {

        protected SpecialVal(String name) {
            super(name);
        }

        public abstract void setSpecial();

        public abstract boolean isNaN();
    }

    public static class FloatVal extends SpecialVal {

        private float v;

        public FloatVal(String name) {
            super(name);
        }

        public float getVal() {
            return v;
        }

        public String toString() {
            return name + " = " + v + " (flt)";
        }

        public Object save() {
            return new Float(v);
        }

        public void restore(Object o) {
            v = ((Float) o).floatValue();
        }

        public void randomize() {
            v = (float) (Math.random() * 100);
        }

        public void perturb() {
            v = v + 1;
        }

        public boolean hasSpecialCases() {
            return true;
        }

        public void setSpecial() {
            switch((int) (Math.random() * 3)) {
                case 0:
                    v = Float.NaN;
                    break;
                case 1:
                    v = Float.POSITIVE_INFINITY;
                    break;
                case 2:
                    v = Float.NEGATIVE_INFINITY;
                    break;
                default:
                    throw new InternalError();
            }
        }

        public boolean isNaN() {
            return (v != v);
        }
    }

    public static class DoubleVal extends SpecialVal {

        private double v;

        public DoubleVal(String name) {
            super(name);
        }

        public double getVal() {
            return v;
        }

        public String toString() {
            return name + " = " + v + " (dbl)";
        }

        public Object save() {
            return new Double(v);
        }

        public void restore(Object o) {
            v = ((Double) o).doubleValue();
        }

        public void randomize() {
            v = Math.random() * 100;
        }

        public void perturb() {
            v = v + 1;
        }

        public boolean hasSpecialCases() {
            return true;
        }

        public void setSpecial() {
            switch((int) (Math.random() * 3)) {
                case 0:
                    v = Double.NaN;
                    break;
                case 1:
                    v = Double.POSITIVE_INFINITY;
                    break;
                case 2:
                    v = Double.NEGATIVE_INFINITY;
                    break;
                default:
                    throw new InternalError();
            }
        }

        public boolean isNaN() {
            return (v != v);
        }
    }

    public static class IntRangeVal extends Val {

        public int v;

        public int min;

        public int max;

        public IntRangeVal(String name, int min, int max) {
            super(name);
            this.min = min;
            this.max = max;
        }

        public int getVal() {
            return v;
        }

        public String toString() {
            return name + " = " + v;
        }

        public Object save() {
            return new Integer(v);
        }

        public void restore(Object o) {
            v = ((Integer) o).intValue();
        }

        public void randomize() {
            v = min + (int) (Math.random() * (max - min + 1));
        }

        public void perturb() {
            v = v + 1;
            if (v > max) {
                v = min;
            }
        }
    }

    public static abstract class ShapeTester {

        public Vector params = new Vector();

        public void addParam(Val v) {
            params.add(v);
        }

        public Val[] getParamArray() {
            Val[] ret = new Val[params.size()];
            for (int i = 0; i < params.size(); i++) {
                ret[i] = (Val) params.get(i);
            }
            return ret;
        }

        public void error(String desc) {
            Val[] params = getParamArray();
            for (int j = 0; j < params.length; j++) {
                System.err.println(params[j]);
            }
            throw new RuntimeException(desc);
        }

        public abstract Object makeShape();

        public void test() {
            Val[] params = getParamArray();
            for (int i = 0; i < NUMTESTS; i++) {
                for (int j = 0; j < params.length; j++) {
                    params[j].randomize();
                }
                Object o1 = makeShape();
                if (!o1.equals(o1)) {
                    error("Shapes not equal to itself!");
                }
                Object o2 = makeShape();
                if (!o1.equals(o2)) {
                    error("Identical shapes not equal!");
                }
                if (o1.hashCode() != o2.hashCode()) {
                    error("Identical hashes not equal!");
                }
                for (int j = 0; j < params.length; j++) {
                    Val param = params[j];
                    Object save = param.save();
                    param.perturb();
                    Object o3 = makeShape();
                    if (o1.equals(o3)) {
                        error("Perturbed shape still equal!");
                    }
                    if (param instanceof SpecialVal) {
                        SpecialVal sparam = (SpecialVal) param;
                        sparam.setSpecial();
                        Object o4 = makeShape();
                        if (o1.equals(o4)) {
                            error("Specialized shape still equal!");
                        }
                        Object o5 = makeShape();
                        if (o4.equals(o5) == sparam.isNaN()) {
                            error("Identical specialized shapes not equal!");
                        }
                        if (o4.hashCode() != o5.hashCode()) {
                            error("Identical specialized hashes not equal!");
                        }
                    }
                    param.restore(save);
                    Object o6 = makeShape();
                    if (!o1.equals(o6)) {
                        error("Restored shape not equal!");
                    }
                    if (o1.hashCode() != o6.hashCode()) {
                        error("Restored hash not equal!");
                    }
                }
            }
        }
    }

    public static abstract class FloatBoundedShape extends ShapeTester {

        public FloatVal x = new FloatVal("x");

        public FloatVal y = new FloatVal("y");

        public FloatVal w = new FloatVal("w");

        public FloatVal h = new FloatVal("h");

        public FloatBoundedShape() {
            addParam(x);
            addParam(y);
            addParam(w);
            addParam(h);
        }
    }

    public static abstract class DoubleBoundedShape extends ShapeTester {

        public DoubleVal x = new DoubleVal("x");

        public DoubleVal y = new DoubleVal("y");

        public DoubleVal w = new DoubleVal("w");

        public DoubleVal h = new DoubleVal("h");

        public DoubleBoundedShape() {
            addParam(x);
            addParam(y);
            addParam(w);
            addParam(h);
        }
    }

    public static class FloatRectangleTester extends FloatBoundedShape {

        public Object makeShape() {
            return new Rectangle2D.Float(x.getVal(), y.getVal(), w.getVal(), h.getVal());
        }
    }

    public static class DoubleRectangleTester extends DoubleBoundedShape {

        public Object makeShape() {
            return new Rectangle2D.Double(x.getVal(), y.getVal(), w.getVal(), h.getVal());
        }
    }

    public static class FloatEllipseTester extends FloatBoundedShape {

        public Object makeShape() {
            return new Ellipse2D.Float(x.getVal(), y.getVal(), w.getVal(), h.getVal());
        }
    }

    public static class DoubleEllipseTester extends DoubleBoundedShape {

        public Object makeShape() {
            return new Ellipse2D.Double(x.getVal(), y.getVal(), w.getVal(), h.getVal());
        }
    }

    public static class FloatArcTester extends FloatBoundedShape {

        public FloatVal start = new FloatVal("start");

        public FloatVal extent = new FloatVal("extent");

        public IntRangeVal type = new IntRangeVal("type", 0, 2);

        public FloatArcTester() {
            addParam(start);
            addParam(extent);
            addParam(type);
        }

        public Object makeShape() {
            return new Arc2D.Float(x.getVal(), y.getVal(), w.getVal(), h.getVal(), start.getVal(), extent.getVal(), type.getVal());
        }
    }

    public static class DoubleArcTester extends DoubleBoundedShape {

        public DoubleVal start = new DoubleVal("start");

        public DoubleVal extent = new DoubleVal("extent");

        public IntRangeVal type = new IntRangeVal("type", 0, 2);

        public DoubleArcTester() {
            addParam(start);
            addParam(extent);
            addParam(type);
        }

        public Object makeShape() {
            return new Arc2D.Double(x.getVal(), y.getVal(), w.getVal(), h.getVal(), start.getVal(), extent.getVal(), type.getVal());
        }
    }

    public static class FloatRoundRectTester extends FloatBoundedShape {

        public FloatVal arcw = new FloatVal("arcw");

        public FloatVal arch = new FloatVal("arch");

        public FloatRoundRectTester() {
            addParam(arcw);
            addParam(arch);
        }

        public Object makeShape() {
            return new RoundRectangle2D.Float(x.getVal(), y.getVal(), w.getVal(), h.getVal(), arcw.getVal(), arch.getVal());
        }
    }

    public static class DoubleRoundRectTester extends DoubleBoundedShape {

        public DoubleVal arcw = new DoubleVal("arcw");

        public DoubleVal arch = new DoubleVal("arch");

        public DoubleRoundRectTester() {
            addParam(arcw);
            addParam(arch);
        }

        public Object makeShape() {
            return new RoundRectangle2D.Double(x.getVal(), y.getVal(), w.getVal(), h.getVal(), arcw.getVal(), arch.getVal());
        }
    }
}
