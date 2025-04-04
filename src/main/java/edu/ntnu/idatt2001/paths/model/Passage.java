package edu.ntnu.idatt2001.paths.model;

import edu.ntnu.idatt2001.paths.model.exceptions.PassageValidationException;
import edu.ntnu.idatt2001.paths.model.validations.PassageValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 * The type Passage. Describes a scene in the Story
 */
public class Passage {


  private static final Logger logger = Logger.getLogger(Passage.class.getName());
  private final Image backgroundImage;
  private final List<Link> links;
  /**
   * A map to track the status of actions. The map contains links as keys and a boolean value
   * indicating whether the action associated with the link has been performed or not.
   */
  private final Map<Link, Boolean> actionStatusMap;
  private String title;
  private String content;
  private Image contentImage;


  /**
   * Main constructor of Passage class Creates a passage with @see param
   *
   * @param title           the title
   * @param content         the content
   * @param backgroundImage the background image
   * @param contentImage    the content image
   * @param links           the links
   */
  public Passage(final String title, final String content, Image backgroundImage,
      Image contentImage, final List<Link> links) {
    this.title = title;
    this.content = content;
    this.backgroundImage = backgroundImage;
    this.contentImage = contentImage;
    this.links = new ArrayList<>(links);
    this.actionStatusMap = new HashMap<>();
    logger.info("Passage created with background and content images and links");
  }

  /**
   * Instantiates a new Passage.
   *
   * @param title   the title
   * @param content the content
   */
  public Passage(String title, String content) {
    this(title, content, null, null, new ArrayList<>());
    logger.info("Passage created with just title and content");
  }

  /**
   * Instantiates a new Passage. with @see param
   *
   * @param title           the title
   * @param content         the content
   * @param backgroundImage the background image
   * @param contentImage    the content image
   */
  public Passage(String title, String content, Image backgroundImage, Image contentImage) {
    this(title, content, backgroundImage, contentImage, new ArrayList<>());
    logger.info("Passage created with images but no links");
  }

  /**
   * Instantiates a new Passage.
   *
   * @param title           the title
   * @param content         the content
   * @param backgroundImage the background image
   */
  public Passage(String title, String content, Image backgroundImage) {
    this(title, content, backgroundImage, null, new ArrayList<>());
    logger.info("Passage created with background image only");
  }

  /**
   * Validate passage {@code @USE} to validate passage in loadStory from file.
   */
  public void validatePassage() {
    PassageValidator.validatePassage(this.title, this.content, this.links);
  }

  public void validatePassageWithOutLinks() {
    PassageValidator.validatePassageWithOutLinks(this.title, this.content);
  }

  /**
   * Gets title.
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Set title.
   *
   * @param newValue the new value
   */
  public void setTitle(String newValue) {
    this.title = newValue;
  }

  /**
   * Gets content.
   *
   * @return the content
   */
  public String getContent() {
    return content;
  }

  /**
   * Sets content.
   *
   * @param newValue the new value
   */
  public void setContent(String newValue) {
    this.content = newValue;
  }

  /**
   * Gets background image.
   *
   * @return the background image
   */
  public Image getBackgroundImage() {
    return backgroundImage;
  }

  /**
   * Gets contente image.
   *
   * @return the content image
   */
  public Image getContentImage() {
    return contentImage;
  }

  /**
   * Sets contente image.
   *
   * @param contentImage the content image
   */
  public void setContentImage(Image contentImage) {
    this.contentImage = contentImage;
  }

  /**
   * Adds a link to another passage to this passage.
   *
   * @param link the link to add
   * @return true if the link was successfully added, false otherwise
   * @throws PassageValidationException if link reference is the same as passage title
   */
  public boolean addLink(Link link) {
    PassageValidator.validateAddLink(link, title);
    return links.add(link);
  }

  /**
   * Remove link.
   *
   * @param link the link
   */
  public void removeLink(Link link) {
    this.links.remove(link);
  }

  /**
   * Gets a list of all the links from this passage to other passages.
   *
   * @return a list of all the links from this passage to other passages
   */
  public List<Link> getLinks() {
    return links;
  }

  /**
   * Gets true if this passage has any links to other passages, false otherwise.
   *
   * @return true if this passage has any links to other passages, false otherwise
   */
  public boolean hasLinks() {
    return !links.isEmpty();
  }

  /**
   * Gets the last link of a passage object.
   *
   * @return the last link
   */
  public Link getLastLink() {
    if (links.isEmpty()) {
      return null;
    }
    return links.get(links.size() - 1);
  }

  /**
   * Checks if the action associated with the provided link has been performed.
   *
   * @param link Link - the link associated with the action
   * @return boolean - true if the action has been performed, false otherwise
   */
  public boolean hasPerformedAction(Link link) {
    return actionStatusMap.getOrDefault(link, false);
  }

  /**
   * Marks the action associated with the provided link as performed.
   *
   * @param link Link - the link associated with the action
   * @return boolean
   */
  public boolean markActionAsPerformed(Link link) {
    actionStatusMap.put(link, true);
    return false;
  }

  /**
   * Gets a textual representation of the passage, consisting of its title and content.
   *
   * @return a textual representation of the passage, consisting of its title and content
   */
  @Override
  public String toString() {
    return "Passage{" +
        "title='" + title + '\'' +
        ", content='" + content + '\'' +
        '}';
  }

  /**
   * Compares this passage to another object for equality.
   *
   * @param o the object to compare to
   * @return true if the object is equal to this passage, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      logger.info("Passage is compared to itself");
      return true;
    }
    if (!(o instanceof Passage passage)) {
      logger.info("Object compared to passage is not of type Passage");
      return false;
    }
    boolean result =
        getTitle().equals(passage.getTitle()) && getContent().equals(passage.getContent());
    logger.info(result ? "Passages are equal" : "Passages are not equal");
    return result;
  }

  /**
   * Returns a hash code for this passage, based on its title and content.
   *
   * @return a hash code for this passage, based on its title and content
   */
  @Override
  public int hashCode() {
    logger.info("Hashcode method called");
    return Objects.hash(getTitle(), getContent());
  }
}
