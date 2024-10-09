

package jdk.test.failurehandler;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class HtmlSection {
    protected final HtmlSection rootSection;
    protected final String id;
    protected final String name;

    public PrintWriter getWriter() {
        return textWriter;
    }

    protected final PrintWriter pw;
    protected final PrintWriter textWriter;
    protected boolean closed;

    private HtmlSection child;


    public HtmlSection(PrintWriter pw) {
        this(pw, "", null, null);
    }

    private HtmlSection(PrintWriter pw, String id, String name, HtmlSection rootSection) {
        this.pw = pw;
        textWriter = new PrintWriter(new HtmlFilterWriter(pw), true);
        this.id = id;
        this.name = name;
        child = null;
        
        if (rootSection == null) {
            this.rootSection = this;
            this.pw.println("<html>");
            this.pw.println("<style>\n"
                    + "div { display:none;}\n"
                    + "</style>\n"
                    + "\n"
                    + "<script>\n"
                    + "function show(e) {\n"
                    + "  while (e != null) {\n"
                    + "    if (e.tagName == 'DIV') {\n"
                    + "      e.style.display = 'block';\n"
                    + "    }\n"
                    + "    e = e.parentNode;\n"
                    + "  }\n"
                    + "}\n"
                    + "\n"
                    + "function toggle(id) {\n"
                    + "  e = document.getElementById(id);\n"
                    + "  d = e.style.display;\n"
                    + "  if (d == 'block') {\n"
                    + "    e.style.display = 'none';\n"
                    + "  } else {\n"
                    + "    show(e);\n"
                    + "  }\n"
                    + "}\n"
                    + "\n"
                    + "function main() {\n"
                    + "  index = location.href.indexOf(\"#\");"
                    + "  if (index != -1) {\n"
                    + "    show(document.getElementById(location.href.substring(index + 1)));\n"
                    + "  }\n"
                    + "}\n"
                    + "\n"
                    + "</script>\n"
                    + "</head>");

            this.pw.println("<body onload='main()'>");
        } else {
            this.rootSection = rootSection;
            this.pw.print("<ul>");
        }
    }

    public HtmlSection createChildren(String section) {
        if (child != null) {
            if (child.name.equals(section)) {
                return child;
            }
            child.close();
        }
        child = new SubSection(this, section, rootSection);
        return child;
    }

    protected final void removeChild(HtmlSection child) {
        if (this.child == child) {
            this.child = null;
        }
    }

    public void close() {
        closeChild();
        if (closed) {
            return;
        }
        closed = true;

        if (rootSection == this) {
            pw.println("</body>");
            pw.println("</html>");
            pw.close();
        } else {
            pw.println("</ul>");
        }

    }

    protected final void closeChild() {
        if (child != null) {
            child.close();
            child = null;
        }
    }

    public void link(HtmlSection section, String child, String name) {
        String path = section.id;
        if (path.isEmpty()) {
            path = child;
        } else if (child != null) {
            path = String.format("%s.%s", path, child);
        }
        pw.printf("<a href=\"#%1$s\" onclick=\"show(document.getElementById('%1$s')); return true;\">%2$s</a>%n",
                path, name);
    }

    public HtmlSection createChildren(String[] sections) {
        int i = 0;
        int n = sections.length;
        HtmlSection current = this;
        for (; i < n && current.child != null;
                ++i, current = current.child) {
            if (!sections[i].equals(current.child.name)) {
                break;
            }
        }
        for (; i < n; ++i) {
            current = current.createChildren(sections[i]);
        }
        return current;
    }

    private static class SubSection extends HtmlSection {
        private final HtmlSection parent;

        public SubSection(HtmlSection parent, String name,
                          HtmlSection rootSection) {
            super(parent.pw,
                    parent.id.isEmpty()
                            ? name
                            : String.format("%s.%s", parent.id, name),
                    name, rootSection);
            this.parent = parent;
            pw.printf("<li><a name='%1$s'/><a href='#%1$s' onclick=\"toggle('%1$s'); return false;\">%2$s</a><div id='%1$s'><code><pre>",
                    id, name);
        }

        @Override
        public void close() {
            closeChild();
            if (closed) {
                return;
            }
            pw.print("</pre></code></div></li><!-- " + id + "-->");
            parent.removeChild(this);
            super.close();
        }
    }

    private static class HtmlFilterWriter extends FilterWriter {
        public HtmlFilterWriter(PrintWriter pw) {
            super(pw);
        }

        @Override
        public void write(int c) throws IOException {
            switch (c) {
                case '<':
                    super.write("&lt;", 0, 4);
                    break;
                case '>':
                    super.write("&gt;", 0, 4);
                    break;
                case '"':
                    super.write("&quot;", 0, 5);
                    break;
                case '&':
                    super.write("&amp;", 0, 4);
                    break;
                default:
                    super.write(c);
            }
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            for (int i = off; i < len; ++i){
                write(cbuf[i]);
            }
        }

        @Override
        public void write(String str, int off, int len) throws IOException {
            for (int i = off; i < len; ++i){
                write(str.charAt(i));
            }
        }
    }
}
