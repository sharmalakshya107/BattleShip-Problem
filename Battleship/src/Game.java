import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;

/**
 * The main Game class that orchestrates the Battleship game.
 * <p>
 * This class manages:
 * <ul>
 *   <li>Game initialization and configuration</li>
 *   <li>Player management and turn alternation</li>
 *   <li>Ship placement and validation</li>
 *   <li>Combat mechanics (firing and hit detection)</li>
 *   <li>Win/loss conditions</li>
 *   <li>User interface (console I/O)</li>
 * </ul>
 * </p>
 * 
 * <p><b>Game Flow:</b></p>
 * <ol>
 *   <li>Initialize battlefield with size N (must be even)</li>
 *   <li>Add ships for both players (same ship types for fairness)</li>
 *   <li>Optionally view the battlefield layout</li>
 *   <li>Start the game (players alternate turns)</li>
 *   <li>Game ends when one player has no ships remaining</li>
 * </ol>
 * 
 * <p><b>Design Patterns:</b></p>
 * <ul>
 *   <li><b>Strategy Pattern:</b> FireStrategy for pluggable firing algorithms</li>
 *   <li><b>Orchestrator Pattern:</b> Game class coordinates all components</li>
 * </ul>
 * 
 * @see Battlefield
 * @see Player
 * @see Ship
 * @see FireStrategy
 * @author Battleship Game
 * @version 1.0
 */
public class Game {
    /** The battlefield grid where ships are placed and battles occur */
    private Battlefield battlefield;
    
    /** Map of player identifiers ("A" and "B") to Player objects */
    private Map<String, Player> players;
    
    /** Identifier of the player whose turn it is ("A" or "B") */
    private String currentTurn;
    
    /** Flag indicating whether the game has ended */
    private boolean isGameOver;
    
    /** Strategy for determining where to fire next */
    private final FireStrategy fireStrategy;
    
    /** 
     * Global set of all coordinates that have been fired at by either player.
     * Prevents duplicate shots and ensures fair gameplay.
     */
    private Set<Coordinate> allFiredCoordinates;

    /**
     * Constructs a new Game with default settings.
     * <p>
     * Initializes an empty player map and sets the default fire strategy
     * to RandomFireStrategy. The battlefield is not initialized until
     * initGame() is called.
     * </p>
     */
    public Game() {
        this.players = new HashMap<>();
        this.fireStrategy = new RandomFireStrategy();
    }

    /**
     * Initializes the game with an N x N battlefield.
     * <p>
     * This method:
     * <ol>
     *   <li>Validates that N is positive and even</li>
     *   <li>Creates a new battlefield</li>
     *   <li>Initializes two players with their respective territories</li>
     *   <li>Resets game state (not game over, no fired coordinates)</li>
     * </ol>
     * </p>
     * 
     * <p><b>Territory Assignment:</b></p>
     * <ul>
     *   <li>Player A: columns [0, N/2-1]</li>
     *   <li>Player B: columns [N/2, N-1]</li>
     * </ul>
     * 
     * @param n The size of the battlefield (must be positive and even)
     */
    public void initGame(int n) {
        if (n <= 0 || n % 2 != 0) {
            System.out.println("Error: Battlefield size 'N' must be a positive even integer.");
            return;
        }
        this.battlefield = new Battlefield(n);
        this.players.put("A", new Player("A", new Boundary(0, n / 2 - 1), n));
        this.players.put("B", new Player("B", new Boundary(n / 2, n - 1), n));
        this.isGameOver = false;
        this.allFiredCoordinates = new HashSet<>();
        System.out.println("Game initialized with a " + n + "x" + n + " battlefield.");
    }

