

package nsk.share.jdi;

public abstract class NamedTask implements Runnable {
    private String name;

    public NamedTask(String name) {
        this.name = name;
    }

    @Override
    public abstract void run();

    public String getName() {
        return name;
    }

}
