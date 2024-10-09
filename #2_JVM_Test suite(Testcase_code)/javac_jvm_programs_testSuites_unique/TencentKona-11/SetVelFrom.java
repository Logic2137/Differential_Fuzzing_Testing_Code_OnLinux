



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.*;

import com.sun.media.sound.*;

public class SetVelFrom {

    public static void main(String[] args) throws Exception {
        ModelPerformer performer = new ModelPerformer();
        performer.setVelFrom(10);
        if(performer.getVelFrom() != 10)
            throw new RuntimeException("performer.getVelFrom() didn't return 10!");
    }
}
