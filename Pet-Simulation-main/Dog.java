package main;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Interpolator;
import javafx.scene.media.AudioClip;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;

public class Dog extends Pet {

    private static StackPane mainLayer;
    private static Pane interactionLayer; 

    public Dog(String name) {
        super(name);
    }
   
    public static void show(Stage stage, String petName, Scene backScene) {
        Dog dog = new Dog(petName);

        Image bgImage = new Image("file:assets/images/dogcare.png");
        BackgroundImage background = new BackgroundImage(
            bgImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(200, 200, true, true, true, false)
        );

        // --- Back Button
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> stage.setScene(backScene));

         // --- Progress Bars 
        final ProgressBar hungerBar = createStyledBar("#ff7675");      // soft pink
        final ProgressBar happinessBar = createStyledBar("#74b9ff");  // calm blue
        final ProgressBar energyBar = createStyledBar("#55efc4");     // mint
        final ProgressBar cleanlinessBar = createStyledBar("#ffeaa7");// yellow pastel

        //label buttons
        Label hungerLbl = new Label("Satisfaction");
        Label happinessLbl = new Label("Happiness");
        Label energyLbl = new Label("Energy");
        Label cleanlinessLbl = new Label("Cleanliness");

        String labelStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3d2c3e;";
        hungerLbl.setStyle(labelStyle);
        happinessLbl.setStyle(labelStyle);
        energyLbl.setStyle(labelStyle);
        cleanlinessLbl.setStyle(labelStyle);

