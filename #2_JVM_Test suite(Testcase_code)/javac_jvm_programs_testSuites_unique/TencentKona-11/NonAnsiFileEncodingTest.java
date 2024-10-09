



public class NonAnsiFileEncodingTest {
    public static void main(String[] s)  {
        String OS = System.getProperty("os.name");
        String lang = System.getProperty("user.language");
        String fileenc = System.getProperty("file.encoding");

        if (!(OS.equals("Windows 2000") || OS.equals("Windows XP"))) {
            System.out.println("This test is not meaningful on the platform \"" + OS + "\".");
            return;
        }

        if (!(lang.equals("hy") ||      
              lang.equals("ka") ||      
              lang.equals("hi") ||      
              lang.equals("pa") ||      
              lang.equals("gu") ||      
              lang.equals("ta") ||      
              lang.equals("te") ||      
              lang.equals("kn") ||      
              lang.equals("mr") ||      
              lang.equals("sa"))) {     
            System.out.println("Windows' locale settings for this test is incorrect.  Select one of \"Armenian\", \"Georgian\", \"Hindi\", \"Punjabi\", \"Gujarati\", \"Tamil\", \"Telugu\", \"Kannada\", \"Marathi\", or \"Sanskrit\" for the user locale, and \"English(United States)\" for the system default locale using the Control Panel.");
            return;
        }

        if (!fileenc.equals("utf-8")) {
            throw new RuntimeException("file.encoding is incorrectly set to \"" + fileenc + "\".  Should be \"utf-8\".");
        }
    }
}
