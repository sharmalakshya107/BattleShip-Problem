import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class RandomFireStrategy implements FireStrategy {
    private final Random random = new Random();

    @Override
    public Coordinate getNextTarget(List<Coordinate> opponentAreaCoords, Set<Coordinate> alreadyFired) {
        List<Coordinate> possibleTargets = opponentAreaCoords.stream()
                .filter(coord -> !alreadyFired.contains(coord))
                .collect(Collectors.toList());
        if (possibleTargets.isEmpty()) {
            return null;
        }
        return possibleTargets.get(random.nextInt(possibleTargets.size()));
    }
}


