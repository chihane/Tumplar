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
    private static final class Initializer {
        private static final DashboardFragment INSTANCE = new DashboardFragment();
    }
    public static DashboardFragment instance() {
        return Initializer.INSTANCE;
    }

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerViewPost;
    private DashboardListAdapter adapter;

    private LayoutMode layoutMode = LayoutMode.LIST;
    private LinearLayoutManager listLayoutManager;
    private StaggeredGridLayoutManager flowLayoutManager;
    private GridLayoutManager gridLayoutManager;

    private DashboardPresenter presenter;

    public DashboardFragment() {
        presenter = new DashboardPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_dashboard, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerViewPost = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerViewPost.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new DashboardListAdapter();
        adapter.setOnLoadMoreListener(this);
        recyclerViewPost.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onTakeView(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // TODO pause network task if hidden?
    }

    @Override
    public void onPostsNext(List<PhotoPost> posts, boolean renew) {
        if (renew) {
            adapter.setData(posts);
        } else {
            adapter.appendData(posts);
        }
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
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
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
        menu.findItem(R.id.menu_staggered).setVisible(layoutMode == LayoutMode.FLOW);
        menu.findItem(R.id.menu_grid).setVisible(layoutMode == LayoutMode.GRID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_download:
                break;

            case R.id.menu_list:
                changeLayoutType(LayoutMode.FLOW);
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
                if (listLayoutManager == null) {
                    listLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                }
                recyclerViewPost.setLayoutManager(listLayoutManager);
                adapter.setImageInSquare(false);
                break;

            case FLOW:
                if (flowLayoutManager == null) {
                    flowLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                }
                recyclerViewPost.setLayoutManager(flowLayoutManager);
                adapter.setImageInSquare(false);
                break;

            case GRID:
                if (gridLayoutManager == null) {
                    gridLayoutManager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
                }
                recyclerViewPost.setLayoutManager(gridLayoutManager);
                adapter.setImageInSquare(true);
                break;
        }
    }

    enum LayoutMode {
        LIST, FLOW, GRID
    }
}
