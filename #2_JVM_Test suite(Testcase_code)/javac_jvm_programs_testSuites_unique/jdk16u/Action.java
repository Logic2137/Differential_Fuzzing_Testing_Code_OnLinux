
package org.netbeans.jemmy;


public interface Action<R, P> {

    
    public R launch(P obj);

    
    public String getDescription();
}
