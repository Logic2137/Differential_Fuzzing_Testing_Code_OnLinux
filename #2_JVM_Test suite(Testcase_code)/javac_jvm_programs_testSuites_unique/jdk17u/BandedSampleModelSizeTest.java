import java.awt.image.BandedSampleModel;
import java.awt.image.DataBuffer;
import java.util.Arrays;

public class BandedSampleModelSizeTest {

    private static final int TEST_NUM_BANDS = 3;

    private static final int TEST_SRC_IMG_DIM = 10;

    private static BandedSampleModel singleBankModel = null;

    private static BandedSampleModel multiBankModel = null;

    private static void initTest() {
        int[] bandOffsets = new int[TEST_NUM_BANDS];
        int[] bankIndices = new int[TEST_NUM_BANDS];
        bandOffsets[0] = 0;
        bandOffsets[1] = 120;
        bandOffsets[2] = 240;
        bankIndices[0] = 0;
        bankIndices[1] = 0;
        bankIndices[2] = 0;
        singleBankModel = new BandedSampleModel(DataBuffer.TYPE_BYTE, TEST_SRC_IMG_DIM, TEST_SRC_IMG_DIM, TEST_SRC_IMG_DIM, bankIndices, bandOffsets);
        bandOffsets[0] = 0;
        bandOffsets[1] = 20;
        bandOffsets[2] = 40;
        bankIndices[0] = 0;
        bankIndices[1] = 1;
        bankIndices[2] = 2;
        multiBankModel = new BandedSampleModel(DataBuffer.TYPE_BYTE, TEST_SRC_IMG_DIM, TEST_SRC_IMG_DIM, TEST_SRC_IMG_DIM, bankIndices, bandOffsets);
    }

    private static void testSingleBankModel() {
        int[] srcSamples = new int[TEST_NUM_BANDS];
        int[] resSamples = new int[TEST_NUM_BANDS];
        DataBuffer imgBuffer = singleBankModel.createDataBuffer();
        Arrays.fill(srcSamples, 125);
        singleBankModel.setPixel(0, 0, srcSamples, imgBuffer);
        singleBankModel.getPixel(0, 0, resSamples, imgBuffer);
        if (!Arrays.equals(srcSamples, resSamples)) {
            throw new RuntimeException("Test Failed. Incorrect samples found" + " in the image");
        }
        Arrays.fill(srcSamples, 250);
        singleBankModel.setPixel(9, 9, srcSamples, imgBuffer);
        singleBankModel.getPixel(9, 9, resSamples, imgBuffer);
        if (!Arrays.equals(srcSamples, resSamples)) {
            throw new RuntimeException("Test Failed. Incorrect samples found" + " in the image");
        }
    }

    private static void testMultiBankModel() {
        int[] srcSamples = new int[TEST_NUM_BANDS];
        int[] resSamples = new int[TEST_NUM_BANDS];
        DataBuffer imgBuffer = multiBankModel.createDataBuffer();
        Arrays.fill(srcSamples, 125);
        multiBankModel.setPixel(0, 0, srcSamples, imgBuffer);
        multiBankModel.getPixel(0, 0, resSamples, imgBuffer);
        if (!Arrays.equals(srcSamples, resSamples)) {
            throw new RuntimeException("Test Failed. Incorrect samples found" + " in the image");
        }
        Arrays.fill(srcSamples, 250);
        multiBankModel.setPixel(9, 9, srcSamples, imgBuffer);
        multiBankModel.getPixel(9, 9, resSamples, imgBuffer);
        if (!Arrays.equals(srcSamples, resSamples)) {
            throw new RuntimeException("Test Failed. Incorrect samples found" + " in the image");
        }
    }

    public static void main(String[] args) {
        initTest();
        testSingleBankModel();
        testMultiBankModel();
    }
}
