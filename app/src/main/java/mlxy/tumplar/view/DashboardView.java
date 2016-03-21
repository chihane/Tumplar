package mlxy.tumplar.view;

import java.util.List;

import mlxy.tumplar.entity.response.DashboardPhotoResponse;

public interface DashboardView {

    void onPostsNext(List<DashboardPhotoResponse.ResponseEntity.PostsEntity> posts);

    void onError(Throwable error);

    void hideProgressIfShown();
}
