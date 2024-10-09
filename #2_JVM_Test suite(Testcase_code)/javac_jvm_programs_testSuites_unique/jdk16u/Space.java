


import java.io.*;

public class Space {
    public static void main(String[] args) throws Exception {
        if (File.separatorChar == '\\') {
            try {
            Process p = Runtime.getRuntime().exec( "cmd /c echo hello" );
            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(p.getInputStream()));
            p.waitFor();
            String echo = reader.readLine();
            if (echo.length() == 6)
                throw new RuntimeException("Extra space in command.");
            } catch (IOException e) {
            
            return;
            }
        }
    }
}
