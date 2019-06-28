package server.russel;

public abstract class Thing {
    private Danger danger;
    private int cost;
    private int age;
    private boolean isMaterial;


    public Thing(boolean isMaterial, Danger danger) {
        this.isMaterial = isMaterial;
        this.danger = danger;
    }

    public Danger getDanger() {
        return danger;
    }

    public int getAge() {
        return age;
    }

    public int getCost() {
        return cost;
    }

    public void setDanger(Danger danger) {
        this.danger = danger;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }



    @Override
    public String toString() {
        return "Name: " +  this.getClass().getSimpleName()+
                "\nlab.russel.Danger: " + danger +
                "\nIs material: " +
                (isMaterial?"\nyes\n":"\nno\n");
    }

}
