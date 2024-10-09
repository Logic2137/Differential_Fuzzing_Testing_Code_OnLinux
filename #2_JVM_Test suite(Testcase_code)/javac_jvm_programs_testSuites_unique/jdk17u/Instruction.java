
package vm.compiler.coverage.parentheses.share;

import java.util.Arrays;
import java.util.List;

public enum Instruction {

    ICONST_M1(2),
    ICONST_0(3),
    ICONST_1(4),
    ICONST_2(5),
    ICONST_3(6),
    ICONST_4(7),
    ICONST_5(8),
    DUP(89),
    IADD(96),
    ISUB(100),
    IMUL(104),
    IAND(126),
    IOR(128),
    IXOR(130),
    ISHL(120),
    ISHR(122),
    SWAP(95),
    INEG(116),
    NOP(0);

    public static final List<Instruction> stackUp = Arrays.asList(ICONST_M1, ICONST_0, ICONST_1, ICONST_2, ICONST_3, ICONST_4, ICONST_5, DUP);

    public static final List<Instruction> stackDown = Arrays.asList(IADD, ISUB, IMUL, IAND, IOR, IXOR, ISHL, ISHR);

    public static final List<Instruction> neutral = Arrays.asList(SWAP, INEG, NOP);

    public final int opCode;

    Instruction(int opCode) {
        this.opCode = opCode;
    }
}
