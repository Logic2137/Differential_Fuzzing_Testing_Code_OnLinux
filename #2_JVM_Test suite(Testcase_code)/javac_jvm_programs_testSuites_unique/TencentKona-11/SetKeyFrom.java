



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.*;

import com.sun.media.sound.*;

public class SetKeyFrom {

    public static void main(String[] args) throws Exception {
        ModelPerformer performer = new ModelPerformer();
        performer.setKeyFrom(10);
        if(performer.getKeyFrom() != 10)
            throw new RuntimeException("performer.getKeyFrom() didn't return 10!");
    }
}
