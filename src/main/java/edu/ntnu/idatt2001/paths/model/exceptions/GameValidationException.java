package edu.ntnu.idatt2001.paths.model.exceptions;

/**
 * Exception class for errors related to the validation of game state or rules.
 * <p>
 * This class extends {@link IllegalArgumentException} and is used to indicate that a method has
 * been passed an invalid argument in the context of game validation. For instance, it can be thrown
 * when an invalid game configuration is detected or a game rule has been violated.
 * <p>
 * As with all Exceptions, the throwing of a GameValidationException should be reserved for
 * conditions that the application cannot or should not handle.
 */
public class GameValidationException extends IllegalArgumentException {

  /**
   * Constructs a new GameValidationException with the specified detail message.
   *
   * @param msg the detail message saved for later retrieval by the {@link #getMessage()} method.
   */
  public GameValidationException(String msg) {
    super(msg);
  }

}
