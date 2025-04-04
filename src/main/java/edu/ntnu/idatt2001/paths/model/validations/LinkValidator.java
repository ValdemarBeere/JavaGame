package edu.ntnu.idatt2001.paths.model.validations;

import edu.ntnu.idatt2001.paths.model.actions.Action;
import edu.ntnu.idatt2001.paths.model.exceptions.LinkValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.StoryValidationException;
import java.util.List;

/**
 * The type Link validator.
 */
public class LinkValidator {

  /**
   * Validate link.
   *
   * @param text      the text
   * @param reference the reference
   * @param actions   the actions
   * @throws LinkValidationException the link validation exception
   */
  public static void validateLink(String text, String reference, List<Action<?>> actions)
      throws LinkValidationException {

    if (text == null || text.trim().isEmpty()) {
      throw new LinkValidationException("Text cannot be null or blank");
    }
    if (text.length() < 3 || text.length() > 40) {
      throw new StoryValidationException("Link Text must be between 3 and 40 characters");
    }

    if (reference == null || reference.trim().isEmpty()) {
      throw new LinkValidationException("Reference cannot be null or blank");
    }

    if (actions == null) {
      throw new LinkValidationException("Actions list cannot be null");
    }
    if (actions.size() > 3) {
      throw new LinkValidationException("Sorry, our Game only supports 2 actions\n per link");
    }

    for (Action<?> action : actions) {
      validateAction(action);
    }
  }

  private static void validateAction(Action<?> action) throws LinkValidationException {
    if (action == null || action.getValue() == null) {
      throw new LinkValidationException("Action or its value is null");
    }
  }

  public static void validateAddAction(Action<?> action) {
    if (action == null) {
      throw new LinkValidationException("added action can not be null");
    }
    if (action.getValue().toString().isEmpty() || action.getValue().toString().isBlank()) {
      throw new LinkValidationException("added action can not be empty");
    }
  }

}
