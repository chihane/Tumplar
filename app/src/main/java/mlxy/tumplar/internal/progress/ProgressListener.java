package mlxy.tumplar.internal.progress;

public interface ProgressListener {
    void onProgress(String url, long bytesRead, long totalBytes);
}
