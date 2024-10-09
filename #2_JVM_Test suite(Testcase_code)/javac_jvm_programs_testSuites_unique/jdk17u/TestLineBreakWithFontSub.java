import java.text.AttributedString;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;

public class TestLineBreakWithFontSub {

    public static void main(String[] args) {
        new TestLineBreakWithFontSub().test();
        System.out.println("Line break / font substitution test PASSED");
    }

    private static final String WORD = "word";

    private static final String SPACING = " ";

    private static final String MIXED = "A\u05D0";

    private static final int NUM_WORDS = 12;

    private static final FontRenderContext DEFAULT_FRC = new FontRenderContext(null, false, false);

    public void test() {
        StringBuffer text = new StringBuffer(MIXED);
        for (int i = 0; i < NUM_WORDS; i++) {
            text.append(SPACING);
            text.append(WORD);
        }
        AttributedString attrString = new AttributedString(text.toString());
        attrString.addAttribute(TextAttribute.SIZE, new Float(24.0));
        LineBreakMeasurer measurer = new LineBreakMeasurer(attrString.getIterator(), DEFAULT_FRC);
        int sequenceLength = WORD.length() + SPACING.length();
        measurer.setPosition(text.length() - sequenceLength);
        TextLayout layout = measurer.nextLayout(10000.0f);
        if (layout.getCharacterCount() != sequenceLength) {
            throw new Error("layout length is incorrect!");
        }
        final float sequenceAdvance = layout.getVisibleAdvance();
        float wrappingWidth = sequenceAdvance * 2;
        while (wrappingWidth < (sequenceAdvance * NUM_WORDS)) {
            measurer.setPosition(0);
            checkMeasurer(measurer, wrappingWidth, sequenceAdvance, text.length());
            wrappingWidth += sequenceAdvance / 5;
        }
    }

    private void checkMeasurer(LineBreakMeasurer measurer, float wrappingWidth, float sequenceAdvance, int endPosition) {
        do {
            TextLayout layout = measurer.nextLayout(wrappingWidth);
            float visAdvance = layout.getVisibleAdvance();
            if (visAdvance > wrappingWidth) {
                throw new Error("layout is too long");
            }
            if (measurer.getPosition() < endPosition) {
                if (visAdvance <= wrappingWidth - sequenceAdvance) {
                    throw new Error("room for more words on line.  diff=" + (wrappingWidth - sequenceAdvance - visAdvance));
                }
            }
        } while (measurer.getPosition() != endPosition);
    }
}
