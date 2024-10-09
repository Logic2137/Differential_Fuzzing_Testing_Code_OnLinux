


import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.spi.AttachProvider;

import java.io.IOException;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;


public class SimpleProvider extends AttachProvider {
    public SimpleProvider() {
    }

    public String name() {
        return "simple";
    }

    public String type() {
        return "none";
    }

    public VirtualMachine attachVirtualMachine(String id)
        throws AttachNotSupportedException, IOException
    {
        if (!id.startsWith("simple:")) {
            throw new AttachNotSupportedException("id not recognized");
        }
        return new SimpleVirtualMachine(this, id);
    }

    public List<VirtualMachineDescriptor> listVirtualMachines() {
        return new ArrayList<VirtualMachineDescriptor>();
    }
}

class SimpleVirtualMachine extends VirtualMachine {
    public SimpleVirtualMachine(AttachProvider provider, String id) {
        super(provider, id);
    }

    public void detach() throws IOException {
    }

    public void loadAgentLibrary(String agentLibrary, String options)
        throws IOException, AgentLoadException, AgentInitializationException
    {
    }

    public void loadAgentPath(String agentLibrary, String options)
        throws IOException, AgentLoadException, AgentInitializationException
    {
    }

    public void loadAgent(String agentLibrary, String options)
        throws IOException, AgentLoadException, AgentInitializationException
    {
    }

    public Properties getSystemProperties() throws IOException {
        return new Properties();
    }

    public Properties getAgentProperties() throws IOException {
        return new Properties();
    }

    public void dataDumpRequest() throws IOException {
    }

    public String startLocalManagementAgent() {
        return null;
    }

    public void startManagementAgent(Properties agentProperties) {
    }

}
