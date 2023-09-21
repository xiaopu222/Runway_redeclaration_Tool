package com.example.runway.view;

import com.example.runway.controller.InputManager;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * The notifications pane. Keeps a record of actions performed by the user inside the app.
 */
public class Notifications extends ScrollPane {

  private final TextFlow textFlow;
  private final InputManager inputManager;

  /**
   * Create a new notifications pane
   * @param inputManager controller for all user inputs
   */
  public Notifications(InputManager inputManager) {
    super();

    this.inputManager = inputManager;

    setPrefSize(700, 100);

    // Set up container for notifications:
    textFlow = new TextFlow();
    textFlow.setMaxWidth(670);

    setContent(textFlow);
  }

  /**
   * Display a red notification
   */
  public void addRedNotification(String notification) {
    Text txtNotification = new Text(notification + "\n");
    txtNotification.getStyleClass().add("red-notification");
    textFlow.getChildren().add(txtNotification);
    inputManager.addNotification(txtNotification);
  }

  /**
   * Display a green notification
   */
  public void addGreenNotification(String notification) {
    Text txtNotification = new Text(notification + "\n");
    txtNotification.getStyleClass().add("green-notification");
    textFlow.getChildren().add(txtNotification);
    inputManager.addNotification(txtNotification);
  }

  /**
   * Display a black notification
   */
  public void addBlackNotification(String notification) {
    Text txtNotification = new Text(notification + "\n");
    txtNotification.getStyleClass().add("black-notification");
    textFlow.getChildren().add(txtNotification);
    inputManager.addNotification(txtNotification);
  }

  /**
   * Add a new notification
   */
  public void addNotification(Text txtNotification) {
    textFlow.getChildren().add(txtNotification);
  }

}
