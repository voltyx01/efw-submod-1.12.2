import sys

path = 'src/main/java/com/paneedah/weaponlib/WeaponRenderer.java'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace(
    'currentLowerDuration = 175L; // default',
    'currentLowerDuration = Math.max(175L, com.paneedah.weaponlib.config.ModernConfigManager.drawSoundOffsetMs); // default'
)

content = content.replace(
    'currentLowerDuration = Math.max(0, total - com.paneedah.weaponlib.config.ModernConfigManager.unequipAnimationCutoffMs);',
    'currentLowerDuration = Math.max((long)com.paneedah.weaponlib.config.ModernConfigManager.drawSoundOffsetMs, total - com.paneedah.weaponlib.config.ModernConfigManager.unequipAnimationCutoffMs);'
)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

