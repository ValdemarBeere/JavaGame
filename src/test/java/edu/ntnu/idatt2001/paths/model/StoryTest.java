package edu.ntnu.idatt2001.paths.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2001.paths.model.exceptions.StoryValidationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class StoryTest {

  @Test
  public void two_stories_are_not_equal_if_they_have_different_titles() {
    Passage passage = new Passage("new valid passage", "content");
    Story story1 = new Story("new story", passage);
    Story story2 = new Story("new story 2", passage);
    assertNotEquals(story1, story2);
  }

  @Nested
  public class StoryConstructorTest {

    private Passage passage;
    private Map<Link, Passage> passages;

    @BeforeEach
    public void setUp() {
      passage = new Passage("new valid passage", "content");
      passages = new HashMap<>();
    }

    @Test
    public void story_cannot_be_instantiated_with_null_title() {
      assertThrows(StoryValidationException.class, () -> new Story(null, passages, passage));
    }

    @Test
    public void story_cannot_be_instantiated_with_whitespace_title() {
      assertThrows(StoryValidationException.class, () -> new Story("   ", passages, passage));
    }

    @Test
    public void story_cannot_be_instantiated_with_null_passages() {
      assertThrows(StoryValidationException.class,
          () -> new Story("new valid title", null, passage));
    }

    @Test
    public void story_cannot_be_instantiated_with_empty_passages() {
      assertThrows(StoryValidationException.class,
          () -> new Story("new story", new HashMap<>(), passage));
    }

    @Test
    public void story_cannot_be_instantiated_with_null_opening_passage() {
      assertThrows(
          StoryValidationException.class,
          () -> new Story("new story", new HashMap<>(), null)
      );
    }

    @Test
    public void story_instantiated_without_passages_has_map_of_only_opening_passage() {
      Story story = new Story("new story", passage);
      assertNotNull(story.getPassages());
      assertEquals(1, story.getPassages().size());
    }
  }

  @Nested
  public class StoryAddPassageTest {

    @Test
    public void adding_passage_creates_link_with_passage_title_as_text_and_reference_in_passages_map() {
      String passageName = "new valid passage";
      String passageName2 = "new valid passage 2";
      Passage passage = new Passage(passageName, "content");
      Passage passage2 = new Passage(passageName2, "content");
      Story story = new Story("new story", passage);
      story.addPassage(passage2);
      assertEquals(2, story.getPassages().size());
      assertTrue(story.getPassages().contains(passage2));
      assertEquals(passage2, story.getPassage(new Link(passageName2, passageName2)));
    }

    @Test
    public void getting_passage_after_adding_passage_checks_link_equality_and_not_text() {
      String openingPassageName = "opening valid passage";
      String passageName = "new valid passage";
      Passage openingPassage = new Passage(openingPassageName, "content");
      Passage passage = new Passage(passageName, "content");
      Story story = new Story("A new story", openingPassage);
      story.addPassage(passage);
      assertEquals(2, story.getPassages().size());
      assertTrue(story.getPassages().contains(passage));
      assertThrows(StoryValidationException.class,
          () -> story.getPassage(new Link(passageName, "not the same value")));
    }

    @Test
    public void adding_null_passage_throws_exception() {
      Story story = new Story("new story", new Passage("opening passage", "content"));
      assertThrows(NullPointerException.class, () -> story.addPassage(null));
    }

    @Test
    public void adding_same_passage_object_throws_exception() {
      String passageName = "new valid passage";
      Passage passage = new Passage(passageName, "content");
      Story story = new Story("new story", passage);
      assertThrows(IllegalArgumentException.class, () -> story.addPassage(passage));
    }

    @Test
    public void adding_passage_with_duplicate_title_throws_exception() {
      String passageName = "new valid passage";
      Passage passage = new Passage(passageName, "content");
      Passage passage2 = new Passage(passageName, "content");
      Story story = new Story("new story", passage);
      assertThrows(IllegalArgumentException.class, () -> story.addPassage(passage2));
    }
  }

  @Nested
  public class RemovePassageTest {

    @Test
    public void remove_passage_that_does_exist_returns_true() {
      Passage passage = new Passage("new valid passage", "content");
      Story story = new Story("new story", passage);
      assertTrue(story.removePassageUsingPassage(passage));
    }

    @Test
    public void remove_passage_that_does_not_exist_returns_false() {
      Passage passage = new Passage("new valid passage", "content");
      Story story = new Story("new story", passage);
      Passage passageToBeRemoved = new Passage("non-existent passage", "content");
      assertFalse(story.removePassageUsingPassage(passageToBeRemoved));
    }

    @Test
    public void remove_passage_with_null_link_returns_false() {
      Passage passage = new Passage("new valid passage", "content");
      Story story = new Story("new story", passage);
      assertFalse(story.removePassageUsingPassage(null));
    }

    @Test
    public void remove_passage_that_references_other_passages() {
      Passage passage = new Passage("new valid passage", "content");
      Passage passage2 = new Passage("new valid passage 2", "content");
      Passage passage3 = new Passage("new valid passage 3", "content");

      passage.addLink(new Link("new valid link", "new valid link"));
      passage2.addLink(new Link("new valid link", "new valid passage"));
      passage3.addLink(new Link("new valid link", "new valid passage"));
      Story story = new Story("new story", passage);
      story.addPassage(passage2);
      story.addPassage(passage3);

      assertTrue(story.removePassageUsingPassage(passage));
      assertEquals(2, story.getPassages().size());
      assertFalse(story.getPassages().contains(passage));
      assertTrue(story.getPassages().contains(passage2));
      assertTrue(story.getPassages().contains(passage3));
    }

    @Test
    public void remove_passage_that_is_not_referenced_by_other_passages() {
      Passage passage = new Passage("new valid passage", "content");
      Passage passage2 = new Passage("new valid passage 2", "content");
      Passage passage3 = new Passage("new valid passage 3", "content");

      passage.addLink(new Link("new valid link", "new valid link"));
      passage2.addLink(new Link("new valid link", "new valid passage 3"));
      passage3.addLink(new Link("new valid link", "new valid passage 2"));
      Story story = new Story("new story", passage);
      story.addPassage(passage2);
      story.addPassage(passage3);

      assertTrue(story.removePassageUsingPassage(passage2));
      assertEquals(2, story.getPassages().size());
      assertTrue(story.getPassages().contains(passage));
      assertFalse(story.getPassages().contains(passage2));
      assertTrue(story.getPassages().contains(passage3));
    }
  }


  @Nested
  public class GetBrokenLinksTest {

    @Test
    public void get_broken_links_returns_broken_links_if_there_are_broken_links() {
      Passage passage = new Passage("new valid passage", "content");
      Passage passage2 = new Passage("new valid passage 2", "content");
      Passage passage3 = new Passage("new valid passage 3", "content");

      // Broken link
      passage.addLink(new Link("new valid link", "new valid link"));

      passage2.addLink(new Link("new valid link", "new valid passage"));
      passage3.addLink(new Link("new valid link", "new valid passage"));
      Story story = new Story("new story", passage);
      story.addPassage(passage2);
      story.addPassage(passage3);

      assertEquals(story.getBrokenLinks().size(), 1);
    }

    @Test
    public void get_broken_links_returns_empty_list_if_there_are_no_broken_links() {
      Passage passage = new Passage("new valid passage", "content");
      Passage passage2 = new Passage("new valid passage 2", "content");
      Passage passage3 = new Passage("new valid passage 3", "content");

      passage.addLink(new Link("new valid link", "new valid passage 2"));
      passage2.addLink(new Link("new valid link", "new valid passage"));
      passage3.addLink(new Link("new valid link", "new valid passage"));
      Story story = new Story("new story", passage);
      story.addPassage(passage2);
      story.addPassage(passage3);

      assertTrue(story.getBrokenLinks().isEmpty());
    }

    @Test
    public void get_broken_links_returns_empty_list_if_there_are_no_links() {
      Passage passage = new Passage("new valid passage", "content");
      Passage passage2 = new Passage("new valid passage 2", "content");
      Passage passage3 = new Passage("new valid passage 3", "content");

      Story story = new Story("new story", passage);
      story.addPassage(passage2);
      story.addPassage(passage3);

      assertTrue(story.getBrokenLinks().isEmpty());
    }

    @Test
    public void get_broken_links_returns_empty_list_if_there_are_no_passages() {
      Passage passage = new Passage("new valid passage", "content");
      Story story = new Story("new story", passage);
      story.removePassageUsingPassage(passage);
      assertTrue(story.getBrokenLinks().isEmpty());
    }
  }

  @Nested
  public class GetPassagesMapTest {

    @Test
    public void getPassagesMap_returns_correct_map() {
      Passage passage = new Passage("new valid passage", "content");
      Passage passage2 = new Passage("new valid passage 2", "content");
      Story story = new Story("new story", passage);
      story.addPassage(passage2);

      Map<Link, Passage> passagesMap = story.getPassagesMap();
      assertNotNull(passagesMap);
      assertEquals(2, passagesMap.size());
      assertTrue(passagesMap.containsKey(new Link("new valid passage", "new valid passage")));
      assertTrue(passagesMap.containsKey(new Link("new valid passage 2", "new valid passage 2")));
    }
  }

  @Nested
  public class HashCodeTest {

    @Test
    public void hashCode_returns_same_value_for_equal_objects() {
      Passage passage = new Passage("new valid passage", "content");
      Story story1 = new Story("new story", passage);
      Story story2 = new Story("new story", passage);
      assertEquals(story1.hashCode(), story2.hashCode());
    }

    @Test
    public void hashCode_returns_different_values_for_different_objects() {
      Passage passage = new Passage("new valid passage", "content");
      Story story1 = new Story("new story", passage);
      Story story2 = new Story("new story 2", passage);
      assertNotEquals(story1.hashCode(), story2.hashCode());
    }
  }

  @Nested
  public class GetterMethodsTest {

    @Test
    public void get_title_returns_correct_title() {
      String title = "new story";
      Passage passage = new Passage("new valid passage", "content");
      Story story = new Story(title, passage);
      assertEquals(title, story.getTitle());
    }

    @Test
    public void get_passages_returns_correct_passages() {
      Passage openingPassage = new Passage("new valid passage", "content");
      Passage addedPassage = new Passage("new valid passage 2", "content");
      Story story = new Story("new story", openingPassage);
      story.addPassage(addedPassage);

      Collection<Passage> expectedPassages = new ArrayList<>();
      expectedPassages.add(openingPassage);
      expectedPassages.add(addedPassage);

      Collection<Passage> actualPassages = story.getPassages();
      assertTrue(expectedPassages.containsAll(actualPassages) && actualPassages.containsAll(
          expectedPassages));
    }


    @Test
    public void get_opening_passage_returns_correct_passage() {
      Passage passage = new Passage("new valid passage", "content");
      Story story = new Story("new story", passage);
      assertEquals(passage, story.getOpeningPassage());
    }
  }


}
