



import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.io.*;
import java.sql.SQLException;
import java.sql.SQLTransientException;

class CheckNoTimeoutException {

    interface V {List<?> foo(List<String> arg) throws EOFException, SQLException, TimeoutException;}
    interface U {Collection foo(List<String> arg) throws IOException, SQLTransientException;}

    
    interface UV extends U, V {}


    private static List<String> strs = new ArrayList<String>();
    void methodUV(UV uv) {
        System.out.println("methodUV(): SAM type interface UV object instantiated: " + uv);
        try{
            System.out.println("result returned: " + uv.foo(strs));
        }catch(EOFException e){
            System.out.println(e.getMessage());
        }catch(SQLTransientException ex){
            System.out.println(ex.getMessage());
        }
    }
}
