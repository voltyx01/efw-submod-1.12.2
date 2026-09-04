import sys

path = 'src/main/java/com/paneedah/weaponlib/WeaponReloadAspect.java'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace(
    'weaponInstance.setMagazineRemoved(false);\n                            Status status = Status.GRANTED;',
    'Status status = Status.GRANTED;'
)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

