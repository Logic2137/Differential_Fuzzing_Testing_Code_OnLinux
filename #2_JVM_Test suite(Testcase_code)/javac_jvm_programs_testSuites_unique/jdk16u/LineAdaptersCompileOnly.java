

import java.util.concurrent.Flow;
import java.util.function.Function;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse.BodySubscriber;
import java.net.http.HttpResponse.BodySubscribers;
import static java.util.function.Function.identity;
import static java.nio.charset.StandardCharsets.*;



public class LineAdaptersCompileOnly {

    public static void main(String[] args) {
        makesSureDifferentGenericSignaturesCompile();
    }

    static void makesSureDifferentGenericSignaturesCompile() {
        BodyHandlers.fromLineSubscriber(new StringSubscriber());
        BodyHandlers.fromLineSubscriber(new CharSequenceSubscriber());
        BodyHandlers.fromLineSubscriber(new ObjectSubscriber());

        BodySubscribers.fromLineSubscriber(new StringSubscriber());
        BodySubscribers.fromLineSubscriber(new CharSequenceSubscriber());
        BodySubscribers.fromLineSubscriber(new ObjectSubscriber());

        BodyHandlers.fromLineSubscriber(new StringSubscriber(),       identity(), "\n");
        BodyHandlers.fromLineSubscriber(new CharSequenceSubscriber(), identity(), "\r\n");
        BodyHandlers.fromLineSubscriber(new ObjectSubscriber(),       identity(), "\n");
        BodyHandlers.fromLineSubscriber(new StringSubscriber(),       identity(), null);
        BodyHandlers.fromLineSubscriber(new CharSequenceSubscriber(), identity(), null);
        BodyHandlers.fromLineSubscriber(new ObjectSubscriber(),       identity(), null);

        BodySubscribers.fromLineSubscriber(new StringSubscriber(),       identity(), UTF_8,    "\n");
        BodySubscribers.fromLineSubscriber(new CharSequenceSubscriber(), identity(), UTF_16,   "\r\n");
        BodySubscribers.fromLineSubscriber(new ObjectSubscriber(),       identity(), US_ASCII, "\n");
        BodySubscribers.fromLineSubscriber(new StringSubscriber(),       identity(), UTF_8,    null);
        BodySubscribers.fromLineSubscriber(new CharSequenceSubscriber(), identity(), UTF_16,   null);
        BodySubscribers.fromLineSubscriber(new ObjectSubscriber(),       identity(), US_ASCII, null);
    }

    static class StringSubscriber implements Flow.Subscriber<String> {
        @Override public void onSubscribe(Flow.Subscription subscription) { }
        @Override public void onNext(String item) { }
        @Override public void onError(Throwable throwable) { }
        @Override public void onComplete() { }
    }

    static class CharSequenceSubscriber implements Flow.Subscriber<CharSequence> {
        @Override public void onSubscribe(Flow.Subscription subscription) { }
        @Override public void onNext(CharSequence item) { }
        @Override public void onError(Throwable throwable) { }
        @Override public void onComplete() { }
    }

    static class ObjectSubscriber implements Flow.Subscriber<Object> {
        @Override public void onSubscribe(Flow.Subscription subscription) { }
        @Override public void onNext(Object item) { }
        @Override public void onError(Throwable throwable) { }
        @Override public void onComplete() { }
    }

    

    static final Function<StringSubscriber,Integer> f1 = subscriber -> 1;
    static final Function<StringSubscriber,Number> f2 = subscriber -> 2;
    static final Function<StringSubscriberX,Integer> f3 = subscriber -> 3;
    static final Function<StringSubscriberX,Number> f4 = subscriber -> 4;

    static class StringSubscriberX extends StringSubscriber {
        int getIntegerX() { return 5; }
    }

