package mlxy.tumplar.internal.progress;

import android.support.v4.util.Pools;

public class Progress {
    public String url;
    public long totalBytesRead;
    public long totalBytes;

    public Progress(String url, long totalBytesRead, long totalBytes) {
        this.url = url;
        this.totalBytesRead = totalBytesRead;
        this.totalBytes = totalBytes;
    }

    private static final int MAX_POOL_SIZE = 20;
    private static final Pools.Pool<Progress> pool = new Pools.SynchronizedPool<>(MAX_POOL_SIZE);

    public static Progress obtain(String url, long totalBytesRead, long totalBytes) {
        Progress progress = pool.acquire();
        if (progress != null) {
            return progress;
        }

        return new Progress(url, totalBytesRead, totalBytes);
    }

    public void recycle() {
        pool.release(this);
    }
}
