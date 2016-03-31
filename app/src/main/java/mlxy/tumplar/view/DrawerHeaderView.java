package mlxy.tumplar.view;

import android.net.Uri;

public interface DrawerHeaderView {
    void goLogin();
    void showLogout();

    void showAvatar(Uri avatarUri);
    void showDefaultAvatar();
    void setUsername(String name);

    void showError(Throwable e);

    void onDrawerOpened();

}
