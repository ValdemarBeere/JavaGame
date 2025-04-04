package edu.ntnu.idatt2001.paths.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2001.paths.model.actions.Action;
import edu.ntnu.idatt2001.paths.model.exceptions.LinkValidationException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class LinkTest {

  @Test
  public void cannot_add_null_action() {
    Link link = new Link("Link text", "Reference");
    assertThrows(LinkValidationException.class, () -> link.addAction(null));
  }

  @Nested
  public class LinkConstructorTest {

    @Test
    public void link_cannot_have_null_reference() {
      assertThrows(
          LinkValidationException.class, () ->
              new Link("Example link to a passage", null));
    }

    @Test
    public void link_cannot_have_whitespace_reference() {
      assertThrows(
          LinkValidationException.class,
          () -> new Link("Example link to a passage", "  ")
      );
    }

    @Test
    public void link_cannot_have_null_text() {
      assertThrows(LinkValidationException.class, () -> new Link(null, "Example reference"));
    }

    @Test
    public void link_cannot_have_empty_text() {
      assertThrows(LinkValidationException.class, () -> new Link("", "Example reference"));
    }

    @Test
    public void illegal_argument_with_actions_as_null() {
      assertThrows(LinkValidationException.class, () ->
          new Link("Link text", "Reference", null, null));
    }

    @Test
    public void link_cannot_have_have_actions_containing_null() {
      List<Action<?>> actions = new ArrayList<>();
      actions.add(null);
      assertThrows(LinkValidationException.class, () ->
          new Link("Link text", "Reference", actions, null));
    }

    @Test
    public void new_array_list_created_when_actions_is_undefined() {
      Link link = new Link("Link text", "Reference");
      assertEquals(0, link.getActions().size());
      assertTrue(link.getActions() instanceof ArrayList);
    }
  }

  @Nested
  public class LinkEqualityTest {

    @Test
    public void links_are_not_equal_if_they_have_same_text_but_not_reference() {
      Link link1 = new Link("Example link to a passage", "Reference 1");
      Link link2 = new Link("Example link to a passage", "Reference 2");
      assertNotEquals(link1, link2);
    }

    @Test
    public void links_with_equal_reference_but_not_text_are_equal() {
      Link link1 = new Link("First link", "Reference");
      Link link2 = new Link("Second link", "Reference");
      assertEquals(link1, link2);
    }
  }

}
