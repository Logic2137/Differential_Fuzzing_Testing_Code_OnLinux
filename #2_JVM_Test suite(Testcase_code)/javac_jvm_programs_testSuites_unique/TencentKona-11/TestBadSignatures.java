



import java.lang.reflect.*;
import sun.reflect.generics.parser.SignatureParser;

public class TestBadSignatures {
    public static void main(String[] args) {
        String[] badSignatures = {
            
            "<T:Lfoo/tools/nsc/symtab/Names;Lfoo/tools/nsc/symtab/Symbols;",

            
            "<E:Ljava/lang/Exception;>(TE;[Ljava/lang/RuntimeException;)V^[TE;",
        };

        for(String badSig : badSignatures) {
            try {
                SignatureParser.make().parseMethodSig(badSig);
                throw new RuntimeException("Expected GenericSignatureFormatError for " +
                                           badSig);
            } catch(GenericSignatureFormatError gsfe) {
                System.out.println(gsfe.toString()); 
            }
        }
    }
}
