class NotionalInterfaceNotBeingInducedTest {

    interface I {
    }

    interface J {

        void foo();
    }

    public void test() {
        Object o1 = (I & J) System::gc;
        Object o2 = (J) System::gc;
        Object o3 = (Object & J) System::gc;
        Object o4 = (Object & I & J) System::gc;
    }
}
