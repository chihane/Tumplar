package mlxy.tumplar.entity;

import com.tumblr.jumblr.types.Video;

import java.util.List;

public class VideoPost extends Post {
    public String caption;
    public String video_url;
    public List<Video> player;
    public String thumbnail_url;
    public Integer thumbnail_width;
    public Integer thumbnail_height;
    public Integer duration;
}
