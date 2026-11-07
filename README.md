# BANGLACLIENT

Client-side Fabric optimization and HUD mod for **Minecraft Java Edition 1.21.11** (Java 21).

- 315 independent modules across 19 categories (Optimizer, Performance, HUD, PvP, Camera, Visual, Inventory, Chat, Network, World Map, Client Settings, ...)
- Click GUI, free-positioning HUD editor (no snapping), profiles, themes, keybind conflict detection
- Custom loading screen and main menu, external mod detection (ModMenu etc.)
- Crash-safe config with automatic `.bak` recovery
- Procedural icon renderer: no per-module PNG files required

## Build

Requires JDK 21.

```bash
./gradlew build        # JAR: build/libs/banglaclient-1.0.0.jar
./gradlew runClient    # launch dev client
./gradlew test         # JUnit 5 suites
```

Versions are set in `gradle.properties` (check https://fabricmc.net/develop for updates).

## Custom branding

Replace these files, no code changes needed:

- `src/main/resources/assets/banglaclient/textures/gui/logo.png`
- `src/main/resources/assets/banglaclient/textures/gui/main_menu_background_demo.png`
- `src/main/resources/assets/banglaclient/textures/gui/loading_background.png`

## Layout

```
src/main/java/com/banglaclient/
  core/ event/ setting/ config/ profile/ keybind/ theme/ animation/
  hud/ gui/ integration/ mixin/ util/ module/ (impl/ = 315 modules)
```
