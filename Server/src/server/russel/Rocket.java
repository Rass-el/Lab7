package server.russel;

import java.util.ArrayList;

public class Rocket extends Thing {
    private String name;
    private ArrayList<Person> persons = new ArrayList<Person>();


    static{
        try {
            throw new RuntimeException();
        }catch(RuntimeException e) {

        }
    }

    public Rocket(Danger danger) {
        super(true, danger);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Person> getPerson() {
        return persons;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void takePerson(Person person) {
        if (persons.size() < 2) {
            persons.add(person);
            System.out.print("\n В ракете теперь " + person.getName() + "!\n");
        }
        else
            System.out.println("Третий лишний!");
    }

    public void removePerson(String name) {
        persons.remove(name);
        System.out.print("\n" + name + " свалил!\n");
    }


    public static class Trinket {
        private int cost;
        private boolean beauty;

        public int getCost() {
            return cost;
        }

        public boolean isBeauty() {
            return beauty;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }
    }

    public class Engine {
        private int cost;
        private int occupancy;

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public void setOccupancy(int occupancy) throws WrongOccupancyException{
            if (occupancy > 100 || occupancy < 0) {
                throw new WrongOccupancyException();
            }else {
                this.occupancy = occupancy;
            }
        }

        public int getOccupancy() {
            return occupancy;
        }
    }
}
