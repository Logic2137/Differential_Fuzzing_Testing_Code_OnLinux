import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Repeatable(RuntimeInvisibleRepeatableContainer.class)
@interface RuntimeInvisibleRepeatable {

    boolean booleanValue() default false;

    byte byteValue() default 0;

    char charValue() default 0;

    short shortValue() default 0;

    int intValue() default 0;

    long longValue() default 0;

    float floatValue() default 0;

    double doubleValue() default 0;

    String stringValue() default "";

    int[] arrayValue1() default {};

    String[] arrayValue2() default {};

    Class<?> classValue1() default void.class;

    Class<?> classValue2() default void.class;

    EnumValue enumValue() default EnumValue.VALUE1;

    AnnotationValue annoValue() default @AnnotationValue(stringValue = "StringValue");

    AnnotationValue[] annoArrayValue() default { @AnnotationValue(stringValue = "StringValue1"), @AnnotationValue(stringValue = "StringValue2"), @AnnotationValue(stringValue = "StringValue3") };
}

@Retention(RetentionPolicy.CLASS)
@interface RuntimeInvisibleRepeatableContainer {

    RuntimeInvisibleRepeatable[] value();
}

@interface RuntimeInvisibleNotRepeatable {

    boolean booleanValue() default false;

    byte byteValue() default 0;

    char charValue() default 0;

    short shortValue() default 0;

    int intValue() default 0;

    long longValue() default 0;

    float floatValue() default 0;

    double doubleValue() default 0;

    String stringValue() default "";

    int[] arrayValue1() default {};

    String[] arrayValue2() default {};

    Class<?> classValue1() default void.class;

    Class<?> classValue2() default void.class;

    EnumValue enumValue() default EnumValue.VALUE1;

    AnnotationValue annoValue() default @AnnotationValue(stringValue = "StringValue");

    AnnotationValue[] annoArrayValue() default { @AnnotationValue(stringValue = "StringValue1"), @AnnotationValue(stringValue = "StringValue2"), @AnnotationValue(stringValue = "StringValue3") };
}

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RuntimeVisibleRepeatableContainer.class)
@interface RuntimeVisibleRepeatable {

    boolean booleanValue() default false;

    byte byteValue() default 0;

    char charValue() default 0;

    short shortValue() default 0;

    int intValue() default 0;

    long longValue() default 0;

    float floatValue() default 0;

    double doubleValue() default 0;

    String stringValue() default "";

    int[] arrayValue1() default {};

    String[] arrayValue2() default {};

    Class<?> classValue1() default void.class;

    Class<?> classValue2() default void.class;

    EnumValue enumValue() default EnumValue.VALUE1;

    AnnotationValue annoValue() default @AnnotationValue(stringValue = "StringValue");

    AnnotationValue[] annoArrayValue() default { @AnnotationValue(stringValue = "StringValue1"), @AnnotationValue(stringValue = "StringValue2"), @AnnotationValue(stringValue = "StringValue3") };
}

@Retention(RetentionPolicy.RUNTIME)
@interface RuntimeVisibleRepeatableContainer {

    RuntimeVisibleRepeatable[] value();
}

@Retention(RetentionPolicy.RUNTIME)
@interface RuntimeVisibleNotRepeatable {

    boolean booleanValue() default false;

    byte byteValue() default 0;

    char charValue() default 0;

    short shortValue() default 0;

    int intValue() default 0;

    long longValue() default 0;

    float floatValue() default 0;

    double doubleValue() default 0;

    String stringValue() default "";

    int[] arrayValue1() default {};

    String[] arrayValue2() default {};

    Class<?> classValue1() default void.class;

    Class<?> classValue2() default void.class;

    EnumValue enumValue() default EnumValue.VALUE1;

    AnnotationValue annoValue() default @AnnotationValue(stringValue = "StringValue");

    AnnotationValue[] annoArrayValue() default { @AnnotationValue(stringValue = "StringValue1"), @AnnotationValue(stringValue = "StringValue2"), @AnnotationValue(stringValue = "StringValue3") };
}

enum EnumValue {

    VALUE1, VALUE2, VALUE3
}

@interface AnnotationValue {

    String stringValue() default "";
}
