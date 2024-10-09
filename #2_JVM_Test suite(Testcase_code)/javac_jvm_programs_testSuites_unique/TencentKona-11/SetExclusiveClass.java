import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.sound.sampled.*;
import com.sun.media.sound.*;

public class SetExclusiveClass {

    public static void main(String[] args) throws Exception {
        ModelPerformer performer = new ModelPerformer();
        performer.setExclusiveClass(10);
        if (performer.getExclusiveClass() != 10)
            throw new RuntimeException("performer.getExclusiveClass() didn't return 10!");
    }
}
