
import java.util.ArrayList;
import java.util.List;



class Tokenizer {

	private int index = 0;
    private final String buffer;

    
    private Tokenizer(String b) {
        buffer = b;
    }

    
    private void skipWhitespace() {
        while ((index < buffer.length()) && Character.isWhitespace(buffer.charAt(index))) {
            index++;
        }
    }

    
    private String token() throws Exception {
        String tok = "";
    	skipWhitespace();

        
        while ((index < buffer.length()) && !Character.isWhitespace(buffer.charAt(index))) {

        	
            char delim = buffer.charAt(index);
            if ((delim == '"') || (delim == '\'')) {
                index++;
                int i = index;

                
                while ((i < buffer.length()) && (buffer.charAt(i) != delim)) {
                    i++;
                }

                
                if (i == buffer.length()) {
                    throw new Exception("TEST CMD FORMAT ERROR: Quote is missing in the path of .jar file: " + buffer);
                }

                tok += buffer.substring(index, i);

                
                index = i + 1;
            } else {

            	
                int i = index;

                while ((i < buffer.length()) && (buffer.charAt(i) != '"') && !Character.isWhitespace(buffer.charAt(i))) {
                    i++;
                }

                tok += buffer.substring(index, i);

                
                index = i;
            }
        }

        return tok;
    }

    
    public static String[] tokenize(String buffer) throws Exception {

    	List<String> result = new ArrayList<String>();
        Tokenizer t = new Tokenizer(buffer);
        while (t.index < buffer.length()) {
            String token = t.token();
            if (!token.isEmpty()) {
                result.add(token);
            }
        }

        String[] command = new String[result.size()];
        result.toArray(command);

        return command;
    }
}
