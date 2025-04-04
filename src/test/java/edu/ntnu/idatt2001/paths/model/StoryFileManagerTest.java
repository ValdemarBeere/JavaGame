package edu.ntnu.idatt2001.paths.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idatt2001.paths.model.actions.inventory.InventoryAction;
import edu.ntnu.idatt2001.paths.model.exceptions.PassageValidationException;
import edu.ntnu.idatt2001.paths.model.exceptions.StoryValidationException;
import java.io.InputStream;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StoryFileManagerTest {

  @Test
  void test_load_story_with_circular_link_throws() {
    String selectedStoryFilePath = "/testStories/storyWithCircularLink.paths";
    InputStream inputStream = getClass().getResourceAsStream(selectedStoryFilePath);
    StoryFileManager fileManager = new StoryFileManager();
    assertThrows(PassageValidationException.class,
        () -> fileManager.loadStoryFromFile(inputStream));
  }

  @Test
  void test_load_story_with_duplicate_passage_throws() {
    String selectedStoryFilePath = "/testStories/storyWithDuplicatePassages.paths";
    InputStream inputStream = getClass().getResourceAsStream(selectedStoryFilePath);
    StoryFileManager fileManager = new StoryFileManager();
    assertThrows(StoryValidationException.class, () -> fileManager.loadStoryFromFile(inputStream));
  }

  @Test
  void test_load_story_with_non_existent_link_throws() {
    String selectedStoryFilePath = "/testStories/storyWithNonExistentLink.paths";
    InputStream inputStream = getClass().getResourceAsStream(selectedStoryFilePath);
    StoryFileManager fileManager = new StoryFileManager();
    assertThrows(StoryValidationException.class, () -> fileManager.loadStoryFromFile(inputStream));
  }

  @Test
  void test_load_story_with_no_links_throws() {
    String selectedStoryFilePath = "/testStories/storyWithNoLinks.paths";
    InputStream inputStream = getClass().getResourceAsStream(selectedStoryFilePath);
    StoryFileManager fileManager = new StoryFileManager();
    assertThrows(PassageValidationException.class,
        () -> fileManager.loadStoryFromFile(inputStream));
  }

  @Nested
  class LoadStoryFromFileTest {

    @Test
    void testLoadStoryFromFile() {
      String selectedStoryFilePath = "/testStories/storyWithCorrectFormat.paths";
      InputStream inputStream = getClass().getResourceAsStream(selectedStoryFilePath);

      StoryFileManager fileManager = new StoryFileManager();
      Story story = fileManager.loadStoryFromFile(inputStream);

      assertEquals("Mysterious Forest", story.getTitle());
      assertEquals(8, story.getPassagesMap().size());
      assertNotNull(story.getOpeningPassage());
      assertNotNull(story.getOpeningPassage().getBackgroundImage());
      assertNotNull(story.getOpeningPassage().getContentImage());
      assertEquals("Entrance", story.getOpeningPassage().getTitle());
      List<Link> links = story.getOpeningPassage().getLinks();
      assertEquals(2, links.size());
      assertEquals("Take the left path", links.get(0).getText());

      for (Passage passage : story.getPassages()) {
        assertTrue(passage.hasLinks());
        for (Link link : passage.getLinks()) {
          if (link.linkHasItemRequirement()) {
            assertNotNull(link.getRequiredItem());
          }
        }
      }
      assertTrue(links.get(0).getActions().get(0) instanceof InventoryAction);
    }

    @Test
    void testLoadStoryFromFileWithPlaneStory() {
      String selectedStoryFilePath = "/testStories/storyWithoutImagesActionsAndRequiredItems.paths";
      InputStream inputStream = getClass().getResourceAsStream(selectedStoryFilePath);

      StoryFileManager fileManager = new StoryFileManager();
      Story story = fileManager.loadStoryFromFile(inputStream);

      assertEquals("Mysterious Forest", story.getTitle());
      assertEquals(8, story.getPassagesMap().size());
      assertNotNull(story.getOpeningPassage());
      assertEquals("Entrance", story.getOpeningPassage().getTitle());
      List<Link> links = story.getOpeningPassage().getLinks();
      assertEquals(2, links.size());
      assertEquals("Take the left path", links.get(0).getText());
      for (Passage passage : story.getPassages()) {
        assertTrue(passage.hasLinks());
        for (Link link : passage.getLinks()) {
          assertNotNull(link.getReference());
          assertNotNull(link.getText());
        }
      }
    }
  }

}
