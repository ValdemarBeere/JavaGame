package edu.ntnu.idatt2001.paths.model;

import edu.ntnu.idatt2001.paths.model.validations.StoryValidator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * A story is an interactive, non-linear narrative consisting of a collection of passages.
 */
public class Story {

  private static final Logger logger = Logger.getLogger(Story.class.getName());

  /**
   * The title of the story.
   */
  private final String title;

  /**
   * A Map containing the story's passages. The key to each passage is a link.
   */
  private final Map<Link, Passage> passages;

  /**
   * The first passage in the story. The object must also be added in passages.
   */
  private Passage openingPassage;

  /**
   * Main Constructs a new Story object with the given title and opening passage.
   *
   * @param title          the title of the story
   * @param passages       the passages
   * @param openingPassage the first passage in the story
   */
  public Story(final String title, final Map<Link, Passage> passages,
      final Passage openingPassage) {
    StoryValidator.validateStory(title, passages, openingPassage);
    this.title = title;
    this.openingPassage = openingPassage;
    this.passages = passages != null ? new HashMap<>(passages) : new HashMap<>();

    // Check if the opening passage is already in the passages map (use: editStory controller)
    boolean isPresent = this.passages.values().stream()
        .anyMatch(passage -> passage.equals(openingPassage));
    if (!isPresent) {
      // Add the opening passage to the passages map only if it's not already present
      addPassage(openingPassage);
    }
    logger.info("New story created with opening passage and additional passages");
  }

  /**
   * Constructs a new Story object with the given title and opening passage.
   *
   * @param title          the title of the story
   * @param openingPassage the first passage in the story
   */
  public Story(String title, Passage openingPassage) {
    this.title = title;
    this.openingPassage = openingPassage;
    this.passages = new HashMap<>();
    // Create a Link object using the opening passage title

    // Add the opening passage to the passages map
    addPassage(openingPassage);
    logger.info("New story created with opening passage");
  }


  /**
   * Instantiates a new Story. with title and empty HashMap
   *
   * @param title the title
   */
  public Story(String title) {
    this.title = title;
    this.passages = new HashMap<>();
    logger.info("New story created with no opening passage");
  }

  /**
   * Returns the title of the story.
   *
   * @return the title of the story
   */
  public String getTitle() {
    return title;
  }

  /**
   * Returns the opening passage of the story.
   *
   * @return the opening passage of the story
   */
  public Passage getOpeningPassage() {
    return openingPassage;
  }

  /**
   * Adds a passage to the story Validates that there are no duplicate passages
   *
   * @param passage The passage to add
   */
  public void addPassage(Passage passage) {
    StoryValidator.validateAddPassage(passage, passages);
    Link link = new Link(passage.getTitle(), passage.getTitle());
    passages.put(link, passage);
    logger.info("Passage " + passage.getTitle() + " added to the story");
  }


  /**
   * Returns a collection of all the passages in the story.
   *
   * @return a collection of all the passages in the story
   */
  public Collection<Passage> getPassages() {
    return passages.values();
  }

  /**
   * Returns the passage with the given link.
   *
   * @param link the link to the desired passage
   * @return the passage with the given link, or null if no such passage exists
   */
  public Passage getPassage(Link link) {
    StoryValidator.validateGetPassage(link, passages);
    Link key = passages.keySet().stream()
        .filter(k -> k.getReference().equals(link.getReference()))
        .findFirst()
        .orElse(null);
    return passages.get(key);
  }

  /**
   * Gets first passage of passage collection.
   *
   * @return the first passage
   */
  public Passage getFirstPassage() {
    List<Passage> passages = new ArrayList<>(this.getPassages());
    return !passages.isEmpty() ? passages.get(0) : null;
  }

  /**
   * Removes a passage from the story and any links pointing to this passage. If opening passage is
   * moved it will set the first passage to opening passage {@code @USE} EditStoryController
   *
   * @param passage the passage to remove
   * @return true if removed false else
   */
  public boolean removePassageUsingPassage(Passage passage) {
    if (passage == null) {
      return false;
    }

    // Get the corresponding link
    Link linkToRemove = passages.entrySet().stream()
        .filter(entry -> entry.getValue().equals(passage))
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(null);

    if (linkToRemove == null) {
      return false;
    }

    // Remove all links that reference the passage to be removed
    for (Passage p : passages.values()) {
      p.getLinks().removeIf(link -> link.getReference().equals(passage.getTitle()));
    }
    // If the passage to remove is the opening passage, also set openingPassage to null
    if (openingPassage.equals(passage)) {
      openingPassage = null;
    }

    // Remove the passage itself
    passages.remove(linkToRemove);

    return true;
  }


  /**
   * Gets broken links.
   *
   * @return the broken links
   */
  public List<Link> getBrokenLinks() {
    return passages.values().stream()
        .flatMap(passage -> passage.getLinks().stream())
        .filter(link -> passages.values().stream()
            .noneMatch(p -> p.getTitle().equals(link.getReference())))
        .collect(Collectors.toList());
  }

  /**
   * Gets passages map.
   *
   * @return the passages map
   */
  public Map<Link, Passage> getPassagesMap() {
    return this.passages;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      logger.info("Story is compared to itself");
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      logger.info("Object compared to story is not of type Story");
      return false;
    }
    Story other = (Story) obj;
    boolean result =
        Objects.equals(this.title, other.title) && Objects.equals(this.passages, other.passages);
    logger.info(result ? "Stories are equal" : "Stories are not equal");
    return result;
  }

  @Override
  public int hashCode() {
    logger.info("Hashcode method called");
    return Objects.hash(title, passages);
  }

}
