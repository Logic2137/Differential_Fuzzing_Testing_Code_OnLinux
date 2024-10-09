


public class StringContentEqualsBug {

    abstract static class Task extends Thread {
        volatile StringBuffer sb;
        volatile Exception exception;

        Task(StringBuffer sb) {
            this.sb = sb;
        }

        @Override
        public void run() {
            try {
                StringBuffer sb;
                while ((sb = this.sb) != null) {
                    doWith(sb);
                }
            }
            catch (Exception e) {
                exception = e;
            }
        }

        protected abstract void doWith(StringBuffer sb);
    }

    static class Tester extends Task {
        Tester(StringBuffer sb) {
            super(sb);
        }

        @Override
        protected void doWith(StringBuffer sb) {
            "AA".contentEquals(sb);
        }
    }

    static class Disturber extends Task {
        Disturber(StringBuffer sb) {
            super(sb);
        }

        @Override
        protected void doWith(StringBuffer sb) {
            sb.setLength(0);
            sb.trimToSize();
            sb.append("AA");
        }
    }


    public static void main(String[] args) throws Exception {
        StringBuffer sb = new StringBuffer();
        Task[] tasks = new Task[3];
        (tasks[0] = new Tester(sb)).start();
        for (int i = 1; i < tasks.length; i++) {
            (tasks[i] = new Disturber(sb)).start();
        }

        try
        {
            
            for (int i = 0; i < 20; i++) {
                for (Task task : tasks) {
                    if (task.exception != null) {
                        throw task.exception;
                    }
                }
                Thread.sleep(250L);
            }
        }
        finally {
            for (Task task : tasks) {
                task.sb = null;
                task.join();
            }
        }
    }
}
