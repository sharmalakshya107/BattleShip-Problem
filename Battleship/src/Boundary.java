/**
 * Represents a horizontal boundary defining a player's territory on the battlefield.
 * <p>
 * The battlefield is divided vertically into two halves:
 * <ul>
 *   <li>Player A owns columns [0, N/2-1]</li>
 *   <li>Player B owns columns [N/2, N-1]</li>
 * </ul>
 * This boundary ensures that ships can only be placed within their owner's designated territory.
 * </p>
 * 
 * @param minCol The minimum column index (inclusive) that a player can use
 * @param maxCol The maximum column index (inclusive) that a player can use
 * 
 * @author Battleship Game
 * @version 1.0
 */
public record Boundary(int minCol, int maxCol) {}


