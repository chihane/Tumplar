package mlxy.tumplar.internal.progress;

import android.net.Uri;

import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;

public class ProgressDataSource extends BaseDataSubscriber<Void> {
    private Uri uri;

    public ProgressDataSource(Uri uri, ProgressListener listener) {
        this.uri = uri;
        ProgressDispatcher.addListener(uri.toString(), listener);
    }

    @Override
    public void onCancellation(DataSource<Void> dataSource) {
        ProgressDispatcher.removeListener(uri.toString());
    }

    @Override
    public void onProgressUpdate(DataSource<Void> dataSource) {

    }

    @Override
    protected void onNewResultImpl(DataSource<Void> dataSource) {
        ProgressDispatcher.removeListener(uri.toString());
    }

    @Override
    protected void onFailureImpl(DataSource<Void> dataSource) {
        ProgressDispatcher.removeListener(uri.toString());
    }
}
