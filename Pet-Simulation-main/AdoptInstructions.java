package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AdoptInstructions {
    
    public static void show(Stage primaryStage) {
        Stage instructionsStage = new Stage();
        instructionsStage.initStyle(StageStyle.UNDECORATED);
        
        // Create background
        Image bgImage = new Image("file:assets/images/instructionbg.png");
        BackgroundImage background = new BackgroundImage(
            bgImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(100, 100, true, true, true, true)
        );

        // Title bar
        Label title = new Label("How to Adopt - Game Instructions");
        title.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-family: 'Viaoda Libre';" +
            "-fx-text-fill: white;"
        );

        Button closeBtn = new Button("✖");
        closeBtn.setOnAction(e -> instructionsStage.close());
        closeBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #fbfbfbd5;" +
            "-fx-font-size: 18px;"
        );

        HBox titleBar = new HBox(title, closeBtn);
        titleBar.setStyle("-fx-background-color: #60b59ad5;");
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(10, 15, 10, 20));
        titleBar.setPrefHeight(50);
        HBox.setHgrow(title, Priority.ALWAYS);

        // Make window draggable
        final double[] offsetX = new double[1];
        final double[] offsetY = new double[1];
        titleBar.setOnMousePressed(event -> {
            offsetX[0] = event.getSceneX();
            offsetY[0] = event.getSceneY();
        });
        titleBar.setOnMouseDragged(event -> {
            instructionsStage.setX(event.getScreenX() - offsetX[0]);
            instructionsStage.setY(event.getScreenY() - offsetY[0]);
        });

        // Instructions content
        VBox instructionsContent = new VBox(20);
        instructionsContent.setPadding(new Insets(30));
        instructionsContent.setAlignment(Pos.TOP_LEFT);
        
        String[] instructionSteps = {
            "1. Enter your pet's name in the text field",
            "2. Choose between a Dog or Cat by clicking the buttons",
            "3. Click 'Adopt Pet' to complete the adoption",
            "4. You'll be taken to the pet care screen where you can:",
            "   - Feed your pet to keep them healthy",
            "   - Play with your pet to keep them happy",
            "   - Put your pet to sleep when they're tired",
            "5. Monitor your pet's stats (Hunger, Happiness, Energy)",
            "6. Keep all stats balanced for a happy pet!",
            "\nTip: Pay attention to your pet's needs and interact regularly!"
        };
        
        for (String step : instructionSteps) {
            Label stepLabel = new Label(step);
            stepLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Viaoda Libre';" +
                "-fx-font-size: 16px;" +
                "-fx-wrap-text: true;"
            );
            instructionsContent.getChildren().add(stepLabel);
        }

        // Back button
        Button backButton = new Button("Back to Main Menu");
        backButton.setStyle(
            "-fx-background-color: rgba(217, 124, 200, 0.35);" +
            "-fx-background-radius: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Viaoda Libre';" +
            "-fx-font-size: 16px;" +
            "-fx-border-color: #ffffff;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 20px;"
        );
        backButton.setPrefSize(180, 50);
        
        backButton.setOnAction(e -> instructionsStage.close());
        
        VBox mainContent = new VBox(30, instructionsContent, backButton);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(20));

        BorderPane root = new BorderPane();
        root.setStyle("-fx-border-color: white; -fx-border-width: 3px; -fx-border-radius: 10px;");
        root.setTop(titleBar);
        root.setCenter(mainContent);
        root.setBackground(new Background(background));
        root.setPrefSize(800, 600);

        Scene instructionsScene = new Scene(root);
        instructionsStage.setScene(instructionsScene);
        instructionsStage.setResizable(false);
        
        // Position the instructions window relative to main window
        instructionsStage.setX(primaryStage.getX() + 200);
        instructionsStage.setY(primaryStage.getY() + 100);
        
        instructionsStage.show();
    }
}