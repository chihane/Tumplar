package mlxy.tumplar.view;

import android.net.Uri;

public interface DrawerHeaderView {
    void goLogin();
    void showLogout();

    void displayAvatar(Uri avatarUri);
    void displayDefaultAvatar();

    void showError(Throwable e);

    void onDrawerOpened();
}
