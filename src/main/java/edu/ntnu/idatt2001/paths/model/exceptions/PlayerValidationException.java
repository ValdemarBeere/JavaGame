package edu.ntnu.idatt2001.paths.model.exceptions;

/**
 * Exception class for errors related to the validation of players.
 * <p>
 * This class extends {@link IllegalArgumentException} and is used to indicate that a method has
 * been passed an invalid argument in the context of player validation. For instance, it can be
 * thrown when attempting to create a player with an invalid configuration or parameters.
 * <p>
 * As with all Exceptions, the throwing of an PassageValidationException should be reserved for
 * conditions that the application cannot or should not handle.
 */
public class PlayerValidationException extends IllegalArgumentException {

  /**
   * Constructs a new PlayerValidationException with the specified detail message.
   *
   * @param msg the detail message. This message is saved for later retrieval by the
   *            {@link #getMessage()} method.
   */
  public PlayerValidationException(String msg) {
    super(msg);
  }
}
