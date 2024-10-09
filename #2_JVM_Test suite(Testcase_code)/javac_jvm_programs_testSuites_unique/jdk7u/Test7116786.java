





class Case {
    private String caseName;    
    private String file;        
    private String location;    
    private String description; 
    private String message;     

    private boolean testable;   

    public Case(String caseName, String file, boolean testable,
                String location, String description, String message) {
        this.caseName = caseName;
        this.file = file;
        this.testable = testable;
        this.location = location;
        this.description = description;
        this.message = message;
    }

    String getCaseName() { return this.caseName; }
    String getFile() { return this.file; }
    String getLocation() { return this.location; }
    String getDescription() { return this.description; }
    String getMessage() { return this.message; }

    boolean isTestable() { return this.testable; }
}


class VerifyErrorCases {
    public static final Case[] cases = {

        new Case("case00", "stackMapFrame.cpp", true, "pop_stack_ex",
                 "stack underflow",
                 "Operand stack underflow"),

        new Case("case01", "stackMapFrame.cpp", true, "pop_stack_ex",
                 "stack pop not assignable to expected",
                 "Bad type on operand stack"),

        new Case("case02", "stackMapFrame.cpp", true, "get_local",
                 "local index out-of-bounds",
                 "Local variable table overflow"),

        new Case("case03", "stackMapFrame.cpp", true, "get_local",
                 "local not assignable to expected",
                 "Bad local variable type"),

        new Case("case04", "stackMapFrame.cpp", true, "get_local_2",
                 "local index out-of-bounds [type2]",
                 "get long/double overflows locals"),

        new Case("case05", "stackMapFrame.cpp", true, "get_local_2",
                 "local not assignabled to expected [type2]",
                 "Bad local variable type"),

        
        new Case("case06", "stackMapFrame.cpp", false, "get_local_2",
                 "local second-word not assignabled to expected",
                 "Bad local variable type"),

        new Case("case07", "stackMapFrame.cpp", true, "set_local",
                 "local index out-of-bounds",
                 "Local variable table overflow"),

        new Case("case08", "stackMapFrame.cpp", true, "set_local_2",
                 "local index out-of-bounds [type2]",
                 "Local variable table overflow"),

        new Case("case09", "stackMapFrame.hpp", true, "push_stack",
                 "stack overflow",
                 "Operand stack overflow"),

        new Case("case10", "stackMapFrame.hpp", true, "push_stack_2",
                 "stack overflow [type2]",
                 "Operand stack overflow"),

        new Case("case11", "stackMapFrame.hpp", true, "pop_stack",
                 "stack underflow",
                 "Operand stack underflow"),

        new Case("case12", "stackMapTable.cpp", true, "StackMapTable ctor",
                 "stackmap offset beyond code size",
                 "StackMapTable error: bad offset"),

        new Case("case13", "stackMapTable.cpp", true, "match_stackmap",
                 "no stackmap frame at expected location",
                 "Expecting a stackmap frame at branch target "),

        new Case("case14", "stackMapTable.cpp", true, "check_jump_target",
                 "no stackmap frame at jump location or bad jump",
                 "Inconsistent stackmap frames at branch target "),

        new Case("case15", "stackMapTable.cpp", true, "check_new_object",
                 "backward jump with uninit",
                 "Uninitialized object exists on backward branch "),

        
        new Case("case16", "verifier.cpp", false, "loop header",
                 "bad op in wide instruction",
                 "Bad wide instruction"),

        new Case("case17", "verifier.cpp", true, "case iaload",
                 "TOS not X array",
                 "Bad type on operand stack in iaload"),

        new Case("case18", "verifier.cpp", true, "case baload",
                 "TOS not X array",
                 "Bad type on operand stack in baload"),

        new Case("case19", "verifier.cpp", true, "case caload",
                 "TOS not X array",
                 "Bad type on operand stack in caload"),

        new Case("case20", "verifier.cpp", true, "case saload",
                 "TOS not X array",
                 "Bad type on operand stack in saload"),

        new Case("case21", "verifier.cpp", true, "case laload",
                 "TOS not X array",
                 "Bad type on operand stack in laload"),

        new Case("case22", "verifier.cpp", true, "case faload",
                 "TOS not X array",
                 "Bad type on operand stack in faload"),

        new Case("case23", "verifier.cpp", true, "case daload",
                 "TOS not X array",
                 "Bad type on operand stack in daload"),

        new Case("case24", "verifier.cpp", true, "case aaload",
                 "TOS not X array",
                 "Bad type on operand stack in aaload"),

        new Case("case25", "verifier.cpp", true, "case iastore",
                 "TOS not int array",
                 "Bad type on operand stack in iastore"),

        new Case("case26", "verifier.cpp", true, "case bastore",
                 "TOS not byte array",
                 "Bad type on operand stack in bastore"),

        new Case("case27", "verifier.cpp", true, "case castore",
                 "TOS not char array",
                 "Bad type on operand stack in castore"),

        new Case("case28", "verifier.cpp", true, "case sastore",
                 "TOS not short array",
                 "Bad type on operand stack in sastore"),

        new Case("case29", "verifier.cpp", true, "case lastore",
                 "TOS not long array",
                 "Bad type on operand stack in lastore"),

        new Case("case30", "verifier.cpp", true, "case fastore",
                 "TOS not float array",
                 "Bad type on operand stack in fastore"),

        new Case("case31", "verifier.cpp", true, "case dastore",
                 "TOS not double array",
                 "Bad type on operand stack in dastore"),

        new Case("case32", "verifier.cpp", true, "case aastore",
                 "TOS not object array",
                 "Bad type on operand stack in aastore"),

        
        new Case("case33", "verifier.cpp", false, "case pop2",
                 "TOS is category2_1st (would split)",
                 "Bad type on operand stack in pop2"),

        
        new Case("case34", "verifier.cpp", false, "case dup_x2",
                 "TOS-1 is category2_1st (would split)",
                 "Bad type on operand stack in dup_x2"),

        
        new Case("case35", "verifier.cpp", false, "case dup2",
                 "TOS-1 is category2_1st (would split)",
                 "Bad type on operand stack in dup2"),

        
        new Case("case36", "verifier.cpp", false, "case dup2_x1",
                 "TOS-1 is category2_1st (would split)",
                 "Bad type on operand stack in dup2_x1"),

        
        new Case("case37", "verifier.cpp", false, "case dup2_x2",
                 "TOS-1 is category2_1st (would split)",
                 "Bad type on operand stack in dup2_x2"),

        
        new Case("case38", "verifier.cpp", false, "case dup2_x2",
                 "TOS-3 is category2_1st (would split)",
                 "Bad type on operand stack in dup2_x2"),

        new Case("case39", "verifier.cpp", true, "case return",
                 "return type of method is not void",
                 "Method expects a return value"),

        new Case("case40", "verifier.cpp", true, "case return",
                 "return with uninitialized this ",
                 "Constructor must call super() or this() before return"),

        new Case("case41", "verifier.cpp", true, "case new",
                 "cp index not a class type",
                 "Illegal new instruction"),

        new Case("case42", "verifier.cpp", true, "case arraylength",
                 "TOS is not an array",
                 "Bad type on operand stack in arraylength"),

        new Case("case43", "verifier.cpp", true, "case multianewarray",
                 "CP index does not refer to array type",
                 "Illegal constant pool index in multianewarray instruction"),

        new Case("case44", "verifier.cpp", true, "case multianewarray",
                 "Bad dimension (<1) or does not match CP signature",
                 "Illegal dimension in multianewarray instruction: "),

        new Case("case45", "verifier.cpp", true, "case default",
                 "Unrecognized bytecode",
                 "Bad instruction: "),

        new Case("case46", "verifier.cpp", true, "loop end",
                 "control flow falls off method",
                 "Control flow falls through code end"),

        new Case("case47", "verifier.cpp", true, "generate_code_data",
                 "illegal bytecode via RawBytecodeStream (breakpoint)",
                 "Bad instruction"),

        new Case("case48", "verifier.cpp", true, "generate_code_data",
                 "illegal bytecode via RawBytecodeStream (other illegal)",
                 "Bad instruction"),

        new Case("case49", "verifier.cpp", true,
                 "verify_exception_handler_table",
                 "catch_type is not throwable",
                 "Catch type is not a subclass of Throwable in " +
                 "exception handler "),

        new Case("case50", "verifier.cpp", true, "verify_stackmap_table",
                 "missing a stack map frame @ target location (mid table)",
                 "Expecting a stack map frame"),

        new Case("case51", "verifier.cpp", true, "verify_stackmap_table",
                 "stack map does not match?",
                 "Instruction type does not match stack map"),

        new Case("case52", "verifier.cpp", true, "verify_stackmap_table",
                 "missing a stack map frame @ target location (end of table)",
                 "Expecting a stack map frame"),

        new Case("case53", "verifier.cpp", true,
                 "verify_exception_handler_targets",
                 "stackmap mismatch at exception handler",
                 "Stack map does not match the one at exception handler "),

        new Case("case54", "verifier.cpp", true, "verify_cp_index",
                 "constant pool index is out-of-bounds",
                 "Illegal constant pool index "),

        new Case("case55", "verifier.cpp", true, "verify_cp_type",
                 "constant pool entry is not expected type",
                 "Illegal type at constant pool entry "),

        new Case("case56", "verifier.cpp", true, "verify_cp_class_type",
                 "constant pool entry is not an object type",
                 "Illegal type at constant pool entry "),

        
        new Case("case57", "verifier.cpp", false, "verify_ldc",
                 "invalid constant pool index in ldc",
                 "Invalid index in ldc"),

        new Case("case58", "verifier.cpp", true, "verify_switch",
                 "bad switch padding",
                 "Nonzero padding byte in lookswitch or tableswitch"),

        new Case("case59", "verifier.cpp", true, "verify_switch",
                 "tableswitch low is greater than high",
                 "low must be less than or equal to high in tableswitch"),

        
        new Case("case60", "verifier.cpp", false, "verify_switch",
                 "high - low + 1 < 0 (overflow?)",
                 "too many keys in tableswitch"),

        
        new Case("case61", "verifier.cpp", false, "verify_switch",
                 "lookupswitch keys < 0",
                 "number of keys in lookupswitch less than 0"),

        new Case("case62", "verifier.cpp", true, "verify_switch",
                 "lookupswitch keys out-of-order",
                 "Bad lookupswitch instruction"),

        
        new Case("case63", "verifier.cpp", false, "verify_field_instructions",
                 "referenced class is not an CP object",
                 "Expecting reference to class in class "),

        new Case("case64", "verifier.cpp", true, "verify_field_instructions",
                 "TOS not assignable to field type in putfield",
                 "Bad type on operand stack in putfield"),

        new Case("case65", "verifier.cpp", true, "verify_field_instructions",
                 "TOS not assignable to class when accessing protected field",
                 "Bad access to protected data in getfield"),

        new Case("case66", "verifier.cpp", true, "verify_invoke_init",
                 "Uninit_this is not of the current type or it's supertype",
                 "Bad <init> method call"),

        
        new Case("case67", "verifier.cpp", false, "verify_invoke_init",
                 "Uninit type with bad new instruction index",
                 "Expecting new instruction"),

        new Case("case68", "verifier.cpp", true, "verify_invoke_init",
                 "calling other class's <init> method",
                 "Call to wrong <init> method"),

        new Case("case69", "verifier.cpp", true, "verify_invoke_init",
                 "Calling protected <init> and type unassignable from current",
                 "Bad access to protected <init> method"),

        new Case("case70", "verifier.cpp", true, "verify_invoke_init",
                 "TOS is not an uninitialized (or Uninit_this) type",
                 "Bad operand type when invoking <init>"),

        new Case("case71", "verifier.cpp", true, "verify_invoke_instructions",
                 "Arg count in instruction doesn't match signature",
                 "Inconsistent args count operand in invokeinterface"),

        new Case("case72", "verifier.cpp", true, "verify_invoke_instructions",
                 "Non-zero pad in invokeinterface",
                 "Fourth operand byte of invokeinterface must be zero"),

        new Case("case73", "verifier.cpp", true, "verify_invoke_instructions",
                 "Non-zero pad in invokedynamic",
                 "Third and fourth operand bytes of " +
                 "invokedynamic must be zero"),

        new Case("case74", "verifier.cpp", true, "verify_invoke_instructions",
                 "Non-invokespecial trying to invoke a '<' method",
                 "Illegal call to internal method"),

        new Case("case75", "verifier.cpp", true, "verify_invoke_instructions",
                 "invokespecial and current unassignable from referenced type",
                 "Bad invokespecial instruction: current class isn't " +
                 "assignable to reference class."),

        new Case("case76", "verifier.cpp", true, "verify_invoke_instructions",
                 "TOS not assignable to current when calling protected method",
                 "Bad access to protected data in invokevirtual"),

        
        new Case("case77", "verifier.cpp", false, "verify_invoke_instructions",
                 "<init> method is not void return",
                 "Return type must be void in <init> method"),

        new Case("case78", "verifier.cpp", true, "get_newarray_type",
                 "newarray type invalid",
                 "Illegal newarray instruction"),

        new Case("case79", "verifier.cpp", true, "verify_return_value",
                 "void return from method which has a return value",
                 "Method expects a return value"),

        new Case("case80", "verifier.cpp", true, "verify_return_value",
                 "TOS type does not match signature",
                 "Bad return type"),

        new Case("case81", "verifier.cpp", true, "verify_stackmap_table",
                 "stack map does not match (flags)",
                 "Instruction type does not match stack map")
    };
}

public class Test7116786 {
    public static void main(String argv[]) throws Exception {
        for (Case c : VerifyErrorCases.cases) {
            System.out.println("******** " + c.getCaseName() + " ********");
            if (c.isTestable()) {
                try {
                    ClassLoader cl = Test7116786.class.getClassLoader();
                    Class<?> cls = Class.forName(c.getCaseName(), true, cl);
                    throw new RuntimeException(
                        "++ FAIL: No verify error encountered");
                } catch (VerifyError ve) {
                    String message = c.getMessage();
                    String veMessage = ve.getMessage();
                    System.out.print(veMessage);
                    if (!veMessage.startsWith(message)) {
                        
                        
                        
                        System.out.println("++ FAIL? " +
                            "Message does not match what was expected: " +
                            message);
                        continue;
                    }
                    if (!veMessage.contains("Exception Details:") &&
                        !veMessage.contains("Reason:")) {
                        System.out.println("++ FAIL: No details found");
                        throw new RuntimeException("FAIL: No details found");
                    }
                    System.out.println("++ PASS");
                }
            } else {
               System.out.println("++ SKIPPED");
            }
        }
    }
}
