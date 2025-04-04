package edu.ntnu.idatt2001.paths.view;

import edu.ntnu.idatt2001.paths.Main;
import edu.ntnu.idatt2001.paths.controller.CreateStoryController;
import edu.ntnu.idatt2001.paths.model.Link;
import edu.ntnu.idatt2001.paths.model.Passage;
import edu.ntnu.idatt2001.paths.model.exceptions.ActionValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.ImageValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.LinkValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.PassageValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.StoryValidationException;
import edu.ntnu.idatt2001.paths.model.validations.ImageValidator;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * View class for CreateStory.
 */
public class CreateStoryView extends VBox {

  private final CreateStoryController controller;
  private final UniversalMenuBar universalMenuBar;
  private final Stage stage;
  private final TextField enterStoryTitle = new TextField();
  private Passage currentPassage;
  private Button createLinkButton;
  private Button addAnotherLinkButton;
  private Button addItemRequirementForLink;
  private Button addAnotherPassageButton;
  private Button createStoryButton;
  private Button addAction;
  private Button addAnotherActionButton;
  private GridPane linkReferencePane;
  private GridPane linkTextPane;
  private ImageView selectedImage;
  private FileChooser fileChooser;
  private ImageView selectedBackgroundImage = null;
  private ImageView selectedContentImage = null;


  /**
   * Constructor for the CreateStoryView class.
   *
   * @param controller       controller for CreateStoryController.
   * @param universalMenuBar menu bar.
   * @param stage            the primary stage on which the UI components are arranged.
   */
  public CreateStoryView(CreateStoryController controller, UniversalMenuBar universalMenuBar,
      Stage stage) {
    this.controller = controller;
    this.universalMenuBar = universalMenuBar;
    this.stage = stage;
    initComponents();
  }

  static void initiatePane(GridPane imagePane) {
    ColumnConstraints imageColumn1 = new ColumnConstraints();
    imageColumn1.setPercentWidth(50);
    ColumnConstraints imageColumn2 = new ColumnConstraints();
    imageColumn2.setPercentWidth(50);
    RowConstraints imageRow1 = new RowConstraints();
    imageRow1.setPercentHeight(100);
    imagePane.getColumnConstraints().addAll(imageColumn1, imageColumn2);
    imagePane.getRowConstraints().add(imageRow1);
  }

  static void initiateLinkText(GridPane linkTextPane, RowConstraints linkTextRow1) {
    linkTextRow1.setPercentHeight(33.33);
    RowConstraints linkTextRow2 = new RowConstraints();
    linkTextRow2.setPercentHeight(33.33);
    RowConstraints linkTextRow3 = new RowConstraints();
    linkTextRow3.setPercentHeight(33.34);
    ColumnConstraints linkTextColumn1 = new ColumnConstraints();
    linkTextColumn1.setPercentWidth(100);
    linkTextPane.getColumnConstraints().add(linkTextColumn1);
    linkTextPane.getRowConstraints().addAll(linkTextRow1, linkTextRow2, linkTextRow3);
  }

  private void initComponents() {
    getChildren().add(universalMenuBar);
    setPrefSize(1600, 900);
    getStyleClass().add("background");
    getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/background.css")).toExternalForm());

    GridPane gridPane = new GridPane();
    RowConstraints Row1 = new RowConstraints();
    Row1.setPercentHeight(33.33);
    setRowConstraintsToGridPane(gridPane, Row1);

    Label storyTitleText = new Label("Enter the title of your story");
    storyTitleText.getStyleClass().add("largeText");
    storyTitleText.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    GridPane.setHalignment(storyTitleText, HPos.CENTER);
    GridPane.setValignment(storyTitleText, VPos.CENTER);
    GridPane.setConstraints(storyTitleText, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    gridPane.getChildren().add(storyTitleText);

    enterStoryTitle.setTextFormatter(
        new TextFormatter<>(TextFieldConsumers.getSymbolAndSizeFilter()));
    enterStoryTitle.setMaxWidth(600);
    enterStoryTitle.setPrefWidth(600);
    enterStoryTitle.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    enterStoryTitle.getStyleClass().addAll("textField", "textFieldText");
    GridPane.setHalignment(enterStoryTitle, HPos.CENTER);
    GridPane.setValignment(enterStoryTitle, VPos.CENTER);
    GridPane.setConstraints(enterStoryTitle, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
    gridPane.getChildren().add(enterStoryTitle);

    Button createStoryTitleButton = new Button("Create story title");
    createStoryTitleButton.setMnemonicParsing(false);
    createStoryTitleButton.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).
            toExternalForm(), Objects.requireNonNull(getClass().getResource("/styles/text.css"))
            .toExternalForm());
    createStoryTitleButton.getStyleClass().addAll("transparentButton", "continueText");
    GridPane.setHalignment(createStoryTitleButton, HPos.CENTER);
    GridPane.setValignment(createStoryTitleButton, VPos.CENTER);
    createStoryTitleButton.setPadding(new Insets(1, 1, 1, 1));
    GridPane.setConstraints(createStoryTitleButton, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);
    gridPane.getChildren().add(createStoryTitleButton);

    createStoryTitleButton.setOnAction(e -> {
      try {
        controller.handleStoryTitle(enterStoryTitle.getText());
      } catch (StoryValidationException ex) {
        handleException(ex);
        return;
      }
      System.out.println("Created Story with title: " + enterStoryTitle.getText());
      getChildren().clear();
      showPassageCreationView();
    });
  }

