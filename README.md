# Mediumcore Fix

**A bugfix mod for Alex's Mediumcore**

[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-green.svg)](https://www.minecraft.net/)
[![Forge](https://img.shields.io/badge/Forge-47.4.0+-orange.svg)](https://files.minecraftforge.net/)

---

## 🐛 What Does This Mod Fix?

When updating **Alex's Mediumcore** from an old version to a new one, players would **lose all their health progress** and instantly recover to full health.

**Example:**
- You died 5 times and had only 10 HP left
- After updating Mediumcore → you suddenly have 30 HP again! ❌

**This mod fixes that issue!** Your health progress is now preserved during updates. ✅

---

## 📦 Installation

### Requirements

- **Minecraft:** 1.20.1
- **Forge:** 47.4.0 or higher
- **Alex's Mediumcore:** Any version

### How to Install

1. Download `mediumcore_fix-1.0.0.jar` from [Releases](https://github.com/Ringo-Studio/MediumcoreFix/releases)
2. Put it in your `mods/` folder alongside Mediumcore
3. Launch the game!

---

## 🧪 Does It Work?

After updating Mediumcore with this fix installed:
- ✅ Your lost health stays lost
- ✅ Death penalties are preserved
- ✅ No config changes needed
- ✅ Works automatically

Check your server logs after joining - you'll see a migration message if your data was fixed:
```
[Mediumcore Fix] Successfully migrated player 'Steve' health data
```

---

## ❓ FAQ

**Q: Do I need this if I'm installing Mediumcore for the first time?**
A: No, but it won't hurt to have it installed.

**Q: Will it work with existing worlds?**
A: Yes! It specifically fixes data migration for existing players.

**Q: Does it affect gameplay?**
A: No, it only fixes a bug during mod updates. Gameplay remains the same.

**Q: Is it compatible with other mods?**
A: Yes, it uses Mixin injection for maximum compatibility.

---

## 📜 License

GNU General Public License

---

## 👨‍💻 Author

**Ringoway** - [Ringo-Studio](https://github.com/Ringo-Studio)

Found a bug? [Open an issue](https://github.com/Ringo-Studio/MediumcoreFix/issues)!

---

## ⭐ Support

If this mod helped you, give it a star! ⭐

---

## 🔧 For Developers

Looking for technical details? Check the [Wiki](https://github.com/Ringo-Studio/MediumcoreFix/wiki) for:
- Mixin implementation details
- Migration algorithm
- Building from source
- Contributing guidelines
