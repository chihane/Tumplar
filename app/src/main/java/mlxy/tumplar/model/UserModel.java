package mlxy.tumplar.model;

import java.util.List;

import javax.inject.Inject;

import mlxy.tumplar.entity.PhotoPost;
import mlxy.tumplar.entity.response.UserInfoResponse;
import mlxy.tumplar.model.service.UserService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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

    public Observable<List<PhotoPost>> likes(int offset) {
        return service.likes(offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(response -> Observable.from(response.response.liked_posts))
                .ofType(PhotoPost.class)
                .cast(PhotoPost.class)
                .toList();
    }
}
