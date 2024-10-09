class ThreadPrint {

    public static void println(String s) {
        System.out.println(Thread.currentThread().getName() + ": " + s);
        System.out.flush();
    }
}
