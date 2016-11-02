package mlxy.tumplar.view.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import mlxy.tumplar.entity.PhotoPost;
import mlxy.tumplar.model.AvatarModel;
import mlxy.tumplar.view.activity.ImageViewerActivity;
import mlxy.tumplar.view.holder.DashboardViewHolder;

import static mlxy.tumplar.view.holder.DashboardViewHolder.TYPE_FOOTER;

public class DashboardListAdapter extends RecyclerView.Adapter<DashboardViewHolder> {
    private OnLoadMoreListener listener;

    private List<PhotoPost> data;
    private boolean showImageInSquare;

    public void setData(List<PhotoPost> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void appendData(List<PhotoPost> data) {
        if (data == null) return;
        if (this.data == null) {
            this.data = new ArrayList<>();
        }

        this.data.addAll(data);
        notifyItemRangeInserted(this.data.size(), data.size());
    }

    public void clearData() {
        if (data != null) {
            data.clear();
            notifyItemRangeRemoved(0, data.size());
        }
    }

    public void showImageInSquare(boolean showImageInSquare) {
        this.showImageInSquare = showImageInSquare;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size() + 1;
    }

    @Override
    public DashboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DashboardViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public void onBindViewHolder(final DashboardViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            if (listener != null) {
                listener.onLoadMore(data.size());
            }
            return;
        }

        final PhotoPost photoPost = data.get(position);
        holder.onBlogNameNext(photoPost.blog_name);
        holder.onPhotosNext(photoPost.photos, showImageInSquare);
        loadAvatar(holder, photoPost);

        holder.setOnPhotoClickListener(view -> {
            String url = photoPost.photos.get(0).original_size.url;
            ImageViewerActivity.startWithImageUri(holder.getContext(), Uri.parse(url));
        });
    }

    private void loadAvatar(final DashboardViewHolder holder, final PhotoPost photoPost) {
        AvatarModel.getInstance().get(photoPost.blog_name)
                .doOnSubscribe(() -> holder.onAvatarNext(null))
                .subscribe(holder::onAvatarNext,
                        throwable -> Logger.e(throwable, "dashboard"));
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.listener = listener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int offset);
    }
}
