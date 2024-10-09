import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

public class TextAndMnemonicUtils {

    private static final String LABEL_SUFFIX = ".labelAndMnemonic";

    private static ResourceBundle bundle = null;

    private static Properties properties = null;

    static {
        bundle = ResourceBundle.getBundle("resources.swingset");
        properties = new Properties();
        try {
            properties.load(TextAndMnemonicUtils.class.getResourceAsStream("resources/swingset.properties"));
        } catch (IOException ex) {
            System.out.println("java.io.IOException: Couldn't load properties from: resources/swingset.properties");
        }
    }

    public static String getTextAndMnemonicString(String key) {
        if (key.endsWith("_label")) {
            String compositeKey = composeKey(key, 6, LABEL_SUFFIX);
            String textAndMnemonic = bundle.getString(compositeKey);
            return getTextFromTextAndMnemonic(textAndMnemonic);
        }
        if (key.endsWith("_mnemonic")) {
            String compositeKey = composeKey(key, 9, LABEL_SUFFIX);
            Object value = properties.getProperty(compositeKey);
            if (value != null) {
                String textAndMnemonic = bundle.getString(compositeKey);
                return getMnemonicFromTextAndMnemonic(textAndMnemonic);
            }
        }
        String compositeKey = composeKey(key, 0, LABEL_SUFFIX);
        Object value = properties.getProperty(compositeKey);
        if (value != null) {
            String textAndMnemonic = bundle.getString(compositeKey);
            return getTextFromTextAndMnemonic(textAndMnemonic);
        }
        String textAndMnemonic = bundle.getString(key);
        return getTextFromTextAndMnemonic(textAndMnemonic);
    }

    public static String getTextFromTextAndMnemonic(String text) {
        StringBuilder sb = new StringBuilder();
        int prevIndex = 0;
        int nextIndex = text.indexOf('&');
        int len = text.length();
        while (nextIndex != -1) {
            String s = text.substring(prevIndex, nextIndex);
            sb.append(s);
            nextIndex++;
            if (nextIndex != len && text.charAt(nextIndex) == '&') {
                sb.append('&');
                nextIndex++;
            }
            prevIndex = nextIndex;
            nextIndex = text.indexOf('&', nextIndex + 1);
        }
        sb.append(text.substring(prevIndex, text.length()));
        return sb.toString();
    }

    public static String getMnemonicFromTextAndMnemonic(String text) {
        int index = text.indexOf('&');
        while (0 <= index && index < text.length() - 1) {
            index++;
            if (text.charAt(index) == '&') {
                index = text.indexOf('&', index + 1);
            } else {
                char c = text.charAt(index);
                return String.valueOf(Character.toUpperCase(c));
            }
        }
        return null;
    }

    private static String composeKey(String key, int reduce, String sufix) {
        return key.substring(0, key.length() - reduce) + sufix;
    }
}
