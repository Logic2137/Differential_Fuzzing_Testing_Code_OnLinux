

public class LambdaConstructTest {
   public static void main(String[] args) {

     System.out.println("=== LambdaConstructTest ===");

     
     Runnable lambda = () -> System.out.println("it's a Lambda world!");

     
     lambda.run();

  }
}

