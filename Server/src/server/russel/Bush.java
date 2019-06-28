package server.russel;

import java.util.ArrayList;

public class Bush extends NatureThing {
    private int weight;
    private ArrayList<Berry> berries = new ArrayList<Berry>();

    public Bush(int weight, Danger d) {
        super(true, d);
        this.weight = weight;
    }

    public int getBerriesNumber() {
        return berries.size();
    }
    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        Bush bu = (Bush) obj;
        return (getDanger() == bu.getDanger() && getCost() == bu.getCost() && getAge() == bu.getAge()
                 && getWeight() == bu.getWeight() && isBeauty() == bu.isBeauty() && berries.equals(bu.berries));
    }

    @Override
    public int hashCode() {
        final int prime = 997;
        int result = (prime * getDanger().ordinal() * getWeight()) / 1_000_000;
        return result;
    }

    @Override
    public String toString() {
        return "Name: " +  this.getClass().getSimpleName()+
                "\nlab.russel.Danger: " + getDanger() +
                "\nIs material: yes" +
                "\nIt has " + getBerriesNumber() + " berries\n";
    }
}
