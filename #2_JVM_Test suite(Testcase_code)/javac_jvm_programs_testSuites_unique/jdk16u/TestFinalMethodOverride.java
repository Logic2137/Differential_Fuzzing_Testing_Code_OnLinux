

















public class TestFinalMethodOverride {

  public static class Super {
    private final void theMethod() {}
  }

  public static class Inner extends Super {
    
    public void theMethod() {}
  }

  public static void main(String[] args) {
    Inner i = new Inner();
  }
}

