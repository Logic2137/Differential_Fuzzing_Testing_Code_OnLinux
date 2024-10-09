



package compiler.loopopts;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestLogSum {
  public static void main(String[] args) {
    double sum;

    for (int i = 0; i < 6; i++) {
        for (int n = 2; n < 30; n++) {
           for (int j = 1; j <= n; j++) {
              for (int k = 1; k <= j; k++) {
                
                sum = computeSum(k, j);
              }
           }
        }
      }
   }

   private static Map<List<Integer>, Double> cache = new HashMap<List<Integer>, Double>();
   public static double computeSum(int x, int y) {
      List<Integer> key = Arrays.asList(new Integer[] {x, y});

      if (!cache.containsKey(key)) {

        
        LogSumArray toReturn = new LogSumArray(x);

        
        
        for (int z = 1; z < x+1; z++) {
           double logSummand = Math.log(z + x + y);
           toReturn.addLogSummand(logSummand);
        }

        
        cache.put(key, toReturn.retrieveLogSum());
      }
      return cache.get(key);
   }

   
   private static class LogSumArray {
      private double[] logSummandArray;
      private int currSize;

      private double maxLogSummand;

      public LogSumArray(int maxEntries) {
        this.logSummandArray = new double[maxEntries];

        this.currSize = 0;
        this.maxLogSummand = Double.NEGATIVE_INFINITY;
      }

      public void addLogSummand(double logSummand) {
        logSummandArray[currSize] = logSummand;
        currSize++;
        
        maxLogSummand = Math.max(maxLogSummand, logSummand);
      }

      public double retrieveLogSum() {
        if (maxLogSummand == Double.NEGATIVE_INFINITY) return Double.NEGATIVE_INFINITY;

        assert currSize <= logSummandArray.length;

        double factorSum = 0;
        for (int i = 0; i < currSize; i++) {
           factorSum += Math.exp(logSummandArray[i] - maxLogSummand);
        }

        return Math.log(factorSum) + maxLogSummand;
      }
   }
}
