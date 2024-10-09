



import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

class T8210495 {
    interface IFilter {
        Component getComponent();
    }

    static class Filter implements IFilter {
        @Override
        public Component getComponent() {
            return null;
        }

    }

    public Component buildFilter(List<? extends Filter> l, Dialog dialog) {
        Panel c = new Panel();
        l.stream()
                .map(f -> {
                    Button btn = (Button)f.getComponent();
                    btn.addActionListener((java.io.Serializable & ActionListener)evt -> {
                        applyFilter(f);
                        dialog.setVisible(false);
                    });
                    return btn;
                })
                .forEach(c::add);
        return c;
    }

    private void applyFilter(IFilter f) { }
}
