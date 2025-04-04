package edu.ntnu.idatt2001.paths.controller;

import edu.ntnu.idatt2001.paths.model.Link;
import edu.ntnu.idatt2001.paths.model.Passage;
import edu.ntnu.idatt2001.paths.model.Story;
import edu.ntnu.idatt2001.paths.model.StoryFileManager;
import edu.ntnu.idatt2001.paths.model.actions.Action;
import edu.ntnu.idatt2001.paths.model.actions.GoldAction;
import edu.ntnu.idatt2001.paths.model.actions.HealthAction;
import edu.ntnu.idatt2001.paths.model.actions.ScoreAction;
import edu.ntnu.idatt2001.paths.model.actions.inventory.InventoryAction;
import edu.ntnu.idatt2001.paths.model.actions.inventory.InventoryItemEnum;
import edu.ntnu.idatt2001.paths.model.exceptions.ActionValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.LinkValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.StoryValidationException;
import edu.ntnu.idatt2001.paths.model.validations.LinkValidator;
import edu.ntnu.idatt2001.paths.model.validations.StoryValidator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

/**
 * Controller class for creating and managing Story instances.
 */
public class CreateStoryController {

  private final StoryFileManager fileManagement;
  private Story story;
  private boolean hasOpening;

  /**
   * Constructor for CreateStoryController.
   *
   * @param fileManagement instance of StoryFileManager for file handling operations.
   */
  public CreateStoryController(StoryFileManager fileManagement) {
    this.fileManagement = fileManagement;
  }

  /**
   * Handles the creation of a Story instance given its title.
   *
   * @param title The title of the story.
   * @throws StoryValidationException If validation of the story fails.
   */
  public void handleStoryTitle(String title) throws StoryValidationException {
    story = new Story(title);
  }

  /**
   * Handles the creation of a passage instance given its storyTitle, title, content,
   * backgroundImage and contentImage.
   *
   * @param storyTitle      The title of the story.
   * @param title           The title of the passage.
   * @param content         The content of the passage.
   * @param backgroundImage The background image of the passage.
   * @param contentImage    The content image of the passage.
   * @return Passage.
   */
  public Passage handlePassage(String storyTitle, String title, String content,
      Image backgroundImage, Image contentImage) {
    Passage newPassage;

    if (backgroundImage != null && contentImage != null) {
      newPassage = new Passage(title, content, backgroundImage, contentImage);
    } else if (backgroundImage != null) {
      newPassage = new Passage(title, content, backgroundImage);
    } else if (contentImage != null) {
      newPassage = new Passage(title, content);
      newPassage.setContentImage(contentImage);
    } else {
      newPassage = new Passage(title, content);
    }
    newPassage.validatePassageWithOutLinks();
    if (title != null) {
      try {
        if (!hasOpening) {
          story = new Story(storyTitle, newPassage);
          hasOpening = true;
        } else {
          if (story != null) {
            if (!story.getPassages().isEmpty()) {
              StoryValidator.validateAddPassage(newPassage, story.getPassagesMap());
            }

            story.addPassage(newPassage);
          }
        }
      } catch (StoryValidationException e) {
        handleException(e);
      }
    }
    return newPassage;
  }

  /**
   * Handles the addition of a Link to a Passage.
   *
   * @param passage   The Passage to which the Link will be added.
   * @param text      The text description of the Link.
   * @param reference The reference passage of the Link.
   * @throws LinkValidationException If validation of the link fails.
   */
  public void handleLink(Passage passage, String text, String reference)
      throws LinkValidationException {
    Link link = new Link(text, reference);
    passage.addLink(link);
  }

  /**
   * Handles the addition of an Action to a Link.
   *
   * @param link       The Link to which the Action should be added.
   * @param ActionName The name of the Action to be added.
   * @param type       The type of Action to be added.
   * @throws ActionValidationException if Action is illegal
   */
  public void handleAction(Link link, String ActionName, String type)
      throws ActionValidationException {
    Action<?> action = switch (ActionName) {
      case "GoldAction" -> new GoldAction(Integer.parseInt(type));
      case "InventoryAction" -> new InventoryAction(type);
      case "ScoreAction" -> new ScoreAction(Integer.parseInt(type));
      case "HealthAction" -> new HealthAction(Integer.parseInt(type));
      default -> throw new IllegalArgumentException("Unknown action type: " + ActionName);
    };
    link.addAction(action);
  }

