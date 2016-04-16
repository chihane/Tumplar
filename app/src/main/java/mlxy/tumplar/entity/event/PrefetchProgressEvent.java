package mlxy.tumplar.entity.event;

import mlxy.tumplar.internal.progress.Progress;

public class PrefetchProgressEvent {
    public static final PrefetchProgressEvent COMPLETE = new PrefetchProgressEvent();

    public String url;
    public Long bytesRead;
    public Long totalBytes;

    public PrefetchProgressEvent() {
    }

    public PrefetchProgressEvent(String url, long bytesRead, long totalBytes) {
        this.url = url;
        this.bytesRead = bytesRead;
        this.totalBytes = totalBytes;
    }

    public PrefetchProgressEvent(Progress progress) {
        this.url = progress.url;
        this.bytesRead = progress.totalBytesRead;
        this.totalBytes = progress.totalBytes;
        progress.recycle();
    }

    public PrefetchProgressEvent setProgress(Progress progress) {
        this.url = progress.url;
        this.bytesRead = progress.totalBytesRead;
        this.totalBytes = progress.totalBytes;
        progress.recycle();
        return this;
    }
}
