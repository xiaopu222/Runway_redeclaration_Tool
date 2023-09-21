package com.example.runway.view;

import com.example.runway.controller.Alerts;
import com.example.runway.controller.FileManager;
import com.example.runway.controller.InputManager;
import com.example.runway.model.Airport;
import com.example.runway.model.Runway;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


/**
 * The main page class. Sets parameters for the UI and manipulates scenes.
 */
public class MainPage {

  // Size of the UI:
  private final int width = 1400;
  private final int height = 700;

  // Main stage and scene of the UI:
  private final Stage stage;
  private Scene scene;

  private final InputManager inputManager;
  private final FileManager fileManager;

  /**
   * Create the main page
   * @param stage     the stage of the UI
   */
  public MainPage(Stage stage) {
    // Set up stage:
    this.stage = stage;
    this.stage.setTitle("Runway Re-declaration Tool");
    this.stage.setMinWidth(width);
    this.stage.setMinHeight(height);

    // Set up scene:
    this.scene = new Scene(new Pane(), width, height);

    // Load the runway scene:
    inputManager = new InputManager();
    fileManager = new FileManager();

    // Load data that has been previously stored:
    List<Airport> airports = fileManager.fetchFiles();
    for (Airport airport : airports) {
      inputManager.addAirport(airport);
    }

    // Display main page:
    showRunway(inputManager, fileManager);
  }

  /**
   * Getter for the scene of UI
   * @return    the scene
   */
  public Scene getScene() {
    return scene;
  }

  /**
   * Getter for primary stage
   * @return    the stage
   */
  public Stage getStage() {
    return stage;
  }

  /**
   * Load the scene for adding a new airport
   * @param inputManager controller for all user inputs
   * @param fileManager controller for all files
   */
  public void showAddAirport(InputManager inputManager, FileManager fileManager) {
    showScene(new AddAirportScene(this, inputManager, fileManager));
  }

  /**
   * Load the scene for adding a new runway
   * @param inputManager controller for all user inputs
   * @param fileManager controller for all files
   */
  public void showAddRunway(InputManager inputManager, FileManager fileManager) {
    showScene(new AddRunwayScene(this, inputManager, fileManager));
  }

  /**
   * Load the scene for adding a new obstacle
   * @param inputManager controller for all user inputs
   * @param fileManager controller for all files
   */
  public void showAddObstacle(InputManager inputManager, FileManager fileManager) {
    showScene(new AddObstacleScene(this, inputManager, fileManager));
  }


  /**
   * Load the runway scene
   * @param inputManager controller for all user inputs
   * @param fileManager controller for all files
   */
  public void showRunway(InputManager inputManager, FileManager fileManager) {
    showScene(new RunwayScene(this, inputManager, fileManager));
  }

  /**
   * Load the runway scene
   * @param inputManager controller for all user inputs
   * @param fileManager controller for all files
   * @param runway runway to display
   */
  public void showRunway(InputManager inputManager, FileManager fileManager, Runway runway) {
    showScene(new RunwayScene(this, inputManager, fileManager, runway));
  }

  /**
   * Load the scene for importing data
   * @param inputManager controller for all user inputs
   * @param fileManager controller for all files
   */
  public void showImport(InputManager inputManager, FileManager fileManager) {
    showScene(new ImportScene(this, inputManager, fileManager));
  }

  /**
   * Load the scene for exporting data
   * @param inputManager controller for all user inputs
   * @param fileManager controller for all files
   */
  public void showExport(InputManager inputManager, FileManager fileManager, int type, StackPane pane) {
    if (inputManager.getCurrentAirport() != null) {
      showScene(new ExportScene(this, inputManager, fileManager, type, pane));
    }
    else {
      Alerts alerts = new Alerts();
      alerts.alertError("No airport chosen", "Please choose an airport first");
    }
  }

  /**
   * Load the scene for modifying data
   * @param inputManager controller for all user inputs
   * @param fileManager controller for all files
   * @param num    airport=1, runway=2, obstacle=3
   */
  public void showModify(InputManager inputManager, FileManager fileManager, int num) {
    showScene(new ModifyScene(this, inputManager, fileManager, num));
  }

  /**
   * Load the scene for modifying data
   * @param inputManager controller for all user inputs
   * @param fileManager controller for all files
   * @param num    re-declaration = 0, adding data = 1, modifying data = 2, imports = 3, exports = 4
   */
  public void showHelp(InputManager inputManager, FileManager fileManager, int num) {
    showScene(new HelpScene(this, inputManager, fileManager, num));
  }

  /**
   * Load a given scene
   * @param newScene    the scene to load
   */
  public void showScene(MainScene newScene) {
    newScene.draw();

    scene = newScene.setupScene();
    stage.setScene(scene);
  }

  /**
   * Getter for input manager
   * @return input manager
   */
  public InputManager getInputManager() {
    return inputManager;
  }

  /**
   * Getter for file manager
   * @return file manager
   */
  public FileManager getFileManager() {
    return fileManager;
  }

}