    /**
     * Adds a ship to both players' fleets.
     * <p>
     * For fairness, both players receive the same ship type (same ID and size)
     * but positioned at different coordinates within their respective territories.
     * </p>
     * 
     * <p><b>Validation includes:</b></p>
     * <ul>
     *   <li>Ship is within battlefield bounds</li>
     *   <li>Ship is within the owner's territory</li>
     *   <li>Ship does not overlap with existing ships</li>
     * </ul>
     * 
     * @param shipId Unique identifier for the ship (e.g., "SH1", "DESTROYER")
     * @param size The width/height of the square ship (must be even)
     * @param xA X-coordinate of Player A's ship center
     * @param yA Y-coordinate of Player A's ship center
     * @param xB X-coordinate of Player B's ship center
     * @param yB Y-coordinate of Player B's ship center
     */
    public void addShip(String shipId, int size, int xA, int yA, int xB, int yB) {
        if (this.battlefield == null) {
            System.out.println("Error: Game not initialized. Please call initGame(N) first.");
            return;
        }
        try {
            // Create and place ship for Player A
            Player playerA = players.get("A");
            Ship shipA = new Ship(shipId, size, xA, yA, "A");
            battlefield.placeShip(shipA, new Boundary(0, battlefield.getSize() / 2 - 1));
            playerA.addShipToFleet(shipA);

            // Create and place ship for Player B
            Player playerB = players.get("B");
            Ship shipB = new Ship(shipId, size, xB, yB, "B");
            battlefield.placeShip(shipB, new Boundary(battlefield.getSize() / 2, battlefield.getSize() - 1));
            playerB.addShipToFleet(shipB);

            System.out.println("Ship '" + shipId + "' of size " + size + "x" + size + " added for both players.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error adding ship '" + shipId + "': " + e.getMessage());
        }
    }

    /**
     * Starts the game and manages turn-based combat until one player wins.
     * <p>
     * Game loop:
     * <ol>
     *   <li>Current player fires at opponent's territory using the fire strategy</li>
     *   <li>Check if shot hits a ship; if so, destroy it</li>
     *   <li>Display turn results and remaining ship counts</li>
     *   <li>Check win condition (opponent has zero ships)</li>
     *   <li>Switch turns and repeat</li>
     * </ol>
     * </p>
     * 
     * <p><b>Important:</b> Player A always goes first.</p>
     * 
     * <p><b>Shot Rules:</b></p>
     * <ul>
     *   <li>Each coordinate can only be fired at once (globally tracked)</li>
     *   <li>Any hit immediately destroys the entire ship</li>
     *   <li>Game ends when a player has zero remaining ships</li>
     * </ul>
     */
    public void startGame() {
        if (battlefield == null || players.get("A").getFleet().isEmpty()) {
            System.out.println("Error: Cannot start game. Initialize and add ships first.");
            return;
        }
        this.currentTurn = "A";
        System.out.println("\n--- Game Started ---");

        while (!isGameOver) {
            Player attacker = players.get(currentTurn);
            String opponentId = currentTurn.equals("A") ? "B" : "A";
            Player opponent = players.get(opponentId);

            // Use fire strategy to select the next target in opponent's territory
            Coordinate target = fireStrategy.getNextTarget(opponent.getAllTerritoryCoords(), allFiredCoordinates);

            // Check if there are no more valid targets (draw condition)
            if (target == null) {
                System.out.println("No more coordinates to fire at. Game is a draw.");
                isGameOver = true;
                continue;
            }
            allFiredCoordinates.add(target);

            // Check if the shot hits a ship
            Ship hitShip = battlefield.getShipAt(target.x(), target.y());
            String resultMsg = "\"Miss\"";
            if (hitShip != null && hitShip.getOwnerName().equals(opponent.getName())) {
                hitShip.destroy();
                resultMsg = "\"Hit\" " + opponent.getName() + "-" + hitShip.getId() + " destroyed";
            }

            // Display turn results
            String attackerDisplay = attacker.getName().equals("A") ? "PlayerA" : "PlayerB";
            long shipsA = players.get("A").getRemainingShips();
            long shipsB = players.get("B").getRemainingShips();
            System.out.printf(
                "%s's turn: Missile fired at (%d, %d) : %s : Ships Remaining - PlayerA:%d, PlayerB:%d%n",
                attackerDisplay, target.x(), target.y(), resultMsg, shipsA, shipsB
            );

            // Check win condition
            if (opponent.getRemainingShips() == 0) {
                System.out.printf("%nGameOver. %s wins.%n", attackerDisplay);
                isGameOver = true;
            } else {
                // Switch turns
                currentTurn = opponentId;
            }
        }
    }
    
