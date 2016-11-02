package mlxy.tumplar.view.holder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import mlxy.tumplar.R;
import mlxy.tumplar.entity.Photo;
import mlxy.tumplar.entity.PhotoSize;
import mlxy.tumplar.global.Settings;
import mlxy.tumplar.global.settings.PreviewQuality;

public class DashboardViewHolder extends RecyclerView.ViewHolder {
    public static int TYPE_FOOTER = 0x111;

    private SimpleDraweeView draweeAvatar;
    private TextView textViewBlogName;
    private SimpleDraweeView draweeImage;

    public DashboardViewHolder(ViewGroup parent, int viewType) {
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

    public void onPhotosNext(List<Photo> photos, boolean showImageInSquare) {
        Photo photo = photos.get(0);

        PreviewQuality previewQuality = Settings.getPreviewQuality();
        PhotoSize photoSize = photo.getPhotoSizeByQuality(previewQuality);

        if (showImageInSquare) {
            draweeImage.setAspectRatio(1);
        } else {
            float aspectRatio = (float) photoSize.width / photoSize.height;
            draweeImage.setAspectRatio(aspectRatio);
        }
        draweeImage.setImageURI(Uri.parse(photoSize.url));
    }

    public void setOnPhotoClickListener(View.OnClickListener listener) {
        draweeImage.setOnClickListener(listener);
    }

    public Context getContext() {
        return draweeImage.getContext();
    }

    private static View inflateViewByViewType(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.indeterminate_progressbar, parent, false);
        } else {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard, parent, false);
        }
    }
}
