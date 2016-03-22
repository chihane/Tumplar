package mlxy.tumplar.model;

import java.util.List;

import javax.inject.Inject;

import mlxy.tumplar.entity.PhotoPost;
import mlxy.tumplar.entity.Post;
import mlxy.tumplar.entity.PostTypes;
import mlxy.tumplar.entity.response.DashboardResponse;
import mlxy.tumplar.entity.response.UserInfoResponse;
import mlxy.tumplar.model.service.UserService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class UserModel {
    @Inject
    UserService service;

    @Inject
    public UserModel() {
    }

    public Observable<UserInfoResponse> me() {
        return service.me()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<PhotoPost>> dashboardPhoto() {
        return dashboardPhoto(0);
    }

    public Observable<List<PhotoPost>> dashboardPhoto(int offset) {
        return service.dashboard(PostTypes.PHOTO.name().toLowerCase(), offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<DashboardResponse, Observable<?>>() {
                    @Override
                    public Observable<Post> call(DashboardResponse dashboardResponse) {
                        return Observable.from(dashboardResponse.response.posts);
                    }
                })
                .ofType(PhotoPost.class)
                .cast(PhotoPost.class)
                .toList();
    }
}
