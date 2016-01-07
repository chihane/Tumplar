package mlxy.tumplar.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import mlxy.tumplar.R;
import mlxy.tumplar.view.fragment.LoginFragment;

public class LoginActivity extends BaseActivity {
    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_layout);
        setTitle(R.string.title_login);

        setupToolbar();

        loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, loginFragment).commit();
    }

    private void setupToolbar() {
        Toolbar toolBar = getToolBar();
        if (toolBar != null) {
            toolBar.setNavigationIcon(R.mipmap.ic_close_white_24dp);
            toolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Called on OAuth authorized. See https://www.tumblr.com/docs/en/api/v2#auth
        Uri data = intent.getData();
        if (data != null && data.getScheme().equals(getString(R.string.oauth_callback_scheme))) {
            getSupportFragmentManager().beginTransaction().detach(loginFragment).commit();
            String verifier = data.getQueryParameter("oauth_verifier");
            loginFragment.onAuthorized(verifier);
        }
    }
}
