package edu.ntnu.idatt2001.paths.model.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.ntnu.idatt2001.paths.model.exceptions.ActionValidationException;
import edu.ntnu.idatt2001.paths.model.player.Player;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ScoreActionTest {

  @Test
  public void score_action_creation_should_succeed_with_valid_value()
      throws ActionValidationException {
    int value = 50;
    ScoreAction scoreAction = new ScoreAction(value);

    assertEquals(value, scoreAction.getValue());
  }

  @Test
  public void score_action_creation_should_throw_exception_for_value_greater_than_100() {
    assertThrows(ActionValidationException.class, () -> new ScoreAction(101));
  }

  @Test
  public void score_action_creation_should_throw_exception_for_value_less_than_minus_100() {
    assertThrows(ActionValidationException.class, () -> new ScoreAction(-101));
  }

  @Test
  public void execute_score_action() {
    Player player = new Player("John", 100, 0, 0);
    ScoreAction action = new ScoreAction(50);

    action.execute(player);

    assertEquals(50, player.getScore());
  }

  @Test
  public void score_action_creation_should_succeed_with_minimal_value()
      throws ActionValidationException {
    int value = -100;
    ScoreAction scoreAction = new ScoreAction(value);

    assertEquals(value, scoreAction.getValue());
  }

  @Test
  public void score_action_creation_should_succeed_with_maximal_value()
      throws ActionValidationException {
    int value = 100;
    ScoreAction scoreAction = new ScoreAction(value);

    assertEquals(value, scoreAction.getValue());
  }

  @Test
  public void set_new_value_for_score_action_should_succeed_with_valid_value()
      throws ActionValidationException {
    ScoreAction scoreAction = new ScoreAction(50);
    int newValue = 75;

    scoreAction.setValue(String.valueOf(newValue));

    assertEquals(newValue, scoreAction.getValue());
  }

}
