package CellSociety.ForagingAnts;

public class Ant {
	private boolean food;
	private int life;
	
	
	public Ant(int lifetime) {
		life=lifetime;
		food=false;
	}
	public void growOlder(){
		life--;
	}
	public boolean isDead(){
		return life<=0;
	}
	public boolean hasFood(){
		return food;
	}

}
