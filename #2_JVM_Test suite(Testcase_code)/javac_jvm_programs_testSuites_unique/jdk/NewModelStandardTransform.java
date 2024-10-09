



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.*;

import com.sun.media.sound.*;

public class NewModelStandardTransform {

    public static void main(String[] args) throws Exception {
        ModelStandardTransform transform = new ModelStandardTransform();
        if(transform.getDirection() != ModelStandardTransform.DIRECTION_MIN2MAX)
            throw new RuntimeException("transform.getDirection() doesn't return ModelStandardTransform.DIRECTION_MIN2MAX!");
        if(transform.getPolarity() != ModelStandardTransform.POLARITY_UNIPOLAR)
            throw new RuntimeException("transform.getPolarity() doesn't return ModelStandardTransform.POLARITY_UNIPOLAR!");
        if(transform.getTransform() != ModelStandardTransform.TRANSFORM_LINEAR)
            throw new RuntimeException("transform.getTransform() doesn't return ModelStandardTransform.TRANSFORM_LINEAR!");
    }
}
