package edu.ntnu.idatt2001.paths.model.exceptions;

/**
 * Exception class for errors related to the validation of passages.
 * <p>
 * This class extends {@link IllegalArgumentException} and is used to indicate that a method has
 * been passed an invalid argument in the context of passage validation. For instance, it can be
 * thrown when attempting to create a passage with an invalid configuration or parameters.
 * <p>
 * As with all Exceptions, the throwing of an PassageValidationException should be reserved for
 * conditions that the application cannot or should not handle.
 */
public class PassageValidationException extends IllegalArgumentException {

  /**
   * Constructs a new PassageValidationException with the specified detail message.
   *
   * @param msg the detail message saved for later retrieval by the {@link #getMessage()} method.
   */
  public PassageValidationException(String msg) {
    super(msg);
  }
}
