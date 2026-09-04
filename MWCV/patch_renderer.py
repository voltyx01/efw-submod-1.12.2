import sys

path = 'src/main/java/com/paneedah/weaponlib/WeaponRenderer.java'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace(
    'if (compatibleAttachment.getAttachment().getCategory() == AttachmentCategory.MAGAZINE) {\n\n			currentMagazine = compatibleAttachment;\n\n		}',
    'if (compatibleAttachment.getAttachment().getCategory() == AttachmentCategory.MAGAZINE) {\n\n\t\t\tcurrentMagazine = compatibleAttachment;\n\n\t\t\tif (renderContext != null && renderContext.getWeaponInstance() != null && renderContext.getWeaponInstance().isMagazineRemoved()) {\n\t\t\t\tWeaponState state = renderContext.getWeaponInstance().getState();\n\t\t\t\tif (state != WeaponState.LOAD && state != WeaponState.LOAD_REQUESTED && state != WeaponState.LOAD_ITERATION && state != WeaponState.LOAD_ITERATION_COMPLETED && state != WeaponState.COMPOUND_RELOAD && state != WeaponState.COMPOUND_RELOAD_EMPTY && state != WeaponState.COMPOUND_RELOAD_UNLOAD && state != WeaponState.COMPOUND_RELOAD_FINISH && state != WeaponState.COMPOUND_RELOAD_FINISHED && state != WeaponState.UNLOAD && state != WeaponState.UNLOAD_REQUESTED && state != WeaponState.UNLOAD_PREPARING && state != WeaponState.TACTICAL_RELOAD && state != WeaponState.UNLOAD_EMPTY) {\n\t\t\t\t\treturn;\n\t\t\t\t}\n\t\t\t}\n\n\t\t}'
)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

