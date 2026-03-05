package main;

public abstract class Pet {
    protected String name;
    protected int hunger;        // 0 to 100
    protected int happiness;     // 0 to 100
    protected int energy;        // 0 to 100
    protected int cleanliness;   // 0 to 100

    public Pet(String name) {
        this.name = name;
        hunger = 50;
        happiness = 50;
        energy = 50;
        cleanliness = 50;
    }

    public void feed() { 
        hunger = Math.min(100, hunger + 20); // increases satisfaction
        clampStats(); 
    }

    public void play() { 
        happiness = Math.min(100, happiness + 20); 
        energy = Math.max(0, energy - 10);
        cleanliness = Math.max(0, cleanliness - 5);
        clampStats(); 
    }

    public void clean() { 
        cleanliness = Math.min(100, cleanliness + 20); 
        clampStats(); 
    }

    public void rest() { 
        energy = Math.min(100, energy + 30); 
        hunger = Math.max(0, hunger - 10); // gets slightly less satisfied
        clampStats(); 
    }
    public void increaseEnergy(int amount) {
    this.energy = Math.min(100, this.energy + amount);
}

    // 🧩 Universal safeguard
    private void clampStats() {
        hunger = clamp(hunger);
        happiness = clamp(happiness);
        energy = clamp(energy);
        cleanliness = clamp(cleanliness);
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(100, value));
    }

    // --- Getters ---
    public String getName() { return name; }
    public int getHunger() { return hunger; }
    public int getHappiness() { return happiness; }
    public int getEnergy() { return energy; }
    public int getCleanliness() { return cleanliness; }
}
