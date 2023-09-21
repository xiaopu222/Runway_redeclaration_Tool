package com.example.runway.view;

import com.example.runway.model.Runway;
import java.text.DecimalFormat;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * The runway data pane. Displays runway data.
 */
public class RunwayDataPane extends StackPane {

  private final HBox boxOriginalRedeclared = new HBox();
  private final VBox boxData = new VBox();
  private final VBox boxDefaults = new VBox();
  Label lblResultRunwayNumber = new Label("");
  private final DecimalFormat df = new DecimalFormat("0.0");

  /**
   * Create a new runway data pane
   */
  public RunwayDataPane() {
    super();

    // Set up the pane:
    setMaxSize(300, 353);
    setMinSize(300, 353);
    getStyleClass().add("obstacle-pane");

    // Display runway data:
    boxOriginalRedeclared.setAlignment(Pos.CENTER);

    boxData.setAlignment(Pos.TOP_CENTER);
    boxData.setSpacing(10);
    StackPane.setMargin(boxData, new Insets(10, 0, 0, 0));
    getChildren().add(boxData);

    HBox boxRunway = new HBox();
    boxRunway.setAlignment(Pos.CENTER);
    boxRunway.setSpacing(2);

    Label lblRunwayNumber = new Label("Runway:");
    lblRunwayNumber.getStyleClass().add("input-output-labels");
    lblResultRunwayNumber.getStyleClass().add("results");
    lblResultRunwayNumber.setAlignment(Pos.CENTER);

    boxDefaults.setAlignment(Pos.TOP_CENTER);
    boxDefaults.setSpacing(5);

    boxRunway.getChildren().addAll(lblRunwayNumber, lblResultRunwayNumber);
    boxData.getChildren().add(boxRunway);
    boxData.getChildren().add(boxDefaults);
  }

  // Remove previous components:
  public void hide() {
    boxDefaults.setVisible(false);
    boxData.getChildren().remove(boxOriginalRedeclared);
    lblResultRunwayNumber.setText("");
  }

