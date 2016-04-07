package mlxy.tumplar.internal.progress;

import java.util.HashMap;

import rx.Subscriber;

public class ProgressDispatcher {
    private static final HashMap<String, Subscriber<? super Progress>> SUBSCRIBERS = new HashMap<>();

    public static void subscribe(String url, Subscriber<? super Progress> subscriber) {
        SUBSCRIBERS.put(url, subscriber);
    }

    public static void unsubscribe(String url) {
        SUBSCRIBERS.remove(url);
    }

    public static Subscriber<? super Progress> getSubscriber(String url) {
        return SUBSCRIBERS.get(url);
    }
}
