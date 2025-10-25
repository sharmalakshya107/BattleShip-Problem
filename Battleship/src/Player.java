import java.util.*;

/**
 * Represents a player in the Battleship game.
 * <p>
 * Each player manages:
 * <ul>
 *   <li>A fleet of ships</li>
 *   <li>A designated territory (vertical half of the battlefield)</li>
 *   <li>A record of fired shots</li>
 * </ul>
 * </p>
 * 
 * <p><b>Territory Assignment:</b></p>
 * <ul>
 *   <li>Player A: columns [0, N/2-1] (left half)</li>
 *   <li>Player B: columns [N/2, N-1] (right half)</li>
 * </ul>
 * 
 * <p><b>Game Objective:</b></p>
 * <p>
 * The player with remaining ships wins. A player loses when all their ships are destroyed.
 * </p>
 * 
 * @author Battleship Game
 * @version 1.0
 */
public class Player {
    /** The player's name/identifier ("A" or "B") */
    private final String name;
    
    /** The horizontal boundary defining this player's territory */
    private final Boundary boundary;
    
    /** The size of the battlefield (N x N grid) */
    private final int battlefieldSize;
    
    /** Map of ship IDs to Ship objects representing this player's fleet */
    private final Map<String, Ship> fleet;
    
    /** Set of coordinates where this player has fired shots */
    private final Set<Coordinate> firedShots;

    /**
     * Constructs a new Player with the specified properties.
     * 
     * @param name The player's identifier ("A" or "B")
     * @param boundary The horizontal boundary defining the player's territory
     * @param battlefieldSize The size of the N x N battlefield
     */
    public Player(String name, Boundary boundary, int battlefieldSize) {
        this.name = name;
        this.boundary = boundary;
        this.battlefieldSize = battlefieldSize;
        this.fleet = new HashMap<>();
        this.firedShots = new HashSet<>();
    }

    /**
     * Adds a ship to this player's fleet.
     * <p>
     * The ship is stored in a map with its ID as the key, allowing
     * for quick lookup and management of the fleet.
     * </p>
     * 
     * @param ship The ship to add to the fleet
     */
    public void addShipToFleet(Ship ship) {
        this.fleet.put(ship.getId(), ship);
    }
    
    /**
     * Counts the number of ships in the fleet that are not destroyed.
     * <p>
     * This method uses Java streams to filter and count ships.
     * A player loses when this count reaches zero.
     * </p>
     * 
     * @return The number of remaining (non-destroyed) ships
     */
    public long getRemainingShips() {
        return fleet.values().stream().filter(ship -> !ship.isDestroyed()).count();
    }
    
    /**
     * Gets all coordinates within this player's territory.
     * <p>
     * This includes all cells in the player's designated columns,
     * spanning all rows of the battlefield. Used by the fire strategy
     * to determine valid targets in the opponent's territory.
     * </p>
     * 
     * @return A list of all coordinates in this player's territory
     */
    public List<Coordinate> getAllTerritoryCoords() {
        List<Coordinate> coords = new ArrayList<>();
        // Iterate through all columns in the player's boundary
        for (int x = boundary.minCol(); x <= boundary.maxCol(); x++) {
            // Iterate through all rows on the battlefield
            for (int y = 0; y < battlefieldSize; y++) {
                coords.add(new Coordinate(x, y));
            }
        }
        return coords;
    }
    
    /**
     * Gets the player's name/identifier.
     * 
     * @return The player's name ("A" or "B")
     */
    public String getName() { return name; }
    
    /**
     * Gets the player's fleet of ships.
     * 
     * @return A map of ship IDs to Ship objects
     */
    public Map<String, Ship> getFleet() { return fleet; }
    
    /**
     * Gets the set of coordinates where this player has fired.
     * 
     * @return A set of previously fired coordinates
     */
    public Set<Coordinate> getFiredShots() { return firedShots; }
}

