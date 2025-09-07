import java.util.HashSet;
import java.util.Set;

public class Ship {
    private final String id;
    private final int size;
    private final Coordinate center;
    private final String ownerName;
    private boolean isDestroyed;
    private final Set<Coordinate> occupiedCells;

    public Ship(String id, int size, int x, int y, String ownerName) {
        if (size % 2 != 0) {
            throw new IllegalArgumentException("Ship size must be an even number for clear centering.");
        }
        this.id = id;
        this.size = size;
        this.center = new Coordinate(x, y);
        this.ownerName = ownerName;
        this.isDestroyed = false;
        this.occupiedCells = calculateOccupiedCells();
    }

    private Set<Coordinate> calculateOccupiedCells() {
        Set<Coordinate> cells = new HashSet<>();
        int halfSize = this.size / 2;
        int startX = this.center.x() - halfSize;
        int startY = this.center.y() - halfSize;
        int endX = this.center.x() + halfSize;
        int endY = this.center.y() + halfSize;

        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                cells.add(new Coordinate(i, j));
            }
        }
        return cells;
    }

    public String getId() { return id; }
    public String getOwnerName() { return ownerName; }
    public boolean isDestroyed() { return isDestroyed; }
    public void destroy() { this.isDestroyed = true; }
    public Set<Coordinate> getOccupiedCells() { return occupiedCells; }

    @Override
    public String toString() {
        return String.format("%s-%s", this.ownerName, this.id);
    }
}


