
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;


enum ReadModel {
    READ_BYTE {
        @Override
        public void read(CipherInputStream cInput, CipherOutputStream ciOutput,
                Cipher ciIn, int inputLen) throws IOException {
            int buffer0 = cInput.read();
            while (buffer0 != -1) {
                ciOutput.write(buffer0);
                buffer0 = cInput.read();
            }
        }
    },
    READ_BUFFER {
        @Override
        public void read(CipherInputStream cInput, CipherOutputStream ciOutput,
                Cipher ciIn, int inputLen) throws IOException {
            byte[] buffer1 = new byte[20];
            int len1;
            while ((len1 = cInput.read(buffer1)) != -1) {
                ciOutput.write(buffer1, 0, len1);
            }

        }
    },
    READ_BUFFER_OFFSET {
        @Override
        public void read(CipherInputStream cInput, CipherOutputStream ciOutput,
                Cipher ciIn, int inputLen) throws IOException {
            byte[] buffer2 = new byte[ciIn.getOutputSize(inputLen)];
            int offset2 = 0;
            int len2 = 0;
            while (len2 != -1) {
                len2 = cInput.read(buffer2, offset2, buffer2.length - offset2);
                offset2 += len2;
            }
            ciOutput.write(buffer2);

        }
    };

    abstract public void read(CipherInputStream cInput,
            CipherOutputStream ciOutput, Cipher ciIn, int inputLen)
            throws IOException;
}
