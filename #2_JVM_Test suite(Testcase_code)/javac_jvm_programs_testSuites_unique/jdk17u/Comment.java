import java.io.*;

public class Comment {

    public static void main(String[] args) throws Exception {
        File f = new File(System.getProperty("test.src", "."), "input.txt");
        int slashIsCommentStart = 1;
        int slashSlashComment = 2;
        int slashStarComment = 4;
        for (int i = 0; i < 8; i++) {
            FileReader reader = new FileReader(f);
            try {
                StreamTokenizer st = new StreamTokenizer(reader);
                boolean slashCommentFlag = ((i & slashIsCommentStart) != 0);
                boolean slashSlashCommentFlag = ((i & slashSlashComment) != 0);
                boolean slashStarCommentFlag = ((i & slashStarComment) != 0);
                if (!slashCommentFlag) {
                    st.ordinaryChar('/');
                }
                st.slashSlashComments(slashSlashCommentFlag);
                st.slashStarComments(slashStarCommentFlag);
                while (st.nextToken() != StreamTokenizer.TT_EOF) {
                    String token = st.sval;
                    if (token == null) {
                        continue;
                    } else {
                        if ((token.compareTo("Error1") == 0) && slashStarCommentFlag) {
                            throw new Exception("Failed to pass one line C comments!");
                        }
                        if ((token.compareTo("Error2") == 0) && slashStarCommentFlag) {
                            throw new Exception("Failed to pass multi line C comments!");
                        }
                        if ((token.compareTo("Error3") == 0) && slashSlashCommentFlag) {
                            throw new Exception("Failed to pass C++ comments!");
                        }
                        if ((token.compareTo("Error4") == 0) && slashCommentFlag) {
                            throw new Exception("Failed to pass / comments!");
                        }
                    }
                }
            } finally {
                reader.close();
            }
        }
    }
}
