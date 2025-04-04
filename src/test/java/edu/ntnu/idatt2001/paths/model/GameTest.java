package edu.ntnu.idatt2001.paths.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2001.paths.model.actions.inventory.InventoryAction;
import edu.ntnu.idatt2001.paths.model.exceptions.GameValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.StoryValidationException;
import edu.ntnu.idatt2001.paths.model.game.Game;
import edu.ntnu.idatt2001.paths.model.goals.Goal;
import edu.ntnu.idatt2001.paths.model.goals.HealthGoal;
import edu.ntnu.idatt2001.paths.model.player.Player;
import edu.ntnu.idatt2001.paths.model.player.PlayerBuilder;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Game class. These tests make sure that the game is created correctly and that it
 * throws the correct exceptions when it is given invalid arguments. We also test that different
 * methods in the game class work as intended.
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class GameTest {

  private Story story;
  private Player player;
  private List<Goal<?>> goals;
  private Passage openingPassage;

  @BeforeEach
  public void setUp() {
    goals = new ArrayList<>();
    goals.add(new HealthGoal(2));
    player = new PlayerBuilder().setName("Name").build();
    openingPassage = new Passage("Passage name", "Content");
    story = new Story("new story", openingPassage);
  }

  /**
   * Tests for the Game constructor. Tests that the constructor cannot have null parameters. We also
   * test that the constructor cannot have null or empty list of goals. We also test that the
   * constructor cannot have null goals in the list.
   */
  @Nested
  public class ConstructorTest {

    @Test
    public void player_cannot_be_null() {
      assertThrows(GameValidationException.class, () -> new Game(null, story, goals));
    }

    @Test
    public void story_cannot_be_null() {
      assertThrows(GameValidationException.class, () -> new Game(player, null, goals));
    }

    @Test
    public void list_of_goals_cannot_be_null() {
      assertThrows(GameValidationException.class, () -> new Game(player, story, null));
    }

    @Test
    public void list_of_goals_cannot_contain_null() {
      goals.add(null);
      assertThrows(GameValidationException.class, () -> new Game(player, story, goals));
    }

    @Test
    public void list_of_goals_cannot_be_empty() {
      assertThrows(
          GameValidationException.class,
          () -> new Game(player, story, new ArrayList<>())
      );
    }

    @Test
    public void game_is_created_with_all_non_null_parameters() {
      Game game = new Game(player, story, goals);
      assertEquals(player, game.getPlayer());
      assertEquals(story, game.getStory());
      assertEquals(goals, game.getGoals());
    }
  }

  /**
   * Tests for the Game traversal methods. Tests that the game can begin and that it returns the
   * correct passage. We also test that the game can go to a passage and that it returns the correct
   * passage. We also test that the game throws the correct exceptions when it is given invalid
   * arguments. We check that the game throws an exception if the story is not playable.
   */
  @Nested
  public class GameTraversal {

    @Test
    public void begin_game_returns_the_opening_passage() {
      Story story = new Story("new story", openingPassage);
      Game game = new Game(player, story, goals);
      assertEquals(openingPassage, game.begin());
    }

    @Test
    public void passage_is_throws_if_link_references_non_existent_passage() {
      Game game = new Game(player, story, goals);
      assertThrows(StoryValidationException.class,
          () -> game.go(new Link("link", "non existent passage")));
    }

    @Nested
    public class ExecuteAction {

      @Test
      public void execute_action_adds_item_to_inventory() {
        String testItem = "gem";
        InventoryAction inventoryAction = new InventoryAction(testItem);

        Link link = new Link("link", "passage");
        link.addAction(inventoryAction);

        Game game = new Game(player, story, goals);
        game.executeAction(link);

        assertTrue(player.getInventory().contains(testItem.toLowerCase()),
            "The action's execute"
                + " method should have added the item to the inventory.");
      }

      @Test
      public void execute_action_does_not_remove_item_from_inventory() {
        String testItem = "gem";
        player.addToInventory(testItem);
        InventoryAction inventoryAction = new InventoryAction(testItem);

        Link link = new Link("link", "passage");
        link.addAction(inventoryAction);

        Game game = new Game(player, story, goals);
        game.executeAction(link);

        assertTrue(player.getInventory().contains(testItem.toLowerCase()),
            "The action's execute method should not remove the item from the inventory if it was already there.");
      }
    }

    @Nested
    public class FollowLink {

      @Test
      public void can_follow_link_returns_true_when_no_required_item() {
        Link link = new Link("link", "passage");
        Game game = new Game(player, story, goals);

        assertTrue(game.canFollowLink(link),
            "Should return true when there is no required item.");
      }

      @Test
      public void can_follow_link_returns_true_when_required_item_in_inventory() {
        String requiredItemValue = "gem";
        player.addToInventory(requiredItemValue);
        InventoryAction inventoryAction = new InventoryAction(requiredItemValue);
        Link link = new Link("link", "passage");
        link.setRequiredItem(inventoryAction);

        Game game = new Game(player, story, goals);

        assertTrue(game.canFollowLink(link),
            "Should return true when the required item is in the inventory.");
      }

      @Test
      public void can_follow_link_returns_false_when_required_item_not_in_inventory() {
        String requiredItemValue = "gem";
        InventoryAction inventoryAction = new InventoryAction(requiredItemValue);
        Link link = new Link("link", "passage");
        link.setRequiredItem(inventoryAction);

        Game game = new Game(player, story, goals);

        assertFalse(game.canFollowLink(link),
            "Should return false when the required item is not in the inventory.");
      }
    }

    @Nested
    public class ProgressGame {

      @Test
      public void progress_game_throws_when_cannot_follow_link() {
        String requiredItemValue = "gem";
        InventoryAction inventoryAction = new InventoryAction(requiredItemValue);
        Link link = new Link("link", "passage");
        link.setRequiredItem(inventoryAction);

        Game game = new Game(player, story, goals);

        assertThrows(RuntimeException.class, () -> game.progressGame(link));
      }

      @Test
      public void progress_game_does_not_throw_when_can_follow_link() {
        Link link = new Link("link", "passage1");
        Passage passage = new Passage("passage", "content");
        passage.addLink(link);
        Link link1 = new Link("Link", "passage");
        Passage passage1 = new Passage("passage1", "content");
        passage1.addLink(link1);
        story.addPassage(passage);
        story.addPassage(passage1);
        Game game = new Game(player, story, goals);

        assertDoesNotThrow(() -> game.progressGame(link));
      }
    }
  }
}
