

package jdk.nashorn.test.models;

@SuppressWarnings("javadoc")
public abstract class ConstructorWithArgument {
    private final String token;

    protected ConstructorWithArgument(final String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    protected abstract void doSomething();
}
