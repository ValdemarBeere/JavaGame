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
public class HealthGoalTest {

  @Test
  public void health_goal_creation_should_succeed_with_valid_value() {
    int value = 100;
    HealthGoal healthGoal = new HealthGoal(value);

    assertEquals(value, healthGoal.getValue());
  }

  @Test
  public void health_goal_creation_should_throw_exception_for_negative_value() {
    assertThrows(GoalValidationException.class, () -> new HealthGoal(-1));
  }

  @Test
  public void is_fulfilled_health_goal_should_return_true_when_player_health_is_greater_than_or_equal_to_goal() {
    Player player = new Player("John", 100, 0, 100);
    HealthGoal goal = new HealthGoal(100);

    assertTrue(goal.isFulfilled(player));
  }

  @Test
  public void is_fulfilled_health_goal_should_return_false_when_player_health_is_less_than_goal() {
    Player player = new Player("John", 50, 0, 50);
    HealthGoal goal = new HealthGoal(100);

    assertFalse(goal.isFulfilled(player));
  }
}
