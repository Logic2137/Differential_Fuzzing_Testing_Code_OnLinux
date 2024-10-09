import java.io.Serializable;

public class SqeParameter implements Serializable {

    private static boolean weird;

    private String glop;

    static {
        if (System.getProperty("WEIRD_PARAM") != null) {
            weird = true;
        }
    }

    public SqeParameter() throws Exception {
        if (weird) {
            throw new Exception();
        }
    }

    public String getGlop() {
        return glop;
    }

    public void setGlop(String value) {
        glop = value;
    }
}
