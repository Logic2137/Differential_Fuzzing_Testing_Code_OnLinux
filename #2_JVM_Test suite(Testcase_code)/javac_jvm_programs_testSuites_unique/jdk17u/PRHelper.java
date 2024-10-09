
package gc.gctests.PhantomReference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

public final class PRHelper extends PhantomReference {

    private int referentHashCode;

    public PRHelper(Object o, ReferenceQueue referenceQueue) {
        super(o, referenceQueue);
        referentHashCode = -1;
    }

    public int getReferentHashCode() {
        return referentHashCode;
    }

    public void setReferentHashCode(int referentHashCode) {
        this.referentHashCode = referentHashCode;
    }
}