    static void makesSureDifferentGenericFunctionSignaturesCompile() {
        BodyHandler<Integer> bh01 = BodyHandlers.fromLineSubscriber(new StringSubscriber(), s -> 6, "\n");
        BodyHandler<Number>  bh02 = BodyHandlers.fromLineSubscriber(new StringSubscriber(), s -> 7, "\n");
        BodyHandler<Integer> bh03 = BodyHandlers.fromLineSubscriber(new StringSubscriber(), f1, "\n");
        BodyHandler<Number>  bh04 = BodyHandlers.fromLineSubscriber(new StringSubscriber(), f1, "\n");
        BodyHandler<Number>  bh05 = BodyHandlers.fromLineSubscriber(new StringSubscriber(), f2, "\n");
        BodyHandler<Integer> bh06 = BodyHandlers.fromLineSubscriber(new StringSubscriberX(), f1, "\n");
        BodyHandler<Number>  bh07 = BodyHandlers.fromLineSubscriber(new StringSubscriberX(), f1, "\n");
        BodyHandler<Number>  bh08 = BodyHandlers.fromLineSubscriber(new StringSubscriberX(), f2, "\n");
        BodyHandler<Integer> bh09 = BodyHandlers.fromLineSubscriber(new StringSubscriberX(), StringSubscriberX::getIntegerX, "\n");
        BodyHandler<Number>  bh10 = BodyHandlers.fromLineSubscriber(new StringSubscriberX(), StringSubscriberX::getIntegerX, "\n");
        BodyHandler<Integer> bh11 = BodyHandlers.fromLineSubscriber(new StringSubscriberX(), f3, "\n");
        BodyHandler<Number>  bh12 = BodyHandlers.fromLineSubscriber(new StringSubscriberX(), f3, "\n");
        BodyHandler<Number>  bh13 = BodyHandlers.fromLineSubscriber(new StringSubscriberX(), f4, "\n");

        BodySubscriber<Integer> bs01 = BodySubscribers.fromLineSubscriber(new StringSubscriber(), s -> 6, UTF_8, "\n");
        BodySubscriber<Number>  bs02 = BodySubscribers.fromLineSubscriber(new StringSubscriber(), s -> 7, UTF_8, "\n");
        BodySubscriber<Integer> bs03 = BodySubscribers.fromLineSubscriber(new StringSubscriber(), f1, UTF_8, "\n");
        BodySubscriber<Number>  bs04 = BodySubscribers.fromLineSubscriber(new StringSubscriber(), f1, UTF_8, "\n");
        BodySubscriber<Number>  bs05 = BodySubscribers.fromLineSubscriber(new StringSubscriber(), f2, UTF_8, "\n");
        BodySubscriber<Integer> bs06 = BodySubscribers.fromLineSubscriber(new StringSubscriberX(), f1, UTF_8, "\n");
        BodySubscriber<Number>  bs07 = BodySubscribers.fromLineSubscriber(new StringSubscriberX(), f1, UTF_8, "\n");
        BodySubscriber<Number>  bs08 = BodySubscribers.fromLineSubscriber(new StringSubscriberX(), f2, UTF_8, "\n");
        BodySubscriber<Integer> bs09 = BodySubscribers.fromLineSubscriber(new StringSubscriberX(), StringSubscriberX::getIntegerX, UTF_8, "\n");
        BodySubscriber<Number>  bs10 = BodySubscribers.fromLineSubscriber(new StringSubscriberX(), StringSubscriberX::getIntegerX, UTF_8, "\n");
        BodySubscriber<Integer> bs11 = BodySubscribers.fromLineSubscriber(new StringSubscriberX(), f3, UTF_8, "\n");
        BodySubscriber<Number>  bs12 = BodySubscribers.fromLineSubscriber(new StringSubscriberX(), f3, UTF_8, "\n");
        BodySubscriber<Number>  bs13 = BodySubscribers.fromLineSubscriber(new StringSubscriberX(), f4, UTF_8, "\n");
    }
}
