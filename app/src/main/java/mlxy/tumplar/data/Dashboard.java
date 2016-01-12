package mlxy.tumplar.data;

import com.tumblr.jumblr.types.Post;

import java.util.List;

public class Dashboard {
    public Meta meta;
    public Response response;

    public static class Meta {
        public int status;
        public String msg;
    }

    public static class Response {
        List<Post> posts;
    }
}
