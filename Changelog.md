# Changelog

## 2.1.1+1.20.1

### Fixes
- Fixed crash when using Pufferfish's Skills versions under 0.17.0


## 2.1.0+1.20.1

This version adds **Title Centering**, **Pufferfish's Skills compat**, and fixes to different crashes.

### New Features
- Added a title centering config option. It automatically centers the item or tooltip title horizontally within the available tooltip space.
- Added full compatibility with Pufferfish's Skills skill trees. Toggleable via the `puffish_compat` config option.

### Fixes
- Refactored quite a bit of the architecture to fix crashes when clearing lists (fixes `UnsupportedOperationException` crash) as well as crashing in Forge due to mixins
- Fixed minor alignment discrepancies when comparing LT and Tierify tooltips. (Especially title alignment)

### Updates
- Updated S-Lib dependency to 1.0.5

## 2.0.2+1.20.1

### Fixes
- Fixed a crash (`NullPointerException`) that could occur when rendering Legendary Tooltips compatibility without a valid item. (e.g. Traveler's Backpack)
- Fixed a visual bug where small tooltips would sometimes have an artificial empty space on the right side.

## 2.0.1+1.20.1

### Fixes
- Refactored internal calculations to optimize memory allocation
- Fixed the wrap logic to only target problematic mods (EMI, Iceberg) instead of overriding vanilla behavior globally. This restores compatibility with other mods that rely on native text wrapping (such as Tips, Boss Checklist, EMI Effect Addon, etc.).
- Preventatively fixed integer overflows in wrapping behavior by setting safer maximum limits.

## 2.0.0+1.20.1

### Changes
- **Internal State Overhaul:** Massive restructuring of the internal tooltip state management to resolve multiple critical layout bugs caused by third-party mod interactions (EMI, Legendary Tooltips, Tierify, Tooltip Overhaul).

### Fixes
- Fixed an issue where displaying a scrollable tooltip from Tooltip Overhaul would cancel scrolling for STS tooltips
- Fixed an issue where text would stop being wrapped for other mods (logic is only enabled for Legendary Tooltips compatibility)
- Fixed a bug where tooltips with less than 3 lines had an artificial empty space inserted at the bottom.

## 1.1.1+1.20.1

### Changes
- Changed default smoothness to 0.25 from 0.5 (250ms)

### Fix
- Fixed crashes due to mixins in both platforms

## 1.1.0

### New Features
- **Smooth Tooltip Scrolling:** Added FPS-independent, time-based smooth scrolling animation. Includes a new config option to adjust the scroll smoothness factor (from `0.0` for instant scrolling up to `1.0` for maximum smoothness).
- **EMI Compatibility:** Added full compatibility with EMI item tooltips. Features scrolling, resizing, and title overflow strategies within EMI's custom tooltip rendering.
- **Language Support:** Added Spanish and English localization for the new config options.

### Improvements & Fixes
- **Compatibility Optimization:** General optimization for mod compatibility functionality. 
- **Tooltip Resizing Fix:** Fixed a bug where tooltips sometimes failed to resize properly.

## 1.0.0

**This is the first standalone release of Smart Tooltip Scroll, decoupling all scrolling, resizing, and tooltip dimension logic from Custom Tooltip API into a dedicated mod!**

### Existing Features (From CTA 3.3.0)
- **Scrollable Tooltips:** Tooltips that are too long to fit on your screen can now be scrolled easily using your mouse wheel.
- **Resizable Tooltips:** Limit the maximum width and height of tooltips to a percentage of your screen, ensuring they never take up too much space.
- **Smart Scrolling Toggles:** Toggle scroll behavior in other containers (like the Creative Inventory) to prevent conflicts with other scrolling menus.

### New Features
- **Title Overflow Handling:** Added three smart ways to handle item names that are too long for the tooltip:
  - *Wrap:* Wraps the item name onto multiple lines cleanly.
  - *Truncate:* Cuts off the name at the tooltip's edge.
  - *Horizontal Scroll:* Automatically scrolls the item name back and forth dynamically.
- **Text Style Preservation:** Wrapping text now perfectly preserves custom colors, gradients, bold, and italic formatting without breaking the visual style.
- **Legendary Tooltips Compatibility:** Solved overlap bugs when rendering Legendary Tooltips components, ensuring clean spacing and alignment.
- **Tierify Compatibility:** The first scroll mod to fully support Tierify, allowing tooltips to scroll and wrap text correctly while viewing a tiered item. Also makes Legendary Tooltips compatible with it.
- **Language Support:** Added German translations and updated Spanish translations.

### Fixes
- Fixed a bug where text would draw over the bottom border of a tooltip when scrolling was active.
- Fixed spacing issues to ensure the gap between elements remains perfectly uniform.
- Fixed native wrapping issues on Forge and NeoForge.