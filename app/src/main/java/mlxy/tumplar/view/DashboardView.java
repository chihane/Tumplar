package mlxy.tumplar.view;

import java.util.List;

import mlxy.tumplar.entity.PhotoPost;

public interface DashboardView {

    void onPostsNext(List<PhotoPost> posts, boolean renew);

    void onError(Throwable error);

    void hideProgressIfShown();
}
