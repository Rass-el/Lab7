package server.russel;

public enum Danger {
    SAFE("SAFE"),
    RISKI("RISKI"),
    DANGER("DANGER"),
    DEADLY("DEADLY"),
    FATAL("FATAL"),
    UNKNOWN("UNKNOWN");

    private final String value;

    Danger(String value){
        this.value = value;
    }

    public static Danger fromValue(String value) {
        if (value != null) {
            value = value.toUpperCase();
            for (Danger danger: values()) {
                if (danger.value.equals(value)) {
                    return danger;
                }
            }
        }
        return UNKNOWN;
    }

    public String toValue() {
        return value.toUpperCase();
    }
}
