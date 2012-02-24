
import com.grid.simulations.simworld.worlds.collector.Agent_F.Item;
import java.util.ArrayList;


/**
 *
 * @author M@
 */
public interface AgentFHelper {

  public boolean fight();

  // this returns true depending on whether the agent wants to consume a food source
  // add your decision-making for eating here
  public boolean eat();

  // template sense function as descibed in the assignment
  // add your code for sensing here
  public void sense(ArrayList<Item> agents, ArrayList<Item> food);

  // template act function as descibed in the assignment
  // add your code for acting here (note that you can only perform one turn and one speed action at the same time
  public void act();
}
