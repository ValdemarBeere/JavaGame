package edu.ntnu.idatt2001.paths.controller;

import edu.ntnu.idatt2001.paths.Main;
import edu.ntnu.idatt2001.paths.model.Link;
import edu.ntnu.idatt2001.paths.model.Passage;
import edu.ntnu.idatt2001.paths.model.Story;
import edu.ntnu.idatt2001.paths.model.game.Game;
import edu.ntnu.idatt2001.paths.model.game.GameInstance;
import edu.ntnu.idatt2001.paths.model.goals.Goal;
import edu.ntnu.idatt2001.paths.model.goals.GoalsEnum;
import edu.ntnu.idatt2001.paths.model.goals.HealthGoal;
import edu.ntnu.idatt2001.paths.model.goals.InventoryGoal;
import edu.ntnu.idatt2001.paths.model.goals.ScoreGoal;
import edu.ntnu.idatt2001.paths.model.player.Player;
import edu.ntnu.idatt2001.paths.view.DeathView;
import edu.ntnu.idatt2001.paths.view.InventoryView;
import edu.ntnu.idatt2001.paths.view.VictoryView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


/**
 * Controller for Game.
 */
public class GameController {

  private final Label titleLabel;
  private final Label contentLabel;
  private final VBox linksVBox;
  private final ImageView backgroundImageView;
  private final ImageView contentImageView;
  private final Button inventoryButton;
  private final Player player;
  private Game game;

  /**
   * Constructor for GameController.
   *
   * @param titleLabel          The titleLabel in GameView.
   * @param contentLabel        The contentLabel in GameView.
   * @param linksVBox           The linksVBox in GameView.
   * @param backgroundImageView The backgroundImageView in GameView.
   * @param contentImageView    The contentImageView in GameView.
   * @param inventoryButton     The inventoryButton in GameView
   */
  public GameController(Label titleLabel, Label contentLabel, VBox linksVBox,
      ImageView backgroundImageView, ImageView contentImageView, Button inventoryButton) {
    this.titleLabel = titleLabel;
    this.contentLabel = contentLabel;
    this.linksVBox = linksVBox;
    this.backgroundImageView = backgroundImageView;
    this.contentImageView = contentImageView;
    this.inventoryButton = inventoryButton;
    this.player = GameInstance.getInstance().getPlayer(); // Get player from GameInstance
    initialize(GameInstance.getInstance().getStory()); // Get story from GameInstance
  }

  /**
   * Initializes the given story.
   *
   * @param story The story to be initialized.
   */
  public void initialize(Story story) {
    // Initialize goals list
    List<Goal<?>> goals = new ArrayList<>();
    Goal<Integer> goldGoal = GoalsEnum.GOLD.getGoal();// target gold is 100
    ScoreGoal scoreGoal = (ScoreGoal) GoalsEnum.SCORE.getGoal(); //target score is 100
    HealthGoal healthGoal = (HealthGoal) GoalsEnum.HEALTH.getGoal(); // target health is 1
    InventoryGoal inventoryGoal = new InventoryGoal("gem");
    goals.add(goldGoal);
    goals.add(scoreGoal);
    goals.add(healthGoal);
    goals.add(inventoryGoal);

    // Use the passed story and goals list
    game = new Game(player, story, goals); // Initialize the game

    // With this block:
    List<Link> brokenLinks = story.getBrokenLinks();
    if (!brokenLinks.isEmpty()) {
      String message = "Warning: Broken links found in the story: " + brokenLinks;
      showInformationBox(message);
    }

    updateUI(game.begin());
  }

  /**
   * Updates the User Interface.
   *
   * @param passage The passage to update the User Interface according to.
   */
  private void updateUI(Passage passage) {
    MusicController.getInstance().getPageFlip().play();
    titleLabel.setText(passage.getTitle());
    contentLabel.setText(passage.getContent());
    if (passage.getBackgroundImage() != null) {
      backgroundImageView.setImage(passage.getBackgroundImage());
    } else {
      backgroundImageView.setImage(new Image(
          Objects.requireNonNull(getClass().getResourceAsStream(
              "/images/passageImages/passageBackgroundImages/mainMenu.png"))));
    }
    if (passage.getContentImage() != null) {
      contentImageView.setImage(passage.getContentImage());
    } else {
      contentImageView.setImage(null);
    }

    // Clear the linksVBox and populate it with buttons
    linksVBox.getChildren().clear();
    for (Link link : passage.getLinks()) {
      Button linkButton = new Button(link.getText());
      linkButton.getStyleClass().add("actionText");
      linkButton.getStyleClass().add("textField");
      linkButton.getStylesheets().addAll(
          Objects.requireNonNull(getClass().
              getResource("/styles/text.css")).toExternalForm(), Objects.requireNonNull(
              getClass().getResource("/styles/buttons.css")).toExternalForm());
      linkButton.setMaxWidth(630);
      VBox.setMargin(linkButton, new Insets(0, 0, 10, 0));

      linkButton.setOnAction(event -> {
        try {
          if (!passage.hasPerformedAction(link)) {

            // If inventory is full, switch to the inventory scene
            if (player.isInventoryFull()) {
              // create inventory controller
              InventoryController inventoryController = new InventoryController();
              // set inventory view to the controller before creating the view
              InventoryView inventoryView = new InventoryView(this);
              // make sure to set the inventory view before calling fillInventory() method
              inventoryController.setInventoryView(inventoryView);
              // now fill the inventory
              inventoryController.fillInventory();
              Main.switchScene(inventoryView);
              return;
            }

            boolean gameIsFinished = game.progressGame(link);

            // Display action summary in an information box if it's not empty
            String actionSummary = link.getActionSummary();
            if (!actionSummary.isEmpty()) {
              MusicController.getInstance().getPickUp().play();
              showInformationBox(actionSummary);
            }

            passage.markActionAsPerformed(link);
            if (!player.isAlive()) {
              DeathView deathView = new DeathView();
              Main.switchScene(deathView);
            }

            if (gameIsFinished) {
              VictoryView victoryView = new VictoryView();
              Main.switchScene(victoryView);
              return;
            }
          }

          Passage nextPassage = game.go(link);
          updateUI(nextPassage);

        } catch (RuntimeException e) {
          showInformationBox(e.getMessage());
        }
      });

      linksVBox.getChildren().add(linkButton);
    }

    inventoryButton.setOnAction(event -> {
      // create inventory controller
      InventoryController inventoryController = new InventoryController();

      // set inventory view to the controller before creating the view
      InventoryView inventoryView = new InventoryView(this);

      // make sure to set the inventory view before calling fillInventory() method
      inventoryController.setInventoryView(inventoryView);

      // now fill the inventory
      inventoryController.fillInventory();

      Main.switchScene(inventoryView);
    });

  }

  /**
   * Gets background image.
   *
   * @return background image.
   */
  public ImageView getBackgroundImageView() {
    return backgroundImageView;
  }

  /**
   * Shows an information box.
   *
   * @param message The message to be added to the information box.
   */
  private void showInformationBox(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Information");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

}
