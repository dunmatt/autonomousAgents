
import com.grid.simulations.simworld.worlds.collector.Agent_F;
import com.grid.simulations.simworld.worlds.collector.Agent_F.Item;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author M@
 */
public class Learner extends Agent_F {
  MentalMap map = new MentalMap();

    public Learner() {
        super();
    }

    public Learner(String thisIDName, int thisIDNumber, double iX, double iY, long seed, Hashtable SchdulerObjectManagementList) {
        super((thisIDName.isEmpty() ? "testagent" : thisIDName),
          thisIDNumber, iX, iY, seed, SchdulerObjectManagementList);
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
      slowdown();
      turnleft();
      return;
    }
    MentalMap.NavigationFeeling feeling = map.whichWayToFood();
    if (feeling == MentalMap.NavigationFeeling.LOST) {
      feeling = map.whichWayToFamiliarTerritory();
    }

    switch (feeling) {
      case LOST:
        if (map.areaSeemsFamiliar()) {
          slowdown();
        } else {
//          System.out.println("OH NOES!  I NEED MY MOMMY!!!");
          // TODO: go back to a familiar area
        }
      case LEFT:
        turnleft();
        map.trackSpin(true);
        slowdown();
        break;
      case LEFT_AHEAD:
        turnleft();
        map.trackSpin(true);
        speedup();
        break;
      case RIGHT:
        turnright();
        map.trackSpin(false);
        slowdown();
        break;
      case RIGHT_AHEAD:
        turnright();
        map.trackSpin(false);
        speedup();
        break;
      case STRAIT_AHEAD:
        speedup();
        break;
      case LEFT_AHEAD_CLOSE:
        turnleft();
        map.trackSpin(true);
        if (getSpeed() > 2) {
          slowdown();
        }
        break;
      case RIGHT_AHEAD_CLOSE:
        turnright();
        map.trackSpin(false);
        if (getSpeed() > 2) {
          slowdown();
        }
        break;
      case STRAIT_AHEAD_CLOSE:
        if (getSpeed() > 2) {
          slowdown();
        }
        break;
    }
    map.trackMove(getSpeed());
  }
}
