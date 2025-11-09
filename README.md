# Mediumcore Fix

**A critical bugfix for Alex's Mediumcore mod - prevents health data loss during updates**

[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-green.svg)](https://www.minecraft.net/)
[![Forge](https://img.shields.io/badge/Forge-47.4.0+-orange.svg)](https://files.minecraftforge.net/)
[![License](https://img.shields.io/badge/License-GPL-blue.svg)](LICENSE)

---

## 🐛 What Bug Does This Fix?

When **Alex's Mediumcore** was updated to a newer version, the mod changed how it stores player health data internally. If you update from an older version to a newer one, the game **erases your death penalties** and you instantly recover all lost health!

### The Problem:

```
Before Update:
✓ You died 5 times
✓ Lost 10 HP (5 deaths × 2 HP each)
✓ Current health: 20 HP

After Update WITHOUT This Fix:
✗ Game doesn't recognize old health data
✗ Resets your health to starting value (30 HP)
✗ You instantly recover 10 HP you should have lost!
```

### What Gets Lost:

- **Death penalties** - All HP you lost from dying
- **Healing progress** - Any HP you regained from golden apples
- **Mod integrations** - Health modifiers from other mods (rings, baubles, etc.)

---

## ✅ How This Fix Works

This mod acts as a "translator" between the old and new data systems. When you first log in after updating Mediumcore:

1. Detects you're using old health data
2. Converts it to the new format correctly
3. Preserves your death penalties and healing
4. Works automatically - no config needed!

**Your health progress is fully preserved!** ✓

---

## 📦 Installation

### Requirements

- **Minecraft:** 1.20.1
- **Forge:** 47.4.0 or higher
- **Alex's Mediumcore:** Any version (old or new)

### How to Install

1. Download the latest `mediumcore_fix-1.0.0.jar` from [Releases](https://github.com/Ringo-Studio/MediumcoreFix/releases)
2. Place it in your `mods/` folder alongside Alex's Mediumcore
3. Start your server/game
4. Done! The fix runs automatically on first login

---

## 🎯 When Do You Need This?

### ✅ You NEED this fix if:

- You're updating Mediumcore from an **older version** to a **newer version**
- You have players with existing death penalties who would lose progress
- You run a server and want to preserve player data during updates
- You use other mods that modify max health (Curios, Baubles, etc.)

### ❌ You DON'T need this if:

- You're installing Mediumcore for the first time (no old data to migrate)
- All players are okay with resetting to full health (though why? 😄)

---

## 🧪 How To Verify It Works

After installing the fix and updating Mediumcore, check your server logs when players join:

### Success Message:
```
[Mediumcore Fix] Successfully migrated player 'Steve' health data: oldModifier=-10.0, newBaseHealth=20.0
```

This confirms:
- Player had lost 10 HP from deaths
- Data was converted correctly
- New base health is 20 HP (30 starting - 10 lost)

### What Players See:
- ✅ Their health stays the same as before the update
- ✅ Death penalties are preserved
- ✅ No sudden health recovery
- ✅ Game feels exactly the same

---

## ❓ Frequently Asked Questions

### Q: Will this work on existing worlds?
**A:** Yes! That's exactly what it's designed for. It fixes the migration for existing players.

### Q: Do I need to configure anything?
**A:** Nope! Install it alongside Mediumcore and it works automatically.

### Q: What if I install this AFTER players already updated?
**A:** Unfortunately, if players already logged in with the new Mediumcore (without the fix), their health was already reset. This fix prevents the problem during the first login after update.

### Q: Does it affect gameplay or performance?
**A:** No. It only runs once per player (during migration) and then does nothing. Zero performance impact.

### Q: Is it compatible with other mods?
**A:** Yes! It uses Mixin injection which is designed for maximum compatibility. Works with Curios, Baubles, and other health-modifying mods.

### Q: Can I remove it after migration is complete?
**A:** Technically yes, but it's harmless to keep installed. It only activates when detecting old data.

---

## 🔧 Technical Details

For developers and server admins who want to understand the internals:

### The Core Issue

**Old Mediumcore** used a single NBT tag (`HEALTH_MODIFIER_TAG`) to store cumulative death/healing changes.

**New Mediumcore** uses two NBT tags:
- `BASE_HEALTH_TAG` - Permanent base health (modified by deaths/healing)
- `HEALTH_MODIFIER_TAG` - External modifiers from other mods

The new version's initialization code assumes fresh players and resets `HEALTH_MODIFIER_TAG` to 0, destroying old death penalty data.

### The Fix Implementation

This mod uses **Mixin @Inject** to add migration code **before** the initialization check:

```java
if (oldVersionData && missingNewVersionTag) {
    // Calculate: new base = starting health + old cumulative losses
    BASE_HEALTH_TAG = startingHealth + oldModifier;
    // Now initialization check sees the tag exists and skips reset
}
```

**Why @Inject and not @Overwrite?**
- ✅ Maximum compatibility with other mods
- ✅ No conflicts with other mixins
- ✅ Future-proof (works even if Mediumcore updates)

### Source Code

All source code is available in this repository. Key file:
- [MixinCommonEvents.java](src/main/java/com/ringoway/mediumcorefix/mixin/MixinCommonEvents.java) - Migration logic with detailed comments

---

## 📜 License

GNU General Public License v3.0

This is free and open-source software. Feel free to review, modify, and redistribute!

---

## 👨‍💻 Author

**Ringoway** - [Ringo-Studio](https://github.com/Ringo-Studio)

### Support

- 🐛 [Report bugs](https://github.com/Ringo-Studio/MediumcoreFix/issues)
- ⭐ [Star this repo](https://github.com/Ringo-Studio/MediumcoreFix) if it helped you!
- 💬 Questions? Open a discussion!

---

## 🙏 Credits

- **Alex** - Original Mediumcore mod
- **Forge Team** - Modding framework
- **SpongePowered** - Mixin library

---

Made with ❤️ for the Minecraft modding community
