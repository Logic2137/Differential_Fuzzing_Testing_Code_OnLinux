
package org.netbeans.jemmy;

public interface Waitable<R, P> {

    public R actionProduced(P obj);

    public String getDescription();
}
