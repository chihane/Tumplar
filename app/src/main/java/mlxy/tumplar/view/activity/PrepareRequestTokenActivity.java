package mlxy.tumplar.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import mlxy.tumplar.R;
import mlxy.utils.L;
import mlxy.utils.T;

public class PrepareRequestTokenActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_layout);
        setTitle("PrepareRequestTokenActivity");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        L.v("==============================================================================================");
        Uri data = intent.getData();
        if (data != null && data.getScheme().equals(getString(R.string.oauth_callback_scheme))) {
            T.showLong(this, data.toString());
        }
    }
}
