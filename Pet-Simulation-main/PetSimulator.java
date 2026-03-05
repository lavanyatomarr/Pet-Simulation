package main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class PetSimulator extends Application {
    private Pet currentPet;
    private ProgressBar hungerBar, happinessBar, energyBar, cleanlinessBar;

    @Override
    public void start(Stage stage) {
        Label title = new Label("🐾 Virtual Pet Simulator 🐾");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // Pet adoption
        ComboBox<String> petChoice = new ComboBox<>();
        petChoice.getItems().addAll("Dog", "Cat");
        TextField petNameField = new TextField();
        petNameField.setPromptText("Enter pet name");

        Button adoptBtn = new Button("Adopt");
        VBox adoptBox = new VBox(10, title, petChoice, petNameField, adoptBtn);
        adoptBox.setStyle("-fx-alignment: center; -fx-padding: 20;");

        StackPane mainPane = new StackPane(adoptBox);
        Scene scene = new Scene(mainPane, 600, 400);
        stage.setTitle("Virtual Pet Simulator");
        stage.setScene(scene);
        stage.show();

        adoptBtn.setOnAction(e -> {
            String name = petNameField.getText();
            String type = petChoice.getValue();
            if (type == null || name.isEmpty()) return;

            if (type.equals("Dog")) currentPet = new Dog(name);
            else currentPet = new Cat(name);

            mainPane.getChildren().clear();
            mainPane.getChildren().add(createPetView());
        });
    }

    private VBox createPetView() {
        Label nameLabel = new Label("Name: " + currentPet.getName());
        hungerBar = new ProgressBar(currentPet.getHunger() / 100.0);
        happinessBar = new ProgressBar(currentPet.getHappiness() / 100.0);
        energyBar = new ProgressBar(currentPet.getEnergy() / 100.0);
        cleanlinessBar = new ProgressBar(currentPet.getCleanliness() / 100.0);

        ImageView petImage = new ImageView(new Image("file:assets/images/dog_happy.png"));
        petImage.setFitHeight(150);
        petImage.setPreserveRatio(true);

        Button feedBtn = new Button("Feed 🍖");
        Button playBtn = new Button("Play 🎾");
        Button cleanBtn = new Button("Clean 🧼");
        Button restBtn = new Button("Rest 😴");

        feedBtn.setOnAction(e -> { currentPet.feed(); updateBars(); });
        playBtn.setOnAction(e -> { currentPet.play(); updateBars(); });
        cleanBtn.setOnAction(e -> { currentPet.clean(); updateBars(); });
        restBtn.setOnAction(e -> { currentPet.rest(); updateBars(); });

        VBox actions = new VBox(10, feedBtn, playBtn, cleanBtn, restBtn);
        VBox stats = new VBox(5,
            new Label("Hunger:"), hungerBar,
            new Label("Happiness:"), happinessBar,
            new Label("Energy:"), energyBar,
            new Label("Cleanliness:"), cleanlinessBar
        );

        HBox content = new HBox(30, petImage, actions, stats);
        content.setStyle("-fx-padding: 20; -fx-alignment: center;");

        return new VBox(10, nameLabel, content);
    }

    private void updateBars() {
        hungerBar.setProgress(currentPet.getHunger() / 100.0);
        happinessBar.setProgress(currentPet.getHappiness() / 100.0);
        energyBar.setProgress(currentPet.getEnergy() / 100.0);
        cleanlinessBar.setProgress(currentPet.getCleanliness() / 100.0);
    }

    public static void main(String[] args) { launch(args); }
}
