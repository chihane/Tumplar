package mlxy.tumplar.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

import mlxy.tumplar.R;
import mlxy.tumplar.view.fragment.HomeFragment;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, new HomeFragment()).commit();
    }
}
