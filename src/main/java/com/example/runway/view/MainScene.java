package com.example.runway.view;

import com.example.runway.controller.Alerts;
import com.example.runway.controller.FileManager;
import com.example.runway.controller.InputManager;
import com.example.runway.controller.Validations;
import javafx.scene.Scene;


/**
 * The main scene abstract class. All scenes of the project will extend this class.
 */
public abstract class MainScene {

  // Main page, main pane and scene of the UI:
  protected final MainPage mainPage;
  protected MainPane root;
  protected Scene scene;


  // Objects of controller classes:
  protected Validations validations = new Validations();
  protected InputManager inputManager;
  protected FileManager fileManager;
  protected Alerts alerts = new Alerts();

  /**
   * Create the main scene
   * @param mainPage     the main page parameters of the UI
   */
  public MainScene (MainPage mainPage) {
    this.mainPage = mainPage;
  }

  /**
   * Abstract method for displaying components on a scene
   */
  public abstract void draw();

  /**
   * Set up the default scene
   * @return    the scene
   */
  public Scene setupScene() {
    Scene last = mainPage.getScene();

    scene = new Scene(root, last.getWidth(), last.getHeight());
    scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
    return scene;
  }



}
