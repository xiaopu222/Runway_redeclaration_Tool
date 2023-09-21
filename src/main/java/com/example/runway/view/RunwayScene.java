package com.example.runway.view;

import com.example.runway.controller.Alerts;
import com.example.runway.controller.FileManager;
import com.example.runway.controller.InputManager;
import com.example.runway.controller.Validations;
import com.example.runway.model.Airport;
import com.example.runway.model.Obstacle;
import com.example.runway.model.Runway;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.util.Pair;


/**
 * The runway scene. Holds the main UI page (input/output panels, runway visualization and main menu).
 */
public class RunwayScene extends MainScene {

  // Variables for displaying input fields:
  private VBox boxChoices;
  private Label lblObstacle;
  private ChoiceBox cbObstacles;
  private Label lblMethod;
  private ChoiceBox cbMethods;
  private ChoiceBox cbAirports;
  private Label lblRunway;
  private ChoiceBox cbRunways;
  private int choiceBoxWidth = 220;
  private Button btnCalculate;
  private boolean airportChosen = false;
  private boolean runwayChosen = false;
  private boolean obstacleChosen = false;
  private boolean methodChosen = false;
  private Obstacle obstacle = null;
  private final String[] methodsStrings = {"none", "Landing over the obstacle", "Landing towards the obstacle", "Take-off towards the obstacle", "Take-off away from the obstacle"};

  // Variables for handling top-down view rotation:
  private boolean rotateOrReturn = true;
  private final Rotate rotate = new Rotate();

  // Identifies which view to show
  private boolean topDown = true;

  // Variables for displaying the visualization and its buttons:
  private final HBox boxButtons = new HBox();
  private StackPane runwayPane;
  private StackPane runwayStrip;
  private Button btnRotate;
  private Button btnRotate10;
  private Button btnTopDown;
  private Button btnSideOn;
  private final Button btnBreakdown = new Button("Calculation Breakdown");

  Pair<String, String> message = new Pair<>("", "");

  // Variables for displaying the runway number:
  private boolean empty = true;
  private int left;
  private int right;
  private String leftPrefix = "";
  private String leftLetter = "";
  private String rightLetter = "";
  private Label lblLeftRunwayNumber = new Label();
  private Label lblLeftLetter = new Label();
  private Label lblRightRunwayNumber = new Label();
  private Label lblRightLetter = new Label();
  private Map<String, String> lettersMap = new HashMap<>();

  // Variables for displaying output fields:
  private VBox outputs = new VBox();
  private Label lblOutput;
  private final RunwayDataPane runwayDataPane = new RunwayDataPane();
  private final ObstacleDataPane obstacleDataPane = new ObstacleDataPane();


  private Notifications notificationsPane;
  private int view = 0;

  private int rotateAngle = 0;

  private List<Label> viewLabels = new ArrayList<>();

  private final DecimalFormat df = new DecimalFormat("0.0");
  private Rectangle rectClearWay;
  private Rectangle rectStopWay;
  private Label lblClear = new Label("Clear Way");
  private Label lblStop = new Label("Stop Way");

  private CompasPane compasPane = new CompasPane();

  private double mouseAnchorX;
  private double mouseAnchorY;
  private double translateAnchorX;
  private double translateAnchorY;
  private boolean isDragging = false;
  private final Scale scale = new Scale(1, 1);

  private boolean allowExport = false;
  private boolean zoomIn = true;

  /**
   * Create a new runway scene
   * @param mainPage     the main page parameters of the UI
   * @param inputManager controller for all user inputs
   * @param fileManager controller for all files
   */
  public RunwayScene(MainPage mainPage, InputManager inputManager, FileManager fileManager) {
    super(mainPage);

    this.inputManager = inputManager;
    this.fileManager = fileManager;

    // Fill the runway letter pairs with values:
    lettersMap.put("L", "R");
    lettersMap.put("R", "L");
    lettersMap.put("C", "C");
    lettersMap.put("", "");

    // Initialise controllers:
    validations = new Validations();
    alerts = new Alerts();
  }

  /**
   * Create a new runway scene
   * @param mainPage     the main page parameters of the UI
   * @param inputManager controller for all user inputs
   * @param fileManager controller for all files
   * @param runway runway to display
   */
  public RunwayScene(MainPage mainPage, InputManager inputManager, FileManager fileManager, Runway runway) {
    super(mainPage);

    this.inputManager = inputManager;
    this.fileManager = fileManager;

    // Fill the runway letter pairs with values:
    lettersMap.put("L", "R");
    lettersMap.put("R", "L");
    lettersMap.put("C", "C");
    lettersMap.put("", "");

    // Initialise controllers:
    validations = new Validations();
    alerts = new Alerts();

    airportChosen = true;
    runwayChosen = true;
    inputManager.setModified(true);

    inputManager.setCurrentRunway(runway);
  }

  /**
   * Draw the UI components
   */
  @Override
  public void draw() {
    // Set up the main pane
    root = new MainPane();
    root.getStylesheets().add(getClass().getResource(inputManager.getStyle()).toExternalForm());
    root.getStyleClass().add(("runwayScene"));

    btnBreakdown.setDisable(true);

    rectClearWay = new Rectangle(50, 50);
    rectClearWay.getStyleClass().add("border");
    rectStopWay = new Rectangle(25, 30);
    rectStopWay.getStyleClass().add("border");

    lblClear.getStyleClass().add("way-label");
    lblClear.setVisible(false);
    lblStop.getStyleClass().add("way-label");
    lblStop.setVisible(false);

    // Draw components
    drawMenu();
    drawNotifications();
    drawInputs();
    drawRunway();
    drawOutputs();
  }

  /**
   * Draw the main menu
   */
  public void drawMenu() {
    MenuBar menuBar = new MenuBar();
    menuBar.setMaxWidth(700);
    StackPane.setMargin(menuBar, new Insets(10, 0, 0, 0));
    StackPane.setAlignment(menuBar, Pos.TOP_CENTER);

    // Menu for adding new data:
    Menu menuNew = new Menu("New");
    MenuItem itemAirport = new MenuItem("Airport");
    itemAirport.setOnAction(event -> mainPage.showAddAirport(inputManager, fileManager));
    MenuItem itemRunway = new MenuItem("Runway");
    itemRunway.setOnAction(event -> {
      if (airportChosen) {
        mainPage.showAddRunway(inputManager, fileManager);
      }
    });
    MenuItem itemObstacle = new MenuItem("Obstacle");
    itemObstacle.setOnAction(event -> {
      if (airportChosen && runwayChosen) {
        mainPage.showAddObstacle(inputManager, fileManager);
      }
    });
    menuNew.getItems().addAll(itemAirport, itemRunway, itemObstacle);

    // Menu for modifying data:
    Menu menuModify = new Menu("Modify");
    MenuItem itemAirportM = new MenuItem("Airport");
    itemAirportM.setOnAction(event -> {
      if (airportChosen) {
        mainPage.showModify(inputManager, fileManager, 1);
      }
      else {
        alerts.alertError("No airport chosen", "Please choose an airport to modify");
      }
    });
    MenuItem itemRunwayM = new MenuItem("Runway");
    itemRunwayM.setOnAction(event -> {
      if (airportChosen) {
        if (runwayChosen) {
          mainPage.showModify(inputManager, fileManager, 2);
        }
        else {
          alerts.alertError("No runway chosen", "Please choose a runway to modify");
        }
      }
      else {
        alerts.alertError("No airport chosen", "Please choose an airport to modify");
      }
    });
    MenuItem itemObstacleM = new MenuItem("Obstacle");
    itemObstacleM.setOnAction(event -> {
      if (airportChosen) {
        if (runwayChosen) {
          if (obstacleChosen && obstacle != null) {
            inputManager.getCurrentRunway().setCurrentObstacle(obstacle);
            mainPage.showModify(inputManager, fileManager, 3);
          }
          else {
            alerts.alertError("No obstacle chosen", "Please choose an obstacle to modify");
          }
        }
        else {
          alerts.alertError("No runway chosen", "Please choose a runway to modify");
        }
      }
      else {
        alerts.alertError("No airport chosen", "Please choose an airport to modify");
      }
    });
    menuModify.getItems().addAll(itemAirportM, itemRunwayM, itemObstacleM);

    // Menu for file import:
    Menu menuImport = new Menu("Import");
    MenuItem itemImportXML = new MenuItem("XML");
    itemImportXML.setOnAction(event -> mainPage.showImport(inputManager, fileManager));
    menuImport.getItems().addAll(itemImportXML);

    // Menu for file export:
    Menu menuExport = new Menu("Export");
    MenuItem itemExportXML = new MenuItem("XML");
    itemExportXML.setOnAction(event -> mainPage.showExport(inputManager, fileManager, 0, null));
    MenuItem itemJPEG = new MenuItem("View (JPEG)");
    itemJPEG.setOnAction(event -> {
      if (allowExport) {
        mainPage.showExport(inputManager, fileManager, 1, runwayPane);
      }
      else {
        alerts.alertError("No data chosen for export", "Insert your inputs and calculate values for the export first.");
      }
    });
    MenuItem itemPNG = new MenuItem("View (PNG)");
    itemPNG.setOnAction(event -> {
      if (allowExport) {
        mainPage.showExport(inputManager, fileManager, 2, runwayPane);
      }
      else {
        alerts.alertError("No data chosen for export", "Insert your inputs and calculate values for the export first.");
      }
    });
    MenuItem itemCurrent = new MenuItem("Current situation (TXT)");
    itemCurrent.setOnAction(event -> {
      if (allowExport) {
        mainPage.showExport(inputManager, fileManager, 3, null);
      }
      else {
        alerts.alertError("No data chosen for export", "Insert your inputs and calculate values for the export first.");
      }
    });
    menuExport.getItems().addAll(itemExportXML, itemJPEG, itemPNG, itemCurrent);

    // Menu for changing colour modes:
    Menu menuChangeColor = new Menu("Change Style");
    MenuItem changeColorItem = new MenuItem("Colour-Blind Mode");
    MenuItem ChangeColorItem2 = new MenuItem("Light Mode");
    ChangeColorItem2.setOnAction(this::LightModel);
    changeColorItem.setOnAction(this::ColourBlindModel);
    menuChangeColor.getItems().addAll(changeColorItem,ChangeColorItem2);

    // Menu for help section:
    Menu menuHelp = new Menu("Help");
    MenuItem itemHelpRedeclaration = new MenuItem("Runway re-declaration");
    itemHelpRedeclaration.setOnAction(event -> mainPage.showHelp(inputManager, fileManager, 0));
    MenuItem itemHelpAdd = new MenuItem("Adding new data");
    itemHelpAdd.setOnAction(event -> mainPage.showHelp(inputManager, fileManager, 1));
    MenuItem itemHelpModify = new MenuItem("Modifying existing data");
    itemHelpModify.setOnAction(event -> mainPage.showHelp(inputManager, fileManager, 2));
    MenuItem itemHelpImport = new MenuItem("Importing files");
    itemHelpImport.setOnAction(event -> mainPage.showHelp(inputManager, fileManager, 3));
    MenuItem itemHelpExport = new MenuItem("Exporting files");
    itemHelpExport.setOnAction(event -> mainPage.showHelp(inputManager, fileManager, 4));
    menuHelp.getItems().addAll(itemHelpRedeclaration, itemHelpAdd, itemHelpModify, itemHelpImport, itemHelpExport);


    menuBar.getMenus().addAll(menuNew, menuModify, menuImport, menuExport, menuChangeColor, menuHelp);
    root.getChildren().add(menuBar);
  }

