package edu.ntnu.idatt2001.paths.model.validations;

import edu.ntnu.idatt2001.paths.model.Link;
import edu.ntnu.idatt2001.paths.model.exceptions.PassageValidationException;
import java.util.List;

/**
 * The type Passage validator.
 */
public class PassageValidator {

  /**
   * Validate passage.
   *
   * @param title   the title
   * @param content the content
   * @throws PassageValidationException the passage validation exception
   */
  public static void validatePassage(String title, String content, List<Link> links)
      throws PassageValidationException {
    validateTitleAndContent(title, content);
    for (Link link : links) {
      if (link == null || links.isEmpty()) {
        throw new PassageValidationException("Links cannot be null or empty");
      }
      LinkValidator.validateLink(link.getReference(), link.getText(), link.getActions());
    }
    if (links.isEmpty()) {
      throw new PassageValidationException("Links can not be empty");
    }
    if (links.size() > 4) {
      throw new PassageValidationException("Sorry, our game only supports 4 links\n per passage");
    }

    for (Link link : links) {
      LinkValidator.validateLink(link.getReference(), link.getText(), link.getActions());
    }
  }

  public static void validateAddLink(Link link, String title) {
    if (link == null) {
      throw new PassageValidationException("Link can not be null");
    }
    if (link.getReference().equals(title)) {
      throw new PassageValidationException("Link reference can not be"
          + " the same as it's own passage title");
    }
  }

  public static void validatePassageWithOutLinks(String title, String content)
      throws PassageValidationException {
    validateTitleAndContent(title, content);
  }

  private static void validateTitleAndContent(String title, String content) {
    if (title == null || title.trim().isEmpty()) {
      throw new PassageValidationException("Title cannot be null or blank");
    }
    if (content == null || content.trim().isEmpty()) {
      throw new PassageValidationException("Content cannot be null or blank");
    }
    if (content.startsWith("|") || content.startsWith("::") || content.startsWith("^")
        || content.startsWith("[") || content.startsWith("{")) {
      throw new PassageValidationException(
          "Content can not start with symbols '|', '::', '^', '[', or '{'");
    }
  }
}
