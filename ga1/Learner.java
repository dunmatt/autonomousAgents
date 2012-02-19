package ga1;

import com.grid.simulations.simworld.worlds.collector.Agent_F;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author M@
 */
public class Learner extends Agent_F {
  MentalMap map = new MentalMap();

  public Learner(String thisID, double iX, double iY, long seed, Hashtable SchdulerObjectManagementList) {
    super("learner" + thisID, iX, iY, seed, SchdulerObjectManagementList);
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

  // template sense function as descibed in the assignment
  // add your code for sensing here
  @Override
  public void sense(ArrayList<Item> agents, ArrayList<Item> food) {
    map.addFood(food);
    if (!map.dialedIn() && !food.isEmpty()) {
      turnleft();
      map.calibrateSpin(food);
    }
    System.out.println(map.dialedIn());
  }

  // template act function as descibed in the assignment
  // add your code for acting here (note that you can only perform one turn and one speed action at the same time
  @Override
  public void act() {
  }
}
