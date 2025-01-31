



import java.io.ByteArrayOutputStream;

import javax.sound.sampled.*;

import com.sun.media.sound.*;

public class NewModelByteBufferWavetableModelByteBufferFloat {

    static float[] testarray;
    static byte[] test_byte_array;
    static byte[] test_byte_array_8ext;
    static AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
    static AudioFormat format24 = new AudioFormat(44100, 24, 1, true, false);
    static ModelByteBuffer buffer;
    static ModelByteBuffer buffer_wave;
    static ModelByteBuffer buffer8;
    static ModelByteBuffer buffer16_8;
    static ModelByteBuffer buffer24;

    static void setUp() throws Exception {
        testarray = new float[1024];
        for (int i = 0; i < 1024; i++) {
            double ii = i / 1024.0;
            ii = ii * ii;
            testarray[i] = (float)Math.sin(10*ii*2*Math.PI);
            testarray[i] += (float)Math.sin(1.731 + 2*ii*2*Math.PI);
            testarray[i] += (float)Math.sin(0.231 + 6.3*ii*2*Math.PI);
            testarray[i] *= 0.3;
        }
        test_byte_array = new byte[testarray.length*2];
        AudioFloatConverter.getConverter(format).toByteArray(testarray, test_byte_array);
        buffer = new ModelByteBuffer(test_byte_array);

        byte[] test_byte_array2 = new byte[testarray.length*3];
        buffer24 = new ModelByteBuffer(test_byte_array2);
        test_byte_array_8ext = new byte[testarray.length];
        byte[] test_byte_array_8_16 = new byte[testarray.length*2];
        AudioFloatConverter.getConverter(format24).toByteArray(testarray, test_byte_array2);
        int ix = 0;
        int x = 0;
        for (int i = 0; i < test_byte_array_8ext.length; i++) {
            test_byte_array_8ext[i] = test_byte_array2[ix++];
            test_byte_array_8_16[x++] = test_byte_array2[ix++];
            test_byte_array_8_16[x++] = test_byte_array2[ix++];
        }
        buffer16_8 = new ModelByteBuffer(test_byte_array_8_16);
        buffer8 = new ModelByteBuffer(test_byte_array_8ext);

        AudioInputStream ais = new AudioInputStream(buffer.getInputStream(), format, testarray.length);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, baos);
        buffer_wave = new ModelByteBuffer(baos.toByteArray());
    }

    public static void main(String[] args) throws Exception {

        setUp();

        ModelByteBufferWavetable wavetable = new ModelByteBufferWavetable(buffer,format,10f);
        if(wavetable.getBuffer() != buffer)
            throw new RuntimeException("wavetable.getBuffer() incorrect!");
        if(!wavetable.getFormat().matches(format))
            throw new RuntimeException("wavetable.getFormat() incorrect!");
        if(wavetable.getPitchcorrection() != 10f)
            throw new RuntimeException("wavetable.getPitchcorrection() not 10!");
    }

}
