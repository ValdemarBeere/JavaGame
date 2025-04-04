package edu.ntnu.idatt2001.paths.model;


import edu.ntnu.idatt2001.paths.model.actions.Action;
import edu.ntnu.idatt2001.paths.model.actions.GoldAction;
import edu.ntnu.idatt2001.paths.model.actions.HealthAction;
import edu.ntnu.idatt2001.paths.model.actions.ScoreAction;
import edu.ntnu.idatt2001.paths.model.actions.inventory.InventoryAction;
import edu.ntnu.idatt2001.paths.model.exceptions.ActionValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.LinkValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.PassageValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.StoryValidationException;
import edu.ntnu.idatt2001.paths.model.validations.ActionValidator;
import edu.ntnu.idatt2001.paths.model.validations.ImageValidator;
import edu.ntnu.idatt2001.paths.model.validations.LinkValidator;
import edu.ntnu.idatt2001.paths.model.validations.StoryValidator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.image.Image;

/**
 * Manages the loading and saving of Story objects from and to files. The story text files follow a
 * specific format for representing the story's structure.
 */
public class StoryFileManager {

  private static final Logger logger = Logger.getLogger(StoryFileManager.class.getName());

  private static final String LINK_REGEX = "\\[(.+)]\\((.+)\\)";
  private static final String ACTION_REGEX = "\\{(\\w+):(.+?)}";

  private static final String REQUIRED_ITEM_REGEX = "\\[(.+?)}";
  private static final String BACKGROUND_IMAGE_REGEX = "\\^(.+)\\^";
  private static final String CONTENT_IMAGE_REGEX = "\\|(.+)\\|";
  private static final Pattern LINK_PATTERN = Pattern.compile(LINK_REGEX);
  private static final Pattern ACTION_PATTERN = Pattern.compile(ACTION_REGEX);
  private static final Pattern REQUIRED_ITEM_PATTERN = Pattern.compile(REQUIRED_ITEM_REGEX);

  private static final Pattern BACKGROUND_IMAGE_PASSAGE_PATTERN = Pattern.compile(
      BACKGROUND_IMAGE_REGEX);
  private static final Pattern CONTENT_IMAGE_PASSAGE_PATTERN = Pattern.compile(CONTENT_IMAGE_REGEX);


  /**
   * Loads a Story object from an input stream.
   *
   * @param inputStream The input stream containing the text representation of a story.
   * @return A new Story object built from the input stream data.
   */
  public Story loadStoryFromFile(InputStream inputStream) {
    HashMap<String, Passage> passageHashMap = new HashMap<>();
    Story story;
    String title;

    try (Scanner scanner = new Scanner(inputStream)) {
      title = readTitle(scanner);
      logger.log(Level.INFO, "Reading title: " + title);
      story = processPassages(scanner, passageHashMap, title);
    }

    return story;
  }

  private String readTitle(Scanner scanner) {
    return scanner.nextLine();
  }

