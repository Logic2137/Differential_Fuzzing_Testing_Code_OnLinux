



import java.awt.geom.GeneralPath;

public class ContainsPoint {
    public static void main(String args[]) {
        GeneralPath gp = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        gp.moveTo(46.69187927246094f, 95.8778305053711f);
        gp.curveTo(-122.75305938720703f, 14.31462574005127f,
                   66.84117889404297f, 26.061769485473633f,
                   -62.62519073486328f, -13.041547775268555f);
        gp.closePath();
        if (gp.contains(-122.75305938720703, -13.041547775268555)) {
            throw new RuntimeException("contains point clearly "+
                                       "outside of curve");
        }
    }
}
