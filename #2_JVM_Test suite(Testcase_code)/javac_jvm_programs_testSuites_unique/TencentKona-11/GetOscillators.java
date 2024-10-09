import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.sound.sampled.*;
import com.sun.media.sound.*;

public class GetOscillators {

    public static void main(String[] args) throws Exception {
        ModelPerformer performer = new ModelPerformer();
        if (performer.getOscillators() == null)
            throw new RuntimeException("performer.getOscillators() returned null!");
    }
}
