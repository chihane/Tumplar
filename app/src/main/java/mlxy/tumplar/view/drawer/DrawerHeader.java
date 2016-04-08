package mlxy.tumplar.view.drawer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import mlxy.tumplar.R;
import mlxy.tumplar.presenter.DrawerHeaderPresenter;
import mlxy.tumplar.view.DrawerHeaderView;
import mlxy.tumplar.view.activity.BaseActivity;
import mlxy.tumplar.view.activity.LoginActivity;

public class DrawerHeader implements DrawerHeaderView, View.OnClickListener {
    private BaseActivity activity;
    private DrawerLayout drawerLayout;

    private View headerView;
    private SimpleDraweeView draweeAvatar;
    private TextView textViewUsername;

    private DrawerHeaderPresenter presenter;

    private AlertDialog confirmLogoutDialog;

    public DrawerHeader(BaseActivity activity, DrawerLayout drawerLayout, NavigationView navigationView) {
        this.activity = activity;
        this.drawerLayout = drawerLayout;
        headerView = navigationView.getHeaderView(0);
        initView();

        presenter = new DrawerHeaderPresenter();
        presenter.onTakeView(this);
    }

    private void initView() {
        draweeAvatar = (SimpleDraweeView) headerView.findViewById(R.id.draweeAvatar);
        textViewUsername = (TextView) headerView.findViewById(R.id.textViewUsername);

        draweeAvatar.setOnClickListener(this);
        textViewUsername.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.draweeAvatar:
            case R.id.textViewUsername:
                presenter.loginOrLogout();
                break;
        }
    }

    @Override
    public void goLogin() {
        activity.startActivity(new Intent(activity, LoginActivity.class));
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showLogout() {
        if (confirmLogoutDialog == null) {
            confirmLogoutDialog = new AlertDialog.Builder(activity)
                    .setMessage(R.string.msg_confirm_logout)
                    .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.logout();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
        }

        confirmLogoutDialog.show();
    }

    @Override
    public void showAvatar(Uri avatarUri) {
        draweeAvatar.setImageURI(avatarUri);
    }

    @Override
    public void showDefaultAvatar() {
        draweeAvatar.setImageURI(null);
    }

    @Override
    public void setUsername(String name) {
        if (name != null) {
            textViewUsername.setText(name);
        } else {
            textViewUsername.setText(activity.getString(R.string.not_logged_in));
        }
    }

    @Override
    public void showError(Throwable e) {
//        T.showLong(activity, e.getLocalizedMessage());
    }

    @Override
    public void onDrawerOpened() {
        if (presenter != null) {
            presenter.refresh();
        }
    }
}
