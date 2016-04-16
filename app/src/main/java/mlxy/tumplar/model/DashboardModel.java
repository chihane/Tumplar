package mlxy.tumplar.model;

import java.util.List;

import javax.inject.Inject;

import mlxy.tumplar.entity.PhotoPost;
import mlxy.tumplar.entity.PostTypes;
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

    public Observable<List<PhotoPost>> dashboardPhoto() {
        return dashboardPhoto(0);
    }

    public Observable<List<PhotoPost>> dashboardPhoto(int offset) {
        return service.dashboard(PostTypes.PHOTO.name().toLowerCase(), offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(dashboardResponse -> Observable.from(dashboardResponse.response.posts))
                .ofType(PhotoPost.class)
                .cast(PhotoPost.class)
                .toList();
    }

    public void savePhotoPost(PhotoPost photoPost) {
        // TODO persist post
    }
}
