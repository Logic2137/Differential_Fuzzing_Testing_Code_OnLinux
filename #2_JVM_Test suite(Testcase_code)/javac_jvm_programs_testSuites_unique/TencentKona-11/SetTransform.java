



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.*;

import com.sun.media.sound.*;

public class SetTransform {


    private static boolean checkLinearity(ModelStandardTransform transform)
    {
        double lastx = 0;
        for (int p = 0; p < 2; p++)
        for (int d = 0; d < 2; d++)
        for (double i = 0; i < 1.0; i+=0.001) {
            if(p == 0)
                transform.setPolarity(ModelStandardTransform.POLARITY_UNIPOLAR);
            else
                transform.setPolarity(ModelStandardTransform.POLARITY_BIPOLAR);
            if(d == 0)
                transform.setDirection(ModelStandardTransform.DIRECTION_MIN2MAX);
            else
                transform.setDirection(ModelStandardTransform.DIRECTION_MAX2MIN);
            double x = transform.transform(i);
            if(i == 0)
                lastx = x;
            else
            {
                if(lastx - x > 0.2) return false;
                lastx = x;
            }
        }
        return true;
    }


    public static void main(String[] args) throws Exception {
        ModelStandardTransform transform = new ModelStandardTransform();
        transform.setTransform(ModelStandardTransform.TRANSFORM_CONVEX);
        if(transform.getTransform() != ModelStandardTransform.TRANSFORM_CONVEX)
            throw new RuntimeException("transform.getTransform() doesn't return ModelStandardTransform.TRANSFORM_CONVEX!");

    }
}
