

public class TestPostFieldModification {

  public String value;  

  public static void main(String[] args){

    System.out.println("Start threads");
    
    new Thread() {
      TestPostFieldModification test = new TestPostFieldModification();
      public void run() {
        test.value="test";
        for(int i = 0; i < 10; i++) {
          test.value += new String("_test");
        }
      }
    }.start();

    
    Thread d = new Thread() {
      public void run() {
        while(true) {
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {

          }
          System.gc();
        }
      }
    };
    d.setDaemon(true);
    d.start();
  }
}
