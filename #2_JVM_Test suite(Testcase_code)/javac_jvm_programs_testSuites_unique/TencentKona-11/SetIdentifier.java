



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.*;

import com.sun.media.sound.*;

public class SetIdentifier {

    public static void main(String[] args) throws Exception {
        ModelSource src = new ModelSource();
        src.setIdentifier(ModelSource.SOURCE_NOTEON_KEYNUMBER);
        if(src.getIdentifier() != ModelSource.SOURCE_NOTEON_KEYNUMBER)
            throw new RuntimeException("src.getIdentifier() doesn't return ModelSource.SOURCE_NOTEON_KEYNUMBER!");
    }
}
