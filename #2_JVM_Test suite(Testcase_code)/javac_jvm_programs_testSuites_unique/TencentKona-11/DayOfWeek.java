



enum DayOfWeek {
    SUNDAY("Sun"),
    MONDAY("Mon"),
    TUESDAY("Tue"),
    WEDNESDAY("Wed"),
    THURSDAY("Thu"),
    FRIDAY("Fri"),
    SATURDAY("Sat");

    private final String abbr;

    private DayOfWeek(String abbr) {
        this.abbr = abbr;
    }

    String getAbbr() {
        return abbr;
    }

    int value() {
        return ordinal() + 1;
    }
}
