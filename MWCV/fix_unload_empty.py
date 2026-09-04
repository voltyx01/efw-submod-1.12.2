import sys

path = 'src/main/java/com/paneedah/weaponlib/WeaponRenderer.java'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace(
    ' && state != WeaponState.UNLOAD_EMPTY',
    ''
)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

