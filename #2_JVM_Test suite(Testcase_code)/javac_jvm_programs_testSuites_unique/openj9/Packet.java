

package org.openj9.test.util.process;

import java.io.Serializable;

public class Packet implements Serializable {

    private static final long serialVersionUID = -1L;

    public enum Type {
        HANDSHAKE, GOODBYE, EXCEPTION, TASK, ACK
    };

    private Type type;

    private Object object;

    public Packet(Type type) {
        this.type = type;
    }

    public Packet(Type type, Object object) {
        this(type);
        this.object = object;
    }

    public Type getType() {
        return type;
    }

    public Object getObject() {
        return object;
    }
}
