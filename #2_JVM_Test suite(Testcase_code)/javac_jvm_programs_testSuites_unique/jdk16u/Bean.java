

import java.awt.event.ActionListener;

public class Bean {
    private String name;
    private int number;
    private ActionListener listener;

    public Bean() {
        this("Bean", 1);
    }

    public Bean(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int i) {
        this.number = i;
    }

    

    public void addActionListener(ActionListener listener) {
        this.listener = listener;
    }

    public void removeActionListener(ActionListener listener) {
        this.listener = null;
    }

    public ActionListener[] getActionListeners() {
        return (this.listener != null)
                ? new ActionListener[] {this.listener}
                : new ActionListener[] {};
    }
}
