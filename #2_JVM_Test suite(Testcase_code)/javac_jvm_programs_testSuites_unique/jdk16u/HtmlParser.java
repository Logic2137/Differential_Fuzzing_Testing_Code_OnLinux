

package javadoc.tester;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;


public abstract class HtmlParser {

    protected final PrintStream out;
    protected final Function<Path,String> fileReader;

    private Path file;
    private StringReader in;
    private int ch;
    private int lineNumber;
    private boolean inScript;
    private boolean xml;

    HtmlParser(PrintStream out, Function<Path,String> fileReader) {
        this.out = out;
        this.fileReader = fileReader;
    }

    
    void read(Path file) throws IOException {
        try (StringReader r = new StringReader(fileReader.apply(file))) {
            this.file = file;
            this.in = r;
            StringBuilder content = new StringBuilder();

            startFile(file);
            try {
                lineNumber = 1;
                xml = false;
                nextChar();

                while (ch != -1) {
                    switch (ch) {

                        case '<':
                            content(content.toString());
                            content.setLength(0);
                            html();
                            break;

                        default:
                            content.append((char) ch);
                            if (ch == '\n') {
                                content(content.toString());
                                content.setLength(0);
                            }
                            nextChar();
                    }
                }
            } finally {
                endFile();
            }
        } catch (IOException e) {
            error(file, lineNumber, e);
        } catch (Throwable t) {
            error(file, lineNumber, t);
            t.printStackTrace(out);
        }
    }


    protected int getLineNumber() {
        return lineNumber;
    }

    
    protected void startFile(Path file) { }

    
    protected void endFile() { }

    
    protected void docType(String s) { }

    
    protected void startElement(String name, Map<String,String> attrs, boolean selfClosing) { }

    
    protected void endElement(String name) { }

    
    protected void content(String content) { }

    
    protected void error(Path file, int lineNumber, String message) {
        out.println(file + ":" + lineNumber + ": " + message);
    }

    
    protected void error(Path file, int lineNumber, Throwable t) {
        out.println(file + ":" + lineNumber + ": " + t);
    }

    private void nextChar() throws IOException {
        ch = in.read();
        if (ch == '\n')
            lineNumber++;
    }

    
    private void html() throws IOException {
        nextChar();
        if (isIdentifierStart((char) ch)) {
            String name = readIdentifier().toLowerCase(Locale.US);
            Map<String,String> attrs = htmlAttrs();
            if (attrs != null) {
                boolean selfClosing = false;
                if (ch == '/') {
                    nextChar();
                    selfClosing = true;
                }
                if (ch == '>') {
                    nextChar();
                    startElement(name, attrs, selfClosing);
                    if (name.equals("script")) {
                        inScript = true;
                    }
                    return;
                }
            }
        } else if (ch == '/') {
            nextChar();
            if (isIdentifierStart((char) ch)) {
                String name = readIdentifier().toLowerCase(Locale.US);
                skipWhitespace();
                if (ch == '>') {
                    nextChar();
                    endElement(name);
                    if (name.equals("script")) {
                        inScript = false;
                    }
                    return;
                }
            }
        } else if (ch == '!') {
            nextChar();
            if (ch == '-') {
                nextChar();
                if (ch == '-') {
                    nextChar();
                    while (ch != -1) {
                        int dash = 0;
                        while (ch == '-') {
                            dash++;
                            nextChar();
                        }
                        
                        
                        
                        
                        if (dash >= 2 && ch == '>') {
                            nextChar();
                            return;
                        }

                        nextChar();
                    }
                }
            } else if (ch == '[') {
                nextChar();
                if (ch == 'C') {
                    nextChar();
                    if (ch == 'D') {
                        nextChar();
                        if (ch == 'A') {
                            nextChar();
                            if (ch == 'T') {
                                nextChar();
                                if (ch == 'A') {
                                    nextChar();
                                    if (ch == '[') {
                                        while (true) {
                                            nextChar();
                                            if (ch == ']') {
                                                nextChar();
                                                if (ch == ']') {
                                                    nextChar();
                                                    if (ch == '>') {
                                                        nextChar();
                                                        return;
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                StringBuilder sb = new StringBuilder();
                while (ch != -1 && ch != '>') {
                    sb.append((char) ch);
                    nextChar();
                }
                Pattern p = Pattern.compile("(?is)doctype\\s+html\\s?.*");
                String s = sb.toString();
                if (p.matcher(s).matches()) {
                    docType(s);
                    return;
                }
            }
        } else if (ch == '?') {
            nextChar();
            if (ch == 'x') {
                nextChar();
                if (ch == 'm') {
                    nextChar();
                    if (ch == 'l') {
                        Map<String,String> attrs = htmlAttrs();
                        if (ch == '?') {
                            nextChar();
                            if (ch == '>') {
                                nextChar();
                                xml = true;
                                return;
                            }
                        }
                    }
                }

            }
        }

        if (!inScript) {
            error(file, lineNumber, "bad html");
        }
    }

    
    private Map<String,String> htmlAttrs() throws IOException {
        Map<String, String> map = new LinkedHashMap<>();
        skipWhitespace();

        loop:
        while (isIdentifierStart((char) ch)) {
            String name = readAttributeName().toLowerCase(Locale.US);
            skipWhitespace();
            String value = null;
            if (ch == '=') {
                nextChar();
                skipWhitespace();
                if (ch == '\'' || ch == '"') {
                    char quote = (char) ch;
                    nextChar();
                    StringBuilder sb = new StringBuilder();
                    while (ch != -1 && ch != quote) {
                        sb.append((char) ch);
                        nextChar();
                    }
                    value = sb.toString() 
                            .replace("&lt;", "<")
                            .replace("&gt;", ">")
                            .replace("&amp;", "&");
                    nextChar();
                } else {
                    StringBuilder sb = new StringBuilder();
                    while (ch != -1 && !isUnquotedAttrValueTerminator((char) ch)) {
                        sb.append((char) ch);
                        nextChar();
                    }
                    value = sb.toString();
                }
                skipWhitespace();
            }
            map.put(name, value);
        }

        return map;
    }

    private boolean isIdentifierStart(char ch) {
        return Character.isUnicodeIdentifierStart(ch);
    }

    private String readIdentifier() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append((char) ch);
        nextChar();
        while (ch != -1 && Character.isUnicodeIdentifierPart(ch)) {
            sb.append((char) ch);
            nextChar();
        }
        return sb.toString();
    }

    private String readAttributeName() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append((char) ch);
        nextChar();
        while (ch != -1 && Character.isUnicodeIdentifierPart(ch)
                || ch == '-'
                || xml && ch == ':') {
            sb.append((char) ch);
            nextChar();
        }
        return sb.toString();
    }

    private boolean isWhitespace(char ch) {
        return Character.isWhitespace(ch);
    }

    private void skipWhitespace() throws IOException {
        while (isWhitespace((char) ch)) {
            nextChar();
        }
    }

    private boolean isUnquotedAttrValueTerminator(char ch) {
        switch (ch) {
            case '\f': case '\n': case '\r': case '\t':
            case ' ':
            case '"': case '\'': case '`':
            case '=': case '<': case '>':
                return true;
            default:
                return false;
        }
    }
}
