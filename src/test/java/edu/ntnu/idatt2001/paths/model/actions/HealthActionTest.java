package edu.ntnu.idatt2001.paths.model.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.ntnu.idatt2001.paths.model.exceptions.ActionValidationException;
import edu.ntnu.idatt2001.paths.model.player.Player;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class HealthActionTest {

  @Test
  public void health_action_creation_should_succeed_with_valid_value()
      throws ActionValidationException {
    int value = 50;
    HealthAction healthAction = new HealthAction(value);

    assertEquals(value, healthAction.getValue());
  }

  @Test
  public void health_action_creation_should_throw_exception_for_value_greater_than_100() {
    assertThrows(ActionValidationException.class, () -> new HealthAction(101));
  }

  @Test
  public void health_action_creation_should_throw_exception_for_value_less_than_minus_100() {
    assertThrows(ActionValidationException.class, () -> new HealthAction(-101));
  }

  @Test
  public void execute_health_action() {
    Player player = new Player("John", 100, 0, 0);
    HealthAction action = new HealthAction(50);

    action.execute(player);

    assertEquals(150, player.getHealth());
  }

  @Test
  public void health_action_creation_should_succeed_with_minimal_value()
      throws ActionValidationException {
    int value = -100;
    HealthAction healthAction = new HealthAction(value);

    assertEquals(value, healthAction.getValue());
  }

  @Test
  public void health_action_creation_should_succeed_with_maximal_value()
      throws ActionValidationException {
    int value = 100;
    HealthAction healthAction = new HealthAction(value);

    assertEquals(value, healthAction.getValue());
  }

  @Test
  public void set_new_value_for_health_action_should_succeed_with_valid_value()
      throws ActionValidationException {
    HealthAction healthAction = new HealthAction(50);
    int newValue = 75;

    healthAction.setValue(String.valueOf(newValue));

    assertEquals(newValue, healthAction.getValue());
  }

}
