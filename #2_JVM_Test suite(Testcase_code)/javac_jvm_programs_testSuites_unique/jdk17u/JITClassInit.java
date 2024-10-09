
package compiler.runtime;

public class JITClassInit {

    public static void main(String[] args) {
        Token t = new Token();
        new TokenTable();
    }
}

class TokenTable {

    public TokenTable() {
        new TokenTypeIterator(this);
    }

    public void for_token_type(Token t) {
        t.keyword_character_class();
    }
}

class Token {

    public Object keyword_character_class() {
        return new Object();
    }
}

class NameOrKeywordToken extends Token {

    static TokenTable kt = new TokenTable();

    public Object keyword_character_class() {
        return new Object();
    }
}

class CapKeywordToken extends NameOrKeywordToken {

    public Object keyword_character_class() {
        return new Object();
    }
}

class TokenTypeIterator {

    public TokenTypeIterator(TokenTable c) {
        c.for_token_type(new CapKeywordToken());
        c.for_token_type(new NameOrKeywordToken());
    }
}
