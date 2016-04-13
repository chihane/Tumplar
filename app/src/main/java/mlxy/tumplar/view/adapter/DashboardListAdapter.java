package mlxy.tumplar.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import mlxy.tumplar.R;
import mlxy.tumplar.entity.Photo;
import mlxy.tumplar.entity.PhotoPost;
import mlxy.tumplar.entity.PhotoSize;
import mlxy.tumplar.global.Settings;
import mlxy.tumplar.global.settings.PreviewQuality;
import mlxy.tumplar.model.AvatarModel;
import mlxy.tumplar.view.activity.ImageViewerActivity;

public class DashboardListAdapter extends RecyclerView.Adapter<DashboardListAdapter.ViewHolder> {
    private static int TYPE_FOOTER = 0x111;

    private OnLoadMoreListener listener;

    private List<PhotoPost> data;
    private boolean setImageInSquare;

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

    public void setImageInSquare(boolean setImageInSquare) {
        this.setImageInSquare = setImageInSquare;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size() + 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, viewType);
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            if (listener != null) {
                listener.onLoadMore(data.size());
            }
            return;
        }

        final PhotoPost photoPost = data.get(position);
        holder.onBlogNameNext(photoPost.blog_name);
        holder.onPhotosNext(photoPost.photos);
        loadAvatar(holder, photoPost);

        holder.setOnPhotoClickListener(view -> {
            String url = photoPost.photos.get(0).original_size.url;
            ImageViewerActivity.startWithImageUri(holder.getContext(), Uri.parse(url));
        });
    }

    private void loadAvatar(final ViewHolder holder, final PhotoPost photoPost) {
        AvatarModel.getInstance().get(photoPost.blog_name)
                .doOnSubscribe(() -> holder.onAvatarNext(null))
                .subscribe(holder::onAvatarNext,
                        throwable -> Logger.e(throwable, "dashboard"));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView draweeAvatar;
        private TextView textViewBlogName;
        private SimpleDraweeView draweeImage;

        public ViewHolder(ViewGroup parent, int viewType) {
            super(inflateViewByViewType(parent, viewType));
            draweeImage = (SimpleDraweeView) itemView.findViewById(R.id.photoViewPhoto);
            draweeAvatar = (SimpleDraweeView) itemView.findViewById(R.id.draweeAvatar);
            textViewBlogName = (TextView) itemView.findViewById(R.id.textViewBlogName);
        }

        public void onBlogNameNext(String blogName) {
            textViewBlogName.setText(blogName);
        }

        public void onAvatarNext(Uri avatarUri) {
            draweeAvatar.setImageURI(avatarUri);
        }

        public void onPhotosNext(List<Photo> photos) {
            Photo photo = photos.get(0);

            PreviewQuality previewQuality = Settings.getPreviewQuality();
            PhotoSize photoSize = photo.getPhotoSizeByQuality(previewQuality);

            if (setImageInSquare) {
                draweeImage.setAspectRatio(1);
            } else {
                float aspectRatio = (float) photoSize.width / photoSize.height;
                draweeImage.setAspectRatio(aspectRatio);
            }
            draweeImage.setImageURI(Uri.parse(photoSize.url));
        }

        void setOnPhotoClickListener(View.OnClickListener listener) {
            draweeImage.setOnClickListener(listener);
        }

        public Context getContext() {
            return draweeImage.getContext();
        }
    }

    private static View inflateViewByViewType(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.indeterminate_progressbar, parent, false);
        } else {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard, parent, false);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.listener = listener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int offset);
    }
}