  /**
   * Reads and processes all passages in the story, adding them to the given Story object.
   *
   * @param scanner        The Scanner object to read from.
   * @param passageHashMap A map to hold passages that have been read.
   * @param title          The title of the story.
   * @return The Story object built up with its passages.
   * @throws StoryValidationException If the validation of a story fails.
   */
  private Story processPassages(Scanner scanner, HashMap<String,
      Passage> passageHashMap, String title) throws StoryValidationException {
    Story story = null;
    boolean openingPassageSet = false;
    Passage passage;
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (line.startsWith("::")) {
        passage = readPassage(scanner, line, passageHashMap);
        passage.validatePassage();
        try {
          if (passage.getLinks().isEmpty()) {
            throw new PassageValidationException("Links cannot be empty");
          }
        } catch (PassageValidationException ex) {
          logger.log(Level.SEVERE, "PassageValidationException: ", ex);
          throw ex;
        }
        if (!openingPassageSet) {
          story = new Story(title, passage);
          StoryValidator.validateStory(story.getTitle(), story.getPassagesMap(),
              story.getOpeningPassage());
          openingPassageSet = true;
        } else {
          StoryValidator.validateAddPassage(passage, story.getPassagesMap());
          story.addPassage(passage);
        }
      }
    }
    assert story != null;
    for (Passage passageInStory : story.getPassagesMap().values()) {
      for (Link linkLinkInPassage : passageInStory.getLinks()) {
        StoryValidator.validateGetPassage(linkLinkInPassage, story.getPassagesMap());
      }
    }
    return story;
  }

  /**
   * Reads and processes a passage from the story, and adds it to the given HashMap.
   *
   * @param scanner        The Scanner object to read from.
   * @param line           The current line to be processed.
   * @param passageHashMap A map to hold passages that have been read.
   * @return The Passage object created.
   * @throws StoryValidationException If the validation of a story fails.
   */
  private Passage readPassage(Scanner scanner, String line, HashMap<String, Passage> passageHashMap)
      throws StoryValidationException {
    String passageTitle = line.substring(2).trim();
    String content = scanner.nextLine();

    Image backgroundImage = null;
    Image contentImage = null;

    line = scanner.nextLine();
    while ((line.matches(CONTENT_IMAGE_REGEX) || line.matches(BACKGROUND_IMAGE_REGEX))
        && !line.isBlank() && scanner.hasNextLine()) {
      Matcher backgroundImageMatcher = BACKGROUND_IMAGE_PASSAGE_PATTERN.matcher(line);
      Matcher contentImageMatcher = CONTENT_IMAGE_PASSAGE_PATTERN.matcher(line);
      if (contentImageMatcher.find()) {
        contentImage = getImage(contentImageMatcher);
      } else if (backgroundImageMatcher.find()) {
        backgroundImage = getImage(backgroundImageMatcher);
      }

      line = scanner.nextLine();
    }

    // Create the Passage object once, with all the images that were found
    Passage passage = new Passage(passageTitle, content, backgroundImage, contentImage);
    passageHashMap.put(passageTitle, passage);

    // Process links and actions after the passage has been created
    while (!line.isBlank()) {
      if (LINK_PATTERN.matcher(line).find()) {
        processLinks(line, passage);
      } else if (ACTION_PATTERN.matcher(line).find()) {
        processActionsForLastLink(line, passage);
      } else if (REQUIRED_ITEM_PATTERN.matcher(line).find()) {
        processRequiredItemForLink(line, passage);
      }
      if (!scanner.hasNextLine()) {
        break; //end of the document
      } else if (scanner.hasNextLine()) {
        line = scanner.nextLine();
      }

    }

    return passage;
  }

  private Image getImage(Matcher backgroundImageMatcher) {
    Image backgroundImage;
    String imagePath = backgroundImageMatcher.group(1).trim();
    if (imagePath.startsWith("uploadedImages/")) {
      File imageFile = new File("./" + imagePath);
      ImageValidator.validateImage(imageFile);
      backgroundImage = new Image(imageFile.toURI().toString());
    } else {
      ImageValidator.validateImage(imagePath);
      backgroundImage = new Image(
          Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }
    return backgroundImage;
  }

  private void processRequiredItemForLink(String line, Passage passage) {
    // Get the last link in the passage
    Link lastLink = passage.getLinks().get(passage.getLinks().size() - 1);

    // Create a matcher for the required item pattern
    Matcher requiredItemMatcher = REQUIRED_ITEM_PATTERN.matcher(line);

    // Check if the line contains a required item
    if (requiredItemMatcher.matches()) {
      // Get the name of the required item from the matcher
      String item = requiredItemMatcher.group(1);

      // Create a new InventoryAction for the required item
      InventoryAction requiredItemAction = new InventoryAction(item);
      ActionValidator.validateAction("InventoryAction", item);

      // Assign the required item action to the link
      lastLink.setRequiredItem(requiredItemAction);
    }
  }


  private void processLinks(String line, Passage passage)
      throws LinkValidationException {
    Matcher matcher = LINK_PATTERN.matcher(line);
    while (matcher.find()) {
      String linkText = matcher.group(1);
      String linkReference = matcher.group(2).trim();
      Link passageLink = new Link(linkText, linkReference);
      passage.addLink(passageLink);
      LinkValidator.validateLink(passageLink.getText(), passageLink.getReference(),
          passageLink.getActions());
    }

  }


  private void processActionsForLastLink(String line, Passage passage)
      throws ActionValidationException, LinkValidationException {

    Link lastLink = passage.getLinks().get(passage.getLinks().size() - 1);
    Matcher actionMatcher = ACTION_PATTERN.matcher(line);

    while (actionMatcher.find()) {
      String actionType = actionMatcher.group(1);
      String actionValue = actionMatcher.group(2);

      ActionValidator.validateAction(actionType, actionValue);

      Action<?> action = switch (actionType) {
        case "HealthAction" -> new HealthAction(Integer.parseInt(actionValue));
        case "GoldAction" -> new GoldAction(Integer.parseInt(actionValue));
        case "InventoryAction" -> new InventoryAction(actionValue);
        case "ScoreAction" -> new ScoreAction(Integer.parseInt(actionValue));
        default -> throw new ActionValidationException("Cannot recognize action type");
      };
      lastLink.addAction(action);
      ActionValidator.validateAction(action);
    }
  }

  /**
   * Saves a Story object to a file.
   *
   * @param story         The Story object to save.
   * @param directoryPath The directory path where to save the file.
   * @throws IOException If an IO error occurs while saving the story.
   */
  public void saveStoryToFile(Story story, String directoryPath) throws IOException {
    if (story == null || directoryPath == null) {
      throw new IllegalArgumentException("Invalid story or directory path.");
    }
    // Create the directory if it doesn't exist
    Path directory = Paths.get(directoryPath);
    if (!Files.exists(directory)) {
      Files.createDirectories(directory);
    }

    String fileName = story.getTitle().replaceAll("[^a-zA-Z0-9-_.]", "_") + ".paths";
    Path filePath = directory.resolve(fileName);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
      writer.write(story.getTitle());
      writer.newLine();
      writer.newLine();

      for (Passage passage : story.getPassages()) {
        writer.write("::" + passage.getTitle());
        writer.newLine();
        writer.write(passage.getContent());
        if (passage.getBackgroundImage() != null) {
          writer.newLine();
          try {
            // Create a URI from the URL string
            URI uri = new URI(passage.getBackgroundImage().getUrl());

            // Create a Path from the URI
            Path path = Paths.get(uri);

            // Check if the image is from uploadedImages or defaultImages
            String subFolder;
            if (Files.exists(
                Paths.get("./uploadedImages/", path.getFileName().toString()))) {
              subFolder = "uploadedImages/";
            } else {
              subFolder = "/images/passageImages/passageBackGroundImages/";
            }

            writer.write("^" + subFolder + path.getFileName() + "^");
            writer.newLine();
          } catch (URISyntaxException | IOException e) {
            // Handle the exception as appropriate for your application
            e.printStackTrace();
          }
        } else {
          writer.newLine();
        }
        if (passage.getContentImage() != null) {
          try {
            // Create a URI from the URL string
            URI uri = new URI(passage.getContentImage().getUrl());

            // Create a Path from the URI
            Path path = Paths.get(uri);

            // Check if the image is from uploadedImages or defaultImages
            String subFolder;
            if (Files.exists(
                Paths.get("./uploadedImages/", path.getFileName().toString()))) {
              subFolder = "uploadedImages/";
            } else {
              subFolder = "/images/passageImages/passageContentImages/";
            }

            writer.write("|" + subFolder + path.getFileName() + "|");
            writer.newLine();
          } catch (URISyntaxException | IOException e) {
            // Handle the exception as appropriate for your application
            e.printStackTrace();
          }
        }

        for (Link link : passage.getLinks()) {
          writer.write("[" + link.getText() + "](" + link.getReference() + ")");
          writer.newLine();
          if (link.getActions() != null) {
            for (Action<?> action : link.getActions()) {
              writer.write(
                  "{" + action.getClass().getSimpleName() + ":" + action.getValue()
                      + "}");
              writer.newLine();
            }
          }
          if (link.getRequiredItem() != null) {
            writer.write("[" + link.getRequiredItem().getValue() + "}");
            writer.newLine();
          }
        }

        writer.newLine();
      }
    }
  }
}
