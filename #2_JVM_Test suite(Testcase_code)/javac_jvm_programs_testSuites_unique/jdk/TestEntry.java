





public class TestEntry {
    int testCaseId;
    boolean transformParent;
    boolean transformChild;
    boolean isParentExpectedShared;
    boolean isChildExpectedShared;

    public TestEntry(int testCaseId,
                     boolean transformParent, boolean transformChild,
                     boolean isParentExpectedShared, boolean isChildExpectedShared) {
        this.testCaseId = testCaseId;
        this.transformParent = transformParent;
        this.transformChild = transformChild;
        this.isParentExpectedShared = isParentExpectedShared;
        this.isChildExpectedShared = isChildExpectedShared;
    }
}

