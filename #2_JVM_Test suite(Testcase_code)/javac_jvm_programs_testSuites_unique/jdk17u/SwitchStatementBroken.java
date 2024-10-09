public class SwitchStatementBroken {

    private void statementBroken(int i) {
        String res;
        switch(i) {
            case 0:
                {
                    res = "NULL-A";
                }
            case 1:
                {
                    res = "NULL-A";
                    break;
                }
            case 2:
                {
                    res = "NULL-A";
                    break;
                }
            default:
                {
                    res = "NULL-A";
                    break;
                }
        }
    }
}
