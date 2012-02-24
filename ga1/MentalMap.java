import com.grid.simulations.simworld.worlds.collector.Agent_F.Item;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author M@
 */
public class MentalMap {

  double minFoodX = Double.POSITIVE_INFINITY;
  double maxFoodX = Double.NEGATIVE_INFINITY;
  double minFoodY = Double.POSITIVE_INFINITY;
  double maxFoodY = Double.NEGATIVE_INFINITY;
  double furthestSeenFoodDistance = 0;
  double furthestSeenMobDistance = 0;
  double spinSpeed = 0;

  Point currentPos = new Point();
  double currentAngle = Double.NaN;

  List<Point> foodLocations = new ArrayList<Point>();
  List<Point> lastStationaryLandmarksPolar = new ArrayList<Point>();
  
  public void addFood(List<Item> food) {
    lastStationaryLandmarksPolar.clear();

    for (Item morsel : food) {
      Point loc = getPosition(morsel);
      foodLocations.add(loc);

      // start tracking the fertile parts of the environment
      if (loc.x < minFoodX) {
        minFoodX = loc.x;
      }
      if (loc.x > maxFoodX) {
        maxFoodX = loc.x;
      }
      if (loc.y < minFoodY) {
        minFoodY = loc.y;
      }
      if (loc.y > maxFoodY) {
        maxFoodY = loc.y;
      }
      // track our own capabilities
      double d = morsel.getDistance();
      if (furthestSeenFoodDistance < d) {
        furthestSeenFoodDistance = d;
      }

      Point polar = new Point();
      polar.x = morsel.getDistance();
      polar.y = morsel.getHeading();
      lastStationaryLandmarksPolar.add(polar);
    }
  }

  public void trackMove(double speed) {
    currentPos.x += speed * Math.cos(currentAngle);
    currentPos.y += speed * Math.sin(currentAngle);
  }

  public void trackSpin(boolean clockwise) {
    currentAngle += clockwise ? -spinSpeed : spinSpeed;
  }

  public void calibrateSpin(List<Item> food) {
    for(Item morsel : food) {
      for (Point formerPos : lastStationaryLandmarksPolar) {
        if (morsel.getDistance() == formerPos.x) {  //NOTE: this will fail if two morsels are the same distance from the calibration point
          spinSpeed = Math.abs(morsel.getHeading() - formerPos.y);
          return;
        }
      }
    }
  }

  public boolean dialedIn() {
    return spinSpeed != 0;
  }

  public boolean areaSeemsFamiliar() {
    return pointSeemsFamiliar(currentPos);
  }

  public boolean pointSeemsFamiliar(Point p) {
    return (minFoodX <= p.x && p.x <= maxFoodX
            && minFoodY <= p.y && p.y <= maxFoodY);
  }

  public Point getPosition(Item item) {
    //Soh Cah Toa
    double dx = item.getDistance() * Math.cos(item.getHeading());
    double dy = item.getDistance() * Math.sin(item.getHeading());
    return currentPos.plus(dx, dy);
  }

  public class Point {

    public double x = 0;
    public double y = 0;

    public Point plus(Point p) {
      Point result = new Point();
      result.x = x + p.x;
      result.y = y + p.y;
      return result;
    }
    public Point plus(double dx, double dy) {
      Point result = new Point();
      result.x = x + dx;
      result.y = y + dy;
      return result;
    }
  }
}
