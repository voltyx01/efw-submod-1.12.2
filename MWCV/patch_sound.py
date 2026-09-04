import sys
import re

# 1. Update ModernConfigManager
path = 'src/main/java/com/paneedah/weaponlib/config/ModernConfigManager.java'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

if 'drawSoundOffsetMs' not in content:
    content = content.replace(
        'public static int drawAnimationCutoffMs = 0;',
        'public static int drawAnimationCutoffMs = 0;\n\n\t@RangeInt(min=0, max=5000)\n\t@ConfigSync(category = CATEGORY_GUNPLAY, comment = "Milliseconds to play the draw sound earlier (during the previous unequip)")\n\tpublic static int drawSoundOffsetMs = 0;'
    )
with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

# 2. Update WeaponRenderer
path = 'src/main/java/com/paneedah/weaponlib/WeaponRenderer.java'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

if 'hasPlayedEarlyDrawSound' not in content:
    content = content.replace('static boolean pendingDrawAfterLowering = false;', 'static boolean pendingDrawAfterLowering = false;\n\tpublic static boolean hasPlayedEarlyDrawSound = false;')

    content = content.replace(
        'public static void triggerLowering(ItemStack outgoingStack, int fromSlot, boolean skipAnimation) {',
        'public static void triggerLowering(ItemStack outgoingStack, int fromSlot, boolean skipAnimation) {\n\t\thasPlayedEarlyDrawSound = false;'
    )

    update_code = '''
		if (lowerStartTime >= 0L) {
			long elapsed = System.currentTimeMillis() - lowerStartTime;
			long remaining = currentLowerDuration - elapsed;

			if (pendingDrawAfterLowering && !hasPlayedEarlyDrawSound && remaining <= com.paneedah.weaponlib.config.ModernConfigManager.drawSoundOffsetMs) {
				net.minecraft.item.ItemStack incomingStack = MC.player.getHeldItemMainhand();
				if (incomingStack != null && !incomingStack.isEmpty() && incomingStack.getItem() instanceof Weapon) {
					Weapon incomingWeapon = (Weapon) incomingStack.getItem();
					MC.player.playSound(incomingWeapon.getDrawSound(), 1, 1);
					hasPlayedEarlyDrawSound = true;
				}
			}

			lowerProgress = Math.min(1f, elapsed / (float) currentLowerDuration);
'''
    content = content.replace(
        'if (lowerStartTime >= 0L) {\n\n\t\t\tlong elapsed = System.currentTimeMillis() - lowerStartTime;\n\n\t\t\tlowerProgress = Math.min(1f, elapsed / (float) currentLowerDuration);',
        update_code
    )
    content = content.replace(
        'if (lowerStartTime >= 0L) {\n\t\t\tlong elapsed = System.currentTimeMillis() - lowerStartTime;\n\t\t\tlowerProgress = Math.min(1f, elapsed / (float) currentLowerDuration);',
        update_code
    )

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

# 3. Update WeaponReloadAspect
path = 'src/main/java/com/paneedah/weaponlib/WeaponReloadAspect.java'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

if 'hasPlayedEarlyDrawSound' not in content:
    content = content.replace(
        'weaponInstance.getPlayer().playSound(weaponInstance.getWeapon().getDrawSound(), 1, 1);',
        'if (com.paneedah.weaponlib.WeaponRenderer.hasPlayedEarlyDrawSound) {\n            com.paneedah.weaponlib.WeaponRenderer.hasPlayedEarlyDrawSound = false;\n        } else {\n            weaponInstance.getPlayer().playSound(weaponInstance.getWeapon().getDrawSound(), 1, 1);\n        }'
    )

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

