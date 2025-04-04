package edu.ntnu.idatt2001.paths.controller;

import edu.ntnu.idatt2001.paths.Main;
import edu.ntnu.idatt2001.paths.view.CreatePlayerView;
import edu.ntnu.idatt2001.paths.view.OpeningView;
import javafx.stage.Stage;

/**
 * Controller for main menu.
 */
public class OpeningController {

  private final OpeningView openingView;

  /**
   * Constructor for OpeningController.
   *
   * @param openingView  OpeningView instance.
   * @param primaryStage Stage to set.
   */
  public OpeningController(OpeningView openingView, Stage primaryStage) {
    this.openingView = openingView;
    initialize(primaryStage);
  }

  /**
   * Initialize method for OpeningController
   *
   * @param primaryStage Stage to set.
   */
  public void initialize(Stage primaryStage) {
    openingView.setStartButtonAction(e -> {
      CreatePlayerView createPlayerView = new CreatePlayerView(primaryStage);
      Main.switchScene(createPlayerView);
    });

  }

}

