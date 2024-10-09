



import java.util.GregorianCalendar;

@SuppressWarnings("serial")
public class Bug4766302 {

    static class MyCalendar extends GregorianCalendar {

        boolean isTimeStillSet() {
            return isTimeSet;
        }

        protected void computeTime() {
            super.computeTime();
        }
    }

    public static void main(String[] args) {
        MyCalendar cal = new MyCalendar();
        cal.computeTime();
        if (!cal.isTimeStillSet()) {
            throw new RuntimeException("computeTime() call reset isTimeSet.");
        }
    }
}
