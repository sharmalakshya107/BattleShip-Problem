import java.util.List;
import java.util.Set;

public interface FireStrategy {
    Coordinate getNextTarget(List<Coordinate> opponentAreaCoords, Set<Coordinate> alreadyFired);
}


