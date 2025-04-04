package edu.ntnu.idatt2001.paths.model.goals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2001.paths.model.actions.inventory.InventoryItemEnum;
import edu.ntnu.idatt2001.paths.model.exceptions.GoalValidationException;
import edu.ntnu.idatt2001.paths.model.player.Player;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class InventoryGoalTest {

  @Test
  public void inventory_goal_creation_should_succeed_with_valid_value() {
    String value = InventoryItemEnum.SWORD.getName();
    InventoryGoal inventoryGoal = new InventoryGoal(value);

    assertEquals(value, inventoryGoal.getValue());
  }

  @Test
  public void inventory_goal_creation_should_throw_exception_for_invalid_value() {
    String invalidValue = "InvalidItem";
    assertThrows(GoalValidationException.class, () -> new InventoryGoal(invalidValue));
  }

  @Test
  public void is_fulfilled_inventory_goal_should_return_true_when_player_has_required_item() {
    Player player = new Player("John", 1, 0, 0);
    player.addToInventory(InventoryItemEnum.SWORD.getName());
    InventoryGoal goal = new InventoryGoal(InventoryItemEnum.SWORD.getName());
    assertTrue(goal.isFulfilled(player));
  }

  @Test
  public void is_fulfilled_inventory_goal_should_return_false_when_player_does_not_have_required_item() {
    Player player = new Player("John", 1, 0, 0);
    InventoryGoal goal = new InventoryGoal(InventoryItemEnum.SWORD.getName());

    assertFalse(goal.isFulfilled(player));
  }
}
