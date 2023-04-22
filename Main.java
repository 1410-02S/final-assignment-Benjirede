import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

class Creature {
    double chanceOfDeath;
    private double chanceOfReplication;
    int age;
    private String name;

    public Creature(double chanceOfDeath, double chanceOfReplication, String name) {
        this.chanceOfDeath = chanceOfDeath;
        this.chanceOfReplication = chanceOfReplication;
        this.name = name;
        this.age = 0;
    }

    public void die() {
        System.out.println(this.name + " died at age " + this.age);
    }

    public Creature replicate() {
        Random random = new Random();
        double roll = random.nextDouble();
        if (roll < this.chanceOfReplication) {
            System.out.println(this.name + " replicated");
            return new Creature(this.chanceOfDeath, this.chanceOfReplication, generateRandomName());
        } else {
            return null;
        }
    }

    private String generateRandomName() {
        String[] names = readNamesFromFile();
        Random random = new Random();
        int index = random.nextInt(names.length);
        return names[index];
    }

    private String[] readNamesFromFile() {
        List<String> names = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(getClass().getResourceAsStream("names.txt"));
            while (scanner.hasNextLine()) {
                names.add(scanner.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names.toArray(new String[0]);
    }
}

class World {
    private double chanceOfSpawn;
    private List<Creature> creatures;
    private List<Food> food;

    public World(double chanceOfSpawn) {
        this.chanceOfSpawn = chanceOfSpawn;
        this.creatures = new ArrayList<>();
        this.food = new ArrayList<>();
    }

    public void createCreature() {
        Random random = new Random();
        double roll = random.nextDouble();
        if (roll < this.chanceOfSpawn) {
            System.out.println("New creature spawned");
            Creature creature = new Creature(0.01, 0.5, generateRandomName());
            this.creatures.add(creature);
        }
    }

    public void spawnFood() {
        Random random = new Random();
        int roll = random.nextInt(100);
        if (roll < 10) {
            int nutrition = random.nextInt(10) + 1;
            Food newFood = new Food(nutrition);
            this.food.add(newFood);
            System.out.println("New food spawned with nutrition " + nutrition);
        }
    }
    
    public void simulate(int numIterations) {
        for (int i = 0; i < numIterations; i++) {
            System.out.println("Iteration " + (i+1));
            for (Creature creature : this.creatures) {
                creature.age++;
                double roll = new Random().nextDouble();
                if (roll < creature.chanceOfDeath) {
                    creature.die();
                } else {
                    Creature newCreature = creature.replicate();
                    if (newCreature != null) {
                        this.creatures.add(newCreature);
                    }
                }
            }
            createCreature();
            spawnFood();
        }
    }

    private String generateRandomName() {
        String[] names = readNamesFromFile();
        Random random = new Random();
        int index = random.nextInt(names.length);
        return names[index];
    }

    private String[] readNamesFromFile() {
        List<String> names = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(getClass().getResourceAsStream("names.txt"));
            while (scanner.hasNextLine()) {
                names.add(scanner.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names.toArray(new String[0]);
    }
}

class Food {
    private int nutrition;

    public Food(int nutrition) {
        this.nutrition = nutrition;
    }
}

public class Main {
    public static void main(String[] args) {
        World world = new World(0.01);
        world.createCreature();
        world.simulate(10);
    }
}
