package mlxy.tumplar.model;

import com.tumblr.jumblr.types.PhotoPost;
import com.tumblr.jumblr.types.Post;

import java.util.List;

import mlxy.tumplar.tumblr.Tumblr;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class DashboardModel {

    public Observable<List<PhotoPost>> requestData() {
        return Observable.create(new Observable.OnSubscribe<List<Post>>() {
            @Override
            public void call(Subscriber<? super List<Post>> subscriber) {
                subscriber.onNext(Tumblr.userDashboard());
                subscriber.onCompleted();
            }
        }).flatMap(new Func1<List<Post>, Observable<Post>>() {
            @Override
            public Observable<Post> call(List<Post> posts) {
                return Observable.from(posts);
            }
        }).ofType(PhotoPost.class).cast(PhotoPost.class).toList();
    }
}
