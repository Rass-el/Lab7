package server.russel;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class Person extends Being implements Comparable<Person>, EquipmentOff, EquipmentOn {
    private User user;
    private String name = new String();
    private int age;
    private int[] coordinatesXY = new int[2];
    private OffsetDateTime initDate;
    private ArrayList<Equipment> equip = new ArrayList<>();

    public Person(String name, Danger danger) {
        super(danger);
        initDate = OffsetDateTime.now(ZoneId.of("Europe/Moscow"));
        this.name = name;
        coordinatesXY[0] = 0;
        coordinatesXY[1] = 0;
    }

    public Person(ArrayList<String> parameters) {
        super(Danger.valueOf(parameters.get(2).replace(" ", "").toUpperCase()));   // Мозгоф нету
        this.name = parameters.get(0);
        this.age = Integer.valueOf(parameters.get(1).replace(" ", ""));

        String[] parachuteParametrs = parameters.get(3).split(",");
        Parachute parachute = new Parachute(Integer.valueOf(parachuteParametrs[0]),
                Rarity.valueOf(parachuteParametrs[1].replace(" ", "").toUpperCase()));
        equip.add(parachute);
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void printEquip() {
        System.out.println("Cнаряжение " + getName() + ": ");
        for (Equipment e: equip) {
            if (e != null)
                System.out.print(e.getName() + " ");
        }
        System.out.print("\n");
    }

    public int[] getCoordinatesXY() {
        return coordinatesXY;
    }

    public void setCoordinatesXY(int X, int Y) {
        coordinatesXY[0] = X;
        coordinatesXY[1] = X;
    }

    public int getAge() { return age; }

    public void setAge(int age) { this.age = age; }

    public ArrayList<Equipment> getEquip() {
        return equip;
    }

    public void setEquip(ArrayList<Equipment> equip) {
        this.equip = equip;
    }

    public void takeOff(String name) {

        equip.remove(name);
        System.out.print("\n" + getName() + "  снял " + name + "!\n");
    }

    public void putOn(Equipment e) {
        if (equip.size() < 3) {
            equip.add(e);
            //System.out.print("\n" + getName() + "  теперь снаряжен " + e.getName() + "!\n");
        }
        else
            System.out.println("Нет места! Может быть тебе взять рюкзак?");
    }

    @Override
    public int compareTo(Person person) {
        int result = this.getName().compareTo(person.getName());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        Person pe = (Person) obj;
        if (pe.getEquip().size() != this.getEquip().size()) return false;
        for (int i = 0; i < this.getEquip().size(); ++i) {
            if (this.getEquip().get(i).getWeight() != pe.getEquip().get(i).getWeight()) return false;
            if (this.getEquip().get(i).getRarity() != pe.getEquip().get(i).getRarity()) return false;
        }
        return (getDanger() == pe.getDanger() && getName().equals(pe.getName())
                && getAge() == pe.getAge());
    }

    @Override
    public String toString() {
        return "Name: " +  getName() +
                "\nDanger: " + getDanger() +
                "\nAge: " + getAge() +
                "\nEquip: " + equip +
                "\n";
    }

    public String[] asArray() {
        String[] array = new String[5];
        array[0] = name;
        array[1] = Integer.toString(age);
        array[2] = getDanger().toString();
        array[3] = (equip.isEmpty())? "0" : String.valueOf(equip.get(0).getWeight());
        array[4] = (equip.isEmpty())? "COMMON" : equip.get(0).getRarity().toString();
        return array;
    }

    public User getUser() {
        return user;
    }
}
