
package com.oti.j9.exclude;


import java.util.Hashtable;

public class XMLSpec {

	static final Hashtable sEscapedChars;

	static {
		sEscapedChars = new Hashtable();
		sEscapedChars.put("lt",  "<");
		sEscapedChars.put("gt",  ">");
		sEscapedChars.put("amp", "&");
		sEscapedChars.put("apos","'");
		sEscapedChars.put("quot","\"");
	}

	static boolean isLetter(char c)
	{
		return (c >= 'A' && c <= 'z');
	}

	static boolean isDigit(char c)
	{
		return Character.isDigit(c);
	}

	static boolean isNameChar(char c)
	{
		return isLetter(c) || isDigit(c) || isAnyOf(".-_:", c);
	}

	static boolean isNameStartChar(char c)
	{
		return isLetter(c) || isAnyOf("_:", c);
	}

	static boolean isLineDelimiter(char c)
	{
		return c == '\n' || c == '\r';
	}

	static boolean isWhitespace(char c)
	{
		return Character.isWhitespace(c);
	}

	static boolean isSpace(char c)
	{
		return c == ' ';
	}

	static String namedEscapeToString(String escapeSequence)
	{
		return (String) sEscapedChars.get(escapeSequence);
	}

	static boolean isAnyOf(String chars, char c)
	{
		return chars.indexOf(String.valueOf(c), 0) >= 0;
	}

}
