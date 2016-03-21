package mlxy.tumplar.entity.response;

import java.util.List;

public class DashboardPhotoResponse {
    public MetaEntity meta;
    public ResponseEntity response;

    public static class MetaEntity {
        public int status;
        public String msg;
    }

    public static class ResponseEntity {
        public List<PostsEntity> posts;

        public static class PostsEntity {
            public String blog_name;
            public Long id;
            public String post_url;
            public String slug;
            public String type;
            public String date;
            public Long timestamp;
            public String state;
            public String format;
            public String reblog_key;
            public String short_url;
            public String summary;
            public Object recommended_source;
            public Object recommended_color;
            public Boolean followed;
            public Boolean liked;
            public Long note_count;
            public String caption;
            public ReblogEntity reblog;
            public String image_permalink;
            public Boolean can_send_in_message;
            public Boolean can_reply;
            public List<String> tags;
            public List<?> highlighted;
            public List<?> trail;
            public List<PhotosEntity> photos;

            public static class ReblogEntity {
                public String tree_html;
                public String comment;
            }

            public static class PhotosEntity {
                public String caption;
                public OriginalSizeEntity original_size;
                public List<AltSizesEntity> alt_sizes;

                public static class OriginalSizeEntity {
                    public String url;
                    public int width;
                    public int height;
                }

                public static class AltSizesEntity {
                    public String url;
                    public int width;
                    public int height;
                }
            }
        }
    }
}
