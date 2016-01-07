package mlxy.tumplar.presenter;

import mlxy.tumplar.global.Application;
import mlxy.tumplar.global.Constants;
import mlxy.tumplar.global.User;
import mlxy.tumplar.tumblr.TumblrClient;
import mlxy.tumplar.view.DrawerHeaderView;
import mlxy.utils.Prefs;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DrawerHeaderPresenter implements Presentable<DrawerHeaderView> {
    private DrawerHeaderView view;

    public DrawerHeaderPresenter() {
        refresh();
    }

    public void refresh() {
        if (!User.hasLogedIn) {
            publish();
            return;
        }
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
                .first(new Func1<com.tumblr.jumblr.types.User, Boolean>() {
                    @Override
                    public Boolean call(com.tumblr.jumblr.types.User user) {
                        return user != null;
                    }
                })
                .filter(new Func1<com.tumblr.jumblr.types.User, Boolean>() {
                    @Override
                    public Boolean call(com.tumblr.jumblr.types.User user) {
                        return user.getBlogs() != null && user.getBlogs().size() > 0;
                    }
                })
                .map(new Func1<com.tumblr.jumblr.types.User, String>() {
                    @Override
                    public String call(com.tumblr.jumblr.types.User user) {
                        return user.getBlogs().get(0).avatar();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                               @Override
                               public void call(String avatarUrl) {
                                   User.avatarUrl = avatarUrl;
                                   publish();
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {
                                   view.showError(throwable);
                               }
                           }
                );
    }

    @Override
    public void onTakeView(DrawerHeaderView view) {
        this.view = view;
        publish();
    }

    @Override
    public void publish() {
        if (view != null) {
            if (User.hasLogedIn && User.avatarUrl != null) {
                view.displayAvatar(User.avatarUrl);
            } else {
                view.restoreDefaultAvatar();
            }
        }
    }

    public void onUserAvatarClicked() {
        boolean hasLogedIn = User.hasLogedIn;
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
