//This entire class is part of my masterpiece

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
    public static final int DEFAULT_GRID_WIDTH = 50;
    public static final int DEFAULT_GRID_HEIGHT = 50;
    public static final int DEFAULT_FRAMES_PER_SECOND = 3;
    public static final String DEFAULT_SIMULATION_TYPE = "GameOfLife";
    public static final String DEFAULT_SHAPE = "Square";
    public static final String DEFAULT_BOUNDS_TYPE = "Finite";
    public static final String DEFAULT_NEIGHBOR_MODE = "";
    public static final String DEFAULT_GRID_OUTLINE_STYLE = "";
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
                int width = root.hasAttribute("width") ? Integer.parseInt(root.getAttribute("width")) : DEFAULT_GRID_WIDTH;
                int height = root.hasAttribute("height") ? Integer.parseInt(root.getAttribute("height")) : DEFAULT_GRID_HEIGHT;
                grid = new String[width][height][0];
                framesPerSecond = root.hasAttribute("fps") ? Double.parseDouble(root.getAttribute("fps")) : DEFAULT_FRAMES_PER_SECOND;
                simulationType = root.hasAttribute("type") ? root.getAttribute("type") : DEFAULT_SIMULATION_TYPE;
                cellShape = root.hasAttribute("shape") ? root.getAttribute("shape") : DEFAULT_SHAPE;
                boundsType = root.hasAttribute("bounds") ? root.getAttribute("bounds") : DEFAULT_BOUNDS_TYPE;
                neighborMode = root.hasAttribute("neighbors") ? root.getAttribute("neighbors") : DEFAULT_NEIGHBOR_MODE;
                gridOutlineStyle = root.hasAttribute("outlines") ? root.getAttribute("outlines") : DEFAULT_GRID_OUTLINE_STYLE;
                try {
                    cellType = (Class<? extends Abstract_Cell>) Class.forName("CellSociety." + simulationType + "." + simulationType + "_Cell");
                    NodeList cells = file.getElementsByTagName("Cell");
                    for (int i = 0; i < cells.getLength(); i++) {
                        Element currentElement = (Element) cells.item(i);
                        try {
                            int x = currentElement.hasAttribute("xPos") ? Integer.parseInt(currentElement.getAttribute("xPos")) : 0;
                            int y = currentElement.hasAttribute("yPos") ? Integer.parseInt(currentElement.getAttribute("yPos")) : 0;
                            for (int j = x; j < (currentElement.hasAttribute("xPos") ? x + 1 : grid.length); j++) {
                                for (int k = y; k < (currentElement.hasAttribute("yPos") ? y + 1 : grid[j].length); k++) {
                                    grid[x][y] = getConstructorParamsFromXMLElement(currentElement);
                                }
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