


import java.util.*;

public class CheckNoClassCastException {
    static String result = "";
    public static void main(String[] args) {
        ListFail.main(null);
        MapFail.main(null);
        if (!result.equals("ListFailDoneMapFailDone"))
            throw new AssertionError("Incorrect result");
    }
}

class ListFail {
    static interface Foo {
    }

    public static void main(String[] args) {
        List<Date> list = new ArrayList<>();
        list.add(new Date());

        List<Foo> cList = (List<Foo>) (List<?>) list;
        Date date = (Date) cList.get(0);
        CheckNoClassCastException.result += "ListFailDone";
    }
}


class MapFail {
    static interface Foo {
    }

    public static void main(String[] args) {
        Map<String,Date> aMap = new HashMap<>();
        aMap.put("test",new Date());

        Map<String,Foo> m = (Map<String,Foo>) (Map<?,?>) aMap;
        Date q = (Date) m.get("test");
        CheckNoClassCastException.result += "MapFailDone";
    }
}
