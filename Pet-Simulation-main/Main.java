package main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.text.Font;

public class Main extends Application {

    private String selectedPet = null; 

    @Override
    public void start(Stage stage) {
        Font.loadFont(getClass().getResourceAsStream("assets/fonts/ViaodaLibre-Regular.ttf"), 20);

        Image bgImage = new Image("file:assets/images/background.png");
        BackgroundImage background = new BackgroundImage(
            bgImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(200, 200, true, true, true, false)
        );

        stage.initStyle(StageStyle.UNDECORATED);

        
        Label title = new Label("Paws & Tails");
        title.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-family: 'Viaoda Libre';" +
            "-fx-text-fill: white;"
        );

        
        Button minimizeBtn = new Button("—");
        minimizeBtn.setOnAction(e -> stage.setIconified(true));
        minimizeBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #fbfbfbd5;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;"
        );

        Button closeBtn = new Button("✖");
        closeBtn.setOnAction(e -> stage.close());
        closeBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #fbfbfbd5;" +
            "-fx-font-size: 18px;"
        );

        HBox controls = new HBox(10, minimizeBtn, closeBtn);
        controls.setAlignment(Pos.CENTER_RIGHT);

       
        HBox titleBar = new HBox(title, controls);
        titleBar.setStyle("-fx-background-color: #60b59ad5;");
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(10, 15, 10, 20));
        titleBar.setPrefHeight(50);
        HBox.setHgrow(title, Priority.ALWAYS);

        
        final double[] offsetX = new double[1];
        final double[] offsetY = new double[1];
        titleBar.setOnMousePressed(event -> {
            offsetX[0] = event.getSceneX();
            offsetY[0] = event.getSceneY();
        });
        titleBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - offsetX[0]);
            stage.setY(event.getScreenY() - offsetY[0]);
        });

        
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your pet's name");
        nameField.setPrefSize(350, 50);
        nameField.setMaxWidth(350);
        nameField.setMinHeight(50);
        nameField.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.8);" +
            "-fx-border-radius: 25px;" +
            "-fx-background-radius: 25px;" +
            "-fx-border-color: #60b59a;" +
            "-fx-border-width: 2px;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-size: 16px;" +
            "-fx-prompt-text-fill: #666666;" +
            "-fx-font-family: 'Viaoda Libre';" +
            "-fx-alignment: center;"
        );

        Button dogBtn = createPetButton("Dog");
        Button catBtn = createPetButton("Cat");

        HBox petButtons = new HBox(20, dogBtn, catBtn);
        petButtons.setAlignment(Pos.CENTER);

        Label status = new Label();
        status.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-family: 'Viaoda Libre'; -fx-font-size: 16px;");

        
        Button adoptButton = new Button("Adopt Pet");
        adoptButton.setStyle(
            "-fx-background-color: rgba(217, 124, 200, 0.35);" +
            "-fx-background-radius: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Viaoda Libre';" +
            "-fx-font-size: 18px;" +
            "-fx-border-color: #ffffff;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 20px;"
        );
        adoptButton.setPrefSize(200, 60);
        
        
        adoptButton.setOnMouseEntered(e -> {
            adoptButton.setStyle(
                "-fx-background-color: rgba(217, 124, 200, 0.5);" +
                "-fx-background-radius: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Viaoda Libre';" +
                "-fx-font-size: 18px;" +
                "-fx-border-color: #ffffff;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 20px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 4);"
            );
        });
        
        adoptButton.setOnMouseExited(e -> {
            adoptButton.setStyle(
                "-fx-background-color: rgba(217, 124, 200, 0.35);" +
                "-fx-background-radius: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Viaoda Libre';" +
                "-fx-font-size: 18px;" +
                "-fx-border-color: #ffffff;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 20px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 3);"
            );
        });

        // Create How to Adopt button
        Button howToAdoptButton = new Button("How to Adopt");
        howToAdoptButton.setStyle(
            "-fx-background-color: rgba(96, 181, 154, 0.35);" +
            "-fx-background-radius: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Viaoda Libre';" +
            "-fx-font-size: 16px;" +
            "-fx-border-color: #ffffff;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 20px;"
        );
        howToAdoptButton.setPrefSize(150, 40);
        
        howToAdoptButton.setOnMouseEntered(e -> {
            howToAdoptButton.setStyle(
                "-fx-background-color: rgba(96, 181, 154, 0.5);" +
                "-fx-background-radius: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Viaoda Libre';" +
                "-fx-font-size: 16px;" +
                "-fx-border-color: #ffffff;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 20px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 4);"
            );
        });
        
        howToAdoptButton.setOnMouseExited(e -> {
            howToAdoptButton.setStyle(
                "-fx-background-color: rgba(96, 181, 154, 0.35);" +
                "-fx-background-radius: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Viaoda Libre';" +
                "-fx-font-size: 16px;" +
                "-fx-border-color: #ffffff;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 20px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 3);"
            );
        });

        VBox contentLayout = new VBox(30, nameField, petButtons, adoptButton, howToAdoptButton, status);
        contentLayout.setAlignment(Pos.TOP_CENTER);
        contentLayout.setPadding(new Insets(40, 20, 20, 20));
        
        
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(titleBar);
        mainLayout.setCenter(contentLayout);
        mainLayout.setBackground(new Background(background));
        mainLayout.setPrefSize(1200, 800);
        
        Scene scene = new Scene(mainLayout);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        adoptButton.setOnAction(e -> {
            String petName = nameField.getText().trim();
            if (petName.isEmpty()) {
                status.setText("Please enter your pet's name!");
            } else if (selectedPet == null) {
                status.setText("Please select a pet!");
            } else {
                status.setText("Yay! You adopted " + petName + " the " + selectedPet + "!");
                PetCareScene.show(stage, selectedPet, petName, scene);
            }
        });

        // How to Adopt button action
        howToAdoptButton.setOnAction(e -> {
            AdoptInstructions.show(stage);
        });

        dogBtn.setOnAction(e -> {
            selectedPet = "Dog";
            status.setText("Dog selected!");
        });
        catBtn.setOnAction(e -> {
            selectedPet = "Cat";
            status.setText("Cat selected!");
        });
    }

    private Button createPetButton(String petName) {
        Button button = new Button(petName);
        button.setStyle(
            "-fx-background-color: rgba(217, 124, 200, 0.35);" +
            "-fx-background-radius: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Viaoda Libre';" +
            "-fx-font-size: 18px;" +
            "-fx-border-color: #ffffff;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 20px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 3);"
        );
        button.setPrefSize(180, 60);
        
        
        button.setOnMouseEntered(e -> {
            button.setStyle(
                "-fx-background-color: rgba(217, 124, 200, 0.5);" +
                "-fx-background-radius: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Viaoda Libre';" +
                "-fx-font-size: 18px;" +
                "-fx-border-color: #ffffff;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 20px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 4);"
            );
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: rgba(217, 124, 200, 0.35);" +
                "-fx-background-radius: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Viaoda Libre';" +
                "-fx-font-size: 18px;" +
                "-fx-border-color: #ffffff;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 20px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 3);"
            );
        });
        
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}