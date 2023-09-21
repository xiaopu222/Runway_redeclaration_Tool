package com.example.runway.controller;

import com.example.runway.model.Airport;import com.example.runway.model.Obstacle;
import com.example.runway.model.Runway;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Arrays;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javax.imageio.ImageIO;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;

public class FileManager {

  private final String cwd;

  // Formatter for setting numbers to 1 d.p.
  private final DecimalFormat df = new DecimalFormat("0.0");

  /*
   * Create a new File Manager
   */
  public FileManager() {
    // Set the current working directory:
    cwd = Path.of("").toAbsolutePath().toString();
  }

  /*
   * Retrieve all previously created data (used at app launch)
   * @return list of airports
   */
  public List<Airport> fetchFiles() {
    List<Airport> airports = new ArrayList<>();
    var folder = Path.of(cwd + "/storage_files/");
    if (Files.isDirectory(folder) && Files.exists(folder)) {
      try {
        for (File file : folder.toFile().listFiles()) {
          airports.add(importFile(file.getAbsolutePath()));
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    return airports;
  }

  /*
   * Function for importing an airport from XML
   * @return airport object
   */
  public Airport importFile(String filename) {

      //Get the Document Builder
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      try {
          DocumentBuilder builder = factory.newDocumentBuilder();

          //Get Document
          Document document = builder.parse(filename);

          //Normalize the xml document
          document.getDocumentElement().normalize();

          // Create new airport
          Airport airport = new Airport(document.getDocumentElement().getAttribute("name"));

          // Store airport's data:
          NodeList runwayList = document.getElementsByTagName("runway");
          for(int i = 0; i< runwayList.getLength(); i++){
              Node runwayNode = runwayList.item(i);

              if(runwayNode.getNodeType()== Node.ELEMENT_NODE) {
                  Element runwayElement = (Element) runwayNode;
                  String runwayNumber = runwayElement.getAttribute("runway_designator");
                  String tora = runwayElement.getElementsByTagName("TORA").item(0).getTextContent();
                  String toda = runwayElement.getElementsByTagName("TODA").item(0).getTextContent();
                  String asda = runwayElement.getElementsByTagName("ASDA").item(0).getTextContent();
                  String lda = runwayElement.getElementsByTagName("LDA").item(0).getTextContent();
                  String displaced = runwayElement.getElementsByTagName("displaced_threshold").item(0).getTextContent();

                  Runway runway = new Runway(runwayNumber, Double.parseDouble(tora), Double.parseDouble(toda), Double.parseDouble(asda), Double.parseDouble(lda), Double.parseDouble(displaced));

                  NodeList runwayDetails = runwayNode.getChildNodes();
                  for(int j = 0; j<runwayDetails.getLength();j++){
                    Node detail = runwayDetails.item(j);
                    if(detail.getNodeType() == Node.ELEMENT_NODE) {
                          if(detail.getNodeName().equals("obstacle")) {
                            Element obstacleElement = (Element) detail;
                            String name = obstacleElement.getAttribute("name");
                            String height = obstacleElement.getElementsByTagName("height").item(0).getTextContent();
                            String length = obstacleElement.getElementsByTagName("length").item(0).getTextContent();
                            String distanceThreshold = obstacleElement.getElementsByTagName("distance_threshold").item(0).getTextContent();
                            String distanceCentre = obstacleElement.getElementsByTagName("distance_centerline").item(0).getTextContent();

                            Obstacle obstacle = new Obstacle(name, Double.parseDouble(height), Double.parseDouble(length), Double.parseDouble(distanceCentre), Double.parseDouble(distanceThreshold));
                            runway.addObstacle(obstacle);
                          }
                      }
                  }
                  airport.addRunway(runway);
              }
          }
          return airport;
      } catch (ParserConfigurationException | IOException | SAXException e) {
          throw new RuntimeException(e);
      }
  }

  /**
   * Function for XML export
   * @param file         file (chosen by user)
   * @param airport      an airport object
   */
  public void exportFile(File file, Airport airport) {

    //create a new XML document
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder;

    try{
      docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();

      //create the root element (airport) and add it to the document
      Element airportElem = doc.createElement("airport");
      airportElem.setAttribute("name", airport.getName());
      //get all the runways in an airport
      List<Runway> runways = airport.getRunways();

      for (Runway runway : runways){
        //create a runway element and add it to the airport element
        Element runwayElem = doc.createElement("runway");
        runwayElem.setAttribute("runway_designator", runway.getRunwayNumber());
        airportElem.appendChild(runwayElem);
        //add TORA element
        Element toraElem = doc.createElement("TORA");
        toraElem.appendChild(doc.createTextNode((Double.toString(runway.getDefaultTora()))));
        runwayElem.appendChild(toraElem);
        //add TODA element
        Element todaElem = doc.createElement("TODA");
        todaElem.appendChild(doc.createTextNode((Double.toString(runway.getDefaultToda()))));
        runwayElem.appendChild(todaElem);
        //add ASDA element
        Element asdaElem = doc.createElement("ASDA");
        asdaElem.appendChild(doc.createTextNode((Double.toString(runway.getDefaultAsda()))));
        runwayElem.appendChild(asdaElem);
        //add LDA element
        Element ldaElem = doc.createElement("LDA");
        ldaElem.appendChild(doc.createTextNode((Double.toString(runway.getDefaultLda()))));
        runwayElem.appendChild(ldaElem);
        //add displaced threshold element
        Element displacedElem = doc.createElement("displaced_threshold");
        displacedElem.appendChild(doc.createTextNode((Double.toString(runway.getDisplacedThreshold()))));
        runwayElem.appendChild(displacedElem);
        //get all the obstacle on the runway
        List<Obstacle> obstacles = runway.getObstacles();
        List<String> predefined = new ArrayList<>(Arrays.asList("ob1", "ob2", "ob3", "ob4"));

        for (Obstacle obstacle : obstacles){
          if (!predefined.contains(obstacle.getName())) {
            //create an obstacle element and add it to the runway element
            Element obstacleElem = doc.createElement("obstacle");
            obstacleElem.setAttribute("name", obstacle.getName());
            runwayElem.appendChild(obstacleElem);
            //add height element
            Element heightElem = doc.createElement("height");
            heightElem.appendChild(doc.createTextNode(Double.toString(obstacle.getHeight())));
            obstacleElem.appendChild(heightElem);
            //add length element
            Element lengthElem = doc.createElement("length");
            lengthElem.appendChild(doc.createTextNode(Double.toString(obstacle.getLength())));
            obstacleElem.appendChild(lengthElem);
            //add distance from threshold element
            Element distanceFromThresholdElem = doc.createElement("distance_threshold");
            distanceFromThresholdElem.appendChild(
                doc.createTextNode(Double.toString(obstacle.getDistanceFromThreshold())));
            obstacleElem.appendChild(distanceFromThresholdElem);
            //add distance from center element
            Element distanceFromCenter = doc.createElement("distance_centerline");
            distanceFromCenter.appendChild(
                doc.createTextNode(Double.toString(obstacle.getDistanceCentre())));
            obstacleElem.appendChild(distanceFromCenter);
          }
        }
      }
      doc.appendChild(airportElem);

      //write the document to an XML file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      transformerFactory.setAttribute("indent-number", 2);
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      DOMSource source = new DOMSource(doc);
      //save files to storage_files folder

      StreamResult result = new StreamResult(new FileOutputStream(file));
      transformer.transform(source, result);

    }  catch (ParserConfigurationException | TransformerException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Function for saving an airport
   * @param airport      an airport object
   */
  public void saveAirport(Airport airport) {

    //create a new XML document
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder;

    try{
      docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();

      //create the root element (airport) and add it to the document
      Element airportElem = doc.createElement("airport");
      airportElem.setAttribute("name", airport.getName());
      //get all the runways in an airport
      List<Runway> runways = airport.getRunways();

      for (Runway runway : runways){
        //create a runway element and add it to the airport element
        Element runwayElem = doc.createElement("runway");
        runwayElem.setAttribute("runway_designator", runway.getRunwayNumber());
        airportElem.appendChild(runwayElem);
        //add TORA element
        Element toraElem = doc.createElement("TORA");
        toraElem.appendChild(doc.createTextNode((Double.toString(runway.getTora()))));
        runwayElem.appendChild(toraElem);
        //add TODA element
        Element todaElem = doc.createElement("TODA");
        todaElem.appendChild(doc.createTextNode((Double.toString(runway.getToda()))));
        runwayElem.appendChild(todaElem);
        //add ASDA element
        Element asdaElem = doc.createElement("ASDA");
        asdaElem.appendChild(doc.createTextNode((Double.toString(runway.getAsda()))));
        runwayElem.appendChild(asdaElem);
        //add LDA element
        Element ldaElem = doc.createElement("LDA");
        ldaElem.appendChild(doc.createTextNode((Double.toString(runway.getLda()))));
        runwayElem.appendChild(ldaElem);
        //add displaced threshold element
        Element displacedElem = doc.createElement("displaced_threshold");
        displacedElem.appendChild(doc.createTextNode((Double.toString(runway.getDisplacedThreshold()))));
        runwayElem.appendChild(displacedElem);
        //get all the obstacle on the runway
        List<Obstacle> obstacles = runway.getObstacles();
        List<String> predefined = new ArrayList<>(Arrays.asList("ob1", "ob2", "ob3", "ob4"));

        for (Obstacle obstacle : obstacles){
          if (!predefined.contains(obstacle.getName())) {
            //create an obstacle element and add it to the runway element
            Element obstacleElem = doc.createElement("obstacle");
            obstacleElem.setAttribute("name", obstacle.getName());
            runwayElem.appendChild(obstacleElem);
            //add height element
            Element heightElem = doc.createElement("height");
            heightElem.appendChild(doc.createTextNode(Double.toString(obstacle.getHeight())));
            obstacleElem.appendChild(heightElem);
            //add length element
            Element lengthElem = doc.createElement("length");
            lengthElem.appendChild(doc.createTextNode(Double.toString(obstacle.getLength())));
            obstacleElem.appendChild(lengthElem);
            //add distance from threshold element
            Element distanceFromThresholdElem = doc.createElement("distance_threshold");
            distanceFromThresholdElem.appendChild(
                doc.createTextNode(Double.toString(obstacle.getDistanceFromThreshold())));
            obstacleElem.appendChild(distanceFromThresholdElem);
            //add distance from center element
            Element distanceFromCenter = doc.createElement("distance_centerline");
            distanceFromCenter.appendChild(
                doc.createTextNode(Double.toString(obstacle.getDistanceCentre())));
            obstacleElem.appendChild(distanceFromCenter);
          }
        }
      }
      doc.appendChild(airportElem);

      //write the document to an XML file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      transformerFactory.setAttribute("indent-number", 2);
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      DOMSource source = new DOMSource(doc);

      //save files to storage_files folder
      var folder = Path.of(cwd + "/storage_files/");
      if (!Files.isDirectory(folder)) {
        try {
          Files.createDirectory(folder);
        } catch (IOException e) {
          e.printStackTrace();
          System.exit(-1);
        }
      }

      File file = new File(String.valueOf(Path.of(cwd + "/storage_files/" + airport.getName() + ".xml")));
      StreamResult result = new StreamResult(new FileOutputStream(file));
      transformer.transform(source, result);

    }  catch (ParserConfigurationException | TransformerException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  /*
   * Delete an airport file
   */
  public void deleteAirport(String name) {
    File file = new File(String.valueOf(Path.of(cwd + "/storage_files/" + name + ".xml")));
    if (file.exists()) {
      file.delete();
    }
  }

  /*
   * Export runway view as JPEG
   */
  public void exportAsJPEG(File file, Pane pane) throws IOException {
    WritableImage image = pane.snapshot(new SnapshotParameters(), null);
    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
  }

  /*
   * Export runway view as PNG
   */
  public void exportAsPNG(File file, Pane pane) throws IOException {
    WritableImage image = pane.snapshot(new SnapshotParameters(), null);
    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
  }

  private final String[] methodsStrings = {"none", "Landing over the obstacle", "Landing towards the obstacle", "Take-off towards the obstacle", "Take-off away from the obstacle"};
  /**
   * Method used when exporting airport data into a txt file.
   * **/
  public void exportAsTXT(File file, Airport airport, Runway runway, Obstacle obstacle, int method) throws IOException {
      //Creation of a file, File Writer and a Print Writer
      FileWriter fw = new FileWriter(file);
      PrintWriter pw = new PrintWriter(fw);

      //Writing to the file using formatted strings
      pw.println(String.format("Airport Name: %s %n",airport.getName()));
      pw.println(String.format("Runway: %s %n",runway.getRunwayNumber()));
      String resa = (int) runway.getResa() + "m";
      String blastAllowance = (int) runway.getBlastAllowance() + "m";
      pw.println(String.format("RESA = %-15s Blast Allowance = %-15s",resa,blastAllowance));
      String displacedThreshold = (int) runway.getDisplacedThreshold() + "m";
      pw.println(String.format("Slope = 1:%-12d Displaced Threshold = %-15s",(int) runway.getSlopeValue(),displacedThreshold));
      String newStripEnd = (int) runway.getNewStripEnd() + "m";
      String alsTocs = df.format(runway.getAlsTocs()) + "m";
      pw.println(String.format("New Strip End = %-6s ALS/TOCS = %-15s",newStripEnd,alsTocs));
      pw.println("");
      pw.println(String.format("%-11S %-10S %-10S %-10S %-10S %-10s %-10s","","TORA","TODA","ASDA","LDA","STOP WAY","CLEAR WAY"));
      pw.println(String.format("%-10S  %-10s %-10s %-10s %-10s %-10s %-10s","Original", df.format(runway.getDefaultTora()),df.format(runway.getDefaultToda()),df.format(runway.getDefaultAsda()),df.format(runway.getDefaultLda()),df.format(runway.getDefaultStopWay()),df.format(runway.getDefaultClearWay())));
      pw.println(String.format("%-10S %-10s %-10s %-10s %-10s %-10s %-10s","Re-Declared",df.format(runway.getTora()),df.format(runway.getToda()),df.format(runway.getAsda()),df.format(runway.getLda()),df.format(runway.getStopWay()),df.format(runway.getClearWay())));
      pw.println("");
      pw.println(String.format("Obstacle: %-10s",obstacle.getName()));
      String height = df.format(obstacle.getHeight()) + "m";
      String length = df.format(obstacle.getLength()) + "m";
      pw.println(String.format("Height = %-15s Length = %-15s",height,length));
      String distanceFromCentreLine = df.format(obstacle.getDistanceCentre())+"m";
      pw.println(String.format("Distance From Centre Line = %-15s",distanceFromCentreLine));
      String distanceFromThreshold = df.format(obstacle.getDistanceFromThreshold())+"m";
      pw.println(String.format("Distance From Threshold = %-15s",distanceFromThreshold));
      pw.println("");
      pw.println(String.format("Landing/Take-off Method: %S",methodsStrings[method]));

      //Close to save the file
      pw.close();
  }

}
