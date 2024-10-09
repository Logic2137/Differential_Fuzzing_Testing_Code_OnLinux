



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.*;

import com.sun.media.sound.*;

public class SetVariable {

    public static void main(String[] args) throws Exception {
        ModelIdentifier id = new ModelIdentifier("test","a",1);
        id.setVariable("b");
        if(!id.getVariable().equals("b"))
            throw new RuntimeException("id.getVariable() does't return \"b\"!");
    }
}
