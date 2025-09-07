# Battleship (SSE Assignment)

A two-player Battleship game played on an N x N grid. The battlefield is split vertically: left half for PlayerA and right half for PlayerB. Ships are square, stationary, and cannot overlap. Players fire alternately using a pluggable "fire strategy" (default: random) until one player's fleet is destroyed.

## Requirements
- Java 16+ (records are used)
- Any shell capable of running `javac`/`java`

## Project Structure
```
src/
  Battlefield.java
  Boundary.java
  Coordinate.java
  FireStrategy.java
  RandomFireStrategy.java
  Player.java
  Ship.java
  Game.java
```

## Build and Run
From the project root:

```powershell
javac -d out src\*.java
java -cp out Game
```

You will be prompted for inputs interactively.

## Sample Input
Paste the following when prompted to reproduce the example run:

```
6
1
SH1
2
1 5
4 4
y
```

- N = 6 (even)
- 1 ship, id `SH1`, size `2`
- PlayerA center at `(1, 5)` and PlayerB center at `(4, 4)`
- `y` to view the battlefield before starting the game

## Expected Behavior (overview)
- Battlefield initializes and splits columns `[0..N/2-1]` to PlayerA and `[N/2..N-1]` to PlayerB
- Ship placement validates: in-bounds, within owner territory, non-overlapping (touching edges allowed)
- Turns alternate: PlayerA then PlayerB
- A fired coordinate is never reused globally
- On hit: the opponent ship is destroyed
- Game ends when one player has zero ships remaining

## APIs (as per spec)
- `initGame(N)` — Initializes battlefield N x N, assigns halves to A/B
- `addShip(id, size, xA, yA, xB, yB)` — Adds same ship id/size for both players at their respective centers
- `startGame()` — Starts alternating turns (PlayerA first) using the current fire strategy
- `viewBattleField()` — Prints N x N grid, marking occupied cells as `A-<id>` or `B-<id>`

## Design Notes
- Data layer: `Coordinate`, `Boundary`, `Ship`, `Player`, `Battlefield`
- Orchestration: `Game`
- Strategy: `FireStrategy` interface with `RandomFireStrategy` default (easily replaceable)
- Validation: even N, in-bounds, correct territory, no overlaps
- Global set of fired coordinates to prevent duplicate shots

## Error Handling
- Clear console errors for invalid N, uninitialized game, or invalid ship placement

## Customization
- Add multiple ships interactively
- Implement a smarter strategy by adding a new class that implements `FireStrategy`
- For deterministic demos, implement a seeded strategy

## Testing Ideas
- Unit tests for placement and hit detection
- Deterministic strategy for reproducible turn sequences
- Snapshot-style checks for `viewBattleField()` output
