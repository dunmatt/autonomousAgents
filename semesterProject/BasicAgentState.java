
import com.grid.simulations.simworld.components.Agent2DState;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author maxsmiley
 */
public class BasicAgentState extends Agent2DState {
    public double maxDistancePerCycleX = 0;
    public double maxDistancePerCycleY = 0;

    public BasicAgentState(String agentIDName,
            int agentIDNumber,
            double x,
            double y,
            double maxDistancePerCycleX,
            double maxDistancePerCycleY) {
        super(agentIDName,
	      agentIDNumber,maxDistancePerCycleX,
	      maxDistancePerCycleY,
                x, y);
    }

    public BasicAgentState(String agentIDName,
            int agentIDNumber,
            double x,
	    double y) {
        super(agentIDName,
              agentIDNumber,0,0,
                x, y);
    }
}
