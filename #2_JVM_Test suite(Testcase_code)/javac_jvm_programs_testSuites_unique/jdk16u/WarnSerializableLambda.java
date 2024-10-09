




import java.io.Serializable;

public class WarnSerializableLambda {
    private void m1() {
        new SerializableClass() {
            @Override
            public void m() {
                packageField = "";
            }
        };
    }

    String packageField;

    class SerializableClass implements Serializable {
        public void m() {}
    }
}
