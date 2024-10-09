



import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;


public class SelectNowWhenEmpty {
    public static void main(String[] args) throws Exception {
        Selector s = SelectorProvider.provider().openSelector();
        s.selectNow();
    }
}
