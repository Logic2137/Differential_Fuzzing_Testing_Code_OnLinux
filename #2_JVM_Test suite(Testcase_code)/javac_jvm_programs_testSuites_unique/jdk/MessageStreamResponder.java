

package jdk.internal.net.http.websocket;

import java.io.*;
import java.nio.*;
import java.util.List;


interface MessageStreamResponder {

    public void sendText(CharBuffer src, boolean last) throws IOException;

    public void sendBinary(ByteBuffer src, boolean last) throws IOException;

    public void sendPing(ByteBuffer src) throws IOException;

    public void sendPong(ByteBuffer src) throws IOException;

    public void sendClose(int statusCode, CharBuffer reason) throws IOException;

    public void close();
}
