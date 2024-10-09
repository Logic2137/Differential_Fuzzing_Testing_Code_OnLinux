class BreakComplexValueNoSwitchExpressions {

    void t() {
        while (true) {
            yield(1, 2);
        }
    }

    private void yield(int i, int j) {
    }
}
