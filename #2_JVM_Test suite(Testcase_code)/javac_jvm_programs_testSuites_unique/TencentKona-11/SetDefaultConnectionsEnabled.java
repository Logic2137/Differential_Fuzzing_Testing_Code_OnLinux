



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.*;

import com.sun.media.sound.*;

public class SetDefaultConnectionsEnabled {

    public static void main(String[] args) throws Exception {
        ModelPerformer performer = new ModelPerformer();
        performer.setDefaultConnectionsEnabled(true);
        if(performer.isDefaultConnectionsEnabled() != true)
            throw new RuntimeException("performer.isAddDefaultConnectionsEnabled() didn't return true!");
        performer.setDefaultConnectionsEnabled(false);
        if(performer.isDefaultConnectionsEnabled() != false)
            throw new RuntimeException("performer.isAddDefaultConnectionsEnabled() didn't return false!");

    }
}
