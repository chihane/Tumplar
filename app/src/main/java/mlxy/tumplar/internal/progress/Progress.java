package mlxy.tumplar.internal.progress;

import android.support.v4.util.Pools;

public class Progress {
    public long bytesRead;
    public long totalBytes;

    public Progress(long totalBytesRead, long totalBytes) {
        bytesRead = totalBytesRead;
        this.totalBytes = totalBytes;
    }

    private static final int MAX_POOL_SIZE = 20;
    private static final Pools.Pool<Progress> pool = new Pools.SynchronizedPool<>(MAX_POOL_SIZE);
    public static Progress obtain(long totalBytesRead, long totalBytes) {
        Progress progress = pool.acquire();
        if (progress != null) {
            return progress;
        }

        Progress progress1 = new Progress(totalBytesRead, totalBytes);
        pool.release(progress1);
        return progress1;
    }
}
