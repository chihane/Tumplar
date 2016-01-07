package mlxy.tumplar.presenter;

public interface Presentable<T> {
    void onTakeView(T view);
    void publish();
}