    /**
     * Displays the current state of the battlefield in a grid format.
     * <p>
     * The grid shows:
     * <ul>
     *   <li>Empty cells as blank spaces</li>
     *   <li>Occupied cells with ship identifiers (e.g., "A-SH1", "B-DESTROYER")</li>
     * </ul>
     * </p>
     * 
     * <p><b>Display Format:</b></p>
     * <p>
     * The grid is printed from top to bottom (highest Y to lowest Y),
     * matching the Cartesian coordinate system visualization.
     * </p>
     */
    public void viewBattleField() {
        if (battlefield == null) {
            System.out.println("Error: Game not initialized.");
            return;
        }
        System.out.println("\n--- Current Battlefield ---");
        int n = battlefield.getSize();
        Ship[][] grid = battlefield.getGrid();
        
        // Print from highest Y to lowest Y (top to bottom)
        for (int y = n - 1; y >= 0; y--) {
            System.out.print("|");
            for (int x = 0; x < n; x++) {
                Ship cell = grid[y][x];
                if (cell != null) {
                    System.out.printf(" %-6s|", cell.toString());
                } else {
                    System.out.print("       |");
                }
            }
            System.out.println();
        }
        System.out.println("-----------------------------------------------------");
    }

    /**
     * Main entry point for the Battleship game.
     * <p>
     * This method:
     * <ol>
     *   <li>Prompts user for battlefield size</li>
     *   <li>Prompts user to add ships with their positions</li>
     *   <li>Optionally displays the battlefield</li>
     *   <li>Starts the game</li>
     * </ol>
     * </p>
     * 
     * <p><b>Interactive Prompts:</b></p>
     * <ul>
     *   <li>Battlefield size (must be even)</li>
     *   <li>Number of ships to add</li>
     *   <li>For each ship: ID, size, and center coordinates for both players</li>
     *   <li>Option to view battlefield before starting</li>
     * </ul>
     * 
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        Game myGame = new Game();
        Scanner scanner = new Scanner(System.in);
        try {
            // Initialize the battlefield
            System.out.print("Enter battlefield size N (even): ");
            int n = scanner.nextInt();
            myGame.initGame(n);
            if (myGame.battlefield == null) {
                return;
            }

            // Add ships for both players
            System.out.print("Enter number of ships to add (equal for A and B): ");
            int count = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            for (int i = 0; i < count; i++) {
                System.out.printf("Ship %d - Enter id (e.g., SH1): ", i + 1);
                String id = scanner.nextLine().trim();
                
                System.out.print("  Enter size (even square width, e.g., 2,4,...): ");
                int size = scanner.nextInt();
                
                System.out.print("  Enter PlayerA ship center x y (space-separated): ");
                int xA = scanner.nextInt();
                int yA = scanner.nextInt();
                
                System.out.print("  Enter PlayerB ship center x y (space-separated): ");
                int xB = scanner.nextInt();
                int yB = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                myGame.addShip(id, size, xA, yA, xB, yB);
            }

            // Optionally view the battlefield before starting
            System.out.print("View battlefield before starting? (y/n): ");
            String view = scanner.nextLine().trim();
            if (view.equalsIgnoreCase("y") || view.equalsIgnoreCase("yes")) {
                myGame.viewBattleField();
            }

            // Start the game
            myGame.startGame();
        } finally {
            scanner.close();
        }
    }
}