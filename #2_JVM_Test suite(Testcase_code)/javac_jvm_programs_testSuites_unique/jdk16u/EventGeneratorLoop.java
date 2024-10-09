
import jdk.jfr.Event;
import jdk.jfr.Description;
import jdk.jfr.Label;



public class EventGeneratorLoop {
    public static final String MAIN_METHOD_STARTED = "MAIN_METHOD_STARTED";

    @Label("SimpleEvent")
    @Description("Simple custom event")
    static class SimpleEvent extends Event {
        @Label("Message")
        String msg;

        @Label("Count")
        int count;
    }

    public static void main(String[] args) throws Exception {
        if ((args.length < 1) || (args[0] == null)) {
            throw new IllegalArgumentException("Expecting one argument: time to run (seconds)");
        }
        int howLong = Integer.parseInt(args[0]);

        System.out.println(MAIN_METHOD_STARTED + ", argument is " + howLong);

        for (int i=0; i < howLong; i++) {
            SimpleEvent ev = new SimpleEvent();
            ev.msg = "Hello";
            ev.count = i;
            ev.commit();

            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            System.out.print(".");
        }

        System.out.println("EventGeneratorLoop is coming to an end");
    }

}
