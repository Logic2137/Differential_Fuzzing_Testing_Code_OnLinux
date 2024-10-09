import java.lang.Class;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.MalformedParametersException;
import java.lang.ClassLoader;
import java.lang.ClassNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BadClassFiles {

    private int errors = 0;

    private static final byte[] EmptyName_bytes = { -54, -2, -70, -66, 0, 0, 0, 52, 0, 18, 10, 0, 3, 0, 15, 7, 0, 16, 7, 0, 17, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 1, 109, 1, 0, 5, 40, 73, 73, 41, 86, 1, 0, 16, 77, 101, 116, 104, 111, 100, 80, 97, 114, 97, 109, 101, 116, 101, 114, 115, 1, 0, 0, 1, 0, 1, 98, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 14, 69, 109, 112, 116, 121, 78, 97, 109, 101, 46, 106, 97, 118, 97, 12, 0, 4, 0, 5, 1, 0, 9, 69, 109, 112, 116, 121, 78, 97, 109, 101, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 0, 33, 0, 2, 0, 3, 0, 0, 0, 0, 0, 2, 0, 1, 0, 4, 0, 5, 0, 1, 0, 6, 0, 0, 0, 29, 0, 1, 0, 1, 0, 0, 0, 5, 42, -73, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 1, 0, 1, 0, 8, 0, 9, 0, 2, 0, 6, 0, 0, 0, 25, 0, 0, 0, 3, 0, 0, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 2, 0, 10, 0, 0, 0, 9, 2, 0, 11, 0, 0, 0, 12, 0, 0, 0, 1, 0, 13, 0, 0, 0, 2, 0, 14 };

    private static final byte[] BadModifiers_bytes = { -54, -2, -70, -66, 0, 0, 0, 52, 0, 18, 10, 0, 3, 0, 15, 7, 0, 16, 7, 0, 17, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 1, 109, 1, 0, 5, 40, 73, 73, 41, 86, 1, 0, 16, 77, 101, 116, 104, 111, 100, 80, 97, 114, 97, 109, 101, 116, 101, 114, 115, 1, 0, 1, 97, 1, 0, 1, 98, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 17, 66, 97, 100, 77, 111, 100, 105, 102, 105, 101, 114, 115, 46, 106, 97, 118, 97, 12, 0, 4, 0, 5, 1, 0, 12, 66, 97, 100, 77, 111, 100, 105, 102, 105, 101, 114, 115, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 0, 33, 0, 2, 0, 3, 0, 0, 0, 0, 0, 2, 0, 1, 0, 4, 0, 5, 0, 1, 0, 6, 0, 0, 0, 29, 0, 1, 0, 1, 0, 0, 0, 5, 42, -73, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 1, 0, 1, 0, 8, 0, 9, 0, 2, 0, 6, 0, 0, 0, 25, 0, 0, 0, 3, 0, 0, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 2, 0, 10, 0, 0, 0, 9, 2, 0, 11, 51, 51, 0, 12, 0, 0, 0, 1, 0, 13, 0, 0, 0, 2, 0, 14 };

    private static final byte[] BadNameIndex_bytes = { -54, -2, -70, -66, 0, 0, 0, 52, 0, 18, 10, 0, 3, 0, 15, 7, 0, 16, 7, 0, 17, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 1, 109, 1, 0, 5, 40, 73, 73, 41, 86, 1, 0, 16, 77, 101, 116, 104, 111, 100, 80, 97, 114, 97, 109, 101, 116, 101, 114, 115, 1, 0, 1, 97, 1, 0, 1, 98, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 17, 66, 97, 100, 78, 97, 109, 101, 73, 110, 100, 101, 120, 46, 106, 97, 118, 97, 12, 0, 4, 0, 5, 1, 0, 12, 66, 97, 100, 78, 97, 109, 101, 73, 110, 100, 101, 120, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 0, 33, 0, 2, 0, 3, 0, 0, 0, 0, 0, 2, 0, 1, 0, 4, 0, 5, 0, 1, 0, 6, 0, 0, 0, 29, 0, 1, 0, 1, 0, 0, 0, 5, 42, -73, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 1, 0, 1, 0, 8, 0, 9, 0, 2, 0, 6, 0, 0, 0, 25, 0, 0, 0, 3, 0, 0, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 2, 0, 10, 0, 0, 0, 9, 2, 0, 1, 0, 0, 0, 12, 0, 0, 0, 1, 0, 13, 0, 0, 0, 2, 0, 14 };

    private static final byte[] NameIndexOutOfBounds_bytes = { -54, -2, -70, -66, 0, 0, 0, 52, 0, 18, 10, 0, 3, 0, 15, 7, 0, 16, 7, 0, 17, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 1, 109, 1, 0, 5, 40, 73, 73, 41, 86, 1, 0, 16, 77, 101, 116, 104, 111, 100, 80, 97, 114, 97, 109, 101, 116, 101, 114, 115, 1, 0, 1, 97, 1, 0, 1, 98, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 25, 78, 97, 109, 101, 73, 110, 100, 101, 120, 79, 117, 116, 79, 102, 66, 111, 117, 110, 100, 115, 46, 106, 97, 118, 97, 12, 0, 4, 0, 5, 1, 0, 20, 78, 97, 109, 101, 73, 110, 100, 101, 120, 79, 117, 116, 79, 102, 66, 111, 117, 110, 100, 115, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 0, 33, 0, 2, 0, 3, 0, 0, 0, 0, 0, 2, 0, 1, 0, 4, 0, 5, 0, 1, 0, 6, 0, 0, 0, 29, 0, 1, 0, 1, 0, 0, 0, 5, 42, -73, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 1, 0, 1, 0, 8, 0, 9, 0, 2, 0, 6, 0, 0, 0, 25, 0, 0, 0, 3, 0, 0, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 2, 0, 10, 0, 0, 0, 9, 2, 0, -1, 0, 0, 0, 12, 0, 0, 0, 1, 0, 13, 0, 0, 0, 2, 0, 14 };

    private static final byte[] ExtraParams_bytes = { -54, -2, -70, -66, 0, 0, 0, 52, 0, 18, 10, 0, 3, 0, 15, 7, 0, 16, 7, 0, 17, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 1, 109, 1, 0, 5, 40, 73, 73, 41, 86, 1, 0, 16, 77, 101, 116, 104, 111, 100, 80, 97, 114, 97, 109, 101, 116, 101, 114, 115, 1, 0, 1, 97, 1, 0, 1, 98, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 16, 69, 120, 116, 114, 97, 80, 97, 114, 97, 109, 115, 46, 106, 97, 118, 97, 12, 0, 4, 0, 5, 1, 0, 11, 69, 120, 116, 114, 97, 80, 97, 114, 97, 109, 115, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 0, 33, 0, 2, 0, 3, 0, 0, 0, 0, 0, 2, 0, 1, 0, 4, 0, 5, 0, 1, 0, 6, 0, 0, 0, 29, 0, 1, 0, 1, 0, 0, 0, 5, 42, -73, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 1, 0, 1, 0, 8, 0, 9, 0, 2, 0, 6, 0, 0, 0, 25, 0, 0, 0, 3, 0, 0, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 2, 0, 10, 0, 0, 0, 13, 3, 0, 11, 0, 0, 0, 12, 0, 0, 0, 11, 0, 0, 0, 1, 0, 13, 0, 0, 0, 2, 0, 14 };

    private static final byte[] BadName1_bytes = { -54, -2, -70, -66, 0, 0, 0, 52, 0, 18, 10, 0, 3, 0, 15, 7, 0, 16, 7, 0, 17, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 1, 109, 1, 0, 5, 40, 73, 73, 41, 86, 1, 0, 16, 77, 101, 116, 104, 111, 100, 80, 97, 114, 97, 109, 101, 116, 101, 114, 115, 1, 0, 1, 46, 1, 0, 1, 98, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 13, 66, 97, 100, 78, 97, 109, 101, 49, 46, 106, 97, 118, 97, 12, 0, 4, 0, 5, 1, 0, 8, 66, 97, 100, 78, 97, 109, 101, 49, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 0, 33, 0, 2, 0, 3, 0, 0, 0, 0, 0, 2, 0, 1, 0, 4, 0, 5, 0, 1, 0, 6, 0, 0, 0, 29, 0, 1, 0, 1, 0, 0, 0, 5, 42, -73, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 1, 0, 1, 0, 8, 0, 9, 0, 2, 0, 6, 0, 0, 0, 25, 0, 0, 0, 3, 0, 0, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 2, 0, 10, 0, 0, 0, 9, 2, 0, 11, 0, 0, 0, 12, 0, 0, 0, 1, 0, 13, 0, 0, 0, 2, 0, 14 };

    private static final byte[] BadName2_bytes = { -54, -2, -70, -66, 0, 0, 0, 52, 0, 18, 10, 0, 3, 0, 15, 7, 0, 16, 7, 0, 17, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 1, 109, 1, 0, 5, 40, 73, 73, 41, 86, 1, 0, 16, 77, 101, 116, 104, 111, 100, 80, 97, 114, 97, 109, 101, 116, 101, 114, 115, 1, 0, 1, 91, 1, 0, 1, 98, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 13, 66, 97, 100, 78, 97, 109, 101, 50, 46, 106, 97, 118, 97, 12, 0, 4, 0, 5, 1, 0, 8, 66, 97, 100, 78, 97, 109, 101, 50, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 0, 33, 0, 2, 0, 3, 0, 0, 0, 0, 0, 2, 0, 1, 0, 4, 0, 5, 0, 1, 0, 6, 0, 0, 0, 29, 0, 1, 0, 1, 0, 0, 0, 5, 42, -73, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 1, 0, 1, 0, 8, 0, 9, 0, 2, 0, 6, 0, 0, 0, 25, 0, 0, 0, 3, 0, 0, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 2, 0, 10, 0, 0, 0, 9, 2, 0, 11, 0, 0, 0, 12, 0, 0, 0, 1, 0, 13, 0, 0, 0, 2, 0, 14 };

    private static final byte[] BadName3_bytes = { -54, -2, -70, -66, 0, 0, 0, 52, 0, 18, 10, 0, 3, 0, 15, 7, 0, 16, 7, 0, 17, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 1, 109, 1, 0, 5, 40, 73, 73, 41, 86, 1, 0, 16, 77, 101, 116, 104, 111, 100, 80, 97, 114, 97, 109, 101, 116, 101, 114, 115, 1, 0, 1, 59, 1, 0, 1, 98, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 13, 66, 97, 100, 78, 97, 109, 101, 51, 46, 106, 97, 118, 97, 12, 0, 4, 0, 5, 1, 0, 8, 66, 97, 100, 78, 97, 109, 101, 51, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 0, 33, 0, 2, 0, 3, 0, 0, 0, 0, 0, 2, 0, 1, 0, 4, 0, 5, 0, 1, 0, 6, 0, 0, 0, 29, 0, 1, 0, 1, 0, 0, 0, 5, 42, -73, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 1, 0, 1, 0, 8, 0, 9, 0, 2, 0, 6, 0, 0, 0, 25, 0, 0, 0, 3, 0, 0, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 2, 0, 10, 0, 0, 0, 9, 2, 0, 11, 0, 0, 0, 12, 0, 0, 0, 1, 0, 13, 0, 0, 0, 2, 0, 14 };

    private static final byte[] BadName4_bytes = { -54, -2, -70, -66, 0, 0, 0, 52, 0, 18, 10, 0, 3, 0, 15, 7, 0, 16, 7, 0, 17, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 1, 109, 1, 0, 5, 40, 73, 73, 41, 86, 1, 0, 16, 77, 101, 116, 104, 111, 100, 80, 97, 114, 97, 109, 101, 116, 101, 114, 115, 1, 0, 1, 47, 1, 0, 1, 98, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 13, 66, 97, 100, 78, 97, 109, 101, 52, 46, 106, 97, 118, 97, 12, 0, 4, 0, 5, 1, 0, 8, 66, 97, 100, 78, 97, 109, 101, 52, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 0, 33, 0, 2, 0, 3, 0, 0, 0, 0, 0, 2, 0, 1, 0, 4, 0, 5, 0, 1, 0, 6, 0, 0, 0, 29, 0, 1, 0, 1, 0, 0, 0, 5, 42, -73, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 1, 0, 1, 0, 8, 0, 9, 0, 2, 0, 6, 0, 0, 0, 25, 0, 0, 0, 3, 0, 0, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 2, 0, 10, 0, 0, 0, 9, 2, 0, 11, 0, 0, 0, 12, 0, 0, 0, 1, 0, 13, 0, 0, 0, 2, 0, 14 };

    private static final byte[] BadParams_bytes = { -54, -2, -70, -66, 0, 0, 0, 52, 0, 18, 10, 0, 3, 0, 15, 7, 0, 16, 7, 0, 17, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 1, 109, 1, 0, 5, 40, 73, 73, 41, 86, 1, 0, 16, 77, 101, 116, 104, 111, 100, 80, 97, 114, 97, 109, 101, 116, 101, 114, 115, 1, 0, 1, 97, 1, 0, 1, 98, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 14, 66, 97, 100, 80, 97, 114, 97, 109, 115, 46, 106, 97, 118, 97, 12, 0, 4, 0, 5, 1, 0, 9, 66, 97, 100, 80, 97, 114, 97, 109, 115, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 0, 33, 0, 2, 0, 3, 0, 0, 0, 0, 0, 2, 0, 1, 0, 4, 0, 5, 0, 1, 0, 6, 0, 0, 0, 29, 0, 1, 0, 1, 0, 0, 0, 5, 42, -73, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 1, 0, 1, 0, 8, 0, 9, 0, 2, 0, 6, 0, 0, 0, 25, 0, 0, 0, 3, 0, 0, 0, 1, -79, 0, 0, 0, 1, 0, 7, 0, 0, 0, 6, 0, 1, 0, 0, 0, 2, 0, 10, 0, 0, 0, 1, 0, 0, 1, 0, 13, 0, 0, 0, 2, 0, 14 };

    private static class InMemoryClassLoader extends ClassLoader {

        public Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }

    private static final InMemoryClassLoader loader = new InMemoryClassLoader();

    private final Class<?>[] classes;

    private BadClassFiles() throws ClassNotFoundException {
        classes = new Class<?>[] { loader.defineClass("EmptyName", EmptyName_bytes), loader.defineClass("BadModifiers", BadModifiers_bytes), loader.defineClass("BadNameIndex", BadNameIndex_bytes), loader.defineClass("NameIndexOutOfBounds", NameIndexOutOfBounds_bytes), loader.defineClass("ExtraParams", ExtraParams_bytes), loader.defineClass("BadParams", BadParams_bytes), loader.defineClass("BadName1", BadName1_bytes), loader.defineClass("BadName2", BadName2_bytes), loader.defineClass("BadName3", BadName3_bytes), loader.defineClass("BadName4", BadName4_bytes) };
    }

    public static void main(String... args) throws NoSuchMethodException, IOException, ClassNotFoundException {
        new BadClassFiles().run();
    }

    public void assertBadParameters(Class<?> cls) throws NoSuchMethodException {
        try {
            System.err.println("Trying " + cls);
            final Method method = cls.getMethod("m", int.class, int.class);
            final Parameter[] params = method.getParameters();
            System.err.println("Name " + params[0].getName());
            System.err.println("Did not see expected exception");
            errors++;
        } catch (MalformedParametersException e) {
            System.err.println("Expected exception seen");
        }
    }

    public void run() throws NoSuchMethodException {
        for (Class<?> cls : classes) assertBadParameters(cls);
        if (errors != 0)
            throw new RuntimeException(errors + " errors in test");
    }
}
