package com.example.runway.view;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;import javafx.scene.shape.Rectangle;

/**
 * The obstacle pane. Draws an obstacle on the runway view.
 */
public class ObstaclePane extends StackPane {

  /**
   * Create a new obstacle pane
   */
  public ObstaclePane(boolean topDown) {
    super();

    // Set up the pane:
    setMaxSize(25, 25);

    // Draw appropriate UI:
    if (topDown) {
      drawTopDown();
    }
    else {
      drawSideOn();
    }
  }

  /**
   * Draw the top-down UI components
   */
  public void drawTopDown() {
    Line line1 = new Line(0, 0, 25, 25);
    line1.getStyleClass().add("obstacle-lines");

    Line line2 = new Line(25, 0, 0, 25);
    line2.getStyleClass().add("obstacle-lines");

    this.getChildren().addAll(line1, line2);
  }

  /**
   * Draw the side-on UI components
   */
  public void drawSideOn() {
    Rectangle rectangle = new Rectangle(25, 25);
    rectangle.getStyleClass().add("obstacle-rectangle");

    this.getChildren().add(rectangle);
  }

}
