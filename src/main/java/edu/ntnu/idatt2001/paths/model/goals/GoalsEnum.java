package edu.ntnu.idatt2001.paths.model.goals;

/**
 * The enum Goals enum. This is just default values for Gold these wll be used in this version of
 * the application a further improvement of the application will have more options
 */
public enum GoalsEnum {
  /**
   * The Gold.
   */
  GOLD(new GoldGoal(100)),
  /**
   * The Score.
   */
  SCORE(new ScoreGoal(100)),
  /**
   * The Health.
   */
  HEALTH(new HealthGoal(1));

  private final Goal<Integer> goal;

  GoalsEnum(Goal<Integer> goal) {
    this.goal = goal;
  }

  /**
   * Gets goal.
   *
   * @return the goal
   */
  public Goal<Integer> getGoal() {
    return this.goal;
  }
}
