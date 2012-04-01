/** A simple foraging agent template
 *
 * @author matthias scheutz
 *
 */

import com.grid.simulations.simworld.worlds.collector.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class TestAgent extends Agent_F {
    // left public on purpose -- do not use these variables in your code!!!
    double foodheading = 0;
    double speedupcount = 0;
    double maxspeed = 4;
    Item closest = null;

    // default constructor, make sure it's present
    public TestAgent() {
	super();
    }    
    
    // standard simworld constructor
    public TestAgent(String thisIDName, int thisIDNumber, double iX, double iY, long seed, Hashtable SchdulerObjectManagementList) {
        super((thisIDName.isEmpty() ? "testagent" : thisIDName),
	      thisIDNumber, iX, iY, seed, SchdulerObjectManagementList);
    }

    // this function will be called automatically by the environment when agents are too
    // close to each other; needs to return true if the agent is fighting vs false if not
    public boolean fight() {
	if (closest != null) {
	    if (closest.getAggression() > getAggression()) {
		System.out.println(agentID + " is retreating...");
		return false;
	    }
	    else {
		System.out.println(agentID + " is fighting...");
		return true;
	    }
	}
	else {
	    System.out.println("GOT NO CLOSEST BUT STILL FIGHTING...");
	    return true;
	}
    }

    // this returns true depending on whether the agent wants to consume a food source
    // add your decision-making for eating here
    public boolean eat() {
	System.out.println(agentID + " is eating...");
	return true;
    }

    // template sense function, could be abstract
    public void sense(ArrayList<Item> agents,ArrayList<Item> food) {
	double distance = 1000;
	foodheading = -1;
	for(Item f:food) {
	    if (f.getDistance() < distance) {
		distance = f.getDistance();
		foodheading = f.getHeading();
	    }
	}
	System.out.println(agentID + " is heading " + foodheading + " to get food at " + distance);
	// look at the other agents, get the closest and remember it
	closest = null;
	for(Item a:agents) {
	    if (a.getDistance() < distance) {
		distance = a.getDistance();
		closest = a;
	    }
	}	
    }

    // template act function, could be abstract
    public void act() {
	//System.out.println("Calling act on " + agentID);
	// only turn if the foodheading is positive
	if (foodheading>15 && foodheading <185) {
	    turnright();
	    if (speedupcount > 0) {
		speedupcount--;
		slowdown();
		System.out.println(agentID + " is slowing down...");
	    }
	}
	else if (foodheading < 345) {
	    turnleft();
	    if (speedupcount > 0) {
		speedupcount--;
		slowdown();
		System.out.println(agentID + " is slowing down...");
	    }
	}
	else if (speedupcount < maxspeed) {
	    speedup();
	    speedupcount++;
	    System.out.println(agentID + " is speeding up...");
	}
	// compute aggression level based on "hunger"
	System.out.println(agentID + " energy: " + getEnergy());	
	setAggression(1.0/getEnergy());
	System.out.println(agentID + " aggression: " + getAggression());
    }
}
