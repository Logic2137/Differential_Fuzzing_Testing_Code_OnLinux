



class BreakComplexValueNoSwitchExpressions {
    void t() {
        while (true) {
            yield(1 + 1);
        }
    }
    private void yield(int i) {}
}
