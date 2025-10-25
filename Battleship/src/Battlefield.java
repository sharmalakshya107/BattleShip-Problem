/**
 * Represents the N x N battlefield grid where ships are placed and battles occur.
 * <p>
 * The battlefield is a square grid that:
 * </p>
 * <ul>
 *   <li>Stores ship positions in a 2D array</li>
 *   <li>Validates ship placements (bounds, territory, overlap)</li>
 *   <li>Provides ship lookup by coordinate</li>
 * </ul>
 * 
 * <p><b>Grid Layout:</b></p>
 * <p>
 * The grid uses standard Cartesian coordinates:
 * - X-axis: columns (0 to N-1, left to right)
 * - Y-axis: rows (0 to N-1, bottom to top)
 * - Array indexing: grid[y][x] (row-major order)
 * </p>
 * 
 * <p><b>Territory Division:</b></p>
 * <ul>
 *   <li>Player A: columns [0, N/2-1]</li>
 *   <li>Player B: columns [N/2, N-1]</li>
 * </ul>
 * 
 * @author Battleship Game
 * @version 1.0
 */
public class Battlefield {
    /** The size of the square battlefield (N x N) */
    private final int size;
    
    /** 
     * 2D grid storing ship references at each coordinate.
     * grid[y][x] contains the Ship at coordinate (x, y), or null if empty.
     */
    private final Ship[][] grid;

    /**
     * Constructs a new empty battlefield with the specified size.
     * 
     * @param n The size of the square battlefield (must be positive and even)
     */
    public Battlefield(int n) {
        this.size = n;
        this.grid = new Ship[n][n];
    }

    /**
     * Places a ship on the battlefield after performing comprehensive validation.
     * <p>
     * This method validates:
     * </p>
     * <ol>
     *   <li><b>Bounds checking:</b> All ship cells must be within [0, N-1] range</li>
     *   <li><b>Territory checking:</b> All ship cells must be within the owner's boundary</li>
     *   <li><b>Overlap checking:</b> No ship cell can overlap with an existing ship</li>
     * </ol>
     * 
     * <p><b>Important:</b> Ships can touch at edges but cannot overlap cells.</p>
     * 
     * @param ship The ship to place on the battlefield
     * @param boundary The territory boundary that the ship must stay within
     * @throws IllegalArgumentException if validation fails (out of bounds, wrong territory, or overlap)
     */
    public void placeShip(Ship ship, Boundary boundary) {
        // First pass: validate all cells before placing anything
        for (Coordinate cell : ship.getOccupiedCells()) {
            int x = cell.x();
            int y = cell.y();
            
            // Check if the coordinate is within battlefield bounds
            if (!(0 <= x && x < size && 0 <= y && y < size)) {
                throw new IllegalArgumentException("Ship '" + ship.getId() + "' is out of the battlefield bounds.");
            }
            
            // Check if the coordinate is within the owner's territory
            if (!(boundary.minCol() <= x && x <= boundary.maxCol())) {
                throw new IllegalArgumentException("Ship '" + ship.getId() + "' is out of its owner's territory.");
            }
            
            // Check if the coordinate is already occupied by another ship
            if (grid[y][x] != null) {
                throw new IllegalArgumentException("Ship '" + ship.getId() + "' overlaps with another ship at (" + x + ", " + y + ").");
            }
        }
        
        // Second pass: place the ship (only after all validations pass)
        for (Coordinate cell : ship.getOccupiedCells()) {
            grid[cell.y()][cell.x()] = ship;
        }
    }

    /**
     * Retrieves the ship located at the specified coordinate.
     * <p>
     * This method is used during combat to determine if a fired shot hits a ship.
     * </p>
     * 
     * @param x The x-coordinate (column)
     * @param y The y-coordinate (row)
     * @return The Ship at the specified location, or null if the cell is empty or out of bounds
     */
    public Ship getShipAt(int x, int y) {
        if (0 <= x && x < size && 0 <= y && y < size) {
            return grid[y][x];
        }
        return null;
    }

    /**
     * Gets the size of the battlefield.
     * 
     * @return The size N of the N x N battlefield
     */
    public int getSize() { return size; }
    
    /**
     * Gets the internal grid representation.
     * <p>
     * Used for visualization and debugging. Direct access allows
     * the Game class to display the battlefield state.
     * </p>
     * 
     * @return The 2D array of Ship references
     */
    public Ship[][] getGrid() { return grid; }
}

