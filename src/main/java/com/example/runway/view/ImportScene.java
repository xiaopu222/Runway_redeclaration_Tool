package com.example.runway.view;

import com.example.runway.controller.FileManager;
import com.example.runway.controller.InputManager;
import com.example.runway.model.Airport;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * The import scene. Asks the user to choose a file for importing to the system.
 */
public class ImportScene extends MainScene {

  private File file;
  private Label lblFilename;

  /**
   * Create a new import scene
   * @param mainPage     the main page parameters of the UI
   * @param inputManager controller for all user inputs
   * @param fileManager controller for all files
   */
  public ImportScene(MainPage mainPage, InputManager inputManager, FileManager fileManager) {
    super(mainPage);

    this.inputManager = inputManager;
    this.fileManager = fileManager;
  }

  /**
   * Draw the UI components
   */
  @Override
  public void draw() {
    // Set up the main pane
    root = new MainPane();
    root.getStylesheets().add(getClass().getResource(inputManager.getStyle()).toExternalForm());
    root.getStyleClass().add("menu-background");

    // Draw UI for user input:
    VBox boxInputs = new VBox();
    boxInputs.setAlignment(Pos.CENTER);
    boxInputs.setSpacing(40);

    Label lblHeading = new Label("Import a New Airport");
    lblHeading.getStyleClass().add("heading");
    boxInputs.getChildren().add(lblHeading);

    HBox boxName = new HBox();
    boxName.setSpacing(5);
    boxName.setAlignment(Pos.CENTER);

    Label lblName = new Label("File name:");
    lblName.getStyleClass().add("input-output-labels");
    lblFilename = new Label("");
    lblFilename.getStyleClass().add("input-output-labels");

    Button btnChoose = new Button("Choose a file");
    btnChoose.setOnAction(this::choose);

    boxName.getChildren().addAll(lblName, lblFilename, btnChoose);

    // Draw buttons:
    HBox boxButtons = new HBox();
    boxButtons.setAlignment(Pos.CENTER);
    boxButtons.setSpacing(70);

    Button btnNext = new Button("Next");
    btnNext.getStyleClass().add("button-next");
    btnNext.setOnAction(this::next);

    Button btnBack = new Button("Back");
    btnBack.getStyleClass().add("button-next");
    btnBack.setOnAction(this::back);

    boxButtons.getChildren().addAll(btnBack, btnNext);

    boxInputs.getChildren().addAll(boxName, boxButtons);
    root.getChildren().add(boxInputs);
  }

  /**
   * Handle when the Back button is clicked (loads previous scene)
   * @param event    action event
   */
  private void back(ActionEvent event) {
    mainPage.showRunway(inputManager, fileManager, inputManager.getCurrentRunway());
  }

  /**
   * Handle when the Choose file button is clicked. Displays File viewer.
   * @param event    action event
   */
  public void choose(ActionEvent event) {
    // Set up file type:
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose a file");
    fileChooser.getExtensionFilters().add(new ExtensionFilter("XML files", "*.xml"));

    // User chooses file:
    file = fileChooser.showOpenDialog(mainPage.getStage());
    if (file != null) {
      if (validations.isValidXMLFile(file)) {
        lblFilename.setText(file.getName());
      }
      // Error message displayed if invalid file was chosen:
      else {
        alerts.alertError("Invalid XML file", "Please import another file.");
      }
    }
  }

  /**
   * Handle when the Next button is clicked (performs file import)
   * @param event    action event
   */
  private void next(ActionEvent event) {
    if (validations.isValidXMLFile(file)) {
      try {
        // Create a new airport object:
        Airport airport = fileManager.importFile(file.getAbsolutePath());

        if (validations.isValidAirport(airport, inputManager)) {
          inputManager.addAirport(airport);

          // Display notification:
          Text txtNotification = new Text(
              "Data about \"" + airport.getName()
                  + "\" has been successfully imported from file.\n");
          txtNotification.getStyleClass().add("green-notification");
          inputManager.addNotification(txtNotification);

          // Returrn to main page:
          mainPage.showRunway(inputManager, fileManager);
        }
        // Appropriate error messages displayed in case of invalid user input:
        else {
          alerts.alertError("Invalid XML file", "Please import another file.");
        }
      }
      catch (Exception e) {
        alerts.alertError("Invalid XML file", "Please import another file.");
      }
    }
    else {
      alerts.alertError("Invalid XML file", "Please import another file.");
    }
  }

}
