package mlxy.tumplar.presenter;

import com.orhanobut.logger.Logger;

import java.util.List;

import javax.inject.Inject;

import mlxy.tumplar.entity.DashboardPhotoResponse;
import mlxy.tumplar.global.App;
import mlxy.tumplar.model.DashboardModel;
import mlxy.tumplar.view.DashboardView;
import rx.functions.Action1;

public class DashboardPresenter implements Presentable<DashboardView> {
    @Inject
    DashboardModel model;

    private DashboardView view;

    private List<DashboardPhotoResponse.ResponseEntity.PostsEntity> data;
    private Throwable error;

    public DashboardPresenter() {
        App.component.inject(this);
        refresh();
    }

    public void refresh() {
        data = null;
        error = null;

        model.dashboard()
                .subscribe(new Action1<DashboardPhotoResponse>() {
                    @Override
                    public void call(DashboardPhotoResponse response) {
                        data = response.response.posts;
                        publish();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        error = throwable;
                        publish();
                    }
                });
    }

    @Override
    public void onTakeView(DashboardView view) {
        this.view = view;
        publish();
    }

    @Override
    public void publish() {
        if (view != null) {
            if (data != null) {
                view.onPostsNext(data);
            } else if (error != null) {
                view.onError(error);
            }

            view.hideProgressIfShown();
        }
    }
}
