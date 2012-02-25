import com.grid.simulations.simworld.worlds.collector.Agent_F;
import com.grid.simulations.simworld.worlds.collector.Agent_F.Item;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author M@
 */
public class Learner implements AgentFHelper {
  Agent_F parent = null;
  MentalMap map = new MentalMap();

  public Learner(Agent_F parent) {
    this.parent = parent;
  }

  // this function will be called automatically by the environment when agents are too
  // close to each other; needs to return true if the agent is fighting vs false if not
  // add your decision-making for fighting here
  @Override
  public boolean fight() {
    return false;
  }

  // this returns true depending on whether the agent wants to consume a food source
  // add your decision-making for eating here
  @Override
  public boolean eat() {
    return true;
  }

  boolean firstSense = true;
  // template sense function as descibed in the assignment
  // add your code for sensing here
  @Override
  public void sense(ArrayList<Item> agents, ArrayList<Item> food) {
    for (Item i : food) {
      System.out.print(i.getHeading()+" ");
    }
    System.out.println();
    if (!firstSense && !map.dialedIn()) {
      map.calibrateSpin(food);
    }
    map.addFood(food);
    System.out.println(map.dialedIn());
    firstSense = false;
  }

  // template act function as descibed in the assignment
  // add your code for acting here (note that you can only perform one turn and one speed action at the same time
  @Override
  public void act() {
    if (!map.dialedIn()) {
      parent.slowdown();
      parent.turnleft();
      return;
    }


    map.trackMove(parent.getSpeed());
  }
}
