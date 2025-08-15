# Rubik's Cube Simulator

A feature-rich 3D Rubik's Cube simulator built with JavaFX, providing interactive visualization, solving algorithms, and comprehensive move tracking.

## Table of Contents
- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [Architecture](#architecture)
- [Solving Algorithm](#solving-algorithm)
- [Controls and Interface](#controls-and-interface)
- [Technical Implementation](#technical-implementation)
- [Development](#development)

## Features

### Core Functionality
- **3D Interactive Visualization**: Fully rotatable 3D cube with realistic lighting and shading
- **Layer-by-Layer Solver**: 7-phase solving algorithm with step-by-step execution
- **Move History Tree**: Complete move tracking with branching paths and undo/redo capabilities
- **Multiple View Angles**: 8 predefined viewing positions (all cube corners)
- **Drag-to-Rotate**: Intuitive mouse controls for cube and face rotations
- **Scramble Generator**: Pre-loaded scramble patterns for practice
- **Explode View**: Animated explosion effect to see all faces simultaneously

### Advanced Features
- **Move Notation Support**: Standard Rubik's Cube notation (F, B, U, D, L, R with modifiers)
- **Visual Move Feedback**: Real-time rotation animations
- **Console Output**: Colored terminal representation using ANSI colors
- **Texture Mapping**: Individual facelet textures for enhanced visual clarity
- **Group-based Actions**: Organize moves into logical groups with comments

## Requirements

- **Java**: JDK 21 or higher
- **Maven**: 3.6 or higher
- **JavaFX**: Version 21 (included as Maven dependency)
- **Operating System**: Cross-platform (Windows, macOS, Linux)

## Installation

### Clone the Repository
```bash
git clone https://github.com/glandais/rubiks-cube.git
cd rubiks-cube
```

### Build the Project
```bash
# Clean and compile
mvn clean compile

# Create executable JAR with all dependencies
mvn clean package
```

## Usage

### Running the Application

#### Via Maven
```bash
mvn compile exec:java -Dexec.mainClass="io.github.glandais.rubikscube.Main"
```

#### Via JAR
```bash
java -jar target/rubiks-cube-1.0-SNAPSHOT.jar
```

### Interface Overview

The application window is divided into several sections:

#### Top Toolbar
- **Rotation Controls**: Direct face rotation buttons (F, B, U, D, L, R) with normal, double (2), and reverse (') modifiers
- **Undo/Redo**: Navigate through move history
- **Reset**: Return to solved state
- **Scramble**: Apply random scramble pattern
- **Solve**: Execute full solving algorithm
- **View Angles**: 8 corner view presets (fru, rbu, blu, lfu, fld, rfd, brd, lbd)

#### Left Sidebar - Solving Phases
Step-by-step solving buttons:
1. **White edges**: Position white edge pieces
2. **First layer**: Complete white face with corners
3. **Second layer**: Position middle layer edges
4. **Yellow cross**: Form cross on yellow face
5. **Yellow edges**: Position yellow edge pieces
6. **Yellow corners**: Position yellow corner pieces
7. **Orient yellow corners**: Final corner orientation

#### Right Sidebar
- **Move History Tree**: Hierarchical view of all moves with branching paths
- **Navigation Controls**: Move through history (<<<, <<, <, >, >>, >>>)
- **Algorithm Library**: Pre-programmed move sequences for each solving phase

#### Center View
- **3D Cube**: Interactive 3D model
- **Mouse Controls**:
  - Left-click and drag: Rotate entire cube
  - Right-click on facelet: Rotate face
  - Scroll: Zoom in/out

## Architecture

### Package Structure

```
io.github.glandais.rubikscube/
├── Main.java                    # Application entry point
├── jfx/                         # JavaFX UI layer
│   ├── RubiksCubeApplication    # Main UI setup
│   ├── RubiksCubeInteract       # User interaction handler
│   ├── model/                   # UI-specific models
│   │   ├── TreeViewItem         # Move history tree nodes
│   │   ├── Moves                # Move sequence parser
│   │   └── FaceletRotation      # Rotation animations
│   └── scene/                   # 3D scene components
│       ├── Cube3                # 3D cube container
│       ├── Cube                 # Individual cube piece
│       ├── Facelet              # Single face element
│       └── RubiksCubeView       # Scene manager
├── model/                       # Core cube logic
│   ├── Cube3Model               # Cube state representation
│   ├── FaceletEnum              # 48 facelet positions
│   ├── SideEnum                 # 6 cube sides
│   ├── rotation/                # Rotation system
│   │   ├── RotationEnum         # 18 possible rotations
│   │   ├── AxisEnum             # X, Y, Z axes
│   │   └── moves/               
│   │       └── RotationMoves    # Pre-computed position mappings
│   └── view/                   # View transformations
│       ├── CubeVisibleOrientation
│       └── RotatedCubes         # View-dependent position mapping
├── solver/                      # Solving algorithms
│   ├── DummySolverInstance      # Main solver implementation
│   ├── Scrambler                # Scramble pattern generator
│   └── SolveMoves               # Move sequence container
└── print/                       # Console output
    └── ConsolePrinter           # ANSI colored terminal display
```

### Core Design Patterns

#### 1. State Representation
The cube state is stored as a byte array of 48 elements, mapping each position to a facelet value:
```java
final byte[] facelets = new byte[48];
```
- Positions 0-47 represent the 48 visible facelets (excluding centers)
- Each byte stores which original facelet is currently at that position
- Efficient for transformations and comparisons

#### 2. Rotation System
Rotations use pre-computed position mappings for O(1) transformation:
```java
byte[] newPosition = RotationMoves.NEW_POSITIONS[rotation.ordinal()];
```
- Each rotation has a corresponding array showing new positions
- Supports 18 rotations: 6 faces × 3 modifiers (normal, double, reverse)

#### 3. View Transformation
Multiple viewing angles are supported through coordinate transformation:
```java
CubeVisibleOrientation.DEFAULT  // Front-Right-Up view
CubeVisibleOrientation.DOWN_FRONT // Different corner perspective
```

#### 4. Move History Tree
Branching history allows exploration of different solution paths:
```java
TreeItem<TreeViewItem> root
├── TreeViewMoves("Scramble")
│   └── TreeViewMove("F R U' R'")
└── TreeViewMoves("Solution")
    ├── TreeViewMove("Phase 1")
    └── TreeViewMove("Phase 2")
```

## Solving Algorithm

The solver implements the **Layer-by-Layer (LBL)** method, a beginner-friendly approach that solves the cube in 7 distinct phases:

### Phase 1: White Edges (Cross)
**Goal**: Position white edge pieces to form a cross on the bottom face

**Strategy**:
- Locate each white edge piece
- Move to correct position using shortest path
- Preserve already-placed edges

**Common Algorithms**:
- From top: `F U' R U`
- From bottom: `F' U' R U`
- From right: `U' R U`
- From left: `U L' U'`

### Phase 2: First Layer (White Corners)
**Goal**: Complete the white face by positioning corner pieces

**Strategy**:
- Position corner above target location
- Use insertion algorithm based on orientation

**Algorithms**:
- Right insert: `R' D' R`
- Front insert: `F D F'`
- Reorient: `R2 D' R2 D R2`

### Phase 3: Second Layer
**Goal**: Position the four edge pieces of the middle layer

**Strategy**:
- Identify edges without yellow color
- Position above target slot
- Insert using appropriate algorithm

**Algorithms**:
- Right insertion: `U R U' R' U' F' U F`
- Left insertion: `U' L' U L U F U' F'`
- Fix wrong orientation: Apply algorithm twice

### Phase 4: Yellow Cross
**Goal**: Form a cross pattern on the yellow (top) face

**Strategy**:
- Identify current pattern (dot, L-shape, line)
- Apply algorithm to progress toward cross

**Algorithm**:
- Standard: `F R U R' U' F'`
- Alternative: `F U R U' R' F'`

### Phase 5: Yellow Edges (OLL Edges)
**Goal**: Position yellow edges in correct locations

**Strategy**:
- Check how many edges are correctly positioned
- Apply swapping algorithm as needed

**Algorithms**:
- Adjacent swap: `R U R' U R U2 R' U`
- Opposite swap: Apply adjacent swap twice

### Phase 6: Yellow Corners (PLL Corners)
**Goal**: Position yellow corners in correct locations (may be misoriented)

**Strategy**:
- Identify corners needing swap
- Apply corner permutation algorithm

**Algorithm**:
- Corner swap: `U R U' L' U R' U' L`

### Phase 7: Orient Yellow Corners
**Goal**: Correctly orient all yellow corners to complete the cube

**Strategy**:
- Keep cube orientation fixed
- Apply orientation algorithm to each corner
- Cube appears scrambled until all corners done

**Algorithm**:
- Orient corner: `R' D' R D` (repeat 2-4 times per corner)

## Controls and Interface

### Mouse Controls
| Action | Effect |
|--------|--------|
| Left-click + Drag | Rotate entire cube view |
| Right-click on facelet | Rotate the face containing that facelet |
| Scroll wheel | Zoom in/out |
| Middle-click | Reset view angle |

### Rotation Notation
Standard Rubik's Cube notation is used throughout:

| Notation | Meaning |
|----------|---------|
| F | Front face clockwise 90° |
| F' | Front face counter-clockwise 90° |
| F2 | Front face 180° |
| B | Back face clockwise 90° |
| U | Up (top) face clockwise 90° |
| D | Down (bottom) face clockwise 90° |
| R | Right face clockwise 90° |
| L | Left face clockwise 90° |

### View Positions
Eight corner views are available, named by the three visible faces:
- **fru**: Front-Right-Up (default)
- **rbu**: Right-Back-Up
- **blu**: Back-Left-Up
- **lfu**: Left-Front-Up
- **fld**: Front-Left-Down
- **rfd**: Right-Front-Down
- **brd**: Back-Right-Down
- **lbd**: Left-Back-Down

## Technical Implementation

### Facelet Enumeration
The cube uses a flattened representation of 48 movable facelets:
```
Face layout:
       U0 U1 U2
       U3 U4 U5
       U6 U7 U8
L0 L1 L2 | F0 F1 F2 | R0 R1 R2 | B0 B1 B2
L3 L4 L5 | F3 F4 F5 | R3 R4 R5 | B3 B4 B5
L6 L7 L8 | F6 F7 F8 | R6 R7 R8 | B6 B7 B8
       D0 D1 D2
       D3 D4 D5
       D6 D7 D8
```
Center pieces (U4, D4, F4, B4, L4, R4) are fixed and not tracked.

### Rotation Implementation
Each rotation is implemented as a permutation of the facelet array:

```java
// Example: F rotation affects these positions
// Rotate face: F0→F2→F8→F6→F0, F1→F5→F7→F3→F1
// Rotate adjacent edges: U6→R0, U7→R3, U8→R6, etc.
```

Pre-computed arrays in `RotationMoves` class store these permutations for efficiency.

### Animation System
Rotations are animated using JavaFX Timeline:
```java
Timeline timeline = new Timeline(
    new KeyFrame(Duration.millis(16), e -> updateRotation())
);
```
- 60 FPS animation (16ms per frame)
- Smooth interpolation between positions
- Queue system for multiple rotations

### 3D Scene Graph
The JavaFX scene graph structure:
```
SubScene
└── Group
    ├── Cube3 (main container)
    │   ├── 27 Cube objects (including center)
    │   │   └── 1-6 Facelet objects per cube
    │   └── Axes (debug visualization)
    ├── AmbientLight
    └── PointLight
```

### Performance Optimizations

1. **Pre-computed Transformations**: All rotation permutations calculated at compile time
2. **Byte Array Storage**: Minimal memory footprint (48 bytes for cube state)
3. **Lazy Evaluation**: View transformations only computed when needed
4. **Texture Caching**: Facelet textures loaded once and reused
5. **Efficient History**: Tree structure allows O(1) move switching

## Development

### Building from Source
```bash
# Full build with tests
mvn clean install

# Skip tests for faster build
mvn clean install -DskipTests

# Generate documentation
mvn javadoc:javadoc
```

### Running Tests
```bash
# All tests
mvn test

# Specific test class
mvn test -Dtest=DummySolverTest

# Test with coverage
mvn test jacoco:report
```

### Key Test Classes
- `DummySolverTest`: Validates solving algorithm correctness
- `Test`: Console output verification
- `FaceletEnumGenerator`: Code generation for facelet mappings
- `RotationMovesGenerator`: Generates rotation permutation arrays

### Development Tools
The project includes several code generators for maintaining consistency:
- `FaceletConstantsGenerator`: Generates facelet position constants
- `RotationEnumGenerator`: Creates rotation enumeration
- `Facelet3DEnumGenerator`: Generates 3D position mappings

### Dependencies
- **Lombok 1.18.32**: Reduces boilerplate code (@Getter, @Setter, etc.)
- **Jansi 2.4.1**: ANSI color support for console output
- **JavaFX 21.0.2**: 3D graphics and UI framework
- **Maven Shade Plugin 3.5.2**: Creates executable JAR with dependencies

### Future Enhancements
Potential areas for expansion:
- [ ] Additional solving methods (CFOP, Roux, ZZ)
- [ ] Pattern recognition and automatic solving
- [ ] Time tracking and statistics
- [ ] Custom color schemes
- [ ] Export/import cube states
- [ ] Network multiplayer mode
- [ ] VR/AR support
- [ ] Mobile version
- [ ] Advanced algorithms database
- [ ] Tutorial mode with step-by-step guidance

## License

This project is open source. See LICENSE file for details.

## Acknowledgments

- Rubik's Cube solving algorithms based on standard Layer-by-Layer method
- JavaFX 3D API for rendering capabilities
- Community-contributed scramble patterns

---

For more information or to report issues, visit the [project repository](https://github.com/glandais/rubiks-cube).