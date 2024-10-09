

package gc.g1.humongousObjects.objectGraphTest;

import java.lang.ref.Reference;


public class ReferenceInfo<T> {
    public final Reference<T> reference;
    public final String graphId;
    public final String nodeId;
    public final boolean softlyReachable;
    public final boolean effectiveHumongous;

    public ReferenceInfo(Reference<T> reference, String graphId, String nodeId, boolean softlyReachable,
                         boolean effectiveHumongous) {
        this.reference = reference;
        this.graphId = graphId;
        this.nodeId = nodeId;
        this.softlyReachable = softlyReachable;
        this.effectiveHumongous = effectiveHumongous;
    }

    @Override
    public String toString() {
        return String.format("Node %s is effectively %shumongous and effectively %ssoft referenced\n"
                        + "\tReference type is %s and it points to %s", nodeId,
                (effectiveHumongous ? "" : "non-"), (softlyReachable ? "" : "non-"),
                reference.getClass().getSimpleName(), reference.get());
    }
}
