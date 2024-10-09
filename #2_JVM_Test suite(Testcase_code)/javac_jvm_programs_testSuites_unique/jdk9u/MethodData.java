

package selectionresolution;


public class MethodData {

    public enum Access {
        PUBLIC(1),
        PACKAGE(0),
        PROTECTED(4),
        PRIVATE(2),
        
        PLACEHOLDER(-1);

        public final int flag;

        Access(int flag) {
            this.flag = flag;
        }
    }

    public enum Context {
        ABSTRACT,
        INSTANCE,
        STATIC,
        
        PLACEHOLDER;
    };

    
    public final Access access;

    
    public final Context context;

    
    public MethodData(final Access access,
                      final Context context) {

        this.access = access;
        this.context = context;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        switch (access) {
        case PUBLIC: sb.append("public"); break;
        case PACKAGE: sb.append("package"); break;
        case PROTECTED: sb.append("protected"); break;
        case PRIVATE: sb.append("private"); break;
        case PLACEHOLDER: sb.append(" _"); break;
        default: throw new RuntimeException("Impossible case");
        }

        switch (context) {
        case STATIC: sb.append(" static"); break;
        case INSTANCE: sb.append(" instance"); break;
        case ABSTRACT: sb.append("  abstract"); break;
        case PLACEHOLDER: sb.append(" _"); break;
        default: throw new RuntimeException("Impossible case");
        }
        sb.append(" Integer m();");

        return sb.toString();
    }

}
