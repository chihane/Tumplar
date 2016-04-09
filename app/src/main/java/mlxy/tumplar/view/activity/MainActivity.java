package mlxy.tumplar.view.activity;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;

import mlxy.tumplar.R;
import mlxy.utils.Prefs;

public class MainActivity extends BaseActivity {
    private static final String IS_FIRST_RUN = "is_first_run";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (isFirstRun()) {
            getDrawerLayout().openDrawer(GravityCompat.START);
        }
    }

    private boolean isFirstRun() {
        Boolean isFirstRun = Prefs.get(this, IS_FIRST_RUN, true);
        if (isFirstRun) {
            Prefs.put(this, IS_FIRST_RUN, false);
        }
        return isFirstRun;
    }
}
