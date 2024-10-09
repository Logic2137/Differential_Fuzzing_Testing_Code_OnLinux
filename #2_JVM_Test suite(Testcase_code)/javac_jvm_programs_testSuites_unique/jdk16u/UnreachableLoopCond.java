



class UnreachableLoopCond {

    public void foo() {
        Integer i = 100;
        do {
            return;
        } while (i++ < 10);
    }

    public void goo() {
        Integer i = 100;
        do {
            break;
        } while (i++ < 10);
    }

    public void zoo() {
        Integer i = 100;
        do {
            throw new RuntimeException();
        } while (i++ < 10);
    }

    public void loo() {
        Integer i = 100;
        Integer j = 100;
        do {
           do {
               return;
           } while (i++ < 10);
        } while (j++ < 10);
    }

    public void moo() {
        Integer i = 100;
        do {
            if (true) {
                return;
            } else {
                return;
            }
        } while (i++ < 10);
    }

    public void moo(boolean cond) {
        Integer i = 100;
        do {
            if (cond) {
                return;
            } else {
                return;
            }
        } while (i++ < 10);
    }
}
