import sys

path = 'src/main/java/com/paneedah/weaponlib/WeaponReloadAspect.java'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace(
    'status = Status.GRANTED;',
    'weaponInstance.setMagazineRemoved(false);\n                            status = Status.GRANTED;'
)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

