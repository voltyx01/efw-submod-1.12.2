import sys
import re

with open('src/main/java/com/paneedah/weaponlib/WeaponReloadAspect.java', 'r', encoding='utf-8') as f:
    content = f.read()

hasAmmoMethod = '''
    private boolean hasAmmo(net.minecraft.entity.player.EntityPlayer player, PlayerWeaponInstance weaponInstance) {
        if (player.isCreative()) return true;

        Weapon weapon = (Weapon) weaponInstance.getItem();
        ItemAttachment<Weapon> attachment = WeaponAttachmentAspect.getActiveAttachment(AttachmentCategory.MAGAZINE, weaponInstance);
        java.util.List<? extends net.minecraft.item.Item> comp = null;
        
        if (attachment instanceof ItemMagazine) {
            comp = ((ItemMagazine) attachment).getCompatibleBullets();
        } else {
            comp = weapon.getCompatibleAttachments(ItemBullet.class);
        }
        
        if (comp == null || comp.isEmpty()) {
            if (weapon.builder.ammo != null) {
                for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                    net.minecraft.item.ItemStack stack = player.inventory.getStackInSlot(i);
                    if (stack != null && stack.getItem() == weapon.builder.ammo) {
                        return true;
                    }
                }
            }
            return false;
        }

        com.paneedah.mwc.items.equipment.ItemAmmoPack ammoPackItem = com.paneedah.mwc.init.MWCItems.ammoPack;
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            net.minecraft.item.ItemStack invStack = player.inventory.getStackInSlot(i);
            if (invStack != null && invStack.getItem() == ammoPackItem) {
                ItemBullet packBullet = com.paneedah.mwc.items.equipment.ItemAmmoPack.getBullet(invStack);
                if (comp.contains(packBullet)) {
                    if (com.paneedah.mwc.items.equipment.ItemAmmoPack.getAmmo(invStack) > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
'''

if 'hasAmmo(net.minecraft.entity.player.EntityPlayer player' not in content:
    content = content.replace('public void reloadMainHeldItem(EntityPlayer player) {', hasAmmoMethod + '\n    public void reloadMainHeldItem(EntityPlayer player) {')

content = re.sub(
    r'boolean canReload = false;.*?if \(!canReload\) \{',
    'boolean canReload = false;\n            if (AnimationModeProcessor.getInstance().isLegacyMode()) {\n                canReload = hasAmmo((net.minecraft.entity.player.EntityPlayer)instance.getPlayer(), instance);\n            } else {\n                canReload = hasAmmo((net.minecraft.entity.player.EntityPlayer)instance.getPlayer(), instance);\n            }\n\n            if (!canReload) {',
    content,
    flags=re.DOTALL
)

content = re.sub(
    r'private ItemAttachment<Weapon> getNextMagazine\(PlayerWeaponInstance weaponInstance\) \{.*?private ItemStack getNextBestMagazineStack',
    'private ItemAttachment<Weapon> getNextMagazine(PlayerWeaponInstance weaponInstance) {\n        ItemAttachment<Weapon> activeMag = WeaponAttachmentAspect.getActiveAttachment(AttachmentCategory.MAGAZINE, weaponInstance);\n        if (activeMag != null && hasAmmo((net.minecraft.entity.player.EntityPlayer)weaponInstance.getPlayer(), weaponInstance)) return activeMag;\n        return null;\n    }\n\n    private ItemStack getNextBestMagazineStack',
    content,
    flags=re.DOTALL
)

content = re.sub(
    r'private ItemStack getNextBestMagazineStack\(PlayerWeaponInstance weaponInstance\) \{.*?private void processActualCompoundPermit',
    'private ItemStack getNextBestMagazineStack(PlayerWeaponInstance weaponInstance) {\n        ItemAttachment<Weapon> activeMag = WeaponAttachmentAspect.getActiveAttachment(AttachmentCategory.MAGAZINE, weaponInstance);\n        if (activeMag != null && hasAmmo((net.minecraft.entity.player.EntityPlayer)weaponInstance.getPlayer(), weaponInstance)) return new net.minecraft.item.ItemStack((net.minecraft.item.Item)activeMag);\n        return null;\n    }\n\n    private void processActualCompoundPermit',
    content,
    flags=re.DOTALL
)

content = re.sub(
    r'private void processActualCompoundPermit\(CompoundPermit p, PlayerWeaponInstance instance\) \{.*?private void processLoadPermit',
    'private void processActualCompoundPermit(CompoundPermit p, PlayerWeaponInstance instance) {\n        processLoadPermit(new LoadPermit(p.getState()), instance);\n        p.setStatus(Status.GRANTED);\n    }\n\n    private void processLoadPermit',
    content,
    flags=re.DOTALL
)

with open('src/main/java/com/paneedah/weaponlib/WeaponReloadAspect.java', 'w', encoding='utf-8') as f:
    f.write(content)