  /**
   * Switch to Colour Blind colour mode:
   */
  public void ColourBlindModel(ActionEvent event) {
    root.getStylesheets().clear();
    root.getStylesheets().add(getClass().getResource("/style/style2.css").toExternalForm());
    inputManager.setStyle("/style/style2.css");
  }

  /**
   * Switch to Light colour mode:
   */
  public void LightModel(ActionEvent event) {
    root.getStylesheets().clear();
    root.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
    inputManager.setStyle("/style/style.css");
  }

  /**
   * Display UI components for user input
   */
  public void drawInputs() {

    VBox boxInputs = new VBox();
    boxInputs.setMaxSize(340, 550);
    boxInputs.setAlignment(Pos.TOP_CENTER);
    boxInputs.setSpacing(5);

    Label lblInput = new Label("Input");
    lblInput.getStyleClass().add("heading");
    boxInputs.getChildren().add(lblInput);

    // Set-up VBox holding all elements:
    VBox inputs = new VBox();
    inputs.setMaxSize(340, 500);
    inputs.setMinSize(340, 500);
    inputs.getStyleClass().add("obstacle-pane");
    inputs.setAlignment(Pos.TOP_CENTER);
    inputs.setSpacing(20);

    // Draw labels, text fields and choice boxes:

    boxChoices = new VBox();
    boxChoices.setAlignment(Pos.TOP_CENTER);
    boxChoices.setSpacing(10);

    // Display airport list:
    Label lblAirport = new Label("Airport:");
    VBox.setMargin(lblAirport, new Insets(50, 0, 0, 0));
    lblAirport.getStyleClass().add("input-output-labels");
    List<String> airportNames = inputManager.getAirportNames();
    cbAirports = new ChoiceBox(FXCollections.observableArrayList(airportNames));
    cbAirports.getStyleClass().add("choice-box");
    cbAirports.setPrefWidth(choiceBoxWidth);
    // Handle user selection:
    cbAirports.getSelectionModel().selectedIndexProperty().addListener(
        (observableValue, number, t1) -> {
          int num = t1.intValue();
          airportChosen = true;
          Airport airport = inputManager.getAirports().get(num);
          inputManager.setCurrentAirport(airport);
          boxChoices.getChildren().remove(lblRunway);
          boxChoices.getChildren().remove(cbRunways);
          boxChoices.getChildren().remove(lblObstacle);
          boxChoices.getChildren().remove(cbObstacles);
          boxChoices.getChildren().remove(lblMethod);
          boxChoices.getChildren().remove(cbMethods);
          obstacleChosen = false;
          allowExport = false;
          methodChosen = false;
          btnCalculate.setVisible(false);
          btnCalculate.setDisable(true);
          if (inputManager.isModified()) {
            if (!runwayChosen) {
              inputManager.setModified(false);
            }
          }
          else {
            runwayChosen = false;
            notificationsPane.addBlackNotification("Airport \"" + airport.getName() + "\" has been chosen.");
          }
          runwayDataPane.hide();
          obstacleDataPane.draw(null);
          addRunwayChoice();
        });

    boxChoices.getChildren().addAll(lblAirport, cbAirports);

    // Button that will start calculations:
    btnCalculate = new Button("Re-declare");
    btnCalculate.getStyleClass().add("button-calculate");
    btnCalculate.setOnAction(this::calculate);
    btnCalculate.setVisible(false);
    btnCalculate.setDisable(true);

    // Add components to the VBox:
    inputs.getChildren().add(boxChoices);
    inputs.getChildren().addAll(btnCalculate);

    boxInputs.getChildren().add(inputs);

    // Add the inputs VBox to main pane:
    StackPane.setAlignment(boxInputs, Pos.TOP_LEFT);
    StackPane.setMargin(boxInputs, new Insets(9, 0, 0, 5));
    root.getChildren().add(boxInputs);

    if (airportChosen) {
      cbAirports.getSelectionModel().select(inputManager.getAirports().indexOf(inputManager.getCurrentAirport()));
    }
  }

  /**
   * Display UI components for available runways
   */
  public void addRunwayChoice() {
    // Display runway list:
    lblRunway = new Label("Runway:");
    lblRunway.getStyleClass().add("input-output-labels");
    List<String> runwayNumbers = inputManager.getCurrentAirport().getRunwayNumbers();
    cbRunways = new ChoiceBox(FXCollections.observableArrayList(runwayNumbers));
    cbRunways.getStyleClass().add("choice-box");
    cbRunways.setPrefWidth(choiceBoxWidth);
    // Handle user selection:
    cbRunways.getSelectionModel().selectedIndexProperty().addListener(
        (observableValue, number, t1) -> {
          int num = t1.intValue();
          runwayChosen = true;
          Runway runway = inputManager.getCurrentAirport().getRunways().get(num);
          inputManager.setCurrentRunway(runway);
          boxChoices.getChildren().remove(lblObstacle);
          boxChoices.getChildren().remove(cbObstacles);
          obstacleChosen = false;
          allowExport = false;
          methodChosen = false;
          btnCalculate.setVisible(false);
          btnCalculate.setDisable(true);
          if (inputManager.isModified()) {
            inputManager.setModified(false);
          }
          else {
            notificationsPane.addBlackNotification(
                "Runway \"" + runway.getRunwayNumber() + "\" has been chosen.");
          }
          runwayDataPane.draw(false, inputManager.getCurrentRunway());
          obstacleDataPane.draw(null);
          addObstacleChoice();
        });

    boxChoices.getChildren().addAll(lblRunway, cbRunways);

    if (runwayChosen) {
      cbRunways.getSelectionModel().select(inputManager.getCurrentAirport().getRunways().indexOf(inputManager.getCurrentRunway()));
    }
  }

  /**
   * Display UI components for available obstacles
   */
  public void addObstacleChoice() {
    leftPrefix = "";
    setLeftRight(inputManager.getCurrentRunway().getRunwayNumber());

    // Display obstacle list:
    lblObstacle = new Label("Obstacle:");
    lblObstacle.getStyleClass().add("input-output-labels");
    List<String> obstacleNames = new ArrayList<>();
    obstacleNames.add("none");
    obstacleNames.addAll(inputManager.getCurrentRunway().getObstacleNames());
    cbObstacles = new ChoiceBox(FXCollections.observableArrayList(obstacleNames));
    cbObstacles.getStyleClass().add("choice-box");
    cbObstacles.setPrefWidth(choiceBoxWidth);
    // Handle user selection:
    cbObstacles.getSelectionModel().selectedIndexProperty().addListener(
        (observableValue, number, t1) -> {
          int num = t1.intValue();
          if (num == 0) {
            obstacleChosen = false;
            allowExport = false;
          }
          else {
            obstacleChosen = true;
            obstacle = inputManager.getCurrentRunway().getObstacles().get(num - 1);
          }
          rotate.setAngle(0);
          rotateAngle = 0;
          rotateOrReturn = true;
          for (Label label : viewLabels) {
            label.setRotate(0);
          }
          if (compasPane != null) {
            compasPane.getRotatablePane().setRotate(0);
          }
          if (btnRotate != null) {
            btnRotate.setText("Rotate to Compas location");
            btnRotate.setStyle(null);
          }
        });

    boxChoices.getChildren().addAll(lblObstacle, cbObstacles);
    boxChoices.getChildren().remove(lblMethod);
    boxChoices.getChildren().remove(cbMethods);
    obstacleChosen = false;
    allowExport = false;
    methodChosen = false;
    addMethodChoice();
  }

  /**
   * Display UI components for available methods
   */
  public void addMethodChoice() {
    // Display method list:
    lblMethod = new Label("Landing / take-off method:");
    lblMethod.getStyleClass().add("input-output-labels");
    cbMethods = new ChoiceBox(FXCollections.observableArrayList(methodsStrings));
    cbMethods.getStyleClass().add("choice-box");
    cbMethods.setPrefWidth(choiceBoxWidth);
    // Handle user selection:
    cbMethods.getSelectionModel().selectedIndexProperty().addListener(
        (observableValue, number, t1) -> {
          int num = t1.intValue();
          if (num != 0) {
            inputManager.setMethod(inputManager.getMethods()[num - 1]);
            methodChosen = true;
          } else {
            methodChosen = false;
          }
          rotate.setAngle(0);
          rotateAngle = 0;
          rotateOrReturn = true;
          for (Label label : viewLabels) {
            label.setRotate(0);
          }
          if (compasPane != null) {
            compasPane.getRotatablePane().setRotate(0);
          }
          if (btnRotate != null) {
            btnRotate.setText("Rotate to Compas location");
            btnRotate.setStyle(null);
          }
        });

    boxChoices.getChildren().addAll(lblMethod, cbMethods);
    btnCalculate.setVisible(true);
    btnCalculate.setDisable(false);
  }

  /**
   * Display UI components for the calculations output
   */
  public void drawOutputs() {
    // Set-up VBox holding all elements:
    outputs = new VBox();
    outputs.setMaxSize(360, 500);
    outputs.setMinWidth(390);
    outputs.setAlignment(Pos.CENTER);
    outputs.setSpacing(5);

    // Draw labels with results:

    lblOutput = new Label("Default values");
    lblOutput.getStyleClass().add("heading");

    // Add components to the VBox:
    outputs.getChildren().add(lblOutput);
    outputs.getChildren().add(runwayDataPane);
    outputs.getChildren().add(obstacleDataPane);

    // Add the outputs VBox to main pane:
    StackPane.setAlignment(outputs, Pos.TOP_RIGHT);
    StackPane.setMargin(outputs, new Insets(10, 0, 0, 5));
    root.getChildren().add(outputs);
  }

