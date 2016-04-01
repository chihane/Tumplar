package mlxy.tumplar.view.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import mlxy.tumplar.R;
import mlxy.tumplar.view.fragment.SettingsFragment;

public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_layout);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.settings);
        }

        getFragmentManager().beginTransaction().replace(R.id.content, new SettingsFragment()).commit();
    }
}
