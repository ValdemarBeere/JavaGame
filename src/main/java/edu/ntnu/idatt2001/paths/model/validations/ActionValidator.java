package edu.ntnu.idatt2001.paths.model.validations;

import edu.ntnu.idatt2001.paths.model.actions.Action;
import edu.ntnu.idatt2001.paths.model.actions.GoldAction;
import edu.ntnu.idatt2001.paths.model.actions.HealthAction;
import edu.ntnu.idatt2001.paths.model.actions.ScoreAction;
import edu.ntnu.idatt2001.paths.model.actions.inventory.InventoryAction;
import edu.ntnu.idatt2001.paths.model.actions.inventory.InventoryItemEnum;
import edu.ntnu.idatt2001.paths.model.exceptions.ActionValidationException;

/**
 * The type Action validator.
 */
public class ActionValidator {

  /**
   * Validate action.
   *
   * @param action the action
   * @throws ActionValidationException the action validation exception
   */
  public static void validateAction(Action<?> action) throws ActionValidationException {
    if (action instanceof GoldAction) {
      validateGoldAction((GoldAction) action);
    } else if (action instanceof HealthAction) {
      validateHealthAction((HealthAction) action);
    } else if (action instanceof InventoryAction) {
      validateInventoryAction((InventoryAction) action);
    } else if (action instanceof ScoreAction) {
      validateScoreAction((ScoreAction) action);
    } else {
      throw new ActionValidationException("Unknown action type");
    }
  }

  private static void validateGoldAction(GoldAction action) throws ActionValidationException {
    if (!(action.getValue() >= -100 && action.getValue() <= 100)) {
      throw new ActionValidationException(
          "Gold amount cannot be 0 or bigger\n than 0 and smaller than -100");
    }
  }

  private static void validateHealthAction(HealthAction action) throws ActionValidationException {
    if (!(action.getValue() >= -100 && action.getValue() <= 100)) {
      throw new ActionValidationException(
          "Health amount cannot be 0 or bigger\n than 0 and smaller than -100");
    }
  }

  private static void validateInventoryAction(InventoryAction action)
      throws ActionValidationException {
    if (action.getValue() == null || action.getValue().trim().isEmpty()) {
      throw new ActionValidationException("Item cannot be null or blank");
    }
    if (!action.getValue().matches("^[a-zA-Z ]*$")) {
      throw new ActionValidationException("Item need to consist of only alphabetic numbers");
    }
    if (!InventoryItemEnum.contains(action.getValue())) {
      throw new ActionValidationException("Inventory Action must be a valid InventoryItemEnum");
    }


  }

  private static void validateScoreAction(ScoreAction action) throws ActionValidationException {
    if (!(action.getValue() >= -100 && action.getValue() <= 100)) {
      throw new ActionValidationException(
          "Score amount cannot be 0 or bigger\n than 0 and smaller than -100");
    }

  }

  public static void validateAction(String actionType, String actionValue) {
    if (("HealthAction".equals(actionType) || "GoldAction".equals(actionType)
        || "ScoreAction".equals(actionType)) && !actionValue.matches("-?\\d+")) {
      throw new ActionValidationException(
          "Action value for " + actionType + " must be of type integer");
    }
  }

  public static void validateAction(final String item) throws ActionValidationException {
    if (item == null || item.trim().isEmpty()) {
      throw new ActionValidationException("Item cannot be null or blank");
    }
    if (!item.matches("^[a-zA-Z ]*$")) {
      throw new ActionValidationException("Item need to consist of only alphabetic numbers");
    }
    if (!InventoryItemEnum.contains(item)) {
      throw new ActionValidationException("Inventory Action must be a valid InventoryItemEnum");
    }

  }

  public static void validateAction(final int number) throws ActionValidationException {
    if (!(number >= -100 && number <= 100)) {
      throw new ActionValidationException(
          "Health amount cannot be 0 or bigger\n than 0 and smaller than -100");
    }
  }


}
