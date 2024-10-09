



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.*;

import com.sun.media.sound.*;

public class SetKeyTo {

    public static void main(String[] args) throws Exception {
        ModelPerformer performer = new ModelPerformer();
        performer.setKeyTo(10);
        if(performer.getKeyTo() != 10)
            throw new RuntimeException("performer.getKeyTo() didn't return 10!");
    }
}
