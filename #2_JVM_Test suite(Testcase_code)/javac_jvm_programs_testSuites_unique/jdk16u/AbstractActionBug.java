


import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public final class AbstractActionBug extends AbstractAction implements Cloneable
{
    public static final void main(String[] args) throws Exception
    {
        AbstractActionBug a1 = new AbstractActionBug("a1");
        a1 = (AbstractActionBug) a1.clone();
        System.out.println("a1 cloned ok");

        AbstractActionBug a2 = new AbstractActionBug("a2");
        a2.putValue(NAME, "null");
        a2 = (AbstractActionBug) a2.clone();
        System.out.println("a2 cloned ok");

        AbstractActionBug a3 = new AbstractActionBug(null);
        a3 = (AbstractActionBug) a3.clone();
        System.out.println("a3 cloned ok");
    }

    private AbstractActionBug(String name) {
        putValue(NAME, name);
    }

    public void actionPerformed(ActionEvent e)
    {
    }
}
