package edu.ntnu.idatt2001.paths.model.game;

import edu.ntnu.idatt2001.paths.model.Link;
import edu.ntnu.idatt2001.paths.model.Passage;
import edu.ntnu.idatt2001.paths.model.Story;
import edu.ntnu.idatt2001.paths.model.actions.Action;
import edu.ntnu.idatt2001.paths.model.actions.inventory.InventoryAction;
import edu.ntnu.idatt2001.paths.model.goals.Goal;
import edu.ntnu.idatt2001.paths.model.player.Player;
import edu.ntnu.idatt2001.paths.model.validations.GameValidator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Game is a facade for a Paths game. The class connects a player to a story, and has handy methods
 * of starting and maneuvering in the game.
 */
public class Game {

  private static final Logger LOGGER = Logger.getLogger(Game.class.getName());
  private final Player player;
  private final Story story;
  private final List<Goal<?>> goals;

  /**
   * Constructs a new Game with the specified player, story, and goals.
   *
   * @param player the player participating in the game
   * @param story  the story of the game
   * @param goals  the goals to be achieved in the game
   */
  public Game(Player player, Story story, List<Goal<?>> goals) {
    GameValidator.validateGame(player, story, goals);
    this.player = player;
    this.story = story;
    this.goals = goals;
  }

  /**
   * Returns the player.
   *
   * @return the player
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Returns the story.
   *
   * @return the story
   */
  public Story getStory() {
    return story;
  }

  /**
   * Returns the list of goals.
   *
   * @return the goals
   */
  public List<Goal<?>> getGoals() {
    return goals;
  }

  /**
   * Checks if all goals have been reached.
   *
   * @return true if all goals have been reached, false otherwise
   */
  public boolean areAllGoalsReached() {
    for (Goal<?> goal : goals) {
      if (!goal.isFulfilled(player)) {
        return false;
      }
    }
    LOGGER.info("All goals reached in the game.");
    return true;
  }

  /**
   * Executes all actions associated with a given link.
   *
   * @param link the link whose actions are to be executed
   */
  public void executeAction(Link link) {
    for (Action<?> action : link.getActions()) {
      action.execute(player);
    }
    LOGGER.info("Actions executed from link: " + link);
  }

  /**
   * Checks if the player can follow a given link.
   *
   * @param link the link to check
   * @return true if the player can follow the link, false otherwise
   */
  public boolean canFollowLink(Link link) {
    InventoryAction requiredItem = link.getRequiredItem();
    boolean canFollow = requiredItem == null || player.isInInventory(requiredItem.getValue());
    if (!canFollow) {
      LOGGER.warning("Cannot follow link: " + link);
    }
    return canFollow;
  }

  /**
   * Progresses the game via a given link.
   *
   * @param link the link to follow to progress the game
   * @return true if all goals have been reached after progressing, false otherwise
   */
  public boolean progressGame(Link link) {
    if (!canFollowLink(link)) {
      LOGGER.severe(
          "Cannot progress the game: you need to obtain " + link.getRequiredItem().getValue());
      throw new RuntimeException(
          "Cannot progress the game: you need to obtain " + link.getRequiredItem().getValue());
    }
    executeAction(link);
    if (!player.isAlive()) {
      LOGGER.severe("You died, your health dropped to 0");
    }
    go(link);
    boolean allGoalsReached = areAllGoalsReached();
    LOGGER.info("Game progress. All goals reached: " + allGoalsReached);
    return allGoalsReached;
  }

  /**
   * Begins the game.
   *
   * @return the opening passage of the story
   */
  public Passage begin() {
    LOGGER.info("Game begins.");
    return story.getOpeningPassage();
  }

  /**
   * Moves to the passage associated with a given link.
   *
   * @param link the link to follow to the next passage
   * @return the next passage in the story
   */
  public Passage go(Link link) {
    if (link == null) {
      LOGGER.severe("Link is null.");
      throw new IllegalArgumentException("Link cannot be null");
    }
    LOGGER.info("Moving to the next passage via link: " + link);
    return story.getPassage(link);
  }

}