  /**
   * Display Calculation Breakdown
   */
  public void breakdown(ActionEvent event) {
    alerts.alertBreakdown(message.getKey(), message.getValue());
  }

  /**
   * Display UI components for the notifications pane
   */
  public void drawNotifications() {
    HBox boxNotifications = new HBox();
    boxNotifications.setAlignment(Pos.CENTER);
    boxNotifications.setMaxSize(975, 100);
    boxNotifications.setSpacing(10);

    Label lblNotifications = new Label("Notifications");
    lblNotifications.getStyleClass().add("notif");

    notificationsPane = new Notifications(inputManager);
    for (Text notification : inputManager.getNotifications()) {
      notificationsPane.addNotification(notification);
    }

    boxNotifications.getChildren().addAll(lblNotifications, notificationsPane);

    StackPane.setAlignment(boxNotifications, Pos.BOTTOM_CENTER);
    StackPane.setMargin(boxNotifications, new Insets(0, 312, 10, 110));
    root.getChildren().add(boxNotifications);
  }

  /**
   * Handle when the Calculate button is pressed
   * @param event    action event
   */
  public void calculate(ActionEvent event) {
    lblOutput.setText("Results");
    btnBreakdown.setDisable(true);

    this.view = 0;

    // Rotate top-down view back to horizontal:
    rotateOrReturn = true;
    rotate.setAngle(0);

    // Set runway parameters back to default:
    inputManager.getCurrentRunway().setDefault();

    // Display notification:
    if (obstacleChosen) {
      Obstacle current = inputManager.getObstacles().get(cbObstacles.getSelectionModel().getSelectedIndex() - 1);
      inputManager.getCurrentRunway().setCurrentObstacle(current);
      notificationsPane.addBlackNotification("Obstacle \"" + current.getName() + "\" has been chosen.");

      allowExport = true;
    }
    else {
      // Display obstacle data:
      obstacleDataPane.draw(null);
    }

    empty = false;
    // Validate user input:
    if (!cbObstacles.getSelectionModel().isEmpty()) {
      if (!cbMethods.getSelectionModel().isEmpty()) {
        if (!(!obstacleChosen && methodChosen)) {

          if (methodChosen) {
            btnBreakdown.setDisable(false);

            // Re-calculate parameters:
            inputManager.calculateValues();

            // Handle negative calculation results:
            List<String> negative = inputManager.getCurrentRunway().getNegativeValues();
            if (inputManager.getCurrentRunway().hasNegative()) {
              String values = "";
              for (String value : negative) {
                values += value + ", ";
              }
              values = values.substring(0, values.length() - 2);
              // Display notification:
              alerts.alertError("Negative values for " + values + " calculated", "Landing / Take-Off not possible");
              notificationsPane.addRedNotification("WARNING!!!  Negative results for " + values + " calculated for airport \"" + inputManager.getCurrentAirport().getName() + "\".");
            }
            else {
              notificationsPane.addGreenNotification("Runway data for airport \"" + inputManager.getCurrentAirport().getName() + "\" has been successfully calculated.");
            }

            int method = cbMethods.getSelectionModel().getSelectedIndex();
            message = inputManager.getCurrentRunway().getBreakdown(method);
          }

          // Display the runway view:
          drawRunway();

          // Display runway and obstacle data:
          runwayDataPane.draw(methodChosen, inputManager.getCurrentRunway());
          if (obstacleChosen) {
            obstacleDataPane.draw(inputManager.getCurrentRunway().getCurrentObstacle());
          }
        }
        // Errors displayed in case of invalid user input:
        else {
          alerts.alertError("No Method Chosen", "A method has to be chosen, because the object is not \"none\"");
        }
      }
      else {
        alerts.alertError("No Method Chosen", "Please choose a method");
      }
    }
    else {
      alerts.alertError("No Obstacle Chosen", "Please choose an obstacle");
    }
  }

  /**
   * Manage strings representing runway numbers on the runway view
   * @param number    user input of runway number
   */
  public void setLeftRight(String number) {
    int len = number.length();
    char last = Character.toUpperCase(number.charAt(len - 1));
    String letter = "";
    if (validations.getRunwayChars().contains(Character.toUpperCase(last))) {
      number = number.substring(0, len - 1);
      letter = String.valueOf(last);
    }
    int num = Integer.parseInt(number);
    if (num >= 0 && num <= 18) {
      left = num;
      leftLetter = letter;
      right = num + 18;
      rightLetter = lettersMap.get(leftLetter);
    }
    else {
      right = num;
      rightLetter = letter;
      left = num - 18;
      leftLetter = lettersMap.get(rightLetter);
    }
    if (left <= 9) {
      leftPrefix = "0";
    }
  }

  /**
   * Display UI components for the runway view
   */
  public void drawRunway() {
    // Background pane of the view:
    StackPane runway = new StackPane();
    runway.getStyleClass().add("runway");
    runway.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
    runway.setMaxSize(700, 500);

    // Rotatable pane of the view:
    runwayPane = new StackPane();
    runwayPane.getStyleClass().add("runway-pane");
    runwayPane.setMaxSize(600, 400);
    runwayPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
    runwayPane.getChildren().addAll(lblClear, lblStop);
    rectClearWay.setVisible(false);
    runwayPane.getChildren().add(rectClearWay);
    rectStopWay.setVisible(false);
    runwayPane.getChildren().add(rectStopWay);

    // Set clip:
    Rectangle clipRect = new Rectangle(runway.getMaxWidth(), runway.getMaxHeight());
    runway.setClip(clipRect);

    // Before user input display:
    if (empty) {
      Label lblNothing = new Label("Please enter the runway data on the left");
      lblNothing.getStyleClass().add("results");
      runwayPane.getChildren().add(lblNothing);

      // Add components to the background pane:
      runway.getChildren().add(runwayPane);
      runway.getChildren().add(boxButtons);
    }
    // After user input display:
    else {
      // Display buttons:
      boxButtons.getChildren().clear();

      boxButtons.setAlignment(Pos.BOTTOM_CENTER);
      boxButtons.setSpacing(20);
      boxButtons.setMaxSize(700, 50);
      StackPane.setAlignment(boxButtons, Pos.BOTTOM_LEFT);

      btnTopDown = new Button("Top-down");
      btnTopDown.setOnAction(this::changeVisualization);
      btnTopDown.setDisable(true);
      boxButtons.getChildren().add(btnTopDown);

      btnSideOn = new Button("Side-on");
      btnSideOn.setOnAction(this::changeVisualization);
      boxButtons.getChildren().add(btnSideOn);

      btnRotate = new Button("Rotate to Compas location");
      btnRotate.setOnAction(this::rotate);
      rotate.setPivotX(300);
      rotate.setPivotY(200);
      runwayPane.getTransforms().add(rotate);
      StackPane.setAlignment(btnRotate, Pos.BOTTOM_CENTER);
      boxButtons.getChildren().add(btnRotate);

      btnRotate10 = new Button("Rotate by 10 degrees");
      btnRotate10.setOnAction(this::rotate10);
      StackPane.setAlignment(btnRotate10, Pos.BOTTOM_CENTER);
      boxButtons.getChildren().add(btnRotate10);

      btnBreakdown.setOnAction(this::breakdown);
      boxButtons.getChildren().add(btnBreakdown);

      // Draw the top-down view:
      drawTopDown();

      // Add components to the background pane:
      runway.getChildren().add(runwayPane);
      runway.getChildren().add(boxButtons);

      drawViewMenu(runway);

      InputStream streamIn = getClass().getResourceAsStream("/help_images/zoom_in.jpg");
      Image imageIn = new Image(streamIn);
      InputStream streamOut = getClass().getResourceAsStream("/help_images/zoom_out.jpg");
      Image imageOut = new Image(streamOut);

      ImageView imageViewIn = new ImageView(imageIn);
      imageViewIn.setFitWidth(20);
      imageViewIn.setFitHeight(20);
      ImageView imageViewOut = new ImageView(imageOut);
      imageViewOut.setFitWidth(20);
      imageViewOut.setFitHeight(20);

      Button btnZoomIn = new Button();
      btnZoomIn.setMinSize(25, 25);
      btnZoomIn.setMaxSize(25, 25);
      btnZoomIn.getStyleClass().add("button-zoom");
      btnZoomIn.setGraphic(imageViewIn);
      btnZoomIn.setOnAction(event -> {
        double scaleValue = scale.getX() + 0.1;
        if (scaleValue > 10) {
          scaleValue = 10;
        }
        scale.setX(scaleValue);
        scale.setY(scaleValue);
      });
      StackPane.setAlignment(btnZoomIn, Pos.TOP_RIGHT);
      StackPane.setMargin(btnZoomIn, new Insets(1, 0, 0, 0));

      Button btnZoomOut = new Button();
      btnZoomOut.setMinSize(25, 25);
      btnZoomOut.setMaxSize(25, 25);
      btnZoomOut.getStyleClass().add("button-zoom");
      btnZoomOut.setGraphic(imageViewOut);
      btnZoomOut.setOnAction(event -> {
        double scaleValue = scale.getX() - 0.1;
        if (scaleValue < 1) {
          scaleValue = 1;
        }
        scale.setX(scaleValue);
        scale.setY(scaleValue);
        if (scaleValue == 1.0) {
          runwayPane.setTranslateX(0);
          runwayPane.setTranslateY(0);
        }
      });
      StackPane.setAlignment(btnZoomOut, Pos.TOP_RIGHT);
      StackPane.setMargin(btnZoomOut, new Insets(1, 25, 0, 0));

      runway.getChildren().addAll(btnZoomIn, btnZoomOut);


      drawCompas();
      runway.getChildren().add(compasPane);
    }

    // Add background pane to main pane:
    StackPane.setAlignment(runway, Pos.TOP_CENTER);
    StackPane.setMargin(runway, new Insets(50, 0, 0, 0));

    runwayPane.getTransforms().add(scale);

    // Add event handlers to make the StackPane draggable:
    runwayPane.setOnMousePressed(event -> {
      // Store the initial anchor point and translation values
      mouseAnchorX = event.getSceneX();
      mouseAnchorY = event.getSceneY();
      translateAnchorX = runwayPane.getTranslateX();
      translateAnchorY = runwayPane.getTranslateY();
      isDragging = true;
    });

    // Add event handlers to make the StackPane draggable:
    runwayPane.setOnMouseDragged(event -> {
      if (isDragging) {
        // Calculate the new translation values based on the drag distance
        double dragX = event.getSceneX() - mouseAnchorX;
        double dragY = event.getSceneY() - mouseAnchorY;
        double newTranslateX = translateAnchorX + dragX;
        double newTranslateY = translateAnchorY + dragY;

        // Set the new translation values
        runwayPane.setTranslateX(newTranslateX);
        runwayPane.setTranslateY(newTranslateY);
      }
    });

    runwayPane.setOnMouseReleased(event -> isDragging = false);

    // Add an event handler to the StackPane to handle mouse wheel events
    runwayPane.setOnScroll(event -> {
      double delta = event.getDeltaY();
      double scaleValue = scale.getX() + delta / 1000;

      // Limit the minimum and maximum scale value
      if (scaleValue < 1.0) {
        scaleValue = 1.0;
      } else if (scaleValue > 10) {
        scaleValue = 10;
      }

      scale.setX(scaleValue);
      scale.setY(scaleValue);

      if (scaleValue == 1.0) {
        runwayPane.setTranslateX(0);
        runwayPane.setTranslateY(0);
      }
    });

    // Add an event filter to the StackPane to handle pinch zoom gestures on macOS
    runwayPane.addEventFilter(ZoomEvent.ZOOM, event -> {
      double zoomFactor = event.getZoomFactor();
      double scaleValue = scale.getX() * zoomFactor;

      // Limit the minimum and maximum scale value
      if (scaleValue < 1.0) {
        scaleValue = 1.0;
      } else if (scaleValue > 10) {
        scaleValue = 10;
      }

      scale.setX(scaleValue);
      scale.setY(scaleValue);

      if (scaleValue == 1.0) {
        runwayPane.setTranslateX(0);
        runwayPane.setTranslateY(0);
      }

      event.consume();
    });

    root.getChildren().add(runway);
  }

