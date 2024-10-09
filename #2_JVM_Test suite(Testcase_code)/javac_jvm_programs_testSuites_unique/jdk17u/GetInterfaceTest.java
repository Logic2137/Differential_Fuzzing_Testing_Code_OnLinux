import javax.script.*;

public class GetInterfaceTest {

    public static void main(String[] args) throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        if (engine == null) {
            System.out.println("Warning: No js engine engine found; test vacuously passes.");
            return;
        }
        engine.eval("");
        Runnable runnable = ((Invocable) engine).getInterface(Runnable.class);
        if (runnable != null) {
            throw new RuntimeException("runnable is not null!");
        }
        engine.eval("function run() { print('this is run function'); }");
        runnable = ((Invocable) engine).getInterface(Runnable.class);
        runnable.run();
        engine.eval("function bar() { print('bar function'); }");
        Foo2 foo2 = ((Invocable) engine).getInterface(Foo2.class);
        if (foo2 != null) {
            throw new RuntimeException("foo2 is not null!");
        }
        engine.eval("function bar2() { print('bar2 function'); }");
        foo2 = ((Invocable) engine).getInterface(Foo2.class);
        foo2.bar();
        foo2.bar2();
    }

    public interface Foo {

        public void bar();
    }

    public interface Foo2 extends Foo {

        public void bar2();
    }
}
