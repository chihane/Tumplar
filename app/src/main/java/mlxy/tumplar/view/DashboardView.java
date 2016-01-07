package mlxy.tumplar.view;

import com.tumblr.jumblr.types.PhotoPost;

import java.util.List;

public interface DashboardView {

    void onPostsNext(List<PhotoPost> posts);

    void onError(Throwable error);

    void hideProgressIfShown();
}
