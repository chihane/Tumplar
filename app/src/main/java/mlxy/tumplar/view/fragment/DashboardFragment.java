package mlxy.tumplar.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mlxy.tumplar.R;
import mlxy.tumplar.entity.PhotoPost;
import mlxy.tumplar.presenter.DashboardPresenter;
import mlxy.tumplar.view.DashboardView;
import mlxy.tumplar.view.adapter.DashboardListAdapter;

public class DashboardFragment extends BaseFragment implements DashboardView, SwipeRefreshLayout.OnRefreshListener, DashboardListAdapter.OnLoadMoreListener {
    private LayoutMode layoutMode = LayoutMode.LIST;

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
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new DashboardListAdapter();
        adapter.setOnLoadMoreListener(this);
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
        adapter.appendData(posts);
    }

    @Override
    public void onError(Throwable error) {
        Snackbar.make(view, error.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onRefresh() {
        presenter.loadPosts(0);
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

    @Override
    public void onLoadMore(int offset) {
        presenter.loadPosts(offset);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_dashboard, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_list).setVisible(layoutMode == LayoutMode.LIST);
        menu.findItem(R.id.menu_staggered).setVisible(layoutMode == LayoutMode.STAGGERED);
        menu.findItem(R.id.menu_grid).setVisible(layoutMode == LayoutMode.GRID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_download:
                break;

            case R.id.menu_list:
                changeLayoutType(LayoutMode.STAGGERED);
                getActivity().invalidateOptionsMenu();
                break;

            case R.id.menu_staggered:
                changeLayoutType(LayoutMode.GRID);
                getActivity().invalidateOptionsMenu();
                break;

            case R.id.menu_grid:
                changeLayoutType(LayoutMode.LIST);
                getActivity().invalidateOptionsMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeLayoutType(LayoutMode type) {
        layoutMode = type;
        switch (type) {
            case LIST:
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                break;
            case STAGGERED:
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                break;
            case GRID:
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false));
                break;
        }
    }

    enum LayoutMode {
        LIST, STAGGERED, GRID;
    }
}
