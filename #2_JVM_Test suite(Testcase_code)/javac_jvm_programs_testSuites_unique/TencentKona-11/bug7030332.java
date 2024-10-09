



import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class bug7030332 extends JApplet {
    public static final String[] HTML_SAMPLES = new String[]{
            "<table border><tr><th>Column1</th><th>Column2</th></tr></table>",
            "<table border=\"\"><tr><th>Column1</th><th>Column2</th></tr></table>",
            "<table border=\"1\"><tr><th>Column1</th><th>Column2</th></tr></table>",
            "<table border=\"2\"><tr><th>Column1</th><th>Column2</th></tr></table>",
    };

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();

                frame.setContentPane(createContentPane());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setSize(600, 400);
                frame.setLocationRelativeTo(null);

                frame.setVisible(true);

            }
        });
    }

    public void init() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    setContentPane(createContentPane());
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Container createContentPane() {
        JPanel result = new JPanel(new GridLayout(HTML_SAMPLES.length + 1, 3, 10, 10));

        result.add(new JLabel("Html code"));
        result.add(new JLabel("Golden image"));
        result.add(new JLabel("JEditorPane"));

        for (int i = 0; i < HTML_SAMPLES.length; i++) {
            String htmlSample = HTML_SAMPLES[i];

            JTextArea textArea = new JTextArea(htmlSample);

            textArea.setLineWrap(true);

            result.add(textArea);

            String imageName = "sample" + i + ".png";
            URL resource = bug7030332.class.getResource(imageName);

            result.add(resource == null ? new JLabel(imageName + " not found") :
                    new JLabel(new ImageIcon(resource), SwingConstants.LEFT));

            result.add(new JEditorPane("text/html", htmlSample));
        }

        return result;
    }
}
