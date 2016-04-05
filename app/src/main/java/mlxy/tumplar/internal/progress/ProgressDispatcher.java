package mlxy.tumplar.internal.progress;

import java.util.HashMap;

public class ProgressDispatcher implements ResponseReadingProgressListener {
    private static final HashMap<String, ProgressListener> LISTENERS = new HashMap<>();

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
}
