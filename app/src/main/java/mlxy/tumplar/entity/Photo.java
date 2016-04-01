package mlxy.tumplar.entity;

import java.util.List;

import mlxy.tumplar.global.settings.PreviewQuality;

public class Photo {
    public String caption;
    public PhotoSize original_size;
    public List<PhotoSize> alt_sizes;

    public PhotoSize getPhotoSizeByQuality(PreviewQuality quality) {
        if (alt_sizes == null || alt_sizes.isEmpty()) {
            return original_size;
        }

        switch (quality) {
            case ORIGINAL:
                return original_size;
            case HIGH:
                return alt_sizes.get(alt_sizes.size() > 2 ? 1 : 0);
            case MEDIUM:
                return alt_sizes.get(alt_sizes.size() / 2);
            case LOW:
                return alt_sizes.get(alt_sizes.size() - 1);
            default:
                return alt_sizes.get(alt_sizes.size() / 2);
        }
    }
}
