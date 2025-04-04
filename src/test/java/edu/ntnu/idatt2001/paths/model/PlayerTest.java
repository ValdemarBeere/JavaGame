package edu.ntnu.idatt2001.paths.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2001.paths.model.exceptions.PlayerValidationException;
import edu.ntnu.idatt2001.paths.model.player.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class PlayerTest {

  @Test
  public void can_instantiate_player_with_image_and_inventory() {
    List<String> inventory = List.of("Sword", "Potion");
    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
        "/images/passageImages/passageContentImages/chest.png"))); // create an Image
    Player player = new Player("Player", 100, 0, 0, inventory, image);
    assertEquals(2, player.getInventory().size());
    assertEquals(image, player.getImage());
  }

  @Test
  public void can_reset_player() {
    Player player = new Player("Player", 50, 100, 50);
    player.addToInventory("Sword");
    player.resetPlayer();
    assertEquals(100, player.getHealth());
    assertEquals(0, player.getScore());
    assertEquals(0, player.getGold());
    assertTrue(player.getInventory().isEmpty());
  }

  @Test
  public void can_generate_to_string() {
    Player player = new Player("Player", 100, 100, 100);
    player.addToInventory("Sword");
    String expected = "Player: Player, Health: 100, Score: 100, Gold: 100, Inventory: [sword]";
    assertEquals(expected, player.toString());
  }

  @Nested
  public class PlayerConstructorTest {

    @Test
    public void player_cannot_have_blank_name() {
      assertThrows(IllegalArgumentException.class, () -> new Player("  "));
    }

    @Test
    public void player_can_be_instantiated_with_name_only() {
      new Player("Player");
      assertTrue(true);
    }

    @Test
    public void player_set_with_name_only_has_defaults_for_health_score_and_gold() {
      Player player = new Player("Player");
      assertEquals(100, player.getHealth());
      assertEquals(0, player.getScore());
      assertEquals(0, player.getGold());
    }

    @Test
    public void inventory_no_set_throws() {
      Player player = new Player("Player");
      assertEquals(0, player.getInventory().size());
    }


    @Test
    public void health_cannot_start_at_zero_or_below() {
      assertThrows(PlayerValidationException.class, () -> new Player("Player", 0, 1, 1));
      assertThrows(PlayerValidationException.class, () -> new Player("Player", -1, 1, 1));
    }

    @Test
    public void gold_cannot_be_negative() {
      assertThrows(PlayerValidationException.class, () -> new Player("Player", 1, 1, -1));
    }

    @Test
    public void score_cannot_start_as_negative() {
      assertThrows(PlayerValidationException.class, () -> new Player("Player", 1, -1, 1));
    }

    @Test
    public void gold_and_score_can_be_zero() {
      new Player("Player", 1, 0, 0);
      assertTrue(true);
    }

    @Test
    public void can_set_and_get_image() {
      Player player = new Player("Player");
      Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
          "/images/passageImages/passageContentImages/chest.png"))); // create an Image
      player.setImage(image);
      assertEquals(image, player.getImage());
    }


    @Test
    public void adding_inventory_items_with_whitespace_or_uppercase_letters_are_changed() {
      List<String> inventory = List.of("Sword", "Shield  ", "  POTION");
      Player player = new Player("Player", 1, 0, 0, inventory, null);
      assertEquals("sword", player.getInventory().get(0));
      assertEquals("shield", player.getInventory().get(1));
      assertEquals("potion", player.getInventory().get(2));
    }

    @Test
    public void adding_inventory_items_which_are_null_throws_exception() {
      List<String> inventory = new ArrayList<>(List.of("Sword", "Potion"));
      inventory.add(null);
      assertThrows(PlayerValidationException.class,
          () -> new Player("Player", 1, 0, 0, inventory, null));
    }

    @Test
    public void constructor_trims_and_converts_inventory_items_to_lowercase() {
      List<String> inventory = List.of("  Sword  ", "  POTION  ");
      Player player = new Player("Player", 100, 0, 0, inventory, null);
      assertEquals(2, player.getInventory().size());
      assertTrue(player.isInInventory("sword"));
      assertTrue(player.isInInventory("potion"));
    }

  }

  @Nested
  public class PlayerEqualityTest {

    @Test
    public void players_with_equal_name_are_not_equal() {
      Player player1 = new Player("Player");
      Player player2 = new Player("Player");
      assertNotEquals(player1, player2);
    }
  }

  @Nested
  public class PlayerModifyingScoreGoldHealthTest {

    @Test
    public void set_health_accepts_negative_numbers() {
      Player player = new Player("Player", 10, 0, 0);
      player.addHealth(-5);
      assertEquals(5, player.getHealth());
    }

    @Test
    public void can_add_health_to_player() {
      Player player = new Player("Player", 10, 0, 0);
      player.addHealth(5);
      assertEquals(15, player.getHealth());
    }

    @Test
    public void set_health_cannot_give_negative_health() {
      Player player = new Player("Player", 1, 0, 0);
      player.addHealth(-5);
      assertEquals(0, player.getHealth());
    }

    @Test
    public void set_score_accepts_negative_numbers() {
      Player player = new Player("Player", 10, 10, 0);
      player.addScore(-5);
      assertEquals(5, player.getScore());
    }

    @Test
    public void can_add_score_to_player() {
      Player player = new Player("Player", 10, 10, 0);
      player.addScore(5);
      assertEquals(15, player.getScore());
    }

    @Test
    public void set_score_can_give_negative_score() {
      Player player = new Player("Player", 1, 0, 0);
      player.addScore(-5);
      assertEquals(-5, player.getScore());
    }

    @Test
    public void set_gold_accepts_negative_numbers() {
      Player player = new Player("Player", 10, 0, 10);
      player.addGold(-5);
      assertEquals(5, player.getGold());
    }

    @Test
    public void can_add_gold_to_player() {
      Player player = new Player("Player", 1, 0, 10);
      player.addGold(5);
      assertEquals(15, player.getGold());
    }

    @Test
    public void set_gold_cannot_give_negative_gold() {
      Player player = new Player("Player", 1, 0, 1);
      player.addGold(-5);
      assertEquals(0, player.getGold());
    }

    @Test
    public void player_is_not_alive_when_health_is_zero() {
      Player player = new Player("Player", 1, 0, 0);
      player.addHealth(-5);
      assertEquals(0, player.getHealth());
      assertFalse(player.isAlive());
    }

    @Test
    public void subtracting_gold_from_broke_player_does_not_go_negative() {
      Player player = new Player("Player", 100, 0, 0);
      player.addGold(-10);
      assertEquals(0, player.getGold());
    }

    @Test
    public void adding_negative_score_to_player_can_go_negative() {
      Player player = new Player("Player", 100, 10, 10);
      player.addScore(-20);
      assertEquals(-10, player.getScore());
    }

  }

  @Nested
  public class ModifyingInventoryTest {

    private Player player;

    @BeforeEach
    public void setup() {
      player = new Player("Player");
      player.addToInventory("Item");
    }

    @Test
    public void can_add_item_to_inventory() {
      player.addToInventory("Item");
      assertEquals(2, player.getInventory().size());
      assertTrue(player.isInInventory("Item"));
    }

    @Test
    public void cannot_add_empty_text_to_inventory() {
      assertThrows(PlayerValidationException.class, () -> player.addToInventory(""));
    }

    @Test
    public void cannot_add_null_to_inventory() {
      assertThrows(PlayerValidationException.class, () -> player.addToInventory(null));
    }

    @Test
    public void can_add_duplicate_item_to_inventory() {
      player.addToInventory("Item");
      player.addToInventory("Item");
      assertEquals(3, player.getInventory().size());
      assertTrue(player.isInInventory("Item"));
      assertEquals("item", player.getInventory().get(0));
      assertEquals("item", player.getInventory().get(1));
    }

    @Test
    public void is_in_inventory_throws_when_item_is_null() {
      assertThrows(PlayerValidationException.class, () -> player.isInInventory(null));
    }

    @Test
    public void is_in_inventory_throws_when_item_is_empty() {
      assertThrows(PlayerValidationException.class, () -> player.isInInventory(""));
    }

    @Test
    public void is_in_inventory_returns_true_if_item_exists() {
      player.addToInventory("Item");
      assertTrue(player.isInInventory("Item"));
    }

    @Test
    public void is_in_inventory_returns_false_if_item_does_not_exists() {
      assertFalse(player.isInInventory("Item2"));
    }

    @Test
    public void inventory_becomes_full() {
      Player player = new Player("Player");
      for (int i = 0; i < 16; i++) { //MAX inventory size
        player.addToInventory("Item" + i);
      }
      assertTrue(player.isInventoryFull());
    }


  }


}
