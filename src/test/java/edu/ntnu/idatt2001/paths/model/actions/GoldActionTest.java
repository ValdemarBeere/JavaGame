package edu.ntnu.idatt2001.paths.model.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.ntnu.idatt2001.paths.model.exceptions.ActionValidationException;
import edu.ntnu.idatt2001.paths.model.player.Player;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class GoldActionTest {

  @Test
  public void gold_action_creation_should_succeed_with_valid_value()
      throws ActionValidationException {
    int value = 50;
    GoldAction goldAction = new GoldAction(value);

    assertEquals(value, goldAction.getValue());
  }

  @Test
  public void gold_action_creation_should_throw_exception_for_value_greater_than_100() {
    assertThrows(ActionValidationException.class, () -> new GoldAction(101));
  }

  @Test
  public void gold_action_creation_should_throw_exception_for_value_less_than_minus_100() {
    assertThrows(ActionValidationException.class, () -> new GoldAction(-101));
  }

  @Test
  public void execute_gold_action() {
    Player player = new Player("John", 100, 0, 0);
    GoldAction action = new GoldAction(50);

    action.execute(player);

    assertEquals(50, player.getGold());
  }

  @Test
  public void gold_action_creation_should_succeed_with_minimal_value()
      throws ActionValidationException {
    int value = -100;
    GoldAction goldAction = new GoldAction(value);

    assertEquals(value, goldAction.getValue());
  }

  @Test
  public void gold_action_creation_should_succeed_with_maximal_value()
      throws ActionValidationException {
    int value = 100;
    GoldAction goldAction = new GoldAction(value);

    assertEquals(value, goldAction.getValue());
  }

  @Test
  public void set_new_value_for_gold_action_should_succeed_with_valid_value()
      throws ActionValidationException {
    GoldAction goldAction = new GoldAction(50);
    int newValue = 75;

    goldAction.setValue(String.valueOf(newValue));

    assertEquals(newValue, goldAction.getValue());
  }

}
