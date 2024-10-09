import java.util.function.Function;

public class LambdaDeduplicate {

    Function<Integer, Integer> f1 = x -> x;

    Function<Integer, Integer> f2 = x -> x;
}
