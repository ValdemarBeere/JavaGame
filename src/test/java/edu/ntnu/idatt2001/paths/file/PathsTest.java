package edu.ntnu.idatt2001.paths.file;

import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.ntnu.idatt2001.paths.model.exceptions.PathsValidationException;
import edu.ntnu.idatt2001.paths.model.validations.PathsValidator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PathsTest {

  private File nullFile;
  private File nonExistentFile;
  private File directory;
  private File invalidExtensionFile;
  private File emptyFile;

  @BeforeEach
  public void setUp() throws IOException {
    nullFile = null;
    nonExistentFile = new File("./nonExistent.paths");
    directory = new File("./testDir");
    invalidExtensionFile = new File("./testDir/invalidExtension.txt");
    emptyFile = new File("./testDir/empty.paths");
    FileWriter emptyFileWriter = new FileWriter(emptyFile);
    emptyFileWriter.write("");
    emptyFileWriter.close();
  }

  @Test
  public void validatePathsFile_withNullFile_throwsPathsValidationException() {
    assertThrows(PathsValidationException.class, () -> PathsValidator.validatePathsFile(nullFile));
  }

  @Test
  public void validatePathsFile_withNonExistentFile_throwsPathsValidationException() {
    assertThrows(PathsValidationException.class,
        () -> PathsValidator.validatePathsFile(nonExistentFile));
  }

  @Test
  public void validatePathsFile_withDirectory_throwsPathsValidationException() {
    assertThrows(PathsValidationException.class, () -> PathsValidator.validatePathsFile(directory));
  }

  @Test
  public void validatePathsFile_withInvalidExtension_throwsPathsValidationException() {
    assertThrows(PathsValidationException.class,
        () -> PathsValidator.validatePathsFile(invalidExtensionFile));
  }

  @Test
  public void validatePathsFile_withEmptyFile_throwsPathsValidationException() {
    assertThrows(PathsValidationException.class, () -> PathsValidator.validatePathsFile(emptyFile));
  }
}
