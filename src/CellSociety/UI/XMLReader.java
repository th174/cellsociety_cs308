package CellSociety.UI;

import java.io.File;

import org.w3c.dom.Element;

import CellSociety.Abstract_Cell;
import CellSociety.Abstract_CellState;
import CellSociety.Grids.SimulationGrid;

public interface XMLReader {

	<E extends Abstract_Cell<E, F>, F extends Abstract_CellState> SimulationGrid<E> readXML(File XMLFile) throws Exception;
	       
	
	<T> T instantiateCellView(Abstract_Cell<? extends Abstract_Cell, ? extends Abstract_CellState> cell);
	       
	String[] getConstructorParamsFromXMLElement(Element currentElement) throws Exception;


	
	
	
	
}
