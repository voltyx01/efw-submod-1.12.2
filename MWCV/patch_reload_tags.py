import sys

path = 'src/main/java/com/paneedah/weaponlib/WeaponReloadAspect.java'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace(
    'weaponInstance.setMagazineRemoved(true);',
    'weaponInstance.setMagazineRemoved(true);\n            Tags.setMagazineRemoved(weaponItemStack, true);'
)

content = content.replace(
    'weaponInstance.setMagazineRemoved(false);',
    'weaponInstance.setMagazineRemoved(false);\n            Tags.setMagazineRemoved(weaponItemStack, false);'
)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

