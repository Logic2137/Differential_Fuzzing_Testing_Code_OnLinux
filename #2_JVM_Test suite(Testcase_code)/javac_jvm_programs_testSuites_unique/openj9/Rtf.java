
package j9vm.test.libpath;

import java.io.InputStream;
import java.io.StringReader;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;


public class Rtf {

	public Rtf() {
	}

	static public String convert(String rtf) throws Exception {
		DefaultStyledDocument styledDoc = new DefaultStyledDocument();
		RTFEditorKit rtfKit = new RTFEditorKit();
		StringReader reader = null;
		reader = new StringReader(rtf);
		rtfKit.read(reader, styledDoc, 0);
		Document doc = styledDoc.getDefaultRootElement().getDocument();
		String txt = doc.getText(0, doc.getLength());
		return txt;
	}

	public static void main(String[] args) throws Exception {
		try {
			System.out.println("com.ibm.oti.vm.bootstrap.library.path=" + System.getProperty("com.ibm.oti.vm.bootstrap.library.path"));
			System.out.println("sun.boot.library.path=" + System.getProperty("sun.boot.library.path"));
			System.out.println("java.library.path=" + System.getProperty("java.library.path"));

			System.out.println("fontmanger LIB: " + System.mapLibraryName("fontmanager"));
			String rtf = "{\\rtf1\\deff0{\\fonttbl{\\f0 Times New Roman;}{\\f1 Courier New;}}{\\colortbl\\red0\\green0\\blue0 ;\\red0\\green0\\blue255 ;}{\\*\\listoverridetable}{\\stylesheet {\\ql\\f1\\fs2 0\\cf0 Normal;}{\\*\\cs1\\f1\\fs20\\cf0 Default Paragraph Font;}{\\*\\cs2\\sbasedon1\\f1\\fs20\\cf0 Line Number;}{\\*\\cs3\\ul\\f1\\fs20\\cf1 Hyperlink;}}\\sectd\\pard\\plain\\ql{\\f1\\fs20\\cf0 draw 2 extra 6ml lav. label with chart labels\"}\\f1\\fs20\\par\\pard\\plain\\ql{\\f1\\fs20\\cf0 ?When to Transfuse: When Avl}\\f1\\fs20\\par}";
			String txt = Rtf.convert(rtf);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("com.ibm.oti.vm.bootstrap.library.path = "
					+ System.getProperty("com.ibm.oti.vm.bootstrap.library.path"));
		}

		ProcessBuilder pb = new ProcessBuilder(args[0], "-Xnocompressedrefs",
				"-Dcom.sun.management.jmxremote", "-cp", args[1], "j9vm.test.libpath.RtfChild");

		System.out.println("launching child");
		Process p = pb.start();
		p.waitFor();
		p.getOutputStream().flush();

		InputStream is = p.getInputStream();
		while (is.available() > 0) {
			byte[] buf = new byte[is.available()];
			is.read(buf);
			System.out.println(new String(buf));
		}

		InputStream es = p.getErrorStream();
		while (es.available() > 0) {
			byte[] buf = new byte[es.available()];
			es.read(buf);
			System.out.println(new String(buf));
		}

	}
}
