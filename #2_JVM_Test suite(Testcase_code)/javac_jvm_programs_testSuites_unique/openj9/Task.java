

package org.openj9.test.util.process;

import java.io.Serializable;

public interface Task extends Serializable {

    public void run() throws Exception;
}
