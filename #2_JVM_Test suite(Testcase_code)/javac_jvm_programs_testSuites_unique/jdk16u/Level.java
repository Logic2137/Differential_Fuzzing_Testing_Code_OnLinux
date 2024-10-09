
package org.openjdk.bench.vm.lambda.chain;


public interface Level {

    public Level up();

    default String getImage() {
        return "";
    }
}
