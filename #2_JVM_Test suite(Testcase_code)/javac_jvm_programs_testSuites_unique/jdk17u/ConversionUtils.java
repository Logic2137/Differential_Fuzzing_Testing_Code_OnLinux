
package nsk.share.jpda;

public class ConversionUtils {

    public static boolean informationLossByteToByte(Byte value) {
        return false;
    }

    public static boolean informationLossByteToShort(Byte value) {
        return false;
    }

    public static boolean informationLossByteToChar(Byte value) {
        return (value.byteValue() > Character.MAX_VALUE) || (value.byteValue() < Character.MIN_VALUE);
    }

    public static boolean informationLossByteToInt(Byte value) {
        return false;
    }

    public static boolean informationLossByteToLong(Byte value) {
        return false;
    }

    public static boolean informationLossByteToFloat(Byte value) {
        return false;
    }

    public static boolean informationLossByteToDouble(Byte value) {
        return false;
    }

    public static boolean informationLossShortToShort(Short value) {
        return false;
    }

    public static boolean informationLossShortToByte(Short value) {
        return (value.shortValue() > Byte.MAX_VALUE) || (value.shortValue() < Byte.MIN_VALUE);
    }

    public static boolean informationLossShortToChar(Short value) {
        return (value.shortValue() > Character.MAX_VALUE) || (value.shortValue() < Character.MIN_VALUE);
    }

    public static boolean informationLossShortToInt(Short value) {
        return false;
    }

    public static boolean informationLossShortToLong(Short value) {
        return false;
    }

    public static boolean informationLossShortToFloat(Short value) {
        return false;
    }

    public static boolean informationLossShortToDouble(Short value) {
        return false;
    }

    public static boolean informationLossCharToChar(Character value) {
        return false;
    }

    public static boolean informationLossCharToByte(Character value) {
        return (value.charValue() > Byte.MAX_VALUE) || (value.charValue() < Byte.MIN_VALUE);
    }

    public static boolean informationLossCharToShort(Character value) {
        return (value.charValue() > Short.MAX_VALUE) || (value.charValue() < Short.MIN_VALUE);
    }

    public static boolean informationLossCharToInt(Character value) {
        return false;
    }

    public static boolean informationLossCharToLong(Character value) {
        return false;
    }

    public static boolean informationLossCharToFloat(Character value) {
        return false;
    }

    public static boolean informationLossCharToDouble(Character value) {
        return false;
    }

    public static boolean informationLossIntToInt(Integer value) {
        return false;
    }

    public static boolean informationLossIntToByte(Integer value) {
        return (value.intValue() > Byte.MAX_VALUE) || (value.intValue() < Byte.MIN_VALUE);
    }

    public static boolean informationLossIntToShort(Integer value) {
        return (value.intValue() > Short.MAX_VALUE) || (value.intValue() < Short.MIN_VALUE);
    }

    public static boolean informationLossIntToChar(Integer value) {
        return (value.intValue() > Character.MAX_VALUE) || (value.intValue() < Character.MIN_VALUE);
    }

    public static boolean informationLossIntToLong(Integer value) {
        return false;
    }

    public static boolean informationLossIntToFloat(Integer value) {
        return false;
    }

    public static boolean informationLossIntToDouble(Integer value) {
        return false;
    }

    public static boolean informationLossLongToLong(Long value) {
        return false;
    }

    public static boolean informationLossLongToByte(Long value) {
        return (value.longValue() > Byte.MAX_VALUE) || (value.longValue() < Byte.MIN_VALUE);
    }

    public static boolean informationLossLongToShort(Long value) {
        return (value.longValue() > Short.MAX_VALUE) || (value.longValue() < Short.MIN_VALUE);
    }

    public static boolean informationLossLongToChar(Long value) {
        return (value.longValue() > Character.MAX_VALUE) || (value.longValue() < Character.MIN_VALUE);
    }

    public static boolean informationLossLongToInt(Long value) {
        return (value.longValue() > Integer.MAX_VALUE) || (value.longValue() < Integer.MIN_VALUE);
    }

    public static boolean informationLossLongToFloat(Long value) {
        return false;
    }

    public static boolean informationLossLongToDouble(Long value) {
        return false;
    }

    public static boolean informationLossFloatToFloat(Float value) {
        return false;
    }

    public static boolean informationLossFloatToByte(Float value) {
        if (value.isInfinite())
            return true;
        if (value.isNaN())
            return true;
        return (value.floatValue() > Byte.MAX_VALUE) || (value.floatValue() < Byte.MIN_VALUE);
    }

    public static boolean informationLossFloatToShort(Float value) {
        if (value.isInfinite())
            return true;
        if (value.isNaN())
            return true;
        return (value.floatValue() > Short.MAX_VALUE) || (value.floatValue() < Short.MIN_VALUE);
    }

    public static boolean informationLossFloatToChar(Float value) {
        if (value.isInfinite())
            return true;
        if (value.isNaN())
            return true;
        return (value.floatValue() > Character.MAX_VALUE) || (value.floatValue() < Character.MIN_VALUE);
    }

    public static boolean informationLossFloatToInt(Float value) {
        if (value.isInfinite())
            return true;
        if (value.isNaN())
            return true;
        return (value.floatValue() > Integer.MAX_VALUE) || (value.floatValue() < Integer.MIN_VALUE) || ((int) value.floatValue() != value.floatValue());
    }

    public static boolean informationLossFloatToLong(Float value) {
        if (value.isInfinite())
            return true;
        if (value.isNaN())
            return true;
        return (value.floatValue() > Long.MAX_VALUE) || (value.floatValue() < Long.MIN_VALUE) || ((long) value.floatValue() != value.floatValue());
    }

    public static boolean informationLossFloatToDouble(Float value) {
        return false;
    }

    public static boolean informationLossDoubleToDouble(Double value) {
        return false;
    }

    public static boolean informationLossDoubleToByte(Double value) {
        if (value.isInfinite())
            return true;
        if (value.isNaN())
            return true;
        return (value.doubleValue() > Byte.MAX_VALUE) || (value.doubleValue() < Byte.MIN_VALUE);
    }

    public static boolean informationLossDoubleToShort(Double value) {
        if (value.isInfinite())
            return true;
        if (value.isNaN())
            return true;
        return (value.doubleValue() > Short.MAX_VALUE) || (value.doubleValue() < Short.MIN_VALUE);
    }

    public static boolean informationLossDoubleToChar(Double value) {
        if (value.isInfinite())
            return true;
        if (value.isNaN())
            return true;
        return (value.doubleValue() > Character.MAX_VALUE) || (value.doubleValue() < Character.MIN_VALUE);
    }

    public static boolean informationLossDoubleToInt(Double value) {
        if (value.isInfinite())
            return true;
        if (value.isNaN())
            return true;
        return (value.doubleValue() > Integer.MAX_VALUE) || (value.doubleValue() < Integer.MIN_VALUE);
    }

    public static boolean informationLossDoubleToLong(Double value) {
        if (value.isInfinite())
            return true;
        if (value.isNaN())
            return true;
        return (value.doubleValue() > Long.MAX_VALUE) || (value.doubleValue() < Long.MIN_VALUE) || ((long) value.doubleValue() != value.doubleValue());
    }

    public static boolean informationLossDoubleToFloat(Double value) {
        if (value.isInfinite())
            return false;
        if (value.isNaN())
            return false;
        float f = (float) value.doubleValue();
        return f != value.doubleValue();
    }
}
