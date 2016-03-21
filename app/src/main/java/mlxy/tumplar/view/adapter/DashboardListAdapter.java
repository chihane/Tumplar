package mlxy.tumplar.view.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tumblr.jumblr.types.Photo;
import com.tumblr.jumblr.types.PhotoPost;

import java.util.List;

import mlxy.tumplar.R;
import mlxy.tumplar.entity.DashboardPhotoResponse;
import mlxy.tumplar.model.AvatarModel;
import rx.functions.Action0;
import rx.functions.Action1;

public class DashboardListAdapter extends RecyclerView.Adapter<DashboardListAdapter.ViewHolder> {
    private List<DashboardPhotoResponse.ResponseEntity.PostsEntity> data;
    private ViewHolder holder;

    public void setData(List<DashboardPhotoResponse.ResponseEntity.PostsEntity> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DashboardPhotoResponse.ResponseEntity.PostsEntity photoPost = data.get(position);
        holder.onBlogNameNext(photoPost.blog_name);
        holder.onPhotosNext(photoPost.photos);
        loadAvatar(holder, photoPost);
    }

    private void loadAvatar(final ViewHolder holder, final DashboardPhotoResponse.ResponseEntity.PostsEntity photoPost) {
        AvatarModel.getInstance().get(photoPost.blog_name)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        holder.onAvatarNext(null);
                    }
                })
                .subscribe(new Action1<Uri>() {
                    @Override
                    public void call(Uri uri) {
                        holder.onAvatarNext(uri);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // eat it
                    }
                });
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView draweeAvatar;
        private TextView textViewBlogName;
        private SimpleDraweeView draweeImage;

        public ViewHolder(ViewGroup parent, int viewType) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard, parent, false));
            draweeImage = (SimpleDraweeView) itemView.findViewById(R.id.draweeImage);
            draweeAvatar = (SimpleDraweeView) itemView.findViewById(R.id.draweeAvatar);
            textViewBlogName = (TextView) itemView.findViewById(R.id.textViewBlogName);
        }

        public void onBlogNameNext(String blogName) {
            textViewBlogName.setText(blogName);
        }

        public void onAvatarNext(Uri avatarUri) {
            draweeAvatar.setImageURI(avatarUri);
        }

        public void onPhotosNext(List<DashboardPhotoResponse.ResponseEntity.PostsEntity.PhotosEntity> photos) {
            DashboardPhotoResponse.ResponseEntity.PostsEntity.PhotosEntity photo = photos.get(0);

            float aspectRatio = (float) photo.original_size.height / photo.original_size.width;
            draweeImage.setAspectRatio(aspectRatio);
            draweeImage.setImageURI(Uri.parse(photo.original_size.url));
        }
    }
}
