package mlxy.tumplar.model;

import javax.inject.Inject;

import mlxy.tumplar.entity.DashboardPhotoResponse;
import mlxy.tumplar.model.service.DashboardService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DashboardModel {
    @Inject
    DashboardService service;

    @Inject
    public DashboardModel() {
    }

    public Observable<DashboardPhotoResponse> dashboard() {
        return service.dashboard()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
