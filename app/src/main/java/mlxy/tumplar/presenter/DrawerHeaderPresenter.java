package mlxy.tumplar.presenter;

import android.net.Uri;

import javax.inject.Inject;

import mlxy.tumplar.entity.Blog;
import mlxy.tumplar.entity.response.UserInfoResponse;
import mlxy.tumplar.global.App;
import mlxy.tumplar.global.User;
import mlxy.tumplar.model.AvatarModel;
import mlxy.tumplar.model.UserModel;
import mlxy.tumplar.view.DrawerHeaderView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DrawerHeaderPresenter {
    @Inject
    UserModel userModel;
    private DrawerHeaderView view;

    private Uri avatarUri;

    public DrawerHeaderPresenter() {
        App.graph.inject(this);
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
                .concatWith(userModel.me()
                        .map(new Func1<UserInfoResponse, mlxy.tumplar.entity.User>() {
                            @Override
                            public mlxy.tumplar.entity.User call(UserInfoResponse userInfoResponse) {
                                return userInfoResponse.response.user;
                            }
                        }).doOnNext(new Action1<mlxy.tumplar.entity.User>() {
                            @Override
                            public void call(mlxy.tumplar.entity.User user) {
                                User.info = user;
                            }
                        }))
                .filter(new Func1<mlxy.tumplar.entity.User, Boolean>() {
                    @Override
                    public Boolean call(mlxy.tumplar.entity.User user) {
                        return user != null;
                    }
                })
                .map(new Func1<mlxy.tumplar.entity.User, Blog>() {
                    @Override
                    public Blog call(mlxy.tumplar.entity.User user) {
                        return user.blogs.get(0);
                    }
                })
                .flatMap(new Func1<Blog, Observable<Uri>>() {
                    @Override
                    public Observable<Uri> call(Blog blog) {
                        return AvatarModel.getInstance().get(blog.name);
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

    public void onTakeView(DrawerHeaderView view) {
        this.view = view;
        publish();
    }

    public void publish() {
        if (view != null) {
            if (User.hasLoggedIn && avatarUri != null) {
                view.displayAvatar(avatarUri);
                view.setUsername(User.info.name);
            } else {
                view.displayDefaultAvatar();
            }
        }
    }

    public void onUserAvatarClicked() {
        boolean hasLoggedIn = User.hasLoggedIn;
        if (!hasLoggedIn) {
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
