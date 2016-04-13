package mlxy.tumplar.presenter;

import android.net.Uri;

import javax.inject.Inject;

import mlxy.tumplar.global.App;
import mlxy.tumplar.global.User;
import mlxy.tumplar.model.AvatarModel;
import mlxy.tumplar.model.UserModel;
import mlxy.tumplar.view.DrawerHeaderView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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
        publish();
        getAvatar();
    }

    private void getAvatar() {
        Observable.just(User.info)
                .concatWith(userModel.me()
                        .map(userInfoResponse -> userInfoResponse.response.user)
                        .doOnNext(user -> {
                            User.info = user;
                            publish();
                        }))
                .filter(user -> user != null)
                .map(user -> user.blogs.get(0))
                .flatMap(blog -> AvatarModel.getInstance().get(blog.name))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uri -> {
                    avatarUri = uri;
                    publish();
                }, throwable -> view.showError(throwable));
    }

    public void onTakeView(DrawerHeaderView view) {
        this.view = view;
        publish();
    }

    public void publish() {
        if (view != null) {
            if (User.hasLoggedIn) {
                if (avatarUri != null) {
                    view.showAvatar(avatarUri);
                }
                if (User.info != null) {
                    view.setUsername(User.info.name);
                }
            } else {
                view.showDefaultAvatar();
                view.setUsername(null);
            }
        }
    }

    public void loginOrLogout() {
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
