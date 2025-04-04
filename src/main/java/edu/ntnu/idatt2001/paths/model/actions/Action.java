package edu.ntnu.idatt2001.paths.model.actions;


import edu.ntnu.idatt2001.paths.model.player.Player;

/**
 * The interface Action.
 *
 * @param <T> the type parameter
 */
public interface Action<T> {

  /**
   * Execute.
   *
   * @param player the player
   */
  void execute(final Player player);

  /**
   * Get method for the action's value.
   *
   * @return T - the value of the action
   */
  T getValue();

  /**
   * Sets value.
   *
   * @param newValue the new value
   */
  void setValue(String newValue);

  /**
   * Get a human-readable description of the action.
   *
   * @return A string describing the action
   */
  String getDescription();

  /**
   * Gets type.
   *
   * @return the type
   */
  ActionType getType();
}
