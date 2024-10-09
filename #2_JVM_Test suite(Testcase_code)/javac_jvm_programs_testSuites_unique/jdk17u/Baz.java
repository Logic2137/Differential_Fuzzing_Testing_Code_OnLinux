
package jdk.test.baz;

import java.lang.module.ModuleDescriptor;

public class Baz {

    public static void main(String[] args) {
        ModuleDescriptor md = Baz.class.getModule().getDescriptor();
        System.out.println("nameAndVersion:" + md.toNameAndVersion());
        md.mainClass().ifPresent(mc -> System.out.println("mainClass:" + mc));
    }
}
