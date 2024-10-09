

package wsgen;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class TestServer {

    @WebMethod(operationName = "version9")
    public String getJavaVersion() {
        return "9";
    }
}
