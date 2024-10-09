



public class CCEForFunctionalInterExtedingRawSuperInterTest {
    interface X<A> { <T extends A> void execute(int a); }
    interface Y<B> { <S extends B> void execute(int a); }

    @FunctionalInterface
    interface Exec<A> extends Y, X<A> { }
}
