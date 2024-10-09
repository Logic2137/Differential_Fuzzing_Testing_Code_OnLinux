


class HostNoNestMember {
  class Member {
    private int value;
  }

  public int test() {
    Member m = new Member();
    return m.value;
  }
}
