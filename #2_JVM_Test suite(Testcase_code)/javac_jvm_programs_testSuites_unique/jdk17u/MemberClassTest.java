class MemberClassTest {

    interface I {

        Long m();

        Long m(Long x, Long yx);
    }

    public class Member implements I {

        public class Member_Member {

            public Member_Member() {
            }

            public Member_Member(String x, String yx) {
            }
        }

        public Member() {
        }

        public Member(Long a, Long ba) {
        }

        public Long m() {
            return 0L;
        }

        public Long m(Long s, Long ts) {
            return 0L;
        }
    }

    static class Static_Member implements I {

        public class Static_Member_Member {

            public Static_Member_Member() {
            }

            public Static_Member_Member(String x, String yx) {
            }
        }

        public static class Static_Member_Static_Member {

            public Static_Member_Static_Member() {
            }

            public Static_Member_Static_Member(String x, String yx) {
            }
        }

        public Static_Member() {
        }

        public Static_Member(Long arg, Long barg) {
        }

        public Long m() {
            return 0L;
        }

        public Long m(Long s, Long ts) {
            return s + ts;
        }
    }

    public MemberClassTest() {
    }

    public MemberClassTest(final Long a, Long ba) {
    }

    public void foo() {
        new I() {

            class Anonymous_Member {

                public Anonymous_Member() {
                }

                public Anonymous_Member(String x, String yx) {
                }
            }

            public Long m() {
                return 0L;
            }

            public Long m(Long s, Long ts) {
                return s + ts;
            }
        }.m();
    }
}
