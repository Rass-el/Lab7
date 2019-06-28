package server.russel;

class Material {
    double straight;
    public boolean isStraight() {
        return true;
    }
}

public class SpaceSuit extends Equipment {

    Material material = new Material() {
        double straight;

        public boolean isStraight() {
            return (straight >= 50);
        }
    };

    public SpaceSuit(int weight, Rarity rarity) {
        super(true, Danger.SAFE, weight);
        setRarity(rarity);
    }


}
