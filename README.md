# DraftRoom

A Java Swing desktop application for designing room floor plans. You organize work in a tree of projects, buildings and rooms, draw each room to scale, and furnish it with interactive furniture elements. Elements can be selected, moved, resized, rotated, copied, deleted and zoomed, and every editing action supports undo/redo.

DraftRoom was built as a team project for the **Software Design** (*Dizajn softvera*) course at the School of Computing (RAF), Belgrade, in the 2024/25 academic year. The course focuses on object-oriented design, so the codebase deliberately applies a set of classic GoF design patterns.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Key Features](#key-features)
- [Technologies Used](#technologies-used)
- [Architecture / Project Structure](#architecture--project-structure)
- [How It Works](#how-it-works)
- [Installation and Setup](#installation-and-setup)
- [How to Run](#how-to-run)
- [Known Limitations](#known-limitations)
- [Team and Contributors](#team-and-contributors)

---

## Project Overview

DraftRoom models interior layout planning as a hierarchy:

```
Project Explorer
└── Project            (name, author, save path)
    ├── Building       (random tab colour)
    │   └── Room       (width × length)
    │       └── Room elements (furniture)
    └── Room           (a room can also sit directly under a project)
```

The left side of the main window shows this hierarchy as a `JTree`. When you double-click a project, its rooms open as tabs on the right. Each tab is a drawing canvas where the room is drawn to scale and furniture is placed with the mouse. A vertical side toolbar switches between editing tools such as select, move, resize and rotate.

Supported furniture types (the class names are in Serbian):

| Class | Element |
|---|---|
| `Bojler` | Water heater |
| `Kada` | Bathtub |
| `Krevet` | Bed |
| `Lavabo` | Sink |
| `Ormar` | Wardrobe |
| `Sto` | Desk / table |
| `VesMasina` | Washing machine |
| `Vrata` | Door |
| `WCSolja` | Toilet |

> The user-facing messages and most identifiers in the source code are in Serbian.

---

## Key Features

**Project tree management**
- Add and remove nodes (Project → Building/Room → Room) from the toolbar, the menu or keyboard shortcuts.
- Edit a project's name, author and path in a dialog.
- Rename any node in place with a triple click. Duplicate names among siblings are rejected.
- Each node type has its own icon in the tree.

**Room drawing and furnishing**
- Set a room's width and length. The room is scaled to fit the panel and centred.
- Add any of the 9 furniture types from an icon picker, then click inside the room and enter the element's dimensions.
- Overlapping furniture is rejected when you place it.
- Each furniture type has its own vector drawing (Java2D `GeneralPath`), not just a rectangle.

**Editing tools (side toolbar)**
- **Select**: click to select one element, or drag a dashed rectangle to select several.
- **Move**: drag selected elements, with snapping to room walls, wall-boundary checks and collision checks. Dragging with nothing selected pans the room.
- **Resize**: drag the bottom-right corner of a selected element.
- **Rotate left / right**: rotate selected elements in 90° steps.
- **Copy / Paste**: duplicate selected elements, placed at a small offset.
- **Edit element**: change an element's dimensions and RGB colour.
- **Delete**: remove the selected elements or the clicked one.
- **Zoom**: zoom with the mouse wheel around the panel centre (0.5× – 3×). Mouse coordinates are mapped back through the inverse transform, so the other tools keep working while zoomed.

**Undo / Redo**
- Adding, moving, resizing, rotating, copying and deleting elements can be undone and redone with the Undo/Redo toolbar buttons.
- Each room tab keeps its own command history.

**Automatic room organisation**
- *Organize room* opens a dialog where you list furniture types and their dimensions. DraftRoom then places the elements automatically by walking a grid of cells in a spiral and aligning edge elements to the walls.

**Persistence**
- **Save Project / Save As / Open Project**: the whole project tree is serialised to and from JSON with Jackson.
- A project is saved only if it has changed since the last save (a "dirty" flag).
- **Save Room / Open Room**: a room is saved as a reusable JSON template in `src/main/resources/templates/` and can be loaded into another room. The target room must be selected in the tree first (see [Loading a room template](#loading-a-room-template-open-room)). Two sample templates are included: `AnjinaSoba.json` and `KacinaSoba.json`.

**Notifications and logging**
- Every error, warning and info message is shown in a dialog, printed to the console and appended to `log.txt`.

### Keyboard shortcuts

| Shortcut | Action |
|---|---|
| `Ctrl+N` | Add node |
| `Ctrl+Backspace` | Remove node |
| `Ctrl+E` | Edit project |
| `Ctrl+S` | Save project |
| `Shift+S` | Save as |
| `Alt+F5` | About us |
| `Alt+F4` | Exit |

These shortcuts belong to actions in the menu bar. Undo, Redo and Organize room are only in the toolbar, so they have no working keyboard shortcut.

---

## Technologies Used

| Technology | Version | Usage in the project |
|---|---|---|
| **Java** | 22 (per `pom.xml`) | Application language. Uses modern APIs such as `List.getFirst()` (Java 21+). |
| **Java Swing** | JDK | The whole GUI: `JFrame`, `JTree`, `JTabbedPane`, `JToolBar`, dialogs, `JFileChooser`, `AbstractAction`. |
| **Java AWT / Java2D** | JDK | Rendering of rooms and furniture: `Graphics2D`, `GeneralPath`, `AffineTransform`, `BasicStroke` (including dashed strokes). |
| **Jackson Databind** | 2.17.2 | JSON serialisation of projects and room templates. Uses polymorphic types (`@JsonTypeInfo`/`@JsonSubTypes`), `@JsonIgnore`, and custom `Color` (de)serialisers. |
| **Project Lombok** | 1.18.34 | Generates getters, setters, constructors and `@Data` boilerplate (compile time only, `provided` scope). |
| **Maven** | – | Build and dependency management (`pom.xml`). |
| **IntelliJ IDEA** | – | Development IDE (`.idea/`, configured with the Corretto 22 JDK). |

The project has no external runtime dependencies other than Jackson. There are no automated tests.

---

## Architecture / Project Structure

The code is split along MVC lines (`model`, `gui` = view, `controller`), with separate packages for each design-pattern subsystem.

```
src/main/java/raf/draft/dsw/
├── AppCore.java                  # Entry point (main)
├── core/
│   └── ApplicationFramework      # Singleton – wires up the application
├── model/
│   ├── nodes/                    # DraftNode, DraftNodeComposite (Composite)
│   ├── structures/               # ProjectExplorer, Project, Building, Room
│   │   └── namestaj/             # RoomElement + 9 furniture classes, Prototype
│   ├── notification/             # Message, MessageType, Notification
│   └── repository/               # DraftRoomRepository – holds the model root
├── gui/swing/
│   ├── MainFrame                 # Singleton main window
│   ├── ProjectView               # Tabs per project, Mediator for tools/states
│   ├── RoomView                  # Drawing canvas for one room
│   ├── MyMenuBar, MyToolBar      # Menus and toolbar
│   ├── *Frame, EditProject, ...  # Dialogs
│   └── jtree/                    # JTree model, view, listeners, node factories
├── controller/
│   ├── actions/                  # Swing Actions + ActionManager
│   │   └── SideToolBarActions/   # Room editing tools
│   ├── RoomMouseListener         # Forwards mouse events to the current state
│   ├── messagegenerator/         # MessageGenerator, Console/File logger, factory
│   └── observer/                 # IPublisher, ISubscriber
├── state/                        # State interface, StateManager, 19 concrete states
├── commands/                     # AbstractCommand, CommandManager, 15 commands
├── painter/                      # ElementPainter + RoomPainter + 9 element painters
└── serializer/                   # Jackson Serializer + Color (de)serialisers

src/main/resources/
├── images/       # Toolbar and tree icons, About-us photos
├── namestaj/     # Furniture icons
└── templates/    # Saved room templates (JSON)
```

### Design patterns

| Pattern | Where it is applied |
|---|---|
| **Singleton** | `ApplicationFramework`, `MainFrame` |
| **Composite** | `DraftNode` (leaf/base) and `DraftNodeComposite` (`ProjectExplorer`, `Project`, `Building`, `Room`), with `RoomElement` as the leaf |
| **Observer** | `IPublisher`/`ISubscriber`: `MessageGenerator` → `ConsoleLogger`, `FileLogger`, `MainFrame`; model nodes → `ProjectView` / `RoomView` |
| **Factory Method** | `DraftNodeFactory` with `ProjectFactory`, `BuildingFactory`, `RoomFactory`, selected through `FactoryUtils` / `ChildFactory` |
| **Simple Factory** | `LoggerFactory` creates console or file loggers |
| **State** | `State` + `StateManager`. Each editing tool (add element, select, move, resize, rotate, zoom, ...) is a separate state that handles mouse events. |
| **Mediator** | `ProjectView` sits between mouse events from `RoomView` and the active `State` |
| **Command** | `AbstractCommand` + `CommandManager` (a list with a pointer) for undo/redo |
| **Prototype** | `Prototype.clone()` on every `RoomElement`, used by copy/paste |

Each furniture type also has a dedicated **painter** class (`ElementPainter` implementations). The painter keeps drawing and hit-testing separate from the model object.

---

## How It Works

### Startup
`AppCore.main()` → `ApplicationFramework.getInstance()` → `initialize()`. This creates the `Serializer`, the `MessageGenerator` and the model repository (which holds the root `ProjectExplorer`). It then shows `MainFrame` and registers the console and file loggers as message subscribers.

### Adding a piece of furniture
1. **Add Room Element** opens `AddElementFrame`. Choosing an element switches the `StateManager` to that element's state, for example `AddKrevet`.
2. Clicking inside the room: `RoomMouseListener` → `ProjectView.misKliknut()` → `currentState.misKliknut()`.
3. The state maps the click through the zoom transform, asks for dimensions (`DimensionFrame`), creates the model object and its painter, and rejects the placement if it overlaps another element (`RoomView.preklapanjeElemenata`).
4. An `Add…Command` is pushed to the room's `CommandManager`. Executing it registers the painter, adds a node to the `JTree` and adds the child to the `Room`. The room then notifies its `RoomView`, which repaints.

### Undo / Redo
`CommandManager` stores commands in a list with a pointer to the first free slot. A new command drops everything to the right of the pointer, `undo` steps back and calls `undoCommand()`, and `redo` steps forward. The Undo/Redo buttons are enabled or disabled based on the pointer position.

### Rendering and zoom
`RoomView.paintComponent()` applies an `AffineTransform` (translate → scale → translate back around the panel centre) and a pan offset. It then asks every painter to draw itself and overlays the selection outlines. `RoomPainter` computes a scaling factor that fits the room's dimensions into the panel. Furniture painters rotate their shape around the element's centre (`rotation × 90°`) and keep the transformed path for hit-testing.

### Automatic organisation
`OrganizeRoomAction` divides the room into an `n × m` grid. The cell size comes from the largest requested element. The action generates a clockwise **spiral order** of cells starting at the top-left corner and assigns elements to cells in that order. Elements in edge or corner cells are pushed against the walls, and elements in interior cells are centred.

### Persistence
`Serializer` uses a Jackson `ObjectMapper`. The node hierarchy is serialised polymorphically with an `@type` property. Parent references and subscriber lists are excluded to avoid cycles. After loading, they are rebuilt recursively (`setParentReferences`), and painters and views are recreated for every room. `java.awt.Color` is handled by custom `ColorSerializer` / `ColorDeserializer` classes.

---

## Installation and Setup

### Prerequisites
- **JDK 22 or newer**. `pom.xml` sets `maven.compiler.source/target` to 22. The project was developed with Amazon Corretto 22.
- **Maven 3.x**, or an IDE with Maven support. IntelliJ IDEA includes a bundled Maven.

### Clone
```bash
git clone https://github.com/katarinaggg/raf-DraftRoom.git
cd raf-DraftRoom
```

Maven downloads the dependencies (Jackson, Lombok) automatically on the first build.

> If your IDE does not process Lombok annotations, enable annotation processing (IntelliJ: *Settings → Build, Execution, Deployment → Compiler → Annotation Processors*).

---

## How to Run

> **Run the application from the project root directory.** `log.txt` and the room-template folder (`src/main/resources/templates`) are resolved relative to the working directory.

### Option 1 – IntelliJ IDEA (recommended)
1. Open the project folder. IntelliJ detects `pom.xml` and imports it as a Maven project.
2. Set the Project SDK to JDK 22+.
3. Run `raf.draft.dsw.AppCore` (the `main` method).

### Option 2 – Maven from the command line
```bash
mvn compile
mvn exec:java -Dexec.mainClass=raf.draft.dsw.AppCore
```
`exec-maven-plugin` is not declared in `pom.xml`. Maven resolves it by its `exec:` prefix and downloads it on first use.

### Quick usage guide
1. Select **Project Explorer** in the tree and click **Add Node** to create a project.
2. Select the project and click **Add Node** again. Choose **Building** or **Room**, and add rooms to buildings the same way.
3. **Double-click** the project to open its rooms as tabs.
4. Click inside a room tab and enter the room dimensions. *Edit Room* is the default tool.
5. Use the side toolbar to add furniture and edit it. Use **Undo/Redo** in the top toolbar.
6. Select a project in the tree and use **Save Project** to write it to JSON. **Open Project** loads it back.

### Loading a room template (Open Room)
**Open Room** does not create a new room. It loads a saved template *into an existing room* that is selected in the tree:

1. Create a project, then add a room to it (see steps 1–2 above).
2. **Click the room once in the tree** to select it.
3. Click **Open Room** and choose a template, for example `AnjinaSoba.json` or `KacinaSoba.json` from `src/main/resources/templates/`.
4. The room takes the template's dimensions and furniture.

If no room is selected, you get the warning *"Morate da izaberete sobu u koju zelite da ucitate"* and nothing is loaded.

> **Tip:** the room view does not always refresh after a template is loaded. If the room or its furniture does not appear, **double-click the project** in the tree to reopen its tabs.

**Save Room** works the same way: select a room in the tree first, and it is saved to `src/main/resources/templates/<room name>.json`.

---

## Known Limitations

DraftRoom is a course project, and a few features are unfinished:

- **Organize room** places elements correctly in the model, but the result is not always drawn reliably. The *Randomizuj* (shuffle) button, next to *Dodaj* and *Ukloni* in the *Organize room* dialog, throws an exception as soon as at least one element is in the list.
- When you open a saved project, door (`Vrata`) elements are not given a painter, so they are not drawn. Opening a room template draws them correctly.
- *Edit element* (dimensions and colour) is not part of the undo/redo history.
- Element positions are stored in panel pixel coordinates, while sizes are scaled from the room's scale factor. A layout can shift if it is reopened at a different window size.
- Room tabs and room views are keyed by room name, and model equality for most nodes is name-based (Lombok `@Data`). Elements with identical names can therefore interfere with each other.
- There are no automated tests.

---

## Team and Contributors

DraftRoom was developed as a two-person team project by:

- **Anja Stanković**
- **Katarina Gordić**

The team built it for the *Software Design* course at the School of Computing (Računarski fakultet, RAF), Belgrade, between October 2024 and January 2025.
