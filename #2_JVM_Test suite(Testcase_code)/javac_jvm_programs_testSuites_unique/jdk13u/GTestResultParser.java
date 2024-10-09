import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GTestResultParser {

    private final List<String> _failedTests;

    public GTestResultParser(Path file) {
        List<String> failedTests = new ArrayList<>();
        try (Reader r = Files.newBufferedReader(file)) {
            try {
                XMLStreamReader xmlReader = XMLInputFactory.newInstance().createXMLStreamReader(r);
                String testSuite = null;
                String testCase = null;
                while (xmlReader.hasNext()) {
                    switch(xmlReader.next()) {
                        case XMLStreamConstants.START_ELEMENT:
                            switch(xmlReader.getLocalName()) {
                                case "testsuite":
                                    testSuite = xmlReader.getAttributeValue("", "name");
                                    break;
                                case "testcase":
                                    testCase = xmlReader.getAttributeValue("", "name");
                                    break;
                                case "failure":
                                    failedTests.add(testSuite + "::" + testCase);
                                default:
                            }
                            break;
                        default:
                    }
                }
            } catch (XMLStreamException e) {
                throw new IllegalArgumentException("can't open parse xml " + file, e);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("can't open result file " + file, e);
        }
        _failedTests = Collections.unmodifiableList(failedTests);
    }

    public List<String> failedTests() {
        return _failedTests;
    }
}
