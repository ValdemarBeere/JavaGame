package edu.ntnu.idatt2001.paths.model.validations;

import edu.ntnu.idatt2001.paths.model.Link;
import edu.ntnu.idatt2001.paths.model.Passage;
import edu.ntnu.idatt2001.paths.model.exceptions.PassageValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.StoryValidationException;
import java.util.Map;

public class StoryValidator {

  public static void validateStory(String title, Map<Link, Passage> passages,
      Passage openingPassage) {
    if (title == null) {
      throw new StoryValidationException("Story title can not be null");
    }
    if (title.length() < 3 || title.length() > 20) {
      throw new StoryValidationException("Story title must be between 3 and 20 characters");
    }
    if (title.isBlank() || title.trim().isEmpty()) {
      throw new StoryValidationException("Story title cannot be blank");
    }
    if (passages == null) {
      throw new StoryValidationException("Passages can not be null");
    }
    if (!passages.containsValue(openingPassage)) {
      throw new StoryValidationException("The opening passage is not found in the passages map.");
    }
    if (openingPassage == null) {
      throw new StoryValidationException("Opening passage can not be null");
    }
    if (title.startsWith("|") || title.startsWith("::") || title.startsWith("^")
        || title.startsWith("[") || title.startsWith("{")) {
      throw new PassageValidationException(
          "Content can not start with symbols '|', '::', '^', '[', or '{'");
    }


  }

  public static void validateAddPassage(Passage passage, Map<Link, Passage> passages) {
    for (Link link : passage.getLinks()) {
      if (link.getReference().equals(passage.getTitle())) {
        throw new StoryValidationException(
            "Circular link detected. A passage cannot link to itself.");
      }
    }
    if (passages.containsKey(new Link(passage.getTitle(), passage.getTitle()))) {
      throw new StoryValidationException("Story contains duplicate passage");
    }
  }

  public static void validateGetPassage(Link link, Map<Link, Passage> passages) {
    String reference = link.getReference();
    boolean exists = passages.values().stream()
        .anyMatch(p -> p.getTitle().equals(reference));
    if (!exists) {
      throw new StoryValidationException("No passage exists for the given link: " + reference);
    }
  }


}
