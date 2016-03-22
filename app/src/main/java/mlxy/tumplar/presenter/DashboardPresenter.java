package mlxy.tumplar.presenter;

import java.util.List;

import javax.inject.Inject;

import mlxy.tumplar.entity.PhotoPost;
import mlxy.tumplar.global.App;
import mlxy.tumplar.model.UserModel;
import mlxy.tumplar.view.DashboardView;
import rx.functions.Action0;
import rx.functions.Action1;

public class DashboardPresenter implements Presentable<DashboardView> {
    @Inject
    UserModel model;

    private DashboardView view;

    public DashboardPresenter() {
        App.component.inject(this);
        loadPosts(0);
    }

    public void loadPosts(int offset) {
        model.dashboardPhoto(offset)
                .subscribe(new Action1<List<PhotoPost>>() {
                    @Override
                    public void call(List<PhotoPost> photoPosts) {
                        if (view != null) {
                            view.onPostsNext(photoPosts);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (view != null) {
                            view.onError(throwable);
                        }
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        if (view != null) {
                            view.hideProgressIfShown();
                        }
                    }
                });
    }

    @Override
    public void onTakeView(DashboardView view) {
        this.view = view;
    }

    @Override
    public void publish() {

    }
}
