

package jdk.jfr.jvm;

import jdk.jfr.Description;
import jdk.jfr.Enabled;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Label("Hello World")
@Description("My second event")
@Enabled
public class HelloWorldEvent2 extends Event {

    @Label("Message")
    public String message;
}
