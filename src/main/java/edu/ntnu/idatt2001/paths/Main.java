package edu.ntnu.idatt2001.paths;

import edu.ntnu.idatt2001.paths.controller.MusicController;
import edu.ntnu.idatt2001.paths.controller.OpeningController;
import edu.ntnu.idatt2001.paths.view.OpeningView;
import java.util.ArrayDeque;
import java.util.Deque;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The main application class for the story game. This class handles the main window (primary stage)
 * and provides methods for switching between different scenes (views). It also maintains a history
 * of visited scenes for easy navigation back and forth.
 */
public class Main extends Application {

  private static final Deque<Parent> sceneHistory = new ArrayDeque<>();
  private static Stage primaryStage;
  private static Scene mainScene;

  /**
   * Switches the scene to the opening view.
   */
  public static void switchToOpeningView() {
    OpeningView openingView = new OpeningView(primaryStage);
    new OpeningController(openingView, primaryStage);
    mainScene.setRoot(openingView);
  }

  /**
   * Switches to a new scene and adds the current scene to the history.
   *
   * @param newSceneRoot the root of the new scene to switch to.
   */
  public static void switchScene(Parent newSceneRoot) {
    sceneHistory.push(mainScene.getRoot()); // Add current scene to history
    mainScene.setRoot(newSceneRoot); // Change to new scene
    MusicController.getInstance().getPageFlip().play();
  }

  /**
   * Switches to the previous scene in the history.
   */
  public static void switchToPreviousScene() {
    if (!sceneHistory.isEmpty()) {
      mainScene.setRoot(sceneHistory.pop()); // Change to the last scene in the history
    }
    MusicController.getInstance().getPageFlip().play();
  }

  /**
   * The main method for the application. Launches the JavaFX application.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Entry point for the JavaFX application. Initializes the main scene and displays the opening
   * view.
   *
   * @param primaryStage the primary stage for this application, onto which the application scene
   *                     can be set.
   */
  @Override
  public void start(Stage primaryStage) {
    Main.primaryStage = primaryStage;

    mainScene = new Scene(new VBox(), 1600, 900); // Initialize with an empty VBox
    primaryStage.setScene(mainScene);
    primaryStage.show();

    switchToOpeningView();
  }
}
