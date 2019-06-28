package server.russel;

public class Equipment extends Thing {
    private Rarity rarity;
    private int weight;
    private String name = "my equip";

    public Equipment(boolean isMaterial, Danger danger, int weight) {
        super(true, danger);
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        Equipment th = (Equipment) obj;
        return (getDanger() == th.getDanger() && getCost() == th.getCost() && getAge() == th.getAge()
        && getRarity() == th.getRarity() && getWeight() == th.getWeight() && getName().equals(th.getName()));
    }

    @Override
    public String toString() {
        return "Name: " + this.name+
                "\nDanger: " + getDanger() +
                "\nIs material: yes" +
                "\nRarity: " + rarity +
                "\nWeight: " + weight + "\n";
    }

    @Override
    public int hashCode() {
        final int prime = 997;
        int result;
        result = (prime * prime * weight * weight) % 1_000_000;
        return result;
    }
}
