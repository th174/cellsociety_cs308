package CellSociety.PredatorPrey;
import CellSociety.Abstract_Cell;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Created by th174 on 1/29/2017.
 */
public class PredatorPrey_Cell extends Abstract_Cell<PredatorPreyCell_State> {
    private final int preyReproductionTime = 5;
    private final int predReproductionTime = 10;
    private final int daysToStarvation = 5;
    private Animal[] currAndNextAnimal= new Animal[2];
    private boolean moved;
    private Animal myAnimal;
    private int predMovesSinceEaten;
    private int movesSinceReproduction = 0;
    private Map<PredatorPreyCell_State, Animal> possibleAnimals = new TreeMap<PredatorPreyCell_State, Animal>() {
	{
        put(PredatorPreyCell_State.EMPTY, null);
        put(PredatorPreyCell_State.PREDATOR, new Predator(predReproductionTime, daysToStarvation));
        put(PredatorPreyCell_State.PREY, new Prey(preyReproductionTime));
    }};
    public PredatorPrey_Cell(int x, int y, String... params) {
        this(x, y, new PredatorPreyCell_State(params[0]));
    }
    public PredatorPrey_Cell(int x, int y, PredatorPreyCell_State state) {
        super(x, y, state);
        myAnimal = possibleAnimals.get(state);
        currAndNextAnimal[0]=possibleAnimals.get(state);
        currAndNextAnimal[1]=possibleAnimals.get(state);
        predMovesSinceEaten = 0;
        moved=false;
    }
    /**
     * Every turn of the simulation a fish will move to a random adjacent cell
     * unless all four are occupied. If the fish has survived the number of turns
     * necessary to breed it produces a new fish if there is an empty adjacent cell..
     * <p>
     * Each turn if there is a fish adjacent to a shark the shark eats it. If there are
     * multiple adjacent fish the shark eats one at random. If there are no adjacent fish
     * the shark moves in the same manner as fish. After eating or moving if the shark has
     * survived the number of turns necessary to breed it produces a new shark if there is
     * an empty adjacent cell.
     *
     * @see Abstract_Cell#interact()
     */
    @Override
    public void interact() {
    	//System.out.println("mving " + myAnimal);
    	if(currAndNextAnimal[1]!=null && currAndNextAnimal[1].isPrey() && currAndNextAnimal[0]==null) System.out.println("mvoing from null to animal");
    	myAnimal=currAndNextAnimal[1];
    	currAndNextAnimal[0]=myAnimal;
    	//if(myAnimal==null) System.out.println("##### " + getState() + getNextAnimal());
    	
        if (myAnimal!=null){ 
            Collection<PredatorPrey_Cell> adjNeighbors = getAdjNeighbors().asCollection().stream().filter(PredatorPrey_Cell.class::isInstance).map(PredatorPrey_Cell.class::cast).collect(Collectors.toSet());
        	
            //Collection<PredatorPrey_Cell> adjNeighbors = getAdjNeighbors().asCollection();
        	if (myAnimal.isPred()){
                /*Optional<PredatorPrey_Cell> potentialPrey = adjNeighbors.stream()
                        .skip((long) Math.random() * adjNeighbors.size())
                        .filter(neighbor -> neighbor.getState().equals(PredatorPreyCell_State.PREY)).findAny();
                */
        		boolean anyPrey=false;
        		for(PredatorPrey_Cell neighbor: adjNeighbors){
        			//System.out.print( " "  +neighbor.getNextAnimal());
        			if(neighbor.getAnimal()!=null && neighbor.getAnimal().isPrey()){
        				//System.out.println("EATING");
        				anyPrey=true;
        				setNextAnimal(null);
        				setState(PredatorPreyCell_State.EMPTY);
        				myAnimal.resetMovesSinceEaten();
        				neighbor.setNextAnimal(myAnimal);
        				neighbor.setState(PredatorPreyCell_State.PREDATOR);
        				break;
        			}
        		}
        		if(!anyPrey){//cant eat any fish, lets try to move
        			myAnimal.updateMovesSinceEaten();
                    if (isStarved()) {
                    	System.out.println("pred dying");
                        setState(PredatorPreyCell_State.EMPTY);
                        setNextAnimal(null);
                        moved=true;
                        return;
                    }else{                  	
                    	for(PredatorPrey_Cell neighbor: adjNeighbors){
                			if(neighbor.getNextAnimal()==null||neighbor.getNextState().equals(PredatorPreyCell_State.EMPTY)){
                				//System.out.println("PRED MOVING");
                				setNextAnimal(null);
                				setState(PredatorPreyCell_State.EMPTY);             				
                				neighbor.setNextAnimal(myAnimal);
                				neighbor.setState(PredatorPreyCell_State.PREDATOR);
                				break;
                			}
                			
                		}
                    }
                    
        		}
        		if (myAnimal.canReproduce()) {
                    setState(PredatorPreyCell_State.PREDATOR);
                    setNextAnimal( new Predator(predReproductionTime, daysToStarvation));
                }else{
                	myAnimal.updateMovesSinceReproduction();
                }
        		/*
                if (potentialPrey.isPresent()) {
                	System.out.println("potential prey");
                	setNextAnimal(null);
                	setState(PredatorPreyCell_State.EMPTY);
                	myAnimal.resetMovesSinceEaten();
                	potentialPrey.ifPresent(e -> {
                		e.setNextAnimal(myAnimal);
                		e.setState(PredatorPreyCell_State.PREDATOR);
                		//move(potentialPrey.get(), PredatorPreyCell_State.EMPTY);
                	}
                	);
                } else {
                    myAnimal.updateMovesSinceEaten();
                    if (isStarved()) {
                    	System.out.println("pred dying");
                        setState(PredatorPreyCell_State.EMPTY);
                        setNextAnimal(null);
                        return;
                    } else {
                    	Optional<PredatorPrey_Cell> potentialMove = adjNeighbors.stream()
                                .skip((long) Math.random() * adjNeighbors.size())
                                .filter(neighbor -> neighbor.getState().equals(PredatorPreyCell_State.EMPTY))
                                .findAny();
                         if(potentialMove.isPresent()){
                        	 System.out.println("can move");
                        	 setNextAnimal(null);
                        	 setState(PredatorPreyCell_State.EMPTY);
                        	 potentialMove.ifPresent(e->{
                        		 e.setNextAnimal(myAnimal);
                        		 e.setState(PredatorPreyCell_State.PREDATOR);
                        		 
                        		 //move(e, PredatorPreyCell_State.EMPTY);
                        	 }
                        	 );
                        	 //.ifPresent(e -> move(e, PredatorPreyCell_State.EMPTY));
                         }
                    }
                }
                if (myAnimal.canReproduce()) {
                    setState(PredatorPreyCell_State.PREDATOR);
                    setNextAnimal( new Predator(predReproductionTime, daysToStarvation));
                }else{
                	myAnimal.updateMovesSinceReproduction();
                }*/
        		}
            if(myAnimal.isPrey()&&currAndNextAnimal[1].isPrey()){ //if this prey hasnt yet been eaten
            	for(PredatorPrey_Cell neighbor: adjNeighbors){
            		System.out.println(neighbor.getNextAnimal() + " and cur  "+neighbor.getAnimal());
        			if(neighbor.getNextAnimal()==null){
        				
        				neighbor.setNextAnimal(myAnimal);
        				neighbor.setState(PredatorPreyCell_State.PREY);
        				break;
        			}        			
        		}
            	if(canReproduce()){
            		setState(PredatorPreyCell_State.PREY);
            		setNextAnimal(new Prey(preyReproductionTime));
            		
            	}else{
            		setState(PredatorPreyCell_State.EMPTY);
            		setNextAnimal(null);
            	}	
            }}
            	/*
                adjNeighbors.stream()
                        .skip((long) Math.random() * adjNeighbors.size())
                        .filter(PredatorPrey_Cell::nextStateEmpty)
                        .findAny().ifPresent(e -> {
                        	e.setState(PredatorPreyCell_State.PREY);
                			e.setNextAnimal(myAnimal);
                        });
                if (!canReproduce()) {
                    setState(PredatorPreyCell_State.EMPTY);
                    setNextAnimal(null);
                } else {
                	setState(PredatorPreyCell_State.PREY);
                    setNextAnimal(new Prey(predReproductionTime));
                }
            }
            myAnimal.updateMovesSinceReproduction();
        }*/
        moved=true;
        //if(myAnimal==null) System.out.println("NEXT##### " + getState() + getNextAnimal());
        
    }
    
    public boolean canReproduce() {
        return (myAnimal.canReproduce());
    }
    
    /*
     * private get state method
     */
    private boolean nextStateEmpty() {
    	return currAndNextAnimal[1]==null;
        //return getNextState().equals(PredatorPreyCell_State.EMPTY);
    }
    private boolean nextStateDead() {
    	return !currAndNextAnimal[1].equals(currAndNextAnimal[0]);
        //return getNextState().equals(PredatorPreyCell_State.PREDATOR);
    }
    private boolean isStarved() {
        return myAnimal.isDead();
    }
    public Animal getAnimal(){
    	return myAnimal;
    }
    public boolean hasPred(){
    	return myAnimal.isPred();
    }
    public boolean hasPrey(){
    	return myAnimal.isPrey();
    }
    public void setNextAnimal(Animal a){
    	currAndNextAnimal[1] = a;
    	
    }
    public Animal getNextAnimal(){
    	return currAndNextAnimal[1];
    }
    
}