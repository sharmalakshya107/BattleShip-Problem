public class Battlefield {
    private final int size;
    private final Ship[][] grid;

    public Battlefield(int n) {
        this.size = n;
        this.grid = new Ship[n][n];
    }

    public void placeShip(Ship ship, Boundary boundary) {
        for (Coordinate cell : ship.getOccupiedCells()) {
            int x = cell.x();
            int y = cell.y();
            if (!(0 <= x && x < size && 0 <= y && y < size)) {
                throw new IllegalArgumentException("Ship '" + ship.getId() + "' is out of the battlefield bounds.");
            }
            if (!(boundary.minCol() <= x && x <= boundary.maxCol())) {
                throw new IllegalArgumentException("Ship '" + ship.getId() + "' is out of its owner's territory.");
            }
            if (grid[y][x] != null) {
                throw new IllegalArgumentException("Ship '" + ship.getId() + "' overlaps with another ship at (" + x + ", " + y + ").");
            }
        }
        for (Coordinate cell : ship.getOccupiedCells()) {
            grid[cell.y()][cell.x()] = ship;
        }
    }

    public Ship getShipAt(int x, int y) {
        if (0 <= x && x < size && 0 <= y && y < size) {
            return grid[y][x];
        }
        return null;
    }

    public int getSize() { return size; }
    public Ship[][] getGrid() { return grid; }
}

