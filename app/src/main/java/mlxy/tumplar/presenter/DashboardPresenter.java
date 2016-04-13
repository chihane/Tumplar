package mlxy.tumplar.presenter;

import javax.inject.Inject;

import mlxy.tumplar.global.App;
import mlxy.tumplar.model.DashboardModel;
import mlxy.tumplar.view.DashboardView;

public class DashboardPresenter {
    @Inject
    DashboardModel model;

    private DashboardView view;

    public DashboardPresenter() {
        App.graph.inject(this);
    }

    public void loadPosts(final int offset) {
        model.dashboardPhoto(offset)
                .subscribe(photoPosts -> {
                    if (view != null) {
                        view.onPostsNext(photoPosts, offset == 0);
                        view.hideProgressIfShown();
                    }
                }, throwable -> {
                    if (view != null) {
                        view.onError(throwable);
                        view.hideProgressIfShown();
                    }
                });
    }

    public void onTakeView(DashboardView view) {
        this.view = view;
    }
}
