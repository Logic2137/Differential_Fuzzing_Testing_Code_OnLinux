

package jdk.deprcases.members;

public class ExampleElements {
    @Deprecated
    Object emptyFalse;

    @Deprecated(since="xyzzy")
    Object sinceFalse;

    @Deprecated(forRemoval=true)
    Object emptyTrue;

    @Deprecated(since="plugh", forRemoval=true)
    Object sinceTrue;

    @Deprecated(since="123,456")
    Object sinceWithComma;

    @Deprecated(since="7.9 \"pre-beta\" snapshot")
    Object sinceWithQuote;
}
