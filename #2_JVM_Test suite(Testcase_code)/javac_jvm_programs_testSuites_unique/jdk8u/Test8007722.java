import java.util.concurrent.atomic.*;

public class Test8007722 {

    int i;

    static AtomicReference<Test8007722> ref;

    static int test(Test8007722 new_obj) {
        Test8007722 o = ref.getAndSet(new_obj);
        int ret = o.i;
        o.i = 5;
        return ret;
    }

    static public void main(String[] args) {
        Test8007722 obj = new Test8007722();
        ref = new AtomicReference<Test8007722>(obj);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 20000; i++) {
            test(obj);
        }
        System.out.println("PASSED");
    }
}
