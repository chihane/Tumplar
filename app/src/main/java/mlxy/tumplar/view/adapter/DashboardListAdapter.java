package mlxy.tumplar.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tumblr.jumblr.types.Photo;
import com.tumblr.jumblr.types.PhotoPost;

import java.util.List;

import mlxy.tumplar.R;
import mlxy.tumplar.tumblr.Tumblr;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DashboardListAdapter extends RecyclerView.Adapter<DashboardListAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private List<PhotoPost> data;

    public DashboardListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(List<PhotoPost> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PhotoPost post = data.get(position);

        updatePosterInfo(holder, post);
        loadImages(holder, post);
    }

    private void updatePosterInfo(final ViewHolder holder, final PhotoPost post) {
        holder.textViewBlogName.setText(post.getBlogName());

//        Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext(Tumblr.blogAvatar(post.getBlogName()));
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String avatarUrl) {
//                        holder.draweeAvatar.setImageURI(Uri.parse(avatarUrl));
//                    }
//                });
    }


    private void loadImages(ViewHolder holder, PhotoPost post) {
        List<Photo> photos = post.getPhotos();
        Photo photo = photos.get(0);

        float aspectRatio = (float) photo.getOriginalSize().getHeight() / photo.getOriginalSize().getWidth();
        holder.draweeImage.setAspectRatio(aspectRatio);
        holder.draweeImage.setImageURI(Uri.parse(photo.getOriginalSize().getUrl()));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView draweeAvatar;
        TextView textViewBlogName;
        SimpleDraweeView draweeImage;

        public ViewHolder(View itemView) {
            super(itemView);
            draweeImage = (SimpleDraweeView) itemView.findViewById(R.id.draweeImage);
            draweeAvatar = (SimpleDraweeView) itemView.findViewById(R.id.draweeAvatar);
            textViewBlogName = (TextView) itemView.findViewById(R.id.textViewBlogName);
        }
    }
}
