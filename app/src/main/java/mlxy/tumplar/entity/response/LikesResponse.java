package mlxy.tumplar.entity.response;

import java.util.List;

import mlxy.tumplar.entity.Post;

public class LikesResponse {
    public Meta meta;
    public ResponseEntity response;

    public static class ResponseEntity {
        public List<Post> liked_posts;
    }
}
