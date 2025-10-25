import java.util.List;
import java.util.Set;

/**
 * Strategy interface for determining the next target coordinate to fire at.
 * <p>
 * This interface follows the Strategy design pattern, allowing different
 * firing algorithms to be plugged into the game. Implementations can range
 * from simple random selection to sophisticated AI algorithms.
 * </p>
 * 
 * <p><b>Usage Example:</b></p>
 * <pre>
 * FireStrategy strategy = new RandomFireStrategy();
 * Coordinate target = strategy.getNextTarget(opponentCoords, firedCoords);
 * </pre>
 * 
 * @see RandomFireStrategy
 * @author Battleship Game
 * @version 1.0
 */
public interface FireStrategy {
    /**
     * Determines the next coordinate to fire at in the opponent's territory.
     * 
     * @param opponentAreaCoords List of all coordinates within the opponent's territory
     * @param alreadyFired Set of coordinates that have already been fired at (should be excluded)
     * @return The next coordinate to target, or null if no valid targets remain
     */
    Coordinate getNextTarget(List<Coordinate> opponentAreaCoords, Set<Coordinate> alreadyFired);
}


