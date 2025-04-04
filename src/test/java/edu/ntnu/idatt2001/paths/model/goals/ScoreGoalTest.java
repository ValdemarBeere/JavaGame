package edu.ntnu.idatt2001.paths.model.goals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2001.paths.model.exceptions.GoalValidationException;
import edu.ntnu.idatt2001.paths.model.player.Player;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ScoreGoalTest {

  @Test
  public void score_goal_creation_should_succeed_with_valid_value() {
    int value = 100;
    ScoreGoal scoreGoal = new ScoreGoal(value);

    assertEquals(value, scoreGoal.getValue());
  }

  @Test
  public void score_goal_creation_should_throw_exception_for_negative_value() {
    assertThrows(GoalValidationException.class, () -> new ScoreGoal(-1));
  }

  @Test
  public void is_fulfilled_score_goal_should_return_true_when_player_score_is_greater_than_or_equal_to_goal() {
    Player player = new Player("John", 1, 100, 0);
    ScoreGoal goal = new ScoreGoal(100);

    assertTrue(goal.isFulfilled(player));
  }

  @Test
  public void is_fulfilled_score_goal_should_return_false_when_player_score_is_less_than_goal() {
    Player player = new Player("John", 1, 50, 0);
    ScoreGoal goal = new ScoreGoal(100);

    assertFalse(goal.isFulfilled(player));
  }
}
