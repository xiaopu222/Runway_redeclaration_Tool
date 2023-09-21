package com.example.runway.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

/**
 * Class used to draw a small compass. The compass is placed on the top-down runway view to make rotations more clear.
 * This is one of the extensions.
 */
public class CompasPane extends StackPane {

  private final StackPane rotatablePane;

  /**
   * Create a new compass pane
   */
  public CompasPane() {
    super();

    setMaxSize(50, 50);

    // Set up the pane that will rotate when view is rotated:
    rotatablePane = new StackPane();
    rotatablePane.setMaxSize(50, 50);

    draw();
  }

  /**
   * Draw the UI components
   */
  public void draw() {
    // Draw compass UI:
    Circle outsideCircle = new Circle();
    outsideCircle.setRadius(24);
    outsideCircle.getStyleClass().add("border-white");

    Label lblN = new Label("N");
    lblN.getStyleClass().add("compas-letter");
    StackPane.setAlignment(lblN, Pos.TOP_CENTER);
    StackPane.setMargin(lblN, new Insets(-17, 0, 0,0));

    Label lblS = new Label("S");
    lblS.getStyleClass().add("compas-letter");
    StackPane.setAlignment(lblS, Pos.BOTTOM_CENTER);
    StackPane.setMargin(lblS, new Insets(0, 0, -17,0));

    Label lblE = new Label("E");
    lblE.getStyleClass().add("compas-letter");
    StackPane.setAlignment(lblE, Pos.CENTER_RIGHT);
    StackPane.setMargin(lblE, new Insets(0, -10, 0,0));

    Label lblW = new Label("W");
    lblW.getStyleClass().add("compas-letter");
    StackPane.setAlignment(lblW, Pos.CENTER_LEFT);
    StackPane.setMargin(lblW, new Insets(0, 0, 0, -15));

    Circle insideCircle = new Circle();
    insideCircle.setRadius(2);
    insideCircle.getStyleClass().add("border");

    Polygon upArrow = new Polygon(2, 0,
        0, 18,
        4, 18);
    upArrow.getStyleClass().add("border-pink");
    StackPane.setAlignment(upArrow, Pos.TOP_CENTER);

    Polygon downArrow = new Polygon(0, 0,
        4, 0,
        2, 18);
    downArrow.getStyleClass().add("border-pink");
    StackPane.setAlignment(downArrow, Pos.BOTTOM_CENTER);

    rotatablePane.getChildren().addAll(outsideCircle, insideCircle, upArrow, downArrow);
    getChildren().addAll(rotatablePane, lblN, lblS, lblE, lblW);
  }

  /**
   * Getter for rotatable pane
   * @return rotatable pane
   */
  public StackPane getRotatablePane() {
    return rotatablePane;
  }

}
