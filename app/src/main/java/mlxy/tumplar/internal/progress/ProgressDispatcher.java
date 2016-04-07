package mlxy.tumplar.internal.progress;

import java.util.HashMap;

import rx.Observable;

public class ProgressDispatcher implements ResponseReadingProgressListener {
    private static final HashMap<String, ProgressListener> LISTENERS = new HashMap<>();

    public ProgressDispatcher() {}

    public static void addListener(String url, ProgressListener listener) {
        LISTENERS.put(url, listener);
    }

    public static void removeListener(String url) {
        LISTENERS.remove(url);
    }

    @Override
    public void onProgress(String url, long bytesRead, long totalBytes) {
        ProgressListener listener = LISTENERS.get(url);
        if (listener != null) {
            listener.onProgress(url, bytesRead, totalBytes);
        }
    }

    public static ProgressDispatcher create() {
        return new ProgressDispatcher();
    }

    private static final HashMap<String, Observable<Long>> OBSERVABLES = new HashMap<>();
    public static void pendingObservable(String url, Observable<Long> observable) {
        OBSERVABLES.put(url, observable);
    }

    public static Observable<Long> fetchObservable(String url) {
        return OBSERVABLES.get(url);
    }

    public static void removeObservable(String url) {
        OBSERVABLES.remove(url);
    }
}
