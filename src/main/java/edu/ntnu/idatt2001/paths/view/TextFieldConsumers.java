package edu.ntnu.idatt2001.paths.view;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class TextFieldConsumers {

  // Consumer that allows only numbers in the range from -100 to 100 excluding 0
  public static UnaryOperator<Change> getIntegerInRangeFilter() {
    return change -> {
      String newText = change.getControlNewText();
      if (newText.isEmpty() || "-".equals(newText)) {
        return change;
      }
      try {
        int newInt = Integer.parseInt(newText);
        if (newInt != 0 && newInt >= -100 && newInt <= 100) {
          return change;
        } else {
          showTooltip((TextField) change.getControl(),
              "Only integers between -100 and 100 (except 0) are allowed.");
          return null;
        }
      } catch (NumberFormatException e) {
        showTooltip((TextField) change.getControl(), "Invalid input: not a valid integer.");
        return null;
      }
    };
  }


  // Consumer that restricts the first character to be one of the symbols :, [, {, |, ^
  // and limits the size to 50 characters.
  public static UnaryOperator<TextFormatter.Change> getSymbolAndSizeFilter() {
    return change -> {
      String newText = change.getControlNewText();
      Pattern pattern = Pattern.compile("^[,\\[{|^].{0,35}$");
      if (pattern.matcher(newText).matches()) {
        showTooltip((TextField) change.getControl(),
            "First character cannot be one of: : [ { | ^. Max length: 35 characters.");
        return null;
      }
      return change;
    };
  }

  public static UnaryOperator<TextFormatter.Change> getSymbolAndSizeFilterPlus() {
    return change -> {
      String newText = change.getControlNewText();
      Pattern pattern = Pattern.compile("^[,\\[{|^].{0,70}$");
      if (pattern.matcher(newText).matches()) {
        showTooltip((TextField) change.getControl(),
            "First character cannot be one of: : [ { | ^. Max length: 70 characters.");
        return null;
      }
      return change;
    };
  }

  public static UnaryOperator<Change> getAlphabeticalAndSizeFilter() {
    return change -> {
      String newText = change.getControlNewText();
      if (newText.length() <= 20 && newText.matches("[a-zA-Z]*")) {
        return change;
      } else {
        showTooltip((TextField) change.getControl(),
            "Only alphabetical characters are allowed. Max length: 20 characters.");
        return null;
      }
    };
  }


  // Helper method to display a tooltip
  private static void showTooltip(TextField textField, String message) {
    Tooltip tooltip = new Tooltip(message);
    tooltip.setShowDelay(Duration.seconds(1));
    tooltip.setHideDelay(Duration.seconds(3));
    tooltip.setAutoHide(true);
    tooltip.show(textField.getScene().getWindow());
  }
}