  /**
   * Display physical / logical runway view menu:
   */
  public void drawViewMenu(StackPane pane) {
    MenuBar menuBar = new MenuBar();
    menuBar.setMaxSize(645, 20);
    StackPane.setMargin(menuBar, new Insets(0, 0, 0, 0));
    StackPane.setAlignment(menuBar, Pos.TOP_LEFT);

    Menu menuChoice = new Menu("Runway View Choice");
    // Physical:
    MenuItem itemPhysical = new MenuItem("Physical");
    itemPhysical.setOnAction(event -> {
      scale.setX(1.0);
      scale.setY(1.0);
      runwayPane.setTranslateX(0);
      runwayPane.setTranslateY(0);

      this.view = 0;
      if (topDown) {
        drawTopDown();
      }
      else {
        drawSideOn();
      }
    });
    // Logical:
    MenuItem itemLogical = new MenuItem("Logical");
    itemLogical.setOnAction(event -> {
      scale.setX(1.0);
      scale.setY(1.0);
      runwayPane.setTranslateX(0);
      runwayPane.setTranslateY(0);

      this.view = 1;
      if (topDown) {
        drawTopDown();
      }
      else {
        drawSideOn();
      }
    });
    menuChoice.getItems().addAll(itemPhysical, itemLogical);

    menuBar.getMenus().add(menuChoice);
    pane.getChildren().add(menuBar);
  }

  /**
   * Handle when "Top-down" or "Side-on" button is pressed.
   * Display the correct view
   * @param event    action event
   */
  public void changeVisualization(ActionEvent event) {
    scale.setX(1.0);
    scale.setY(1.0);
    runwayPane.setTranslateX(0);
    runwayPane.setTranslateY(0);
    if (topDown) {
      btnSideOn.setDisable(true);
      btnTopDown.setDisable(false);
      drawSideOn();
      topDown = false;
    }
    else {
      btnTopDown.setDisable(true);
      btnSideOn.setDisable(false);
      drawTopDown();
      topDown = true;
    }
  }

  /**
   * Display UI components for the top-down view
   */
  public void drawTopDown() {
    // Activate the Rotate button:
    runwayPane.getChildren().clear();
    runwayPane.getStyleClass().clear();
    runwayPane.getStyleClass().add("runway-pane");
    compasPane.setVisible(true);
    compasPane.getRotatablePane().setRotate(0);

    topDown = true;

    btnRotate.setDisable(false);
    // btnRotate.setVisible(true);
    btnRotate10.setDisable(false);
    // btnRotate10.setVisible(true);

    // Polygon for the cleared area:
    Polygon polyClearedArea = new Polygon();
    polyClearedArea.getStyleClass().add("polygon");
    polyClearedArea.getPoints().addAll(0.0, 150.0,
        100.0, 150.0,
        150.0, 100.0,
        450.0, 100.0,
        500.0, 150.0,
        600.0, 150.0,
        600.0, 300.0,
        500.0, 300.0,
        450.0, 350.0,
        150.0, 350.0,
        100.0, 300.0,
        0.0, 300.0);

    // Draw the runway strip:

    runwayStrip = new StackPane();
    runwayStrip.setStyle("-fx-background-color: grey;");
    runwayStrip.setMaxSize(500, 50);

    Line lineDotted = new Line(150, 225, 450, 225);
    lineDotted.getStrokeDashArray().addAll(10d, 7d, 0d, 7d);
    lineDotted.setStroke(Color.WHITE);

    VBox leftLines = getLines();
    StackPane.setAlignment(leftLines, Pos.CENTER_LEFT);
    StackPane.setMargin(leftLines, new Insets(0, 0, 0, 10));

    VBox rightLines = getLines();
    StackPane.setAlignment(rightLines, Pos.CENTER_RIGHT);
    StackPane.setMargin(rightLines, new Insets(0, 0, 0, 465));

    lblLeftRunwayNumber.setText(leftPrefix + left);
    lblLeftRunwayNumber.setRotate(90);
    lblLeftRunwayNumber.getStyleClass().add("runway-number");
    lblLeftLetter.setText(leftLetter);
    lblLeftLetter.setRotate(90);
    lblLeftLetter.getStyleClass().add("runway-number");

    StackPane.setAlignment(lblLeftRunwayNumber, Pos.CENTER_LEFT);
    StackPane.setMargin(lblLeftRunwayNumber, new Insets(0, 0, 0, 55));
    StackPane.setAlignment(lblLeftLetter, Pos.CENTER_LEFT);
    StackPane.setMargin(lblLeftLetter, new Insets(0, 0, 0, 42));

    lblRightRunwayNumber.setText(String.valueOf(right));
    lblRightRunwayNumber.setRotate(-90);
    lblRightRunwayNumber.getStyleClass().add("runway-number");
    lblRightLetter.setText(rightLetter);
    lblRightLetter.setRotate(-90);
    lblRightLetter.getStyleClass().add("runway-number");

    StackPane.setAlignment(lblRightRunwayNumber, Pos.CENTER_LEFT);
    StackPane.setMargin(lblRightRunwayNumber, new Insets(0, 0, 0, 426));
    StackPane.setAlignment(lblRightLetter, Pos.CENTER_LEFT);
    StackPane.setMargin(lblRightLetter, new Insets(0, 0, 0, 449));

    runwayStrip.getChildren().add(lineDotted);
    runwayStrip.getChildren().add(leftLines);
    runwayStrip.getChildren().add(rightLines);

    if (!methodChosen) {
      if (view == 0) {
        runwayStrip.getChildren().addAll(lblLeftRunwayNumber, lblLeftLetter);
        runwayStrip.getChildren().addAll(lblRightRunwayNumber, lblRightLetter);
      } else {
        if (inputManager.getCurrentRunway().getDirection() == 0) {
          runwayStrip.getChildren().addAll(lblLeftRunwayNumber, lblLeftLetter);
        }
        else {
          runwayStrip.getChildren().addAll(lblRightRunwayNumber, lblRightLetter);
        }
      }
    }

    // Add the components to main runway view:
    runwayPane.getChildren().add(polyClearedArea);
    runwayPane.getChildren().add(runwayStrip);

    // Show obstacle data:
    if (obstacleChosen) {
      ObstaclePane obstaclePane = new ObstaclePane(true);
      StackPane.setAlignment(obstaclePane, Pos.CENTER_RIGHT);
      StackPane.setMargin(obstaclePane, new Insets(0, 140, 0, 0));
      runwayPane.getChildren().add(obstaclePane);
    }

    // Draw parameters on the runway view:
    if (methodChosen) {
      int method = cbMethods.getSelectionModel().getSelectedIndex();
      switch (method) {
        case 1->drawArrows(true, true, true);
        case 2->drawArrows(true, true, false);
        case 3->drawArrows(false, true, true);
        case 4->drawArrows(false, true, false);
      }
    }
  }

