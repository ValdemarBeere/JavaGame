package edu.ntnu.idatt2001.paths.model.exceptions;

/**
 * This class represents an exception that is thrown when a validation error occurs in paths. It
 * extends the IllegalArgumentException, therefore it is a form of RuntimeException.
 */
public class PathsValidationException extends IllegalArgumentException {

  /**
   * Constructs a new PathsValidationException with the specified detail message.
   *
   * @param msg the detail message. This message is saved for later retrieval by the
   *            {@link #getMessage()} method.
   */
  public PathsValidationException(String msg) {
    super(msg);
  }
}
