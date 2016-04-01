package mlxy.tumplar.global.settings;

public enum PreviewQuality {
    ORIGINAL(0), HIGH(1), MEDIUM(2), LOW(3);

    private int identifier;
    PreviewQuality(int identifier) {
        this.identifier = identifier;
    }

    public static PreviewQuality byIdentifier(int identifier) {
        switch (identifier) {
            case 0:
                return ORIGINAL;
            case 1:
                return HIGH;
            case 2:
                return MEDIUM;
            case 3:
                return LOW;
            default:
                return MEDIUM;
        }
    }

    public int getIdentifier() {
        return identifier;
    }
}