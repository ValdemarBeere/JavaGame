package edu.ntnu.idatt2001.paths.controller;

import edu.ntnu.idatt2001.paths.model.Story;
import edu.ntnu.idatt2001.paths.model.StoryFileManager;
import edu.ntnu.idatt2001.paths.model.exceptions.ActionValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.ImageValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.LinkValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.PassageValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.StoryValidationException;
import edu.ntnu.idatt2001.paths.model.game.GameInstance;
import edu.ntnu.idatt2001.paths.view.SelectStoryView;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Controller for SelectStory.
 */
public class SelectStoryController {

  private final SelectStoryView selectStoryView;
  private Story story;
  private Map<String, String> storyTitleToFileMap;
  private Consumer<Story> gameStarter;

  /**
   * Constructor for SelectStoryController.
   *
   * @param selectStoryView The view class of SelectStory.
   */
  public SelectStoryController(SelectStoryView selectStoryView) {
    this.selectStoryView = selectStoryView;
    initialize(); // Add this line to call the initialize method
  }

  /**
   * Initialize method for SelectStoryController.
   */
  public void initialize() {
    storyTitleToFileMap = new HashMap<>();

    // Get a File object representing the directory
    File storiesDirectory = new File("userStories");
    if (storiesDirectory.exists() && storiesDirectory.isDirectory()) {
      File[] storyFiles = storiesDirectory.listFiles();
      if (storyFiles != null) {
        for (File storyFile : storyFiles) {
          if (!storyFile.isDirectory()) {
            String storyTitle = storyFile.getName().replaceFirst("[.][^.]+$", "");
            String storyPath = storyFile.getAbsolutePath();
            storyTitleToFileMap.put(storyTitle, storyPath);
          }
        }
      }
    }
    selectStoryView.setChoiceBoxItems(storyTitleToFileMap.keySet());
  }

  /**
   * Gets story title to file map.
   *
   * @return storyTitleToFileMap. story title to file map
   */
  public Map<String, String> getStoryTitleToFileMap() {
    return this.storyTitleToFileMap;
  }

  /**
   * Saves story file to resources.
   *
   * @param file File to be saved to resources.
   */
  public void saveStoryToFolder(File file) {
    if (storyTitleToFileMap.size() >= 8) {
      // Show an error message or disable the upload button
      return;
    }

    try {
      Path destinationPath = Paths.get("userStories", file.getName());
      if (!checkFileExists(destinationPath.toString())) {
        // File exists and user chose not to replace it, return without saving
        return;
      }
      Files.createDirectories(destinationPath.getParent()); // Ensure the directory exists
      Files.copy(file.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets the selected story.
   *
   * @param storyTitle title of selected story.
   * @return Selected story
   */
  public Story storySelected(String storyTitle) {
    String selectedStoryFilePath = storyTitleToFileMap.get(storyTitle);
    try {
      InputStream inputStream = Files.newInputStream(Paths.get(selectedStoryFilePath));
      StoryFileManager storyFileManager = new StoryFileManager();
      story = storyFileManager.loadStoryFromFile(inputStream);
      GameInstance.getInstance().setStory(story);
    } catch (IOException | StoryValidationException | PassageValidationException |
             LinkValidationException | ActionValidationException | ImageValidationException e) {
      handleException(e);
    }
    if (story != null && gameStarter != null) {
      gameStarter.accept(story);
    }
    return story;
  }

  /**
   * Checks if the file exists.
   *
   * @param filePath Filepath to check
   * @return Whether the file exists.
   */
  private boolean checkFileExists(String filePath) {
    File file = new File(filePath);
    if (file.exists()) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("File Exists");
      alert.setHeaderText("A file with the same name already exists.");
      alert.setContentText("Do you want to replace the existing file?");

      Optional<ButtonType> result = alert.showAndWait();
      // User chose not to replace the file, return false
      if (result.isPresent()) {
        return result.get() == ButtonType.OK;
      }
    }
    // File does not exist or user chose to replace it, return true
    return true;
  }

  /**
   * Remove story from file map.
   *
   * @param storyTitle the story title
   */
  public void removeStory(String storyTitle) {
    String storyPath = storyTitleToFileMap.get(storyTitle);
    File file = new File(storyPath);
    if (file.delete()) {
      storyTitleToFileMap.remove(storyTitle);
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

  /**
   * Sets the game starter.
   *
   * @param gameStarter Consumer<Story>
   */
  public void setGameStarter(Consumer<Story> gameStarter) {
    this.gameStarter = gameStarter;
  }

  /**
   * Gets story.
   *
   * @return story. story
   */
  public Story getStory() {
    return story;
  }
}


