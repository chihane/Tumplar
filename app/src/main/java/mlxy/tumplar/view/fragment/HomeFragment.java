package mlxy.tumplar.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tumblr.jumblr.types.Post;

import java.security.GeneralSecurityException;
import java.util.List;

import mlxy.tumplar.R;
import mlxy.tumplar.data.Dashboard;
import mlxy.tumplar.model.DashboardModel;
import mlxy.utils.L;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeFragment extends BaseFragment {
    private TextView textView;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_home, container, false);
        textView = (TextView) view.findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardModel dashboardModel = new DashboardModel();
                try {
                    dashboardModel.dashboard()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Dashboard>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Logger.e(e, "error");
                                }

                                @Override
                                public void onNext(Dashboard dashboard) {
                                      Logger.v(dashboard.toString());
                                }
                            });
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
}
