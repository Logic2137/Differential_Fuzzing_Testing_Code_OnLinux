
package p;

import java.security.Provider;

public final class TestProvider extends Provider {

    public TestProvider() {
        super("TestProvider", "1.0", "Test Security provider");
        System.out.println(String.format("TEST Security provider loaded" + " successfully : %s", this.toString()));
    }

    @Override
    public String toString() {
        return "TestProvider [getName()=" + getName() + ", getVersion()=" + getVersionStr() + ", getInfo()=" + getInfo() + ", toString()=" + super.toString() + "]";
    }
}
