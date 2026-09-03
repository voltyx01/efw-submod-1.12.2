using System;
using System.IO;
using System.Text.RegularExpressions;

class Program {
    static void Main() {
        string path = @"c:\Users\reizv\Videos\MWCV\src\main\java\com\paneedah\weaponlib\WeaponReloadAspect.java";
        string content = File.ReadAllText(path);

        // 1. Add hasAmmo method
        string hasAmmoMethod = @"
    private boolean hasAmmo(EntityPlayer player, PlayerWeaponInstance weaponInstance) {
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
";
        if (!content.Contains("hasAmmo(EntityPlayer player")) {
            content = content.Replace("public void reloadMainHeldItem(EntityPlayer player) {", hasAmmoMethod + "\r\n    public void reloadMainHeldItem(EntityPlayer player) {");
        }

        // 2. Change canReload logic in reloadMainHeldItem
        content = Regex.Replace(content, 
            @"boolean canReload = false;.*?if \(!canReload\) \{", 
            "boolean canReload = false;\r\n            if (AnimationModeProcessor.getInstance().isLegacyMode()) {\r\n                canReload = hasAmmo((EntityPlayer)instance.getPlayer(), instance);\r\n            } else {\r\n                canReload = hasAmmo((EntityPlayer)instance.getPlayer(), instance);\r\n            }\r\n\r\n            if (!canReload) {",
            RegexOptions.Singleline);

        // 3. Simplify getNextMagazine and getNextBestMagazineStack
        content = Regex.Replace(content,
            @"private ItemAttachment<Weapon> getNextMagazine\(PlayerWeaponInstance weaponInstance\) \{.*?private ItemStack getNextBestMagazineStack",
            "private ItemAttachment<Weapon> getNextMagazine(PlayerWeaponInstance weaponInstance) {\n        ItemAttachment<Weapon> activeMag = WeaponAttachmentAspect.getActiveAttachment(AttachmentCategory.MAGAZINE, weaponInstance);\n        if (activeMag != null && hasAmmo((EntityPlayer)weaponInstance.getPlayer(), weaponInstance)) return activeMag;\n        return null;\n    }\n\n    private ItemStack getNextBestMagazineStack",
            RegexOptions.Singleline);

        content = Regex.Replace(content,
            @"private ItemStack getNextBestMagazineStack\(PlayerWeaponInstance weaponInstance\) \{.*?private void processActualCompoundPermit",
            "private ItemStack getNextBestMagazineStack(PlayerWeaponInstance weaponInstance) {\n        ItemAttachment<Weapon> activeMag = WeaponAttachmentAspect.getActiveAttachment(AttachmentCategory.MAGAZINE, weaponInstance);\n        if (activeMag != null && hasAmmo((EntityPlayer)weaponInstance.getPlayer(), weaponInstance)) return new net.minecraft.item.ItemStack((net.minecraft.item.Item)activeMag);\n        return null;\n    }\n\n    private void processActualCompoundPermit",
            RegexOptions.Singleline);

        // 4. Rewrite processActualCompoundPermit
        content = Regex.Replace(content,
            @"private void processActualCompoundPermit\(CompoundPermit p, PlayerWeaponInstance instance\) \{.*?private void processLoadPermit",
            "private void processActualCompoundPermit(CompoundPermit p, PlayerWeaponInstance instance) {\n        processLoadPermit(new LoadPermit(p.getState()), instance);\n        p.setStatus(Status.GRANTED);\n    }\n\n    private void processLoadPermit",
            RegexOptions.Singleline);

        File.WriteAllText(path, content);
    }
}
