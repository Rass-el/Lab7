package server.russel;

public class Air extends NatureThing {
    private double freshness;

    public Air() {
        super(false, Danger.SAFE);
    }

    public double getFreshness() {
        return freshness;
    }

    public void setFreshness(double freshness) {
        this.freshness = freshness;
    }

    public boolean isClean() {
        if (freshness >= 70)
            return true;
        return false;
    }
}
