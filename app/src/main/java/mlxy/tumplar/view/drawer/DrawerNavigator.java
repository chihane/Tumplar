package mlxy.tumplar.view.drawer;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import mlxy.tumplar.R;
import mlxy.tumplar.view.activity.HelpActivity;
import mlxy.tumplar.view.activity.SettingsActivity;
import mlxy.tumplar.view.fragment.DashboardFragment;

public class DrawerNavigator implements NavigationView.OnNavigationItemSelectedListener {
    private FragmentActivity fragmentActivity;
    private final DrawerLayout drawerLayout;
    private final NavigationView navigationView;
    private Toolbar toolBar;

    public DrawerNavigator(FragmentActivity fragmentActivity, DrawerLayout drawerLayout, NavigationView navigationView, Toolbar toolBar) {
        this.fragmentActivity = fragmentActivity;
        this.drawerLayout = drawerLayout;
        this.navigationView = navigationView;
        this.navigationView.setNavigationItemSelectedListener(this);
        this.toolBar = toolBar;

        navigationView.setCheckedItem(R.id.menu_dashboard);
        switchToDashboard();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        boolean toCheckItem = false;

        switch (item.getItemId()) {
            case R.id.menu_dashboard:
                switchToDashboard();
                toCheckItem = true;
                break;
            case R.id.menu_likes:
                switchToLikes();
                toCheckItem = true;
                break;
            case R.id.menu_settings:
                openSettings();
                toCheckItem = false;
                break;
            case R.id.menu_help:
                openHelp();
                toCheckItem = false;
                break;
        }

        closeDrawer();
        return toCheckItem;
    }

    private void switchToDashboard() {
        toolBar.setTitle(R.string.dashboard);

        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();

//        if (HomeFragment.instance().isVisible()) {
//            transaction.hide(HomeFragment.instance());
//        }

        if (!DashboardFragment.instance().isAdded()) {
            transaction.add(R.id.content, DashboardFragment.instance());
        } else {
            transaction.show(DashboardFragment.instance());
        }

        transaction.commit();
    }

    private void switchToLikes() {
    }

    private void openSettings() {
        Intent intent = new Intent(fragmentActivity, SettingsActivity.class);
        fragmentActivity.startActivity(intent);
    }

    private void openHelp() {
        Intent intent = new Intent(fragmentActivity, HelpActivity.class);
        fragmentActivity.startActivity(intent);
    }

    private void closeDrawer() {
        drawerLayout.closeDrawers();
    }
}
