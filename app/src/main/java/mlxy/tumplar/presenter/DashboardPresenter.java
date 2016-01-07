package mlxy.tumplar.presenter;

import com.tumblr.jumblr.types.PhotoPost;
import com.tumblr.jumblr.types.Post;

import java.util.List;

import mlxy.tumplar.model.DashboardModel;
import mlxy.tumplar.tumblr.Tumblr;
import mlxy.tumplar.view.DashboardView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DashboardPresenter implements Presentable<DashboardView> {
    private DashboardView view;

    private List<PhotoPost> data;
    private Throwable error;

    public DashboardPresenter() {
        refresh();
    }

    public void refresh() {
        data = null;
        error = null;

        Observable.create(new Observable.OnSubscribe<List<Post>>() {
            @Override
            public void call(Subscriber<? super List<Post>> subscriber) {
                subscriber.onNext(Tumblr.userDashboard());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<Post>, Observable<Post>>() {
                    @Override
                    public Observable<Post> call(List<Post> posts) {
                        return Observable.from(posts);
                    }
                }).ofType(PhotoPost.class).cast(PhotoPost.class).toList()
                .subscribe(new Subscriber<List<PhotoPost>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        error = e;
                        publish();
                    }

                    @Override
                    public void onNext(List<PhotoPost> photoPosts) {
                        data = photoPosts;
                        publish();
                    }
                });
    }

    @Override
    public void onTakeView(DashboardView view) {
        this.view = view;
        publish();
    }

    @Override
    public void publish() {
        if (view != null) {
            if (data != null) {
                view.onPostsNext(data);
            } else if (error != null) {
                view.onError(error);
            }

            view.hideProgressIfShown();
        }
    }
}
