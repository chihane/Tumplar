package mlxy.tumplar.global.settings;

public enum AvatarSize {
    TINY(16), SMALL(48), NORMAL(64), LARGE(128), HUGE(512);

    private int size;
    AvatarSize(int size) {
        this.size = size;
    }

    public static AvatarSize bySize(int size) {
        switch (size) {
            case 16:
                return TINY;
            case 48:
                return SMALL;
            case 64:
                return NORMAL;
            case 128:
                return LARGE;
            case 512:
                return HUGE;
            default:
                return NORMAL;
        }
    }

    public int getSize() {
        return size;
    }
}
