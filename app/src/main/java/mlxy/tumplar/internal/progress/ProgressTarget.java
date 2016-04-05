package mlxy.tumplar.internal.progress;

import android.graphics.drawable.Drawable;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.orhanobut.logger.Logger;

import java.io.File;

public class ProgressTarget extends TargetWrapper {
    private final String url;
    private final ProgressListener listener;

    public ProgressTarget(String url, ProgressListener listener) {
        this.url = url;
        this.listener = listener;
    }

    @Override
    public void onLoadStarted(Drawable placeholder) {
        super.onLoadStarted(placeholder);
        ProgressDispatcher.addListener(url, listener);
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        super.onLoadFailed(e, errorDrawable);
        Logger.e(e, "failed");
    }

    @Override
    public void onLoadCleared(Drawable placeholder) {
        super.onLoadCleared(placeholder);
    }

    @Override
    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
        super.onResourceReady(resource, glideAnimation);
    }
}
