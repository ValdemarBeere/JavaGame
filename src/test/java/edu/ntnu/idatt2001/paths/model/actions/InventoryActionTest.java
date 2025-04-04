package edu.ntnu.idatt2001.paths.model.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2001.paths.model.actions.inventory.InventoryAction;
import edu.ntnu.idatt2001.paths.model.actions.inventory.InventoryItemEnum;
import edu.ntnu.idatt2001.paths.model.exceptions.ActionValidationException;
import edu.ntnu.idatt2001.paths.model.player.Player;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class InventoryActionTest {


  @Test
  public void inventory_action_creation_should_throw_exception_for_null_item() {
    assertThrows(ActionValidationException.class, () -> new InventoryAction(null));
  }

  @Test
  public void inventory_action_creation_should_throw_exception_for_empty_item() {
    assertThrows(ActionValidationException.class, () -> new InventoryAction(""));
  }

  @Test
  public void inventory_action_creation_should_throw_exception_for_non_alphabetic_item() {
    assertThrows(ActionValidationException.class, () -> new InventoryAction("123"));
  }

  @Test
  public void inventory_action_creation_should_throw_exception_non_InventoryItemEnum() {
    assertThrows(ActionValidationException.class, () -> new InventoryAction("Cat"));
  }

  @Test
  void inventory_action_creation_should_succeed_with_valid_item() {
    String item = InventoryItemEnum.AMULET.name().toLowerCase();
    InventoryAction inventoryAction = new InventoryAction(item);

    assertEquals(item, inventoryAction.getValue());
    assertEquals("You obtained " + item + " item", inventoryAction.getDescription());
  }

  @Test
  void execute_inventory_action() {
    Player player = new Player("John", 100, 0, 0);
    InventoryAction action = new InventoryAction(InventoryItemEnum.GEM.name().toLowerCase());

    action.execute(player);

    assertTrue(player.getInventory().contains(InventoryItemEnum.GEM.name().toLowerCase()));
  }

}
