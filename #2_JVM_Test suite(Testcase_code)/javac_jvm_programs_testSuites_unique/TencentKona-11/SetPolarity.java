



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.*;

import com.sun.media.sound.*;

public class SetPolarity {

    public static void main(String[] args) throws Exception {
        ModelStandardTransform transform = new ModelStandardTransform();
        transform.setPolarity(ModelStandardTransform.POLARITY_BIPOLAR);
        if(transform.getPolarity() != ModelStandardTransform.POLARITY_BIPOLAR)
            throw new RuntimeException("transform.getPolarity() doesn't return ModelStandardTransform.POLARITY_BIPOLAR!");
    }
}
