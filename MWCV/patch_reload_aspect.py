import sys

path = 'src/main/java/com/paneedah/weaponlib/WeaponReloadAspect.java'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace(
    'weaponInstance.setAmmo(0);\n            player.world.playSound',
    'weaponInstance.setAmmo(0);\n            weaponInstance.setMagazineRemoved(true);\n            player.world.playSound'
)

content = content.replace(
    'weaponInstance.setAmmo(weaponInstance.getAmmo() + loadIterationCount);\n            \n            p.setStatus(Status.GRANTED);',
    'weaponInstance.setAmmo(weaponInstance.getAmmo() + loadIterationCount);\n            weaponInstance.setMagazineRemoved(false);\n            p.setStatus(Status.GRANTED);'
)

content = content.replace(
    'Tags.setAmmo(weaponItemStack, ammo);\n            weaponInstance.setAmmo(ammo);\n\n            p.setStatus(Status.GRANTED);',
    'Tags.setAmmo(weaponItemStack, ammo);\n            weaponInstance.setAmmo(ammo);\n            weaponInstance.setMagazineRemoved(false);\n\n            p.setStatus(Status.GRANTED);'
)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

