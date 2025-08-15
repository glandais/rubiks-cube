# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Rubik's Cube simulator built with JavaFX. It provides a 3D visualization of a Rubik's cube with interactive controls for rotations, solving algorithms, and history management.

## Development Commands

### Build and Package
```bash
# Compile the project
mvn compile

# Package into executable JAR with dependencies
mvn package

# Clean build artifacts
mvn clean

# Clean and package
mvn clean package
```

### Running the Application
```bash
# Run via Maven
mvn compile exec:java -Dexec.mainClass="io.github.glandais.rubikscube.Main"

# Run packaged JAR
java -jar target/rubiks-cube-1.0-SNAPSHOT.jar
```

### Testing
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=DummySolverTest

# Run test to verify console output
mvn test -Dtest=Test
```

## Architecture

### Core Components

1. **Main Entry Point**: `Main.java` launches `RubiksCubeApplication` which creates the JavaFX UI

2. **Model Layer** (`model/`):
   - `Cube3Model`: Core cube state representation using byte array for 48 facelets
   - `FaceletEnum`: Represents the 48 individual facelets (U0-U8, D0-D8, F0-F8, B0-B8, L0-L8, R0-R8)
   - `rotation/`: Contains rotation logic and move sequences
   - `view/`: Manages different viewing orientations (8 corner views)

3. **JavaFX Layer** (`jfx/`):
   - `RubiksCubeApplication`: Main UI setup with toolbars and controls
   - `RubiksCubeInteract`: Handles user interactions, move history, and solving phases
   - `scene/`: 3D scene components including `Cube3`, `Facelet`, `RubiksCubeView`
   - `model/`: UI-specific models for tree view (move history) and interactions

4. **Solver** (`solver/`):
   - Implements 7-phase layer-by-layer solving algorithm
   - Phase 1: White edges
   - Phase 2: First layer (white corners)
   - Phase 3: Second layer
   - Phase 4: Yellow cross
   - Phase 5: Yellow edges positioning
   - Phase 6: Yellow corners positioning
   - Phase 7: Orient yellow corners

### Key Design Patterns

- **Move History**: Tree structure allowing navigation through move sequences with undo/redo
- **Rotation System**: Uses pre-computed position arrays in `RotationMoves` for efficient cube state updates
- **View Transformations**: Supports 8 different viewing angles corresponding to cube corners
- **Interactive Controls**: Mouse drag for 3D rotation, click on facelets, keyboard shortcuts

## Project Structure

- Java 21 with Maven build system
- JavaFX 21 for 3D graphics and UI
- Lombok for reducing boilerplate
- Jansi for colored console output
- Resources include facelet textures (48 PNG files) and scramble sequences

## Key Implementation Notes

- Cube state is stored as a byte array mapping 48 positions to facelet values
- Rotations are applied by remapping positions using precomputed arrays
- The UI provides both programmatic solving and manual interaction modes
- Move history is maintained as a tree structure allowing branching paths