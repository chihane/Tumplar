package mlxy.tumplar.view.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import mlxy.tumplar.R;
import mlxy.tumplar.view.fragment.HelpFragment;

public class HelpActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_layout);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getFragmentManager().beginTransaction().replace(R.id.content, new HelpFragment()).commit();
    }
}
