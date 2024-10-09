package jdk.test.lib.artifacts;


public class ArtifactResolverException extends Exception {

    public ArtifactResolverException(String message) {
        super(message);
    }

    public ArtifactResolverException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        Throwable root = getRootCause();
        if (root != null) {
            return super.toString() + ": " + root.toString();
        } else {
            return super.toString();
        }
    }

    public Throwable getRootCause() {
        Throwable root = getCause();
        if (root == null) {
            return null;
        }
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        return root;
    }
}