  /**
   * Display UI components for the side-on view
   */
  public void drawSideOn() {
    // Disable the Rotate button:
    runwayPane.getChildren().clear();
    runwayPane.getStyleClass().clear();
    runwayPane.getStyleClass().add("polygon");
    compasPane.setVisible(false);

    topDown = false;

    btnRotate.setDisable(true);
    btnRotate10.setDisable(true);
    rotate.setAngle(0);
    rotateAngle = 0;
    rotateOrReturn = true;
    btnRotate.setText("Rotate to Compas location");

    // Draw the side-on runway:

    Line lineRunway = new Line(0, 200, 600, 200);
    lineRunway.setStrokeWidth(10);

    lblLeftRunwayNumber.setText(leftPrefix + left);
    lblLeftRunwayNumber.setRotate(0);
    lblLeftRunwayNumber.getStyleClass().add("runway-number");
    lblLeftLetter.setText(leftLetter);
    lblLeftLetter.setRotate(0);
    lblLeftLetter.getStyleClass().add("runway-number");

    StackPane.setAlignment(lblLeftRunwayNumber, Pos.CENTER_LEFT);
    StackPane.setMargin(lblLeftRunwayNumber, new Insets(175, 0, 205, 0));
    StackPane.setAlignment(lblLeftLetter, Pos.CENTER_LEFT);
    StackPane.setMargin(lblLeftLetter, new Insets(205, 0, 175, 3));

    lblRightRunwayNumber.setText(String.valueOf(right));
    lblRightRunwayNumber.setRotate(0);
    lblRightRunwayNumber.getStyleClass().add("runway-number");
    lblRightLetter.setText(rightLetter);
    lblRightLetter.setRotate(0);
    lblRightLetter.getStyleClass().add("runway-number");

    StackPane.setAlignment(lblRightRunwayNumber, Pos.CENTER_RIGHT);
    StackPane.setMargin(lblRightRunwayNumber, new Insets(175, 0, 205, 0));
    StackPane.setAlignment(lblRightLetter, Pos.CENTER_RIGHT);
    StackPane.setMargin(lblRightLetter, new Insets(205, 5, 175, 0));

    // Add the components to main runway view:
    runwayPane.getChildren().add(lineRunway);

    if (!methodChosen) {
      if (view == 0) {
        runwayPane.getChildren().addAll(lblLeftRunwayNumber, lblLeftLetter);
        runwayPane.getChildren().addAll(lblRightRunwayNumber, lblRightLetter);
      } else {
        if (inputManager.getCurrentRunway().getDirection() == 0) {
          runwayPane.getChildren().addAll(lblLeftRunwayNumber, lblLeftLetter);
        }
        else {
          runwayPane.getChildren().addAll(lblRightRunwayNumber, lblRightLetter);
        }
      }
    }

    // Show obstacle data:
    if (obstacleChosen) {
      ObstaclePane obstaclePane = new ObstaclePane(false);
      StackPane.setAlignment(obstaclePane, Pos.TOP_RIGHT);
      StackPane.setMargin(obstaclePane, new Insets(172, 145, 0, 0));
      runwayPane.getChildren().add(obstaclePane);
    }

    // Draw parameters on the runway view:
    if (methodChosen) {
      int method = cbMethods.getSelectionModel().getSelectedIndex();
      switch (method) {
        case 1->drawArrows(true, false, true);
        case 2->drawArrows(true, false, false);
        case 3->drawArrows(false, false, true);
        case 4->drawArrows(false, false, false);
      }
    }
  }

  /*
   * Display compass on runway view:
   */
  public void drawCompas() {
    CompasPane compasPane = new CompasPane();
    StackPane.setAlignment(compasPane, Pos.BOTTOM_RIGHT);
    StackPane.setMargin(compasPane, new Insets(0, 67, 67, 0));
    this.compasPane = compasPane;
  }

