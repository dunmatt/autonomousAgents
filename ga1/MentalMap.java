
import com.grid.simulations.simworld.worlds.collector.Agent_F.Item;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author M@
 */
public class MentalMap {

  final double SPATIAL_DISTANCE_WEIGHT = 2;
  double minFoodX = Double.POSITIVE_INFINITY;
  double maxFoodX = Double.NEGATIVE_INFINITY;
  double minFoodY = Double.POSITIVE_INFINITY;
  double maxFoodY = Double.NEGATIVE_INFINITY;
  double furthestSeenFoodDistance = 0;
  double furthestSeenMobDistance = 0;
  double furthestFightRange = 0;
  double furthestEatRange = 5;
  double spinSpeed = 0;
  double lastSpeed = 1;
  double fastestSeenSpeed = 1;
  Point currentPos = new Point();
  double currentAngle = Double.NaN;
  AbstractMap<Point, Item> foodLocations = new HashMap<Point, Item>();
  List<Point> lastStationaryLandmarksPolar = new ArrayList<Point>();
  List<Item> lastMobs = null;

  public void addFood(List<Item> food) {
    lastStationaryLandmarksPolar.clear();

    if (dialedIn()) {
      ArrayList<Point> emptyPoints = new ArrayList<Point>();
      for (Entry<Point, Item> e : foodLocations.entrySet()) {
        if (e.getValue().getDistance() < furthestSeenFoodDistance) {
          emptyPoints.add(e.getKey());
        }
      }
      for (Point key : emptyPoints) {
        foodLocations.remove(key);
      }
    }

    for (Item morsel : food) {
      Point loc = getPosition(morsel);
      if (!foodLocations.containsKey(loc)) {
        foodLocations.put(loc, morsel);
      }

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

  public void setMobs(List<Item> agents) {
    lastMobs = agents;
  }

  public void trackMove(double speed) {
    lastSpeed = speed;
    if (speed > fastestSeenSpeed) {
      fastestSeenSpeed = speed;
    }
    currentPos.x += speed * Math.cos(currentAngle);
    currentPos.y += speed * Math.sin(currentAngle);
  }

  public void trackSpin(boolean clockwise) {
    currentAngle += clockwise ? spinSpeed : -spinSpeed;
  }

  public void trackEat() {
    double nearestFoodDistance = Double.POSITIVE_INFINITY;
    for (Item morsel : foodLocations.values()) {
      if (morsel.getDistance() < nearestFoodDistance) {
        nearestFoodDistance = morsel.getDistance();
      }
    }
    if (nearestFoodDistance > furthestEatRange) {
      furthestEatRange = nearestFoodDistance;
    }
  }

  public void trackFight() {
    Item nearestMob = lastMobs.get(0);
    for (Item mob : lastMobs) {
      if (mob.getDistance() < nearestMob.getDistance()) {
        nearestMob = mob;
      }
    }
    if (nearestMob.getDistance() > furthestFightRange) {
      furthestFightRange = nearestMob.getDistance();
    }
  }

  public void calibrateSpin(List<Item> food) {
    if (food.isEmpty()
            || food.size() != lastStationaryLandmarksPolar.size()) {
      return;
    }
    Item closestMorsel = food.get(0);
    for (Item morsel : food) {
      if (morsel.getDistance() < closestMorsel.getDistance()) {
        closestMorsel = morsel;
      }
    }
    Point closestLandmark = lastStationaryLandmarksPolar.get(0);
    for (Point formerPos : lastStationaryLandmarksPolar) {
      if (formerPos.x < closestLandmark.x) {
        closestLandmark = formerPos;
      }
    }
    spinSpeed = Math.abs(closestMorsel.getHeading() - closestLandmark.y);
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

  public double itemETA(Item item) {
    return (item.getHeading() - (item.getHeading() > 180 ? 180 : 0)) / spinSpeed + (SPATIAL_DISTANCE_WEIGHT * item.getDistance() / fastestSeenSpeed);
  }
  double lastNearestFoodDistance = Double.POSITIVE_INFINITY;

  public NavigationFeeling whichWayToFood() {
    double nearestVal = Double.POSITIVE_INFINITY;
    Item nearestFoodLoc = null;
    for (Item p : foodLocations.values()) {
      double eta = itemETA(p);
//      System.err.println(eta);
      if (eta < nearestVal) {
        nearestVal = eta;
        nearestFoodLoc = p;
      }
    }
    
    if (nearestFoodLoc != null) {
      double heading = nearestFoodLoc.getHeading() % 360;
      double stoppingDistance = 1.25 * nearestFoodLoc.getDistance() / Math.max(1, lastSpeed);  // magic number here arbitrary, needs to be < 1
      System.out.println("Heading: " + heading + " Distance: " + stoppingDistance);
      if (stoppingDistance == lastNearestFoodDistance && lastSpeed > 0) {
        return NavigationFeeling.LOST;
      } else {
        lastNearestFoodDistance = stoppingDistance;
      }

      if (heading < 10 || heading > 350) {
        return stoppingDistance < furthestEatRange && lastSpeed > 1 ? NavigationFeeling.STRAIT_AHEAD_CLOSE : NavigationFeeling.STRAIT_AHEAD;
      } else if (heading < 90) {
        return stoppingDistance < furthestEatRange && lastSpeed > 1 ? NavigationFeeling.RIGHT_AHEAD_CLOSE : NavigationFeeling.RIGHT_AHEAD;
      } else if (heading < 180) {
        return NavigationFeeling.RIGHT;
      } else if (heading < 270) {
        return NavigationFeeling.LEFT;
      } else if (heading < 350) {
        return stoppingDistance < furthestEatRange && lastSpeed > 1 ? NavigationFeeling.LEFT_AHEAD_CLOSE : NavigationFeeling.LEFT_AHEAD;
      }
    }
    return NavigationFeeling.LOST;
  }

  public NavigationFeeling whichWayToFamiliarTerritory() {
    double midX = (maxFoodX - minFoodX) / 2;
    double midY = (maxFoodY - minFoodY) / 2;
    double theta = Math.atan((midY - currentPos.y)/(midX - currentPos.x));
    double distance = Math.sqrt(Math.pow(maxFoodX - minFoodX, 2) + Math.pow(maxFoodY - minFoodY, 2));
    double dTheta = (360 + theta - currentAngle) % 360;
      if (dTheta < 10 || dTheta > 350) {
        return distance < 20 && lastSpeed > 1 ? NavigationFeeling.STRAIT_AHEAD_CLOSE : NavigationFeeling.STRAIT_AHEAD;
      } else if (dTheta < 90) {
        return distance < 20 && lastSpeed > 1 ? NavigationFeeling.RIGHT_AHEAD_CLOSE : NavigationFeeling.RIGHT_AHEAD;
      } else if (dTheta < 180) {
        return NavigationFeeling.RIGHT;
      } else if (dTheta < 270) {
        return NavigationFeeling.LEFT;
      } else if (dTheta < 350) {
        return distance < 20 && lastSpeed > 1 ? NavigationFeeling.LEFT_AHEAD_CLOSE : NavigationFeeling.LEFT_AHEAD;
      }
    return NavigationFeeling.LOST;
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

    @Override
    public boolean equals(Object o) {
      if (o instanceof Point) {
        Point p = (Point) o;
        return x == p.x && y == p.y;
      }
      return false;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 97 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
      hash = 97 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
      return hash;
    }
  }

  public enum NavigationFeeling {

    LOST,
    LEFT,
    RIGHT,
    LEFT_AHEAD,
    RIGHT_AHEAD,
    STRAIT_AHEAD,
    LEFT_AHEAD_CLOSE,
    RIGHT_AHEAD_CLOSE,
    STRAIT_AHEAD_CLOSE,
  }
}
