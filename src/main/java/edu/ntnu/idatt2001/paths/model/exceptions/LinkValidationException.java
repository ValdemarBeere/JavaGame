package edu.ntnu.idatt2001.paths.model.exceptions;

/**
 * Exception class for errors related to the validation of links.
 * <p>
 * This class extends {@link IllegalArgumentException} and is used to indicate that a method has
 * been passed an invalid argument in the context of link validation. For instance, it can be thrown
 * when attempting to create a link with an invalid configuration or parameters.
 * <p>
 * As with all Exceptions, the throwing of an ActionValidationException should be reserved for
 * conditions that the application cannot or should not handle.
 */
public class LinkValidationException extends IllegalArgumentException {

  /**
   * Constructs a new LinkValidationException with the specified detail message.
   *
   * @param msg the detail message saved for later retrieval by the {@link #getMessage()} method.
   */
  public LinkValidationException(String msg) {
    super(msg);
  }
}
