package edu.ntnu.idatt2001.paths.controller;

import java.util.Objects;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Controller for Music.
 */
public class MusicController {

  private static MediaPlayer menuMusic;
  private static MediaPlayer victoryMusic;
  private static MediaPlayer pageFlip;
  private static MediaPlayer pickUp;
  private static MediaPlayer deathMusic;
  private static MusicController instance;

  /**
   * Constructor for MusicController.
   */
  private MusicController() {
    Media menuMedia = new Media(
        Objects.requireNonNull(getClass().getResource("/music/menuMusic.mp3")).toString());
    menuMusic = new MediaPlayer(menuMedia);
    menuMusic.setVolume(0.2);
    menuMusic.setCycleCount(MediaPlayer.INDEFINITE);

    Media victoryMedia = new Media(
        Objects.requireNonNull(getClass().getResource("/music/victoryMusic.mp3")).toString());
    victoryMusic = new MediaPlayer(victoryMedia);
    victoryMusic.setVolume(0.2);

    Media pageFlipMedia = new Media(
        Objects.requireNonNull(getClass().getResource("/music/pageFlip.mp3")).toString());
    pageFlip = new MediaPlayer(pageFlipMedia);
    pageFlip.setVolume(0.4);
    pageFlip.setOnEndOfMedia(() -> pageFlip.stop());

    Media pickUpMedia = new Media(
        Objects.requireNonNull(getClass().getResource("/music/pickUp.mp3")).toString());
    pickUp = new MediaPlayer(pickUpMedia);
    pickUp.setVolume(0.5);
    pickUp.setOnEndOfMedia(() -> pickUp.stop());

    Media deathMedia = new Media(
        Objects.requireNonNull(getClass().getResource("/music/deathMusic.mp3")).toString());
    deathMusic = new MediaPlayer(deathMedia);
    deathMusic.setVolume(0.2);
  }

  /**
   * gets MusicController instance.
   *
   * @return MusicController.
   */
  public static MusicController getInstance() {
    if (instance == null) {
      instance = new MusicController();
    }
    return instance;
  }

  /**
   * Gets pageFlip sound effect.
   *
   * @return pageFlip sound effect.
   */
  public MediaPlayer getPageFlip() {
    return pageFlip;
  }

  /**
   * Gets death music.
   *
   * @return Death music.
   */
  public MediaPlayer getDeathMusic() {
    return deathMusic;
  }

  /**
   * Gets pick up sound effect.
   *
   * @return Pick up sound effect.
   */
  public MediaPlayer getPickUp() {
    return pickUp;
  }

  /**
   * Gets menu music.
   *
   * @return Menu music.
   */
  public MediaPlayer getMenuMusic() {
    return menuMusic;
  }

  /**
   * Gets victory sound effect.
   *
   * @return Victory sound effect.
   */
  public MediaPlayer getVictoryMusic() {
    return victoryMusic;
  }
}
