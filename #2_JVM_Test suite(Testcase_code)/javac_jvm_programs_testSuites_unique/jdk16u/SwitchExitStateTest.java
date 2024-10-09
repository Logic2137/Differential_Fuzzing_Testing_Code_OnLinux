



public class SwitchExitStateTest {
    public static void main(String[] args) throws Exception {
        switch (0) {
        case 0:
            String a = "";
            break;
        default:
            throw new Exception("Unknown ");
        }

        switch (0) {
        case 0:
            String b = "";
            break;
        default:
            throw new Exception("Unknown ");
        }
    }
}
