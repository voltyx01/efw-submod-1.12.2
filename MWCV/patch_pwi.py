import sys

path = 'src/main/java/com/paneedah/weaponlib/PlayerWeaponInstance.java'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace(
    'private boolean loadAfterUnloadEnabled;',
    'private boolean loadAfterUnloadEnabled;\n\n\tprivate boolean magazineRemoved;'
)

content = content.replace(
    'loadAfterUnloadEnabled = byteBuf.readBoolean();',
    'loadAfterUnloadEnabled = byteBuf.readBoolean();\n\n\t\tmagazineRemoved = byteBuf.readBoolean();'
)

content = content.replace(
    'byteBuf.writeBoolean(loadAfterUnloadEnabled);',
    'byteBuf.writeBoolean(loadAfterUnloadEnabled);\n\n\t\tbyteBuf.writeBoolean(magazineRemoved);'
)

content = content.replace(
    'setLoadAfterUnloadEnabled(otherWeaponInstance.loadAfterUnloadEnabled);',
    'setLoadAfterUnloadEnabled(otherWeaponInstance.loadAfterUnloadEnabled);\n\n\t\tsetMagazineRemoved(otherWeaponInstance.magazineRemoved);'
)

content = content.replace(
    'public boolean isLoadAfterUnloadEnabled() {',
    'public boolean isMagazineRemoved() {\n\n        return magazineRemoved;\n\n    }\n\n\t\n\n\tpublic void setMagazineRemoved(boolean magazineRemoved) {\n\n        this.magazineRemoved = magazineRemoved;\n\n    }\n\n\n\n\tpublic boolean isLoadAfterUnloadEnabled() {'
)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

