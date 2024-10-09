



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.*;

import com.sun.media.sound.*;

public class SetSelfNonExclusive {

    public static void main(String[] args) throws Exception {
        ModelPerformer performer = new ModelPerformer();
        performer.setSelfNonExclusive(true);
        if(performer.isSelfNonExclusive() != true)
            throw new RuntimeException("performer.isSelfNonExclusive() didn't return true!");
        performer.setSelfNonExclusive(false);
        if(performer.isSelfNonExclusive() != false)
            throw new RuntimeException("performer.isSelfNonExclusive() didn't return false!");
    }
}
