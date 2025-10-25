/**
 * Represents a 2D coordinate on the battlefield grid.
 * <p>
 * This immutable record stores the x (column) and y (row) position of a cell
 * on the battlefield. Coordinates are 0-indexed, with (0, 0) representing 
 * the bottom-left corner of the grid.
 * </p>
 * 
 * @param x The x-coordinate (column index), must be in range [0, N-1] where N is battlefield size
 * @param y The y-coordinate (row index), must be in range [0, N-1] where N is battlefield size
 * 
 * @author Battleship Game
 * @version 1.0
 */
public record Coordinate(int x, int y) {}


