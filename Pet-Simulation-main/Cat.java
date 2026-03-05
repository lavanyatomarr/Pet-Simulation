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
import javafx.animation.Interpolator;
import javafx.scene.media.AudioClip;   // For playing sound
import java.io.File;                   // For handling file paths
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;


public class Cat extends Pet {

    private static StackPane mainLayer;
    private static Pane interactionLayer; 

    public Cat(String name) {
        super(name);
    }
   
    public static void show(Stage stage, String petName, Scene backScene) {
        Cat cat = new Cat(petName);

        Image bgImage = new Image("file:assets/images/catcare.png");
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

        Button feedBtn = createFancyButton("Feed", "linear-gradient(to right, #ff9a9e, #fad0c4)");
        Button playBtn = createFancyButton("Play", "linear-gradient(to right, #a1c4fd, #c2e9fb)");
        Button cleanBtn = createFancyButton("Clean", "linear-gradient(to right, #fddb92, #d1fdff)");
        Button restBtn = createFancyButton("Rest", "linear-gradient(to right, #a8edea, #fed6e3)");

        feedBtn.setDisable(true);
        playBtn.setDisable(true);
        cleanBtn.setDisable(true);
        restBtn.setDisable(true);

        // --- Top HBox containing Back and Stats ---
        BorderPane topPane = new BorderPane();
        topPane.setLeft(backBtn);
        topPane.setRight(statsBox);
        topPane.setPadding(new Insets(5));

        // --- Cat Image (Center) ---
        Image catImg = new Image("file:assets/images/sleepcat.png");
        ImageView catView = new ImageView(catImg);
        catView.setFitWidth(250);
        catView.setTranslateX(-380);
        catView.setTranslateY(60);
        catView.setPreserveRatio(true);
        playWakeUpIntro(catView);   //ANIMATION STARTS FROM HERE   //ANIMATION STARTS FROM HERE
        
        PauseTransition afterIntro = new PauseTransition(Duration.seconds(16));
        afterIntro.setOnFinished(e -> {
            // 🐾 Step 3 — Start walking animation
            Image catImg1 = new Image("file:assets/images/catwalk1.png");
            Image catImg2 = new Image("file:assets/images/catwalk2.png");
            Image catImg3 = new Image("file:assets/images/catwalk3.png");
            catView.setImage(new Image("file:assets/images/catwalk1.png"));

            animateCatWalk(catView, catImg1, catImg2, catImg3, 2,  0.5, 150); // speed=2px/frame, 300 frames
            ScaleTransition grow = new ScaleTransition(Duration.seconds(5), catView);
        
            grow.setFromX(1.0);  // start small
            grow.setFromY(1.0);
            grow.setToX(1.2);    // end large (closer)
            grow.setToY(1.2);
            grow.setInterpolator(Interpolator.EASE_BOTH);
            grow.play();

            // 🕐 Step 4 — Wait for the walk to finish, then lick
            PauseTransition afterWalk = new PauseTransition(Duration.seconds(5)); // match the walk duration
            afterWalk.setOnFinished(ev -> playCatLick(catView, feedBtn,  playBtn, cleanBtn, restBtn));
            afterWalk.play();
        });
        afterIntro.play();
        
        VBox centerBox = new VBox(new Label(""), catView);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(15);

        // --- Buttons (Bottom) ---
        

        feedBtn.setOnAction(e -> {
            String actualAction = "feed"; // Use the actual action directly
            cat.feed();
            updateBars(cat, hungerBar, happinessBar, energyBar, cleanlinessBar);
            animateCatReaction(catView, actualAction, cat, hungerBar, happinessBar, energyBar, cleanlinessBar,
                    feedBtn, playBtn, cleanBtn, restBtn);
        });

        playBtn.setOnAction(e -> {
            String actualAction = "play"; // 🆕 CHANGED: Direct action instead of getPriorityAction()
            cat.play();
            updateBars(cat, hungerBar, happinessBar, energyBar, cleanlinessBar);
            animateCatReaction(catView, actualAction, cat, hungerBar, happinessBar, energyBar, cleanlinessBar,
                    playBtn, feedBtn, cleanBtn, restBtn);
        });

        cleanBtn.setOnAction(e -> {
            String actualAction = "clean"; // 🆕 CHANGED: Direct action instead of getPriorityAction()
            cat.clean();
            updateBars(cat, hungerBar, happinessBar, energyBar, cleanlinessBar);
            animateCatReaction(catView, actualAction, cat, hungerBar, happinessBar, energyBar, cleanlinessBar,
                    cleanBtn, feedBtn, playBtn, restBtn);
        });

        restBtn.setOnAction(e -> {
            String actualAction = "rest"; // 🆕 CHANGED: Direct action instead of getPriorityAction()
            cat.rest();
            updateBars(cat, hungerBar, happinessBar, energyBar, cleanlinessBar);
            animateCatReaction(catView, actualAction, cat, hungerBar, happinessBar, energyBar, cleanlinessBar,
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

        // 🐱 Emotion Icon - independent floating box
        ImageView emotionView = new ImageView(new Image("file:assets/images/caticon.png"));
        emotionView.setFitWidth(100);
        emotionView.setPreserveRatio(true);
        emotionView.setImage(new Image("file:assets/images/caticonheart.png"));

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

        // ✨ Text appears first
        Label welcomeText = new Label("");

        // 🐱 Cat image appears later
        ImageView introCat = new ImageView(new Image("file:assets/images/catmeet.png"));
        introCat.setFitWidth(450);
        introCat.setTranslateY(80);
        introCat.setPreserveRatio(true);
        introCat.setOpacity(0);

        // 🌸 Banner container
        VBox banner = new VBox(15, welcomeText, introCat);
        banner.setAlignment(Pos.CENTER);
        banner.setPadding(new Insets(30, 60, 30, 60));
        banner.setMaxWidth(520);
        banner.setMaxHeight(300);

        // add the banner to the dim overlay
        StackPane bannerWrapper = new StackPane();
        bannerWrapper.setMaxWidth(520);
        bannerWrapper.setMaxHeight(300);

        // Create a separate ImageView as the *true* banner background layer
        ImageView bannerBgImage = new ImageView(new Image("file:assets/images/bannerbg.png"));
        bannerBgImage.setFitWidth(1200);
        bannerBgImage.setPreserveRatio(true);

        // Place actual banner (with text + cat) above it
        bannerWrapper.getChildren().addAll(bannerBgImage, banner);

        // Center everything inside overlay
        overlay.getChildren().add(bannerWrapper);

        // 🌟 Place overlay on top of everything
        // interaction layer — transparent pane for temporary clickable overlays (bib, bowl, toys)
        interactionLayer = new Pane();
        interactionLayer.setPickOnBounds(false); // allow clicks to pass through where no node exists
        interactionLayer.setMouseTransparent(true); // allow clicks on nodes we add

        // 🆕 REMOVED: Debug borders and logging for cleaner code
        // interactionLayer.setStyle("-fx-border-color: green; -fx-border-width: 3; -fx-background-color: rgba(0,255,0,0.1);");

        // Build the main stack: root (layout) -> interactionLayer (overlays) -> overlay (intro)
        mainLayer = new StackPane(root, interactionLayer, overlay);

        Scene scene = new Scene(mainLayer, 1200, 800);
        stage.setScene(scene);

        // 💫 Animation sequence: dim fade → text → cat → fade away
        FadeTransition overlayFadeIn = new FadeTransition(Duration.seconds(0.8), overlay);
        overlayFadeIn.setFromValue(0);
        overlayFadeIn.setToValue(1);

        FadeTransition textFadeIn = new FadeTransition(Duration.seconds(1), welcomeText);
        textFadeIn.setFromValue(0);
        textFadeIn.setToValue(1);

        PauseTransition pause = new PauseTransition(Duration.seconds(0.7));

        FadeTransition catFadeIn = new FadeTransition(Duration.seconds(1.2), introCat);
        catFadeIn.setFromValue(0);
        catFadeIn.setToValue(1);

        PauseTransition hold = new PauseTransition(Duration.seconds(2.5));

        FadeTransition fadeOutAll = new FadeTransition(Duration.seconds(1.2), overlay);
        fadeOutAll.setFromValue(1);
        fadeOutAll.setToValue(0);

        SequentialTransition sequence = new SequentialTransition(
            overlayFadeIn, textFadeIn, pause, catFadeIn, hold, fadeOutAll
        );
        sequence.play();
        sequence.setOnFinished(e -> {
            overlay.setVisible(false);
            overlay.setManaged(false);
            overlay.setMouseTransparent(true);
            mainLayer.getChildren().remove(overlay);
            System.out.println("✅ Overlay completely removed");
        });

        // 🐱 Preload all emotion images (so they don't lag later)
        Image heartImg = new Image("file:assets/images/caticonheart.png");
        Image happyImg = new Image("file:assets/images/caticonhappy.png");
        Image sadImg = new Image("file:assets/images/caticonsad.png");
        Image angryImg = new Image("file:assets/images/caticonangry.png");
        Image sleepyImg = new Image("file:assets/images/caticonsleepy.png");
        Image dirtyImg = new Image("file:assets/images/caticondirty.png");

        // --- Timeline for automatic stat updates ---
        // --- REDUCED depletion rates ---
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            // Decrease all stats over time (REDUCED depletion rates)
            cat.hunger = Math.max(0, cat.getHunger() - 2);        // Reduced from 5 to 2
            cat.cleanliness = Math.max(0, cat.getCleanliness() - 1); // Reduced from 3 to 1
            cat.happiness = Math.max(0, cat.getHappiness() - 1);     // Reduced from 2 to 1
            cat.energy = Math.max(0, cat.getEnergy() - 1);           // Reduced from 3 to 1
            
            updateBars(cat, hungerBar, happinessBar, energyBar, cleanlinessBar);
            autoUpdateEmotion(cat, emotionView, emotionText,heartImg, happyImg, sadImg, angryImg, sleepyImg, dirtyImg);
            
            // Debug output to see what's happening
            System.out.println("📊 Auto-update - Hunger: " + cat.getHunger() + 
                              ", Energy: " + cat.getEnergy() + 
                              ", Happiness: " + cat.getHappiness() + 
                              ", Cleanliness: " + cat.getCleanliness());
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();      
    }

    private static void playCatLick(ImageView catView, Button feedBtn, Button playBtn, Button cleanBtn, Button restBtn)  {
        // 🐱 Change to licking cat for 3 seconds, then back to sitcat
        catView.setImage(new Image("file:assets/images/catlick.png"));

        PauseTransition backToSit = new PauseTransition(Duration.seconds(3));
        backToSit.setOnFinished(e -> {
            catView.setImage(new Image("file:assets/images/sitcat2.png"));
        feedBtn.setDisable(false);
        playBtn.setDisable(false);
        cleanBtn.setDisable(false);
        restBtn.setDisable(false);
        });
        backToSit.play();
    }

    private static boolean isAnimating = false;
    private static String currentAction = "";

    private static void animateCatReaction(
        ImageView catView,
        String action,
        Cat cat,
        ProgressBar hungerBar,
        ProgressBar happinessBar,
        ProgressBar energyBar,
        ProgressBar cleanlinessBar,
        Button activeButton,
        Button... otherButtons
    ) {
        System.out.println("🎬 ANIMATE CAT REACTION CALLED - Action: " + action);
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

        Image originalImage = new Image("file:assets/images/sitcat2.png");
        
        switch (action) {
            case "feed" -> {
    // 🥣 Step 1 — Create the bib
    ImageView bib = new ImageView("file:assets/images/bib.png");
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
            System.out.println("⏳ Cat waited too long for food!");
            
            try {
                Image angryImage = new Image("file:assets/images/catbibangry.png");
                catView.setImage(angryImage);
                System.out.println("😾 Cat is angry!");
            } catch (Exception ex) {
                System.out.println("❌ Failed to load angry image");
            }

            // Remove the bowl container
            mainLayer.getChildren().removeIf(node -> {
                if (node instanceof Pane pane) {
                    return !pane.getChildren().isEmpty() && 
                           pane.getChildren().get(0) instanceof ImageView imageView &&
                           imageView.getImage() != null &&
                           imageView.getImage().getUrl().contains("bowl");
                }
                return false;
            });

            // Re-enable buttons after a moment
            PauseTransition angryDelay = new PauseTransition(Duration.seconds(2));
            angryDelay.setOnFinished(e2 -> {
                catView.setImage(originalImage);
                enableOtherButtons(otherButtons);
                isAnimating = false;
                currentAction = "";
            });
            angryDelay.play();
        }
    });
    
    bib.setOnMouseClicked(e -> {
        System.out.println("🎯 Bib clicked!");
        
        catView.setImage(new Image("file:assets/images/catbib.png"));
        mainLayer.getChildren().remove(bibContainer);

        // 🍲 Create the bowl
        ImageView bowl = new ImageView("file:assets/images/bowl.png");
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
                // cat_eat2
                eatingLoop.getKeyFrames().add(new KeyFrame(
                    Duration.seconds(0.6 * (2 * i)),
                    ev -> catView.setImage(new Image("file:assets/images/cateating.png"))
                ));

                // cat_eat3
                eatingLoop.getKeyFrames().add(new KeyFrame(
                    Duration.seconds(0.6 * (2 * i + 1)),
                    ev -> catView.setImage(new Image("file:assets/images/catbibbowl.png"))
                ));
            }

            // After looping → reset pose
            eatingLoop.getKeyFrames().add(new KeyFrame(
                Duration.seconds(0.6 * loops * 2),
                ev -> {
                    catView.setImage(originalImage);
                    mainLayer.getChildren().remove(bowlContainer);

                    // 🍽️ SMOOTH HUNGER REPLENISHMENT (like energy in rest)
                    Timeline hungerReplenish = new Timeline(
                        new KeyFrame(Duration.seconds(0.1), replenishEv -> {
                            int currentHunger = cat.getHunger();
                            if (currentHunger < 100) {
                                cat.hunger = Math.min(100, cat.hunger + 2); // Increase hunger
                                updateBars(cat, hungerBar, happinessBar, energyBar, cleanlinessBar);
                                System.out.println("🍖 Hunger: " + cat.getHunger() + "/100");
                            }
                        })
                    );
                    hungerReplenish.setCycleCount(50); // 50 cycles * 0.1 seconds = 5 seconds total
                    hungerReplenish.play();

                    enableOtherButtons(otherButtons);
                    isAnimating = false;
                    currentAction = "";
                    System.out.println("✅ Eating animation complete");
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
    isAnimating = true;
    currentAction = "play";
    
    // Store original position
    double originalX = catView.getTranslateX();
    double originalY = catView.getTranslateY();
    
    // Load walking animation frames
    final Image walk1 = new Image("file:assets/images/catwalk1.png");
    final Image walk2 = new Image("file:assets/images/catwalk2.png");
    final Image walk3 = new Image("file:assets/images/catwalk3.png");
    final Image walk4 = new Image("file:assets/images/catwalk4.png");
    final Image walk5 = new Image("file:assets/images/catwalk5.png");
    final Image walk6 = new Image("file:assets/images/catwalk6.png"); 
    
    // Step 1: Walk to cat stand - Increased duration
    double standX = 200;
    double standY = 60;
    double walkDuration = 3.0; // Increased from 2.0 to 3.0
    double speedX = (standX - originalX) / (walkDuration * 16.67);
    double speedY = (standY - originalY) / (walkDuration * 16.67);
    
    catView.setImage(walk1);
    animateCatWalk(catView, walk1, walk2, walk3, speedX, speedY, (int)(walkDuration * 16.67));
    
    PauseTransition walkToStand = new PauseTransition(Duration.seconds(walkDuration));
    walkToStand.setOnFinished(e -> {
        // Step 2: Hide in cat stand and meow - Increased duration
        catView.setImage(new Image("file:assets/images/catpeek.png"));
        catView.setVisible(false);
        
        // Show cat stand
        ImageView catStand = new ImageView(new Image("file:assets/images/catpeek.png"));
        catStand.setFitWidth(100);
        catStand.setPreserveRatio(true);
        catStand.setTranslateX(standX);
        catStand.setTranslateY(standY+40);
        mainLayer.getChildren().add(catStand);

        ImageView catPeekView = new ImageView(new Image("file:assets/images/catpeeking2.png"));
        catPeekView.setFitWidth(100);
        catPeekView.setPreserveRatio(true);
        catPeekView.setVisible(false);
        mainLayer.getChildren().add(catPeekView);
        
        PauseTransition hidePause = new PauseTransition(Duration.seconds(4)); // Increased from 3 to 4
        hidePause.setOnFinished(ev -> {
            // Cat peeks out and meows
            catView.setVisible(false);
            catPeekView.setVisible(true);
            catPeekView.setTranslateX(standX);
            catPeekView.setTranslateY(standY+40);
            
            // Play meow sound
            AudioClip meowSound = new AudioClip(new File("assets/audio/cat-meow.mp3").toURI().toString());
            meowSound.play();

            PauseTransition meowPause = new PauseTransition(Duration.seconds(1.2)); // Increased from 0.8 to 1.2
            meowPause.setOnFinished(meowEnd -> {
                // Cat goes back to hiding
                catPeekView.setVisible(false);
                
                PauseTransition afterMeow = new PauseTransition(Duration.seconds(0.5)); // Increased from 0.2 to 0.5
                afterMeow.setOnFinished(afterMeowEv -> {
                    // Step 3: Walk to bed - Increased duration
                    mainLayer.getChildren().remove(catStand);
                    catView.setVisible(true);
                    
                    double bedX = -350;
                    double bedY = 50;
                    double walkToBedDuration = 5.0; // Increased from 4.0 to 5.0
                    double bedSpeedX = (bedX - standX) / (walkToBedDuration * 16.67);
                    double bedSpeedY = (bedY - standY) / (walkToBedDuration * 16.67);
                    
                    catView.setImage(walk1);
                    animateCatWalk(catView, walk4, walk5, walk6, bedSpeedX, bedSpeedY, (int)(walkToBedDuration * 16.67));
                    
                    PauseTransition walkToBed = new PauseTransition(Duration.seconds(walkToBedDuration));
                    walkToBed.setOnFinished(walkBedEnd -> {
                        // Show teddy on bed
                        ImageView teddy = new ImageView(new Image("file:assets/images/teddy.png"));
                        teddy.setFitWidth(100);
                        teddy.setPreserveRatio(true);
                        teddy.setTranslateX(bedX + 20);
                        teddy.setTranslateY(bedY - 10);
                        mainLayer.getChildren().add(teddy);
                        
                        // Cat picks up teddy
                        catView.setImage(new Image("file:assets/images/cathug.png"));
                        mainLayer.getChildren().remove(teddy);
                        
                        PauseTransition hugPause = new PauseTransition(Duration.seconds(2)); // Increased from 1 to 2
                        hugPause.setOnFinished(hugEnd -> {
                            // Hugging animation - Increased durations
                            Timeline hugAnimation = new Timeline();
                            
                            // Create 3 hug cycles with increased durations
                            for (int i = 0; i < 3; i++) {
                                double startTime = i * 1.8; // Increased from 1.3 to 1.8
                                
                                // First pose
                                hugAnimation.getKeyFrames().add(new KeyFrame(
                                    Duration.seconds(startTime),
                                    hugEv -> catView.setImage(new Image("file:assets/images/cathug.png"))
                                ));
                                
                                // Second pose with breathing
                                hugAnimation.getKeyFrames().add(new KeyFrame(
                                    Duration.seconds(startTime + 0.7), // Increased from 0.5 to 0.7
                                    hugEv -> {
                                        catView.setImage(new Image("file:assets/images/cathug2.png"));
                                        ScaleTransition breathe = new ScaleTransition(Duration.seconds(0.4), catView); // Increased from 0.3 to 0.4
                                        breathe.setToX(1.1);
                                        breathe.setToY(1.1);
                                        breathe.play();
                                    }
                                ));
                                
                                // Return to normal size
                                hugAnimation.getKeyFrames().add(new KeyFrame(
                                    Duration.seconds(startTime + 1.1), // Increased from 0.8 to 1.1
                                    hugEv -> {
                                        ScaleTransition normalSize = new ScaleTransition(Duration.seconds(0.3), catView); // Increased from 0.2 to 0.3
                                        normalSize.setToX(1.0);
                                        normalSize.setToY(1.0);
                                        normalSize.play();
                                    }
                                ));
                            }
                            
                            hugAnimation.setOnFinished(hugDone -> {
                                // Step 4: Scratch the floor - Increased duration
                                catView.setImage(new Image("file:assets/images/catscratch.png"));

                                AudioClip scratchSound = new AudioClip(new File("assets/audio/scratch.mp3").toURI().toString());
                                scratchSound.play();

                                // Simple scratch animation
                                int[] scratchCycle = new int[1]; // array to make it effectively final
                                scratchCycle[0] = 0;

                                Timeline scratchAnimation = new Timeline(
                                    new KeyFrame(Duration.seconds(0.3), event -> {
                                        switch (scratchCycle[0] % 3) {
                                            case 0:
                                                catView.setImage(new Image("file:assets/images/catscratch.png"));
                                                break;
                                            case 1:
                                                catView.setImage(new Image("file:assets/images/cat_scratch2.png"));
                                                break;
                                            case 2:
                                                catView.setImage(new Image("file:assets/images/cat_scratch.png"));
                                                break;
                                        }
                                        scratchCycle[0]++;
                                    })
                                );

                                scratchAnimation.setCycleCount(15); // 5 cycles * 3 frames
                                
                                scratchAnimation.setOnFinished(scratchDone -> {
                                    // Walk back to original position - Increased duration
                                    catView.setImage(walk1);
                                    double returnWalkDuration = 3.0; // Increased duration for return walk
                                    animateCatWalk(catView, walk1, walk2, walk3, speedX, -speedY, (int)(returnWalkDuration * 16.67));
                                    
                                    PauseTransition returnWalk = new PauseTransition(Duration.seconds(returnWalkDuration));
                                    returnWalk.setOnFinished(returnEnd -> {
                                        // Final reset
                                        catView.setImage(originalImage);
                                        catView.setTranslateX(originalX);
                                        catView.setTranslateY(originalY);
                                        
                                        enableOtherButtons(otherButtons);
                                        isAnimating = false;
                                        currentAction = "";
                                        System.out.println("🎉 Play sequence finished!");
                                    });
                                    returnWalk.play();
                                });
                                scratchAnimation.play();
                            });
                            hugAnimation.play();
                        });
                        hugPause.play();
                    });
                    walkToBed.play();
                });
                afterMeow.play();
            });
            meowPause.play();
        });
        hidePause.play();
    });
    walkToStand.play();
}
case "clean" -> {
    isAnimating = true;
    currentAction = "clean";
    
    // Store original background and position
    String originalBG = "file:assets/images/catcare.png";
    double originalX = catView.getTranslateX();
    double originalY = catView.getTranslateY();
    
    BorderPane root = (BorderPane) mainLayer.getChildren().get(0);
    
    // Step 1: Cat walks out to the right
    final Image walk1 = new Image("file:assets/images/catwalk1.png");
    final Image walk2 = new Image("file:assets/images/catwalk2.png");
    final Image walk3 = new Image("file:assets/images/catwalk3.png");
    
    // Walk off-screen to the right
    double exitDuration = 10.0;
    catView.setImage(walk1);
    animateCatWalk(catView, walk1, walk2, walk3, 2, 0.75, (int)(exitDuration * 33.33));
    
    PauseTransition exitDelay = new PauseTransition(Duration.seconds(exitDuration));
    exitDelay.setOnFinished(e -> {
        // Step 2: Switch to bath scene
        Image bathBG = new Image("file:assets/images/catcarebath.png");
        BackgroundImage bathBackground = new BackgroundImage(
            bathBG,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true)
        );
        root.setBackground(new Background(bathBackground));
        
        // Reset cat position to left side for entrance
        catView.setTranslateX(-600);
        catView.setTranslateY(originalY);
        animateCatWalk(catView, walk1, walk2, walk3, 1.1, 0.3, (int)(15 * 33.33));
        
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
                // Step 6: Cat goes into bath tub
                catView.setImage(new Image("file:assets/images/catbath.png"));
                catView.setTranslateY(catView.getTranslateY()-240);
                catView.setTranslateX(catView.getTranslateX()+100);
                
                // 🛁 SMOOTH CLEANLINESS REPLENISHMENT
                Timeline cleanlinessReplenish = new Timeline(
                    new KeyFrame(Duration.seconds(0.1), replenishEv -> {
                        int currentCleanliness = cat.getCleanliness();
                        if (currentCleanliness < 100) {
                            cat.cleanliness = Math.min(100, cat.cleanliness + 2);
                            updateBars(cat, hungerBar, happinessBar, energyBar, cleanlinessBar);
                            System.out.println("✨ Cleanliness: " + cat.getCleanliness() + "/100");
                        }
                    })
                );
                cleanlinessReplenish.setCycleCount(60); // 6 seconds total
                
                // Step 7: Up-down bobbing animation in water
                TranslateTransition bobAnimation = new TranslateTransition(Duration.seconds(1), catView);
                bobAnimation.setByY(-10);
                bobAnimation.setAutoReverse(true);
                bobAnimation.setCycleCount(6);
                
                PauseTransition bobDelay = new PauseTransition(Duration.seconds(1));
                bobDelay.setOnFinished(bobStart -> {
                    bobAnimation.play();
                    cleanlinessReplenish.play(); // Start cleanliness replenishment during bath
                });
                
                bobAnimation.setOnFinished(bobEnd -> {
                    // Step 8: Cat comes out with towel and sneezes
                    catView.setImage(new Image("file:assets/images/cattowel.png"));
                    catView.setTranslateY(catView.getTranslateY() + 240);
                    catView.setTranslateX(catView.getTranslateX()-130);

                    // Remove duck from tub
                    mainLayer.getChildren().remove(duckInTub);

                    // Sneezing sequence
                    PauseTransition firstTowelPause = new PauseTransition(Duration.seconds(1.5));
                    firstTowelPause.setOnFinished(sneeze1 -> {
                        AudioClip sneezeSound = new AudioClip(new File("assets/audio/sneeze.mp3").toURI().toString());
                        sneezeSound.play();
                        
                        PauseTransition sneezePause = new PauseTransition(Duration.seconds(0.8));
                        sneezePause.setOnFinished(backToTowel1 -> {
                            catView.setImage(new Image("file:assets/images/cattowel.png"));
                            
                            PauseTransition betweenSneezes = new PauseTransition(Duration.seconds(1.0));
                            betweenSneezes.setOnFinished(sneeze2 -> {
                                catView.setImage(new Image("file:assets/images/catsneeze.png"));
                                
                                PauseTransition finalSneezePause = new PauseTransition(Duration.seconds(0.8));
                                finalSneezePause.setOnFinished(backToTowel2 -> {
                                    catView.setImage(new Image("file:assets/images/cattowel.png"));
                                    
                                    PauseTransition towelTime = new PauseTransition(Duration.seconds(1.5));
                                    towelTime.setOnFinished(towelEnd -> {
                                        // Step 10: Cat walks back to left
                                        final Image exitWalk1 = new Image("file:assets/images/catwalk4.png");
                                        final Image exitWalk2 = new Image("file:assets/images/catwalk5.png");
                                        final Image exitWalk3 = new Image("file:assets/images/catwalk6.png");
                                        
                                        double leaveDuration = 10.0;
                                        animateCatWalk(catView, exitWalk1, exitWalk2, exitWalk3, -2, 0, (int)(leaveDuration * 33.33));
                                        
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
                                            
                                            // Return cat to original position
                                            catView.setTranslateX(originalX);
                                            catView.setTranslateY(originalY);
                                            catView.setImage(new Image("file:assets/images/sitcat2.png"));
                                            
                                            // Update stats and finish
                                            cat.clean();
                                            updateBars(cat, hungerBar, happinessBar, energyBar, cleanlinessBar);
                                            
                                            enableOtherButtons(otherButtons);
                                            isAnimating = false;
                                            currentAction = "";
                                            System.out.println("🛁 Clean complete! Cleanliness: " + cat.getCleanliness() + "/100");
                                        });
                                        leaveDelay.play();
                                    });
                                    towelTime.play();
                                });
                                finalSneezePause.play();
                            });
                            betweenSneezes.play();
                        });
                        sneezePause.play();
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
    String originalBG = "file:assets/images/catcare.png";
    double originalX = catView.getTranslateX();
    double originalY = catView.getTranslateY();
    
    // Change to nighttime background
    Image nightBG = new Image("file:assets/images/catcarenight.png");
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
    final Image walk1 = new Image("file:assets/images/catwalk4.png");
    final Image walk2 = new Image("file:assets/images/catwalk5.png");
    final Image walk3 = new Image("file:assets/images/catwalk6.png");
    
    // Set initial walking image BEFORE animation starts
    catView.setImage(walk1);
    
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
    animateCatWalkToBed(catView, walk1, walk2, walk3, speedX, speedY, (int)(walkDuration * 16.67));
    
    // Sleep sequence after walk
    PauseTransition walkDelay = new PauseTransition(Duration.seconds(walkDuration));
    walkDelay.setOnFinished(e -> {
        // Switch to sleep cat image
        catView.setImage(new Image("file:assets/images/sleepcat.png"));
        
        // Smooth energy replenishment timeline
        Timeline energyReplenish = new Timeline(
            new KeyFrame(Duration.seconds(0.1), ev -> { // Update every 0.1 seconds for smoother animation
                int currentEnergy = cat.getEnergy();
                if (currentEnergy < 100) {
                    cat.energy = Math.min(100, cat.energy + 1); // Increase by 1 instead of 10
                    updateBars(cat, hungerBar, happinessBar, energyBar, cleanlinessBar);
                    System.out.println("🔋 Energy: " + cat.getEnergy() + "/100");
                }
            })
        );
        energyReplenish.setCycleCount(100); // 100 cycles * 0.1 seconds = 10 seconds total
        
        // Sleep for 10 seconds
        PauseTransition sleep = new PauseTransition(Duration.seconds(10));
        
        sleep.setOnFinished(ev -> {
            energyReplenish.stop();
            
            // Change background to DAY first, THEN cat walks back
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
            final Image rwalk1 = new Image("file:assets/images/catwalk1.png");
            final Image rwalk2 = new Image("file:assets/images/catwalk2.png");
            final Image rwalk3 = new Image("file:assets/images/catwalk3.png");
            
            catView.setImage(rwalk1);
            animateCatWalk(catView, rwalk1, rwalk2, rwalk3, -speedX, -speedY, (int)(walkDuration * 16.67));
            
            PauseTransition returnDelay = new PauseTransition(Duration.seconds(walkDuration));
            returnDelay.setOnFinished(returnEv -> {
                // Return to sitting position
                catView.setImage(originalImage);
                catView.setTranslateX(originalX);
                catView.setTranslateY(originalY);
                
                enableOtherButtons(otherButtons);
                isAnimating = false;
                currentAction = "";
                System.out.println("😴 Rest complete! Energy: " + cat.getEnergy() + "/100");
            });
            returnDelay.play();
        });
        
        energyReplenish.play();
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

    private static void animateCatWalk(ImageView catView, Image cat1, Image cat2, Image cat3, double speedx, double speedy, int steps) {
        // --- Alternate between 3 walking poses ---
        Timeline walkCycle = new Timeline(
            new KeyFrame(Duration.seconds(0.3), e -> catView.setImage(cat1)),
            new KeyFrame(Duration.seconds(0.6), e -> catView.setImage(cat2)),
            new KeyFrame(Duration.seconds(0.9), e -> catView.setImage(cat3))
        );
        walkCycle.setCycleCount(Timeline.INDEFINITE);
        walkCycle.play();

        // --- Move forward gradually ---
        Timeline moveForward = new Timeline(
            new KeyFrame(Duration.millis(30), e -> {
                catView.setTranslateX(catView.getTranslateX() + speedx);
                catView.setTranslateY(catView.getTranslateY() + speedy);
            })
        );
        moveForward.setCycleCount(steps);

        // --- Stop walking & return to sit pose ---
        moveForward.setOnFinished(e -> {
            walkCycle.stop();
            catView.setImage(new Image("file:assets/images/sitcat2.png"));
        });

        moveForward.play();
    }

    private static void playWakeUpIntro(ImageView catView) {
        // 💤 Step 1: Start asleep
        catView.setImage(new Image("file:assets/images/sleepcat.png"));
        catView.setSmooth(true);
        catView.setCache(true);
        catView.setOpacity(1);

        // 😺 Step 2: Wait for 10 seconds, then open eyes
        PauseTransition firstPause = new PauseTransition(Duration.seconds(10));
        firstPause.setOnFinished(e -> {
            catView.setImage(new Image("file:assets/images/awakecat.png"));

            // 🐾 Step 3: After 1 second, sit up
            PauseTransition secondPause = new PauseTransition(Duration.seconds(1));
            secondPause.setOnFinished(ev -> {
                // Slight scale and sit pose
                catView.setScaleX(1.3);
                catView.setScaleY(1.3);
                catView.setTranslateY(3);
                catView.setImage(new Image("file:assets/images/sitcat.png"));

                // 🐱 Step 4: Meow after a short delay
                PauseTransition meowPause = new PauseTransition(Duration.seconds(0.8));
                meowPause.setOnFinished(done -> {
                    AudioClip meowSound = new AudioClip(new File("assets/audio/cat-meow.mp3").toURI().toString());
                    meowSound.play();
                    catView.setImage(new Image("file:assets/images/sitcatmeow.png"));

                    // 🕐 Step 5: Return to sitcat after meowing
                    // 🕐 Step 5: Return to sitcat after meowing
                    PauseTransition backToSit = new PauseTransition(Duration.seconds(1.2));
                    backToSit.setOnFinished(end -> {
                        catView.setImage(new Image("file:assets/images/sitcat.png"));
                        
                        // ENABLE BUTTONS AFTER INTRO IS COMPLETE
                        
                    });
                    backToSit.play();
                });
                meowPause.play();
            });
            secondPause.play();
        });

        firstPause.play();
    }

    private static String getPriorityAction(Cat cat) {
        double hunger = cat.getHunger();
        double happiness = cat.getHappiness();
        double energy = cat.getEnergy();
        double cleanliness = cat.getCleanliness();

        // 🩵 Step 1: If any stat is critically low, fix that first
        if (hunger < 20) return "feed";
        if (energy < 20) return "rest";
        if (cleanliness < 20) return "clean";
        if (happiness < 20) return "play";

        return "sit";
    }

    private static void updateBars(Cat cat, ProgressBar satisfaction, ProgressBar happiness, ProgressBar energy, ProgressBar cleanliness) {
        // --- Convert all values safely ---
        double sat = safeProgress(cat.getHunger() / 100.0);
        double happy = safeProgress(cat.getHappiness() / 100.0);
        double ener = safeProgress(cat.getEnergy() / 100.0);
        double clean = safeProgress(cat.getCleanliness() / 100.0);

        // --- Apply them safely ---
        satisfaction.setProgress(sat);
        happiness.setProgress(happy);
        energy.setProgress(ener);
        cleanliness.setProgress(clean);

        // --- Optional: dynamic colors ---
        satisfaction.setStyle("-fx-accent:" + getDynamicColor((int)(sat * 100)) + ";");
        happiness.setStyle("-fx-accent:" + getDynamicColor(cat.getHappiness()) + ";");
        energy.setStyle("-fx-accent:" + getDynamicColor(cat.getEnergy()) + ";");
        cleanliness.setStyle("-fx-accent:" + getDynamicColor(cat.getCleanliness()) + ";");
    }
    private static void animateCatWalkToBed(ImageView catView, Image cat1, Image cat2, Image cat3, double speedX, double speedY, int steps) {
    // Walking animation cycle
    Timeline walkCycle = new Timeline(
        new KeyFrame(Duration.seconds(0.3), e -> catView.setImage(cat1)),
        new KeyFrame(Duration.seconds(0.6), e -> catView.setImage(cat2)),
        new KeyFrame(Duration.seconds(0.9), e -> catView.setImage(cat3))
    );
    walkCycle.setCycleCount(Timeline.INDEFINITE);
    walkCycle.play();

    // Movement timeline
    Timeline moveTimeline = new Timeline(
        new KeyFrame(Duration.millis(60), e -> {
            catView.setTranslateX(catView.getTranslateX() + speedX);
            catView.setTranslateY(catView.getTranslateY() + speedY);
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
        Cat cat, ImageView emotionView, Label emotionText,
        Image heartImg, Image happyImg, Image sadImg,
        Image angryImg, Image sleepyImg, Image dirtyImg) {

        Image newImage = heartImg; // default
        String message = "Hi! I love You 💖";

        if (cat.getCleanliness() < 30) {
                newImage = dirtyImg;
                message = "Oh no, I feel so dirty!";
        } 
        else if (cat.getHunger() < 40) {
                newImage = angryImg;
                message = "I'm starving...feed me please!";
        } 
        else if (cat.getEnergy() < 30) {
                newImage = sleepyImg;
                message = "Yaaawn... I need some rest!";
        } 
        else if (cat.getHappiness() < 40) {
                newImage = sadImg;
                message = "I'm feeling lonely...";
        } 
        else if (cat.getHappiness() > 70 && cat.getEnergy() > 50 && cat.getHunger() > 40) {
                newImage = happyImg;
                message = "I'm purrfectly happy!";
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