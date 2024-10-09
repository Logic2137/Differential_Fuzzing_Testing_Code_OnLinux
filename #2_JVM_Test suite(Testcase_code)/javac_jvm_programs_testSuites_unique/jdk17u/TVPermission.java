
package TVJar;

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringJoiner;
import java.util.StringTokenizer;

public class TVPermission extends Permission {

    private final static int WATCH = 0x1;

    private final static int PREVIEW = 0x2;

    private final static int NONE = 0x0;

    private final static int ALL = WATCH | PREVIEW;

    private int mask;

    private String actions;

    private String cname;

    private boolean wildcard;

    private int[] numrange;

    private final static int NUM_MIN = 1;

    private final static int NUM_MAX = 128;

    public TVPermission(String channel, String action) {
        this(channel, getMask(action));
    }

    TVPermission(String channel, int mask) {
        super(channel);
        init(channel, mask);
    }

    private synchronized int[] parseNum(String num) throws Exception {
        if (num == null || num.equals("") || num.equals("*")) {
            wildcard = true;
            return new int[] { NUM_MIN, NUM_MAX };
        }
        int dash = num.indexOf('-');
        if (dash == -1) {
            int p = 0;
            try {
                p = Integer.parseInt(num);
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("invalid input" + num);
            }
            return new int[] { p, p };
        } else {
            String low = num.substring(0, dash);
            String high = num.substring(dash + 1);
            int l, h;
            if (low.equals("")) {
                l = NUM_MIN;
            } else {
                try {
                    l = Integer.parseInt(low);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("invalid input" + num);
                }
            }
            if (high.equals("")) {
                h = NUM_MAX;
            } else {
                try {
                    h = Integer.parseInt(high);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("invalid input" + num);
                }
            }
            if (h < l || l < NUM_MIN || h > NUM_MAX) {
                throw new IllegalArgumentException("invalid num range");
            }
            return new int[] { l, h };
        }
    }

    private synchronized void init(String channel, int mask) {
        int sep = channel.indexOf(':');
        if (sep != -1) {
            String num = channel.substring(sep + 1);
            cname = channel.substring(0, sep);
            try {
                numrange = parseNum(num);
            } catch (Exception e) {
                throw new IllegalArgumentException("invalid num range: " + num);
            }
        } else {
            numrange = new int[] { NUM_MIN, NUM_MAX };
        }
    }

    private synchronized static int getMask(String action) {
        int mask = NONE;
        if (action == null) {
            return mask;
        }
        StringTokenizer st = new StringTokenizer(action.toLowerCase(), ",");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.equals("watch")) {
                mask |= WATCH;
            } else if (token.equals("preview")) {
                mask |= PREVIEW;
            } else {
                throw new IllegalArgumentException("invalid TV permission: " + token);
            }
        }
        return mask;
    }

    @Override
    public boolean implies(Permission p) {
        if (!(p instanceof TVPermission)) {
            return false;
        }
        if (this.wildcard) {
            return true;
        }
        TVPermission that = (TVPermission) p;
        if ((this.mask & that.mask) != that.mask) {
            System.out.println("Masks are not ok this = " + this.mask + "THat = " + that.mask);
            return false;
        }
        if ((this.numrange[0] > that.numrange[0]) || (this.numrange[1] < that.numrange[1])) {
            System.out.println("This 0= " + this.numrange[0] + " 1 = " + this.numrange[1]);
            System.out.println("That 0= " + that.numrange[0] + " 1 = " + that.numrange[1]);
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TVPermission)) {
            return false;
        }
        TVPermission that = (TVPermission) obj;
        if (this.mask != that.mask) {
            return false;
        }
        if ((this.numrange[0] != that.numrange[0]) || (this.numrange[1] != that.numrange[1])) {
            return false;
        }
        return this.getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    private synchronized static String getActions(int mask) {
        StringJoiner sj = new StringJoiner(",");
        if ((mask & WATCH) == WATCH) {
            sj.add("watch");
        }
        if ((mask & PREVIEW) == PREVIEW) {
            sj.add("preview");
        }
        return sj.toString();
    }

    @Override
    public String getActions() {
        if (actions == null) {
            actions = getActions(this.mask);
        }
        return actions;
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + "cname = " + cname + "\n" + "wildcard = " + wildcard + "\n" + "numrange = " + numrange[0] + "," + numrange[1] + "\n";
    }

    @Override
    public PermissionCollection newPermissionCollection() {
        return new TVPermissionCollection();
    }
}

final class TVPermissionCollection extends PermissionCollection {

    private final ArrayList<TVPermission> permissions = new ArrayList<>();

    @Override
    public void add(Permission permission) {
        if (!(permission instanceof TVPermission)) {
            throw new IllegalArgumentException("invalid permission: " + permission);
        }
        permissions.add((TVPermission) permission);
    }

    @Override
    public boolean implies(Permission p) {
        if (!(p instanceof TVPermission)) {
            return false;
        }
        Iterator<TVPermission> i = permissions.iterator();
        while (i.hasNext()) {
            if (((TVPermission) i.next()).implies(p)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Enumeration elements() {
        return Collections.enumeration(permissions);
    }
}
