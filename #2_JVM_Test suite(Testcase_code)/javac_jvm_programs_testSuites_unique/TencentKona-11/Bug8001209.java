


import java.text.*;

public class Bug8001209 {

    public static void main(String[] args) throws Exception {
        boolean err = false;

        
        double[] limits = {1,2,3,4,5,6,7};
        String[] dayOfWeekNames = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
        ChoiceFormat form = new ChoiceFormat(limits, dayOfWeekNames);
        ParsePosition status = new ParsePosition(0);

        StringBuilder before = new StringBuilder();
        for (double i = 1.0; i <= 7.0; ++i) {
            status.setIndex(0);
            String s = form.format(i);
            before.append(" ");
            before.append(s);
            before.append(form.parse(form.format(i),status));
        }
        String original = before.toString();

        double[] newLimits = form.getLimits();
        String[] newFormats = (String[])form.getFormats();
        newFormats[6] = "Doyoubi";
        StringBuilder after = new StringBuilder();
        for (double i = 1.0; i <= 7.0; ++i) {
            status.setIndex(0);
            String s = form.format(i);
            after.append(" ");
            after.append(s);
            after.append(form.parse(form.format(i),status));
        }
        if (!original.equals(after.toString())) {
            err = true;
            System.err.println("  Expected:" + before
                               + "\n  Got:     " + after);
        }

        dayOfWeekNames[6] = "Saturday";
        after = new StringBuilder();
        for (double i = 1.0; i <= 7.0; ++i) {
            status.setIndex(0);
            String s = form.format(i);
            after.append(" ");
            after.append(s);
            after.append(form.parse(form.format(i),status));
        }
        if (!original.equals(after.toString())) {
            err = true;
            System.err.println("  Expected:" + before
                               + "\n  Got:     " + after);
        }

        if (err) {
            throw new RuntimeException("Failed.");
        } else {
            System.out.println("Passed.");
        }
    }
}
