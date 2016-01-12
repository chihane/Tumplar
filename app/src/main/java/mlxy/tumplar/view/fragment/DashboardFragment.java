package mlxy.tumplar.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.tumblr.jumblr.types.PhotoPost;

import java.util.List;

import mlxy.tumplar.R;
import mlxy.tumplar.presenter.DashboardPresenter;
import mlxy.tumplar.view.DashboardView;
import mlxy.tumplar.view.adapter.DashboardListAdapter;

public class DashboardFragment extends BaseFragment implements DashboardView, SwipeRefreshLayout.OnRefreshListener {
    private View view;
    private RecyclerView recyclerView;
    private DashboardPresenter presenter;
    private DashboardListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public DashboardFragment() {
        presenter = new DashboardPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_dashboard, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new DashboardListAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onTakeView(this);
    }

    @Override
    public void onPostsNext(List<PhotoPost> posts) {
        adapter.setData(posts);
    }

    @Override
    public void onError(Throwable error) {
        adapter.setData(null);
        Snackbar.make(view, error.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onRefresh() {
//        presenter.refresh();
    }

    @Override
    public void hideProgressIfShown() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
