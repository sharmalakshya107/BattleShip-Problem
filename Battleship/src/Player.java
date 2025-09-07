import java.util.*;

public class Player {
    private final String name;
    private final Boundary boundary;
    private final int battlefieldSize;
    private final Map<String, Ship> fleet;
    private final Set<Coordinate> firedShots;

    public Player(String name, Boundary boundary, int battlefieldSize) {
        this.name = name;
        this.boundary = boundary;
        this.battlefieldSize = battlefieldSize;
        this.fleet = new HashMap<>();
        this.firedShots = new HashSet<>();
    }

    public void addShipToFleet(Ship ship) {
        this.fleet.put(ship.getId(), ship);
    }
    
    public long getRemainingShips() {
        return fleet.values().stream().filter(ship -> !ship.isDestroyed()).count();
    }
    
    public List<Coordinate> getAllTerritoryCoords() {
        List<Coordinate> coords = new ArrayList<>();
        for (int x = boundary.minCol(); x <= boundary.maxCol(); x++) {
            for (int y = 0; y < battlefieldSize; y++) {
                coords.add(new Coordinate(x, y));
            }
        }
        return coords;
    }
    
    public String getName() { return name; }
    public Map<String, Ship> getFleet() { return fleet; }
    public Set<Coordinate> getFiredShots() { return firedShots; }
}

