

package jdk.nashorn.internal.performance;

import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptRuntime;

@SuppressWarnings("javadoc")
public class PerformanceWrapper extends jdk.nashorn.tools.Shell {

    int _numberOfIterations;
    int _runsPerIteration;

    protected void runCompileOnlyTest(final String name, final int numberOfIterations, final int runsPerIteration, final String testURL) throws Throwable {
        final String[] args = { name, "--compile-only=true", "-dump-on-error", "--", testURL };

        final long[] times = new long[numberOfIterations + 1];
        times[0] = System.nanoTime(); 

        for (int iteration = 1; iteration <= numberOfIterations; iteration++) {
            for (int i = 0; i < runsPerIteration; i++) {
                run(System.in, System.out, System.err, args);
            }
            times[iteration] = System.nanoTime();
        }

        for (int i = 0; i < numberOfIterations; i++) {
            System.out.println("Iteration " + (i + 1) + " average time: " + ((times[i + 1] - times[i]) / (float)runsPerIteration) / 1000000.0 + " ms.");
        }
    }

    protected void runExecuteOnlyTest(final String name, final int numberOfIterations, final int runsPerIteration, final String testURL) throws Throwable {
        runExecuteOnlyTest(name, numberOfIterations, runsPerIteration, testURL, System.out, System.err, new String[0]);
    }

    protected void runExecuteOnlyTest(final String name, final int numberOfIterations, final int runsPerIteration, final String testURL, final OutputStream out, final OutputStream err) throws Throwable {
        runExecuteOnlyTest(name, numberOfIterations, runsPerIteration, testURL, out, err, new String[0]);
    }


    protected void runExecuteOnlyTest(final String name, final int numberOfIterations, final int runsPerIteration, final String testURL, final OutputStream out, final OutputStream err, final String[] newargs) throws Throwable {
        final String[] args=new String[newargs.length+1];
        System.arraycopy(newargs, 0, args, 1, newargs.length);
        args[0]=name;




        _numberOfIterations = numberOfIterations;
        _runsPerIteration = runsPerIteration;
        run(System.in, out, err, args);

    }

    @Override
    protected Object apply(final ScriptFunction target, final Object self) {
        if (_runsPerIteration == 0 && _numberOfIterations == 0) {
            final Global global = jdk.nashorn.internal.runtime.Context.getGlobal();
            final ScriptFunction _target = target;
            final Object _self = self;

            class MyThread implements Callable<Object> {
                @Override
                public Object call() {
                    Context.setGlobal(global);
                    
                    final Object scriptRuntimeApplyResult = ScriptRuntime.apply(_target, _self);
                    return scriptRuntimeApplyResult;
                }
            }

            final java.util.concurrent.ThreadPoolExecutor executor = new java.util.concurrent.ThreadPoolExecutor(1, 1, 1, TimeUnit.MINUTES, new java.util.concurrent.ArrayBlockingQueue<Runnable>(10));
            final MyThread myThread = new MyThread();
            
            Object result;
            Future<?> futureResult = null;

            try {
                futureResult = executor.submit(myThread);
                final String timeout = System.getProperty("timeout.value");
                int tmout = 0;
                if (timeout != null) {
                    try {
                        tmout = Integer.parseInt(timeout);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
                if (tmout != 0) {
                    result = futureResult.get(10, TimeUnit.MINUTES);
                } else {
                    result = futureResult.get();
                }
            } catch (final InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            } catch (final TimeoutException e) {
                System.out.println("timeout while script execution");
                futureResult.cancel(true);
                return null;
            }

            return result;
        }

        final long[] times = new long[_numberOfIterations + 1];
        times[0] = System.nanoTime();
        for (int iteration = 1; iteration <= _numberOfIterations; iteration++) {
            for (int i = 0; i < _runsPerIteration; i++) {
                
            }
            times[iteration] = System.nanoTime();
        }

        for (int i = 0; i < _numberOfIterations; i++) {
            System.out.println("Iteration " + (i + 1) + " average time: " + ((times[i + 1] - times[i]) / (float)_runsPerIteration) / 1000000.0 + " ms.");
        }

        return ScriptRuntime.apply(target, self);
    }
}
