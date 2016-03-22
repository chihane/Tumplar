package mlxy.tumplar.model;

import javax.inject.Inject;

import mlxy.tumplar.entity.response.DashboardResponse;
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

    public Observable<DashboardResponse> dashboard() {
        return service.dashboard()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
