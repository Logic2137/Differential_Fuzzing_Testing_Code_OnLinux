
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public class SimpleGrep
{
	static String sSCCSid="%Z%%M% %I% %W% %G% %U%";
	
  public static void main(String[] args) throws IOException
  {
    boolean result = false;
    switch (args.length)
    {
      case 2 :
        result = searchForStringInFile(args[0], args[1]);
        break;
      case 3 :
        result = searchForStringInFile(args[0], args[1], args[2]);
        break;
      case 0 :
      default :
        System.err.println("usage: SimpleGrep <string> <filename> [<count>]");
        System.err.println("       search for <string> in <filename>");
        System.err.println("       if <min> is specified, search for at <count> matching lines");
        break;
    }
    printResult(result);
  }
  public static boolean searchForStringInFile(String searchText, String filename, String count) throws IOException, FileNotFoundException
  {
    boolean result;
    
    int actualCount = countLinesContainingString(searchText, new FileInputStream(filename));
    int expectedCount = Integer.parseInt(count);
    
    result = expectedCount == actualCount;
    if (!result)
      displayFile(filename);
    return result;
  }
  public static boolean searchForStringInFile(String searchText, String filename) throws IOException, FileNotFoundException
  {
    boolean result;
    
    result = searchForString(searchText, new FileInputStream(filename));
    
    if (!result)
      displayFile(filename);
    return result;
  }
  public static boolean searchForString(String searchString, InputStream in) throws IOException
  {
    
    BufferedReader br = new BufferedReader(new InputStreamReader(in));

    for (String line = br.readLine(); line != null; line = br.readLine())
    {
      if (line.indexOf(searchString) != -1)
      {
        return true;
      }
    }
    br.close();
    return false;
  }

  public static int countLinesContainingString(String searchString, InputStream in) throws IOException
  {
    int count = 0;
    
    BufferedReader br = new BufferedReader(new InputStreamReader(in));

    for (String line = br.readLine(); line != null; line = br.readLine())
    {
      if (line.indexOf(searchString) != -1)
      {
        count++;
      }
    }
    br.close();
    return count;
  }

  public static void displayFile(String filename) throws IOException, FileNotFoundException
  {
    BufferedReader br = new BufferedReader(new FileReader(filename));

    for (String line = br.readLine(); line != null; line = br.readLine())
    {
      System.out.println(filename + ": " + line);
    }
  }
  public static void printResult(boolean result)
  {
    System.out.println("TEST " + (result ? "PASSED" : "FAILED"));
  }
}
