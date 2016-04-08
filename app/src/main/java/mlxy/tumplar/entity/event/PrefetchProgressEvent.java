package mlxy.tumplar.entity.event;

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
}
