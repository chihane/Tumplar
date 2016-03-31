package mlxy.tumplar.view.internal;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import mlxy.tumplar.R;
import mlxy.tumplar.view.activity.HelpActivity;
import mlxy.tumplar.view.fragment.DashboardFragment;
import mlxy.tumplar.view.fragment.HomeFragment;

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
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        boolean toCheckItem = false;

        switch (item.getItemId()) {
            case R.id.menu_home:
                switchToHome();
                toCheckItem = true;
                break;
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

    private void switchToHome() {
        toolBar.setTitle(R.string.home);
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, new HomeFragment()).commit();
    }

    private void switchToDashboard() {
        toolBar.setTitle(R.string.dashboard);
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, new DashboardFragment()).commit();
    }

    private void switchToLikes() {
    }

    private void openSettings() {
    }

    private void openHelp() {
        Intent intent = new Intent(fragmentActivity, HelpActivity.class);
        fragmentActivity.startActivity(intent);
    }

    private void closeDrawer() {
        drawerLayout.closeDrawers();
    }
}
