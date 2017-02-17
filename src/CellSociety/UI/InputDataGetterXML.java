package CellSociety.UI;

import CellSociety.AbstractDiscrete_CellState;
import CellSociety.Abstract_Cell;
import CellSociety.Grids.BoundsHandler;
import CellSociety.Grids.NeighborsGetter;
import CellSociety.Grids.SimulationGrid;
import CellSociety.Grids.SimulationGridImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This class implements InputDataGetter by reading initial configuration from an XML file.
 * <p>
 * Created by th174 on 1/31/2017.
 *
 * @see CellSociety.UI.InputDataGetter
 */
public class InputDataGetterXML implements InputDataGetter {
    private final ResourceBundle myResources;
    private double framesPerSecond;
    private String simulationType;
    private String cellShape;
    private String boundsType;
    private String neighborMode;
    private String gridOutlineStyle;
    private SimulationGrid<? extends Abstract_Cell, ? extends AbstractDiscrete_CellState> simulationGrid;

    /**
     * Constructs InputDataGetter from XML input
     *
     * @param xmlFile       Input file to parse, formatted in XML
     * @param errorMessages Resource bundle containing error messages
     * @throws Exception if file could not be read or parsed
     */
    public InputDataGetterXML(File xmlFile, ResourceBundle errorMessages) throws Exception {
        myResources = errorMessages;
        readXML(xmlFile);
    }

    /**
     * @return simulationGrid of the animation
     * @see CellSociety.UI.InputDataGetter#getSimulationGrid()
     */
    @Override
    public SimulationGrid<? extends Abstract_Cell, ? extends AbstractDiscrete_CellState> getSimulationGrid() {
        return simulationGrid;
    }

    /**
     * @return simulation type
     * @see CellSociety.UI.InputDataGetter#getSimulationType()
     */
    @Override
    public String getSimulationType() {
        return simulationType;
    }

    /**
     * @return frames per second of the animation
     * @see CellSociety.UI.InputDataGetter#getFramesPerSecond()
     */
    @Override
    public double getFramesPerSecond() {
        return framesPerSecond;
    }

    /**
     * @return shape of the cells
     * @see CellSociety.UI.InputDataGetter#getCellShape()
     */
    @Override
    public String getCellShape() {
        return cellShape;
    }

    /**
     * @see CellSociety.UI.InputDataGetter#getGridBoundType()
     */
    @Override
    public String getGridBoundType() {
        return boundsType;
    }

    /**
     * @see CellSociety.UI.InputDataGetter#getNeighborMode()
     */
    @Override
    public String getNeighborMode() {
        return neighborMode;
    }

    /**
     * @return gridOutline as a String
     * @see CellSociety.UI.InputDataGetter#getGridOutline()
     */
    @Override
    public String getGridOutline() {
        return gridOutlineStyle;
    }

    private void readXML(File xmlInputFile) throws Exception {
        try {
            Document file = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlInputFile);
            Element root = file.getDocumentElement();
            String[][][] grid;
            Class<? extends Abstract_Cell> cellType;
            try {
                int width = root.hasAttribute("width") ? Integer.parseInt(root.getAttribute("width")) : 50;
                int height = root.hasAttribute("height") ? Integer.parseInt(root.getAttribute("height")) : 50;
                grid = new String[width][height][0];
                framesPerSecond = root.hasAttribute("fps") ? Double.parseDouble(root.getAttribute("fps")) : 3;
                simulationType = root.hasAttribute("type") ? root.getAttribute("type") : "GameOfLife";
                cellShape = root.hasAttribute("shape") ? root.getAttribute("shape") : "Square";
                boundsType = root.hasAttribute("bounds") ? root.getAttribute("bounds") : "Finite";
                neighborMode = root.hasAttribute("neighbors") ? root.getAttribute("neighbors") : "";
                gridOutlineStyle = root.hasAttribute("outlines") ? root.getAttribute("outlines") : "";
                try {
                    cellType = (Class<? extends Abstract_Cell>) Class.forName("CellSociety." + simulationType + "." + simulationType + "_Cell");
                    NodeList cells = file.getElementsByTagName("Cell");
                    for (int i = 0; i < cells.getLength(); i++) {
                        Element currentElement = (Element) cells.item(i);
                        try {
                            if (!currentElement.hasAttribute("xPos") && !currentElement.hasAttribute("yPos")) {
                                for (int j = 0; j < grid.length; j++) {
                                    for (int k = 0; k < grid[j].length; k++) {
                                        grid[j][k] = getConstructorParamsFromXMLElement(currentElement);
                                    }
                                }
                            } else if (!currentElement.hasAttribute("xPos")) {
                                for (int j = 0; j < grid.length; j++) {
                                    grid[j][Integer.parseInt(currentElement.getAttribute("yPos"))] = getConstructorParamsFromXMLElement(currentElement);
                                }
                            } else if (!currentElement.hasAttribute("yPos")) {
                                for (int j = 0; j < grid[0].length; j++) {
                                    grid[Integer.parseInt(currentElement.getAttribute("xPos"))][j] = getConstructorParamsFromXMLElement(currentElement);
                                }
                            } else {
                                grid[Integer.parseInt(currentElement.getAttribute("xPos"))][Integer.parseInt(currentElement.getAttribute("yPos"))] = getConstructorParamsFromXMLElement(currentElement);
                            }
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                            throw new Exception(myResources.getString("IndexOutOfBoundsException") + "(x,y)=(" + currentElement.getAttribute("xPos") + "," + currentElement.getAttribute("yPos") + ")");
                        } catch (NumberFormatException e) {
                            throw new Exception(myResources.getString("CoordinateFormatException"));
                        }
                    }
                    try {
                        simulationGrid = new SimulationGridImpl<>(grid, cellType);
                        NeighborsGetter gridShape;
                        try {
                            gridShape = (NeighborsGetter) Class.forName("CellSociety.Grids.SimulationGridImpl$" + neighborMode + cellShape + "sGrid").getConstructor(SimulationGridImpl.class).newInstance(simulationGrid);
                            try {
                                BoundsHandler gridBounds = (BoundsHandler) Class.forName("CellSociety.Grids.SimulationGridImpl$" + boundsType + "Bounds").getConstructor(SimulationGridImpl.class).newInstance(simulationGrid);
                                simulationGrid.setShapeType(gridShape).setBoundsType(gridBounds);
                            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                                throw new Exception(myResources.getString("BoundsTypeException") + boundsType);
                            }
                        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                            e.printStackTrace();
                            throw new Exception(myResources.getString("ShapeException") + neighborMode + cellShape);
                        }
                    } catch (SimulationGrid.CellInstantiationException e) {
                        throw new Exception(myResources.getString("CellInstantiationException") + e.getMessage());
                    }
                } catch (ClassNotFoundException e) {
                    throw new Exception(myResources.getString("ClassNotFoundException") + simulationType);
                }
            } catch (NumberFormatException e) {
                throw new Exception(myResources.getString("ConfigException"));
            }
        } catch (SAXException e) {
            throw new Exception(myResources.getString("XMLException") + xmlInputFile.getName());
        } catch (IOException e) {
            throw new Exception(myResources.getString("IOException") + xmlInputFile.getName());
        }
    }

    private String[] getConstructorParamsFromXMLElement(Element currentElement) {
        java.util.List<String> constructorParams = new ArrayList<>();
        for (int j = 0; j < currentElement.getElementsByTagName("*").getLength(); j++) {
            constructorParams.add(currentElement.getElementsByTagName("*").item(j).getTextContent().toUpperCase());
        }
        return constructorParams.toArray(new String[constructorParams.size()]);
    }
}