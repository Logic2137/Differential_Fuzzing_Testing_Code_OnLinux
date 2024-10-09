

import javax.swing.UIDefaults;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.metal.MetalLookAndFeel;


public class Test8039750 {
    public static void main(String[] args) {
        UIDefaults table= new MetalLookAndFeel().getDefaults();
        test(table.get("ToolBar.rolloverBorder"),
                "javax.swing.plaf.metal.MetalBorders$ButtonBorder",
                "javax.swing.plaf.metal.MetalBorders$RolloverMarginBorder");
        test(table.get("ToolBar.nonrolloverBorder"),
                "javax.swing.plaf.metal.MetalBorders$ButtonBorder",
                "javax.swing.plaf.metal.MetalBorders$RolloverMarginBorder");
        test(table.get("RootPane.frameBorder"),
                "javax.swing.plaf.metal.MetalBorders$FrameBorder");
        test(table.get("RootPane.plainDialogBorder"),
                "javax.swing.plaf.metal.MetalBorders$DialogBorder");
        test(table.get("RootPane.informationDialogBorder"),
                "javax.swing.plaf.metal.MetalBorders$DialogBorder");
        test(table.get("RootPane.errorDialogBorder"),
                "javax.swing.plaf.metal.MetalBorders$ErrorDialogBorder");
        test(table.get("RootPane.colorChooserDialogBorder"),
                "javax.swing.plaf.metal.MetalBorders$QuestionDialogBorder");
        test(table.get("RootPane.fileChooserDialogBorder"),
                "javax.swing.plaf.metal.MetalBorders$QuestionDialogBorder");
        test(table.get("RootPane.questionDialogBorder"),
                "javax.swing.plaf.metal.MetalBorders$QuestionDialogBorder");
        test(table.get("RootPane.warningDialogBorder"),
                "javax.swing.plaf.metal.MetalBorders$WarningDialogBorder");
    }

    private static void test(Object value, String name) {
        if (!value.getClass().getName().equals(name)) {
            throw new Error(name);
        }
    }

    private static void test(Object value, String one, String two) {
        if (value instanceof CompoundBorder) {
            CompoundBorder border = (CompoundBorder) value;
            test(border.getOutsideBorder(), one);
            test(border.getInsideBorder(), two);
        } else {
            throw new Error("CompoundBorder");
        }
    }
}
