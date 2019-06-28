package server.russel;

public abstract class Being {
    private Danger danger;

    public Being(Danger danger) {
        this.danger = danger;
    }



    public Danger getDanger() {
        return danger;
    }

    public void setDanger(Danger danger) {
        this.danger = danger;
    }

    @Override
    public String toString() {
        return "Name: " +  this.getClass().getSimpleName()+
                "\nlab.russel.Danger: " + getDanger() + "\n";

    }

    @Override
    public int hashCode() {
        final int prime = 997;
        int result = (prime * prime * getDanger().ordinal() * getDanger().ordinal()) / 1_000_000;
        return result;
    }


}
