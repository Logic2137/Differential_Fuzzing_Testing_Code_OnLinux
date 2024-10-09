

 
class InferWeak {
    private void test() {
        GroupLayout l = new GroupLayout();
        l.setHorizontalGroup(
            l.createParallelGroup().addGroup(l.createParallelGroup().addGroup(
            l.createParallelGroup().addGroup(l.createParallelGroup().addGroup(
            l.createParallelGroup().addGroup(l.createParallelGroup().addGroup(
            l.createParallelGroup().addGroup(l.createParallelGroup().addGroup(
            l.createParallelGroup().addGroup(l.createParallelGroup().addGroup(
            l.createParallelGroup().addGroup(l.createParallelGroup().addGroup(
            l.createParallelGroup().addGroup(l.createParallelGroup().addGroup(
            l.createParallelGroup().addGroup(l.createParallelGroup().addGroup(
            l.createParallelGroup().addGroup(l.createParallelGroup().addGroup(
            l.createParallelGroup().addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object())).addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object())).addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object())).addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object())).addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object())).addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object())).addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object())).addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object())).addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object())).addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object())).addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object())).addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object())).addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object())).addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object())).addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object())).addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object())).addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object())).addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object())).addGap(1).addComponent(new Object()).addGap(1)
            .addComponent(new Object()));
    }

    static class GroupLayout {
        <T extends ParallelGroup> T createParallelGroup() {return null;}
        <T extends ParallelGroup> T createParallelGroup(int i) {return null;}
        <T extends ParallelGroup> T createParallelGroup(int i, int j) {return null;}
        void setHorizontalGroup(ParallelGroup g) { }
    }

    static class ParallelGroup {
        <T extends ParallelGroup> T addGroup(ParallelGroup g) { return null; }
        <T extends ParallelGroup> T addGroup(int i, ParallelGroup g) { return null; }
        <T extends ParallelGroup> T addGap(int i) { return null; }
        <T extends ParallelGroup> T addGap(int i, int j) { return null; }
        <T extends ParallelGroup> T addComponent(Object c) { return null; }
        <T extends ParallelGroup> T addComponent(int i, Object c) { return null; }
    }
 }
