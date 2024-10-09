

import java.lang.instrument.*;

class ZeroArgPremainAgent {

    
    public static void premain () {
        System.out.println("Hello from ZeroArgInheritAgent!");
    }
}
