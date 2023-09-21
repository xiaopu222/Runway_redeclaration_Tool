package com.example.runway;

import com.example.runway.controller.FileManager;
import com.example.runway.controller.InputManager;
import com.example.runway.model.Airport;
import com.example.runway.view.MainPage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * The Launcher class for starting the JavaFX program.
 */
public class App  extends Application {

  private MainPage mainPage;

  /**
   * Method called by JavaFX with the stage as a parameter.
   * Starts the program by calling the MainPage class
   * @param stage    the default stage
   */
  @Override
  public void start(Stage stage) throws Exception {
    var folder = Path.of(Path.of("").toAbsolutePath() + "/storage_files/");
    if (!Files.isDirectory(folder)) {
      try {
        Files.createDirectory(folder);
      } catch (IOException e) {
        System.out.println("Unexpected error");
      }
    }
    mainPage = new MainPage(stage);
    
    stage.setResizable(true);
    stage.show();
  }

  /**
   * Method called by JavaFX with the stage as a parameter.
   * Called when user exits the app.
   * Saves all data into files to use them next time when the app is launched.
   */
  @Override
  public void stop(){
    InputManager inputManager = mainPage.getInputManager();
    FileManager fileManager = mainPage.getFileManager();

    for (Airport airport : inputManager.getAirports()) {
      airport.setDefaultRunways();
      fileManager.saveAirport(airport);
    }
  }

  /**
   * Start the program
   * @param args    command-line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

}
