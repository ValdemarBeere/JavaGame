package edu.ntnu.idatt2001.paths.model;

import edu.ntnu.idatt2001.paths.model.actions.Action;
import edu.ntnu.idatt2001.paths.model.actions.inventory.InventoryAction;
import edu.ntnu.idatt2001.paths.model.exceptions.LinkValidationException;
import edu.ntnu.idatt2001.paths.model.validations.LinkValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * A link makes it possible to go from one passage to another. Links bind the various parts together
 * of a story.
 */
public class Link {

  private static final Logger logger = Logger.getLogger(Link.class.getName());
  /**
   * A list of special objects that make it possible to influence the properties of a player.
   */
  private final List<Action<?>> actions;
  /**
   * A descriptive text that indicates a choice or an action in a story. The text is the part of the
   * link that will be visible to the player.
   */
  private String text;
  /**
   * A string that uniquely identifies a passage (part of a story). In practice this will be the
   * title of the passage you wish to refer to.
   */
  private String reference;
  private InventoryAction requiredItem;

  /**
   * Instantiates a new Link.
   *
   * @param text         the text
   * @param reference    the reference
   * @param actions      the actions
   * @param requiredItem the required item
   */
  public Link(String text, String reference, final List<Action<?>> actions,
      InventoryAction requiredItem) {
    LinkValidator.validateLink(text, reference, actions);
    this.text = text;
    this.reference = reference;
    this.actions = new ArrayList<>();
    this.requiredItem = requiredItem;
    logger.info("Link created with required item");
  }


  /**
   * Creates a new link with the specified text and reference.
   *
   * @param text      The descriptive text of the link
   * @param reference The reference string of the link
   * @throws LinkValidationException the link validation exception
   */
  public Link(final String text, final String reference) {
    LinkValidator.validateLink(text, reference, new ArrayList<>());
    this.text = text;
    this.reference = reference;
    this.actions = new ArrayList<>();
    this.requiredItem = null;
    logger.info("Link created without required item");
  }

  /**
   * Returns the descriptive text of the link.
   *
   * @return The text of the link
   */
  public String getText() {
    return text;
  }

  /**
   * Sets text.
   *
   * @param linkText the link text
   */
  public void setText(String linkText) {
    this.text = linkText;
  }

  /**
   * Returns the reference string of the link.
   *
   * @return The reference string of the link
   */
  public String getReference() {
    return reference;
  }

  /**
   * Sets reference.
   *
   * @param newValue the new value
   */
  public void setReference(String newValue) {
    this.reference = newValue;
  }

  /**
   * Adds the specified action to the list of actions associated with the link.
   *
   * @param action The action to add
   */
  public void addAction(final Action<?> action) {
    LinkValidator.validateAddAction(action);
    actions.add(action);
    logger.info("Action added to the link");
  }

  /**
   * Returns the list of actions associated with the link.
   *
   * @return The list of actions associated with the link
   */
  public List<Action<?>> getActions() {
    return actions;
  }

  /**
   * Gets required item.
   *
   * @return the required item
   */
  public InventoryAction getRequiredItem() {
    return requiredItem;
  }

  /**
   * Sets required item.
   *
   * @param requiredItemAction the required item action
   */
  public void setRequiredItem(InventoryAction requiredItemAction) {
    this.requiredItem = requiredItemAction;
  }

  /**
   * Returns a string summary of all the actions associated with the link.
   *
   * @return A summary of the actions
   */
  public String getActionSummary() {
    StringBuilder sb = new StringBuilder();
    for (Action<?> action : actions) {
      sb.append(action.getDescription()).append("\n");
    }
    return sb.toString();
  }

  /**
   * Link has item requirement boolean.
   *
   * @return the boolean
   */
  public boolean linkHasItemRequirement() {
    return getRequiredItem() != null;
  }

  /**
   * Returns a string representation of the link, which is just the descriptive text.
   *
   * @return The descriptive text of the link
   */
  @Override
  public String toString() {
    return text;
  }

  /**
   * Two links are equal if they have the same reference This is to make the required map of links
   * to passages point links with different text and actions to the same passage
   *
   * @param o Object - Any object equality checked with this link
   * @return boolean - true if the links are equal, false if not
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      logger.info("Link is compared to itself");
      return true;
    }
    if (!(o instanceof Link link)) {
      logger.info("Object compared to link is not of type Link");
      return false;
    }
    boolean result = Objects.equals(getReference(), link.getReference());
    logger.info(result ? "Links are equal" : "Links are not equal");
    return result;
  }

  /**
   * Returns a hashcode for the link based on the reference
   *
   * @return int - hashcode for the link
   */
  @Override
  public int hashCode() {
    logger.info("Hashcode method called");
    return Objects.hash(getReference());
  }
}