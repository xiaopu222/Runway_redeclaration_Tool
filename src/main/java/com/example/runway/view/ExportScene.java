package com.example.runway.view;

import com.example.runway.controller.FileManager;
import com.example.runway.controller.InputManager;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * The file export scene. Asks the user to choose a file where data will be saved.
 */
public class ExportScene extends MainScene {

  private Label lblFilename;
  private File file;
  private final int type;
  private final StackPane pane;

  /**
   * Create a new export scene
   * @param mainPage     the main page parameters of the UI
   * @param inputManager controller for all user inputs
   * @param fileManager controller for all files
   * @param type type of file that will be stored
   * @param pane pane that will be exported (only used in PNG and JPEG)
   */
  public ExportScene(MainPage mainPage, InputManager inputManager, FileManager fileManager, int type, StackPane pane) {
    super(mainPage);

    this.inputManager = inputManager;
    this.fileManager = fileManager;
    this.type = type;
    this.pane = pane;
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

    Label lblHeading = new Label("Export Data about Airport \"" + inputManager.getCurrentAirport().getName() + "\"");
    lblHeading.getStyleClass().add("heading");
    boxInputs.getChildren().add(lblHeading);

    HBox boxName = new HBox();
    boxName.setSpacing(5);
    boxName.setAlignment(Pos.CENTER);

    Label lblName = new Label("File name:");
    lblName.getStyleClass().add("input-output-labels");

    lblFilename = new Label("");
    lblFilename.getStyleClass().add("input-output-labels");

    Button btnChoose = new Button("Choose location");
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
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose a file");

    // Set appropriate file type:
    if (type == 0) {
      fileChooser.getExtensionFilters().add(new ExtensionFilter("XML files", "*.xml"));
    }
    else if (type == 1) {
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG files", "*.jpeg"));
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG files", "*.jpg"));
    }
    else if (type == 2) {
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files", "*.png"));
    }
    else if (type == 3) {
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files", "*.txt"));
    }

    // User chooses file:
    file = fileChooser.showSaveDialog(mainPage.getStage());
    if (file != null) {
      lblFilename.setText(file.getName());
    }
  }

  /**
   * Handle when the Next button is clicked (performs file export)
   * @param event    action event
   */
  private void next(ActionEvent event) {
    try {
      Text txtNotification;
      // Export file:
      if (type == 0) {
        fileManager.exportFile(file, inputManager.getCurrentAirport());
        txtNotification = new Text(
            "Data about \"" + inputManager.getCurrentAirport().getName() + "\" airport has been successfully exported as XML file.\n");
      } else if (type == 1) {
        fileManager.exportAsJPEG(file, pane);
        txtNotification = new Text(
            "\"" + inputManager.getCurrentAirport().getName() + "\" airport's runway view has been successfully exported as JPEG file.\n");
      } else if (type == 2) {
        fileManager.exportAsPNG(file, pane);
        txtNotification = new Text(
            "\"" + inputManager.getCurrentAirport().getName() + "\" airport's runway view has been successfully exported as PNG file.\n");
      } else {
        fileManager.exportAsTXT(file, inputManager.getCurrentAirport(),
            inputManager.getCurrentRunway(),
            inputManager.getCurrentRunway().getCurrentObstacle(), inputManager.getMethod());
        txtNotification = new Text(
            "Data about \"" + inputManager.getCurrentAirport().getName() + "\" airport's current situation has been successfully exported as TXT file.\n");
      }
      // Display notification:
      txtNotification.getStyleClass().add("green-notification");
      inputManager.addNotification(txtNotification);
    }
    // Appropriate error messages displayed in case of invalid user input:
    catch (Exception e) {
      alerts.alertError("An error occurred when exporting file", "Please enter a valid file path.");
    }
    // Return to main page:
    mainPage.showRunway(inputManager, fileManager, inputManager.getCurrentRunway());
  }

}
