package server.russel;

public class Berry extends NatureThing {
    private BerryType type;

    public Berry(Danger d) {
        super(true, d);
    }

    public BerryType getBerryType() {
        return type;
    }
}