  /**
   * Gets the list of Inventory items
   *
   * @return the inventoryItems
   */
  public List<String> getInventoryItems() {
    return Arrays.stream(InventoryItemEnum.values())
        .map(InventoryItemEnum::getName)
        .collect(Collectors.toList());
  }

  /**
   * Handles the addition of an item requirement to a Link.
   *
   * @param link The link to which the item requirement should be added.
   * @param text The item that should be required.
   */
  public void handleItemRequirementForLink(Link link, String text) {
    InventoryAction inventoryAction = new InventoryAction(text);
    link.setRequiredItem(inventoryAction);
  }

  /**
   * Whether the required item has been acquired.
   *
   * @param link The Link that contains the item requirement.
   * @return Whether the required item has been acquired.
   */
  public boolean statusOfItemRequirement(Link link) {
    return !link.linkHasItemRequirement();
  }

  public Link getLastLink(Passage passage) {
    return passage.getLastLink();
  }

  /**
   * Saves the current Story instance to a file.
   *
   * @param directoryPath The directory path where the file will be saved.
   * @throws IOException              If an I/O error occurs.
   * @throws StoryValidationException If validation of the story fails.
   */
  public void saveStory(String directoryPath) throws IOException, StoryValidationException {
    StoryValidator.validateStory(story.getTitle(), story.getPassagesMap(),
        story.getOpeningPassage());
    for (Passage passageInStory : story.getPassagesMap().values()) {
      passageInStory.validatePassage();
      for (Link linkLinkInPassage : passageInStory.getLinks()) {
        LinkValidator.validateLink(linkLinkInPassage.getText(),
            linkLinkInPassage.getReference(), linkLinkInPassage.getActions());
        StoryValidator.validateGetPassage(linkLinkInPassage, story.getPassagesMap());
      }
    }
    fileManagement.saveStoryToFile(story, directoryPath);
  }

  /**
   * Gets a list of all background images.
   *
   * @return List of all background images.
   */
  public List<Image> getBackGroundImages() {
    List<Image> images = new ArrayList<>();
    try {
      Path dir = Paths.get(
          Objects.requireNonNull(
              getClass().getResource("/images/passageImages/passageBackGroundImages/")).toURI());
      addImagesToList(images, dir);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return images;
  }

  /**
   * Adds images to a list
   *
   * @param images List of images.
   * @param dir    Directory location of images.
   * @throws IOException if File is error
   */
  private void addImagesToList(List<Image> images, Path dir) throws IOException {
    List<Path> files = Files.list(dir)
        .filter(file -> !Files.isDirectory(file) && (file.toString().endsWith(".png")
            || file.toString().endsWith(".jpg") || file.toString().endsWith(".jpeg")))
        .toList();

    for (Path file : files) {
      images.add(new Image(file.toUri().toString()));
    }
  }

  /**
   * Gets a list of all content images.
   *
   * @return List of all content images.
   */
  public List<Image> getContentImages() {
    List<Image> images = new ArrayList<>();
    try {
      Path dir = Paths.get(
          Objects.requireNonNull(
              getClass().getResource("/images/passageImages/passageContentImages/")).toURI());
      addImagesToList(images, dir);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return images;
  }

  /**
   * Saves image to uploadedImages directory.
   *
   * @param imageFile Image to be uploaded to uploadedImages directory.
   */
  public void saveImageToResources(File imageFile) {
    try {
      Path destinationPath = Paths.get(
          "./uploadedImages/", imageFile.getName());
      Files.copy(imageFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to handle exceptions and display alerts with error information.
   *
   * @param e The exception that occurred.
   */
  private void handleException(Exception e) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Invalid input");
    alert.setHeaderText("Validation Error:");
    alert.setContentText(e.getMessage());
    alert.showAndWait();
  }


}


