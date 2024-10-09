package jdk.test.lib.artifacts;

public class ArtifactResolverException extends Exception {

    public ArtifactResolverException(String message) {
        super(message);
    }

    public ArtifactResolverException(String message, Throwable cause) {
        super(message, cause);
    }
}
