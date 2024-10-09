import java.text.DateFormatSymbols;

@SuppressWarnings("serial")
public class DateFormatSymbolsCloneTest extends DateFormatSymbols {

    private int value;

    public DateFormatSymbolsCloneTest() {
        value = 1;
    }

    @Override
    public Object clone() {
        if (this.value == 0) {
            throw new RuntimeException("clone() should not be called from a DateFormatSymbols constructor");
        }
        return super.clone();
    }

    public static void main(String[] args) {
        new DateFormatSymbolsCloneTest();
    }
}
