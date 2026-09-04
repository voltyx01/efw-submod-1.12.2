import sys

path = 'src/main/java/com/paneedah/weaponlib/Weapon.java'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace(
    'instance.setState(WeaponState.READY);',
    'instance.setState(WeaponState.READY);\n\n        instance.setMagazineRemoved(Tags.isMagazineRemoved(itemStack));'
)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

