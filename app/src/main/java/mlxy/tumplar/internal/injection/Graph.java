package mlxy.tumplar.internal.injection;

import mlxy.tumplar.model.AvatarModel;
import mlxy.tumplar.presenter.DashboardPresenter;
import mlxy.tumplar.presenter.DrawerHeaderPresenter;
import mlxy.tumplar.service.PrefetchService;
import mlxy.tumplar.view.adapter.DashboardListAdapter;
import mlxy.tumplar.view.fragment.HomeFragment;

public interface Graph {
    void inject(DashboardPresenter dashboardPresenter);

    void inject(DrawerHeaderPresenter drawerHeaderPresenter);

    void inject(AvatarModel avatarModel);

    void inject(DashboardListAdapter dashboardListAdapter);

    void inject(PrefetchService prefetchService);

    void inject(HomeFragment homeFragment);
}
