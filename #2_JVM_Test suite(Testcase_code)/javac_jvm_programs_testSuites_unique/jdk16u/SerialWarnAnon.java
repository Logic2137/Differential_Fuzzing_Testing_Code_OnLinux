

class SerialWarnAnon {
    interface SerialWarnAnonInterface extends java.io.Serializable { }
    Object m() {
        return new SerialWarnAnonInterface() { };
    }
}
