



public class TestNestmateAttributes {
    public static void main(String args[]) throws Throwable {
        String[] badClasses = new String[] {
            "NestmateAttributeHolder$TwoNestHost",
            "NestmateAttributeHolder",
            "ConflictingAttributesInNestHost",
            "NestmateAttributeHolder$ConflictingAttributesInNestMember",
            "BadNestMembersLength",
            "BadNestMembersEntry",
            "NestmateAttributeHolder$BadNestHost",
            "NestmateAttributeHolder$BadNestHostLength",
        };

        String[] messages = new String[] {
            "Multiple NestHost attributes in class file",
            "Multiple NestMembers attributes in class file",
            "Conflicting NestMembers and NestHost attributes",
            "Conflicting NestHost and NestMembers attributes",
            "Wrong NestMembers attribute length",
            "Nest member class_info_index 9 has bad constant type",
            "Nest-host class_info_index 10 has bad constant type",
            "Wrong NestHost attribute length",
        };

        for (int i = 0; i < badClasses.length; i++ ) {
            try {
                Class c = Class.forName(badClasses[i]);
                throw new Error("Missing ClassFormatError: " + messages[i]);
            }
            catch (ClassFormatError expected) {
                if (!expected.getMessage().contains(messages[i]))
                   throw new Error("Wrong ClassFormatError message: \"" +
                                   expected.getMessage() + "\" does not contain \"" +
                                   messages[i] + "\"");
                System.out.println("OK - got expected exception: " + expected);
            }
        }
    }
}
