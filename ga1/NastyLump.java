
/** Template for foraging agent
 *
 * @author matthias scheutz
 *
 */
import com.grid.simulations.simworld.*;
import com.grid.simulations.simworld.components.*;
import com.grid.simulations.simworld.worlds.collector.*;
import java.util.*;
import com.grid.simulations.simworld.worlds.collector.Agent_F.Item;

public class NastyLump implements AgentFHelper{

  // add your local variables here
  private boolean initialized = false;
  private Agent_F fRef;

  private Item closestFood; 

  // Do not change this line
  /*
  public NastyLump(String thisIDName, int thisIDNumber, double iX, double iY, long seed, Hashtable SchdulerObjectManagementList) {
    super((thisIDName.isEmpty() ? "testagent" : thisIDName),
            thisIDNumber, iX, iY, seed, SchdulerObjectManagementList);
    System.err.println("(x,y) = (" + iX + "," + iY + ")" ); 
  } */
  public NastyLump(Agent_F ref) {
    fRef = ref;
  } 

  void init(){
    initialized = true;
  }

  // this function will be called automatically by the environment when agents are too
  // close to each other; needs to return true if the agent is fighting vs false if not
  // add your decision-making for fighting here
  public boolean fight() {
    return true;
  }

  // this returns true depending on whether the agent wants to consume a food source
  // add your decision-making for eating here
  public boolean eat() {
    return true;
  }

  // template sense function as descibed in the assignment
  // add your code for sensing here
  public void sense(ArrayList<Item> agents, ArrayList<Item> food) {
	double cfDist = Double.MAX_VALUE;
	for(Item f: food) {
		if(f.getDistance() < cfDist) {		
			closestFood = f;
			cfDist = f.getDistance();
		}	
	} 

  }

  // template act function as descibed in the assignment
  // add your code for acting here (note that you can only perform one turn and one speed action at the same time
  public void act() {
    if(fRef.getSpeed() >= 5) fRef.slowdown();
    else if(fRef.getSpeed() < 5) fRef.speedup();

    if(closestFood != null) {
	if(closestFood.getHeading() > 0) fRef.turnright();
    	else fRef.turnleft();
    }

    
  }
}
