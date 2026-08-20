# Smart Tooltip Scroll

**Smart Tooltip Scroll (STS)** allows for scrollable and resizable tooltips, aiming for full compatibility with other mods.

---

## Features

*   **Scrollable Tooltips:** Easily scroll through tooltips that are too big to fit on your screen.
*   **Resizable Tooltips:** Customize the maximum width and height of tooltips.
*   **Pinned Headers:** The item name and header remain pinned at the very top of the tooltip while you scroll through the description.

---

## Compatibility

Current compatible mods:

* **Legendary Tooltips**
* **Obscure Tooltips**
* **EMI**
* **Tierify**
* **Spell Engine**
* **Pufferfish's Skills**
* **Tooltip Overhaul (WIP)**

If there's any mod that breaks functionality, request it and it will be added

---

## Building from Source

STS depends on **S-Lib**, which must be published to your local Maven repository before compiling.

#### 1. Clone and Publish S-Lib
```bash
git clone https://github.com/Stalemated/s-lib.git
cd s-lib
# Publish to maven local
gradlew.bat publishToMavenLocal # (Windows)
./gradlew publishToMavenLocal   # (Linux/macOS)
cd ..
```

#### 2. Clone STS and Build
```bash
git clone https://github.com/Stalemated/smart_tooltip_scroll.git
cd smart_tooltip_scroll
# Build the mod
gradlew.bat build # (Windows)
./gradlew build   # (Linux/macOS)
```

Output jars will be located in `[loader]/build/libs/` or `build/libs/` depending on the platform.

---

# Platform Support

| Platform | Versions             |
|----------|----------------------|
| Fabric   | 1.20.1, 1.21.1 (WIP) |
| Forge    | 1.20.1               |
| NeoForge | 1.21.1 (WIP)         |

---

## Dependencies

- [S-Lib](https://github.com/Stalemated/s-lib)
- [YACL](https://www.curseforge.com/minecraft/mc-mods/yacl)
#### Fabric only
- [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
- [ModMenu](https://www.curseforge.com/minecraft/mc-mods/modmenu)
