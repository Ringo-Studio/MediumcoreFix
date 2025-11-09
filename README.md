# Mediumcore Fix

**Fixes health data loss when updating Alex's Mediumcore mod**

[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-green.svg)](https://www.minecraft.net/)
[![Forge](https://img.shields.io/badge/Forge-47.4.0+-orange.svg)](https://files.minecraftforge.net/)

---

## The Problem

When updating **Alex's Mediumcore** from an older version to a newer one, players lose all their death penalties and instantly recover to full health.

**Example:**
- Player died 5 times and lost 10 HP
- After updating Mediumcore → suddenly has full health again

This mod fixes that bug. Your death penalties and health progress are preserved during updates.

---

## Installation

**Requirements:**
- Minecraft 1.20.1
- Forge 47.4.0+
- Alex's Mediumcore (any version)

**Steps:**
1. Download `mediumcore_fix-1.0.0.jar` from [Releases](https://github.com/Ringo-Studio/MediumcoreFix/releases)
2. Put it in your `mods/` folder with Mediumcore
3. Start the game - it works automatically

---

## Verification

Check server logs after players join:

```
[Mediumcore Fix] Successfully migrated player 'Steve' health data: oldModifier=-10.0, newBaseHealth=20.0
```

This confirms the migration worked correctly.

---

## FAQ

**Q: Do I need this for new installations?**
A: No, only if you're updating from an older Mediumcore version.

**Q: What if I already updated without this fix?**
A: Unfortunately, data was already lost. Install this before the next update.

**Q: Does it affect performance?**
A: No, it only runs once per player during migration.

**Q: Compatible with other mods?**
A: Yes, works with Curios, Baubles, and other health-modifying mods.

---

## Technical Details

**The Issue:**
- Old Mediumcore used `HEALTH_MODIFIER_TAG` for death penalties
- New Mediumcore uses `BASE_HEALTH_TAG` + `HEALTH_MODIFIER_TAG` (different purpose)
- Update code resets `HEALTH_MODIFIER_TAG` to 0, erasing old death data

**The Fix:**
- Uses Mixin @Inject to detect old data
- Converts it to new format before reset happens
- Source: [MixinCommonEvents.java](src/main/java/com/ringoway/mediumcorefix/mixin/MixinCommonEvents.java)

---

## License

GNU General Public License v3.0

---

## Author

**Ringoway** - [Ringo-Studio](https://github.com/Ringo-Studio)

- [Report bugs](https://github.com/Ringo-Studio/MediumcoreFix/issues)
- [Star this repo](https://github.com/Ringo-Studio/MediumcoreFix) if it helped!
