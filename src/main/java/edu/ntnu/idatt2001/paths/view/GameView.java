package edu.ntnu.idatt2001.paths.view;

import edu.ntnu.idatt2001.paths.controller.GameController;
import edu.ntnu.idatt2001.paths.model.game.GameInstance;
import edu.ntnu.idatt2001.paths.model.player.Player;
import java.util.Objects;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * View class for Game.
 */
public class GameView extends VBox {

  /**
   * Constructor for the GameView class. Initializes the components of the UI and arranges them on
   * the stage.
   *
   * @param primaryStage the primary stage on which the UI components are arranged
   */
  public GameView(Stage primaryStage) {
    // Initialize root before creating GameController

    UniversalMenuBar universalMenuBar = new UniversalMenuBar();
    getChildren().add(universalMenuBar);
    setPrefSize(1600, 900);

    Label titleLabel = new Label();
    ImageView passageContentImageView = new ImageView();
    ImageView passageBackgroundImageView = new ImageView();
    Label contentLabel = new Label();
    VBox linksVBox = new VBox(10);
    linksVBox.setAlignment(Pos.CENTER);

    Button inventoryButton = new Button("Inventory");
    // Pass the player to the GameController constructor
    Player player = GameInstance.getInstance().getPlayer();
    GameController gameController = new GameController(titleLabel, contentLabel,
        linksVBox, passageBackgroundImageView, passageContentImageView, inventoryButton);
    universalMenuBar.setGameController(gameController);

    // Add a change listener to the backgroundImageView's image property
    gameController.getBackgroundImageView().imageProperty()
        .addListener((observable, oldValue, newValue) -> {
          // Create a new BackgroundImage
          BackgroundImage backgroundImage = new BackgroundImage(
              newValue,
              BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
              new BackgroundSize(1.0, 1.0, true, true, false, true)
          );
          // Create a new Background with the new BackgroundImage
          Background newBackground = new Background(backgroundImage);
          // Set the new Background to the root
          setBackground(newBackground);
        });

    // Create an ImageView to display the player's image
    ImageView playerImageView = new ImageView();
    playerImageView.setPreserveRatio(true);

    passageContentImageView.setPreserveRatio(true);

    if (player.getImage() != null) {
      playerImageView.setImage(player.getImage());
      System.out.println(player.getImage().getUrl());
    } else {
      System.out.println("Player image is null");
    }

    // Create a BackgroundImage object
    BackgroundImage backgroundImage = new BackgroundImage(
        passageBackgroundImageView.getImage(),
        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
    );
    // Create a Background object with the BackgroundImage
    Background background = new Background(backgroundImage);
    setBackground(background);
    GridPane mainGrid = new GridPane();
    mainGrid.setPrefSize(1600, 900);
    ColumnConstraints mainColumn1 = new ColumnConstraints();
    mainColumn1.setPercentWidth(50);
    ColumnConstraints mainColumn2 = new ColumnConstraints();
    mainColumn2.setPercentWidth(50);
    RowConstraints mainRow1 = new RowConstraints();
    mainRow1.setPercentHeight(100);
    mainGrid.getColumnConstraints().addAll(mainColumn1, mainColumn2);
    mainGrid.getRowConstraints().add(mainRow1);
    getChildren().add(mainGrid);

    GridPane nestedGrid = new GridPane();
    RowConstraints nestedRow1 = new RowConstraints();
    nestedRow1.setPercentHeight(50);
    RowConstraints nestedRow2 = new RowConstraints();
    nestedRow2.setPercentHeight(35);
    RowConstraints nestedRow3 = new RowConstraints();
    nestedRow3.setPercentHeight(15);
    ColumnConstraints nestedColumn1 = new ColumnConstraints();
    nestedColumn1.setPercentWidth(100);
    nestedGrid.getColumnConstraints().add(nestedColumn1);
    nestedGrid.getRowConstraints().addAll(nestedRow1, nestedRow2, nestedRow3);
    mainGrid.getChildren().add(nestedGrid);

    GridPane textGrid = new GridPane();
    RowConstraints textRow1 = new RowConstraints();
    textRow1.setPercentHeight(30);
    RowConstraints textRow2 = new RowConstraints();
    textRow2.setPercentHeight(70);
    ColumnConstraints textColumn1 = new ColumnConstraints();
    textColumn1.setPercentWidth(100);
    textGrid.getColumnConstraints().add(textColumn1);
    textGrid.getRowConstraints().addAll(textRow1, textRow2);
    nestedGrid.getChildren().add(textGrid);

    titleLabel.getStyleClass().add("largeText");
    titleLabel.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    GridPane.setConstraints(titleLabel, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    textGrid.getChildren().add(titleLabel);

    GridPane.setConstraints(contentLabel, 0, 1, 1, 1, HPos.CENTER, VPos.TOP);
    contentLabel.setPadding(new Insets(0, 0, 0, 20));
    contentLabel.getStyleClass().add("textParagraph");
    contentLabel.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    contentLabel.setWrapText(true);
    textGrid.getChildren().add(contentLabel);

    GridPane.setConstraints(passageContentImageView, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    passageContentImageView.setFitWidth(800);
    passageContentImageView.setEffect(new DropShadow(10, Color.WHITE));
    mainGrid.getChildren().add(passageContentImageView);

    GridPane.setConstraints(linksVBox, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
    nestedGrid.getChildren().add(linksVBox);

    inventoryButton.getStyleClass().addAll("transparentButton", "continueText");
    inventoryButton.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).toExternalForm(),
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    GridPane.setConstraints(inventoryButton, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);
    inventoryButton.setPadding(new Insets(1, 1, 1, 1));
    nestedGrid.getChildren().add(inventoryButton);

    primaryStage.setTitle("Non-linear Story Game");
  }
}
