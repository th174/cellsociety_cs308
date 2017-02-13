package CellSociety.SugarScape;

public class Agent {
    private int sugarMetabolism;
    private int startingSugar;
    private int sugar;
    private int vision; //how many patches ahead he can see
    //next 5 reproduction limits
    private boolean genderMale;
    private int age;
    private int maxAge;
    private int fertilityStart = 5;
    private int fertilityEnd = 10;

    /*
     * can add color based on age
     */
    public Agent() {
        this(1, 0, 2);
    }

    public Agent(int metabolism, int sug, int eyesight) {
        sugarMetabolism = metabolism;
        startingSugar = sugar = sug;
        vision = eyesight;
        age = 0;
        genderMale = Math.random() > 0.50; //50% chance
        maxAge = (int) (60 + (Math.random() * (100 - 60) - 1)); //random number between 60 and 100
    }

    public void grabSugar(SugarScape_Cell cell) {
        sugar += cell.getCurrentState().getSugar();
        cell.getCurrentState().removeSugar();
    }

    public void metabolize() {
        sugar -= sugarMetabolism;
    }

    public boolean isDead() {
        return sugar <= 0;
    }

    public boolean canReproduce() {
        return sugar > startingSugar && age <= fertilityEnd && age >= fertilityStart;
    }

    public boolean canReproduce(Agent a) {
        return a.canReproduce() && canReproduce() && (a.genderMale != genderMale);
    }

    public int getSugar() {
        return sugar;
    }
    public void divideSugar(){
    	sugar/=2;
    }

    public Agent reproduceWith(Agent a) {
        Agent baby =new Agent(sugarMetabolism, (getSugar() + a.getSugar()) / 2, vision);
        divideSugar();
        a.divideSugar();
        return baby;
    }



}
