package server.russel;

public class Parachute extends Equipment {
    private boolean isOpen;

    public Parachute(int weight, Rarity rarity) {
        super(true, Danger.SAFE, weight);
        setRarity(rarity);
    }

    public void openParachute() {
        isOpen = true;
    }

    public void closeParachute() {
        isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }
}
