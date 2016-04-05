package mlxy.tumplar.internal.progress;

interface ResponseReadingProgressListener {
    void onProgress(String url, long bytesRead, long totalBytes);
}
