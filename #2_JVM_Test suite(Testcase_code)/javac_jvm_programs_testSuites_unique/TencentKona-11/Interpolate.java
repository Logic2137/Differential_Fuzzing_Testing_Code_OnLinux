import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.sound.sampled.*;
import com.sun.media.sound.*;

public class Interpolate {

    private static float getResamplerTestValue(double i) {
        return (float) Math.sin(i / 10.0);
    }

    private static void perfectInterpolation(float[] in_offset, float in_end, float[] startpitch, float pitchstep, float[] out, int[] out_offset, int out_end) {
        float pitch = startpitch[0];
        float ix = in_offset[0];
        int ox = out_offset[0];
        float ix_end = in_end;
        int ox_end = out_end;
        if (pitchstep == 0f) {
            while (ix < ix_end && ox < ox_end) {
                out[ox++] = getResamplerTestValue(ix);
                ix += pitch;
            }
        } else {
            while (ix < ix_end && ox < ox_end) {
                out[ox++] = getResamplerTestValue(ix);
                ix += pitch;
                pitch += pitchstep;
            }
        }
        in_offset[0] = ix;
        out_offset[0] = ox;
        startpitch[0] = pitch;
    }

    private static float testResampler(SoftAbstractResampler resampler, float p_pitch, float p_pitch2) {
        float[] testbuffer = new float[4096];
        float[] testbuffer2 = new float[1024];
        float[] testbuffer3 = new float[1024];
        for (int i = 0; i < testbuffer.length; i++) testbuffer[i] = getResamplerTestValue(i);
        int pads = resampler.getPadding();
        float pitchstep = (p_pitch2 - p_pitch) / 1024f;
        int[] out_offset2 = { 0 };
        int[] out_offset3 = { 0 };
        resampler.interpolate(testbuffer, new float[] { pads }, testbuffer.length - pads, new float[] { p_pitch }, pitchstep, testbuffer2, out_offset2, testbuffer2.length);
        perfectInterpolation(new float[] { pads }, testbuffer.length - pads, new float[] { p_pitch }, pitchstep, testbuffer3, out_offset3, testbuffer3.length);
        int out_off = out_offset2[0];
        if (out_offset3[0] < out_off)
            out_off = out_offset3[0];
        float ac_error = 0;
        int counter = 0;
        for (int i = pads; i < out_off; i++) {
            ac_error += Math.abs(testbuffer2[i] - testbuffer3[i]);
            counter++;
        }
        return ac_error / ((float) counter);
    }

    private static void fail(String error) throws Exception {
        throw new RuntimeException(error);
    }

    public static void main(String[] args) throws Exception {
        SoftLinearResampler2 resampler = new SoftLinearResampler2();
        float max = testResampler(resampler, 0.3f, 0.3f);
        if (max > 0.2)
            fail("Interpolation failed, error=" + max);
        max = testResampler(resampler, 0.3f, 0.01f);
        if (max > 0.2)
            fail("Interpolation failed, error=" + max);
        max = testResampler(resampler, 1.0f, 0.00f);
        if (max > 0.2)
            fail("Interpolation failed, error=" + max);
    }
}
