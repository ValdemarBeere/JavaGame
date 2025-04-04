package edu.ntnu.idatt2001.paths.view;

import edu.ntnu.idatt2001.paths.Main;
import edu.ntnu.idatt2001.paths.controller.GameController;
import edu.ntnu.idatt2001.paths.controller.MusicController;
import edu.ntnu.idatt2001.paths.model.game.GameInstance;
import java.awt.Desktop;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * View class for UniversalMenu.
 */
public class UniversalMenuBar extends MenuBar {

  private GameController gameController;

  /**
   * Constructor for the UniversalMenuBar class. Initializes the components of the UI and arranges
   * them on the stage.
   */
  public UniversalMenuBar() {
    this.getStyleClass().add("translucentMenuBar");
    this.getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource("/styles/menu.css")).toExternalForm());

    Menu fileMenu = new Menu("File");
    MenuItem restartItem = new MenuItem("Restart");
    MenuItem homeItem = new MenuItem("Home");
    MenuItem exitItem = new MenuItem("Exit");
    MenuItem helpItem = new MenuItem("Help");
    MenuItem creditItem = new MenuItem("Credit");

    // Add MenuItems to fileMenu
    fileMenu.getItems().addAll(restartItem, homeItem, exitItem, helpItem, creditItem);

    Menu musicItem = new Menu("Music");
    MenuItem playItem = new MenuItem("Play");
    MenuItem pauseItem = new MenuItem("Pause");

    musicItem.getItems().addAll(playItem, pauseItem);

    restartItem.setOnAction(e -> {
      if (gameController != null) {
        // Create an alert and display it to the user
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("RESTART");
        alert.setHeaderText("Are you sure you want to restart\nDifficulty will be set to default");
        alert.setContentText("All your items will be cleared");

        // Customizing the buttons
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Show the alert and wait for the user's response
        Optional<ButtonType> result = alert.showAndWait();
        // If user confirmed the restart
        if (result.isPresent() && result.get() == buttonTypeYes) {
          GameInstance.getInstance().getPlayer().resetPlayer();
          gameController.initialize(GameInstance.getInstance().getStory());
        }
      } else {
        System.out.println("GameController is not set!");
      }
    });

    homeItem.setOnAction(e -> Main.switchToOpeningView());
    exitItem.setOnAction(e -> Platform.exit());
    helpItem.setOnAction(e -> {
      try {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop()
            .isSupported(Desktop.Action.BROWSE)) {
          Desktop.getDesktop().browse(new URI(
              "https://gitlab.stud.idi.ntnu.no/magngri/group11idatt2001/-/wikis/User-Manual"));
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });
    creditItem.setOnAction(e -> {
      try {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop()
            .isSupported(Desktop.Action.BROWSE)) {
          Desktop.getDesktop().browse(
              new URI("https://gitlab.stud.idi.ntnu.no/magngri/group11idatt2001/-/wikis/Credit"));
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });
    playItem.setOnAction(e -> MusicController.getInstance().getMenuMusic().play());
    pauseItem.setOnAction(e -> MusicController.getInstance().getMenuMusic().pause());

    // Add Menus to MenuBar
    this.getMenus().addAll(fileMenu, musicItem);
  }

  public void setGameController(GameController gameController) {
    this.gameController = gameController;
  }
}
