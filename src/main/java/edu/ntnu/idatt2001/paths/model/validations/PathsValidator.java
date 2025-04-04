package edu.ntnu.idatt2001.paths.model.validations;

import edu.ntnu.idatt2001.paths.model.exceptions.PathsValidationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PathsValidator {

  public static void validatePathsFile(File pathsFile) throws PathsValidationException {
    // Check if the file is null
    if (pathsFile == null) {
      throw new PathsValidationException("File cannot be null");
    }

    // Check if the file exists
    if (!pathsFile.exists()) {
      throw new PathsValidationException("File does not exist");
    }

    // Check if the file is a file (not a directory)
    if (!pathsFile.isFile()) {
      throw new PathsValidationException("Path does not point to a file");
    }

    // Check if the file has the correct extension
    if (!pathsFile.getName().endsWith(".paths")) {
      throw new PathsValidationException("File is not a .paths file");
    }

    // Check if the file is empty
    try {
      if (Files.readString(pathsFile.toPath()).isBlank()) {
        throw new PathsValidationException("File is empty");
      }
    } catch (IOException e) {
      throw new PathsValidationException("Error reading file");
    }
  }
}
