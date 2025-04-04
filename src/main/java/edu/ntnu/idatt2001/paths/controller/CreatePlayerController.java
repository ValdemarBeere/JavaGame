package edu.ntnu.idatt2001.paths.controller;

import edu.ntnu.idatt2001.paths.model.game.GameInstance;
import edu.ntnu.idatt2001.paths.model.player.Player;
import edu.ntnu.idatt2001.paths.model.player.PlayerBuilder;
import java.util.ArrayList;
import java.util.function.Consumer;
import javafx.scene.image.Image;

/**
 * Controller class responsible for creating a Player instance and managing the game starting
 * mechanism.
 */
public class CreatePlayerController {

  private Consumer<Player> gameStarter;

  /**
   * Creates a new Player with the given name and image, and starts the game.
   *
   * @param name        The name of the Player.
   * @param playerImage The image of the Player.
   */
  public void createPlayer(String name, Image playerImage) {

    Player player = new PlayerBuilder()
        .setName(name)
        .setInventory(new ArrayList<>())
        .setImage(playerImage)
        .build();
    GameInstance.getInstance().setPlayer(player);

    if (gameStarter != null) {
      gameStarter.accept(player);

    }
  }

  /**
   * Sets the Consumer<Player> callback that will be triggered when a new player is created.
   *
   * @param gameStarter A Consumer<Player> instance.
   */
  public void setGameStarter(Consumer<Player> gameStarter) {
    this.gameStarter = gameStarter;
  }
}
