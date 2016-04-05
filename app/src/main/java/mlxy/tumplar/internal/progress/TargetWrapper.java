package mlxy.tumplar.internal.progress;

import android.graphics.drawable.Drawable;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;

import java.io.File;

public class TargetWrapper implements Target<File> {
    private final Target<File> target;

    public TargetWrapper() {
        this.target = new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                boolean b = resource.canRead();
                boolean b1 = resource.canExecute();
                // ?
            }
        };
    }

    @Override
    public void onLoadStarted(Drawable placeholder) {
        target.onLoadStarted(placeholder);
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        target.onLoadFailed(e, errorDrawable);
    }

    @Override
    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
        target.onResourceReady(resource, glideAnimation);
    }

    @Override
    public void onLoadCleared(Drawable placeholder) {
        target.onLoadCleared(placeholder);
    }

    @Override
    public void getSize(SizeReadyCallback cb) {
        target.getSize(cb);
    }

    @Override
    public void setRequest(Request request) {
        target.setRequest(request);
    }

    @Override
    public Request getRequest() {
        return target.getRequest();
    }

    @Override
    public void onStart() {
        target.onStart();
    }

    @Override
    public void onStop() {
        target.onStop();
    }

    @Override
    public void onDestroy() {
        target.onDestroy();
    }
}
