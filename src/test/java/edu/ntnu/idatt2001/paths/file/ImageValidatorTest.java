package edu.ntnu.idatt2001.paths.file;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.ntnu.idatt2001.paths.model.exceptions.ImageValidationException;
import edu.ntnu.idatt2001.paths.model.validations.ImageValidator;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ImageValidatorTest {

  private File validImage;
  private File invalidExtensionImage;
  private File nonExistentFile;
  private String nonExistentImagePath;

  @BeforeEach
  public void setUp() {
    validImage = new File("./testImages/testImage6.jpg");
    invalidExtensionImage = new File("./testImages/image.txt");
    nonExistentFile = new File("./testImages/nonExistentImage.jpg");
    nonExistentImagePath = "./testImages/nonExistentImage.jpg";
  }

  @Test
  public void validateImageFile_withValidImage_noExceptionThrown() {
    assertDoesNotThrow(() -> ImageValidator.validateImage(validImage));
  }

  @Test
  public void validateImageFile_withInvalidExtension_throwsImageValidationException() {
    assertThrows(ImageValidationException.class,
        () -> ImageValidator.validateImage(invalidExtensionImage));
  }

  @Test
  public void validateImageFile_withNonExistentImage_throwsImageValidationException() {
    assertThrows(ImageValidationException.class,
        () -> ImageValidator.validateImage(nonExistentFile));
  }


  @Test
  public void validateImagePath_withNonExistentImage_throwsImageValidationException() {
    assertThrows(ImageValidationException.class,
        () -> ImageValidator.validateImage(nonExistentImagePath));
  }
}

