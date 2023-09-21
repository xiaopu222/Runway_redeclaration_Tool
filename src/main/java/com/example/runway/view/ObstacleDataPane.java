package com.example.runway.view;

import com.example.runway.model.Obstacle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * The obstacle data pane. Displays obstacle data.
 */
public class ObstacleDataPane extends StackPane {

  /**
   * Create a new obstacle data pane
   */
  public ObstacleDataPane() {
    super();

    // Set up the pane:
    setMaxSize(300, 140);
    setMinSize(300, 140);
    getStyleClass().add("obstacle-pane");

    draw(null);
  }

  /**
   * Draw the UI components
   */
  public void draw(Obstacle obstacle) {
    getChildren().clear();

    // Display obstacle data:
    VBox boxData = new VBox();
    boxData.setAlignment(Pos.TOP_CENTER);
    boxData.setSpacing(10);
    StackPane.setMargin(boxData, new Insets(10, 0, 0, 0));

    Label lblObstacle = new Label("Obstacle:");
    lblObstacle.getStyleClass().add("input-output-labels");
    Label lblHeight = new Label("");
    Label lblDistanceCentre = new Label("");
    Label lblDistanceThreshold = new Label("");

    if (obstacle != null) {
      lblHeight.setText("Height = " + obstacle.getHeight() + "m\t   Length = " + obstacle.getLength() + " m");
      lblDistanceCentre.setText("Distance from centre line = " + obstacle.getDistanceCentre() + " m");
      lblDistanceThreshold.setText("Distance from threshold = " + obstacle.getDistanceFromThreshold() + " m");
    }

    boxData.getChildren().addAll(lblObstacle, lblHeight, lblDistanceCentre, lblDistanceThreshold);
    getChildren().add(boxData);
  }

}
