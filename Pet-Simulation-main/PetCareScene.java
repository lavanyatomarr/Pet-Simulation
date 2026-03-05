package main;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class PetCareScene {
    public static void show(Stage stage, String petType, String petName, Scene backscene) {
        if (petType.equals("Dog")) Dog.show(stage, petName, backscene);
        else Cat.show(stage, petName, backscene);
    }
}