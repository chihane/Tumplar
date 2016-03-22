package mlxy.tumplar.entity;

import java.util.List;

public class Post {
    public String blog_name;
    public Long id;
    public String post_url;
    public String type;
    public Long timestamp;
    public String date;
    public String format;
    public String reblog_key;
    public List<String> tags;
    public Boolean bookmarklet;
    public Boolean mobile;
    public String source_url;
    public String source_title;
    public Boolean liked;
    public String state;
    public Integer total_posts;
    public Long reblogged_from_id;
    public String reblogged_from_name;
    public Long note_count;
    public List<Note> notes;
}
