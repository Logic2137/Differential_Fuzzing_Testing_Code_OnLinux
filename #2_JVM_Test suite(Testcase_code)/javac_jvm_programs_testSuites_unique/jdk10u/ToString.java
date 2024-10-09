

import javax.sound.sampled.CompoundControl;
import javax.sound.sampled.Control;


public class ToString {
    public static void main(String args[]) throws Exception {
        System.out.println();
        System.out.println();
        System.out.println("4629190: CompoundControl: getMemberControls() and toString() throw NullPointerException");

        String firstControlTypeName = "first_Control_Type_Name";
        String secondControlTypeName = "second_Control_Type_Name";
        String thirdControlTypeName = "third_Control_Type_Name";

        Control.Type firstControlType = new TestControlType(firstControlTypeName);
        Control.Type secondControlType = new TestControlType(secondControlTypeName);
        Control.Type thirdControlType = new TestControlType(thirdControlTypeName);

        Control firstControl = new TestControl(firstControlType);
        Control secondControl = new TestControl(secondControlType);
        Control thirdControl = new TestControl(thirdControlType);

        String testCompoundControlTypeName = "CompoundControl_Type_Name";
        CompoundControl.Type testCompoundControlType
            = new TestCompoundControlType(testCompoundControlTypeName);

        Control[] setControls = { firstControl, secondControl, thirdControl };
        CompoundControl testedCompoundControl
            = new TestCompoundControl(testCompoundControlType, setControls);

        
        Control[] producedControls = testedCompoundControl.getMemberControls();
        System.out.println("Got "+producedControls.length+" member controls.");

        
        String producedString = testedCompoundControl.toString();
        System.out.println("toString() returned: "+producedString);

        System.out.println("Test passed.");
    }

}

class TestControl extends Control {

    TestControl(Control.Type type) {
        super(type);
    }
}

class TestControlType extends Control.Type {

    TestControlType(String name) {
        super(name);
    }
}

class TestCompoundControl extends CompoundControl {

    TestCompoundControl(CompoundControl.Type type, Control[] memberControls) {
        super(type, memberControls);
    }
}

class TestCompoundControlType extends CompoundControl.Type {

    TestCompoundControlType(String name) {
        super(name);
    }
}
