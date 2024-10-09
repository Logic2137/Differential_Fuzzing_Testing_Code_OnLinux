




public class Host {

    private static int forNestmatesOnly = 1;

    public static class Member {
        
        
        static final Class<?> hostClass = Host.class;

        int id;

        
        
        public Member() {
            id = forNestmatesOnly++;
        }
    }
}
