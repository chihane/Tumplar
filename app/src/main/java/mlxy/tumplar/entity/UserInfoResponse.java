package mlxy.tumplar.entity;

import java.util.List;

public class UserInfoResponse {
    public MetaEntity meta;
    public ResponseEntity response;

    public static class MetaEntity {
        public int status;
        public String msg;
    }

    public static class ResponseEntity {
        public UserEntity user;

        public static class UserEntity {
            public String name;
            public int likes;
            public int following;
            public String default_post_format;
            public List<BlogsEntity> blogs;

            public static class BlogsEntity {
                public String title;
                public String name;
                public int posts;
                public String url;
                public int updated;
                public String description;
                public boolean is_nsfw;
                public boolean ask;
                public String ask_page_title;
                public boolean ask_anon;
                public boolean followed;
                public boolean can_send_fan_mail;
                public boolean is_blocked_from_primary;
                public boolean share_likes;
                public int likes;
                public boolean twitter_enabled;
                public boolean twitter_send;
                public String facebook_opengraph_enabled;
                public String tweet;
                public String facebook;
                public int followers;
                public boolean primary;
                public boolean admin;
                public int messages;
                public int queue;
                public int drafts;
                public String type;
                public boolean subscribed;
                public boolean can_subscribe;
            }
        }
    }
}
