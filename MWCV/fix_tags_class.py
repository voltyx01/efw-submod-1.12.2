import sys

path = 'src/main/java/com/paneedah/weaponlib/Tags.java'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# remove from end
methods = """    public static void setMagazineRemoved(net.minecraft.item.ItemStack itemStack, boolean removed) {
        if (itemStack.getTagCompound() == null) {
            itemStack.setTagCompound(new net.minecraft.nbt.NBTTagCompound());
        }
        itemStack.getTagCompound().setBoolean("MagRemoved", removed);
    }

    public static boolean isMagazineRemoved(net.minecraft.item.ItemStack itemStack) {
        if (itemStack.getTagCompound() == null) {
            return false;
        }
        return itemStack.getTagCompound().getBoolean("MagRemoved");
    }"""

content = content.replace('\n\n' + methods + '\n', '')
content = content.replace('}\n', methods + '\n}\n', 1) # Wait, replace the LAST '}'!