  /**
   * Draw the UI components
   */
  public void draw(boolean reDeclared, Runway runway) {
    getChildren().clear();
    boxData.getChildren().remove(boxOriginalRedeclared);
    boxOriginalRedeclared.getChildren().clear();
    boxDefaults.getChildren().clear();
    boxDefaults.setVisible(true);

    lblResultRunwayNumber.setText(runway.getRunwayNumber());

    HBox boxResaSlopeBlastDisplaced = new HBox();
    boxResaSlopeBlastDisplaced.setAlignment(Pos.CENTER);
    boxResaSlopeBlastDisplaced.setSpacing(10);

    VBox boxResaSlope = new VBox();
    boxResaSlope.setSpacing(5);
    boxResaSlope.setAlignment(Pos.CENTER_LEFT);
    VBox boxBlastDisplaced = new VBox();
    boxBlastDisplaced.setSpacing(5);
    boxBlastDisplaced.setAlignment(Pos.CENTER_RIGHT);

    Label lblResa = new Label("RESA = " + (int) runway.getResa() + " m");
    Label lblSlope = new Label("Slope = 1:" + (int) runway.getSlopeValue());
    Label lblBlastAllowance = new Label("Blast Allowance = " + (int) runway.getBlastAllowance() + " m");
    Label lblDisplaced = new Label( "Displaced threshold = " + (int) runway.getDisplacedThreshold() + " m");

    boxResaSlope.getChildren().addAll(lblResa, lblSlope);
    boxBlastDisplaced.getChildren().addAll(lblBlastAllowance, lblDisplaced);
    boxResaSlopeBlastDisplaced.getChildren().addAll(boxResaSlope, boxBlastDisplaced);

    boxDefaults.getChildren().add(boxResaSlopeBlastDisplaced);

    VBox boxRowNames = new VBox();
    boxRowNames.setAlignment(Pos.TOP_CENTER);
    boxRowNames.setSpacing(10);

    Label lblParameter = new Label("");
    lblParameter.getStyleClass().add("input-output-labels");
    Label lblTora = new Label("TORA:");
    lblTora.getStyleClass().add("input-output-labels-smaller");
    Label lblToda = new Label("TODA:");
    lblToda.getStyleClass().add("input-output-labels-smaller");
    Label lblAsda = new Label("ASDA:");
    lblAsda.getStyleClass().add("input-output-labels-smaller");
    Label lblLda = new Label("LDA:");
    lblLda.getStyleClass().add("input-output-labels-smaller");
    Label lblStopWay = new Label("Stop Way:");
    lblStopWay.getStyleClass().add("input-output-labels-smaller");
    Label lblClearWay = new Label("Clear Way:");
    lblClearWay.getStyleClass().add("input-output-labels-smaller");

    boxRowNames.getChildren().addAll(lblParameter, lblTora, lblToda, lblAsda, lblLda, lblStopWay, lblClearWay);

    VBox boxOriginal = new VBox();
    boxOriginal.setAlignment(Pos.TOP_CENTER);
    boxOriginal.setSpacing(10);

    Label lblOriginal = new Label("Original");
    lblOriginal.getStyleClass().add("input-output-labels-smaller");
    boxOriginal.getChildren().add(lblOriginal);

    Label resultDefaultTora = new Label(df.format(runway.getDefaultTora()) + " m");
    resultDefaultTora.getStyleClass().add("results-smaller");
    Label resultDefaultToda = new Label(df.format(runway.getDefaultToda()) + " m");
    resultDefaultToda.getStyleClass().add("results-smaller");
    Label resultDefaultAsda = new Label(df.format(runway.getDefaultAsda()) + " m");
    resultDefaultAsda.getStyleClass().add("results-smaller");
    Label resultDefaultLda = new Label(df.format(runway.getDefaultLda()) + " m");
    resultDefaultLda.getStyleClass().add("results-smaller");
    Label resultDefaultStopWay = new Label(df.format(runway.getDefaultStopWay()) + " m");
    resultDefaultStopWay.getStyleClass().add("results-smaller");
    Label resultDefaultClearWay = new Label(df.format(runway.getDefaultClearWay()) + " m");
    resultDefaultClearWay.getStyleClass().add("results-smaller");

    boxOriginal.getChildren().addAll(resultDefaultTora, resultDefaultToda, resultDefaultAsda, resultDefaultLda, resultDefaultStopWay, resultDefaultClearWay);
    boxOriginalRedeclared.getChildren().addAll(boxRowNames, boxOriginal);

    if (reDeclared) {
      drawRedeclared(runway);
    }

    boxData.getChildren().add(boxOriginalRedeclared);
    getChildren().add(boxData);
  }

  /**
   * Draw the UI components for re-declared parameters:
   */
  public void drawRedeclared(Runway runway) {
    Label lblNewStripEnd = new Label("New strip end = " + (int) runway.getNewStripEnd() + " m");
    Label lblAlsTocs = new Label("ALS/TOCS = " + df.format(runway.getAlsTocs()) + " m");
    boxDefaults.getChildren().addAll(lblNewStripEnd, lblAlsTocs);

    VBox boxRedeclared = new VBox();
    boxRedeclared.setAlignment(Pos.CENTER);
    boxRedeclared.setSpacing(10);

    Label lblRedeclared = new Label("Re-declared");
    lblRedeclared.getStyleClass().add("input-output-labels-smaller");
    boxRedeclared.getChildren().add(lblRedeclared);

    Label lblResultTora = new Label(df.format(runway.getTora()) + " m");
    lblResultTora.getStyleClass().add("results-smaller");
    Label lblResultToda = new Label(df.format(runway.getToda()) + " m");
    lblResultToda.getStyleClass().add("results-smaller");
    Label lblResultAsda = new Label(df.format(runway.getAsda()) + " m");
    lblResultAsda.getStyleClass().add("results-smaller");
    Label lblResultLda = new Label(df.format(runway.getLda()) + " m");
    lblResultLda.getStyleClass().add("results-smaller");
    Label lblResultStopWay = new Label(df.format(runway.getStopWay()) + " m");
    lblResultStopWay.getStyleClass().add("results-smaller");
    Label lblResultClearWay = new Label(df.format(runway.getClearWay()) + " m");
    lblResultClearWay.getStyleClass().add("results-smaller");

    boxRedeclared.getChildren().addAll(lblResultTora, lblResultToda, lblResultAsda, lblResultLda, lblResultStopWay, lblResultClearWay);
    boxOriginalRedeclared.getChildren().add(boxRedeclared);
  }

}