        HBox statsBox = new HBox(30,
                new VBox(8, hungerLbl, hungerBar),
                new VBox(8, happinessLbl, happinessBar),
                new VBox(8, energyLbl, energyBar),
                new VBox(8, cleanlinessLbl, cleanlinessBar)
        );
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(15, 40, 15, 40));
        statsBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.44); -fx-background-radius: 15;");
        statsBox.setMinWidth(150);

        // --- Top HBox containing Back and Stats ---
        BorderPane topPane = new BorderPane();
        topPane.setLeft(backBtn);
        topPane.setRight(statsBox);
        topPane.setPadding(new Insets(5));

        // --- Dog Image (Center) ---
        Image dogImg = new Image("file:assets/images/sleepdog.png");
        ImageView dogView = new ImageView(dogImg);
        dogView.setFitWidth(250);
        dogView.setTranslateX(-380);
        dogView.setTranslateY(60);
        dogView.setPreserveRatio(true);
        playWakeUpIntro(dogView);   //ANIMATION STARTS FROM HERE
        
        // --- Buttons (Bottom) ---
        Button feedBtn = createFancyButton("Feed", "linear-gradient(to right, #ff9a9e, #fad0c4)");
        Button playBtn = createFancyButton("Play", "linear-gradient(to right, #a1c4fd, #c2e9fb)");
        Button cleanBtn = createFancyButton("Clean", "linear-gradient(to right, #fddb92, #d1fdff)");
        Button restBtn = createFancyButton("Rest", "linear-gradient(to right, #a8edea, #fed6e3)");

        // Disable buttons initially
        feedBtn.setDisable(true);
        playBtn.setDisable(true);
        cleanBtn.setDisable(true);
        restBtn.setDisable(true);
        
        PauseTransition afterIntro = new PauseTransition(Duration.seconds(16));
        afterIntro.setOnFinished(e -> {
            // Step 3 — Start walking animation
            Image dogImg1 = new Image("file:assets/images/dogwalk1.png");
            Image dogImg2 = new Image("file:assets/images/dogwalk2.png");
            Image dogImg3 = new Image("file:assets/images/dogwalk3.png");
            dogView.setImage(new Image("file:assets/images/dogwalk1.png"));

            animateDogWalk(dogView, dogImg1, dogImg2, dogImg3, 2,  0.5, 150); // speed=2px/frame, 300 frames
            ScaleTransition grow = new ScaleTransition(Duration.seconds(5), dogView);
        
            grow.setFromX(1.0);  // start small
            grow.setFromY(1.0);
            grow.setToX(1.2);    // end large (closer)
            grow.setToY(1.2);
            grow.setInterpolator(Interpolator.EASE_BOTH);
            grow.play();

            // Step 4 — Wait for the walk to finish, then lick
            PauseTransition afterWalk = new PauseTransition(Duration.seconds(5)); // match the walk duration
            afterWalk.setOnFinished(ev -> playDogLick(dogView, feedBtn, playBtn, cleanBtn, restBtn));
            afterWalk.play();
        });
        afterIntro.play();
        
        VBox centerBox = new VBox(new Label(""), dogView);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(15);

        feedBtn.setOnAction(e -> {
            String actualAction = "feed"; // Use the actual action directly
            animateDogReaction(dogView, actualAction, dog, hungerBar, happinessBar, energyBar, cleanlinessBar,
                    feedBtn, playBtn, cleanBtn, restBtn);
        });

        playBtn.setOnAction(e -> {
            String actualAction = "play"; // Direct action instead of getPriorityAction()
            animateDogReaction(dogView, actualAction, dog, hungerBar, happinessBar, energyBar, cleanlinessBar,
                    playBtn, feedBtn, cleanBtn, restBtn);
        });

        cleanBtn.setOnAction(e -> {
            String actualAction = "clean"; // Direct action instead of getPriorityAction()
            animateDogReaction(dogView, actualAction, dog, hungerBar, happinessBar, energyBar, cleanlinessBar,
                    cleanBtn, feedBtn, playBtn, restBtn);
        });

        restBtn.setOnAction(e -> {
            String actualAction = "rest"; // Direct action instead of getPriorityAction()
            animateDogReaction(dogView, actualAction, dog, hungerBar, happinessBar, energyBar, cleanlinessBar,
                    restBtn, feedBtn, playBtn, cleanBtn);
        });

        HBox buttonBox = new HBox(35, feedBtn, playBtn, cleanBtn, restBtn);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.setPadding(new Insets(10, 10, 10, 10)); 
        buttonBox.setStyle("-fx-background-color: rgba(255,255,255,0.25); -fx-background-radius: 15;");

        // stretch evenly
        for (Button btn : new Button[]{feedBtn, playBtn, cleanBtn, restBtn}) {
            HBox.setHgrow(btn, Priority.ALWAYS);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setPrefWidth(120); // reduced width (was 130)
            btn.setPrefHeight(40); // slightly smaller height
            HBox.setHgrow(btn, Priority.ALWAYS);
        }
        buttonBox.setSpacing(25); // space between buttons (was 35)
        buttonBox.setMinHeight(60);

        // Emotion Icon - independent floating box
        ImageView emotionView = new ImageView(new Image("file:assets/images/dogicon.png"));
        emotionView.setFitWidth(100);
        emotionView.setPreserveRatio(true);
        emotionView.setImage(new Image("file:assets/images/dogiconheart.png"));

        Label emotionText = new Label("I'm feeling great!");
        emotionText.setStyle("-fx-font-size: 14px; -fx-text-fill: #67434eff; -fx-font-weight: bold;");

        VBox emotionBox = new VBox(emotionView, emotionText);
        emotionBox.setAlignment(Pos.CENTER);
        emotionBox.setSpacing(5);
        emotionBox.setTranslateX(40); // 40 px from left
        emotionBox.setPickOnBounds(false);
        emotionText.setText("Hi! I love You");

        // --- Final Layout using BorderPane ---
        BorderPane root = new BorderPane();
        root.setTop(topPane);
        root.setCenter(centerBox);
        StackPane bottomLayer = new StackPane();
        bottomLayer.getChildren().addAll(buttonBox, emotionBox);
        bottomLayer.setPickOnBounds(false);

        StackPane.setAlignment(buttonBox, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(emotionBox, Pos.BOTTOM_RIGHT);

        // less bottom gap and slightly nudged left
        StackPane.setMargin(buttonBox, new Insets(0, 170, 5, 20));
        StackPane.setMargin(emotionBox, new Insets(0, 0, 0, 940));

        root.setBottom(bottomLayer);
        BorderPane.setMargin(buttonBox, new Insets(0, 0, 5, 0)); // very close to bottom
        root.setPadding(new Insets(20));
        root.setBackground(new Background(background));

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.18);"); // faint translucent background
        overlay.setOpacity(0); // start hidden
        overlay.setPickOnBounds(false);
        overlay.setMouseTransparent(true);

        // Text appears first
        Label welcomeText = new Label("");

        // Dog image appears later
        ImageView introDog = new ImageView(new Image("file:assets/images/dogmeet.png"));
        introDog.setFitWidth(450);
        introDog.setTranslateY(80);
        introDog.setPreserveRatio(true);
        introDog.setOpacity(0);

        // Banner container
        VBox banner = new VBox(15, welcomeText, introDog);
        banner.setAlignment(Pos.CENTER);
        banner.setPadding(new Insets(30, 60, 30, 60));
        banner.setMaxWidth(520);
        banner.setMaxHeight(300);

        // add the banner to the dim overlay
        StackPane bannerWrapper = new StackPane();
        bannerWrapper.setMaxWidth(520);
        bannerWrapper.setMaxHeight(300);

        // Create a separate ImageView as the *true* banner background layer
        ImageView bannerBgImage = new ImageView(new Image("file:assets/images/bannerbg1.png"));
        bannerBgImage.setFitWidth(1200);
        bannerBgImage.setPreserveRatio(true);

        // Place actual banner (with text + dog) above it
        bannerWrapper.getChildren().addAll(bannerBgImage, banner);

        // Center everything inside overlay
        overlay.getChildren().add(bannerWrapper);

        // Place overlay on top of everything
        // interaction layer — transparent pane for temporary clickable overlays (bib, bowl, toys)
        interactionLayer = new Pane();
        interactionLayer.setPickOnBounds(false); // allow clicks to pass through where no node exists
        interactionLayer.setMouseTransparent(true); // allow clicks on nodes we add

        // Build the main stack: root (layout) -> interactionLayer (overlays) -> overlay (intro)
        mainLayer = new StackPane(root, interactionLayer, overlay);

        Scene scene = new Scene(mainLayer, 1200, 800);
        stage.setScene(scene);

        // Animation sequence: dim fade → text → dog → fade away
        FadeTransition overlayFadeIn = new FadeTransition(Duration.seconds(0.8), overlay);
        overlayFadeIn.setFromValue(0);
        overlayFadeIn.setToValue(1);

        FadeTransition textFadeIn = new FadeTransition(Duration.seconds(1), welcomeText);
        textFadeIn.setFromValue(0);
        textFadeIn.setToValue(1);

        PauseTransition pause = new PauseTransition(Duration.seconds(0.7));

        FadeTransition dogFadeIn = new FadeTransition(Duration.seconds(1.2), introDog);
        dogFadeIn.setFromValue(0);
        dogFadeIn.setToValue(1);

        PauseTransition hold = new PauseTransition(Duration.seconds(2.5));

        FadeTransition fadeOutAll = new FadeTransition(Duration.seconds(1.2), overlay);
        fadeOutAll.setFromValue(1);
        fadeOutAll.setToValue(0);

        SequentialTransition sequence = new SequentialTransition(
            overlayFadeIn, textFadeIn, pause, dogFadeIn, hold, fadeOutAll
        );
        sequence.play();
        sequence.setOnFinished(e -> {
            overlay.setVisible(false);
            overlay.setManaged(false);
            overlay.setMouseTransparent(true);
            mainLayer.getChildren().remove(overlay);
            System.out.println("✅ Overlay completely removed");
        });

        // Preload all emotion images (so they don't lag later)
        Image heartImg = new Image("file:assets/images/dogiconheart.png");
        Image happyImg = new Image("file:assets/images/dogiconhappy.png");
        Image sadImg = new Image("file:assets/images/dogiconsad.png");
        Image angryImg = new Image("file:assets/images/dogiconangry.png");
        Image sleepyImg = new Image("file:assets/images/dogiconsleepy.png");
        Image dirtyImg = new Image("file:assets/images/dogicondirty.png");

        // --- Timeline for automatic stat updates ---
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(8), e -> {
            // Decrease all stats over time (slower rate)
            dog.hunger = Math.max(0, dog.getHunger() - 3);        // Reduced from 5 to 3
            dog.cleanliness = Math.max(0, dog.getCleanliness() - 2); // Reduced from 3 to 2
            dog.happiness = Math.max(0, dog.getHappiness() - 1);     // Reduced from 2 to 1
            dog.energy = Math.max(0, dog.getEnergy() - 2);           // Reduced from 3 to 2
            
            updateBars(dog, hungerBar, happinessBar, energyBar, cleanlinessBar);
            autoUpdateEmotion(dog, emotionView, emotionText,heartImg, happyImg, sadImg, angryImg, sleepyImg, dirtyImg);
            
            // Debug output to see what's happening
            System.out.println("📊 Auto-update - Hunger: " + dog.getHunger() + 
                              ", Energy: " + dog.getEnergy() + 
                              ", Happiness: " + dog.getHappiness() + 
                              ", Cleanliness: " + dog.getCleanliness());
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();      
    }

    private static void playDogLick(ImageView dogView, Button feedBtn, Button playBtn, Button cleanBtn, Button restBtn) {
        // Change to licking dog for 3 seconds, then back to sitdog
        dogView.setImage(new Image("file:assets/images/doglick.png"));

        PauseTransition backToSit = new PauseTransition(Duration.seconds(3));
        backToSit.setOnFinished(e -> {
            dogView.setImage(new Image("file:assets/images/sitdog2.png"));
            
            // 🆕 RE-ENABLE BUTTONS after lick animation completes
            feedBtn.setDisable(false);
            playBtn.setDisable(false);
            cleanBtn.setDisable(false);
            restBtn.setDisable(false);
            System.out.println("✅ Initial animation complete - Buttons enabled");
        });
        backToSit.play();
    }

    private static boolean isAnimating = false;
    private static String currentAction = "";

    private static void animateDogReaction(
        ImageView dogView,
        String action,
        Dog dog,
        ProgressBar hungerBar,
        ProgressBar happinessBar,
        ProgressBar energyBar,
        ProgressBar cleanlinessBar,
        Button activeButton,
        Button... otherButtons
    ) {
        System.out.println("🎬 ANIMATE DOG REACTION CALLED - Action: " + action);
        if (isAnimating && currentAction.equals(action)) {
            return;
        }
        
        if (isAnimating) {
            return;
        }
        
        isAnimating = true;
        currentAction = action;

        for (Button b : otherButtons) {
            b.setDisable(true);
        }

        Image originalImage = new Image("file:assets/images/sitdog2.png");
        
        switch (action) {
            case "feed" -> {
    // 🥣 Step 1 — Create the bib
    ImageView bib = new ImageView("file:assets/images/bib1.png");
    bib.setFitWidth(100);
    bib.setPreserveRatio(true);           
    Pane bibContainer = new Pane();
    bibContainer.setPickOnBounds(false);
    bib.setLayoutX(1050);
    bib.setLayoutY(520);
    bibContainer.getChildren().add(bib);
    mainLayer.getChildren().add(bibContainer);
    
    System.out.println("✅ Bib added");

    // 👆 Step 4 — When bib is clicked
    PauseTransition feedingTimeout = new PauseTransition(Duration.seconds(10));

    feedingTimeout.setOnFinished(ev -> {
        // Only check currentAction, remove isAnimating check
        if (currentAction.equals("waitingForFood")) {
            System.out.println("⏳ Dog waited too long for food!");
            
            try {
                Image angryImage = new Image("file:assets/images/dogbibangry.png");
                dogView.setImage(angryImage);
                System.out.println("😾 Dog is angry!");
            } catch (Exception ex) {
                System.out.println("❌ Failed to load angry image");
            }

            // Remove the bowl container
            mainLayer.getChildren().removeIf(node -> {
                if (node instanceof Pane pane) {
                    return !pane.getChildren().isEmpty() && 
                           pane.getChildren().get(0) instanceof ImageView imageView &&
                           imageView.getImage() != null &&
                           imageView.getImage().getUrl().contains("dogbowl");
                }
                return false;
            });

            // Re-enable buttons after a moment
            PauseTransition angryDelay = new PauseTransition(Duration.seconds(2));
            angryDelay.setOnFinished(e2 -> {
                dogView.setImage(originalImage);
                enableOtherButtons(otherButtons);
                isAnimating = false;
                currentAction = "";
            });
            angryDelay.play();
        }
    });
    
    bib.setOnMouseClicked(e -> {
        System.out.println("🎯 Bib clicked!");
        
        dogView.setImage(new Image("file:assets/images/dogbib.png"));
        mainLayer.getChildren().remove(bibContainer);

        // 🍲 Create the bowl
        ImageView bowl = new ImageView("file:assets/images/dogbowl.png");
        bowl.setFitWidth(100);
        bowl.setPreserveRatio(true);
        
        Pane bowlContainer = new Pane();
        bowlContainer.setPickOnBounds(false);
        bowl.setLayoutX(1050);
        bowl.setLayoutY(520);
        bowlContainer.getChildren().add(bowl);
        mainLayer.getChildren().add(bowlContainer);
        
        currentAction = "waitingForFood";
        System.out.println("🍲 Bowl added, starting 10s timeout");
        feedingTimeout.playFromStart();

        // When bowl is clicked - FIXED: Only set one click handler
        bowl.setOnMouseClicked(e2 -> {
            System.out.println("🎯 Bowl clicked - stopping timeout");
            feedingTimeout.stop();
            currentAction = "feed"; 
            isAnimating = true;

            System.out.println("🎯 Bowl clicked!");
            
            int loops = 3;
            Timeline eatingLoop = new Timeline();

            for (int i = 0; i < loops; i++) {
                // dog_eat1
                eatingLoop.getKeyFrames().add(new KeyFrame(
                    Duration.seconds(0.6 * (2 * i)),
                    ev -> dogView.setImage(new Image("file:assets/images/dogeating.png"))
                ));

                // dog_eat2
                eatingLoop.getKeyFrames().add(new KeyFrame(
                    Duration.seconds(0.6 * (2 * i + 1)),
                    ev -> dogView.setImage(new Image("file:assets/images/dogbibbowl.png"))
                ));
            }

            // After looping → reset pose
            eatingLoop.getKeyFrames().add(new KeyFrame(
                Duration.seconds(0.6 * loops * 2),
                ev -> {
                    dogView.setImage(originalImage);
                    mainLayer.getChildren().remove(bowlContainer);

                    // 🆕 SET HUNGER TO 100% AFTER FEEDING COMPLETES
                    dog.hunger = 100; // Set to max after feeding
                    updateBars(dog, hungerBar, happinessBar, energyBar, cleanlinessBar);
                    
                    enableOtherButtons(otherButtons);
                    isAnimating = false;
                    currentAction = "";
                    System.out.println("✅ Eating animation complete - Hunger: 100%");
                }
            ));

            eatingLoop.play();
        });
    });
    
    // Bib timeout (15 seconds) - remove silently
    PauseTransition bibTimeout = new PauseTransition(Duration.seconds(15));
    bibTimeout.setOnFinished(e -> {
        if (mainLayer.getChildren().contains(bibContainer)) {
            System.out.println("⏳ Bib timeout - removing silently");
            mainLayer.getChildren().remove(bibContainer);
            enableOtherButtons(otherButtons);
            isAnimating = false;
            currentAction = "";
        }
    });
    bibTimeout.play();
}
            case "play" -> {
    // Store original position
    double originalX = dogView.getTranslateX();
    double originalY = dogView.getTranslateY();
    
    // 🎾 Step 1 — Create the ball icon
    ImageView ballIcon = new ImageView("file:assets/images/ballicon.png");
    ballIcon.setFitWidth(100);
    ballIcon.setPreserveRatio(true);           
    Pane ballContainer = new Pane();
    ballContainer.setPickOnBounds(false);
    ballIcon.setLayoutX(1050);
    ballIcon.setLayoutY(520);
    ballContainer.getChildren().add(ballIcon);
    mainLayer.getChildren().add(ballContainer);
    
    System.out.println("✅ Ball icon added");

    ballIcon.setOnMouseClicked(e -> {
        System.out.println("🎯 Ball icon clicked!");
        
        mainLayer.getChildren().remove(ballContainer);

        // 🎾 Step 2: Generate actual ball at target coordinates
        ImageView ball = new ImageView("file:assets/images/ball.png");
        ball.setFitWidth(80);
        ball.setPreserveRatio(true);
        ball.setTranslateX(400);  // Target coordinates on right side
        ball.setTranslateY(100);
        ball.setOpacity(0);
        
        mainLayer.getChildren().add(ball);
        
        // Fade in the actual ball
        FadeTransition ballFadeIn = new FadeTransition(Duration.seconds(1), ball);
        ballFadeIn.setFromValue(0);
        ballFadeIn.setToValue(1);
        
        ballFadeIn.setOnFinished(ballEv -> {
            // 🐕 Step 3: Dog walks to the actual ball
            final Image walk1 = new Image("file:assets/images/dogwalk1.png");
            final Image walk2 = new Image("file:assets/images/dogwalk2.png");
            final Image walk3 = new Image("file:assets/images/dogwalk3.png");
            
            dogView.setImage(walk1);
            
            // Calculate walking path to actual ball
            double ballX = 400;  // Ball position
            double ballY = 100;
            double currentX = dogView.getTranslateX();
            double currentY = dogView.getTranslateY();
            
            double distanceX = ballX - currentX;
            double distanceY = ballY - currentY;
            double walkDuration = 5.0;
            double speedX = distanceX / (walkDuration * 16.67);
            double speedY = distanceY / (walkDuration * 16.67);
            
            animateDogWalk(dogView, walk1, walk2, walk3, speedX, speedY, (int)(walkDuration * 16.67));
            
            PauseTransition walkToBall = new PauseTransition(Duration.seconds(walkDuration));
            walkToBall.setOnFinished(ev -> {
                // 🎾 Step 4: Dog plays with ball - remove ball and change image
                mainLayer.getChildren().remove(ball);
                dogView.setImage(new Image("file:assets/images/dogwithball.png"));
                
                // Play with ball for 5 seconds
                PauseTransition playTime = new PauseTransition(Duration.seconds(3));
                playTime.setOnFinished(playEv -> {
                    // 🏠 Step 5: Dog walks back to center with walk4,5,6
                    final Image walk4 = new Image("file:assets/images/dogwalk4.png");
                    final Image walk5 = new Image("file:assets/images/dogwalk5.png");
                    final Image walk6 = new Image("file:assets/images/dogwalk6.png");
                    
                    dogView.setImage(walk4);
                    
                    // Walk back to original position
                    double returnDistanceX = originalX - ballX;
                    double returnDistanceY = originalY - ballY;
                    double returnDuration = 5.0;
                    double returnSpeedX = returnDistanceX / (returnDuration * 16.67);
                    double returnSpeedY = returnDistanceY / (returnDuration * 16.67);
                    
                    animateDogWalk(dogView, walk4, walk5, walk6, returnSpeedX, returnSpeedY, (int)(returnDuration * 16.67));
                    
                    PauseTransition returnWalk = new PauseTransition(Duration.seconds(returnDuration));
                    returnWalk.setOnFinished(returnEv -> {
                        // Step 6: Return to sitting position
                        dogView.setImage(originalImage);
                        
                        // 🆕 SET HAPPINESS TO 100% AFTER PLAY COMPLETES
                        dog.happiness = 100; // Set to max after playing
                        updateBars(dog, hungerBar, happinessBar, energyBar, cleanlinessBar);
                        
                        enableOtherButtons(otherButtons);
                        isAnimating = false;
                        currentAction = "";
                        System.out.println("🎾 Play complete! Happiness: 100%");
                    });
                    returnWalk.play();
                });
                playTime.play();
            });
            walkToBall.play();
        });
        
        ballFadeIn.play();
    });
    
    // Ball icon timeout (15 seconds) - remove silently
    PauseTransition ballTimeout = new PauseTransition(Duration.seconds(15));
    ballTimeout.setOnFinished(e -> {
        if (mainLayer.getChildren().contains(ballContainer)) {
            System.out.println("⏳ Ball icon timeout - removing silently");
            mainLayer.getChildren().remove(ballContainer);
            enableOtherButtons(otherButtons);
            isAnimating = false;
            currentAction = "";
        }
    });
    ballTimeout.play();
}
            case "clean" -> {
                isAnimating = true;
                currentAction = "clean";
                
                // Store original background and position
                String originalBG = "file:assets/images/dogcare.png";
                double originalX = dogView.getTranslateX();
                double originalY = dogView.getTranslateY();
                
                BorderPane root = (BorderPane) mainLayer.getChildren().get(0);
                
                // Step 1: Dog walks out to the right
                final Image walk1 = new Image("file:assets/images/dogwalk1.png");
                final Image walk2 = new Image("file:assets/images/dogwalk2.png");
                final Image walk3 = new Image("file:assets/images/dogwalk3.png");
                
                // Walk off-screen to the right
                double exitDuration = 10.0;
                animateDogWalk(dogView, walk1, walk2, walk3, 2, 0.75, (int)(exitDuration * 33.33));
                
                PauseTransition exitDelay = new PauseTransition(Duration.seconds(exitDuration));
                exitDelay.setOnFinished(e -> {
                    // Step 2: Switch to bath scene
                    Image bathBG = new Image("file:assets/images/dogcarebath.png");
                    BackgroundImage bathBackground = new BackgroundImage(
                        bathBG,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true)
                    );
                    root.setBackground(new Background(bathBackground));
                    
                    // Reset dog position to left side for entrance
                    dogView.setTranslateX(-600);
                    dogView.setTranslateY(originalY);
                    animateDogWalk(dogView, walk1, walk2, walk3, 1.1, 0.3, (int)(15 * 33.33));
                    
                    // Step 3: Show rubber ducky icon/button
                    ImageView rubberDucky = new ImageView(new Image("file:assets/images/duckbutton.png"));
                    rubberDucky.setFitWidth(100);
                    rubberDucky.setPreserveRatio(true);
                    rubberDucky.setTranslateX(1050);
                    rubberDucky.setTranslateY(550);
                    rubberDucky.setCursor(javafx.scene.Cursor.HAND);
                    
                    // Add duck to interaction layer
                    interactionLayer.getChildren().add(rubberDucky);
                    interactionLayer.setMouseTransparent(false);
                    
                    // Step 4: Wait for duck click
                    rubberDucky.setOnMouseClicked(duckClick -> {
                        interactionLayer.getChildren().remove(rubberDucky);
                        interactionLayer.setMouseTransparent(true);
                        
                        // Show rubber duck in tub
                        ImageView duckInTub = new ImageView(new Image("file:assets/images/duck.png"));
                        duckInTub.setFitWidth(60);
                        duckInTub.setPreserveRatio(true);
                        duckInTub.setTranslateX(-180);
                        duckInTub.setTranslateY(60);
                        mainLayer.getChildren().add(duckInTub);

                        PauseTransition bathEntranceDelay = new PauseTransition(Duration.seconds(2));
                        bathEntranceDelay.setOnFinished(ev -> {
                            // Step 6: Dog goes into bath tub
                            dogView.setImage(new Image("file:assets/images/dogbath.png"));
                            dogView.setTranslateY(dogView.getTranslateY()-240);
                            dogView.setTranslateX(dogView.getTranslateX()+100);
                            
                            // Step 7: Up-down bobbing animation in water
                            TranslateTransition bobAnimation = new TranslateTransition(Duration.seconds(1), dogView);
                            bobAnimation.setByY(-10);
                            bobAnimation.setAutoReverse(true);
                            bobAnimation.setCycleCount(6);
                            
                            PauseTransition bobDelay = new PauseTransition(Duration.seconds(1));
                            bobDelay.setOnFinished(bobStart -> {
                                bobAnimation.play();
                            });
                            
                            bobAnimation.setOnFinished(bobEnd -> {
                                // Step 8: Dog comes out with towel and shakes
                                dogView.setImage(new Image("file:assets/images/dogtowel.png"));
                                dogView.setTranslateY(dogView.getTranslateY() + 240);
                                dogView.setTranslateX(dogView.getTranslateX()-130);

                                // Remove duck from tub
                                mainLayer.getChildren().remove(duckInTub);

                                // Shaking sequence
                                PauseTransition firstTowelPause = new PauseTransition(Duration.seconds(1.5));
                                firstTowelPause.setOnFinished(shake1 -> {
                                    AudioClip shakeSound = new AudioClip(new File("assets/audio/shake.mp3").toURI().toString());
                                    shakeSound.play();
                                    
                                    PauseTransition shakePause = new PauseTransition(Duration.seconds(0.4));
                                    shakePause.setOnFinished(backToTowel1 -> {
                                        dogView.setImage(new Image("file:assets/images/dogtowel.png"));
                                        
                                        PauseTransition betweenShakes = new PauseTransition(Duration.seconds(1.8));
                                        betweenShakes.setOnFinished(shake2 -> {
                                            dogView.setImage(new Image("file:assets/images/dogshake.png"));
                                            
                                            PauseTransition finalShakePause = new PauseTransition(Duration.seconds(0.8));
                                            finalShakePause.setOnFinished(backToTowel2 -> {
                                                dogView.setImage(new Image("file:assets/images/dogtowel.png"));
                                                
                                                PauseTransition towelTime = new PauseTransition(Duration.seconds(1.5));
                                                towelTime.setOnFinished(towelEnd -> {
                                                    // Step 10: Dog walks back to left
                                                    final Image exitWalk1 = new Image("file:assets/images/dogwalk4.png");
                                                    final Image exitWalk2 = new Image("file:assets/images/dogwalk5.png");
                                                    final Image exitWalk3 = new Image("file:assets/images/dogwalk6.png");
                                                    
                                                    double leaveDuration = 10.0;
                                                    animateDogWalk(dogView, exitWalk1, exitWalk2, exitWalk3, -2, 0, (int)(leaveDuration * 33.33));
                                                    
                                                    PauseTransition leaveDelay = new PauseTransition(Duration.seconds(leaveDuration));
                                                    leaveDelay.setOnFinished(leaveEnd -> {
                                                        // Switch back to home screen
                                                        Image originalBGImage = new Image(originalBG);
                                                        BackgroundImage originalBackground = new BackgroundImage(
                                                            originalBGImage,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundPosition.CENTER,
                                                            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true)
                                                        );
                                                        root.setBackground(new Background(originalBackground));
                                                        
                                                        // Return dog to original position
                                                        dogView.setTranslateX(originalX);
                                                        dogView.setTranslateY(originalY);
                                                        dogView.setImage(new Image("file:assets/images/sitdog2.png"));
                                                        
                                                        // 🆕 SET CLEANLINESS TO 100% AFTER CLEANING COMPLETES
                                                        dog.cleanliness = 100; // Set to max after cleaning
                                                        updateBars(dog, hungerBar, happinessBar, energyBar, cleanlinessBar);
                                                        
                                                        enableOtherButtons(otherButtons);
                                                        isAnimating = false;
                                                        currentAction = "";
                                                        System.out.println("🛁 Clean complete! Cleanliness: 100%");
                                                    });
                                                    leaveDelay.play();
                                                });
                                                towelTime.play();
                                            });
                                            finalShakePause.play();
                                        });
                                        betweenShakes.play();
                                    });
                                    shakePause.play();
                                });
                                firstTowelPause.play();
                            });
                            bobDelay.play();
                        });
                        bathEntranceDelay.play();
                    });
                });
                exitDelay.play();
            }
            
            case "rest" -> {
                isAnimating = true;
                currentAction = "rest";
                
                // Store original background and position
                String originalBG = "file:assets/images/dogcare.png";
                double originalX = dogView.getTranslateX();
                double originalY = dogView.getTranslateY();
                
                // Change to nighttime background
                Image nightBG = new Image("file:assets/images/dogcarenight.png");
                BackgroundImage nightBackground = new BackgroundImage(
                    nightBG,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true)
                );
                
                BorderPane root = (BorderPane) mainLayer.getChildren().get(0);
                root.setBackground(new Background(nightBackground));
                
                // Load walking animation images - make them final
                final Image walk1 = new Image("file:assets/images/dogwalk4.png");
                final Image walk2 = new Image("file:assets/images/dogwalk5.png");
                final Image walk3 = new Image("file:assets/images/dogwalk6.png");
                
                // Set initial walking image BEFORE animation starts
                dogView.setImage(walk1);
                
                // Walk to bed - adjust these values based on your layout
                double targetX = -370;  // Adjust based on where the bed is
                double targetY = 70;   // Adjust based on where the bed is
                double distanceX = targetX - originalX;
                double distanceY = targetY - originalY;
                
                // Calculate walk duration based on distance
                double walkDuration = 3.0;
                double speedX = distanceX / (walkDuration * 16.67); // 16.67 frames per second for 3 seconds
                double speedY = distanceY / (walkDuration * 16.67);
                
                // Start walking animation
                animateDogWalkToBed(dogView, walk1, walk2, walk3, speedX, speedY, (int)(walkDuration * 16.67));
                
                // Sleep sequence after walk
                PauseTransition walkDelay = new PauseTransition(Duration.seconds(walkDuration));
                walkDelay.setOnFinished(e -> {
                    // Switch to sleep dog image
                    dogView.setImage(new Image("file:assets/images/sleepdog.png"));
                    
                    // Sleep for 10 seconds
                    PauseTransition sleep = new PauseTransition(Duration.seconds(10));
                    
                    sleep.setOnFinished(ev -> {
                        // 🆕 SET ENERGY TO 100% AFTER REST COMPLETES
                        dog.energy = 100; // Set to max after rest
                        updateBars(dog, hungerBar, happinessBar, energyBar, cleanlinessBar);
                        
                        // Change background to DAY first, THEN dog walks back
                        Image originalBGImage = new Image(originalBG);
                        BackgroundImage originalBackground = new BackgroundImage(
                            originalBGImage,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER,
                            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true)
                        );
                        root.setBackground(new Background(originalBackground));
                        
                        // Walk back to original position - make images final
                        final Image rwalk1 = new Image("file:assets/images/dogwalk1.png");
                        final Image rwalk2 = new Image("file:assets/images/dogwalk2.png");
                        final Image rwalk3 = new Image("file:assets/images/dogwalk3.png");
                        
                        dogView.setImage(rwalk1);
                        animateDogWalk(dogView, rwalk1, rwalk2, rwalk3, -speedX, -speedY, (int)(walkDuration * 16.67));
                        
                        PauseTransition returnDelay = new PauseTransition(Duration.seconds(walkDuration));
                        returnDelay.setOnFinished(returnEv -> {
                            // Return to sitting position
                            dogView.setImage(originalImage);
                            dogView.setTranslateX(originalX);
                            dogView.setTranslateY(originalY);
                            
                            enableOtherButtons(otherButtons);
                            isAnimating = false;
                            currentAction = "";
                            System.out.println("😴 Rest complete! Energy: 100%");
                        });
                        returnDelay.play();
                    });
                    
                    sleep.play();
                });
                
                walkDelay.play();
            }
        }
    }

    private static void enableOtherButtons(Button... otherButtons) {
        for (Button b : otherButtons) {
            b.setDisable(false);
        }
    }

    private static void animateDogWalk(ImageView dogView, Image dog1, Image dog2, Image dog3, double speedx, double speedy, int steps) {
        // --- Alternate between 3 walking poses ---
        Timeline walkCycle = new Timeline(
            new KeyFrame(Duration.seconds(0.3), e -> dogView.setImage(dog1)),
            new KeyFrame(Duration.seconds(0.6), e -> dogView.setImage(dog2)),
            new KeyFrame(Duration.seconds(0.9), e -> dogView.setImage(dog3))
        );
        walkCycle.setCycleCount(Timeline.INDEFINITE);
        walkCycle.play();

        // --- Move forward gradually ---
        Timeline moveForward = new Timeline(
            new KeyFrame(Duration.millis(30), e -> {
                dogView.setTranslateX(dogView.getTranslateX() + speedx);
                dogView.setTranslateY(dogView.getTranslateY() + speedy);
            })
        );
        moveForward.setCycleCount(steps);

        // --- Stop walking & return to sit pose ---
        moveForward.setOnFinished(e -> {
            walkCycle.stop();
            dogView.setImage(new Image("file:assets/images/sitdog2.png"));
        });

        moveForward.play();
    }

    private static void playWakeUpIntro(ImageView dogView) {
        // Step 1: Start asleep
        dogView.setImage(new Image("file:assets/images/sleepdog.png"));
        dogView.setSmooth(true);
        dogView.setCache(true);
        dogView.setOpacity(1);

        // Step 2: Wait for 10 seconds, then open eyes
        PauseTransition firstPause = new PauseTransition(Duration.seconds(10));
        firstPause.setOnFinished(e -> {
            dogView.setImage(new Image("file:assets/images/awakedog.png"));

            // Step 3: After 1 second, sit up
            PauseTransition secondPause = new PauseTransition(Duration.seconds(1));
            secondPause.setOnFinished(ev -> {
                // Slight scale and sit pose
                dogView.setScaleX(1.3);
                dogView.setScaleY(1.3);
                dogView.setTranslateY(3);
                dogView.setImage(new Image("file:assets/images/sitdog.png"));

                // Step 4: Bark after a short delay
                PauseTransition barkPause = new PauseTransition(Duration.seconds(0.8));
                barkPause.setOnFinished(done -> {
                    AudioClip barkSound = new AudioClip(new File("assets/audio/dog-bark.mp3").toURI().toString());
                    barkSound.play();
                    dogView.setImage(new Image("file:assets/images/sitdogbark.png"));

                    // Step 5: Return to sitdog after barking
                    PauseTransition backToSit = new PauseTransition(Duration.seconds(1.2));
                    backToSit.setOnFinished(end -> dogView.setImage(new Image("file:assets/images/sitdog.png")));
                    backToSit.play();
                });
                barkPause.play();
            });
            secondPause.play();
        });

        firstPause.play();
    }

    private static String getPriorityAction(Dog dog) {
        double hunger = dog.getHunger();
        double happiness = dog.getHappiness();
        double energy = dog.getEnergy();
        double cleanliness = dog.getCleanliness();

        // Step 1: If any stat is critically low, fix that first
        if (hunger < 20) return "feed";
        if (energy < 20) return "rest";
        if (cleanliness < 20) return "clean";
        if (happiness < 20) return "play";

        return "sit";
    }

    private static void updateBars(Dog dog, ProgressBar satisfaction, ProgressBar happiness, ProgressBar energy, ProgressBar cleanliness) {
        // --- Convert all values safely ---
        double sat = safeProgress(dog.getHunger() / 100.0);
        double happy = safeProgress(dog.getHappiness() / 100.0);
        double ener = safeProgress(dog.getEnergy() / 100.0);
        double clean = safeProgress(dog.getCleanliness() / 100.0);

        // --- Apply them safely ---
        satisfaction.setProgress(sat);
        happiness.setProgress(happy);
        energy.setProgress(ener);
        cleanliness.setProgress(clean);

        // --- Optional: dynamic colors ---
        satisfaction.setStyle("-fx-accent:" + getDynamicColor((int)(sat * 100)) + ";");
        happiness.setStyle("-fx-accent:" + getDynamicColor(dog.getHappiness()) + ";");
        energy.setStyle("-fx-accent:" + getDynamicColor(dog.getEnergy()) + ";");
        cleanliness.setStyle("-fx-accent:" + getDynamicColor(dog.getCleanliness()) + ";");
    }
    
    private static void animateDogWalkToBed(ImageView dogView, Image dog1, Image dog2, Image dog3, double speedX, double speedY, int steps) {
        // Walking animation cycle
        Timeline walkCycle = new Timeline(
            new KeyFrame(Duration.seconds(0.3), e -> dogView.setImage(dog1)),
            new KeyFrame(Duration.seconds(0.6), e -> dogView.setImage(dog2)),
            new KeyFrame(Duration.seconds(0.9), e -> dogView.setImage(dog3))
        );
        walkCycle.setCycleCount(Timeline.INDEFINITE);
        walkCycle.play();

        // Movement timeline
        Timeline moveTimeline = new Timeline(
            new KeyFrame(Duration.millis(60), e -> {
                dogView.setTranslateX(dogView.getTranslateX() + speedX);
                dogView.setTranslateY(dogView.getTranslateY() + speedY);
            })
        );
        moveTimeline.setCycleCount(steps);

        // Stop walking animation when movement completes
        moveTimeline.setOnFinished(e -> {
            walkCycle.stop();
        });

        moveTimeline.play();
    }

    private static void autoUpdateEmotion(
        Dog dog, ImageView emotionView, Label emotionText,
        Image heartImg, Image happyImg, Image sadImg,
        Image angryImg, Image sleepyImg, Image dirtyImg) {

        Image newImage = heartImg; // default
        String message = "Hi! I love You 💖";

        if (dog.getCleanliness() < 30) {
                newImage = dirtyImg;
                message = "Oh no, I feel so dirty!";
        } 
        else if (dog.getHunger() < 40) {
                newImage = angryImg;
                message = "I'm starving...feed me please!";
        } 
        else if (dog.getEnergy() < 30) {
                newImage = sleepyImg;
                message = "Yaaawn... I need some rest!";
        } 
        else if (dog.getHappiness() < 40) {
                newImage = sadImg;
                message = "I'm feeling lonely...";
        } 
        else if (dog.getHappiness() > 70 && dog.getEnergy() > 50 && dog.getHunger() > 40) {
                newImage = happyImg;
                message = "I'm perfectly happy!";
        }

        // Instant update — no disk delay
        emotionView.setImage(newImage);
        emotionText.setText(message);
    }

    private static double safeProgress(double value) {
        if (Double.isNaN(value)) return 0; 
        return Math.max(0, Math.min(1, value));
    }

    private static String getDynamicColor(int value) {
        if (value < 30) return "#ff8b94";     // red (low)
        if (value < 60) return "#ffeaa7";     // yellow (medium)
        return "#a8e6cf";                     // green (good)
    }

    private static ProgressBar createStyledBar(String color) {
        ProgressBar bar = new ProgressBar(0.5);
        bar.setPrefWidth(130);
        bar.setPrefHeight(20);
        bar.setStyle(
            "-fx-accent: " + color + ";" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-control-inner-background: rgba(255,255,255,0.6);" +
            "-fx-border-color: rgba(0,0,0,0.1);" +
            "-fx-border-width: 1;"
        );
        return bar;
    }

    private static Button createFancyButton(String text, String gradient) {
        Button btn = new Button(text);
        btn.setPrefWidth(130);
        btn.setPrefHeight(45);
        btn.setMinHeight(45);
        btn.setMinWidth(130);
        btn.setStyle(
            "-fx-background-radius: 12;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #2c3e50;" +
            "-fx-background-color: " + gradient + ";" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 3);"
        );

        // Hover effects ✨
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-radius: 12;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #2c3e50;" +
            "-fx-background-color: " + gradient + ";" +
            "-fx-effect: dropshadow(three-pass-box, rgba(255,182,193,0.8), 15, 0, 0, 5);"
        ));

        return btn;
    }

}
