
import com.grid.simulations.simworld.worlds.collector.Agent_F;
import com.grid.simulations.simworld.worlds.collector.Agent_F.Item;
import java.util.ArrayList;
import java.util.Hashtable;

public class VectorSum implements AgentFHelper {

  Agent_F af;
  // add your local variables here
  int turnDecision = 0;
  //turhDecision = 0 will be keep going
  //turnDecision = 1 will be turn right
  //turnDecision = -1 will be turn left
  //turnDecision = 10 will be turn around
  int goDecision = 0;
  //goDecision = 0 is stay constant
  //goDecision = 1 is speedup
  //goDecision = -1 is slowdown

  // Do not change this line
  public VectorSum(Agent_F agent) {
    af = agent;
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

  public int foodTurnDecision(double ang) {
    int tDecision = 0;
    if (ang <= 180) {
      tDecision = 1;
    }
    if (ang > 180) {
      tDecision = -1;
    }
    if (ang > 345 || ang < 15) {
      tDecision = 0;
    }
    return tDecision;
  }

  public int enemyTurnDecision(double ang) {
    int tDecision = 0;
    if (ang < 270 && ang > 90) {
      return 0;
    }
    if (ang <= 90) {
      tDecision = -1;
    }
    if (ang >= 270) {
      tDecision = 1;
    }
    return tDecision;
  }

  // template sense function as descibed in the assignment
  // add your code for sensing here
  @Override
  public void sense(ArrayList<Item> agents, ArrayList<Item> food) {
    for (int i = 0; i < agents.size(); i++) {
      //this is for bad agents
      Item sensedAgent = agents.get(i);
      double dis = sensedAgent.getDistance();
      double ang = sensedAgent.getHeading();
      turnDecision = enemyTurnDecision(ang);
      if (dis < 10) {
        turnDecision = 180;
        //if turnDecision = 180, turn around
        break;
      }
      if (dis <= 20) {
        turnDecision = turnDecision * 3;
        //return 3 or -3
        break;
      }
      if (dis <= 45) {
        turnDecision = turnDecision * 2;
        //this should return 2 or -2
        break;
      }
    }

    for (int i = 0; i < food.size(); i++) {
      //this is code for food
      Item sensedFood = food.get(i);
      double dis = sensedFood.getDistance();
      double ang = sensedFood.getHeading();
      if (dis < 10) {
        goDecision = 0;
        break;
      }
      if (dis < 30 && dis >= 10) {
        goDecision = -3;
        break;
      }
      if (dis < 50) {
        goDecision = 0;
        break;
      }
      if (dis < 75) {
        goDecision = 1;
        break;
      }
      if (dis < 100) {
        goDecision = 2;
      }
      if (dis > 100) {
        goDecision = 0;
      }
      turnDecision = foodTurnDecision(ang);
      if (dis < 10) {
        turnDecision = 180;
        //if turnDecision = 180, turn around
        break;
      }
      if (dis <= 20) {
        turnDecision = turnDecision * 3;
        //return 3 or -3
        break;
      }
      if (dis <= 45) {
        turnDecision = turnDecision * 2;
        //this should return 2 or -2
        break;
      }
    }
  }

  @Override
  public void act() {
    if (turnDecision == 180) {
      //turn around
      for (int i = 0; i < 6; i++) {
        //I'm guessing 5 turns is enough
        af.turnright();
      }
    } else {
      int k = 0;
      if (turnDecision < 0) {
        while (k > turnDecision) {
          af.turnleft();
          k--;
        }
      }
      if (turnDecision > 0) {
        while (k < turnDecision) {
          af.turnright();
          k++;
        }
      }
      //do nothing for turnDecision=0
    }
    if (goDecision < 0) {
      int j = 0;
      while (j > goDecision) {
        af.slowdown();
        j--;
      }
    }
    if (goDecision > 0) {
      int p = 0;
      while (p < goDecision) {
        af.speedup();
        p++;
      }
    }

  }
}
