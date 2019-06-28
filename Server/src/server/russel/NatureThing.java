package server.russel;

public abstract class NatureThing extends Thing {
    private boolean beauty;

    public NatureThing(boolean isMaterial, Danger danger) {
        super(isMaterial, danger);
    }

    public boolean isBeauty() {
        return beauty;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        Bush bu = (Bush) obj;
        return (getDanger() == bu.getDanger() && getCost() == bu.getCost() && getAge() == bu.getAge()
                 && isBeauty() == bu.isBeauty());
    }
}
