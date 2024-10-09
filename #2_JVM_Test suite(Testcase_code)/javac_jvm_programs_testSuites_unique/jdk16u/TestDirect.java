





import java.util.List;
import javax.smartcardio.Card;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.TerminalFactory;

public class TestDirect {
    public static void main(String[] args) throws Exception {
        TerminalFactory terminalFactory = TerminalFactory.getDefault();
        List<CardTerminal> cardTerminals = terminalFactory.terminals().list();
        if (cardTerminals.isEmpty()) {
            System.out.println("Skipping the test: " +
                    "no card terminals available");
            return;
        }
        System.out.println("Terminals: " + cardTerminals);
        CardTerminal cardTerminal = cardTerminals.get(0);
        Card card = cardTerminal.connect("DIRECT");
        card.disconnect(true);

        System.out.println("OK.");
    }
}
