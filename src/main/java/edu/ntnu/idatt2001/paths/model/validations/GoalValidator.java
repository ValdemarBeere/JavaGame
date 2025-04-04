package edu.ntnu.idatt2001.paths.model.validations;

import edu.ntnu.idatt2001.paths.model.actions.inventory.InventoryItemEnum;
import edu.ntnu.idatt2001.paths.model.exceptions.GoalValidationException;
import edu.ntnu.idatt2001.paths.model.goals.Goal;
import edu.ntnu.idatt2001.paths.model.goals.GoldGoal;
import edu.ntnu.idatt2001.paths.model.goals.HealthGoal;
import edu.ntnu.idatt2001.paths.model.goals.InventoryGoal;
import edu.ntnu.idatt2001.paths.model.goals.ScoreGoal;

public class GoalValidator {

  public static void validateGoals(Goal<?> goal) throws GoalValidationException {
    if (goal instanceof GoldGoal) {
      if (((GoldGoal) goal).getValue() < 0) {
        throw new GoalValidationException("Gold goal cannot be negative");
      }
    } else if (goal instanceof ScoreGoal) {
      if (((ScoreGoal) goal).getValue() < 0) {
        throw new GoalValidationException("Score goal cannot be negative");
      }
    } else if (goal instanceof HealthGoal) {
      if (((HealthGoal) goal).getValue() <= 0) {
        throw new GoalValidationException("Health goal must be greater than 0");
      }
    } else if (goal instanceof InventoryGoal) {
      if (!InventoryItemEnum.contains(((InventoryGoal) goal).getValue())) {
        throw new GoalValidationException("Inventory goal must be a valid InventoryItemEnum");
      }
    } else {
      throw new GoalValidationException("Unknown goal type");
    }
  }
}
