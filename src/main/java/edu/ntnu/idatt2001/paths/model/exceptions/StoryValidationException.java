package edu.ntnu.idatt2001.paths.model.exceptions;

/**
 * Exception class for errors related to the validation of stories.
 * <p>
 * This class extends {@link IllegalArgumentException} and is used to indicate that a method has
 * been passed an invalid argument in the context of Story validation. For instance, it can be
 * thrown when attempting to create a Story with an invalid configuration or parameters.
 * <p>
 * As with all Exceptions, the throwing of an StoryValidationException should be reserved for
 * conditions that the application cannot or should not handle.
 */
public class StoryValidationException extends IllegalArgumentException {

  /**
   * Constructs a new StoryValidationException with the specified detail message.
   *
   * @param msg the detail message. This message is saved for later retrieval by the
   *            {@link #getMessage()} method.
   */
  public StoryValidationException(String msg) {
    super(msg);
  }

}
