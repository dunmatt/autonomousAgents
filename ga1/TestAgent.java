
/** Template for foraging agent
 *
 * @author matthias scheutz
 *
 */
import com.grid.simulations.simworld.components.*;
import com.grid.simulations.simworld.worlds.collector.*;
import java.util.*;

public final class TestAgent extends Agent_F {

  public static int counter = 0;

  List<AgentFHelper> helpers = new ArrayList<AgentFHelper>();
  AgentFHelper chosenHelper = null;
  boolean initialized = false;
  // add your local variables here

  // Do not change this line
  public TestAgent(String thisIDName, int thisIDNumber, double iX, double iY, long seed, Hashtable SchdulerObjectManagementList) {
    super((thisIDName.isEmpty() ? "testagent" : thisIDName),
            thisIDNumber, iX, iY, seed, SchdulerObjectManagementList);
    helpers.add(new Learner(this));
    helpers.add(new NastyLump(this));
    helpers.add(new VectorSum(this));
    init();
  }

  void init(){
    if (initialized) {
      return;
    }
    /*
    helpers.add(new Learner(this));
    Random rand = new Random();
    int i = rand.nextInt(helpers.size());
    chosenHelper = helpers.get(i);*/

    int numagents = helpers.size();
    chosenHelper = helpers.get(counter % numagents);
    counter++;

    initialized = true;
  }

  // this function will be called automatically by the environment when agents are too
  // close to each other; needs to return true if the agent is fighting vs false if not
  // add your decision-making for fighting here
  public boolean fight() {
    return chosenHelper.fight();
  }

  // this returns true depending on whether the agent wants to consume a food source
  // add your decision-making for eating here
  public boolean eat() {
    return chosenHelper.eat();
  }

  // template sense function as descibed in the assignment
  // add your code for sensing here
  public void sense(ArrayList<Item> agents, ArrayList<Item> food) {
    chosenHelper.sense(agents, food);
  }

  // template act function as descibed in the assignment
  // add your code for acting here (note that you can only perform one turn and one speed action at the same time
  public void act() {
    chosenHelper.act();
  }
}
