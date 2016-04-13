package mlxy.tumplar.view.activity;

import android.os.Bundle;

import mlxy.tumplar.R;
import mlxy.tumplar.view.fragment.HelpFragment;

public class HelpActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.help);
        setContentView(R.layout.normal_layout);

        getFragmentManager().beginTransaction().replace(R.id.content, new HelpFragment()).commit();
    }
}
