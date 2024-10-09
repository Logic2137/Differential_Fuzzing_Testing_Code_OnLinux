

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class CustomEditor implements AutoCloseable {

    public static final int SOURCE_CODE = 0;
    public static final int GET_SOURCE_CODE = 1;
    public static final int REMOVE_CODE = 2;
    public static final int GET_FILENAME = 3;

    public static final int EXIT_CODE = -1;
    public static final int ACCEPT_CODE = -2;
    public static final int CANCEL_CODE = -3;

    private final Socket socket;
    private final Path path;
    private final StringWriter writer;
    private final String source;

    public CustomEditor(int port, String fileName) throws IOException {
        this.socket = new Socket((String) null, port);
        this.path = Paths.get(fileName);
        this.writer = new StringWriter();
        this.source = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    public void loop() throws IOException {
        DataInputStream input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

        while (true) {
            int cmd = input.readInt();
            switch (cmd) {
                case EXIT_CODE: {
                    Files.write(path, Collections.singletonList(writer.toString()));
                    return;
                }
                case GET_SOURCE_CODE: {
                    writeString(output, source);
                    break;
                }
                case REMOVE_CODE: {
                    
                    Files.delete(path);
                    break;
                }
                case GET_FILENAME: {
                    writeString(output, path.toString());
                    break;
                }
                case CANCEL_CODE: {
                    return;
                }
                case ACCEPT_CODE: {
                    Files.write(path, Collections.singletonList(writer.toString()));
                    break;
                }
                case SOURCE_CODE: {
                    int length = input.readInt();
                    byte[] bytes = new byte[length];
                    input.readFully(bytes);
                    writer.write(new String(bytes, StandardCharsets.UTF_8));
                    break;
                }
            }
        }
    }

    private void writeString(DataOutputStream output, String s) throws IOException {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        output.writeInt(bytes.length);
        output.write(bytes);
        output.flush();
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: port file");
            System.exit(1);
        }
        try (CustomEditor editor = new CustomEditor(Integer.parseInt(args[0]), args[1])) {
            editor.loop();
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
