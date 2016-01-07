package mlxy.tumplar.view;

public interface DrawerHeaderView {
    void goLogin();
    void showLogout();

    void displayAvatar(String avatarUrl);
    void restoreDefaultAvatar();

    void showError(Throwable e);

    void onDrawerOpened();
}
