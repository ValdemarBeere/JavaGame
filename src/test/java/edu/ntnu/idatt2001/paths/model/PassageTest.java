package edu.ntnu.idatt2001.paths.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2001.paths.model.exceptions.PassageValidationException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class PassageTest {

  private static Passage passage1, passage2, passage3;
  private static Link link;

  @BeforeEach
  public void setUp() {
    passage1 = new Passage("Title", "Content");
    passage2 = new Passage("Title", "Content");
    passage3 = new Passage("NotATitle", "NotContent");
    link = new Link("Text", "Reference");
  }

  @Test
  public void test_equals() {
    assertEquals(passage1, passage2);
    assertNotEquals(passage1, passage3);
  }

  @Test
  @DisplayName("Test hashCode for passages")
  void test_hash_code() {
    assertEquals(passage1.hashCode(), passage2.hashCode());
    assertNotEquals(passage1.hashCode(), passage3.hashCode());
  }

  @Nested
  class TestConstructor {


    @Test
    void constructor_should_throw_exception_when_title_is_null() {
      Passage passage = new Passage(null, "Content");
      assertThrows(PassageValidationException.class, passage::validatePassage);
    }

    @Test
    void constructor_should_throw_exception_when_title_is_empty() {
      Passage passage = new Passage("", "Content");
      assertThrows(PassageValidationException.class, passage::validatePassage);
    }

    @Test
    void constructor_should_throw_exception_when_content_is_null() {
      Passage passage = new Passage("Title", null);
      assertThrows(PassageValidationException.class, passage::validatePassage);
    }

    @Test
    void constructor_should_throw_exception_when_content_is_empty() {
      Passage passage = new Passage("Title", "");
      assertThrows(PassageValidationException.class, passage::validatePassage);
    }

    @Test
    void constructor_should_throw_exception_when_content_starts_with_invalid_symbol() {
      Passage passage = new Passage("Title", "|Content");
      Passage passage1 = new Passage("Title", "::Content");
      Passage passage2 = new Passage("Title", "^Content");
      Passage passage3 = new Passage("Title", "[Content");
      Passage passage4 = new Passage("Title", "{Content");
      assertThrows(PassageValidationException.class, passage::validatePassage);
      assertThrows(PassageValidationException.class, passage1::validatePassage);
      assertThrows(PassageValidationException.class, passage2::validatePassage);
      assertThrows(PassageValidationException.class, passage3::validatePassage);
      assertThrows(PassageValidationException.class, passage4::validatePassage);
    }

    @Test
    void constructor_should_not_throw_exception_when_all_parameters_are_valid() {
      Passage passage = new Passage("Title", "Content",
          null, null,
          List.of(new Link("Text", "Reference")));
      assertDoesNotThrow(passage::validatePassage);
    }

    @Test
    void test_getters() {
      assertEquals("Title", passage1.getTitle());
      assertEquals("Content", passage1.getContent());
    }
  }

  @Nested
  class TestHasLink {

    @Test
    public void test_has_links_empty() {
      assertFalse(passage2.hasLinks(), "Passage should not have any links");
    }

    @Test
    public void test_has_links_non_empty() {
      passage1.addLink(link);
      assertTrue(passage1.hasLinks(), "Passage should have at least one link");
    }
  }

  @Nested
  class AddLink {

    @Test
    public void test_add_link() {
      assertTrue(passage1.addLink(link));
      assertTrue(passage1.getLinks().contains(link));
    }

    @Test
    public void test_add_link_throws_if_link_is_null() {
      assertThrows(PassageValidationException.class, () -> passage1.addLink(null));
    }

    @Test
    public void test_add_link_with_equal_reference_as_passage_title_throws() {
      Link link = new Link(passage1.getTitle(), passage1.getTitle());
      assertThrows(PassageValidationException.class, () -> passage1.addLink(link));
    }
  }

  @Nested
  class MarkedActionAsPerformed {

    @Test
    public void test_marked_link_as_performed() {
      // Given
      Story story = new Story("title", passage1);
      story.addPassage(passage3);

      // Mark action as performed
      Link lastLink = passage1.getLastLink();
      passage1.markActionAsPerformed(lastLink);

      // Then
      assertTrue(passage1.hasPerformedAction(lastLink));
    }

    @Test
    public void test_not_marked_link_as_not_performed() {
      // Given
      Story story = new Story("title", passage1);
      story.addPassage(passage3);

      // Then
      assertFalse(passage2.hasPerformedAction(passage2.getLastLink()));
    }

  }

}