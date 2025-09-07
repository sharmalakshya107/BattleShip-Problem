import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;

public class Game {
    private Battlefield battlefield;
    private Map<String, Player> players;
    private String currentTurn;
    private boolean isGameOver;
    private final FireStrategy fireStrategy;
    private Set<Coordinate> allFiredCoordinates;

    public Game() {
        this.players = new HashMap<>();
        this.fireStrategy = new RandomFireStrategy();
    }

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

    public void addShip(String shipId, int size, int xA, int yA, int xB, int yB) {
        if (this.battlefield == null) {
            System.out.println("Error: Game not initialized. Please call initGame(N) first.");
            return;
        }
        try {
            Player playerA = players.get("A");
            Ship shipA = new Ship(shipId, size, xA, yA, "A");
            battlefield.placeShip(shipA, new Boundary(0, battlefield.getSize() / 2 - 1));
            playerA.addShipToFleet(shipA);

            Player playerB = players.get("B");
            Ship shipB = new Ship(shipId, size, xB, yB, "B");
            battlefield.placeShip(shipB, new Boundary(battlefield.getSize() / 2, battlefield.getSize() - 1));
            playerB.addShipToFleet(shipB);

            System.out.println("Ship '" + shipId + "' of size " + size + "x" + size + " added for both players.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error adding ship '" + shipId + "': " + e.getMessage());
        }
    }

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

            Coordinate target = fireStrategy.getNextTarget(opponent.getAllTerritoryCoords(), allFiredCoordinates);

            if (target == null) {
                System.out.println("No more coordinates to fire at. Game is a draw.");
                isGameOver = true;
                continue;
            }
            allFiredCoordinates.add(target);

            Ship hitShip = battlefield.getShipAt(target.x(), target.y());
            String resultMsg = "\"Miss\"";
            if (hitShip != null && hitShip.getOwnerName().equals(opponent.getName())) {
                hitShip.destroy();
                resultMsg = "\"Hit\" " + opponent.getName() + "-" + hitShip.getId() + " destroyed";
            }

            String attackerDisplay = attacker.getName().equals("A") ? "PlayerA" : "PlayerB";
            long shipsA = players.get("A").getRemainingShips();
            long shipsB = players.get("B").getRemainingShips();
            System.out.printf(
                "%s's turn: Missile fired at (%d, %d) : %s : Ships Remaining - PlayerA:%d, PlayerB:%d%n",
                attackerDisplay, target.x(), target.y(), resultMsg, shipsA, shipsB
            );

            if (opponent.getRemainingShips() == 0) {
                System.out.printf("%nGameOver. %s wins.%n", attackerDisplay);
                isGameOver = true;
            } else {
                currentTurn = opponentId;
            }
        }
    }
    
    public void viewBattleField() {
        if (battlefield == null) {
            System.out.println("Error: Game not initialized.");
            return;
        }
        System.out.println("\n--- Current Battlefield ---");
        int n = battlefield.getSize();
        Ship[][] grid = battlefield.getGrid();
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

    public static void main(String[] args) {
        Game myGame = new Game();
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter battlefield size N (even): ");
            int n = scanner.nextInt();
            myGame.initGame(n);
            if (myGame.battlefield == null) {
                return;
            }

            System.out.print("Enter number of ships to add (equal for A and B): ");
            int count = scanner.nextInt();
            scanner.nextLine();
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
                scanner.nextLine();
                myGame.addShip(id, size, xA, yA, xB, yB);
            }

            System.out.print("View battlefield before starting? (y/n): ");
            String view = scanner.nextLine().trim();
            if (view.equalsIgnoreCase("y") || view.equalsIgnoreCase("yes")) {
                myGame.viewBattleField();
            }

            myGame.startGame();
        } finally {
            scanner.close();
        }
    }
}