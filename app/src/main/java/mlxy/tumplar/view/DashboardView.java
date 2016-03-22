package mlxy.tumplar.view;

import java.util.List;

import mlxy.tumplar.entity.Post;

public interface DashboardView {

    void onPostsNext(List<Post> posts);

    void onError(Throwable error);

    void hideProgressIfShown();
}
