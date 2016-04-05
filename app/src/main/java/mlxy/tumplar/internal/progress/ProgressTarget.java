package mlxy.tumplar.internal.progress;

public class ProgressTarget extends TargetWrapper {
    public ProgressTarget(String url, ProgressListener listener) {
        ProgressDispatcher.addListener(url, listener);
    }
}
