# Smart Tooltip Scroll

**Smart Tooltip Scroll (STS)** is a client-side Minecraft mod that gives you full control over the size and behavior of your item tooltips. No more tooltips going off-screen: limit their size, scroll through long descriptions, and handle massive item names perfectly.

---

## ✨ Key Features

### 📜 Scrollable Tooltips
- Tooltips that are too tall to fit on your screen can now be scrolled using your mouse wheel.
- **Smart Container Toggles:** Prevent conflicts by disabling scroll behavior in specific containers, like the Creative Inventory.

### 📐 Resizable Tooltips
- Cap the maximum height and width of your tooltips to a specific percentage of your screen. Keep your UI clean and unobtrusive.

### 🔠 Title Overflow Strategies
When an item's name is too long for the tooltip, choose exactly how to handle it:
- **Wrap:** Wraps the title onto multiple lines perfectly, preserving custom colors, gradients, and italics.
- **Truncate:** Cleanly cuts off the text with an ellipsis (`...`).
- **Horizontal Scroll:** Animates the title to scroll side-to-side dynamically.

### 🤝 Compatibility
- Full support for **Legendary Tooltips** and **Tierify** (including custom borders and components).
- Built-in compatibility with **Mouse Tweaks**.

### 🖥️ In-Game GUI
Full **YACL + ModMenu** integration. Configure your scrolling and dimension limits instantly from the main menu!

---

## 🏗️ Building from Source

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

## 🌍 Platform Support

| Platform | Versions             |
|----------|----------------------|
| Fabric   | 1.20.1, 1.21.1 (WIP) |
| Forge    | 1.20.1               |
| NeoForge | 1.21.1 (WIP)         |

---

## 📦 Dependencies

- [S-Lib](https://github.com/Stalemated/s-lib)
- [YACL](https://www.curseforge.com/minecraft/mc-mods/yacl)
#### Fabric only
- [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
- [ModMenu](https://www.curseforge.com/minecraft/mc-mods/modmenu)

---

## 📄 License

This project is licensed under the **MIT License**.
