import java.util.HashSet;
import java.util.Set;

/**
 * Represents a square ship on the battlefield.
 * <p>
 * Ships are stationary, square-shaped objects that occupy multiple cells on the grid.
 * Each ship has:
 * <ul>
 *   <li>A unique identifier (id)</li>
 *   <li>An even size (width/height of the square)</li>
 *   <li>A center coordinate that determines its position</li>
 *   <li>An owner (Player A or Player B)</li>
 *   <li>A destroyed state (initially false, set to true when hit)</li>
 * </ul>
 * </p>
 * 
 * <p><b>Ship Placement:</b></p>
 * <p>
 * For a ship with size=2 centered at (1, 5), it occupies:
 * - (0, 4), (1, 4) (bottom row)
 * - (0, 5), (1, 5) (top row)
 * </p>
 * 
 * <p><b>Important:</b> Ships must have an even size for clear centering logic.</p>
 * 
 * @author Battleship Game
 * @version 1.0
 */
public class Ship {
    /** Unique identifier for this ship (e.g., "SH1", "DESTROYER") */
    private final String id;
    
    /** The width and height of this square ship (must be even) */
    private final int size;
    
    /** The center coordinate of this ship on the battlefield */
    private final Coordinate center;
    
    /** The name of the player who owns this ship ("A" or "B") */
    private final String ownerName;
    
    /** Flag indicating whether this ship has been destroyed */
    private boolean isDestroyed;
    
    /** Set of all coordinates occupied by this ship */
    private final Set<Coordinate> occupiedCells;

    /**
     * Constructs a new Ship with the specified properties.
     * <p>
     * The ship's occupied cells are calculated based on its center coordinate and size.
     * A ship with size N centered at (x, y) will occupy cells from:
     * (x - N/2, y - N/2) to (x + N/2 - 1, y + N/2 - 1)
     * </p>
     * 
     * @param id Unique identifier for the ship
     * @param size Width and height of the square ship (must be even)
     * @param x X-coordinate of the ship's center
     * @param y Y-coordinate of the ship's center
     * @param ownerName Name of the player owning this ship ("A" or "B")
     * @throws IllegalArgumentException if size is not an even number
     */
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

    /**
     * Calculates all coordinates occupied by this ship based on its center and size.
     * <p>
     * For example, a ship with size=2 centered at (1, 5) occupies:
     * [(0, 4), (1, 4), (0, 5), (1, 5)]
     * </p>
     * 
     * @return A set of Coordinate objects representing all cells occupied by this ship
     */
    private Set<Coordinate> calculateOccupiedCells() {
        Set<Coordinate> cells = new HashSet<>();
        int halfSize = this.size / 2;
        
        // Calculate the starting and ending coordinates
        int startX = this.center.x() - halfSize;
        int startY = this.center.y() - halfSize;
        int endX = this.center.x() + halfSize;
        int endY = this.center.y() + halfSize;

        // Add all cells within the square boundary (exclusive of end coordinates)
        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                cells.add(new Coordinate(i, j));
            }
        }
        return cells;
    }

    /**
     * Gets the unique identifier of this ship.
     * 
     * @return The ship's ID
     */
    public String getId() { return id; }
    
    /**
     * Gets the name of the player who owns this ship.
     * 
     * @return The owner's name ("A" or "B")
     */
    public String getOwnerName() { return ownerName; }
    
    /**
     * Checks if this ship has been destroyed.
     * 
     * @return true if the ship is destroyed, false otherwise
     */
    public boolean isDestroyed() { return isDestroyed; }
    
    /**
     * Marks this ship as destroyed.
     * <p>
     * Once a ship is hit in the game, it is immediately destroyed.
     * This method sets the isDestroyed flag to true.
     * </p>
     */
    public void destroy() { this.isDestroyed = true; }
    
    /**
     * Gets all coordinates occupied by this ship.
     * 
     * @return An immutable set of coordinates
     */
    public Set<Coordinate> getOccupiedCells() { return occupiedCells; }

    /**
     * Returns a string representation of this ship.
     * <p>
     * Format: "OwnerName-ShipID" (e.g., "A-SH1" or "B-DESTROYER")
     * </p>
     * 
     * @return A formatted string identifying the ship and its owner
     */
    @Override
    public String toString() {
        return String.format("%s-%s", this.ownerName, this.id);
    }
}


