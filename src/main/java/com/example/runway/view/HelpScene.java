package com.example.runway.view;

import com.example.runway.controller.FileManager;
import com.example.runway.controller.InputManager;
import java.io.InputStream;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * The help scene. Contains instructions explaining how to use the system.
 */
public class HelpScene extends MainScene {

  private final int helpNum;
  private StackPane instructions;

  /**
   * Create a help scene
   * @param mainPage     the main page parameters of the UI
   * @param inputManager controller for all user inputs
   * @param fileManager controller for all files
   * @param helpNum which help section to display
   */
  public HelpScene(MainPage mainPage, InputManager inputManager, FileManager fileManager, int helpNum) {
    super(mainPage);

    this.inputManager = inputManager;
    this.fileManager = fileManager;
    this.helpNum = helpNum;
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

    // Display appropriate instructions:
    switch (helpNum) {
      case 0 -> drawRedeclare();
      case 1 -> drawAdd();
      case 2 -> drawModify();
      case 3 -> drawImport();
      case 4 -> drawExport();
    }

    // Draw button:
    Button btnBack = new Button("Back");
    btnBack.getStyleClass().add("button-next");
    btnBack.setOnAction(this::back);
    StackPane.setAlignment(btnBack, Pos.BOTTOM_CENTER);
    StackPane.setMargin(btnBack, new Insets(0, 0, 5, 0));
    root.getChildren().add(btnBack);

  }

  /**
   * Handle when the Back button is clicked (loads previous scene)
   * @param event    action event
   */
  private void back(ActionEvent event) {
    mainPage.showRunway(inputManager, fileManager, inputManager.getCurrentRunway());
  }

  /**
   * Draw UI with runway re-declaration instructions
   */
  public void drawRedeclare() {
    // Page heading label:
    Label lblHeading = new Label("Runway Re-declaration (Instructions)");
    lblHeading.getStyleClass().add("heading");
    StackPane.setAlignment(lblHeading, Pos.TOP_CENTER);
    StackPane.setMargin(lblHeading, new Insets(10, 0, 0, 0));
    root.getChildren().add(lblHeading);

    // Instructions:
    InputStream stream = getClass().getResourceAsStream("/help_images/redeclare.jpg");
    Image image = new Image(stream);
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(900);
    imageView.setFitHeight(600);

    instructions = new StackPane(imageView);
    instructions.setMaxSize(900, 600);

    StackPane.setAlignment(instructions, Pos.BOTTOM_CENTER);
    StackPane.setMargin(instructions, new Insets(0, 0, 50, 0));
    root.getChildren().add(instructions);
  }

  /**
   * Draw UI with adding new data instructions
   */
  public void drawAdd() {
    // Page heading label:
    Label lblHeading = new Label("Adding new data (Instructions)");
    lblHeading.getStyleClass().add("heading");
    StackPane.setAlignment(lblHeading, Pos.TOP_CENTER);
    StackPane.setMargin(lblHeading, new Insets(10, 0, 0, 0));
    root.getChildren().add(lblHeading);

    // Instructions:
    InputStream stream = getClass().getResourceAsStream("/help_images/add.jpg");
    Image image = new Image(stream);
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(1110);
    imageView.setFitHeight(600);

    instructions = new StackPane(imageView);
    instructions.setMaxSize(1110, 600);

    StackPane.setAlignment(instructions, Pos.BOTTOM_CENTER);
    StackPane.setMargin(instructions, new Insets(0, 0, 50, 0));
    root.getChildren().add(instructions);
  }

  /**
   * Draw UI with modifying data instructions
   */
  public void drawModify() {
    // Page heading label:
    Label lblHeading = new Label("Modifying existing data (Instructions)");
    lblHeading.getStyleClass().add("heading");
    StackPane.setAlignment(lblHeading, Pos.TOP_CENTER);
    StackPane.setMargin(lblHeading, new Insets(10, 0, 0, 0));
    root.getChildren().add(lblHeading);

    // Instructions:
    InputStream stream = getClass().getResourceAsStream("/help_images/modify.jpg");
    Image image = new Image(stream);
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(970);
    imageView.setFitHeight(600);

    instructions = new StackPane(imageView);
    instructions.setMaxSize(970, 600);

    StackPane.setAlignment(instructions, Pos.BOTTOM_CENTER);
    StackPane.setMargin(instructions, new Insets(0, 0, 50, 0));
    root.getChildren().add(instructions);
  }

  /**
   * Draw UI with import instructions
   */
  public void drawImport() {
    // Page heading label:
    Label lblHeading = new Label("Importing files (Instructions)");
    lblHeading.getStyleClass().add("heading");
    StackPane.setAlignment(lblHeading, Pos.TOP_CENTER);
    StackPane.setMargin(lblHeading, new Insets(10, 0, 0, 0));
    root.getChildren().add(lblHeading);

    // Instructions:
    InputStream stream = getClass().getResourceAsStream("/help_images/import.jpg");
    Image image = new Image(stream);
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(1100);
    imageView.setFitHeight(600);

    instructions = new StackPane(imageView);
    instructions.setMaxSize(1100, 600);

    StackPane.setAlignment(instructions, Pos.BOTTOM_CENTER);
    StackPane.setMargin(instructions, new Insets(0, 0, 50, 0));
    root.getChildren().add(instructions);
  }

  /**
   * Draw UI with export instructions
   */
  public void drawExport() {
    // Page heading label:
    Label lblHeading = new Label("Exporting files (Instructions)");
    lblHeading.getStyleClass().add("heading");
    StackPane.setAlignment(lblHeading, Pos.TOP_CENTER);
    StackPane.setMargin(lblHeading, new Insets(10, 0, 0, 0));
    root.getChildren().add(lblHeading);

    // Instructions:
    InputStream stream = getClass().getResourceAsStream("/help_images/export.jpg");
    Image image = new Image(stream);
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(1080);
    imageView.setFitHeight(600);

    instructions = new StackPane(imageView);
    instructions.setMaxSize(1080, 600);

    StackPane.setAlignment(instructions, Pos.BOTTOM_CENTER);
    StackPane.setMargin(instructions, new Insets(0, 0, 50, 0));
    root.getChildren().add(instructions);
  }

}
