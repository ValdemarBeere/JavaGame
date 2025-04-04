package edu.ntnu.idatt2001.paths.model.exceptions;


/**
 * Exception class for errors related to the validation of goals.
 * <p>
 * This class extends {@link IllegalArgumentException} and is used to indicate that a method has
 * been passed an invalid argument in the context of goal validation. For instance, it can be thrown
 * when attempting to create a goal with an invalid configuration or parameters.
 * <p>
 * As with all Exceptions, the throwing of a GoalValidationException should be reserved for
 * conditions that the application cannot or should not handle.
 */
public class GoalValidationException extends IllegalArgumentException {

  /**
   * Constructs a new GoalValidationException with the specified detail message.
   *
   * @param msg the detail message, saved for later retrieval by the {@link #getMessage()} method.
   */
  public GoalValidationException(String msg) {
    super(msg);
  }
}
