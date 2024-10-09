
package nsk.share.aod;


public class AgentInformation {

    

    private static int jarAgentsCounter;

    private static int nativeAgentsCounter;

    public boolean jarAgent;

    public String pathToAgent;

    public String agentOptions;

    public AgentInformation(boolean jarAgent, String pathToAgent, String options, boolean addAgentNameOption) {
        this.jarAgent = jarAgent;
        this.pathToAgent = pathToAgent;
        this.agentOptions = options;

        

        String name;

        if (jarAgent)
            name = "JarAgent-" + jarAgentsCounter++;
        else
            name = "NativeAgent-" + nativeAgentsCounter++;

        if (addAgentNameOption) {
            if (this.agentOptions == null) {
                this.agentOptions = "-agentName=" + name;
            } else {
                this.agentOptions += " -agentName=" + name;
            }
        }
    }

    public AgentInformation(boolean jarAgent, String pathToAgent, String options) {
        this(jarAgent, pathToAgent, options, true);
    }
}
