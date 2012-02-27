
import com.grid.simulations.simworld.worlds.collector.Agent_F;
import com.grid.simulations.simworld.worlds.collector.Agent_F.Item;
import java.util.ArrayList;

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
    map.trackFight();
    return false;
  }

  // this returns true depending on whether the agent wants to consume a food source
  // add your decision-making for eating here
  @Override
  public boolean eat() {
    map.trackEat();
    System.out.println("EATING");
    return true;
  }
  boolean firstSense = true;
  // template sense function as descibed in the assignment
  // add your code for sensing here

  @Override
  public void sense(ArrayList<Item> agents, ArrayList<Item> food) {
    if (!firstSense && !map.dialedIn()) {
      map.calibrateSpin(food);
    }
    map.addFood(food);
    map.setMobs(agents);
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
    MentalMap.NavigationFeeling feeling = map.whichWayToFood();
    if (feeling == MentalMap.NavigationFeeling.LOST) {
      feeling = map.whichWayToFamiliarTerritory();
    }

    switch (feeling) {
      case LOST:
        if (map.areaSeemsFamiliar()) {
          parent.slowdown();
        } else {
          System.out.println("OH NOES!  I NEED MY MOMMY!!!");
          // TODO: go back to a familiar area
        }
      case LEFT:
        parent.turnleft();
        map.trackSpin(true);
        parent.slowdown();
        break;
      case LEFT_AHEAD:
        parent.turnleft();
        map.trackSpin(true);
        parent.speedup();
        break;
      case RIGHT:
        parent.turnright();
        map.trackSpin(false);
        parent.slowdown();
        break;
      case RIGHT_AHEAD:
        parent.turnright();
        map.trackSpin(false);
        parent.speedup();
        break;
      case STRAIT_AHEAD:
        parent.speedup();
        break;
      case LEFT_AHEAD_CLOSE:
        parent.turnleft();
        map.trackSpin(true);
        if (parent.getSpeed() > 2) {
          parent.slowdown();
        }
        break;
      case RIGHT_AHEAD_CLOSE:
        parent.turnright();
        map.trackSpin(false);
        if (parent.getSpeed() > 2) {
          parent.slowdown();
        }
        break;
      case STRAIT_AHEAD_CLOSE:
        if (parent.getSpeed() > 2) {
          parent.slowdown();
        }
        break;
    }
//    paren
    map.trackMove(parent.getSpeed());
  }
}
