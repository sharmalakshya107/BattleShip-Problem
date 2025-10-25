import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A simple random fire strategy implementation.
 * <p>
 * This strategy randomly selects a coordinate from the opponent's territory
 * that has not been fired at yet. It provides a basic, non-strategic approach
 * to the game, ensuring fair and unpredictable gameplay.
 * </p>
 * 
 * <p><b>Algorithm:</b></p>
 * <ol>
 *   <li>Filter out coordinates that have already been fired at</li>
 *   <li>Randomly select one coordinate from the remaining options</li>
 *   <li>Return null if no valid targets remain</li>
 * </ol>
 * 
 * @see FireStrategy
 * @author Battleship Game
 * @version 1.0
 */
public class RandomFireStrategy implements FireStrategy {
    /** Random number generator for selecting target coordinates */
    private final Random random = new Random();

    /**
     * Selects a random coordinate from the opponent's territory that hasn't been fired at.
     * 
     * @param opponentAreaCoords List of all coordinates in the opponent's territory
     * @param alreadyFired Set of coordinates that have already been targeted
     * @return A randomly selected valid coordinate, or null if no targets remain
     */
    @Override
    public Coordinate getNextTarget(List<Coordinate> opponentAreaCoords, Set<Coordinate> alreadyFired) {
        // Filter out coordinates that have already been fired at
        List<Coordinate> possibleTargets = opponentAreaCoords.stream()
                .filter(coord -> !alreadyFired.contains(coord))
                .collect(Collectors.toList());
        
        // Return null if no valid targets remain
        if (possibleTargets.isEmpty()) {
            return null;
        }
        
        // Select and return a random coordinate from the available targets
        return possibleTargets.get(random.nextInt(possibleTargets.size()));
    }
}


