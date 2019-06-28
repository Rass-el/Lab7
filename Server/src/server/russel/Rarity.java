package server.russel;

public enum Rarity {
    COMMON("COMMON"),
    UNCOMMON("UNCOMMON"),
    RARE("RARE"),
    EPIC("EPIC"),
    LEGENDARY("LEGENDARY"),
    IMPOSSIBLE("IMPOSSIBLE");

    private final String value;

    Rarity(String value){
        this.value = value;
    }

    public static Rarity fromValue(String value) {
        if (value != null) {
            value = value.toUpperCase();
            for (Rarity rarity: values()) {
                if (rarity.value.equals(value)) {
                    return rarity;
                }
            }
        }
        return COMMON;
    }

    public String toValue() {
        return value.toUpperCase();
    }
}
