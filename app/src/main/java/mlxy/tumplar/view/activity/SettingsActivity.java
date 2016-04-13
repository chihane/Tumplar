package mlxy.tumplar.view.activity;

import android.os.Bundle;

import mlxy.tumplar.R;
import mlxy.tumplar.view.fragment.SettingsFragment;

public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.settings);
        setContentView(R.layout.normal_layout);

        getFragmentManager().beginTransaction().replace(R.id.content, new SettingsFragment()).commit();
    }
}