  /**
   * Display parameters on the runway view:
   */
  public void drawArrows(boolean landing, boolean top, boolean method1) {
    rectClearWay.setVisible(false);
    rectStopWay.setVisible(false);
    lblClear.setVisible(false);
    lblStop.setVisible(false);

    // Display the landing/take-off direction:
    Line lineDirection = new Line(0, 0, 300, 0);
    StackPane.setAlignment(lineDirection, Pos.TOP_LEFT);
    StackPane.setMargin(lineDirection, new Insets(50, 0, 0,150));

    // Display the runway numbers:
    StackPane pane;
    if (topDown) {
      pane = runwayStrip;
    }
    else {
      pane = runwayPane;
    }
    if (view == 0) {
      pane.getChildren().addAll(lblLeftRunwayNumber, lblLeftLetter);
      pane.getChildren().addAll(lblRightRunwayNumber, lblRightLetter);
    }
    else {
      if (inputManager.getCurrentRunway().getDirection() == 0) {
        pane.getChildren().addAll(lblLeftRunwayNumber, lblLeftLetter);
      } else {
        pane.getChildren().addAll(lblRightRunwayNumber, lblRightLetter);
      }
    }

    Polygon triangleDirection;
    if (landing) {
      if (method1) {
        triangleDirection = leftArrowPointer(10);
        StackPane.setAlignment(triangleDirection, Pos.TOP_LEFT);
        StackPane.setMargin(triangleDirection, new Insets(46, 0, 0, 149));
      } else {
        triangleDirection = rightArrowPointer(10);
        StackPane.setAlignment(triangleDirection, Pos.TOP_RIGHT);
        StackPane.setMargin(triangleDirection, new Insets(46, 149, 0, 0));
      }
    }
    else {
      if (!method1) {
        triangleDirection = leftArrowPointer(10);
        StackPane.setAlignment(triangleDirection, Pos.TOP_LEFT);
        StackPane.setMargin(triangleDirection, new Insets(46, 0, 0,149));
      }
      else {
        triangleDirection = rightArrowPointer(10);
        StackPane.setAlignment(triangleDirection, Pos.TOP_RIGHT);
        StackPane.setMargin(triangleDirection, new Insets(46, 149, 0,0));
      }
    }

    String direction = "Take-Off";
    if (landing) {
      direction = "Landing";
    }
    Label lblDirection = new Label(direction + " Direction");
    viewLabels.add(lblDirection);
    StackPane.setAlignment(lblDirection, Pos.TOP_LEFT);
    StackPane.setMargin(lblDirection, new Insets(30, 0, 0, 250));

    boolean unequal1 = inputManager.getCurrentRunway().getTora() != inputManager.getCurrentRunway().getToda();
    boolean unequal2 = inputManager.getCurrentRunway().getTora() != inputManager.getCurrentRunway().getAsda();

    // Display the TORA and TODA:
    if (landing) {
      Line lineLeftTora;
      Line lineRightTora;
      if (top) {
        lineLeftTora = new Line(0, 0, 0, 80);
        lineRightTora = new Line(0, 0, 0, 80);
      } else {
        lineLeftTora = new Line(0, 0, 0, 100);
        lineRightTora = new Line(0, 0, 0, 100);
      }
      StackPane.setAlignment(lineLeftTora, Pos.TOP_LEFT);
      StackPane.setMargin(lineLeftTora, new Insets(95, 0, 0, 50));
      StackPane.setAlignment(lineRightTora, Pos.TOP_RIGHT);
      StackPane.setMargin(lineRightTora, new Insets(95, 50, 0, 0));

      Line lineToda;
      Polygon triangleLeftToda;
      Polygon triangleRightToda;
      Label lblToda = new Label("TODA = " + df.format(inputManager.getCurrentRunway().getToda()) + " m");
      StackPane.setAlignment(lblToda, Pos.TOP_LEFT);
      if (!unequal1) {
        lineToda = new Line(0, 0, 500, 0);
        StackPane.setAlignment(lineToda, Pos.TOP_CENTER);
        StackPane.setMargin(lineToda, new Insets(105, 0, 0, 0));

        triangleLeftToda = leftArrowPointer(5);
        StackPane.setAlignment(triangleLeftToda, Pos.TOP_LEFT);
        StackPane.setMargin(triangleLeftToda, new Insets(103, 0, 0, 51));

        triangleRightToda = rightArrowPointer(5);
        StackPane.setAlignment(triangleRightToda, Pos.TOP_RIGHT);
        StackPane.setMargin(triangleRightToda, new Insets(103, 51, 0, 0));

        StackPane.setMargin(lblToda, new Insets(90, 0, 0, 300));
      }
      else {
        if (method1) {
          drawClearWay(true, topDown);
          lineLeftTora.setEndY(60);
          StackPane.setMargin(lineLeftTora, new Insets(115, 0, 0, 50));

          lineToda = new Line(0, 0, 550, 0);
          StackPane.setAlignment(lineToda, Pos.TOP_CENTER);
          StackPane.setMargin(lineToda, new Insets(105, 50, 0, 0));

          triangleLeftToda = leftArrowPointer(5);
          StackPane.setAlignment(triangleLeftToda, Pos.TOP_LEFT);
          StackPane.setMargin(triangleLeftToda, new Insets(103, 0, 0, 0));

          triangleRightToda = rightArrowPointer(5);
          StackPane.setAlignment(triangleRightToda, Pos.TOP_RIGHT);
          StackPane.setMargin(triangleRightToda, new Insets(103, 51, 0, 0));

          StackPane.setMargin(lblToda, new Insets(90, 0, 0, 200));
        }
        else {
          drawClearWay(false, topDown);
          lineRightTora.setEndY(60);
          StackPane.setMargin(lineRightTora, new Insets(115, 50, 0, 0));

          lineToda = new Line(0, 0, 550, 0);
          StackPane.setAlignment(lineToda, Pos.TOP_CENTER);
          StackPane.setMargin(lineToda, new Insets(105, 0, 0, 50));

          triangleLeftToda = leftArrowPointer(5);
          StackPane.setAlignment(triangleLeftToda, Pos.TOP_LEFT);
          StackPane.setMargin(triangleLeftToda, new Insets(103, 0, 0, 51));

          triangleRightToda = rightArrowPointer(5);
          StackPane.setAlignment(triangleRightToda, Pos.TOP_RIGHT);
          StackPane.setMargin(triangleRightToda, new Insets(103, 0, 0, 0));

          StackPane.setMargin(lblToda, new Insets(90, 0, 0, 400));
        }
      }

      viewLabels.add(lblToda);

      // Display ASDA:
      Line lineAsda;
      Polygon triangleLeftAsda;
      Polygon triangleRightAsda;
      Label lblAsda = new Label("ASDA = " + df.format(inputManager.getCurrentRunway().getAsda()) + " m");
      StackPane.setAlignment(lblAsda, Pos.TOP_LEFT);
      if (!unequal2) {
        lineAsda = new Line(0, 0, 500, 0);
        StackPane.setAlignment(lineAsda, Pos.TOP_CENTER);
        StackPane.setMargin(lineAsda, new Insets(125, 0, 0, 0));

        triangleLeftAsda = leftArrowPointer(5);
        StackPane.setAlignment(triangleLeftAsda, Pos.TOP_LEFT);
        StackPane.setMargin(triangleLeftAsda, new Insets(123, 0, 0, 51));

        triangleRightAsda = rightArrowPointer(5);
        StackPane.setAlignment(triangleRightAsda, Pos.TOP_RIGHT);
        StackPane.setMargin(triangleRightAsda, new Insets(123, 51, 0, 0));

        StackPane.setMargin(lblAsda, new Insets(110, 0, 0, 300));
      }
      else {
        if (method1) {
          drawStopWay(true, topDown);
          if (unequal1) {
            lineLeftTora.setEndY(40);
            StackPane.setMargin(lineLeftTora, new Insets(135, 0, 0, 50));
          }

          Line lineLeftAsda = new Line(0, 0, 0, 60);
          StackPane.setAlignment(lineLeftAsda, Pos.TOP_LEFT);
          StackPane.setMargin(lineLeftAsda, new Insets(115, 0, 0, 25));
          runwayPane.getChildren().add(lineLeftAsda);

          lineAsda = new Line(0, 0, 525, 0);
          StackPane.setAlignment(lineAsda, Pos.TOP_LEFT);
          StackPane.setMargin(lineAsda, new Insets(125, 0, 0, 25));

          triangleLeftAsda = leftArrowPointer(5);
          StackPane.setAlignment(triangleLeftAsda, Pos.TOP_LEFT);
          StackPane.setMargin(triangleLeftAsda, new Insets(123, 0, 0, 25));

          triangleRightAsda = rightArrowPointer(5);
          StackPane.setAlignment(triangleRightAsda, Pos.TOP_RIGHT);
          StackPane.setMargin(triangleRightAsda, new Insets(123, 51, 0, 0));

          StackPane.setMargin(lblAsda, new Insets(110, 0, 0, 250));
        }
        else {
          drawStopWay(false, topDown);
          if (unequal1) {
            lineRightTora.setEndY(40);
            StackPane.setMargin(lineRightTora, new Insets(135, 50, 0, 0));
          }

          Line lineRightAsda = new Line(0, 0, 0, 60);
          StackPane.setAlignment(lineRightAsda, Pos.TOP_RIGHT);
          StackPane.setMargin(lineRightAsda, new Insets(115, 25, 0, 0));
          runwayPane.getChildren().add(lineRightAsda);

          lineAsda = new Line(0, 0, 525, 0);
          StackPane.setAlignment(lineAsda, Pos.TOP_RIGHT);
          StackPane.setMargin(lineAsda, new Insets(125, 25, 0, 0));

          triangleLeftAsda = leftArrowPointer(5);
          StackPane.setAlignment(triangleLeftAsda, Pos.TOP_LEFT);
          StackPane.setMargin(triangleLeftAsda, new Insets(123, 0, 0, 51));

          triangleRightAsda = rightArrowPointer(5);
          StackPane.setAlignment(triangleRightAsda, Pos.TOP_RIGHT);
          StackPane.setMargin(triangleRightAsda, new Insets(123, 25, 0, 0));

          StackPane.setMargin(lblAsda, new Insets(110, 0, 0, 350));
        }
      }

      viewLabels.add(lblAsda);

      Line lineTora = new Line(0, 0, 500, 0);
      StackPane.setAlignment(lineTora, Pos.TOP_CENTER);
      StackPane.setMargin(lineTora, new Insets(145, 0, 0, 0));

      Polygon triangleLeftTora = leftArrowPointer(5);
      StackPane.setAlignment(triangleLeftTora, Pos.TOP_LEFT);
      StackPane.setMargin(triangleLeftTora, new Insets(143, 0, 0, 51));

      Polygon triangleRightTora = rightArrowPointer(5);
      StackPane.setAlignment(triangleRightTora, Pos.TOP_RIGHT);
      StackPane.setMargin(triangleRightTora, new Insets(143, 51, 0, 0));

      Label lblTora = new Label("TORA = " + df.format(inputManager.getCurrentRunway().getTora()) + " m");
      StackPane.setAlignment(lblTora, Pos.TOP_LEFT);
      StackPane.setMargin(lblTora, new Insets(130, 0, 0, 300));

      viewLabels.add(lblTora);

      runwayPane.getChildren().addAll(lineAsda, lblAsda, triangleLeftAsda, triangleRightAsda);
      runwayPane.getChildren().addAll(lineLeftTora, lineRightTora, lineTora, lblTora, triangleLeftTora, triangleRightTora);
      runwayPane.getChildren().addAll(lineToda, lblToda, triangleLeftToda, triangleRightToda);
    }

    // Display LDA:
    Line lineLeftLda;
    Line lineRightLda;
    if (top) {
      lineLeftLda = new Line(0, 0, 0, 25);
      lineRightLda = new Line(0, 0, 0, 55);
    }
    else {
      lineLeftLda = new Line(0, 0, 0, 45);
      lineRightLda = new Line(0, 0, 0, 85);
    }
    StackPane.setAlignment(lineLeftLda, Pos.BOTTOM_LEFT);
    StackPane.setMargin(lineLeftLda, new Insets(0, 0, 150, 50));
    StackPane.setAlignment(lineRightLda, Pos.BOTTOM_LEFT);
    StackPane.setMargin(lineRightLda, new Insets(0, 0, 120, 300));

    Line lineLda = new Line(0, 0, 250, 0);
    StackPane.setAlignment(lineLda, Pos.BOTTOM_LEFT);
    StackPane.setMargin(lineLda, new Insets(0, 0, 155,50));

    Polygon triangleLeftLda = leftArrowPointer(5);
    StackPane.setAlignment(triangleLeftLda, Pos.BOTTOM_LEFT);
    StackPane.setMargin(triangleLeftLda, new Insets(0, 0, 153,51));

    Polygon triangleRightLda = rightArrowPointer(5);
    StackPane.setAlignment(triangleRightLda, Pos.BOTTOM_LEFT);
    StackPane.setMargin(triangleRightLda, new Insets(0, 0, 153,296));

    Label lblLda;
    if (landing) {
      lblLda = new Label("LDA = " + df.format(inputManager.getCurrentRunway().getLda()) + " m");
      StackPane.setAlignment(lblLda, Pos.BOTTOM_LEFT);
      StackPane.setMargin(lblLda, new Insets(0, 0, 140, 150));
    }
    else {
      String str;
      if (method1 || (!unequal1 && !unequal2)) {
        str = "TORA = TODA = ASDA = " + df.format(inputManager.getCurrentRunway().getTora()) + " m";
      }
      else {
        str = "TORA = " + df.format(inputManager.getCurrentRunway().getTora()) + " m";

        Line lineToda2 = new Line(0, 0, 250, 0);
        StackPane.setAlignment(lineToda2, Pos.BOTTOM_LEFT);
        StackPane.setMargin(lineToda2, new Insets(0, 0, 115, 50));
        runwayPane.getChildren().add(lineToda2);

        Label lblToda2 = new Label("TODA = " + df.format(inputManager.getCurrentRunway().getToda()) + " m");
        StackPane.setAlignment(lblToda2, Pos.TOP_LEFT);
        StackPane.setMargin(lblToda2, new Insets(285, 0, 0, 100));
        viewLabels.add(lblToda2);
        runwayPane.getChildren().add(lblToda2);

        Line lineAsda2 = new Line(0, 0, 250, 0);
        StackPane.setAlignment(lineAsda2, Pos.BOTTOM_LEFT);
        StackPane.setMargin(lineAsda2, new Insets(0, 0, 135, 50));
        runwayPane.getChildren().add(lineAsda2);

        Label lblAsda2 = new Label("ASDA = " + df.format(inputManager.getCurrentRunway().getAsda()) + " m");
        StackPane.setAlignment(lblAsda2, Pos.TOP_LEFT);
        StackPane.setMargin(lblAsda2, new Insets(265, 0, 0, 100));
        viewLabels.add(lblAsda2);
        runwayPane.getChildren().add(lblAsda2);

        if (!topDown) {
          lineRightLda.setEndY(115);
          StackPane.setMargin(lineRightLda, new Insets(0, 0, 110, 300));
        }
        else {
          lineRightLda.setEndY(65);
          StackPane.setMargin(lineRightLda, new Insets(0, 0, 110, 300));
        }

        Polygon triangleLeftAsda = leftArrowPointer(5);
        StackPane.setAlignment(triangleLeftAsda, Pos.BOTTOM_LEFT);
        StackPane.setMargin(triangleLeftAsda, new Insets(0, 0, 133,51));
        runwayPane.getChildren().add(triangleLeftAsda);

        Polygon triangleRightAsda = rightArrowPointer(5);
        StackPane.setAlignment(triangleRightAsda, Pos.BOTTOM_LEFT);
        StackPane.setMargin(triangleRightAsda, new Insets(0, 0, 133,296));
        runwayPane.getChildren().add(triangleRightAsda);

        Polygon triangleLeftToda = leftArrowPointer(5);
        StackPane.setAlignment(triangleLeftToda, Pos.BOTTOM_LEFT);
        StackPane.setMargin(triangleLeftToda, new Insets(0, 0, 113,51));
        runwayPane.getChildren().add(triangleLeftToda);

        Polygon triangleRightToda = rightArrowPointer(5);
        StackPane.setAlignment(triangleRightToda, Pos.BOTTOM_LEFT);
        StackPane.setMargin(triangleRightToda, new Insets(0, 0, 113,296));
        runwayPane.getChildren().add(triangleRightToda);

        // Display stop way and clear way:
        if (unequal1) {
          drawClearWay(true, topDown);

          if (!unequal2) {
            lineLeftLda.setEndY(45);
            StackPane.setMargin(lineLeftLda, new Insets(0, 0, 130, 50));
          }

          lineToda2.setEndX(300);
          StackPane.setMargin(lineToda2, new Insets(0, 0, 115, 0));
          StackPane.setMargin(lblToda2, new Insets(285, 0, 0, 50));
          StackPane.setMargin(triangleLeftToda, new Insets(0, 0, 113,1));
        }
        if (unequal2) {
          drawStopWay(true, topDown);

          if (!unequal1) {
            lineLeftLda.setEndY(65);
            StackPane.setMargin(lineLeftLda, new Insets(0, 0, 110, 50));
          }

          lineAsda2.setEndX(275);
          StackPane.setMargin(lineAsda2, new Insets(0, 0, 135, 25));
          StackPane.setMargin(lblAsda2, new Insets(265, 0, 0, 75));
          StackPane.setMargin(triangleLeftAsda, new Insets(0, 0, 133,26));

          Line lineLeftAsda2 = new Line(0, 0, 0, 45);
          StackPane.setAlignment(lineLeftAsda2, Pos.BOTTOM_LEFT);
          StackPane.setMargin(lineLeftAsda2, new Insets(0, 0, 130, 25));
          runwayPane.getChildren().add(lineLeftAsda2);
        }

      }
      lblLda = new Label(str);
      StackPane.setAlignment(lblLda, Pos.TOP_LEFT);
      if (top) {
        StackPane.setMargin(lblLda, new Insets(245, 0, 0, 100));
      }
      else {
        StackPane.setMargin(lblLda, new Insets(250, 0, 0, 100));
      }
    }
    viewLabels.add(lblLda);

    // Display RESA:
    Line lineLeftResa;
    Line lineRightResa;
    if (top) {
      lineRightResa  = new Line(0, 0, 0, 55);
      StackPane.setAlignment(lineRightResa, Pos.BOTTOM_RIGHT);
      StackPane.setMargin(lineRightResa, new Insets(0, 170, 120,0));

      if (method1)  {
        lineLeftResa  = new Line(0, 0, 0, 75);
        StackPane.setAlignment(lineLeftResa, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(lineLeftResa, new Insets(0, 250, 95,0));
      }
      else {
        lineLeftResa  = new Line(0, 0, 0, 25);
        StackPane.setAlignment(lineLeftResa, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(lineLeftResa, new Insets(0, 250, 150,0));
      }
    }
    else {
      lineRightResa  = new Line(0, 0, 0, 85);
      StackPane.setAlignment(lineRightResa, Pos.BOTTOM_RIGHT);
      StackPane.setMargin(lineRightResa, new Insets(0, 170, 120,0));

      if (method1)  {
        lineLeftResa  = new Line(0, 0, 0, 105);
        StackPane.setAlignment(lineLeftResa, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(lineLeftResa, new Insets(0, 250, 95,0));
      }
      else {
        lineLeftResa  = new Line(0, 0, 0, 45);
        StackPane.setAlignment(lineLeftResa, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(lineLeftResa, new Insets(0, 250, 150,0));
      }
    }

    Line lineResa = new Line(0, 0, 80, 0);
    StackPane.setAlignment(lineResa, Pos.BOTTOM_RIGHT);
    StackPane.setMargin(lineResa, new Insets(0, 170, 155,0));

    Polygon triangleLeftResa = leftArrowPointer(5);
    StackPane.setAlignment(triangleLeftResa, Pos.BOTTOM_RIGHT);
    StackPane.setMargin(triangleLeftResa, new Insets(0, 246, 153,0));

    Polygon triangleRightResa = rightArrowPointer(5);
    StackPane.setAlignment(triangleRightResa, Pos.BOTTOM_RIGHT);
    StackPane.setMargin(triangleRightResa, new Insets(0, 171, 153,0));

    Label lblResa = new Label("RESA");
    viewLabels.add(lblResa);
    StackPane.setAlignment(lblResa, Pos.BOTTOM_RIGHT);
    StackPane.setMargin(lblResa, new Insets(0, 195, 155, 0));

    Label lblResaValue = new Label((int) inputManager.getCurrentRunway().getResa() + " m");
    StackPane.setAlignment(lblResaValue, Pos.BOTTOM_RIGHT);
    StackPane.setMargin(lblResaValue, new Insets(0, 185, 140, 0));
    viewLabels.add(lblResaValue);

    // Display new strip end and blast allowance:
    Line lineStripEnd;
    Line lineBlastAllowance;
    Polygon triangleLeftStripEnd = leftArrowPointer(5);
    StackPane.setAlignment(triangleLeftStripEnd, Pos.BOTTOM_RIGHT);
    Label lblBlastAllowance = new Label("Blast Allowance");
    viewLabels.add(lblBlastAllowance);
    StackPane.setAlignment(lblBlastAllowance, Pos.BOTTOM_RIGHT);
    Label lblBlastAllowanceValue = new Label((int) inputManager.getCurrentRunway().getBlastAllowance() + " m");
    viewLabels.add(lblBlastAllowanceValue);
    StackPane.setAlignment(lblBlastAllowanceValue, Pos.BOTTOM_RIGHT);
    Polygon triangleLeftBlastAllowance = leftArrowPointer(5);
    StackPane.setAlignment(triangleLeftBlastAllowance, Pos.BOTTOM_RIGHT);
    Polygon triangleRightBlastAllowance = rightArrowPointer(5);
    StackPane.setAlignment(triangleRightBlastAllowance, Pos.BOTTOM_RIGHT);
    if (top) {
      lineStripEnd = new Line(0, 0, 50, 0);
      StackPane.setMargin(triangleLeftStripEnd, new Insets(0, 296, 153, 0));

      lineBlastAllowance = new Line(0, 0, 130, 0);
      StackPane.setMargin(lblBlastAllowance, new Insets(0, 193, 125, 0));
      StackPane.setMargin(lblBlastAllowanceValue, new Insets(0, 213, 110, 0));

      StackPane.setMargin(triangleLeftBlastAllowance, new Insets(0, 296, 123, 0));
      StackPane.setMargin(triangleRightBlastAllowance, new Insets(0, 171, 123, 0));
    }
    else {
      lineStripEnd = new Line(0, 0, 60, 0);
      StackPane.setMargin(triangleLeftStripEnd, new Insets(0, 306, 153, 0));

      lineBlastAllowance = new Line(0, 0, 140, 0);
      StackPane.setMargin(lblBlastAllowance, new Insets(0, 193, 125, 0));
      StackPane.setMargin(lblBlastAllowanceValue, new Insets(0, 213, 110, 0));

      StackPane.setMargin(triangleLeftBlastAllowance, new Insets(0, 306, 123, 0));
      StackPane.setMargin(triangleRightBlastAllowance, new Insets(0, 171, 123, 0));
    }
    StackPane.setAlignment(lineStripEnd, Pos.BOTTOM_RIGHT);
    StackPane.setMargin(lineStripEnd, new Insets(0, 250, 155, 0));
    StackPane.setAlignment(lineBlastAllowance, Pos.BOTTOM_RIGHT);
    StackPane.setMargin(lineBlastAllowance, new Insets(0, 170, 125, 0));

    Polygon triangleRightStripEnd = rightArrowPointer(5);
    StackPane.setAlignment(triangleRightStripEnd, Pos.BOTTOM_RIGHT);
    StackPane.setMargin(triangleRightStripEnd, new Insets(0, 251, 153,0));

    Label lblStripEnd = new Label("new strip end");
    StackPane.setAlignment(lblStripEnd, Pos.BOTTOM_RIGHT);
    StackPane.setMargin(lblStripEnd, new Insets(0, 252, 157, 0));
    Label lblStripEndValue = new Label((int) inputManager.getCurrentRunway().getNewStripEnd() + " m");
    viewLabels.add(lblStripEndValue);
    StackPane.setAlignment(lblStripEndValue, Pos.BOTTOM_RIGHT);
    StackPane.setMargin(lblStripEndValue, new Insets(0, 262, 140, 0));

    // Display displaced threshold:
    Line lineDisplacedThreshold;
    Polygon triangleDisplacedThreshold = downArrowPointer(10);
    triangleDisplacedThreshold.getStyleClass().add("displaced-threshold-triangle");
    StackPane.setAlignment(triangleDisplacedThreshold, Pos.TOP_LEFT);
    if (top) {
      lblStripEnd.setStyle("-fx-font-size: 8.5;");
      lineDisplacedThreshold = new Line(0, 0, 0, 48);

      StackPane.setMargin(triangleDisplacedThreshold, new Insets(210, 0, 0, 295));
    }
    else {
      lblStripEnd.setStyle("-fx-font-size: 10;");
      lineDisplacedThreshold = new Line(0, 0, 0, 40);

      StackPane.setMargin(triangleDisplacedThreshold, new Insets(200, 0, 0, 295));
    }
    viewLabels.add(lblStripEnd);
    lineDisplacedThreshold.getStyleClass().add("displaced-threshold-line");
    StackPane.setAlignment(lineDisplacedThreshold, Pos.TOP_LEFT);
    StackPane.setMargin(lineDisplacedThreshold, new Insets(167, 0, 0, 300));

    Label lblDisplacedThreshold = new Label("Displaced Threshold");
    viewLabels.add(lblDisplacedThreshold);
    lblDisplacedThreshold.getStyleClass().add("displaced-threshold-label");
    StackPane.setAlignment(lblDisplacedThreshold, Pos.TOP_LEFT);
    StackPane.setMargin(lblDisplacedThreshold, new Insets(155, 0, 0, 255));

    // Display obstacle height:
    Line lineHeight;
    Polygon triangleUpHeight = upArrowPointer(5);
    StackPane.setAlignment(triangleUpHeight, Pos.TOP_RIGHT);
    Polygon triangleDownHeight = downArrowPointer(5);
    StackPane.setAlignment(triangleDownHeight, Pos.TOP_RIGHT);
    Label lblHeight = new Label("h");
    viewLabels.add(lblHeight);
    if (top) {
      lineHeight = new Line(0, 0, 0, 35);

      StackPane.setAlignment(lineHeight, Pos.CENTER_RIGHT);
      StackPane.setAlignment(lblHeight, Pos.CENTER_RIGHT);

      StackPane.setMargin(lineHeight, new Insets(0, 138, 0,0));
      StackPane.setMargin(triangleUpHeight, new Insets(180, 136, 0,0));
      StackPane.setMargin(triangleDownHeight, new Insets(214, 136, 0,0));
      StackPane.setMargin(lblHeight, new Insets(0, 130, 0, 0));

      // Display obstacle length:
      Line lineLength = new Line(0, 0, 30, 0);
      Polygon triangleLeftLength = leftArrowPointer(5);
      StackPane.setAlignment(triangleLeftLength, Pos.TOP_RIGHT);
      Polygon triangleRightLength = rightArrowPointer(5);
      StackPane.setAlignment(triangleRightLength, Pos.TOP_RIGHT);
      Label lblLength = new Label("l");
      viewLabels.add(lblLength);

      StackPane.setAlignment(lineLength, Pos.TOP_RIGHT);
      StackPane.setAlignment(lblLength, Pos.TOP_RIGHT);

      StackPane.setMargin(lineLength, new Insets(180, 141, 0,0));
      StackPane.setMargin(triangleLeftLength, new Insets(178, 172, 0,0));
      StackPane.setMargin(triangleRightLength, new Insets(178, 139, 0,0));
      StackPane.setMargin(lblLength, new Insets(165, 156, 0, 0));

      runwayPane.getChildren().addAll(lineLength, triangleLeftLength, triangleRightLength, lblLength);
    }
    else {
      lineHeight = new Line(0, 0, 0, 25);

      StackPane.setAlignment(lineHeight, Pos.TOP_RIGHT);
      StackPane.setAlignment(lblHeight, Pos.TOP_RIGHT);

      StackPane.setMargin(lineHeight, new Insets(170, 138, 0,0));
      StackPane.setMargin(triangleUpHeight, new Insets(170, 136, 0,0));
      StackPane.setMargin(triangleDownHeight, new Insets(194, 136, 0,0));
      StackPane.setMargin(lblHeight, new Insets(175, 127, 0, 0));
    }

    // Display h * slope:
    if (method1) {
      Line lineRightHSlope;

      if (!top) {

        Line lineAlsTocs = new Line(103, 0, 0, 22);
        StackPane.setAlignment(lineAlsTocs, Pos.TOP_RIGHT);
        int slope = (int) inputManager.getCurrentRunway().getSlopeValue();
        Label lblSlope = new Label("1:" + slope + " slope");
        Label lblAlsTocs;
        if (landing) {
          lblAlsTocs = new Label("ALS");
        }
        else {
          lblAlsTocs = new Label("TOCS");
        }
        lblAlsTocs.getStyleClass().add("slope-label");
        StackPane.setAlignment(lblSlope, Pos.TOP_RIGHT);
        StackPane.setAlignment(lblAlsTocs, Pos.TOP_RIGHT);
        StackPane.setMargin(lineAlsTocs, new Insets(173, 145, 0, 0));
        StackPane.setMargin(lblAlsTocs, new Insets(167, 185, 0, 0));
        StackPane.setMargin(lblSlope, new Insets(175, 52, 0, 0));
        runwayPane.getChildren().addAll(lineAlsTocs, lblAlsTocs, lblSlope);

        viewLabels.add(lblSlope);
        viewLabels.add(lblAlsTocs);
      }

      if (top) {
        lineRightHSlope  = new Line(0, 0, 0, 80);
      }
      else {
        lineRightHSlope  = new Line(0, 0, 0, 100);
      }
      StackPane.setAlignment(lineRightHSlope, Pos.BOTTOM_RIGHT);
      StackPane.setMargin(lineRightHSlope, new Insets(0, 140, 95,0));

      Line lineHSlope = new Line(0, 0, 105, 0);
      StackPane.setAlignment(lineHSlope, Pos.BOTTOM_RIGHT);
      StackPane.setMargin(lineHSlope, new Insets(0, 140, 100,0));

      Polygon triangleLeftHSlope = leftArrowPointer(5);
      StackPane.setAlignment(triangleLeftHSlope, Pos.BOTTOM_RIGHT);
      StackPane.setMargin(triangleLeftHSlope, new Insets(0, 246, 98,0));

      Polygon triangleRightHSlope = rightArrowPointer(5);
      StackPane.setAlignment(triangleRightHSlope, Pos.BOTTOM_RIGHT);
      StackPane.setMargin(triangleRightHSlope, new Insets(0, 141, 98,0));

      double hSlope = inputManager.getCurrentRunway().getCurrentObstacle().getHeight() * inputManager.getCurrentRunway().getSlopeValue();
      Label lblHSlope = new Label("h * slope = " + df.format(hSlope) + " m");
      StackPane.setAlignment(lblHSlope, Pos.BOTTOM_RIGHT);
      StackPane.setMargin(lblHSlope, new Insets(0, 141, 82, 0));

      viewLabels.add(lblHSlope);

      runwayPane.getChildren().addAll(lineRightHSlope, lineHSlope, lblHSlope, triangleLeftHSlope, triangleRightHSlope);
    }

    runwayPane.getChildren().addAll(lineDirection, lblDirection, triangleDirection);
    runwayPane.getChildren().addAll(lineLeftLda, lineRightLda, lineLda, lblLda, triangleLeftLda, triangleRightLda);
    runwayPane.getChildren().addAll(lineLeftResa, lineRightResa, lineResa, lblResa, lblResaValue, triangleLeftResa, triangleRightResa);
    runwayPane.getChildren().addAll(lineStripEnd, lblStripEnd, lblStripEndValue, triangleLeftStripEnd, triangleRightStripEnd);
    runwayPane.getChildren().addAll(lineBlastAllowance, lblBlastAllowance, lblBlastAllowanceValue, triangleLeftBlastAllowance, triangleRightBlastAllowance);
    runwayPane.getChildren().addAll(lineDisplacedThreshold, triangleDisplacedThreshold, lblDisplacedThreshold);
    runwayPane.getChildren().addAll(lineHeight, triangleUpHeight, triangleDownHeight, lblHeight);
  }

  /*
   * Draw Clear Way on the runway view:
   */
  public void drawClearWay(boolean left, boolean top) {
    runwayPane.getChildren().remove(rectClearWay);
    // top-down view:
    if (top) {
      rectClearWay = new Rectangle(50, 50);
      lblClear.setText("Clear Way");
    }
    // side-on view:
    else {
      rectClearWay = new Rectangle(50, 70);
      lblClear.setText("Clear\t   Way");
      if (left) {
        StackPane.setMargin(lblLeftRunwayNumber, new Insets(145, 0, 235, 0));
        StackPane.setMargin(lblLeftLetter, new Insets(235, 0, 145, 3));
      }
      else {
        StackPane.setMargin(lblRightRunwayNumber, new Insets(145, 0, 235, 0));
        StackPane.setMargin(lblRightLetter, new Insets(235, 3, 145, 0));
      }
    }
    rectClearWay.getStyleClass().add("border");
    // clear way on the left:
    if (left) {
      StackPane.setAlignment(rectClearWay, Pos.CENTER_LEFT);
      lblClear.setRotate(-90);
      StackPane.setAlignment(lblClear, Pos.CENTER_LEFT);
      StackPane.setMargin(lblClear, new Insets(0, 0, 0, -10));
    }
    // clear way on the right:
    else {
      StackPane.setAlignment(rectClearWay, Pos.CENTER_RIGHT);
      lblClear.setRotate(90);
      StackPane.setAlignment(lblClear, Pos.CENTER_RIGHT);
      StackPane.setMargin(lblClear, new Insets(0, -10 , 0, 0));
    }
    rectClearWay.setVisible(true);
    runwayPane.getChildren().add(rectClearWay);
    lblClear.setVisible(true);
    runwayPane.getChildren().remove(lblClear);
    runwayPane.getChildren().add(lblClear);
  }

  /*
   * Draw Clear Way on the runway view:
   */
  public void drawStopWay(boolean left, boolean top) {
    runwayPane.getChildren().remove(rectStopWay);
    // top-down view:
    if (top) {
      rectStopWay = new Rectangle(25, 45);
      lblStop.setText("Stop Way");
    }
    // side-on view:
    else {
      rectStopWay = new Rectangle(25, 65);
      lblStop.setText("Stop\t Way");
    }
    rectStopWay.getStyleClass().add("border");
    // clear way on the left:
    if (left) {
      StackPane.setAlignment(rectStopWay, Pos.CENTER_LEFT);
      StackPane.setMargin(rectStopWay, new Insets(0, 0, 0, 25));
      lblStop.setRotate(-90);
      StackPane.setAlignment(lblStop, Pos.CENTER_LEFT);
      StackPane.setMargin(lblStop, new Insets(0, 0, 0, 17));
    }
    // clear way on the right:
    else {
      StackPane.setAlignment(rectStopWay, Pos.CENTER_RIGHT);
      StackPane.setMargin(rectStopWay, new Insets(0, 25, 0, 0));
      lblStop.setRotate(90);
      StackPane.setAlignment(lblStop, Pos.CENTER_RIGHT);
      StackPane.setMargin(lblStop, new Insets(0, 17, 0, 0));
    }
    rectStopWay.setVisible(true);
    runwayPane.getChildren().add(rectStopWay);
    lblStop.setVisible(true);
    runwayPane.getChildren().remove(lblStop);
    runwayPane.getChildren().add(lblStop);
  }

  /**
   * Create a VBox of lines that will be used to display top-down view:
   * @return    VBox of lines
   */
  public VBox getLines() {
    VBox boxLines = new VBox();
    boxLines.setSpacing(3);
    for (int i = 0; i < 13; i++) {
      Line line = new Line();
      line.setStartX(0);
      line.setEndX(25);
      line.setStroke(Color.WHITE);
      boxLines.getChildren().add(line);
    }
    return boxLines;
  }

  /**
   * Create a triangle for the left arrow pointer
   * @return    left arrow pointer
   */
  public Polygon leftArrowPointer(double size) {
    return new Polygon(0, size / 2,
        size, 0,
        size, size);
  }

  /**
   * Create a triangle for the right arrow pointer
   * @return    right arrow pointer
   */
  public Polygon rightArrowPointer(double size) {
    return new Polygon(size, size / 2,
        0, 0,
        0, size);
  }

  /**
   * Create a triangle for the down arrow pointer
   * @return    down arrow pointer
   */
  public Polygon downArrowPointer(double size) {
    return new Polygon(0, 0,
        size, 0,
        size / 2, size);
  }

  /**
   * Create a triangle for the up arrow pointer
   * @return    up arrow pointer
   */
  public Polygon upArrowPointer(double size) {
    return new Polygon(size / 2, 0,
        0, size,
        size, size);
  }

  /**
   * Handle when Rotate button is pressed
   * @param event    action event
   */
  public void rotate(ActionEvent event){
    // Rotate the view:
    if (rotateOrReturn) {
      int angle = left * 10 - 90;
      rotate.setAngle(angle);
      rotateOrReturn = false;
      btnRotate.setText("Return to Normal location");
      btnRotate.setStyle("-fx-background-color: #D0FFE0; -fx-border-color: blue;");
      compasPane.getRotatablePane().setRotate(angle);
    }
    // Rotate back to horizontal:
    else {
      rotate.setAngle(0);
      rotateAngle = 0;
      rotateOrReturn = true;
      btnRotate.setText("Rotate to Compas location");
      btnRotate.setStyle(null);
      for (Label label : viewLabels) {
        label.setRotate(0);
      }
      compasPane.getRotatablePane().setRotate(0);
    }
  }

  /**
   * Handle when Rotate button is pressed
   * @param event    action event
   */
  public void rotate10(ActionEvent event){
    // Handle text rotation:
    if (rotateAngle == 90) {
      for (Label label : viewLabels) {
        label.setRotate(180);
      }
    }
    else if (rotateAngle == 270) {
      for (Label label : viewLabels) {
        label.setRotate(0);
      }
    }
    else if (rotateAngle == 360) {
      rotateAngle = 0;
    }
    // Rotate the view:
    rotate.setAngle(rotateAngle + 10);
    compasPane.getRotatablePane().setRotate(rotateAngle + 10);
    rotateAngle += 10;
    btnRotate.setText("Return to Normal location");
    btnRotate.setStyle("-fx-background-color: #D0FFE0; -fx-border-color: blue;");
    rotateOrReturn = false;
  }

}
