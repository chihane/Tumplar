package mlxy.tumplar.entity.response;

import mlxy.tumplar.entity.User;

public class UserInfoResponse {
    public Meta meta;
    public Response response;

    public static class Response {
        public User user;
    }
}
