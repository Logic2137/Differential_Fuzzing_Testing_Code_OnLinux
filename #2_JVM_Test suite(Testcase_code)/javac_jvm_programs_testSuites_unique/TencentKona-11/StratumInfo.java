

package vm.mlvm.share.jpda;

public class StratumInfo {
    public final String stratum;
    public final String stratumSourceName;
    public final int stratumSourceLine;

    public StratumInfo(String stratum, String stratumSourceName, int stratumSourceLine) {
        this.stratum = stratum;
        this.stratumSourceName = stratumSourceName;
        this.stratumSourceLine = stratumSourceLine;
    }

    @Override
    public String toString() {
        return this.stratum + "=[" + this.stratumSourceName + ":" + this.stratumSourceLine + "]";
    }
}
