



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.*;

import com.sun.media.sound.*;

public class SetConnectionBlocks {

    public static void main(String[] args) throws Exception {
        ModelPerformer performer = new ModelPerformer();
        List<ModelConnectionBlock> newlist = new ArrayList<ModelConnectionBlock>();
        performer.setConnectionBlocks(newlist);
        if(performer.getConnectionBlocks() != newlist)
            throw new RuntimeException("performer.getConnectionBlocks() returned incorrect data!");
    }
}
