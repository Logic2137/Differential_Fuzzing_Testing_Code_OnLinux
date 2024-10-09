public class BranchToFewerDefines {

    public static void main(String[] args) {
    }

    private void problematicMethod(int p) {
        switch(p) {
            case 3:
                long n;
                while (true) {
                    if (false) {
                        break;
                    }
                }
                break;
            case 2:
                loop: while (true) {
                    while (true) {
                        int i = 4;
                        if (p != 16) {
                            return;
                        }
                        break loop;
                    }
                }
                break;
            default:
                while (true) {
                    if (false) {
                        break;
                    }
                }
                break;
        }
        long b;
        if (p != 7) {
            switch(p) {
                case 1:
                    long a = 17;
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }
    }

    private void problematicMethod2(int p) {
        switch(p) {
            case 3:
                long n;
                {
                    int i = 4;
                    break;
                }
            case 2:
                {
                    int i = 4;
                    break;
                }
            default:
                {
                    int i = 4;
                    break;
                }
        }
        long b;
        if (p != 7) {
            switch(p) {
                case 1:
                    long a = 17;
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }
    }
}
