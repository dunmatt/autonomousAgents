
import com.grid.simulations.simworld.worlds.collector.Agent_F;
import com.grid.simulations.simworld.worlds.collector.Agent_F.Item;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

/**
 *
 * @author M@
 */
public class Learner extends Agent_F {
	final double OBVIOUS_FAMILY_TRAIT = 0x0F00D000;
	MentalMap map = new MentalMap();
	Random rand = new Random();

	public Learner() {
		super();
	}

	public Learner(String thisIDName, int thisIDNumber, double iX, double iY, long seed, Hashtable SchdulerObjectManagementList) {
		super((thisIDName.isEmpty() ? "testagent" : thisIDName),
					thisIDNumber, iX, iY, seed, SchdulerObjectManagementList);
	}

	boolean oneOfUs(Item agent) {
		return ((int) agent.getAggression() & 0x0FFFF000) == OBVIOUS_FAMILY_TRAIT;
	}

	int getOtherEnergy(Item agent) {
		return (int) (agent.getAggression() - OBVIOUS_FAMILY_TRAIT);
	}

	void front() {
		setAggression(getEnergy() + OBVIOUS_FAMILY_TRAIT);
	}

	// this function will be called automatically by the environment when agents are too
	// close to each other; needs to return true if the agent is fighting vs false if not
	// add your decision-making for fighting here
	@Override
	public boolean fight() {
		map.trackFight();
		if (oneOfUs(map.nearestMob)) {
			return getEnergy() < getOtherEnergy(map.nearestMob);
		} else {
			double p = (getEnergy() / 500.0 - 1.0);
			p *= p; // get parabola
			return rand.nextDouble() < p;
		}
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
//		System.out.println(this.getEnergy());
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
		front();

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
