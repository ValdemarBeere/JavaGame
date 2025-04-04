package edu.ntnu.idatt2001.paths.model.goals;

import edu.ntnu.idatt2001.paths.model.player.Player;

public interface Goal<T> {

  boolean isFulfilled(final Player player);

  T getValue();
}





