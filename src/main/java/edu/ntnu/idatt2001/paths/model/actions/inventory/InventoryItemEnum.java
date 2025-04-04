package edu.ntnu.idatt2001.paths.model.actions.inventory;

public enum InventoryItemEnum {
  SWORD("Sword", "/images/items/sword.png"),
  SHIELD("Shield", "/images/items/shield.png"),
  HELMET("Helmet", "/images/items/helmet.png"),
  ARMOR("Armor", "/images/items/armor.png"),
  POTION("Potion", "/images/items/potion.png"),
  SCROLL("Scroll", "/images/items/scroll.png"),
  RING("Ring", "/images/items/ring.png"),
  AMULET("Amulet", "/images/items/amulet.png"),
  CROWN("Crown", "/images/items/crown.png"),
  MAP("Map", "/images/items/map.png"),
  KEY("Key", "/images/items/key.png"),
  COIN("Coin", "/images/items/coin.png"),
  GEM("Gem", "/images/items/gem.png"),
  BOOK("Book", "/images/items/book.png"),
  TORCH("Torch", "/images/items/torch.png"),
  BOW("Bow", "/images/items/bow.png");

  private final String name;
  private final String imagePath;

  InventoryItemEnum(String name, String imagePath) {
    this.name = name;
    this.imagePath = imagePath;
  }

  public static boolean contains(String test) {
    for (InventoryItemEnum item : InventoryItemEnum.values()) {
      if (item.getName().equals(test)) {
        return true;
      }
    }
    return false;
  }

  public String getName() {
    return name.toLowerCase();
  }

  public String getImagePath() {
    return imagePath;
  }
}

