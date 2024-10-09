
package org.openjdk.bench.jdk.incubator.foreign.points.support;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BBPoint {

    private final ByteBuffer buff;

    public BBPoint(int x, int y) {
        this.buff = ByteBuffer.allocateDirect(4 * 2).order(ByteOrder.nativeOrder());
        setX(x);
        setY(y);
    }

    public void setX(int x) {
        buff.putInt(0, x);
    }

    public int getX() {
        return buff.getInt(0);
    }

    public int getY() {
        return buff.getInt(1);
    }

    public void setY(int y) {
        buff.putInt(0, y);
    }
}
