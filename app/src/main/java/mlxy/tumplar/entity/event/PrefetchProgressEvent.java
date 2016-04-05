package mlxy.tumplar.entity.event;

public class PrefetchProgressEvent {
    public String url;
    public long bytesRead;
    public long totalBytes;

    public PrefetchProgressEvent(String url, long bytesRead, long totalBytes) {
        this.url = url;
        this.bytesRead = bytesRead;
        this.totalBytes = totalBytes;
    }
}
