package mlxy.tumplar.presenter;

import android.net.Uri;

import com.tumblr.jumblr.types.Blog;

import mlxy.tumplar.global.User;
import mlxy.tumplar.model.AvatarModel;
import mlxy.tumplar.global.TumblrClient;
import mlxy.tumplar.view.DrawerHeaderView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DrawerHeaderPresenter implements Presentable<DrawerHeaderView> {
    private DrawerHeaderView view;

    private Uri avatarUri;

    public DrawerHeaderPresenter() {
        refresh();
    }

    public void refresh() {
        if (!User.hasLoggedIn) {
            publish();
            return;
        }

        displayAvatar();
    }

    private void displayAvatar() {
        Observable.just(User.info)
                .concatWith(Observable.create(new Observable.OnSubscribe<com.tumblr.jumblr.types.User>() {
                    @Override
                    public void call(Subscriber<? super com.tumblr.jumblr.types.User> subscriber) {
                        subscriber.onNext(TumblrClient.userInfo());
                        subscriber.onCompleted();
                    }
                }).doOnNext(new Action1<com.tumblr.jumblr.types.User>() {
                    @Override
                    public void call(com.tumblr.jumblr.types.User user) {
                        User.info = user;
                    }
                }))
                .filter(new Func1<com.tumblr.jumblr.types.User, Boolean>() {
                    @Override
                    public Boolean call(com.tumblr.jumblr.types.User user) {
                        return user != null;
                    }
                })
                .map(new Func1<com.tumblr.jumblr.types.User, Blog>() {
                    @Override
                    public Blog call(com.tumblr.jumblr.types.User user) {
                        return user.getBlogs().get(0);
                    }
                })
                .flatMap(new Func1<Blog, Observable<Uri>>() {
                    @Override
                    public Observable<Uri> call(Blog blog) {
                        return AvatarModel.getInstance().get(blog.getName());
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Uri>() {
                    @Override
                    public void call(Uri uri) {
                        avatarUri = uri;
                        publish();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.showError(throwable);
                    }
                });
    }

    @Override
    public void onTakeView(DrawerHeaderView view) {
        this.view = view;
        publish();
    }

    @Override
    public void publish() {
        if (view != null) {
            if (User.hasLoggedIn && avatarUri != null) {
                view.displayAvatar(avatarUri);
            } else {
                view.displayDefaultAvatar();
            }
        }
    }

    public void onUserAvatarClicked() {
        boolean hasLogedIn = User.hasLoggedIn;
        if (!hasLogedIn) {
            view.goLogin();
        } else {
            view.showLogout();
        }
    }

    public void logout() {
        User.logout();
        publish();
    }
}
