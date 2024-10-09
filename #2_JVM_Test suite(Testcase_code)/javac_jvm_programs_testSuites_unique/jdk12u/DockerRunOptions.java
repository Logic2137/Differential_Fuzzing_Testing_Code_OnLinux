

package jdk.test.lib.containers.docker;

import java.util.ArrayList;
import java.util.Collections;




public class DockerRunOptions {
    public String imageNameAndTag;
    public ArrayList<String> dockerOpts = new ArrayList<String>();
    public String command;    
    public ArrayList<String> javaOpts = new ArrayList<String>();
    public String classToRun;  
    public ArrayList<String> classParams = new ArrayList<String>();

    public boolean tty = true;
    public boolean removeContainerAfterUse = true;
    public boolean appendTestJavaOptions = true;
    public boolean retainChildStdout = false;

    
    public DockerRunOptions(String imageNameAndTag, String javaCmd,
                            String classToRun, String... javaOpts) {
        this.imageNameAndTag = imageNameAndTag;
        this.command = javaCmd;
        this.classToRun = classToRun;
        this.addJavaOpts(javaOpts);
    }

    public DockerRunOptions addDockerOpts(String... opts) {
        Collections.addAll(dockerOpts, opts);
        return this;
    }

    public DockerRunOptions addJavaOpts(String... opts) {
        Collections.addAll(javaOpts, opts);
        return this;
    }

    public DockerRunOptions addClassOptions(String... opts) {
        Collections.addAll(classParams,opts);
        return this;
    }
}
