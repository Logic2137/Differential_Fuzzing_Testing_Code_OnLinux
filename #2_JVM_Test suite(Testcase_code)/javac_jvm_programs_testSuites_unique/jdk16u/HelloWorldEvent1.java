

package jdk.jfr.jvm;

import jdk.jfr.Description;
import jdk.jfr.Enabled;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Label("Hello World")
@Description("My first event")
@Enabled
public class HelloWorldEvent1 extends Event {

    @Label("Message")
    public String message;
}
