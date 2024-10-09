import javax.swing.text.html.parser.ContentModel;
import javax.swing.text.html.parser.DTD;
import javax.swing.text.html.parser.Element;

public class bug8074956 {

    public static void main(String[] args) throws Exception {
        final DTD html32 = DTD.getDTD("html32");
        ContentModel contentModel = new ContentModel('&', new ContentModel());
        Element elem1 = html32.getElement("html-element");
        contentModel.first(elem1);
        Element elem2 = html32.getElement("test-element");
        contentModel.first(elem2);
    }
}