  private void showPassageCreationView() {
    getChildren().add(universalMenuBar);
    GridPane gridPane = new GridPane();
    RowConstraints Row1 = new RowConstraints();
    Row1.setMaxHeight(165);
    Row1.setVgrow(Priority.NEVER);
    RowConstraints Row2 = new RowConstraints();
    Row2.setMaxHeight(150);
    Row2.setVgrow(Priority.NEVER);
    RowConstraints Row3 = new RowConstraints();
    Row3.setMaxHeight(80);
    Row3.setVgrow(Priority.NEVER);
    RowConstraints Row4 = new RowConstraints();
    Row4.setMaxHeight(396);
    Row4.setVgrow(Priority.NEVER);
    RowConstraints Row5 = new RowConstraints();
    Row5.setMaxHeight(90);
    Row5.setVgrow(Priority.NEVER);
    ColumnConstraints Column1 = new ColumnConstraints();
    Column1.setPercentWidth(100);
    gridPane.getColumnConstraints().add(Column1);
    gridPane.getRowConstraints().addAll(Row1, Row2, Row3, Row4, Row5);
    gridPane.setPrefSize(1600, 900);
    getChildren().add(gridPane);

    GridPane titlePane = new GridPane();
    RowConstraints titleRow1 = new RowConstraints();
    titleRow1.setPercentHeight(50);
    RowConstraints titleRow2 = new RowConstraints();
    titleRow2.setPercentHeight(50);
    ColumnConstraints titleColumn1 = new ColumnConstraints();
    titleColumn1.setPercentWidth(100);
    titlePane.getColumnConstraints().add(titleColumn1);
    titlePane.getRowConstraints().addAll(titleRow1, titleRow2);
    gridPane.getChildren().add(titlePane);

    GridPane contentPane = new GridPane();
    RowConstraints contentRow1 = new RowConstraints();
    contentRow1.setPercentHeight(50);
    RowConstraints contentRow2 = new RowConstraints();
    contentRow2.setPercentHeight(50);
    ColumnConstraints contentColumn1 = new ColumnConstraints();
    contentColumn1.setPercentWidth(100);
    contentPane.getColumnConstraints().add(contentColumn1);
    contentPane.getRowConstraints().addAll(contentRow1, contentRow2);
    GridPane.setConstraints(contentPane, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
    gridPane.getChildren().add(contentPane);

    GridPane imagePane = new GridPane();
    initiatePane(imagePane);
    GridPane.setConstraints(imagePane, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);
    gridPane.getChildren().add(imagePane);

    Label passageTitleText = new Label("Enter the title of your passage");
    Label passageContentText = new Label(" Enter the content of your passage: ");

    TextField enterPassageTitle = initiatePassageTextFields(titlePane, passageTitleText);
    enterPassageTitle.setTextFormatter(
        new TextFormatter<>(TextFieldConsumers.getSymbolAndSizeFilterPlus()));
    TextField enterPassageContent = initiatePassageTextFields(contentPane, passageContentText);
    enterPassageTitle.setTextFormatter(
        new TextFormatter<>(TextFieldConsumers.getSymbolAndSizeFilterPlus()));

    fileChooser = new FileChooser();

    List<Image> backgroundImages = controller.getBackGroundImages();
    List<Image> contentImages = controller.getContentImages();

    GridPane imageGrid = new GridPane();
    imageGrid.setVgap(50); // Vertical gap
    GridPane.setMargin(imageGrid, new Insets(40, 0, 0, 0));

    // Set equal width and height for all cells
    for (int i = 0; i < 5; i++) {
      ColumnConstraints colConst = new ColumnConstraints();
      colConst.setPercentWidth(100.0 / 5);
      imageGrid.getColumnConstraints().add(colConst);

      RowConstraints rowConst = new RowConstraints();
      rowConst.setPercentHeight(100.0 / 5);
      imageGrid.getRowConstraints().add(rowConst);
    }

    GridPane.setConstraints(imageGrid, 0, 3, 1, 1, HPos.CENTER, VPos.TOP);
    gridPane.getChildren().add(imageGrid);

    Button showBackgroundImagesButton = new Button("Select Backgrounds");
    showBackgroundImagesButton.getStyleClass().addAll("transparentButton", "continueText");
    showBackgroundImagesButton.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).
            toExternalForm(), Objects.requireNonNull(getClass().getResource("/styles/text.css"))
            .toExternalForm());
    showBackgroundImagesButton.setPadding(new Insets(1, 1, 1, 1));
    GridPane.setConstraints(showBackgroundImagesButton, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    showBackgroundImagesButton.setOnAction(e -> {
      initiateImagesInGridPane(backgroundImages, imageGrid, true);
      addUploadButton(imageGrid, backgroundImages, true);
      imageGrid.setVisible(true);
    });
    imagePane.getChildren().add(showBackgroundImagesButton);

    Button showContentImagesButton = new Button("Select Content");
    showContentImagesButton.getStyleClass().addAll("transparentButton", "continueText");
    showContentImagesButton.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).
            toExternalForm(), Objects.requireNonNull(
            getClass().getResource("/styles/text.css")).toExternalForm());
    showContentImagesButton.setPadding(new Insets(1, 1, 1, 1));
    GridPane.setConstraints(showContentImagesButton, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    showContentImagesButton.setOnAction(e -> {
      initiateImagesInGridPane(contentImages, imageGrid, false);
      addUploadButton(imageGrid, contentImages, false);
      imageGrid.setVisible(true);
    });
    imagePane.getChildren().add(showContentImagesButton);

    Button createPassageButton = new Button("Create Passage");
    createPassageButton.getStyleClass().addAll("transparentButton", "continueText");
    createPassageButton.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).
            toExternalForm(), Objects.requireNonNull(getClass().getResource("/styles/text.css"))
            .toExternalForm());
    createPassageButton.setPadding(new Insets(1, 1, 1, 1));
    GridPane.setConstraints(createPassageButton, 0, 4, 1, 1, HPos.CENTER, VPos.CENTER);
    createPassageButton.setOnAction(e -> {
      Image backgroundImage =
          selectedBackgroundImage != null ? selectedBackgroundImage.getImage() : null;
      Image contentImage = selectedContentImage != null ? selectedContentImage.getImage() : null;
      try {
        currentPassage = controller.handlePassage(enterStoryTitle.getText(),
            enterPassageTitle.getText(), enterPassageContent.getText(), backgroundImage,
            contentImage);
      } catch (PassageValidationException ex) {
        handleException(ex);
        return;
      }
      getChildren().clear();
      initializeCommonButtons();
      showLinkCreationView();
    });
    gridPane.getChildren().add(createPassageButton);
  }

  private TextField initiatePassageTextFields(GridPane titlePane, Label passageText) {
    return getTextField(titlePane, passageText);
  }

  private TextField initiatePassageTitleTextField() {
    TextField enterPassageTitle = new TextField();
    enterPassageTitle.getStyleClass().addAll("textField", "textFieldText");
    enterPassageTitle.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    GridPane.setConstraints(enterPassageTitle, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
    enterPassageTitle.setMaxWidth(635);
    enterPassageTitle.setAlignment(Pos.CENTER);
    return enterPassageTitle;
  }

  private void initiateImagesInGridPane(List<Image> images, GridPane imageGrid,
      boolean isBackgroundImage) {
    imageGrid.getChildren().clear();
    for (int i = 0; i < Math.min(9, images.size()); i++) {
      ImageView imageView = new ImageView(images.get(i));
      imageView.setPreserveRatio(true);
      imageView.setFitWidth(Double.MAX_VALUE);
      imageView.setFitHeight(Double.MAX_VALUE);
      GridPane.setFillWidth(imageView, true);
      GridPane.setFillHeight(imageView, true);
      GridPane.setHalignment(imageView, HPos.CENTER);
      GridPane.setValignment(imageView, VPos.CENTER);
      addImageViewsToGrid(imageView, isBackgroundImage);
      imageGrid.add(imageView, i % 5, i / 5);
    }
  }

  private void addUploadButton(GridPane imageGrid, List<Image> images, boolean isBackgroundImage) {
    Button uploadButton = new Button("Upload");
    GridPane.setHalignment(uploadButton, HPos.CENTER);
    GridPane.setValignment(uploadButton, VPos.CENTER);
    uploadButton.getStyleClass().addAll("transparentButton", "continueText");
    uploadButton.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).
            toExternalForm(), Objects.requireNonNull(getClass().getResource("/styles/text.css"))
            .toExternalForm());
    uploadButton.setPadding(new Insets(1, 1, 1, 1));
    uploadButton.setOnAction(e -> {
      File file = fileChooser.showOpenDialog(null);
      if (file != null) {
        try {
          // Validate the image file
          ImageValidator.validateImage(file);
        } catch (ImageValidationException ex) {
          handleException(ex);
          return;
        }

        controller.saveImageToResources(file);
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);

        // Set the size of the uploaded image here
        imageView.setFitWidth(200); // Adjust this value as needed
        imageView.setFitHeight(200); // Adjust this value as needed
        imageView.setPreserveRatio(true);

        // Replace the last image in the grid with the new image
        if (images.size() > 9) {
          images.set(9, image);
        } else {
          images.add(image);
        }

        // Refresh the grid
        imageGrid.getChildren().clear();
        for (int i = 0; i < images.size(); i++) {
          ImageView currentImageView = new ImageView(images.get(i));
          // Set the size of the uploaded image here
          currentImageView.setFitWidth(200); // Adjust this value as needed
          currentImageView.setFitHeight(200); // Adjust this value as needed
          currentImageView.setPreserveRatio(true);

          // Add click listener to ImageView
          addImageViewsToGrid(currentImageView, isBackgroundImage);

          imageGrid.add(currentImageView, i % 5, i / 5);
        }

        // Highlight the uploaded image
        imageView.setEffect(new DropShadow(20, Color.WHITE));

        // Set the newly uploaded image as selected
        if (isBackgroundImage) {
          selectedBackgroundImage = imageView;
        } else {
          selectedContentImage = imageView;
        }
      }
    });

    imageGrid.add(uploadButton, 9 % 5, 9 / 5);
  }

  private void addImageViewsToGrid(ImageView imageView, boolean isBackgroundImage) {
    imageView.setFitWidth(200); // Adjust this value as needed
    imageView.setFitHeight(200); // Adjust this value as needed
    imageView.setPreserveRatio(true);

    imageView.setOnMouseClicked(e -> {
      if (selectedImage != null) {
        // Unhighlight the previously selected image
        selectedImage.setEffect(null);
      }
      selectedImage = imageView;
      // Highlight the selected image
      selectedImage.setEffect(new DropShadow(20, Color.WHITE));

      // Set the newly uploaded image as selected
      if (isBackgroundImage) {
        selectedBackgroundImage = imageView;
      } else {
        selectedContentImage = imageView;
      }
    });
  }

  private void showLinkCreationView() {
    // hiding components
    toggleComponentsVisibility(false);
    if (controller.getLastLink(currentPassage) != null) {
      addItemRequirementForLink.setVisible(controller.
          statusOfItemRequirement(controller.getLastLink(currentPassage)));
    }
    getChildren().add(universalMenuBar);
    GridPane gridPane = new GridPane();
    RowConstraints Row1 = new RowConstraints();
    Row1.setPercentHeight(33.33);
    Row1.setVgrow(Priority.ALWAYS);
    RowConstraints Row2 = new RowConstraints();
    Row2.setPercentHeight(33.33);
    Row2.setVgrow(Priority.ALWAYS);
    RowConstraints Row3 = new RowConstraints();
    Row3.setPercentHeight(33.34);
    Row3.setVgrow(Priority.ALWAYS);
    ColumnConstraints Column1 = new ColumnConstraints();
    Column1.setPercentWidth(100);
    Column1.setHgrow(Priority.ALWAYS);
    gridPane.getColumnConstraints().add(Column1);
    gridPane.getRowConstraints().addAll(Row1, Row2, Row3);
    gridPane.setPrefSize(1600, 900);
    getChildren().add(gridPane);

    linkTextPane = new GridPane();
    RowConstraints linkTextRow1 = new RowConstraints();
    initiateLinkText(linkTextPane, linkTextRow1);
    GridPane.setConstraints(linkTextPane, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    gridPane.getChildren().add(linkTextPane);

    linkReferencePane = new GridPane();
    RowConstraints linkReferenceRow1 = new RowConstraints();
    linkReferenceRow1.setPercentHeight(33.33);
    RowConstraints linkReferenceRow2 = new RowConstraints();
    linkReferenceRow1.setPercentHeight(33.33);
    RowConstraints linkReferenceRow3 = new RowConstraints();
    linkReferenceRow3.setPercentHeight(33.34);
    ColumnConstraints linkReferenceColumn1 = new ColumnConstraints();
    linkReferenceColumn1.setPercentWidth(100);
    linkReferencePane.getColumnConstraints().add(linkReferenceColumn1);
    linkReferencePane.getRowConstraints()
        .addAll(linkReferenceRow1, linkReferenceRow2, linkReferenceRow3);
    GridPane buttonPane = initiateButtonPane(gridPane, linkReferencePane);

    Label linkTextText = new Label(" Enter the Text for your link");
    Label linkReferenceText = new Label(" Enter the reference for your link");
    TextField enterLinkText = initiateLinkTextLabel(linkTextPane, linkTextText);
    TextField enterLinkReference = initiateLinkTextLabel(linkReferencePane, linkReferenceText);

    createLinkButton = new Button("Create Link");
    GridPane.setConstraints(createLinkButton, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);
    createLinkButton.getStyleClass().addAll("transparentButton", "continueText");
    createLinkButton.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).
            toExternalForm(), Objects.requireNonNull(getClass().getResource("/styles/text.css"))
            .toExternalForm());
    createLinkButton.setPadding(new Insets(1, 1, 1, 1));
    createLinkButton.setOnAction(e -> {
      try {
        controller.handleLink(currentPassage, enterLinkText.getText(),
            enterLinkReference.getText());
      } catch (LinkValidationException | PassageValidationException ex) {
        handleException(ex);
        return;
      }
      createLinkButton.setVisible(false);
      linkReferencePane.setVisible(false);
      linkTextPane.setVisible(false);
      //showing components
      toggleComponentsVisibility(true);
      System.out.println("Created link with text: " + enterLinkText.getText() + "\n and reference"
          + enterLinkReference.getText());
    });

    gridPane.getChildren().add(createLinkButton);
    buttonPane.getChildren().addAll(addAction, addAnotherLinkButton, addAnotherPassageButton,
        createStoryButton);
    gridPane.getChildren().add(addItemRequirementForLink);
  }

  private TextField initiateLinkTextLabel(GridPane linkPane, Label linkText) {
    return getTextField(linkPane, linkText);
  }

  private TextField getTextField(GridPane linkPane, Label linkText) {
    linkText.getStyleClass().add("largeText");
    linkText.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    GridPane.setConstraints(linkText, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    linkPane.getChildren().add(linkText);
    TextField enterLinkText = initiatePassageTitleTextField();
    enterLinkText.setTextFormatter(
        new TextFormatter<>(TextFieldConsumers.getSymbolAndSizeFilter()));

    linkPane.getChildren().add(enterLinkText);
    return enterLinkText;
  }

  private GridPane initiateButtonPane(GridPane gridPane, GridPane linkReferencePane) {
    GridPane.setConstraints(linkReferencePane, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
    gridPane.getChildren().add(linkReferencePane);

    GridPane buttonPane = new GridPane();
    RowConstraints buttonRow1 = new RowConstraints();
    buttonRow1.setPercentHeight(50);
    RowConstraints buttonRow2 = new RowConstraints();
    buttonRow2.setPercentHeight(50);
    ColumnConstraints buttonColumn1 = new ColumnConstraints();
    buttonColumn1.setPercentWidth(50);
    ColumnConstraints buttonColumn2 = new ColumnConstraints();
    buttonColumn2.setPercentWidth(50);
    buttonPane.getColumnConstraints().addAll(buttonColumn1, buttonColumn2);
    buttonPane.getRowConstraints().addAll(buttonRow1, buttonRow2);
    GridPane.setConstraints(buttonPane, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);
    buttonPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    GridPane.setHgrow(buttonPane, Priority.ALWAYS);
    GridPane.setVgrow(buttonPane, Priority.ALWAYS);
    gridPane.getChildren().add(buttonPane);
    return buttonPane;
  }

  private void showActionCreationView() {
    toggleComponentsVisibility(false);

    getChildren().add(universalMenuBar);

    GridPane gridPane = new GridPane();
    RowConstraints Row1 = new RowConstraints();
    Row1.setPercentHeight(33.33);
    setRowConstraintsToGridPane(gridPane, Row1);

    GridPane actionPane = new GridPane();
    RowConstraints actionRow1 = new RowConstraints();
    actionRow1.setPercentHeight(50);
    RowConstraints actionRow2 = new RowConstraints();
    actionRow2.setPercentHeight(50);
    ColumnConstraints actionColumn1 = new ColumnConstraints();
    actionColumn1.setPercentWidth(100);
    actionPane.getColumnConstraints().add(actionColumn1);
    actionPane.getRowConstraints().addAll(actionRow1, actionRow2);
    gridPane.getChildren().add(actionPane);

    GridPane valuePane = new GridPane();
    RowConstraints valueRow1 = new RowConstraints();
    valueRow1.setPercentHeight(50);
    RowConstraints valueRow2 = new RowConstraints();
    valueRow2.setPercentHeight(50);
    ColumnConstraints valueColumn1 = new ColumnConstraints();
    valueColumn1.setPercentWidth(100);
    valuePane.getColumnConstraints().add(valueColumn1);
    valuePane.getRowConstraints().addAll(valueRow1, valueRow2);
    GridPane buttonPane = initiateButtonPane(gridPane, valuePane);

    // Add Action choice box
    Label actionTypeText = new Label("Choose an action type");
    GridPane.setConstraints(actionTypeText, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    actionTypeText.getStyleClass().add("largeText");
    actionTypeText.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    actionPane.getChildren().add(actionTypeText);

    ChoiceBox<String> actionTypeChoiceBox = new ChoiceBox<>();
    GridPane.setConstraints(actionTypeChoiceBox, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
    actionTypeChoiceBox.setMaxWidth(635);
    actionTypeChoiceBox.getStyleClass().addAll("textField", "textFieldText");
    actionTypeChoiceBox.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    actionTypeChoiceBox.getItems()
        .addAll("GoldAction", "InventoryAction", "ScoreAction", "HealthAction");
    actionPane.getChildren().add(actionTypeChoiceBox);

    // Add Action value text field
    Label actionValueText = new Label("Enter the value for your action");
    GridPane.setConstraints(actionValueText, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    actionValueText.getStyleClass().add("largeText");
    actionValueText.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    valuePane.getChildren().add(actionValueText);

    ChoiceBox<String> inventoryItemsChoiceBox = new ChoiceBox<>();
    inventoryItemsChoiceBox.getItems().addAll(controller.getInventoryItems());
    inventoryItemsChoiceBox.setVisible(false);
    valuePane.getChildren().add(inventoryItemsChoiceBox);

    TextField enterActionValue = new TextField();
    enterActionValue.setTextFormatter(
        new TextFormatter<>(TextFieldConsumers.getIntegerInRangeFilter()));

    enterActionValue.setVisible(false);
    GridPane.setConstraints(enterActionValue, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
    enterActionValue.setMaxWidth(635);
    enterActionValue.getStyleClass().addAll("textField", "textFieldText");
    enterActionValue.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    valuePane.getChildren().add(enterActionValue);

    //Listener for actiontype
    actionTypeChoiceBox.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (newValue.equals("InventoryAction")) {
            GridPane.setConstraints(inventoryItemsChoiceBox, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
            inventoryItemsChoiceBox.setMaxWidth(635);
            inventoryItemsChoiceBox.getStyleClass().addAll("textField", "textFieldText");
            inventoryItemsChoiceBox.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styles/text.css"))
                    .toExternalForm());
            inventoryItemsChoiceBox.setVisible(true);
            enterActionValue.setVisible(false);
          } else {
            GridPane.setConstraints(enterActionValue, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
            enterActionValue.setMaxWidth(635);
            enterActionValue.getStyleClass().addAll("textField", "textFieldText");
            enterActionValue.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styles/text.css"))
                    .toExternalForm());
            enterActionValue.setVisible(true);
            inventoryItemsChoiceBox.setVisible(false);
          }
        });

    // Handle action creation
    Button createActionButton = new Button("Add Action");
    GridPane.setConstraints(createActionButton, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);
    createActionButton.getStyleClass().addAll("transparentButton", "continueText");
    createActionButton.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).
            toExternalForm(), Objects.requireNonNull(getClass().getResource("/styles/text.css"))
            .toExternalForm());
    createActionButton.setOnAction(e -> {
      String actionType = actionTypeChoiceBox.getValue();
      String actionValue;
      if (actionType.equals("InventoryAction")) {
        actionValue = inventoryItemsChoiceBox.getValue();
      } else {
        actionValue = enterActionValue.getText();
      }
      Link lastLink = controller.getLastLink(currentPassage);
      try {
        controller.handleAction(lastLink, actionType, actionValue);
      } catch (ActionValidationException ex) {
        handleException(ex);
        return;
      }
      createActionButton.setVisible(false);
      toggleComponentsVisibility(true);
      actionPane.setVisible(false);
      valuePane.setVisible(false);
      addAnotherActionButton.setVisible(true);
      if (controller.getLastLink(currentPassage) != null) {
        addItemRequirementForLink.setVisible(controller.
            statusOfItemRequirement(controller.getLastLink(currentPassage)));
      }
      System.out.println("Created action with type: " + actionType + " and value: " + actionValue);
    });
    gridPane.getChildren().add(createActionButton);
    gridPane.getChildren().add(addItemRequirementForLink);

    addAnotherActionButton = new Button("Add Another Action");
    GridPane.setConstraints(addAnotherActionButton, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
    addAnotherActionButton.getStyleClass().addAll("transparentButton", "continueText");
    addAnotherActionButton.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).
            toExternalForm(), Objects.requireNonNull(getClass().getResource("/styles/text.css"))
            .toExternalForm());
    addAnotherActionButton.setVisible(false);
    addAnotherActionButton.setOnAction(e -> {
      getChildren().clear();
      showActionCreationView();
    });
    buttonPane.getChildren()
        .addAll(addAnotherActionButton, addAnotherLinkButton, addAnotherPassageButton,
            createStoryButton);
  }

  private void setRowConstraintsToGridPane(GridPane gridPane, RowConstraints row1) {
    RowConstraints Row2 = new RowConstraints();
    Row2.setPercentHeight(33.33);
    RowConstraints Row3 = new RowConstraints();
    Row3.setPercentHeight(33.34);
    ColumnConstraints Column1 = new ColumnConstraints();
    Column1.setPercentWidth(100);
    gridPane.getColumnConstraints().add(Column1);
    gridPane.getRowConstraints().addAll(row1, Row2, Row3);
    gridPane.setPrefSize(1600, 900);
    getChildren().add(gridPane);
  }

  private void toggleComponentsVisibility(boolean isVisible) {
    createStoryButton.setVisible(isVisible);
    addAnotherPassageButton.setVisible(isVisible);
    addAction.setVisible(isVisible);
    addAnotherLinkButton.setVisible(isVisible);
    addItemRequirementForLink.setVisible(isVisible);
  }

  private void initializeCommonButtons() {
    addAnotherLinkButton = new Button("Add Another Link");
    GridPane.setConstraints(addAnotherLinkButton, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    addAnotherLinkButton.getStyleClass().addAll("transparentButton", "continueText");
    addAnotherLinkButton.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).
            toExternalForm(), Objects.requireNonNull(getClass().getResource("/styles/text.css"))
            .toExternalForm());
    addAnotherLinkButton.setPadding(new Insets(1, 1, 1, 1));
    addAnotherLinkButton.setOnAction(e -> {
      getChildren().clear();
      showLinkCreationView();
    });

    addAnotherPassageButton = new Button("Add Another Passage");
    GridPane.setConstraints(addAnotherPassageButton, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    addAnotherPassageButton.getStyleClass().addAll("transparentButton", "continueText");
    addAnotherPassageButton.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).
            toExternalForm(), Objects.requireNonNull(getClass().getResource("/styles/text.css"))
            .toExternalForm());
    addAnotherPassageButton.setPadding(new Insets(0, 1, 1, 1));
    addAnotherPassageButton.setOnAction(e -> {
      getChildren().clear();
      showPassageCreationView();
    });

    createStoryButton = new Button("Confirm Story");
    GridPane.setConstraints(createStoryButton, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER);
    createStoryButton.getStyleClass().addAll("transparentButton", "continueText");
    createStoryButton.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).
            toExternalForm(), Objects.requireNonNull(getClass().getResource("/styles/text.css"))
            .toExternalForm());
    createStoryButton.setPadding(new Insets(1, 1, 1, 1));
    createStoryButton.setOnAction(event -> {

      String directoryPath = "userStories";
      try {
        controller.saveStory(directoryPath);
      } catch (IOException e) {
        throw new RuntimeException(e);
      } catch (StoryValidationException ex) {
        handleException(ex);
        return;
      }
      String msg = "Saved Story:" + enterStoryTitle.getText();
      handleSaving(msg);
      SelectStoryView selectStoryView = new SelectStoryView(stage);
      Main.switchScene(selectStoryView);
    });

    addAction = new Button("Add Action");
    GridPane.setConstraints(addAction, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
    addAction.getStyleClass().addAll("transparentButton", "continueText");
    addAction.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).
            toExternalForm(), Objects.requireNonNull(getClass().getResource("/styles/text.css"))
            .toExternalForm());
    addAction.setPadding(new Insets(1, 1, 1, 1));
    addAction.setOnAction(e -> {
      getChildren().clear();
      showActionCreationView();
    });

    addItemRequirementForLink = new Button("Add item requirement for Link");
    GridPane.setConstraints(addItemRequirementForLink, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
    addItemRequirementForLink.getStyleClass().addAll("transparentButton", "continueText");
    addItemRequirementForLink.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).
            toExternalForm(), Objects.requireNonNull(getClass().getResource("/styles/text.css"))
            .toExternalForm());
    addItemRequirementForLink.setPadding(new Insets(1, 1, 1, 1));
    addItemRequirementForLink.setOnAction(e -> {
      getChildren().clear();
      showItemRequirementCreationView();
    });
  }

  private void showItemRequirementCreationView() {
    // hiding components
    toggleComponentsVisibility(false);

    getChildren().add(universalMenuBar);
    GridPane gridPane = new GridPane();
    RowConstraints Row1 = new RowConstraints();
    Row1.setPercentHeight(33.33);
    Row1.setVgrow(Priority.ALWAYS);
    RowConstraints Row2 = new RowConstraints();
    Row2.setPercentHeight(33.33);
    Row2.setVgrow(Priority.ALWAYS);
    RowConstraints Row3 = new RowConstraints();
    Row3.setPercentHeight(33.34);
    Row3.setVgrow(Priority.ALWAYS);
    ColumnConstraints Column1 = new ColumnConstraints();
    Column1.setPercentWidth(100);
    Column1.setHgrow(Priority.ALWAYS);
    gridPane.getColumnConstraints().add(Column1);
    gridPane.getRowConstraints().addAll(Row1, Row2, Row3);
    gridPane.setPrefSize(1600, 900);
    getChildren().add(gridPane);

    GridPane linkItemRequirementPane = new GridPane();
    RowConstraints linkItemReqRow1 = new RowConstraints();
    initiateLinkText(linkItemRequirementPane, linkItemReqRow1);
    GridPane.setConstraints(linkItemRequirementPane, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    gridPane.getChildren().add(linkItemRequirementPane);

    Label linkItemRequirementText = new Label(" Enter requirement for link");
    GridPane.setConstraints(linkItemRequirementText, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
    linkItemRequirementText.getStyleClass().add("largeText");
    linkItemRequirementText.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    linkItemRequirementPane.getChildren().add(linkItemRequirementText);

    ChoiceBox<String> inventoryItemsChoiceBox = new ChoiceBox<>();
    inventoryItemsChoiceBox.getItems().addAll(controller.getInventoryItems());
    GridPane.setConstraints(inventoryItemsChoiceBox, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
    inventoryItemsChoiceBox.setMaxWidth(635);
    inventoryItemsChoiceBox.getStyleClass().addAll("textField", "textFieldText");
    inventoryItemsChoiceBox.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/styles/text.css")).toExternalForm());
    linkItemRequirementPane.getChildren().add(inventoryItemsChoiceBox);

    Button createItemRequirementForLink = new Button("Create item requirement for link");
    GridPane.setConstraints(createItemRequirementForLink, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);
    createItemRequirementForLink.getStyleClass().addAll("transparentButton", "continueText");
    createItemRequirementForLink.getStylesheets().addAll(
        Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).
            toExternalForm(), Objects.requireNonNull(getClass().getResource("/styles/text.css"))
            .toExternalForm());
    createItemRequirementForLink.setPadding(new Insets(1, 1, 1, 1));
    gridPane.getChildren().add(createItemRequirementForLink);
    createItemRequirementForLink.setVisible(true);
    createItemRequirementForLink.setOnAction(e -> {
      Link lastLink = currentPassage.getLinks().get(currentPassage.getLinks().size() - 1);
      try {
        controller.handleItemRequirementForLink(lastLink, inventoryItemsChoiceBox.getValue());
      } catch (LinkValidationException ex) {
        handleException(ex);
        return;
      }
      getChildren().clear();
      showLinkCreationView();
      createLinkButton.setVisible(false);
      linkReferencePane.setVisible(false);
      linkTextPane.setVisible(false);
      toggleComponentsVisibility(true);
      if (controller.getLastLink(currentPassage) != null) {
        addItemRequirementForLink.setVisible(controller.
            statusOfItemRequirement(controller.getLastLink(currentPassage)));
      }
    });
  }


  private void handleException(Exception e) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Invalid input");
    alert.setHeaderText("Validation Error:");
    alert.setContentText(e.getMessage());
    alert.showAndWait();
  }

  private void handleSaving(String msg) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Story saved");
    alert.setContentText(msg);
    alert.showAndWait();
  }
}
