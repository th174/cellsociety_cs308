/**
 * Created by th174 on 1/29/2017.
 */

import CellSociety.Abstract_Cell;
import CellSociety.SimulationGrid;
import CellSociety.UI.CellSocietyView;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

public class CellSocietyMain extends Application {
    public static final double SIZE = 900;
    public static final String TITLE = "Cell Society";
    private double framesPerSecond = 3;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        SimulationGrid<? extends Abstract_Cell> mySimulationGrid = readXML(getParameters().getUnnamed().get(0));
        CellSocietyView myUI = new CellSocietyView(mySimulationGrid, framesPerSecond, SIZE, SIZE);
        primaryStage.setResizable(false);
        primaryStage.setScene(myUI.getScene());
        primaryStage.sizeToScene();
        primaryStage.setTitle(TITLE);
        primaryStage.show();
    }

    /**
     * Read and parse xml file using DOM
     *
     * @param XMLFile name of XML file
     * @return generated SimulationGrid of cells based on XML input
     */
    private SimulationGrid<? extends Abstract_Cell> readXML(String XMLFile) {
        try {
            Document file = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(XMLFile);
            Element root = file.getDocumentElement();
            //set up grid of references to constructorParams
            String[][][] grid = new String[Integer.parseInt(root.getAttribute("width"))][Integer.parseInt(root.getAttribute("height"))][0];
            try {
                framesPerSecond = Double.parseDouble(root.getAttribute("fps"));
            } catch (Exception e) {
            }
            String simulationType = root.getAttribute("type");
            Class<? extends Abstract_Cell> cellType = (Class<? extends Abstract_Cell>) Class.forName("CellSociety." + simulationType + "." + simulationType + "_Cell");
            //instantiate cells
            NodeList cells = file.getElementsByTagName("Cell");
            for (int i = 0; i < cells.getLength(); i++) {
                Element currentElement = (Element) cells.item(i);
                try {
                    if (!currentElement.hasAttribute("xPos") && !currentElement.hasAttribute("yPos")) {
                        for (int j = 0; j < grid.length; j++) {
                            for (int k = 0; k < grid[j].length; k++) {
                                grid[j][k] = getConstructorParamsFromXMLElement(currentElement, simulationType);
                            }
                        }
                    } else if (!currentElement.hasAttribute("xPos")) {
                        for (int j = 0; j < grid.length; j++) {
                            grid[j][Integer.parseInt(currentElement.getAttribute("yPos"))] = getConstructorParamsFromXMLElement(currentElement, simulationType);
                        }
                    } else if (!currentElement.hasAttribute("yPos")) {
                        for (int j = 0; j < grid[0].length; j++) {
                            grid[Integer.parseInt(currentElement.getAttribute("xPos"))][j] = getConstructorParamsFromXMLElement(currentElement, simulationType);
                        }
                    } else {
                        grid[Integer.parseInt(currentElement.getAttribute("xPos"))][Integer.parseInt(currentElement.getAttribute("yPos"))] = getConstructorParamsFromXMLElement(currentElement, simulationType);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Could not instantiate " + simulationType + "." + simulationType + "Cell");
                }
            }
            return new SimulationGrid<>(grid, cellType);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Could not read input file");
        }
    }

    private String[] getConstructorParamsFromXMLElement(Element currentElement, String simulationType) throws Exception {
        List<String> constructorParams = new ArrayList<>();
        for (int j = 0; j < currentElement.getElementsByTagName("*").getLength(); j++) {
            constructorParams.add(currentElement.getElementsByTagName("*").item(j).getTextContent());
        }
        return constructorParams.toArray(new String[constructorParams.size()]);
    }
}
