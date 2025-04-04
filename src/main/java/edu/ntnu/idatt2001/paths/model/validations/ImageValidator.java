package edu.ntnu.idatt2001.paths.model.validations;

import edu.ntnu.idatt2001.paths.model.exceptions.ImageValidationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import javafx.scene.image.Image;

public class ImageValidator {

  private static final List<String> VALID_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif",
      "bmp");
  private static final long MAX_FILE_SIZE = 1024 * 1024 * 5; // 5 MB

  public static void validateImage(File imageFile) throws ImageValidationException {
    if (imageFile == null) {
      throw new ImageValidationException("Image file cannot be null");
    }

    if (!imageFile.exists()) {
      throw new ImageValidationException("Image file does not exist");
    }

    if (!isValidImageExtension(imageFile.getName())) {
      throw new ImageValidationException("Invalid image file extension");
    }

    if (!isValidFileSize(imageFile)) {
      throw new ImageValidationException("Image file size exceeds the limit");
    }
  }

  private static boolean isValidImageExtension(String fileName) {
    String fileExtension = getFileExtension(fileName);
    return VALID_EXTENSIONS.contains(fileExtension.toLowerCase());
  }

  private static String getFileExtension(String fileName) {
    int lastIndexOfDot = fileName.lastIndexOf(".");
    if (lastIndexOfDot == -1) {
      return ""; // Return empty string if there's no file extension
    }
    return fileName.substring(lastIndexOfDot + 1);
  }

  private static boolean isValidFileSize(File imageFile) {
    return imageFile.length() <= MAX_FILE_SIZE;
  }

  public static void validateImage(String imagePath) throws ImageValidationException {
    InputStream imageStream = ImageValidator.class.getResourceAsStream(imagePath);
    if (imageStream == null) {
      // The image is not found in the classpath resources. Check in the uploaded images' directory.
      Path imageFilePath = Paths.get("./uploadedImages", imagePath);
      File imageFile = imageFilePath.toFile();
      if (imageFile.exists()) {
        try {
          imageStream = new FileInputStream(imageFile);
        } catch (FileNotFoundException e) {
          throw new ImageValidationException("Image not found: " + imagePath);
        }
      } else {
        throw new ImageValidationException("Image not found: " + imagePath);
      }
    }

    Image image = new Image(imageStream);
    if (image.isError()) {
      throw new ImageValidationException("Invalid image: " + imagePath);
    }
  }


}
